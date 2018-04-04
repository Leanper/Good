package com.yongche.merchant.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yongche.merchant.R;
import com.yongche.merchant.ui.activity.base.BaseActivity;
import com.yongche.merchant.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TestActivity extends BaseActivity {

    //ButterKnife是一个专注于Android系统的View注入框架,可以减少大量的findViewById以及setOnClickListener代码，可视化一键生成。
    @BindView(R.id.tv_test1)
    TextView mTvTest1;
    @BindView(R.id.tv_test2)
    TextView mTvTest2;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.edit_text)
    EditText edit;
    @BindView(R.id.butt1)
    Button butt1;
    @BindView(R.id.butt2)
    Button butt2;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //绑定activity
        ButterKnife.bind(this);
        setToolBar(mToolbar, "测试");
        mTvTest1.setText(CommonUtils.getIntentData(this, "key1"));
        mTvTest2.setText(CommonUtils.getIntentData(this, "key2"));
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        butt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //绑定软键盘到EditText
                edit.setFocusable(true);
                edit.setFocusableInTouchMode(true);
                edit.requestFocus();
                InputMethodManager inputManager = (InputMethodManager) edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(edit, 0);
            }
        });
        butt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                去除软键盘显示
                edit.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
            }
        });

    }
}
