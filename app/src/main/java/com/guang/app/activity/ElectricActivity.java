package com.guang.app.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.guang.app.R;
import com.guang.app.adapter.ElectricAdapter;
import com.guang.app.api.CardApiFactory;
import com.guang.app.model.Electric;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * 本部部分宿舍电控查询
 * Created by xiaoguang on 2017/9/9.
 */
public class ElectricActivity extends QueryActivity {
    private static CardApiFactory factory = CardApiFactory.getInstance();

    @Bind(R.id.common_recycleView)
    RecyclerView mRecyclerView;
    @Bind(R.id.ed_electric_room)
    EditText edRoom;
    @Bind(R.id.ed_electric_building)
    EditText edBuilding;

    private ElectricAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addTitleBackBtn();
        setTitle(R.string.title_query_electric);
        setContentView(R.layout.electric);
        initAdapter();
    }
    @OnClick(R.id.btn_electric_query) void clickQueryElectric() {
        String room = edRoom.getText().toString().trim();
        String building = edBuilding.getText().toString().trim();
        if(! (TextUtils.isEmpty(room) || TextUtils.isEmpty(building)) ) {
            realQueryElectric(building, room);
        }
    }

    @Override
    protected void loadData() {
    }

    private void realQueryElectric(String building,String room) {
        factory.getElectric(building, room, new Observer<List<Electric>>() {
            @Override
            public void onSubscribe(Disposable d) {
                startLoadingProgess();
            }

            @Override
            public void onNext(List<Electric> value) {
                if (value.size() == 0) {
                    Toast.makeText(ElectricActivity.this, "没有记录喔", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAdapter.cleanData();
                mAdapter.addData(value);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(ElectricActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                stopLoadingProgess();
            }

            @Override
            public void onComplete() {
                stopLoadingProgess();
                mAdapter.isUseEmpty(true);
            }
        });
    }

    private void initAdapter() {
        mAdapter = new ElectricAdapter(R.layout.electric_item);
        mAdapter.openLoadAnimation();
        mAdapter.setEmptyView(R.layout.layout_empty_data, (ViewGroup) mRecyclerView.getParent());
        mAdapter.isUseEmpty(false); //避免一开始就出现空页面
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}