package org.zero.collection.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 用于获取设备的相关信息
 * Created by Zero on 2016/2/4.
 */
public class DeviceInfoUtil {
    /**
     * 获取mac地址
     */
    public static String macAddress(Context context) {
        SharedPreferences sp = context.getSharedPreferences("FeelCafe", 0);
        String strMacAddress = sp.getString("MacAddress", "");
        if (strMacAddress.equals(null) || strMacAddress.equals("")) {
            strMacAddress = wifiMacAddress(context);
            if (strMacAddress == null || strMacAddress.length() == 0) {
                strMacAddress = localMacAddress();
            }
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("MacAddress", strMacAddress);
            edit.commit();
        }
        return strMacAddress;
    }

    /**
     * wifi下获取mac地址（需要保证wifi在本次开机以来曾经是打开过的，否则会返回null）
     */
    private static String wifiMacAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        String strMacAddress = info.getMacAddress();
        return strMacAddress;
    }

    /**
     * 读取本地文件获取mac地址
     */
    private static String localMacAddress() {
        String strMacAddress = null;
        FileInputStream fis1 = null;
        FileInputStream fis2 = null;
        try {
            String path1 = "sys/class/net/wlan0/address";
            if ((new File(path1).exists())) {
                fis1 = new FileInputStream(path1);
                byte[] buffer = new byte[8192];
                int byteCount = fis1.read(buffer);
                if (byteCount > 0) {
                    strMacAddress = new String(buffer, 0, byteCount, "utf-8");
                }
                if (strMacAddress == null || strMacAddress.length() == 0) {
                    String path2 = "sys/class/net/eth0/address";
                    fis2 = new FileInputStream(path2);
                    byte[] buffer2 = new byte[8192];
                    int byteCount2 = fis2.read(buffer2);
                    if (byteCount2 > 0) {
                        strMacAddress = new String(buffer2, 0, byteCount2, "utf-8");
                    }
                }
                if (strMacAddress == null || strMacAddress.length() == 0) {
                    strMacAddress = "";
                }
            }
        } catch (Exception e) {
            ExceptionUtil.getInstance().outputException(e);
        } finally {
            try {
                if (fis1 != null) {
                    fis1.close();
                    fis1 = null;
                }
                if (fis2 != null) {
                    fis2.close();
                    fis2 = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return strMacAddress.trim();
    }

}
