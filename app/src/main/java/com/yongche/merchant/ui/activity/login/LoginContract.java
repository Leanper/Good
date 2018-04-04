package com.yongche.merchant.ui.activity.login;

import com.yongche.merchant.bean.CodeBean;
import com.yongche.merchant.bean.login.LoginBean;
import com.yongche.merchant.mvp.BasePresenter;
import com.yongche.merchant.mvp.BaseView;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class LoginContract {
    public interface View extends BaseView {
        void getSMSCodeSuccess(CodeBean codeBean);

        void getSMSCodeError();

        void checkSMSCodeSuccess(LoginBean loginBean);

        void checkSMSCodeError();

    }

    interface Presenter extends BasePresenter<View> {
        void getSMSCode(String phone) throws Exception;

        void checkSMSCode(String phone, String code,String registration_id);

    }
}
