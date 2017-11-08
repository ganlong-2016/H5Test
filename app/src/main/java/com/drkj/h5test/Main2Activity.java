package com.drkj.h5test;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import io.dcloud.EntryProxy;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICore;
import io.dcloud.common.DHInterface.IOnCreateSplashView;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.DHInterface.IWebviewStateListener;
import io.dcloud.feature.internal.sdk.SDK;

public class Main2Activity extends AppCompatActivity {

    EntryProxy mEntryProxy = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main2);
        FrameLayout frameLayout = new FrameLayout(this);
        WebappModeListener listener = new WebappModeListener(this,frameLayout);
        mEntryProxy = EntryProxy.init(this,listener);
        mEntryProxy.onCreate(this,savedInstanceState,SDK.IntegratedMode.WEBAPP,null);
        setContentView(frameLayout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEntryProxy.onStop(this);
    }

    class WebappModeListener implements ICore.ICoreStatusListener, IOnCreateSplashView{
        Activity activity;
        View splashView = null;
        ViewGroup rootView;
        IApp app = null;
        ProgressDialog pd = null;

        public WebappModeListener(Activity activity, ViewGroup rootView){
            this.activity = activity;
            this.rootView = rootView;
        }

        @Override
        public void onCoreReady(ICore iCore) {
            SDK.initSDK(iCore);
            SDK.requestAllFeature();
        }

        @Override
        public void onCoreInitEnd(ICore iCore) {
            String appBasePath = "/apps/H5BF312F2";
            String args = "{url:'http://www.baidu.com'}";
            app = SDK.startWebApp(Main2Activity.this, appBasePath, args, new IWebviewStateListener() {
                @Override
                public Object onCallBack(int i, Object o) {
                    switch (i){
                        case IWebviewStateListener.ON_WEBVIEW_READY:
                            View view = ((IWebview) o).obtainApp().obtainWebAppRootView().obtainMainView();
                            view.setVisibility(View.INVISIBLE);

                            if(view.getParent() != null){
                                ((ViewGroup)view.getParent()).removeView(view);
                            }
                            rootView.addView(view, 0);
                            break;
                    }
                    return null;
                }
            },this);

        }

        @Override
        public boolean onCoreStop() {
            return false;
        }

        @Override
        public Object onCreateSplash(Context context) {
            return null;
        }

        @Override
        public void onCloseSplash() {
            app.obtainWebAppRootView().obtainMainView().setVisibility(View.VISIBLE);
        }
    }
}
