package com.guang.app.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.guang.app.R;
import com.guang.app.adapter.FewSztzAdapter;
import com.guang.app.api.InfoApiFactory;
import com.guang.app.model.FewSztz;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

//import org.reactivestreams.Subscriber;

//TODO 错误时显示空白

/**
 * 素拓
 * Created by xiaoguang on 2017/2/13.
 */
public class FewSztzActivity extends QueryActivity {
    private static InfoApiFactory factory = InfoApiFactory.getInstance();

    @Bind(R.id.common_recycleView) RecyclerView mRecyclerView;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addTitleBackBtn();
        setContentView(R.layout.common_listview);
        setTitle(R.string.title_fewsztz);
        ButterKnife.bind(this);
        initAdapterAndData();
    }

    private void initAdapterAndData() {
        final FewSztzAdapter mAdapter = new FewSztzAdapter(getApplicationContext(), R.layout.fewsztz_listitem);
        mAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        factory.getFewSztz(new Observer<List<FewSztz>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(List<FewSztz> value) {
                mAdapter.addData(value);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(FewSztzActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                FewSztzActivity.this.finish();
            }

            @Override
            public void onComplete() {
            }
        });
    }

}