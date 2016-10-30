package com.tal.interactors;

import com.tal.base.AbstractInteractor;
import com.tal.base.Repository;
import com.tal.executors.PostExecutor;
import com.tal.executors.ThreadExecutor;

import rx.Observable;


public class GetDataInteractor<T> extends AbstractInteractor<T> {

    Repository<T> mRepository;

    public GetDataInteractor(ThreadExecutor threadExecutor, PostExecutor postExecutor,Repository<T> repository) {
        super(threadExecutor, postExecutor);
        this.mRepository = repository;
    }

    @Override
    public Observable<T> buildObservable() {
        return mRepository.getObservable();
    }
}
