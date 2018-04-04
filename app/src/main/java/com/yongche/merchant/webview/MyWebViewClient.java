package com.yongche.merchant.webview;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.blankj.utilcode.utils.LogUtils;
import com.karics.library.zxing.android.CaptureActivity;
import com.yongche.merchant.app.MyApplication;
import com.yongche.merchant.ui.activity.login.LoginActivity;
import com.yongche.merchant.ui.activity.main.WebViewActivity;
import com.yongche.merchant.utils.CommonUtils;


/**
 * Created by quantan.liu on 2017/3/29.
 * 监听网页链接:
 * - 优酷视频直接跳到自带浏览器
 * - 根据标识:打电话、发短信、发邮件
 * - 进度条的显示
 * - 添加javascript监听
 */
public class MyWebViewClient extends WebViewClient {
    String TAG = "WEBVIEW";
    private static final int REQUEST_CODE_SCAN = 0x0000;

    private IWebPageView mIWebPageView;
    private WebViewActivity mActivity;

    public MyWebViewClient(IWebPageView mIWebPageView) {
        this.mIWebPageView = mIWebPageView;
        mActivity = (WebViewActivity) mIWebPageView;

    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        LogUtils.i(TAG,"URL: "+url);
//         // 电话、短信、邮箱
//        if (url.startsWith(WebView.SCHEME_TEL) || url.startsWith("sms:") || url.startsWith(WebView.SCHEME_MAILTO)) {
//            try {
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(url));
//                mActivity.startActivity(intent);
//            } catch (ActivityNotFoundException ignored) {
//            }
//            return true;
//        }
        if (MyApplication.isLoginEd()) {
            // ------  对alipays:相关的scheme处理 -------
            if (url.startsWith("tel:") || url.startsWith("tel")) {
                try {
                    mActivity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                } catch (Exception e) {
//                            new AlertDialog.Builder(context)
////                                    .setMessage("未检测到支付宝客户端，请安装后重试。")
//                                    .setPositiveButton("立即安装", new DialogInterface.OnClickListener() {
//
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            Uri Url = Uri.parse(URL_MAIN_H5);
//                                            context.startActivity(new Intent("android.intent.action.VIEW", Url));
//                                        }
//                                    }).setNegativeButton("取消", null).show();
                }
                return true;
            }

            //扫描二维码
            if (url.equals(mActivity.URL_SAOYISAO)||url.startsWith("saoyisao")) {
//                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
//                                != PackageManager.PERMISSION_GRANTED) {
//                            // 没有权限，申请权限。
//                        }else{
//                            // 有权限了，去放肆吧。
//                        }
                getCameraPermission();
                return true;
            }
            // ------- 处理结束 -------
            if (!(url.startsWith("http") || url.startsWith("https"))) {
                return true;
            }
        } else {
            // 直接跳到登录
            CommonUtils.startActivity(mActivity, LoginActivity.class);
            return true;
        }
        mIWebPageView.startProgress();
        view.loadUrl(url);
        return false;
    }


    @Override
    public void onPageFinished(WebView view, String url) {
        if (mActivity.mProgress90) {
            mIWebPageView.hindProgressBar();
        } else {
            mActivity.mPageFinish = true;
        }
        if (!CheckNetwork.isNetworkConnected(mActivity)) {
            mIWebPageView.hindProgressBar();
        }
        // html加载完成之后，添加监听图片的点击js函数
//        mIWebPageView.addImageClickListener();
        super.onPageFinished(view, url);
    }

//    @Override
//    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//        super.onReceivedError(view, errorCode, description, failingUrl);
//        LogUtils.i("WEBVIEW", "进来了");
//    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        LogUtils.i("WEBVIEW", "进来了1");
        view.loadUrl("about:blank");
    }


    /**
     * 获取相机权限（6.0以上系统需要代码里获取权限）
     */
    public void getCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mActivity.requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
//             Manifest.permission.WRITE_EXTERNAL_STORAGE  获取图片，文件，权限
        } else {
            Intent intent = new Intent(mActivity,
                    CaptureActivity.class);
            mActivity.startActivityForResult(intent, REQUEST_CODE_SCAN);
        }
    }
}
