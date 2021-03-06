//package com.yongche.merchant.jpush;
//
//import android.app.NotificationManager;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//
//import cn.jpush.android.api.JPushInterface;
//
//public class TalkReceiver extends BroadcastReceiver {
//    private static final String TAG = "TalkReceiver";
//
//    private NotificationManager nm;
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        if (null == nm) {
//            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        }
//
//        Bundle bundle = intent.getExtras();
//        Logger.d(TAG, "onReceive - " + intent.getAction() + ", extras: " +  printBundle(bundle));
//
//        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
//            Logger.d(TAG, "JPush用户注册成功");
//
//        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//            Logger.d(TAG, "接受到推送下来的自定义消息");
//
//            // Push Talk messages are push down by custom message format
//            processCustomMessage(context, bundle);
//
//        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//            Logger.d(TAG, "接受到推送下来的通知");
//
////            receivingNotification(context,bundle);
//
//        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
//            Logger.d(TAG, "用户点击打开了通知");
//
////           openNotification(context,bundle);
//
//        } else {
//            Logger.d(TAG, "Unhandled intent - " + intent.getAction());
//        }
//    }
//
//    private void processCustomMessage(Context context, Bundle bundle) {
//        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
//        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//        if (TextUtils.isEmpty(title)) {
//            Logger.w(TAG, "Unexpected: empty title (friend). Give up");
//            return;
//        }
//
//        boolean needIncreaseUnread = true;
//
//        if (title.equalsIgnoreCase(Config.myName)) {
//            Logger.d(TAG, "Message from myself. Give up");
//            needIncreaseUnread = false;
//            if (!Config.IS_TEST_MODE) {
//                return;
//            }
//        }
//
//        String channel = null;
//        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//        try {
//            JSONObject extrasJson = new JSONObject(extras);
//            channel = extrasJson.optString(Constants.KEY_CHANNEL);
//        } catch (Exception e) {
//            Logger.w(TAG, "Unexpected: extras is not a valid json", e);
//        }
//
//        // Send message to UI (Webview) only when UI is up
//        if (!Config.isBackground) {
//            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//            msgIntent.putExtra(Constants.KEY_MESSAGE, message);
//            msgIntent.putExtra(Constants.KEY_TITLE, title);
//            if (null != channel) {
//                msgIntent.putExtra(Constants.KEY_CHANNEL, channel);
//            }
//
//            JSONObject all = new JSONObject();
//            try {
//                all.put(Constants.KEY_TITLE, title);
//                all.put(Constants.KEY_MESSAGE, message);
//                all.put(Constants.KEY_EXTRAS, new JSONObject(extras));
//            } catch (JSONException e) {
//            }
//            msgIntent.putExtra("all", all.toString());
//
//            context.sendBroadcast(msgIntent);
//        }
//
//        String chatting = title;
//        if (!StringUtils.isEmpty(channel)) {
//            chatting = channel;
//        }
//
//        String currentChatting = MyPreferenceManager.getString(Constants.PREF_CURRENT_CHATTING, null);
//        if (chatting.equalsIgnoreCase(currentChatting)) {
//            Logger.d(TAG, "Is now chatting with - " + chatting + ". Dont show notificaiton.");
//            needIncreaseUnread = false;
//            if (!Config.IS_TEST_MODE) {
//                return;
//            }
//        }
//
//        if (needIncreaseUnread) {
//            unreadMessage(title, channel);
//        }
//
//        NotificationHelper.showMessageNotification(context, nm, title, message, channel);
//    }
//
//    // When received message, increase unread number for Recent Chat
//    private void unreadMessage(final String friend, final String channel) {
//        new Thread() {
//            public void run() {
//                String chattingFriend = null;
//                if (TextUtils.isEmpty(channel)) {
//                    chattingFriend = friend;
//                }
//
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("udid", Config.udid);
//                params.put("friend", chattingFriend);
//                params.put("channel_name", channel);
//
//                try {
//                    HttpHelper.post(Constants.PATH_UNREAD, params);
//                } catch (Exception e) {
//                    Logger.e(TAG, "Call pushtalk api to report unread error", e);
//                }
//            }
//        }.start();
//    }
//
//    // 打印所有的 intent extra 数据
//    private static String printBundle(Bundle bundle) {
//        StringBuilder sb = new StringBuilder();
//        for (String key : bundle.keySet()) {
//            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
//                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
//            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
//                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
//            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
//                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
//                    Logger.i(TAG, "This message has no Extra data");
//                    continue;
//                }
//
//                try {
//                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
//                    Iterator<String> it = json.keys();
//
//                    while (it.hasNext()) {
//                        String myKey = it.next().toString();
//                        sb.append("\nkey:" + key + ", value: [" +
//                                myKey + " - " + json.optString(myKey) + "]");
//                    }
//                } catch (JSONException e) {
//                    Logger.e(TAG, "Get message extra JSON error!");
//                }
//
//            } else {
//                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
//            }
//        }
//        return sb.toString();
//    }
//}