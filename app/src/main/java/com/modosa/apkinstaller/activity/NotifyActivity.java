package com.modosa.apkinstaller.activity;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.modosa.apkinstaller.R;

/**
 * @author dadaewq
 */
public class NotifyActivity extends Activity {
    private String packageLable;
    private String channelId;
    private String channelName;
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        channelId = getIntent().getStringExtra("channelId");
        channelName = getIntent().getStringExtra("channelName");
        String packageName = getIntent().getStringExtra("packageName");
        packageLable = getIntent().getStringExtra("packageLable");


        int id = (int) System.currentTimeMillis();


        PendingIntent clickIntent = getContentIntent(this, id, packageName);
        notifyLiveStart(this, clickIntent, id);

        finish();

    }

    private void notifyLiveStart(Activity context, PendingIntent intent, int id) {

        NotificationChannel channel;


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            if (notificationManager == null) {
                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            }
            channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            //是否在桌面icon右上角展示小红点
            channel.enableLights(true);
            //是否在久按桌面图标时显示此渠道的通知
            channel.setShowBadge(true);

            notificationManager.createNotificationChannel(channel);

        }

        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        Notification notification = new NotificationCompat.Builder(context, channelId)
                //设置通知栏标题
                .setContentTitle(packageLable + " " + getString(R.string.install_over))
                //设置通知栏显示内容
                .setContentText(getString(R.string.click_run))
                //通知产生的时间，会在通知信息里显示
                .setWhen(System.currentTimeMillis())
                //设置小图标（通知栏没有下拉的图标）
                .setSmallIcon(android.R.mipmap.sym_def_app_icon)
                //设置右侧大图标
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
//                        R.drawable.ic_launcher_background))
                //设置点击通知后自动删除通知
                .setAutoCancel(true)
                .setContentIntent(intent)
                .build();
//        .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
//
//        .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
//
//        .setOngoing(false)//ture，设置他为一个正在进行的通知,通常是用来表示一个后台任务,以某种方式正在等待,如一个文件下载,同步操作
//
//        .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果


        notificationManager.notify(id, notification);

    }

    private PendingIntent getContentIntent(Activity context, int id, String packageName) {

        try {
            Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);

            return PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        } catch (Exception e) {
            return null;
        }


    }


}