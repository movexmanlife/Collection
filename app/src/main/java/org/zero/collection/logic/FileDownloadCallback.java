package org.zero.collection.logic;

import android.os.Handler;
import android.os.Looper;


import org.zero.collection.util.ExceptionUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 文件下载回调
 * Created by Zero on 2016/1/28.
 */
public abstract class FileDownloadCallback implements Callback {
    private Handler handler;
    private String strFileName;

    /**
     * @param strFileName 目标文件夹
     */
    public FileDownloadCallback(String strFileName) {
        this.strFileName = strFileName;
        handler = new Handler(Looper.getMainLooper());
    }

    /**
     * 将进度传递到主线程中，用于更新界面
     */
    private void postProgress(final int porgress) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                onProgress(porgress);
            }
        });
    }

    /**
     * 将成功结果传递到主线程中，用于更新界面
     */
    private void postSuccess() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                onSuccess("Success");
            }
        });
    }


    /**
     * 将失败结果传递到主线程中，用于更新界面
     */
    @Override
    public void onFailure(final Call call, final IOException e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                onFail(call, e);
            }
        });
    }

    public abstract void onProgress(int progress);

    public abstract void onFail(Call call, IOException e);

    public abstract void onSuccess(String result);

    @Override
    public void onResponse(Call call, Response response) {
        InputStream is = null;
        FileOutputStream fos = null;
        is = response.body().byteStream();
        final long lngTotal = response.body().contentLength();
        long lngSum = 0;
        try {
            fos = new FileOutputStream(new File(Constant.SDCARD_PATH, strFileName));
            byte[] buf = new byte[1024];
            int length = 0;
            long lngLastTime = System.currentTimeMillis();
            while ((length = is.read(buf)) != -1) {
                lngSum += length;
                fos.write(buf, 0, length);
                if (System.currentTimeMillis() - lngLastTime > 1000) {
                    lngLastTime = System.currentTimeMillis();
                    final long sum = lngSum;
                    postProgress((int) (sum * 100 / lngTotal));
                }
            }
            fos.flush();
            postProgress(100);
            postSuccess();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                fos.close();
            } catch (IOException e) {
                ExceptionUtil.getInstance().outputException(e);
            }
        }
    }

}