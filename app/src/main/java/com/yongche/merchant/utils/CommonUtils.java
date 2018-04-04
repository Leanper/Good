package com.yongche.merchant.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.yongche.merchant.R;
import com.yongche.merchant.app.MyApplication;
import com.yongche.merchant.bean.BaseBean;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 普通的工具类
 */
public class CommonUtils {
    private static final String TAG = "CommonUtils";

    public static Context getContext() {
        return MyApplication.getContext();
    }

    //加载进度的progressDialog
    public static ProgressDialog showProgressDialog(Context context, String title) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(title + "...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    // progressDialog 消失
    public static void dissProgressDialog(ProgressDialog dialog) {
        dialog.dismiss();
    }

    // 获取资源文件夹操作
    public static Resources getResources() {
        return getContext().getResources();
    }

    // dip--->px, 1dp = 2px,定义一个控件的宽高 layoutParams(w,h)
    public static int dip2px(int dip) {
        // 获取dp和px的转换关系的变量
        float density = getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5);
    }

    // px---->dp
    public static int px2dip(int px) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5);
    }

    public static View inflate(int layoutId) {
        return View.inflate(getContext(), layoutId, null);
    }

    // 将dimens中的dp转换成像素值
    public static int getDimens(int dimensId) {
        return getResources().getDimensionPixelSize(dimensId);
    }

    /**
     * 获取Color中的属性
     *
     * @param colorId
     * @return
     */
    public static int getResourseColor(int colorId) {
        int color = MyApplication.getContext().getResources().getColor(colorId);
        return color;
    }

    public static ProgressDialog showInfoDialog(Context context, String message) {
        showInfoDialog(context, message, "提示", "取消", "确定", null, null);
        return null;
    }

    public static void showInfoDialog(Context context, String message,
                                      String titleStr, String positiveStr, String negativeStr,
                                      DialogInterface.OnClickListener positiveOnClickListener,
                                      DialogInterface.OnClickListener negativeOnClickListener) {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
        localBuilder.setTitle(titleStr);
        localBuilder.setMessage(message);
        if (positiveOnClickListener == null)
            positiveOnClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            };
        localBuilder.setPositiveButton(positiveStr, positiveOnClickListener);
        localBuilder.setNegativeButton(negativeStr, negativeOnClickListener);
        localBuilder.show();
    }

    /**
     * textView 字体加粗
     */

    public static void fakeBoldText(TextView textView) {
        TextPaint tp = textView.getPaint();
        tp.setFakeBoldText(true);

    }

    public static String md5(String paramString) {
        String returnStr;
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramString.getBytes());
            returnStr = byteToHexString(localMessageDigest.digest());
            return returnStr;
        } catch (Exception e) {
            return paramString;
        }
    }

    /**
     * 将指定byte数组转换成16进制字符串
     *
     * @param b
     * @return
     */
    public static String byteToHexString(byte[] b) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hexString.append(hex.toUpperCase());
        }
        return hexString.toString();
    }

    /**
     * 判断当前是否有可用的网络以及网络类型 0：无网络 1：WIFI 2：CMWAP 3：CMNET
     *
     * @param context
     * @return
     */
    public static int isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return 0;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        NetworkInfo netWorkInfo = info[i];
                        if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                            return 1;
                        } else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                            String extraInfo = netWorkInfo.getExtraInfo();
                            if ("cmwap".equalsIgnoreCase(extraInfo)
                                    || "cmwap:gsm".equalsIgnoreCase(extraInfo)) {
                                return 2;
                            }
                            return 3;
                        }
                    }
                }
            }
        }
        return 0;
    }


    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd HH:mm:ss
     */

    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getScreenPicHeight(int screenWidth, Bitmap bitmap) {
        int picWidth = bitmap.getWidth();
        int picHeight = bitmap.getHeight();
        int picScreenHeight = 0;
        picScreenHeight = (screenWidth * picHeight) / picWidth;
        return picScreenHeight;
    }


    private static Drawable createDrawable(Drawable d, Paint p) {

        BitmapDrawable bd = (BitmapDrawable) d;
        Bitmap b = bd.getBitmap();
        Bitmap bitmap = Bitmap.createBitmap(bd.getIntrinsicWidth(),
                bd.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(b, 0, 0, p); // 关键代码，使用新的Paint画原图，

        return new BitmapDrawable(bitmap);
    }

    /**
     * 设置Selector。 本次只增加点击变暗的效果，注释的代码为更多的效果
     */
    public static StateListDrawable createSLD(Context context, Drawable drawable) {
        StateListDrawable bg = new StateListDrawable();
        int brightness = 50 - 127;
        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(new float[]{1, 0, 0, 0, brightness, 0, 1, 0, 0,
                brightness,// 改变亮度
                0, 0, 1, 0, brightness, 0, 0, 0, 1, 0});

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));

        Drawable normal = drawable;
        Drawable pressed = createDrawable(drawable, paint);
        bg.addState(new int[]{android.R.attr.state_pressed,}, pressed);
        bg.addState(new int[]{android.R.attr.state_focused,}, pressed);
        bg.addState(new int[]{android.R.attr.state_selected}, pressed);
        bg.addState(new int[]{}, normal);
        return bg;
    }

    public static Bitmap getImageFromAssetsFile(Context ct, String fileName) {
        Bitmap image = null;
        AssetManager am = ct.getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;

    }

    public static String getUploadtime(long created) {
        StringBuffer when = new StringBuffer();
        int difference_seconds;
        int difference_minutes;
        int difference_hours;
        int difference_days;
        int difference_months;
        long curTime = System.currentTimeMillis();
        difference_months = (int) (((curTime / 2592000) % 12) - ((created / 2592000) % 12));
        if (difference_months > 0) {
            when.append(difference_months + "月");
        }

        difference_days = (int) (((curTime / 86400) % 30) - ((created / 86400) % 30));
        if (difference_days > 0) {
            when.append(difference_days + "天");
        }

        difference_hours = (int) (((curTime / 3600) % 24) - ((created / 3600) % 24));
        if (difference_hours > 0) {
            when.append(difference_hours + "小时");
        }

        difference_minutes = (int) (((curTime / 60) % 60) - ((created / 60) % 60));
        if (difference_minutes > 0) {
            when.append(difference_minutes + "分钟");
        }

        difference_seconds = (int) ((curTime % 60) - (created % 60));
        if (difference_seconds > 0) {
            when.append(difference_seconds + "秒");
        }

        return when.append("前").toString();
    }

    public static void runOnUIThread(Runnable runable) {
        //先判断当前属于子线程主线程
        if (android.os.Process.myTid() == MyApplication.getMainThreadId()) {
            runable.run();
        } else {
            //子线程
            MyApplication.getHandler().post(runable);
        }
    }


    //  判断是否是手机号码
    public static boolean isUserNumber(String number) {
        boolean re = false;
        //1. 控制前2位 ：13、14、15、17、18

//        2. 总长度：11位
        if (TextUtils.isEmpty(number)) {
            return false;
        }
        if (number.length() == 11) {
            if (number.startsWith("13")) {
                re = true;
            } else if (number.startsWith("14")) {
                re = true;
            } else if (number.startsWith("15")) {
                re = true;
            } else if (number.startsWith("17")) {
                re = true;
            } else if (number.startsWith("18")) {
                re = true;
            }
        }
        return re;
    }

    //  List<Stirng> 集合转换成 数组
    public static String[] listToArray(List<String> list) {
        final int size = list.size();
        String[] arr = (String[]) list.toArray(new String[size]);
        return arr;
    }

    public static String getLetter(String name) {
        String DefaultLetter = "#";
        if (TextUtils.isEmpty(name)) {
            return DefaultLetter;
        }
        char char0 = name.toLowerCase().charAt(0);
        if (Character.isDigit(char0)) {
            return DefaultLetter;
        }
        ArrayList<HanziToPinyin.Token> l = HanziToPinyin.getInstance().get(name.substring(0, 1));
        if (l != null && l.size() > 0 && l.get(0).target.length() > 0) {
            HanziToPinyin.Token token = l.get(0);
            // toLowerCase()返回小写， toUpperCase()返回大写
            String letter = token.target.substring(0, 1).toLowerCase();
            char c = letter.charAt(0);
            // 这里的 'a' 和 'z' 要和letter的大小写保持一直。
            if (c < 'a' || c > 'z') {
                return DefaultLetter;
            }
            return letter;
        }
        return DefaultLetter;
    }

    //  获取设备的唯一ID
    public static String getIMEI(Context context) {
   /*     TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String szImei = TelephonyMgr.getDeviceId();*/

        String m_szDevIDShort = "35" + //we make this look like a valid IMEI
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 + Build.USER.length() % 10; //13 digits

        String m_szAndroidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

    /*    WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();*/

    /*    BluetoothAdapter m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        ; // Local Bluetooth adapter
        String m_szBTMAC = m_BluetoothAdapter.getAddress();*/

        //  String m_szLongID = szImei + m_szDevIDShort + m_szAndroidID + m_szWLANMAC + m_szBTMAC;
        String m_szLongID = m_szDevIDShort + m_szAndroidID;
        // compute md5
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
        // get md5 bytes
        byte p_md5Data[] = m.digest();
        // create a hex string
        String m_szUniqueID = new String();
        for (int i = 0; i < p_md5Data.length; i++) {
            int b = (0xFF & p_md5Data[i]);
            // if it is a single digit, make sure it have 0 in front (proper padding)
            if (b <= 0xF)
                m_szUniqueID += "0";
            // add number to string
            m_szUniqueID += Integer.toHexString(b);
        }   // hex string to uppercase
        m_szUniqueID = m_szUniqueID.toUpperCase();
        Log.i(TAG, "getIMEI: ++++++++++++++" + m_szUniqueID);
//        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
//        String imei = tm.getDeviceId();//String
//        Log.i(TAG, "++++++++++++++getIMEI:  " + imei);
        return m_szUniqueID;
    }

    // 收起软件盘
    public static void closeKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
