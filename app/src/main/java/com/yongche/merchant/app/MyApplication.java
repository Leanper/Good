package com.yongche.merchant.app;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Process;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.yongche.merchant.R;
import com.yongche.merchant.utils.CommonUtils;
import com.yongche.merchant.utils.SharedPreferencesUtils;

import java.util.ArrayList;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;


/**
 * @author :   xueyanan
 * @date :   2017/1/10
 */

public class MyApplication extends Application {
    private static boolean isOwner = false;
    private static final String IS_LOGIN_ED = "isLoginEd";
    private static final String IS_NOTIFICATION = "isNotification";
    private static final String IS_OWNER = "isOwner";
    private static ArrayList<Activity> activityList = new ArrayList();
    private static Context mContext = null;
    private static boolean isLoginEd = false;
    public static String location = "北京";

    public static int getMainThreadId() {
        return mainThreadId;
    }

    public static Handler getHandler() {
        return handler;
    }

    /**
     * 查询是否登陆
     *
     * @return
     */
    public static boolean isLoginEd() {
        return SharedPreferencesUtils.getBoolean(IS_LOGIN_ED, false);
    }

    /**
     * 设置是否登陆
     *
     * @param isLoginEd
     */
    public static void setIsLoginEd(boolean isLoginEd) {
        MyApplication.isLoginEd = isLoginEd;
        SharedPreferencesUtils.saveBoolean(IS_LOGIN_ED, isLoginEd);
    }

    /**
     * 查询是否勾选不再提示（开始通知栏权限时）
     *
     * @return
     */
    public static boolean isNotification() {
        return SharedPreferencesUtils.getBoolean(IS_NOTIFICATION, false);
    }

    /**
     * 设置是否勾选不再提示（开始通知栏权限时）
     *
     * @param isLoginEd
     */
    public static void setIsNotification(boolean isLoginEd) {
        MyApplication.isLoginEd = isLoginEd;
        SharedPreferencesUtils.saveBoolean(IS_NOTIFICATION, isLoginEd);
    }

    /**
     * 设置是否是车主
     */

    public static void setIsOwner(boolean isOwner) {
        MyApplication.isOwner = isOwner;
        SharedPreferencesUtils.saveBoolean(IS_OWNER, isOwner);
    }


    /**
     * 保存用户类型
     */
    public static void saveUserStatus(String token) {
        SharedPreferencesUtils.saveString("UserStatus", token);
    }
    /*
    获取 用户类型
     */

    public static String getUserStatus() {
        return SharedPreferencesUtils.getString("UserStatus", null);
    }

    /**
     * 保存token
     */
    public static void saveToken(String token) {
        SharedPreferencesUtils.saveString("TOKEN", token);
    }
    /*
    获取token
     */

    public static String getToken() {
        return SharedPreferencesUtils.getString("TOKEN", null);
    }

    /**
     * 保存头像
     */
    public static void saveIcon(String token) {
        SharedPreferencesUtils.saveString("ICON", token);
    }
    /*
    获取token
     */

    public static String getIcon() {
        return SharedPreferencesUtils.getString("ICON", null);
    }

    /**
     * 保存carID
     */
    public static void saveUpCarId(String token) {
        SharedPreferencesUtils.saveString("CarId", token);
    }
    /*
    获取carID
     */

    public static String getAndroidId() {
        return SharedPreferencesUtils.getString("ANDROID_ID", null);
    }


    /**
     * 保存carID
     */
    public static void saveAndroidId() {
        SharedPreferencesUtils.saveString("ANDROID_ID", CommonUtils.getIMEI(MyApplication.getContext()));
    }
    /*
    获取carID
     */

    public static String getUpCarId() {
        return SharedPreferencesUtils.getString("CarId", null);
    }

    /**
     * 保存UsrId
     */
    public static void saveUserId(String userId) {
        SharedPreferencesUtils.saveString("USER_ID", userId);
    }

    /**
     * 获取UsrId
     */
    public static String getUserId() {
        return SharedPreferencesUtils.getString("USER_ID", "");
    }

    /**
     * 保存Id
     */
    public static void saveId(String id) {
        SharedPreferencesUtils.saveString("SID", id);
    }

    /**
     * 获取Id
     */
    public static String getId() {
        return SharedPreferencesUtils.getString("SID", "");
    }

    /**
     * 保存UsrId
     */
    public static void saveRegistrationId(String userId) {
        SharedPreferencesUtils.saveString("REGISTRATION_ID", userId);
    }

    /**
     * 获取UsrId
     */
    public static String getRegistrationId() {
        return SharedPreferencesUtils.getString("REGISTRATION_ID", "");
    }

    /**
     * 保存商家用户名
     */
    public static void saveStoreOwn(String storeUserName) {
        SharedPreferencesUtils.saveString("STORE_USER_NAME", storeUserName);
    }

