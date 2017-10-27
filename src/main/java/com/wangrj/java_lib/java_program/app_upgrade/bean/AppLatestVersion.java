package com.wangrj.java_lib.java_program.app_upgrade.bean;

/**
 * by 王荣俊 on 2016/10/9.
 */
public class AppLatestVersion {

    private int versionCode;
    private String versionName;
    private String description;
    private String apkFileUrl;

    public AppLatestVersion() {
    }

    public AppLatestVersion(int versionCode, String versionName, String description, String apkFileUrl) {
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.description = description;
        this.apkFileUrl = apkFileUrl;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getDescription() {
        return description;
    }

    public String getApkFileUrl() {
        return apkFileUrl;
    }
}
