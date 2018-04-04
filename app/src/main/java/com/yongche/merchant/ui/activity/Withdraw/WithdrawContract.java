package com.yongche.merchant.ui.activity.Withdraw;

import com.yongche.merchant.bean.CodeBean;
import com.yongche.merchant.bean.withdraw.WithdrawBean;
import com.yongche.merchant.mvp.BasePresenter;
import com.yongche.merchant.mvp.BaseView;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class WithdrawContract {
    public interface View extends BaseView {
        void getSMSCodeSuccess(CodeBean codeBean);

        void getSMSCodeError();

        void checkSMSCodeSuccess(WithdrawBean withdrawBean);

        void checkSMSCodeError();

    }

    interface Presenter extends BasePresenter<View> {
        void getSMSCode(String phone) throws Exception;

        void checkSMSCode(String phone, String code);
    }
}
