package com.yongche.merchant.ui.activity.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.karics.library.zxing.android.CaptureActivity;
import com.yongche.merchant.R;
import com.yongche.merchant.app.MyApplication;
import com.yongche.merchant.jpush.MyReceiver;
import com.yongche.merchant.mvp.MVPBaseActivity;
import com.yongche.merchant.ui.activity.Withdraw.WithdrawActivity;
import com.yongche.merchant.ui.activity.about.AboutActivity;
import com.yongche.merchant.ui.activity.login.LoginActivity;
import com.yongche.merchant.utils.CommonUtils;
import com.yongche.merchant.utils.ToastUtil;
import com.yongche.merchant.utils.UrlUtils;
import com.yongche.merchant.webview.FullscreenHolder;
import com.yongche.merchant.webview.IWebPageView;
import com.yongche.merchant.webview.ImageClickInterface;
import com.yongche.merchant.webview.MyWebChromeClient;
import com.yongche.merchant.webview.MyWebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 网页可以处理:
 * 点击相应控件:拨打电话、发送短信、发送邮件、上传图片、播放视频
 * 进度条、返回网页上一层、显示网页标题
 * Thanks to: https://github.com/youlookwhat/WebViewStudy
 * contact me: http://www.jianshu.com/users/e43c6e979831/latest_articles
 */
public class WebViewActivity extends MVPBaseActivity<MainWebContract.View, MainWebPresenter> implements MainWebContract.View, IWebPageView {

    //点击通知打开指定页
    String mOpen = ""; // =2，预约保养页；其他，主页
    //H5主页(未登录)
    String URL_MAIN_NO = UrlUtils.URL_H5 + "/page/merchant/index.html";

    //H5主页（已登录）
    String URL_MAIN_YES = "";
    // 历史
    String HISTORY = UrlUtils.URL_H5 + "/page/merchant/history.html";
    // 保养预约
    String RESERVATION = UrlUtils.URL_H5 + "/page/merchant/reservation.html";
    //点击链接弹出扫一扫
    public String URL_SAOYISAO = UrlUtils.URL_H5 + "/page/merchant/saoyisao.html";
    //扫完之后跳转到此链接
    String URL_SCANCODE_HTML = UrlUtils.URL_H5 + "/page/merchant/scancode.html";
    String uid;
    String sid;
    //拼接的链接
    String scanCode = "";
    //判断是否扫描到数据
    boolean isScanCode = false;

    // 进度条
    ProgressBar mProgressBar;
    WebView webView;
    // 全屏时视频加载view
    FrameLayout videoFullView;
    //    Toolbar mTitleToolBar;
    // 进度条是否加载到90%
    public boolean mProgress90;
    // 网页是否加载完成
    public boolean mPageFinish;
    // 加载视频相关
    private MyWebChromeClient mWebChromeClient;


    @BindView(R.id.tv_title)
    TextView mTV_title;

    @BindView(R.id.toolbar)
    Toolbar tbToolbar;
    @BindView(R.id.rl_main_toolbar)
    RelativeLayout mRlMainToolbar;
    //侧滑菜单按钮的layout（好像没用到）
    @BindView(R.id.fl_title_menu)
    FrameLayout nvMenu;

    //侧滑菜单按钮点击事件
    @OnClick(R.id.fl_title_menu)
    public void flTitleMenu() {
        dlLayout.openDrawer(GravityCompat.START);
    }

    //侧滑菜单布局
    @BindView(R.id.dl_layout)
    DrawerLayout dlLayout;

    @BindView(R.id.rl_nologin)
    RelativeLayout mRLnoLogin;
    //用户头像
    @BindView(R.id.iv_head)
    ImageView mIVhead;

    @OnClick(R.id.iv_head)
    public void userHeadOnClick() {
        if (MyApplication.isLoginEd()) {

        } else {
            CommonUtils.startActivity(this, LoginActivity.class);
        }
    }

    //用户名字
    @BindView(R.id.tv_name)
    TextView mTVname;

    @OnClick(R.id.tv_name)
    public void userNameOnClick() {
        if (MyApplication.isLoginEd()) {

        } else {
            CommonUtils.startActivity(this, LoginActivity.class);
        }
    }

