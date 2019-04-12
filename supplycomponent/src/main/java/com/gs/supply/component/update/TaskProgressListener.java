package com.gs.supply.component.update;

import java.io.File;

/**
 * @author husky
 * create on 2019/4/12-14:42
 */
public interface TaskProgressListener {
    /**
     * 任务开始
     */
    void onStart();

    /**
     * 进度回调
     *
     * @param progress
     */
    void onNext(int progress);

    /**
     * 任务完成
     */
    void onComplete(File downFile);

    /**
     * 任务出错
     */
    void onError();

}
