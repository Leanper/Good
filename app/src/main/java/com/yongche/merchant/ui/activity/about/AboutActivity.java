package com.yongche.merchant.ui.activity.about;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.yongche.merchant.R;
import com.yongche.merchant.mvp.MVPBaseActivity;
import com.yongche.merchant.utils.UrlUtils;
import com.yongche.merchant.webview.WebViewActivity2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class AboutActivity extends MVPBaseActivity<AboutContract.View, AboutPresenter> implements AboutContract.View {

    @BindView(R.id.toolbar)
    Toolbar tbToolbar;
    @OnClick(R.id.tv_1yongche)
    public void onClick1yongche(){
        WebViewActivity2.loadUrl(this, UrlUtils.URL_H5,"官方网站");
    }
    @OnClick(R.id.rl_feedback)
    public void onClickFeedback(){
        Toast.makeText(this, "意见反馈暂时未开通", Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.rl_about_us)
    public void onClickAboutUs(){
        WebViewActivity2.loadUrl(this, UrlUtils.URL_H5+"/about.html","关于我们");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolBar(tbToolbar, "关于", true);
    }
}