    //公司名称
    @BindView(R.id.tv_company_name)
    TextView mTVcompany_name;
    //地址
    @BindView(R.id.tv_address)
    TextView mTVaddress;
    //联系电话
    @BindView(R.id.tv_phone)
    TextView mTVphone;

    //提现设置
    @OnClick(R.id.fl_setting)
    public void setting() {
        CommonUtils.startActivity(this, WithdrawActivity.class);
    }

    //关于
    @OnClick(R.id.fl_about)
    public void about() {
        CommonUtils.startActivity(this, AboutActivity.class);
    }

    //退出登录功能
    @OnClick(R.id.btn_logout)
    public void logout() {

        if (MyApplication.isLoginEd()) {
            CommonUtils.showInfoDialog(this, "确定退出登录吗", "提示", "取消", "确定", null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 退出登录
                    mPresenter.logOut();
                }
            });
        } else {
            ToastUtil.show(this, "您还没有登录哦...");
        }
    }

    //退出APP功能
    @OnClick(R.id.fl_exit_app)
    public void exitApp() {
        //退出程序调用这个
        this.killAll();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolBar(tbToolbar, "", false);
        mRlMainToolbar.setVisibility(View.VISIBLE);

        mPresenter.openNotification();
        initTitle();
        initWebView();
        mainLoadUrl();
    }

    private void initTitle() {
        if (UrlUtils.BASEURl.equals(UrlUtils.BASEURl_TEST)) {
            mTV_title.setText("测试");
        } else if (UrlUtils.BASEURl.equals(UrlUtils.BASEURl_BENDI)) {
            mTV_title.setText("本地");
        } else {
            mTV_title.setText("商家");
        }
        mProgressBar = (ProgressBar) findViewById(R.id.pb_progress);
        videoFullView = (FrameLayout) findViewById(R.id.video_fullView);
    }


    private void initWebView() {
        webView = (WebView) findViewById(R.id.webview_detail);
        mProgressBar.setVisibility(View.VISIBLE);
        webView.setWebContentsDebuggingEnabled(true);
        WebSettings ws = webView.getSettings();
        // 网页内容的宽度是否可大于WebView控件的宽度
        ws.setLoadWithOverviewMode(false);
        // 保存表单数据
        ws.setSaveFormData(true);
        // 是否应该支持使用其屏幕缩放控件和手势缩放
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(true);
        ws.setDisplayZoomControls(false);
        // 启动应用缓存
        ws.setAppCacheEnabled(true);
        // 设置缓存模式
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        // setDefaultZoom  api19被弃用
        // 设置此属性，可任意比例缩放。
        ws.setUseWideViewPort(true);
        // 缩放比例 1
        webView.setInitialScale(1);
        // 告诉WebView启用JavaScript执行。默认的是false。
        ws.setJavaScriptEnabled(true);
        //  页面加载好以后，再放开图片
        ws.setBlockNetworkImage(false);
        // 使用localStorage则必须打开
        ws.setDomStorageEnabled(true);
        // 排版适应屏幕
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // WebView是否支持多个窗口。
        ws.setSupportMultipleWindows(true);

        // webview从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        /** 设置字体默认缩放大小(改变网页字体大小,setTextSize  api14被弃用)*/
        ws.setTextZoom(100);

        mWebChromeClient = new MyWebChromeClient(this);
        webView.setWebChromeClient(mWebChromeClient);
        // 与js交互
        webView.addJavascriptInterface(new ImageClickInterface(this), "injectedObject");
        webView.setWebViewClient(new MyWebViewClient(this));
    }

    //webview加载
    private void mainLoadUrl() {
        boolean loginEd = MyApplication.isLoginEd();
//            //公钥加密
//        uid = "uid=" + RSAUtil.encryptByPublic(this,MyApplication.getUserId());
//        sid = "sid=" + RSAUtil.encryptByPublic(this,MyApplication.getId());
        uid = "uid=" + MyApplication.getUserId();
        sid = "sid=" + MyApplication.getId();
        URL_MAIN_YES = URL_MAIN_NO + "?" + uid + "&" + sid;

        if (getIntent() != null) {
            mOpen = getIntent().getStringExtra(MyReceiver.OPEN_NOTIFICATION);
            if (TextUtils.isEmpty(mOpen)) {
                mOpen = "没有获取到值";
            }
            LogUtils.i(TAG, "open:" + mOpen);
        }

        LogUtils.i(TAG, "getUrl:" + webView.getUrl());
        if (loginEd) {
            if (mOpen.equals("2")) {
                //如果mOpen= 2,表示通知栏跳转指定为此页
                webView.loadUrl(RESERVATION + "?" + uid + "&" + sid);
            } else {
                webView.loadUrl(URL_MAIN_YES);
            }
        } else {
            webView.loadUrl(URL_MAIN_NO);
        }
    }

    @Override
    public void hindProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void startProgress() {
        startProgress90();
    }

    @Override
    public void showWebView() {
        webView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hindWebView() {
        webView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void fullViewAddView(View view) {
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        videoFullView = new FullscreenHolder(WebViewActivity.this);
        videoFullView.addView(view);
        decor.addView(videoFullView);
    }

    @Override
    public void showVideoFullView() {
        videoFullView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hindVideoFullView() {
        videoFullView.setVisibility(View.GONE);
    }

    @Override
    public void progressChanged(int newProgress) {
        if (mProgress90) {
            int progress = newProgress * 100;
            if (progress > 900) {
                mProgressBar.setProgress(progress);
                if (progress == 1000) {
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void addImageClickListener() {
        // 这段js函数的功能就是，遍历所有的img节点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        // 如要点击一张图片在弹出的页面查看所有的图片集合,则获取的值应该是个图片数组
        webView.loadUrl("javascript:(function(){" +

                "var objs = document.getElementsByTagName(\"img\");" +
                "for(var i=0;i<objs.length;i++)" +
                "{" +
                //  "objs[i].onclick=function(){alert(this.getAttribute(\"has_link\"));}" +
                "objs[i].onclick=function(){window.injectedObject.imageClick(this.getAttribute(\"src\"),this.getAttribute(\"has_link\"));}" +
                "}" +
                "})()");

        // 遍历所有的a节点,将节点里的属性传递过去(属性自定义,用于页面跳转)
        webView.loadUrl("javascript:(function(){" +
                "var objs =document.getElementsByTagName(\"a\");" +
                "for(var i=0;i<objs.length;i++)" +
                "{" +
                "objs[i].onclick=function(){" +
                "window.injectedObject.textClick(this.getAttribute(\"type\"),this.getAttribute(\"item_pk\"));}" +
                "}" +
                "})()");
    }

    /**
     * 进度条 假装加载到90%
     */
    public void startProgress90() {
        for (int i = 0; i < 900; i++) {
            final int progress = i + 1;
            mProgressBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.setProgress(progress);
                    if (progress == 900) {
                        mProgress90 = true;
                        if (mPageFinish) {
                            startProgress90to100();
                        }
                    }
                }
            }, (i + 1) * 2);
        }
    }

    /**
     * 进度条 加载到100%
     */
    public void startProgress90to100() {
        for (int i = 900; i <= 1000; i++) {
            final int progress = i + 1;
            mProgressBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.setProgress(progress);
                    if (progress == 1000) {
                        mProgressBar.setVisibility(View.GONE);
                    }
                }
            }, (i + 1) * 2);
        }
    }


    public FrameLayout getVideoFullView() {
        return videoFullView;
    }

    /**
     * 回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //上传图片之后的回调
        if (requestCode == MyWebChromeClient.FILECHOOSER_RESULTCODE) {
            mWebChromeClient.mUploadMessage(intent, resultCode);
        } else if (requestCode == MyWebChromeClient.FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            mWebChromeClient.mUploadMessageForAndroid5(intent, resultCode);
        }

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (intent != null) {

                String content = intent.getStringExtra(DECODED_CONTENT_KEY);
                scanCode = URL_SCANCODE_HTML + "?code=" + content;
                isScanCode = true;
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.checkLogin();
        webView.onResume();
        // 支付宝网页版在打开文章详情之后,无法点击按钮下一步
        webView.resumeTimers();
        // 设置为横屏
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoFullView.removeAllViews();
        if (webView != null) {
            ViewGroup parent = (ViewGroup) webView.getParent();
            if (parent != null) {
                parent.removeView(webView);
            }
            webView.removeAllViews();
            webView.loadUrl("about:blank");
            webView.stopLoading();
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.destroy();
            webView = null;
        }
    }

    /**
     * 按返回键不退出应用。
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (dlLayout.isDrawerOpen(GravityCompat.START)) {
                dlLayout.closeDrawer(GravityCompat.START);
            } else {
                if (URL_MAIN_YES.equals(webView.getUrl()) || URL_MAIN_NO.equals(webView.getUrl())) {
                    // 不退出程序，进入后台
                    moveTaskToBack(true);
                } else {
                    //返回主页
                    webView.loadUrl(URL_MAIN_YES);
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            onUserInteraction();
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    String TAG = "WEBVIEW";

    //登陆状态
    @Override
    public void isLogin() {

        LogUtils.i(TAG,"getURL: "+webView.getUrl());
        MyApplication.setIsLoginEd(true);
        mRLnoLogin.setVisibility(View.GONE);
//        //公钥加密
//        uid = "uid=" + RSAUtil.encryptByPublic(this,MyApplication.getUserId());
//        sid = "sid=" + RSAUtil.encryptByPublic(this,MyApplication.getId());

        uid = "uid=" + MyApplication.getUserId();
        sid = "sid=" + MyApplication.getId();
        URL_MAIN_YES = URL_MAIN_NO + "?" + uid + "&" + sid;

        //判断生命周期进入onResume执行后跳入对应页面
        if (isScanCode) {
            //如果扫描成功isScanCode = true，跳入扫描后的页面
            scanCode = scanCode + "&" + uid + "&" + sid;
            LogUtils.i(TAG,"scanCode:"+scanCode);
            webView.loadUrl(scanCode);
            scanCode = "";
        } else if (URL_MAIN_NO.equals(webView.getUrl())) {
            webView.loadUrl(URL_MAIN_YES);
        }
        //扫描结束后，将isScanCode = false
        isScanCode = false;

        mTVname.setText(MyApplication.getStoreOwn());
        mTVcompany_name.setText(MyApplication.getStore());
        mTVaddress.setText(MyApplication.getStoreAddr());
        mTVphone.setText(MyApplication.getPhone());
        if (!TextUtils.isEmpty(MyApplication.getIcon())) {
            Glide.with(this).load(MyApplication.getIcon()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.mipmap.micon).into(mIVhead);
        }
    }

    // 未登录状态
    @Override
    public void noLogin() {
        if (!URL_MAIN_NO.equals(webView.getUrl())) {
            webView.loadUrl(URL_MAIN_NO);
        }
        MyApplication.setIsLoginEd(false);
        mRLnoLogin.setVisibility(View.VISIBLE);
        mTVname.setText("登录");
        mIVhead.setImageResource(R.mipmap.micon);
    }

    //退出登录成功
    @Override
    public void isExit() {
        MyApplication.setIsLoginEd(false);
        mRLnoLogin.setVisibility(View.VISIBLE);
        mTVname.setText("登录");
        mTVcompany_name.setText(MyApplication.getStore());
        mTVaddress.setText(MyApplication.getStoreAddr());
        mTVphone.setText(MyApplication.getPhone());
        if (!TextUtils.isEmpty(MyApplication.getIcon())) {
            Glide.with(this).load(MyApplication.getIcon()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.mipmap.micon).into(mIVhead);
        }
    }

    private static final int REQUEST_CODE_SCAN = 0x0000;
    private static final String DECODED_CONTENT_KEY = "codedContent";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意，可以去放肆了。
                    Intent intent = new Intent(WebViewActivity.this,
                            CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_SCAN);
                } else {
                    // 权限被用户拒绝了，洗洗睡吧。
                    CommonUtils.showInfoDialog(this, "请前往设置中开启权限", "提示", "取消", "确定", null, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 前往设置
                            mPresenter.getAppDetailSettingIntent(WebViewActivity.this);
                        }
                    });
                }
                return;
            }
        }
    }
}
