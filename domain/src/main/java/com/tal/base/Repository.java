package com.tal.base;

import rx.Observable;

public interface Repository<T> {
    Observable<T> getObservable();
}
