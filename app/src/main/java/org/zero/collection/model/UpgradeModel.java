package org.zero.collection.model;

/**
 * 版本升级模型
 * Created by Zero on 2016/2/23.
 */
public class UpgradeModel {
    /**
     * 版本号
     */
    private int VersionCode;
    /**
     * 版本特性
     */
    private String feature;
    /**
     * apk下载url
     */
    private String apkUrl;
    /**
     * apk大小
     */
    private int apkSize;

    public int getVersionCode() {
        return VersionCode;
    }

    public void setVersionCode(int versionCode) {
        VersionCode = versionCode;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public int getApkSize() {
        return apkSize;
    }

    public void setApkSize(int apkSize) {
        this.apkSize = apkSize;
    }
}
