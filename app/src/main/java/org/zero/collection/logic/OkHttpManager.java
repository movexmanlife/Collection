package org.zero.collection.logic;

import org.json.JSONException;
import org.zero.collection.util.ExceptionUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Zero on 2016/1/28.
 */
public class OkHttpManager {
    private static OkHttpManager okHttpManager = null;
    private static OkHttpClient client;

    /**
     * 获取对象
     */
    public static OkHttpManager getInstance() {
        if (okHttpManager == null) {
            synchronized (OkHttpManager.class) {
                okHttpManager = new OkHttpManager();
            }
        }
        return okHttpManager;
    }

    /**
     * 获取client对象，推荐全局只用一个
     */
    private OkHttpManager() {
        client = new OkHttpClient();
    }

    /**
     * 同步get请求(android4.0后要求不能在主线程中进行网络请求)
     */
    public String syncGet(String url) {
        String strResult = "";
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                strResult = response.body().string();
            } else {
                strResult = "获取失败：" + response;
            }
        } catch (IOException e) {
            ExceptionUtil.getInstance().outputException(e);
        }
        return strResult;
    }

    /**
     * 异步get请求
     */
    public void asyncGet(String url, Callback responseCallback) {
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(responseCallback);
    }

    /**
     * 同步post请求
     */
    public String syncPost(String url, HashMap<String, String> hashMap) throws JSONException {
        String strResult = "";
        FormBody.Builder builder = new FormBody.Builder();
        Iterator<Map.Entry<String, String>> iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            builder.add(next.getKey(), next.getValue());
        }
        FormBody body = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                strResult = response.body().string();
            } else {
                strResult = "获取失败：" + response;
            }
        } catch (IOException e) {
            ExceptionUtil.getInstance().outputException(e);
        }
        return strResult;
    }

    /**
     * 异步post请求
     */
    public void asyncPost(String url, HashMap<String, String> hashMap, Callback callback) {
        Iterator<Map.Entry<String, String>> iterator = hashMap.entrySet().iterator();
        FormBody.Builder builder = new FormBody.Builder();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            builder.add(next.getKey(), next.getValue());
        }
        FormBody body = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 下载文件
     */
    public void downloadFile(String url, FileDownloadCallback fileDownloadCallback) {
        if (!url.startsWith("http://")) {
            url = "http://" + url;
        }
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(fileDownloadCallback);
    }

}


