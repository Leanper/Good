package com.yongche.merchant.ui.activity.main;

import com.yongche.merchant.mvp.BasePresenter;
import com.yongche.merchant.mvp.BaseView;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class MainWebContract {
    public interface View extends BaseView {
        //已登陆
        void isLogin();

        //未登陆
        void noLogin();

        //退出登录
        void isExit();
//            void isOwner();
//
//            void noOwner();

    }

    interface Presenter extends BasePresenter<View> {
        void checkLogin();

        //退出登录
        void logOut();

        //提示开启通知功能
        void openNotification();
//        void checkOwner();
    }
}
