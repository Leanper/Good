package com.yongche.merchant.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.blankj.utilcode.utils.LogUtils;
import com.umeng.analytics.MobclickAgent;
import com.yongche.merchant.R;
import com.yongche.merchant.app.MyApplication;
import com.yongche.merchant.ui.activity.base.BaseActivity;
import com.yongche.merchant.ui.activity.login.LoginActivity;
import com.yongche.merchant.ui.activity.main.WebViewActivity;
import com.yongche.merchant.utils.CommonUtils;
import com.yongche.merchant.webview.WebViewActivity2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class SplashActivity extends BaseActivity {
    String url =  "http://m.1yongche.com/page/merchant/history.html?uid=0&sid=1";
    //ButterKnife是一个专注于Android系统的View注入框架,可以减少大量的findViewById以及setOnClickListener代码，可视化一键生成。
    @BindView(R.id.iv_pic)
    ImageView ivPic;
    private Unbinder bind;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        //取消状态栏(已在style文件中设置了)
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        LogUtils.Builder builder = new LogUtils.Builder();
        //logSwitch为false关闭日志
        builder.setLogSwitch(true).create();

        /** 设置是否对日志信息进行加密, 默认false(不加密). */
//        AnalyticsConfig.enableEncrypt(false);//6.0.0版本以前
        MobclickAgent.enableEncrypt(true);//6.0.0版本及以后

        //绑定activity
        bind = ButterKnife.bind(this);
        ivPic.setImageResource(R.mipmap.yiyongche);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toMainActivity();
            }
        }, 2000);

//        toWebViewActivity();
//        toTestActivity();
    }

    private void toMainActivity() {
        if(MyApplication.isLoginEd()){
            CommonUtils.startActivity(this, WebViewActivity.class);
        }else{
            CommonUtils.startActivity(this,LoginActivity.class);
        }
        finish();
    }

    private void toWebViewActivity() {
        WebViewActivity2.loadUrl(this,url,"TEST");
        finish();
    }
    private void toTestActivity() {
        List<CommonUtils.Builder> myList = new ArrayList<CommonUtils.Builder>();
        myList.add(new CommonUtils.Builder().setKey("key1").setValue("name1"));
        myList.add(new CommonUtils.Builder().setKey("key2").setValue("name2"));
        CommonUtils.startActivity(this,TestActivity.class,myList);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind!=null){
            bind.unbind();
        }
    }
}
