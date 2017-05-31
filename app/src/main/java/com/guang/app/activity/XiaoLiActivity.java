package com.guang.app.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.guang.app.R;
import com.guang.app.widget.PinchImageView;

import butterknife.Bind;

/**
 * 校历、排课表
 * Created by xiaoguang on 2017/2/18.
 */
public class XiaoLiActivity extends QueryActivity {
    @Bind(R.id.xiaoli_zoom_image_view)
    PinchImageView zoomImageView;

    public static final String doWhat = "what";
    public static final int doTimeTable = 0;
    public static final int doXiaoLi = 1;
    Bitmap mCurBitmap;
    int doFrom; //当前做啥

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_likeXiaoli);

        super.addTitleBackBtn();
        setContentView(R.layout.xiaoli);

    }

    //显示校历或者排课表
    @Override
    protected void loadData() {
        startLoadingProgess();
        doFrom = getIntent().getIntExtra(doWhat,0);
        int mapId = R.mipmap.xiaoli;
        switch (doFrom){
            case XiaoLiActivity.doXiaoLi:
                mapId = R.mipmap.xiaoli;
                getSupportActionBar().setSubtitle("以后将制作成日历表格式，非图片");
                break;
            case XiaoLiActivity.doTimeTable:
                mapId = R.mipmap.time_table;
                getSupportActionBar().setSubtitle("可在课表主页右上角 添加到周日一列");
                break;
        }
        mCurBitmap = BitmapFactory.decodeResource(getResources(), mapId);
        zoomImageView.setImageBitmap(mCurBitmap);
        stopLoadingProgess();
    }
    @Override
    protected boolean shouldHideLoadingIcon() {
        return true;
    }
}
