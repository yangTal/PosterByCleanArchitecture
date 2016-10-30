package com.tal.posterbycleanarchitecture.presenter.contract;

import com.tal.posterbycleanarchitecture.presenter.IPresenter;
import com.tal.posterbycleanarchitecture.presenter.IView;


public interface PosterContract {

    interface View extends IView {

        void setPosterImage(String imageUrl);

        void setPosterTitle(String title);
    }

    interface Presenter extends IPresenter{

        void getPosterImage();

        void getPosterTitle();

    }

}
