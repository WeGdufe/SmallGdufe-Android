package com.guang.app.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.guang.app.AppConfig;
import com.guang.app.R;
import com.guang.app.activity.AboutActivity;
import com.guang.app.activity.CardHistoryActivity;
import com.guang.app.activity.FeedbackActivity;
import com.guang.app.activity.LoginActivity;
import com.guang.app.api.CardApiFactory;
import com.guang.app.api.JwApiFactory;
import com.guang.app.model.BasicInfo;
import com.guang.app.model.CardBasic;
import com.guang.app.model.Schedule;
import com.guang.app.util.FileUtils;
import com.guang.app.util.drcom.DrcomFileUtils;
import com.umeng.analytics.MobclickAgent;

import org.litepal.crud.DataSupport;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MeFragment extends Fragment {
    private JwApiFactory factory = JwApiFactory.getInstance();
    private CardApiFactory cardFactory = CardApiFactory.getInstance();
    private static String mCardNum;         //校园卡卡号，获取校园卡余额时赋值
    public static final long localId = 1; //用户基本信息存在数据库的id

    @Bind(R.id.tv_me_icon)
    ImageView tvMeIcon;
    @Bind(R.id.tv_me_sno)
    TextView tvMeSno;
    @Bind(R.id.tv_me_name)
    TextView tvMeName;
    @Bind(R.id.tv_me_class)
    TextView tvMeClass;
    @Bind(R.id.tv_me_cardnum)
    TextView tvMeCardNum;
//    @Bind(R.id.tv_me_update)
//    TextView tvMeUpdate;
//

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        tvMeSno.setText(AppConfig.sno);

        BasicInfo basicInfo = DataSupport.find(BasicInfo.class,localId);
        if(null != basicInfo) {
            setBasicInfo4View(basicInfo);
        }else{
            queryBasicInfo();
        }
        queryCurrentCash();
        return view;
    }

    //获取用户基本信息，姓名班级等
    private void queryBasicInfo(){
        factory.getBasicInfo(new Observer<BasicInfo>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(BasicInfo value) {
                value.setId(localId);
                value.save();
                setBasicInfo4View(value);
            }
            @Override
            public void onError(Throwable e) {
                LogUtils.e(e.toString());
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onComplete() {

            }
        });
    }

    //校园卡余额
    private void queryCurrentCash(){
        cardFactory.getCurrentCash(new Observer<CardBasic>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(CardBasic value) {
                if(null != value) {
                    tvMeCardNum.setText("￥" + value.getCash());
                    mCardNum = value.getCardNum();
                }
            }
            @Override
            public void onError(Throwable e) {
                LogUtils.e(e.toString());
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onComplete() {

            }
        });
    }

    private void setBasicInfo4View(BasicInfo value) {
        if (value.getSex().equals("女")){
            tvMeName.setTextColor(getResources().getColor(R.color.pink));
        }
        tvMeName.setText(value.getName());
        tvMeClass.setText(value.getClassroom());
        String iconUrl = AppConfig.Avator_URL+value.getNamePy().charAt(0);
//        String iconUrl = AppConfig.Avator_URL+value.getNamePy().charAt(0);
//        tvMeIcon.setImageDrawable(Drawable.createFromResourceStream(getResources().getDrawable(R.mipmap.avatar_H)));
        tvMeIcon.setBackgroundResource(R.mipmap.avatar_h);
//        tvMeIcon.setImageDrawable(Drawable.c(R.mipmap.avatar_H));

//        tvMeIcon
    }
    @OnClick(R.id.layout_me_cashhistory) void showConsumeToday(){
        if(TextUtils.isEmpty(mCardNum)){
            Toast.makeText(getActivity(), "交易记录获取异常", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(getActivity(), CardHistoryActivity.class);
        intent.putExtra(CardHistoryActivity.intentCardNum,mCardNum);
        startActivity(intent);
    }

    private int mSelectedPage = AppConfig.DefaultPage.HOME; //选择默认页的对话框的当前选择项
    @OnClick(R.id.tv_me_default_page) void clickDefaultPage(){
        final Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.mipmap.me_default_page);
        builder.setTitle("打开APP后直达该页面");

        final String[] pages = {
                context.getString(R.string.tab_home), context.getString(R.string.tab_features),
                context.getString(R.string.tab_settings),context.getString(R.string.menu_drcom) };

        int pageIndex = AppConfig.defaultPage;   //默认选择哪个
        if(pageIndex == AppConfig.DefaultPage.DRCOM){
            pageIndex = 3;
        }
        mSelectedPage = pageIndex;  //当前选择
        //点击item选择时的事件
        builder.setSingleChoiceItems(pages, pageIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSelectedPage = which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(mSelectedPage == 3){
                    mSelectedPage = AppConfig.DefaultPage.DRCOM;
                }
                AppConfig.defaultPage = mSelectedPage;
                FileUtils.setStoredDefaultPage(context,AppConfig.defaultPage);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    @OnClick(R.id.tv_me_about) void clickAbout(){
        startActivity(new Intent(getActivity(), AboutActivity.class));
    }
    @OnClick(R.id.tv_me_feedback) void feedback(){
        startActivity(new Intent(getActivity(), FeedbackActivity.class));
    }

    @OnClick(R.id.tv_me_exit) void logout() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
        FileUtils.expireStoredAccount(getActivity());//防止点退出后重新打开APP会进入旧帐号
        DrcomFileUtils.expireStoredAccount(getActivity());  //drcom信息
        DataSupport.deleteAll(Schedule.class);  //清空课程表
        DataSupport.deleteAll(BasicInfo.class);
        MobclickAgent.onProfileSignOff();//友盟统计用户退出
        getActivity().finish();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
