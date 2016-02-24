package org.zero.collection.logic;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * Activity管理器
 * Created by Zero on 2016/1/22.
 */
public class ActivityManager {
    private static ActivityManager activityManager;

    //存放activity的容器
    private List<Activity> lstActivity = new LinkedList<>();

    private ActivityManager() {
    }

    /**
     * 获取对象
     */
    public static ActivityManager getInstance() {
        if (activityManager == null) {
            activityManager = new ActivityManager();
        }
        return activityManager;
    }

    /**
     * 添加activity对象到容器中
     */
    public void addActivity(Activity activity) {
        lstActivity.add(activity);
    }

    /**
     * 安全退出应用
     */
    public void exitApp() {
        for (int i = 0; i < lstActivity.size(); i++) {
            Activity activity = lstActivity.get(i);
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }


}
