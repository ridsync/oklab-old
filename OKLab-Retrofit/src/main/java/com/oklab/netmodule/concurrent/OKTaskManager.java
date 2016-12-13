package com.oklab.netmodule.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by andman on 2016-12-12.
 */
public class OKTaskManager {

    /***************************
     * Static Member Variable
     ***************************/
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    private static OKTaskManager sThreadManager = null;

    private final ThreadPoolExecutor mForBackgroundTasks;
    private final Executor mMainThreadExecutor;

    private OKTaskManager() {

        ThreadFactory backgroundPriorityThreadFactory = new
                PriorityThreadFactory(android.os.Process.THREAD_PRIORITY_BACKGROUND);

        mForBackgroundTasks = new ThreadPoolExecutor(
                NUMBER_OF_CORES * 2,
                NUMBER_OF_CORES * 2,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                backgroundPriorityThreadFactory
        );

        mMainThreadExecutor = new MainThreadExecutor();
    }

    public static OKTaskManager getInstance(){
        if(sThreadManager == null){
            synchronized (OKTaskManager.class){
                sThreadManager = new OKTaskManager();
            }
        }
        return sThreadManager;
    }

    /***************************
     * Logical Member Variable
     ***************************/

    /***************************
     * Member Methods or Others
     ***************************/
    public ThreadPoolExecutor getBgTasks() {
        return mForBackgroundTasks;
    }

    public Executor getMainThreadTasks() {
        return mMainThreadExecutor;
    }

}
