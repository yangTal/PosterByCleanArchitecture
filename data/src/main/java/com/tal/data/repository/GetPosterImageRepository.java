package com.tal.data.repository;

import com.tal.base.Repository;

import rx.Observable;


public class GetPosterImageRepository implements Repository<String> {
    @Override
    public Observable<String> getObservable() {
        return Observable.just("http://cdn.aixifan.com/dotnet/20130418/umeditor/dialogs/emotion/images/ac2/29.gif");
    }
}
