package com.guang.app.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.guang.app.R;
import com.guang.app.widget.ZoomImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by xiaoguang on 2017/2/18.
 */
public class XiaoLiActivity extends QueryActivity {
    @Bind(R.id.xiaoli_zoom_image_view) ZoomImageView zoomImageView;
    public static final String doWhat = "what";
    public static final int doXiaoLi = 1;
    public static final int doTimeTable = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_likeXiaoli);
        super.addTitleBackBtn();
        setContentView(R.layout.xiaoli);
        ButterKnife.bind(this);

        Bitmap bitmap;
        int from = getIntent().getIntExtra(doWhat,0);
        if(from==XiaoLiActivity.doXiaoLi) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.xiaoli);
        }else{
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.time_table);
        }
        zoomImageView.setImageBitmap(bitmap);
    }
}
