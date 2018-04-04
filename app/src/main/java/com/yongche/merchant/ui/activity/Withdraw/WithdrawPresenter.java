package com.yongche.merchant.ui.activity.Withdraw;

import android.app.ProgressDialog;
import android.util.Log;

import com.google.gson.Gson;
import com.yongche.merchant.bean.CodeBean;
import com.yongche.merchant.bean.withdraw.WithdrawBean;
import com.yongche.merchant.manager.GsonManger;
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

public class WithdrawPresenter extends BasePresenterImpl<WithdrawContract.View> implements WithdrawContract.Presenter {
    //  获取验证码（提现设置）
    @Override
    public void getSMSCode(String phone) throws Exception {

        HttpManager.getHttpManager().getMethod(UrlUtils.GETSMSCODE + "?phone=" + phone, new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("LoginPresenter", "onError:++++++++++++ " + e.getMessage());
                if (mView != null)
                    mView.getSMSCodeError();

            }

            @Override
            public void onNext(String s) {
                Log.i("LoginPresenter", "onNext:++++++++++++------------------ " + s);

                CodeBean o = (CodeBean) GsonManger.getGsonManger().gsonFromat(s, new CodeBean());
                if ("0000".equals(o.getCode())) {
                    if (mView != null)
                        mView.getSMSCodeSuccess(o);
                } else {
                    ToastUtil.show(mView.getContext(), o.getMsg());
                }

            }
        });

    }

    // 检查验证码是否输入成功（提现设置）
    @Override
    public void checkSMSCode(String phone, String code) {

        final ProgressDialog progressDialog = CommonUtils.showProgressDialog(mView.getContext(), "正在校验");
        String sign = MD5Util.string2MD5(phone+code+UrlUtils.KEY);
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("smsCode",code);
        map.put("sign", sign);
//       +"?phone="+phone+"&smsCode="+code+"&sign="+sign
        HttpManager.getHttpManager().postMethod(UrlUtils.CHECKSMSCODE, new Observer<String>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                progressDialog.dismiss();
                if (mView != null)
                    mView.checkSMSCodeError();
                Log.i("checkSMSCodeError", "onError:+ ++++++++++++++" + e.toString());
            }

            @Override
            public void onNext(String s) {
                progressDialog.dismiss();
                Log.i("checkSMSCodeError", "onNext:+ ++++++++++++++" + s);
//                LoginBean loginBean = (LoginBean) GsonManger.getGsonManger().gsonFromat(s, new LoginBean());

                Gson gson = new Gson();
                WithdrawBean withdrawBean =gson.fromJson(s, WithdrawBean.class);

                if ("0000".equals(withdrawBean.getCode())) {
                    if (mView != null)
                        mView.checkSMSCodeSuccess(withdrawBean);
                } else {
                    ToastUtil.show(mView.getContext(), withdrawBean.getMsg());
                }
            }
        }, map);

    }

}
