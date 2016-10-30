package com.tal.data.executor;


import com.tal.executors.PostExecutor;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

public class UIExecutor implements PostExecutor {


    public UIExecutor(){

    }

    @Override
    public Scheduler getScheduler() {
        return AndroidSchedulers.mainThread();
    }

}
