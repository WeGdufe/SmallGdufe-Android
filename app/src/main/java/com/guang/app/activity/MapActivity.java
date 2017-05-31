package com.guang.app.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.guang.app.R;
import com.guang.app.widget.PinchImageView;

import butterknife.Bind;

/**
 * 显示地图，含两校区地图、广州地铁图
 * Created by xiaoguang on 2017/2/19.
 */
public class MapActivity extends QueryActivity {
    @Bind(R.id.xiaoli_zoom_image_view)
    PinchImageView zoomImageView;

    public static final String doWhat = "what";
    public static final int doMapSanShui = 0;
    public static final int doMapGuangZhou = 1;
    public static final int doMapSubway = 2;
    private Bitmap mCurBitmap;
    private int nowMapFlag; //当前显示哪个校区的图片
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_likeXiaoli);
        super.addTitleBackBtn();
        setContentView(R.layout.xiaoli);
        nowMapFlag = getIntent().getIntExtra(doWhat,doMapSanShui);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_map_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void loadData() {
        loadMapAccordingMapFlag();
    }

    /**
     * 加载图片
     */
    private void loadMapAccordingMapFlag() {
        int mapId = R.mipmap.map_sanshui;
        if(nowMapFlag == MapActivity.doMapGuangZhou){
            mapId = R.mipmap.map_guangzhou; 
        }else if(nowMapFlag == MapActivity.doMapSubway){
            mapId = R.mipmap.map_subway;
        }
        mCurBitmap = BitmapFactory.decodeResource(getResources(), mapId);
        zoomImageView.reset();
        zoomImageView.setImageBitmap(mCurBitmap);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_change_Xiaoqu:
                nowMapFlag = Math.abs(1 - nowMapFlag);  //一行代码切换 0->1 1->0 2->1
                loadMapAccordingMapFlag();
                return true;
            case R.id.menu_subway_guangzhou:
                nowMapFlag = doMapSubway;
                loadMapAccordingMapFlag();
                return true;
        }
        return super.onOptionsItemSelected(item);   //返回按钮靠这个
    }

    @Override
    protected boolean shouldHideLoadingIcon() {
        return true;
    }
 
}
