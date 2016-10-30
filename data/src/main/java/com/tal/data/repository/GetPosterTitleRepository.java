package com.tal.data.repository;

import com.tal.base.Repository;

import rx.Observable;


public class GetPosterTitleRepository implements Repository<String>{
    @Override
    public Observable<String> getObservable() {
        return Observable.just("NicoNicoNi~");
    }
}
