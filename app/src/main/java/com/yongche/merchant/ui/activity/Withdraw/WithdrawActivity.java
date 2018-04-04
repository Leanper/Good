package com.yongche.merchant.ui.activity.Withdraw;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.yongche.merchant.R;
import com.yongche.merchant.app.MyApplication;
import com.yongche.merchant.bean.CodeBean;
import com.yongche.merchant.bean.withdraw.WithdrawBean;
import com.yongche.merchant.mvp.MVPBaseActivity;
import com.yongche.merchant.utils.CommonUtils;
import com.yongche.merchant.utils.TextViewUtils;
import com.yongche.merchant.utils.ToastUtil;
import com.yongche.merchant.view.TimeCount;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class WithdrawActivity extends MVPBaseActivity<WithdrawContract.View, WithdrawPresenter> implements WithdrawContract.View, View.OnClickListener {

    private EditText et_code, et_phone;
    private Button bt_check, bt_getCode;
    private TimeCount timeCount;
    private TextWatcher textWatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //s:变化前的所有字符； start:字符开始的位置； count:变化前的总字节数；after:变化后的字节数

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //S：变化后的所有字符；start：字符起始的位置；before: 变化之前的总字节数；count:变化后的字节数
            if (TextViewUtils.getText(et_code).length() >= 4) {
                //设置按钮可点击
                bt_check.setClickable(true);
                //设置按钮为正常状态
                bt_check.setPressed(true);
            } else {
                bt_check.setClickable(false);
                //设置按钮为按下状态
                bt_check.setPressed(false);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
            //s:变化后的所有字符

        }
    };

    @BindView(R.id.toolbar)
    Toolbar tbToolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_withdraw;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolBar(tbToolbar, "提现设置", true);
        //找控件
        initView();
    }

    private void initView() {
        timeCount = new TimeCount(60000, 1000);
        //验证码
        et_code = (EditText) findViewById(R.id.et_code);

        et_code.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        //手机号码
        et_phone = (EditText) findViewById(R.id.et_phone);
        //获取手机号
        et_phone.setText(MyApplication.getPhone().toString());
        //验证验证码
        bt_check = (Button) findViewById(R.id.bt_check);
        //获取验证码
        bt_getCode = (Button) findViewById(R.id.bt_getCode);

        bt_check.setOnClickListener(this);
        bt_getCode.setOnClickListener(this);
        bt_getCode.setClickable(false);
        bt_check.setClickable(false);
        timeCount.setButton(bt_getCode);
        et_code.addTextChangedListener(textWatch);
    }

    @Override
    public void getSMSCodeSuccess(CodeBean codeBean) {
        ToastUtil.show(this, "获取验证码成功");

    }

    @Override
    public void getSMSCodeError() {
        timeCount.cancel();
        timeCount.onFinish();
        ToastUtil.show(this, "获取验证码失败");
        //设置按钮可点击
        bt_getCode.setClickable(true);
        //设置按钮为正常状态
        bt_getCode.setPressed(true);
    }

    @Override
    public void checkSMSCodeSuccess(WithdrawBean withdrawBean) {
        ToastUtil.show(this, "验证码校验成功");
        timeCount.cancel();
        timeCount.onFinish();
        CommonUtils.finishActivity(this);
        CommonUtils.startActivity(this,WithdrawSetPwdActivity.class);
    }

    @Override
    public void checkSMSCodeError() {
        ToastUtil.show(this, "检查验证码失败");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_check:
                if (!TextUtils.isEmpty(TextViewUtils.getText(et_phone)) && !TextUtils.isEmpty(TextViewUtils.getText(et_code))) {

                    mPresenter.checkSMSCode(TextViewUtils.getText(et_phone), TextViewUtils.getText(et_code));
                } else {
                    CommonUtils.showInfoDialog(this, "手机号码或验证码为空");
                }

                break;
            case R.id.bt_getCode:

                if (!TextUtils.isEmpty(TextViewUtils.getText(et_phone))) {
                    if (CommonUtils.isUserNumber(TextViewUtils.getText(et_phone))) {
                        try {
                            mPresenter.getSMSCode(TextViewUtils.getText(et_phone));

                            timeCount.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonUtils.showInfoDialog(this, "请输入正确的手机号码");
                        //ToastUtil.show(this, "请输入正确的手机号码");
                    }
                } else {
                    CommonUtils.showInfoDialog(this, "请输入您的手机号码");
                    // ToastUtil.show(this, "请输入您的手机号码");
                }
                break;
        }
    }
}
