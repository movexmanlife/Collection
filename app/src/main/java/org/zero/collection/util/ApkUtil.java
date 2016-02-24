package org.zero.collection.util;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.gson.Gson;

import org.zero.collection.R;
import org.zero.collection.logic.FileDownloadCallback;
import org.zero.collection.logic.OkHttpManager;
import org.zero.collection.model.UpgradeModel;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * apk工具类
 * Created by Zero on 2016/1/29.
 */
public class ApkUtil {

    private static RemoteViews notificationView;
    private static NotificationManager notificationManager;
    private static Notification notification;

    /**
     * 检查升级
     */
    public static void checkUpgrade(final Context context,String url) {
        OkHttpManager.getInstance().asyncGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(context,"获取版本信息失败，请稍后再试",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                analyseVersionJson(context,response.body().string());
            }
        });
    }

    /**
     * 分析版本信息
     */
    private static void analyseVersionJson(final Context context, String json) {
        Gson gson = new Gson();
        final UpgradeModel upgradeModel = gson.fromJson(json, UpgradeModel.class);
        if (upgradeModel.getVersionCode() > getVersionCode(context)) {
            new AlertDialog.Builder(context)
                    .setTitle("版本升级")
                    .setMessage(upgradeModel.getFeature())
                    .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            versionUpgrade(context,upgradeModel);
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }else{
            Toast.makeText(context, "已是最新版本!", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 版本升级
     */
    private static void versionUpgrade(final Context context, final UpgradeModel upgradeModel) {
        Toast.makeText(context, "正在下载，请稍候！", Toast.LENGTH_SHORT).show();
        sendUpgradeNotification(context);
        final String strAppName = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
        OkHttpManager.getInstance().downloadFile(upgradeModel.getApkUrl(), new FileDownloadCallback(strAppName + ".apk") {
            @Override
            public void onProgress(int progress) {
                updateNotification(progress);
            }

            @Override
            public void onFail(Call call, IOException e) {
                cancelNotification();
                Toast.makeText(context, "下载失败，请重试", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(String result) {
                cancelNotification();
                installApk(context, Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + strAppName + ".apk");
            }
        });
    }

    /**
     * 发送升级通知
     */
    private static void sendUpgradeNotification(Context context) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationView = new RemoteViews(context.getPackageName(), R.layout.notification_upgrade);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(), 0);
        //单击后自动删除
        notification = new NotificationCompat.Builder(context)
                .setContent(notificationView)
                .setAutoCancel(true) //单击后自动删除
                // 定制通知布局
                .setSmallIcon(R.drawable.ic_launcher).setTicker("开始更新")
                .setContentIntent(pendingIntent).build();
        notificationManager.notify(123456, notification);
    }

    /**
     * 更新进度条
     */
    private static void updateNotification(int progress) {
        notificationView.setTextViewText(R.id.txt_progress_upgrade_notification, progress + "%");
        notificationView.setProgressBar(R.id.progressBar_upgrade_notification, 100, progress, false);
        notificationManager.notify(123456, notification);
    }

    /**
     * 取消下载notification
     */
    private static void cancelNotification() {
        notificationManager.cancel(123456);
    }

    /**
     * 获取应用的版本号
     */
    public static int getVersionCode(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 安装apk
     */
    public static void installApk(Context context, String apkPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(apkPath)), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

}
