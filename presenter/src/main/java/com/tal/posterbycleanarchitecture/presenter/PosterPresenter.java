package com.tal.posterbycleanarchitecture.presenter;

import com.tal.interactors.GetDataInteractor;
import com.tal.interactors.GetPosterImageInteractor;
import com.tal.interactors.GetPosterTitleInteractor;
import com.tal.base.DefaultSubscriber;
import com.tal.data.repository.GetPosterImageRepository;
import com.tal.data.repository.GetPosterTitleRepository;
import com.tal.data.executor.JobExecutor;
import com.tal.data.executor.UIExecutor;
import com.tal.posterbycleanarchitecture.presenter.contract.PosterContract;


public class PosterPresenter implements PosterContract.Presenter {

    GetPosterImageInteractor mImageInteractor;
    GetPosterTitleInteractor mTitleInteractor;
    GetDataInteractor<String> mDataInteractor;
    PosterContract.View mView;

    public PosterPresenter(PosterContract.View view){
        this.mView = view;
        mImageInteractor = new GetPosterImageInteractor(new JobExecutor(),new UIExecutor(),new GetPosterImageRepository());
        mTitleInteractor = new GetPosterTitleInteractor(new JobExecutor(),new UIExecutor(),new GetPosterTitleRepository());
//        mDataInteractor = new GetDataInteractor<>(new JobExecutor(),new UIExecutor(),new GetPosterImageRepository());
    }

    @Override
    public void getPosterImage() {
        mImageInteractor.execute(new DefaultSubscriber<String>(){
            @Override
            public void onNext(String s) {
                mView.setPosterImage(s);
            }
        });
//        mDataInteractor.execute(new DefaultSubscriber<String>(){
//            @Override
//            public void onNext(String s) {
//                mView.setPosterImage(s);
//            }
//        });
    }

    @Override
    public void getPosterTitle() {
        mTitleInteractor.execute(new DefaultSubscriber<String>(){
            @Override
            public void onNext(String s) {
                mView.setPosterTitle(s);
            }
        });
    }
}
