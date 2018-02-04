package com.guang.app.fragment;


import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.guang.app.R;
import com.guang.app.activity.FeedAddActivity;
import com.guang.app.api.SocialApiFactory;
import com.guang.app.api.WorkApiFactory;
import com.guang.app.model.Feed;
import com.guang.app.model.Schedule;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.baseadapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.baseadapter.BGAOnRVItemLongClickListener;
import cn.bingoogolapple.baseadapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bingoogolapple.photopicker.imageloader.BGARVOnScrollListener;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

//社交
public class SocialFragment extends Fragment  implements BGARefreshLayout.BGARefreshLayoutDelegate, EasyPermissions.PermissionCallbacks, BGANinePhotoLayout.Delegate, BGAOnRVItemClickListener, BGAOnRVItemLongClickListener {

    @Bind(R.id.social_feed_listview)
    RecyclerView mFeedRv;
    FeedAdapter mFeedAdapter;
    BGANinePhotoLayout mCurrentClickNpl;
    private static SocialApiFactory socialApiFactory = SocialApiFactory.getInstance();

    @Bind(R.id.social_feed_refresh)
    BGARefreshLayout mRefreshLayout;    //上拉刷新
    private static final int PAGE_NUM = 20;  //一次分页的大小
    List<Feed> mFeeds = new ArrayList<>();
    int mPageNo = 0;

    //下拉刷新
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        goFindLatestData();
    }
    //上拉加载，implement BGARefreshLayoutDelegate 即可
    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        goFindMoreData();
        return true;
    }

    private class FeedAdapter extends BGARecyclerViewAdapter<Feed> {

        public FeedAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.item_feed);
        }

        @Override
        protected void fillData(BGAViewHolderHelper helper, int position, Feed feed) {
            if (TextUtils.isEmpty(feed.getContent())) {
                helper.setVisibility(R.id.tv_item_feed_content, View.GONE);
            } else {
                helper.setVisibility(R.id.tv_item_feed_content, View.VISIBLE);
                helper.setText(R.id.tv_item_feed_content, feed.getContent());
            }
            BGANinePhotoLayout ninePhotoLayout = helper.getView(R.id.npl_item_feed_photos);
            ninePhotoLayout.setDelegate(SocialFragment.this);
            ninePhotoLayout.setData(feed.getPhotos());
        }
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_social, container,false);
        ButterKnife.bind(this, view);

        mFeedAdapter = new FeedAdapter(mFeedRv);
        mFeedAdapter.setOnRVItemClickListener(this);
        mFeedAdapter.setOnRVItemLongClickListener(this);

        mFeedRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFeedRv.setAdapter(mFeedAdapter);
        mFeedRv.addOnScrollListener(new BGARVOnScrollListener(getActivity()));


        mRefreshLayout.setDelegate(this);
        BGANormalRefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(getActivity(), true);
        // 设置下拉刷新和上拉加载更多的风格
        refreshViewHolder.setPullDownRefreshText("往下拉！");
        refreshViewHolder.setRefreshingText("正在刷！");
        refreshViewHolder.setReleaseRefreshText("放开我！");
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);

        mPageNo = 0;
        mFeeds = new ArrayList<>();
