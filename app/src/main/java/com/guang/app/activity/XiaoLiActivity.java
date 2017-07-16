package com.guang.app.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.guang.app.AppConfig;
import com.guang.app.R;
import com.guang.app.api.WorkApiFactory;
import com.guang.app.util.FileUtils;
import com.guang.app.util.TimeUtils;
import com.guang.app.widget.PinchImageView;

import butterknife.Bind;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * 校历、排课表
 * Created by xiaoguang on 2017/2/18.
 */
public class XiaoLiActivity extends QueryActivity {
    @Bind(R.id.xiaoli_zoom_image_view)
    PinchImageView zoomImageView;
//    private static JwcApiFactory factory = JwcApiFactory.getInstance();
    private static WorkApiFactory workApiFactory = WorkApiFactory.getInstance();

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
        zoomImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String path = FileUtils.saveImageFile(XiaoLiActivity.this,mCurBitmap, TimeUtils.getCurrentDateString()+".jpg",false);
                Toast.makeText(XiaoLiActivity.this, "已保存图片到"+path, Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    //显示校历或者排课表
    @Override
    protected void loadData() {
        startLoadingProgess();
        doFrom = getIntent().getIntExtra(doWhat,0);
        switch (doFrom){
            case XiaoLiActivity.doXiaoLi:
                getSupportActionBar().setSubtitle("以后将制作成日历表格式，非图片");
                workApiFactory.getDocumentFile(AppConfig.Const.DocumentCodeXiaoli, new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(ResponseBody value) {
                        mCurBitmap = BitmapFactory.decodeStream(value.byteStream());
                        zoomImageView.setImageBitmap(mCurBitmap);
                        stopLoadingProgess();
                    }
                    @Override
                    public void onError(Throwable e) {
                        mCurBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.xiaoli);
                    }
                    @Override
                    public void onComplete() {
                    }
                });
                break;
            case XiaoLiActivity.doTimeTable:
                getSupportActionBar().setSubtitle("可在课表主页右上角 添加到周日一列");
                mCurBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.time_table);
                zoomImageView.setImageBitmap(mCurBitmap);
                stopLoadingProgess();
                break;
        }
    }
    @Override
    protected boolean shouldHideLoadingIcon() {
        return true;
    }
}
