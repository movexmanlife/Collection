package org.zero.collection.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.widget.Toast;

import org.zero.collection.logic.ActivityManager;
import org.zero.collection.logic.Constant;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 捕获异常工具类，将异常写入sd卡中，便于查询
 * 使用：在application中getInstance后init,然后修改try{}catch(){}语句中的e.printStackTrace为ExceptionUtil.getInstance.outputException(e)
 */

public class ExceptionUtil implements UncaughtExceptionHandler {
    private static ExceptionUtil exceptionUtil = null;
    private Context mContext;

    private ExceptionUtil() {
    }

    /**
     * 获得对象
     */
    public static ExceptionUtil getInstance() {
        if (exceptionUtil == null) {
            exceptionUtil = new ExceptionUtil();
        }
        return exceptionUtil;
    }

    /**
     * 初始化
     */
    public void init(Context context) {
        mContext = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当有未处理的异常发生时触发
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        //程序异常提示
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉，程序出现异常，即将退出", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }).start();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateAndTime = format.format(new Date());
        String strContent = null;
        strContent = "UncaughtException---Time:" + dateAndTime + "-----" + "\n"
                + "thread: " + thread.getName() + "\n"
                + "exception:" + ex.toString() + "\n"
                + "cause:" + ex.getCause() + "\n"
                + "message:" + ex.getMessage() + "\n";
        strContent = strContent + "-----------------------   DeviceInfo   -----------------------" + "\n"
                + deviceInfo(mContext);
        StackTraceElement[] trace = ex.getStackTrace();
        String traceDetail = "";
        for (int i = 0; i < trace.length; i++) {
            traceDetail = traceDetail + "\n"
                    + "-----------------------  the " + i + " element  -----------------------" + "\n"
                    + "toString: " + trace[i].toString() + "\n"
                    + "ClassName: " + trace[i].getClassName() + "\n"
                    + "FileName: " + trace[i].getFileName() + "\n"
                    + "LineNumber: " + trace[i].getLineNumber() + "\n"
                    + "MethodName: " + trace[i].getMethodName();
        }
        strContent = strContent + traceDetail + "\n\n\n";
        write2sd(strContent);

        //安全退出程序
        ActivityManager.getInstance().exitApp();
    }

    /**
     * 主动捕获的异常，应用于try{}catch(){}中
     *
     * @param e
     */
    public void outputException(Exception e) {
        StackTraceElement[] trace = e.getStackTrace();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateAndTime = format.format(new Date());
        String strContent = null;
        strContent = "CaughtException---Time:" + dateAndTime + "-----" + "\n"
                + "Exception:" + e.toString() + "\n";
        strContent = strContent + "-----------------------   DeviceInfo   -----------------------" + "\n"
                + deviceInfo(mContext);
        String traceDetail = "";
        for (int i = 0; i < trace.length; i++) {
            traceDetail = traceDetail + "\n"
                    + "-----------------------  the " + i + " element  -----------------------" + "\n"
                    + "ClassName: " + trace[i].getClassName() + "\n"
                    + "FileName: " + trace[i].getFileName() + "\n"
                    + "LineNumber: " + trace[i].getLineNumber() + "\n"
                    + "MethodName: " + trace[i].getMethodName();
        }
        strContent = strContent + traceDetail + "\n\n\n";
        write2sd(strContent);
    }

    /**
     * 异常写入sd卡上
     */
    private void write2sd(String content) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());

        File bugPath = new File(Constant.BUGS_PATH);
        if (!bugPath.exists()) {
            bugPath.mkdirs();
        }
        File file = new File(bugPath + "/FeelCafeBug" + date + ".txt");
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file, true));
            bos.write(content.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取设备信息（手机品牌、系统版本号、系统版本名、app版本号）
     */
    private String deviceInfo(Context context) {
        String info = null;
        try {
            info = "Brand:" + Build.MODEL + "\n"
                    + "SDKVersion:" + Build.VERSION.SDK_INT + "\n"
                    + "SystemVersion:" + Build.VERSION.RELEASE + "\n"
                    + "AppVersion:" + context.getPackageManager().getPackageInfo("com.cldvision.coffee", 0).versionName;
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        return info;
    }
}

