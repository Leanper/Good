package com.yongche.merchant.ui.activity.Withdraw;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yongche.merchant.R;
import com.yongche.merchant.app.MyApplication;
import com.yongche.merchant.bean.withdraw.WithdrawBean;
import com.yongche.merchant.mvp.MVPBaseActivity;
import com.yongche.merchant.utils.CommonUtils;
import com.yongche.merchant.utils.TextViewUtils;
import com.yongche.merchant.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class WithdrawSetPwdActivity extends MVPBaseActivity<WithdrawSetPwdContract.View, WithdrawSetPwdPresenter> implements WithdrawSetPwdContract.View, View.OnClickListener {
    private Button bt_check;
    private TextWatcher textWatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //s:变化前的所有字符； start:字符开始的位置； count:变化前的总字节数；after:变化后的字节数

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //S：变化后的所有字符；start：字符起始的位置；before: 变化之前的总字节数；count:变化后的字节数
            if (TextViewUtils.getText(mETpwdFrist).length() == 6) {
                //设置按钮可点击
                bt_check.setClickable(true);
                //设置按钮为正常状态
                bt_check.setPressed(true);
                if("".equals(TextViewUtils.getText(mETpwdSecond))){
                    mTVhint.setText("请重复密码");
                }else if (TextViewUtils.getText(mETpwdFrist).equals(TextViewUtils.getText(mETpwdSecond))) {
                    mTVhint.setText("");
                    //设置按钮可点击
                    bt_check.setClickable(true);
                    //设置按钮为正常状态
                    bt_check.setPressed(true);
                } else {
                    mTVhint.setText("两次密码不一致");
                    bt_check.setClickable(false);
                    //设置按钮为按下状态
                    bt_check.setPressed(false);
                }
            } else {
                mTVhint.setText("请输入6位数字密码");
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

    @BindView(R.id.et_pwd_frist)
    EditText mETpwdFrist;
    @BindView(R.id.et_pwd_second)
    EditText mETpwdSecond;
    @BindView(R.id.tv_hint)
    TextView mTVhint;

    @BindView(R.id.toolbar)
    Toolbar tbToolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_withdraw_setpwd;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolBar(tbToolbar, "提现密码设置", true);
        //找控件
        initView();
    }

    private void initView() {
        //提交密码
        bt_check = (Button) findViewById(R.id.bt_check);

        bt_check.setOnClickListener(this);
        bt_check.setClickable(false);
        mETpwdFrist.addTextChangedListener(textWatch);
        mETpwdSecond.addTextChangedListener(textWatch);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_check:
                if (!TextUtils.isEmpty(TextViewUtils.getText(mETpwdSecond)) && !TextUtils.isEmpty(TextViewUtils.getText(mETpwdFrist))) {

                    mPresenter.storeSetPass(MyApplication.getId(), TextViewUtils.getText(mETpwdFrist),MyApplication.getPhone());
                } else {
                    CommonUtils.showInfoDialog(this, "密码不能为空");
                }
                break;
        }
    }


    @Override
    public void setPwdSuccess(WithdrawBean withdrawBean) {
        ToastUtil.show(this, "提现设置成功");
        CommonUtils.finishActivity(this);

    }

    @Override
    public void setPwdCodeError() {
        ToastUtil.show(this, "提现设置失败");

    }
}
