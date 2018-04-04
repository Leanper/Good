package com.yongche.merchant.ui.activity.Withdraw;

import com.yongche.merchant.bean.withdraw.WithdrawBean;
import com.yongche.merchant.mvp.BasePresenter;
import com.yongche.merchant.mvp.BaseView;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class WithdrawSetPwdContract {
    public interface View extends BaseView {

        void setPwdSuccess(WithdrawBean withdrawBean);

        void setPwdCodeError();

    }

    interface Presenter extends BasePresenter<View> {

        void storeSetPass(String sid, String payPass,String bindPhone);
    }
}