// 得到InputMethodManager的实例
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    // 呆值跳转的方法（Intent传递对象，未测试，暂时不使用）
    public static void startActivity(Activity activity, Class goTo, String name, BaseBean dataBean) {
        Intent intent = new Intent(activity, goTo);
        intent.putExtra(name, dataBean);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.xin_right, R.anim.xout_left);
    }

    // 呆值跳转的方法（测试成功，暂时不使用）
    public static void startActivity(Activity activity, Class goTo, Builder builder) {
        Intent intent = new Intent(activity, goTo);
        intent.putExtra(builder.key, builder.value);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.xin_right, R.anim.xout_left);
    }

    // 呆值跳转的方法（测试成功，可用）
    public static void startActivity(Activity activity, Class goTo, List<Builder> listBuilder) {
        Intent intent = new Intent(activity, goTo);
        if (listBuilder != null)
            for (Builder builder : listBuilder) {
                if (builder.dataBean != null) {
                    intent.putExtra(builder.key, builder.dataBean);
                } else {
                    intent.putExtra(builder.key, builder.value);
                }
            }
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.xin_right, R.anim.xout_left);
    }


    // 获取带值跳转的方法（测试成功，可用）
    public static String getIntentData(Activity activity, String key) {
        Intent intent = activity.getIntent();
        String stringExtra = intent.getStringExtra(key);
        return stringExtra;
    }

    // 获取带值跳转的方法（Intent获取传递的对象，该方法待测试）
    public static BaseBean getIntentSerializableData(Activity activity, String key) {
        Intent intent = activity.getIntent();
        BaseBean dataBean = (BaseBean) intent.getSerializableExtra(key);
        return dataBean;
    }

    //跳转的方法
    public static void startActivity(Activity activity, Class goTo) {
        Intent intent = new Intent(activity, goTo);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.xin_right, R.anim.xout_left);
    }

    //返回
    public static void finishActivity(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.xin_left, R.anim.xout_right);
    }

    public static class Builder {
        private String key;
        private String value;
        BaseBean dataBean;

        public Builder setKey(String key) {
            this.key = key;
            return this;
        }

        public Builder setValue(String value) {
            this.value = value;
            return this;
        }

        public Builder setDataBean(BaseBean dataBean) {
            this.dataBean = dataBean;
            return this;
        }
    }
}
