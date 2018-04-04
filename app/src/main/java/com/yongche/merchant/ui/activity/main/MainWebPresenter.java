package com.yongche.merchant.ui.activity.main;

import android.app.AppOpsManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;

import com.blankj.utilcode.utils.LogUtils;
import com.umeng.analytics.MobclickAgent;
import com.yongche.merchant.app.MyApplication;
import com.yongche.merchant.bean.CodeMsg;
import com.yongche.merchant.manager.GsonManger;
import com.yongche.merchant.manager.HttpManager;
import com.yongche.merchant.mvp.BasePresenterImpl;
import com.yongche.merchant.utils.CommonUtils;
import com.yongche.merchant.utils.ToastUtil;
import com.yongche.merchant.utils.UrlUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import rx.Observer;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class MainWebPresenter extends BasePresenterImpl<MainWebContract.View> implements MainWebContract.Presenter {

    @Override
    public void checkLogin() {
        boolean loginEd = MyApplication.isLoginEd();

        if (loginEd) {
            mView.isLogin();
        } else {
            mView.noLogin();
        }
    }

    @Override
    public void logOut() {

        final ProgressDialog p = CommonUtils.showProgressDialog(mView.getContext(), "正在退出...");
        Map map = new HashMap();
        map.put("sid", MyApplication.getId());
        HttpManager.getHttpManager().postMethod(UrlUtils.EXIT, new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                p.dismiss();
                ToastUtil.show(mView.getContext(), "请稍后重试哦");
            }

            @Override
            public void onNext(String s) {
                p.dismiss();
                CodeMsg codeMsg = (CodeMsg) GsonManger.getGsonManger().gsonFromat(s, new CodeMsg());
                if ("0000".equals(codeMsg.getCode())) {
                    ToastUtil.show(mView.getContext(), "退出成功");
                    MyApplication.clearLogin();
                    //退出友盟账号统计
                    MobclickAgent.onProfileSignOff();
                    mView.isExit();
                } else {
                    if (!"0015".equals(codeMsg.getCode())) {
                        ToastUtil.show(mView.getContext(), codeMsg.getMsg());
                    }
                }
            }
        }, map);
    }

    /**
     * 提示开启通知功能
     */
    @Override
    public void openNotification() {
        if (!isNotificationEnabled(mView.getContext())) {
            LogUtils.i("Noti", "通知栏未开启");
            // 权限被用户拒绝了，洗洗睡吧。
            CommonUtils.showInfoDialog(mView.getContext(), "通知权限未开启，请前往设置中开启权限", "提示", "取消", "前往开启", null,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 前往设置
                            getAppDetailSettingIntent(mView.getContext());
                        }
                    });
        } else {
            LogUtils.i("Noti", "通知栏开启");
        }
    }

    /**
     * 跳转到权限设置界面
     */
    public void getAppDetailSettingIntent(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(intent);
    }

    /**
     * 检验是否开启通知功能
     *
     * @param context
     * @return
     */
    private boolean isNotificationEnabled(Context context) {

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
     /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
}
