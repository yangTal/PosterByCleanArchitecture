package com.tal.posterbycleanarchitecture.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tal.posterbycleanarchitecture.R;
import com.tal.posterbycleanarchitecture.presenter.PosterPresenter;
import com.tal.posterbycleanarchitecture.presenter.contract.PosterContract;

public class PosterActivity extends AppCompatActivity implements PosterContract.View {

    PosterPresenter mPosterPresenter;
    private ImageView mIv;
    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPosterPresenter = new PosterPresenter(this);
        initView();
        initData();
    }

    private void initData() {
        mPosterPresenter.getPosterImage();
        mPosterPresenter.getPosterTitle();
    }

    private void initView() {
        mIv = (ImageView) findViewById(R.id.iv);
        mTv = (TextView) findViewById(R.id.tv);
    }


    @Override
    public void setPosterImage(String imageUrl) {
        Glide.with(this).load(imageUrl).into(mIv);
    }

    @Override
    public void setPosterTitle(String title) {
        mTv.setText(title);
    }
}