    /**
     * 获取商家用户名
     */
    public static String getStoreOwn() {
        return SharedPreferencesUtils.getString("STORE_USER_NAME", "");
    }

    /**
     * 保存商家店名
     */
    public static void saveStore(String storeName) {
        SharedPreferencesUtils.saveString("STORE_NAME", storeName);
    }

    /**
     * 获取商家店名
     */
    public static String getStore() {
        return SharedPreferencesUtils.getString("STORE_NAME", "");
    }

    /**
     * 保存商家地址
     */
    public static void saveStoreAddr(String storeAddr) {
        SharedPreferencesUtils.saveString("STORE_ADDR", storeAddr);
    }

    /**
     * 获取商家地址
     */
    public static String getStoreAddr() {
        return SharedPreferencesUtils.getString("STORE_ADDR", "");
    }

    /**
     * 保存商家电话
     */
    public static void savePhone(String storeAddr) {
        SharedPreferencesUtils.saveString("PHONE", storeAddr);
    }

    /**
     * 获取商家电话
     */
    public static String getPhone() {
        return SharedPreferencesUtils.getString("PHONE", "");
    }


    // 保存验证状态
    public static void savaStatus(String time) {
        SharedPreferencesUtils.saveString("STATTUS", time);
    }

    public static String getStatus() {
        return SharedPreferencesUtils.getString("STATTUS", "0");
    }

    //清除登陆
    public static void clearLogin() {
        Log.i("clearLogin", "isTokenExpired: +++++++++++++++++++++++++++++++0005");
        SharedPreferencesUtils.saveString("USER_ID", "");
        SharedPreferencesUtils.saveBoolean(IS_LOGIN_ED, false);
        setIsLoginEd(false);
        saveStoreOwn("");
        saveStore("");
        saveStoreAddr("");
        savePhone("");

        saveToken("");
        saveUpCarId("");
        saveIcon("");
        savaStatus("");
        saveUserStatus("");

    }

    /**
     * 查询是否车主
     *
     * @return
     */
    public static boolean isIsOwner() {
        return SharedPreferencesUtils.getBoolean(IS_OWNER, false);
    }

    /**
     * 创建添加
     *
     * @param activity
     */
    public static void addActivityList(Activity activity) {
        activityList.add(activity);
    }

    /**
     * 退出移除
     *
     * @param activity
     */
    public static void removeActivityList(Activity activity) {
        activityList.remove(activity);
    }

    /**
     * 退出应用
     *
     * @param activity
     */
    public static void outApplication(Activity activity) {
        for (int i = 0; i < activityList.size(); i++) {
            activityList.get(i).finish();
        }
    }

    private static Handler handler;
    private static int mainThreadId;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        System.out.println("获取context：etApplicationContext()");
        mContext = getApplicationContext();

        //极光推送配置接口
        JPushInterface.setDebugMode(false);
        JPushInterface.init(mContext);
//        setBasicBuilder();

        handler = new Handler();//创建Handle
        mainThreadId = Process.myTid();//得到主线程id
        isLoginEd = SharedPreferencesUtils.getBoolean(IS_LOGIN_ED, false);
        isOwner = SharedPreferencesUtils.getBoolean(IS_OWNER, false);
        saveAndroidId();
        // autolayout 的适配初始化 包括状态栏和底部操作栏
//        AutoLayoutConifg.getInstance().useDeviceSize().init(this);
    }

    /**
     * 解决java.lang.NoClassDefFoundError错误，方法数超过65536了
     * 5.0以下系统会出次问题
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
    /**
     * 设置自定义通知栏样式
     */

    public void setBasicBuilder() {
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(mContext);
        builder.statusBarDrawable = R.mipmap.logo;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
                | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
        builder.notificationDefaults = Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE
                | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要

        JPushInterface.setPushNotificationBuilder(1, builder);
    }

    /**
     * 高级自定义的 PushNotificationBuilder
     */
    public void setCustomBuilder() {
        CustomPushNotificationBuilder builder = new
                CustomPushNotificationBuilder(mContext,
                R.layout.customer_notitfication_layout,
                R.id.icon,
                R.id.title,
                R.id.text);
        // 指定定制的 Notification Layout

        builder.statusBarDrawable = R.mipmap.logo;
        // 指定最顶层状态栏小图标
        builder.layoutIconDrawable = R.mipmap.logo;
        // 指定下拉状态栏时显示的通知图标
        JPushInterface.setPushNotificationBuilder(2, builder);
    }


    public void JPush() {
        //停止推送服务。需要调用resumePush恢复。
        JPushInterface.stopPush(mContext);
        //恢复推送服务。调用了此 API 后，极光推送完全恢复正常工作。
        JPushInterface.resumePush(mContext);
        //用来检查 Push Service 是否已经被停止(SDK 1.5.2 以上版本支持。)
        JPushInterface.isPushStopped(mContext);
    }

}
