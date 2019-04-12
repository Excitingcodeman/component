package com.gs.supply.component.manager;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author husky
 * create on 2019/4/12-09:17
 */
public class ActivityLifeManager {
    private static List<Activity> activityStack;

    private static class ActivityLifeManagerInstance {
        private static final ActivityLifeManager instance = new ActivityLifeManager();
    }

    private ActivityLifeManager() {
        activityStack = Collections.synchronizedList(new LinkedList<Activity>());
    }

    public static ActivityLifeManager getInstance() {
        return ActivityLifeManagerInstance.instance;
    }

    /**
     * activity入栈
     *
     * @param activity activity
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = Collections.synchronizedList(new LinkedList<Activity>());
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前的Activity
     *
     * @return
     */
    public Activity getCurrentActivity() {
        if (null == activityStack || activityStack.size() <= 0) {
            return null;
        }
        return activityStack.get(activityStack.size() - 1);
    }

    /**
     * 结束指定的activity
     *
     * @param activity activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定的activity
     *
     * @param cls cls
     */
    public void finishActivity(Class<? extends Activity> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)
                    && !activity.isFinishing()) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有的activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出程序
     *
     * @param context
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr =
                    (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断当前应用是否结束
     *
     * @return
     */
    public boolean isAppExit() {
        return activityStack == null || activityStack.isEmpty();
    }

}
