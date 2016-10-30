package com.tal.interactors;

import com.tal.base.AbstractInteractor;
import com.tal.base.Repository;
import com.tal.executors.PostExecutor;
import com.tal.executors.ThreadExecutor;

import rx.Observable;


public class GetPosterImageInteractor extends AbstractInteractor<String>{

    Repository<String> mRepository;

    public GetPosterImageInteractor(ThreadExecutor threadExecutor, PostExecutor postExecutor, Repository<String> repository) {
        super(threadExecutor, postExecutor);
        this.mRepository = repository;
    }

    @Override
    public Observable<String> buildObservable() {
        return mRepository.getObservable();
    }
}