//        mRefreshLayout.beginLoadingMore();
        goFindMoreData();
		return view;
	}


    private void goFindMoreData() {
        mPageNo ++;
        socialApiFactory.getImFeedList(mPageNo,PAGE_NUM, new Observer<List<Feed>>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(List<Feed> feeds) {
                mFeeds.addAll(feeds);
                mFeedAdapter.setData(mFeeds);
                mRefreshLayout.endLoadingMore();
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
    //下拉刷新，获取最新数据
    private void goFindLatestData() {
        mPageNo = 1;
        socialApiFactory.getImFeedList(mPageNo,PAGE_NUM, new Observer<List<Feed>>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(List<Feed> feeds) {
                mFeeds.clear();
                mFeeds.addAll(feeds);
                mFeedAdapter.setData(mFeeds);
                mRefreshLayout.endRefreshing();
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

        /**
         * 添加网络图片测试数据
         */
    private void addNetImageTestData() {
//        feeds.add(new Feed("1张网络图片", new ArrayList<>(Arrays.asList("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered1.png"))));
//        feeds.add(new Feed("2张网络图片", new ArrayList<>(Arrays.asList("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered2.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered3.png"))));
//        feeds.add(new Feed("9张网络图片", new ArrayList<>(Arrays.asList("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered11.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered12.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered13.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered14.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered15.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered16.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered17.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered18.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered19.png"))));
//        feeds.add(new Feed("5张网络图片", new ArrayList<>(Arrays.asList("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered11.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered12.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered13.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered14.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered15.png"))));
//        feeds.add(new Feed("3张网络图片", new ArrayList<>(Arrays.asList("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered4.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered5.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered6.png"))));
//        feeds.add(new Feed("8张网络图片", new ArrayList<>(Arrays.asList("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered11.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered12.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered13.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered14.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered15.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered16.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered17.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered18.png"))));
//        feeds.add(new Feed("4张网络图片", new ArrayList<>(Arrays.asList("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered7.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered8.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered9.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered10.png"))));
//        feeds.add(new Feed("3张网络图片", new ArrayList<>(Arrays.asList("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered4.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered5.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered6.png"))));
//        mFeeds.add(new Feed("4张网络图片", new ArrayList<>(Arrays.asList("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered7.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered8.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered9.png", "http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/images/staggered10.png"))));
//        mFeedAdapter.setData(mFeeds);
    }
    
    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
//        Toast.makeText(getActivity(), "点击了item " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onRVItemLongClick(ViewGroup parent, View itemView, int position) {
//        Toast.makeText(getActivity(), "长按了item " + position, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
        mCurrentClickNpl = ninePhotoLayout;
        photoPreviewWrapper();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }


    private static final int PRC_PHOTO_PREVIEW = 1;

    private static final int RC_ADD_MOMENT = 1;
    /**
     * 图片预览，兼容6.0动态权限
     */
    @AfterPermissionGranted(PRC_PHOTO_PREVIEW)
    private void photoPreviewWrapper() {

        if (mCurrentClickNpl == null) {
            return;
        }

        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getActivity(), perms)) {
            File downloadDir = new File(Environment.getExternalStorageDirectory(), "SmallGdufeDownload");
            BGAPhotoPreviewActivity.IntentBuilder photoPreviewIntentBuilder = new BGAPhotoPreviewActivity.IntentBuilder(getActivity())
                    .saveImgDir(downloadDir); // 保存图片的目录，如果传 null，则没有保存图片功能

            if (mCurrentClickNpl.getItemCount() == 1) {
                // 预览单张图片
                photoPreviewIntentBuilder.previewPhoto(mCurrentClickNpl.getCurrentClickItem());
            } else if (mCurrentClickNpl.getItemCount() > 1) {
                // 预览多张图片
                photoPreviewIntentBuilder.previewPhotos(mCurrentClickNpl.getData())
                        .currentPosition(mCurrentClickNpl.getCurrentClickItemPosition()); // 当前预览图片的索引
            }
            startActivity(photoPreviewIntentBuilder.build());
        } else {
            EasyPermissions.requestPermissions(this, "图片预览需要以下权限:\n\n1.访问设备上的照片", PRC_PHOTO_PREVIEW, perms);
        }
    }
    public void onClick(View v) {
//        if (v.getId() == R.id.tv_moment_list_add) {
//            startActivityForResult(new Intent(this, MomentAddActivity.class), RC_ADD_MOMENT);
//        } else if (v.getId() == R.id.tv_moment_list_system) {
//            startActivity(new Intent(this, SystemGalleryActivity.class));
//        }
    }

    //写feed消息
    @OnClick(R.id.btn_social_feed_write) void writeFeed() {
        startActivity(new Intent(getActivity(), FeedAddActivity.class));
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && requestCode == RC_ADD_MOMENT) {
//            mMomentAdapter.addFirstItem(MomentAddActivity.getMoment(data));
//            mMomentRv.smoothScrollToPosition(0);
//        }
//    }
}
