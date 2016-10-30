package com.tal.base;

import com.tal.executors.PostExecutor;
import com.tal.executors.ThreadExecutor;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public abstract class AbstractInteractor<T> implements Interactor<Subscriber>{

    protected CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    protected PostExecutor mPostExecutor;

    protected ThreadExecutor mThreadExecutor;

    public AbstractInteractor(ThreadExecutor threadExecutor, PostExecutor postExecutor){
        this.mThreadExecutor = threadExecutor;
        this.mPostExecutor = postExecutor;
    }

    public abstract Observable<T> buildObservable();

    @SuppressWarnings("unchecked")
    @Override
    public void execute(Subscriber task) {

        mCompositeSubscription.add(task);

        buildObservable()
                .subscribeOn(Schedulers.from(mThreadExecutor))
                .observeOn(mPostExecutor.getScheduler())
                .subscribe(task);
    }

    public void unsubscribe() {
        mCompositeSubscription.clear();
    }
}

