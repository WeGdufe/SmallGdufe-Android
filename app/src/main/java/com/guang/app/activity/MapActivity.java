package com.guang.app.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.guang.app.R;
import com.guang.app.widget.ZoomImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 显示地图，含两校区地图
 * Created by xiaoguang on 2017/2/19.
 */
public class MapActivity extends QueryActivity {
    @Bind(R.id.xiaoli_zoom_image_view) ZoomImageView zoomImageView;

    public static final String doWhat = "what";
    public static final boolean doMapSanShui = false;
    public static final boolean doMapGuangZhou = true;
    Bitmap mCurBitmap;
    boolean nowMapFlag; //当前显示哪个校区的图片
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_likeXiaoli);
        super.addTitleBackBtn();
        setContentView(R.layout.xiaoli);
        ButterKnife.bind(this);
        nowMapFlag = getIntent().getBooleanExtra(doWhat,false);
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
        }
        mCurBitmap = BitmapFactory.decodeResource(getResources(), mapId);
        zoomImageView.setImageBitmap(mCurBitmap);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_change_Xiaoqu:
                nowMapFlag = !nowMapFlag;
                loadMapAccordingMapFlag();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected boolean hideLoadingIcon() {
        return true;
    }
 
}
