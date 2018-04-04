package com.yongche.merchant.ui.activity.Withdraw;

import android.app.ProgressDialog;
import android.util.Log;

import com.google.gson.Gson;
import com.yongche.merchant.bean.withdraw.WithdrawBean;
import com.yongche.merchant.manager.HttpManager;
import com.yongche.merchant.mvp.BasePresenterImpl;
import com.yongche.merchant.utils.CommonUtils;
import com.yongche.merchant.utils.MD5Util;
import com.yongche.merchant.utils.ToastUtil;
import com.yongche.merchant.utils.UrlUtils;

import java.util.HashMap;
import java.util.Map;

import rx.Observer;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class WithdrawSetPwdPresenter extends BasePresenterImpl<WithdrawSetPwdContract.View> implements WithdrawSetPwdContract.Presenter {

    @Override
    public void storeSetPass(String sid, String payPass, String bindPhone) {


        final ProgressDialog progressDialog = CommonUtils.showProgressDialog(mView.getContext(), "正在设置密码");
        String sign = MD5Util.string2MD5(sid+payPass+bindPhone+UrlUtils.KEY);
        Map<String, String> map = new HashMap<>();
        map.put("sid", sid);
        map.put("bindPhone",bindPhone);
        map.put("payPass",payPass);
        map.put("sign", sign);
        HttpManager.getHttpManager().postMethod(UrlUtils.WITHDRAW_SET_PWD, new Observer<String>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                progressDialog.dismiss();
                if (mView != null)
                    mView.setPwdCodeError();
                Log.i("setPwdCodeError", "onError:+ ++++++++++++++" + e.toString());
            }

            @Override
            public void onNext(String s) {
                progressDialog.dismiss();
                Log.i("setPwdCodeError", "onNext:+ ++++++++++++++" + s);
//                LoginBean loginBean = (LoginBean) GsonManger.getGsonManger().gsonFromat(s, new LoginBean());

                Gson gson = new Gson();
                WithdrawBean withdrawBean =gson.fromJson(s, WithdrawBean.class);

                if ("0000".equals(withdrawBean.getCode())) {
                    if (mView != null)
                        mView.setPwdSuccess(withdrawBean);
                } else {
                    ToastUtil.show(mView.getContext(), withdrawBean.getMsg());
                }
            }
        }, map);
    }
}
