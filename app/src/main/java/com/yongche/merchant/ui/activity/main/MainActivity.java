package com.yongche.merchant.ui.activity.main;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.yongche.merchant.R;
import com.yongche.merchant.ui.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.fl_title_menu)
    FrameLayout nvMenu;

    @BindView(R.id.dl_layout)
    DrawerLayout dlLayout;

    @BindView(R.id.toolbar)
    Toolbar tbToolbar;


    @OnClick(R.id.fl_title_menu)
    public void flTitleMenu() {
        dlLayout.openDrawer(GravityCompat.START);
    }

    @OnClick(R.id.fl_exit_app)
    public void exitApp() {
        this.killAll();
    }

//    @OnClick(R.id.fl_feedback)
//    public void feedback() {
//        Toast.makeText(this, "意见反馈暂时未开通", Toast.LENGTH_SHORT).show();
//    }
//
//    @OnClick(R.id.fl_setting)
//    public void setting() {
//        Toast.makeText(this, "设置暂时还没有开发哦", Toast.LENGTH_SHORT).show();
//    }
//
//    @OnClick(R.id.fl_theme_color)
//    public void themeColor() {
//        Toast.makeText(this, "个性换肤暂时还没有开发", Toast.LENGTH_SHORT).show();
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolBar(tbToolbar, "", false);
    }

    @Override
    public void onBackPressed() {
        if (dlLayout.isDrawerOpen(GravityCompat.START)) {
            dlLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 按返回键不退出应用。
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (dlLayout.isDrawerOpen(GravityCompat.START)) {
                dlLayout.closeDrawer(GravityCompat.START);
            } else {
                // 不退出程序，进入后台
                moveTaskToBack(true);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            onUserInteraction();
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

}
