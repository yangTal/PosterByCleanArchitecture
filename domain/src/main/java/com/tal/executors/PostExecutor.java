package com.tal.executors;

import rx.Scheduler;


public interface PostExecutor {
    Scheduler getScheduler();
}
