package com.guang.app.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.guang.app.R;
import com.guang.app.adapter.BookAdapter;
import com.guang.app.api.OpacApiFactory;
import com.guang.app.model.Book;
import com.guang.app.model.StrObjectResponse;
import com.guang.app.util.CalcUtils;
import com.guang.app.widget.RefreshActionItem;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * 当前借阅和历史借阅共用Activity，界面也一样，Intent参数去区分
 * Created by xiaoguang on 2017/2/18.
 */
public class BookActivity extends QueryActivity {
    private static OpacApiFactory factory = OpacApiFactory.getInstance();
    private BookAdapter mAdapter;
    public static final String doWhat = "what";
    public static final int doBorrowedBook = 1;
    public static final int doCurrentBook = 0;
    public String mVerifyCodeBase64Str = ""; //验证码的base64字符串

    @Bind(R.id.common_recycleView) RecyclerView mRecyclerView;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addTitleBackBtn();
        setContentView(R.layout.common_listview);
        ButterKnife.bind(this);
        initAdapter();
        initEvent();
    }

    @Override
    protected void loadData() {
        startLoadingProgess();
        int from = getIntent().getIntExtra(doWhat,0);
        if(from == BookActivity.doBorrowedBook) {
            setTitle(R.string.title_borrowedBook);
            factory.getBorrowedBook(new Observer<List<Book>>() {
                @Override
                public void onSubscribe(Disposable d) {
                    startLoadingProgess();
                }

                @Override
                public void onNext(List<Book> value) {
                    Toast.makeText(BookActivity.this, "共借阅过" + value.size() + "本书", Toast.LENGTH_SHORT).show();
                    mAdapter.cleanData();
                    mAdapter.addData(value);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(BookActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    BookActivity.this.finish();
                }

                @Override
                public void onComplete() {
                    mAdapter.isUseEmpty(true);
                    stopLoadingProgess();
                }
            });
        }else{
            setTitle(R.string.title_currentBook);
            factory.getCurrentBook(new Observer<List<Book>>() {
                @Override
                public void onSubscribe(Disposable d) {
                    startLoadingProgess();
                }

                @Override
                public void onNext(List<Book> value) {
                    mAdapter.cleanData();
                    mAdapter.addData(value);
                    //刷新续借按钮需要mRecyclerView.setAdapter
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(BookActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    BookActivity.this.finish();
                }

                @Override
                public void onComplete() {
                    mAdapter.isUseEmpty(true);
                    stopLoadingProgess();
                }
            });
        }
    }

    private void initAdapter() {
        mAdapter = new BookAdapter(R.layout.book_listitem);
        mAdapter.setEmptyView(R.layout.layout_empty_data, (ViewGroup) mRecyclerView.getParent());
        mAdapter.isUseEmpty(false); //避免一开始就出现空页面
        mAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    private void initEvent() {
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener( ) {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, final int position) {

                final AlertDialog.Builder verifyCodeBuilder = new AlertDialog.Builder(BookActivity.this);
                LayoutInflater layoutInflater = BookActivity.this.getLayoutInflater();
                View customLayout = layoutInflater.inflate(R.layout.view_verifycode, null);
                verifyCodeBuilder.setView(customLayout);
                verifyCodeBuilder.setTitle("输个验证码咯");
                final ImageView imgCode = (ImageView)customLayout.findViewById(R.id.img_verifycode);
                final EditText edCode = (EditText)customLayout.findViewById(R.id.ed_verifycode_input);
                verifyCodeBuilder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                //续借验证码输入对话框的确定按钮
                verifyCodeBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String code = edCode.getText().toString();
                        if(TextUtils.isEmpty(code)){
                            Toast.makeText(BookActivity.this, "别闹", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //续借
                        Book item = mAdapter.getItem(position);
                        LogUtils.e(item.getBarId()+" "+ item.getCheckId()+" "+ code);
                        factory.renewBook(item.getBarId(), item.getCheckId(), code, new Observer<StrObjectResponse>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }
                            @Override
                            public void onNext(StrObjectResponse value) {
                                loadData();
                                LogUtils.e(value.getData());
                                Toast.makeText(BookActivity.this, value.getData(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                LogUtils.e(e.getMessage());
                                Toast.makeText(BookActivity.this, "续借失败："+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onComplete() {
                            }
                        });
                    }
                });
                verifyCodeBuilder.create().show();
//               mVerifyCodeBase64Str = "R0lGODdhPAAkAIAAAAQCBNTWzCwAAAAAPAAkAAACcoyPqcvtD6OctNqLs968+w+G4kiW5omm6sq27rsBgCM3cs3g123zCq5D3GaWYY4YDASTQGLRKYQykVCltCpJGpZYY4K7w1qjX2qZ/FyAD+vtNXx0n9nVJkZLx9t/vsoQP3YEGAhTaHiImKi4yNjo+FhRAAA7";

                //获取验证码图片
                factory.getRenewBookVerifyCode(new Observer<StrObjectResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(StrObjectResponse value) {
                        LogUtils.e(value);
                        LogUtils.e(value.getData());
                        mVerifyCodeBase64Str = value.getData();
                        imgCode.setImageBitmap(CalcUtils.base64String2Bitmap(mVerifyCodeBase64Str));
                    }
                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(e.getMessage());
                        Toast.makeText(BookActivity.this, "验证码获取错误:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onComplete() {
                    }
                });
            }
        });
    }

    @Override
    public void onRefresh(RefreshActionItem refreshActionItem) {
        loadData();
        super.onRefresh(refreshActionItem);
    }
}