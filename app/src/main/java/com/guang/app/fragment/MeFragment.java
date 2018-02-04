package com.guang.app.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.guang.app.api.WorkApiFactory;
import com.guang.app.model.BasicInfo;
import com.guang.app.model.CardBasic;
import com.guang.app.model.Schedule;
import com.guang.app.model.StrObjectResponse;
import com.guang.app.util.FileUtils;
import com.guang.app.util.ShareUtils;
import com.guang.app.util.TimeUtils;
import com.guang.app.util.drcom.DrcomFileUtils;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.umeng.analytics.MobclickAgent;

import org.litepal.crud.DataSupport;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public class MeFragment extends Fragment {
    private static JwApiFactory factory = JwApiFactory.getInstance();
    private static CardApiFactory cardFactory = CardApiFactory.getInstance();
    private static WorkApiFactory workApiFactory = WorkApiFactory.getInstance();

    private static String mCardNum;         //校园卡卡号，获取校园卡余额时赋值

    @Bind(R.id.tv_me_icon)
    ImageView tvMeIcon;
    @Bind(R.id.tv_me_sno)
    TextView tvMeSno;
    @Bind(R.id.tv_me_name)
    TextView tvMeName;
    @Bind(R.id.tv_me_class)
    TextView tvMeClass;
    @Bind(R.id.tv_me_cash)
    TextView tvMeCash;
//    @Bind(R.id.tv_me_update)
//    TextView tvMeUpdate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        getActivity().setTitle(R.string.app_name);

        tvMeSno.setText(AppConfig.sno);

        BasicInfo basicInfo = DataSupport.findFirst(BasicInfo.class);
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
                DataSupport.deleteAll(BasicInfo.class);
                value.save();
                setBasicInfo4View(value);
            }
            @Override
            public void onError(Throwable e) {
                if(e != null && !TextUtils.isEmpty(e.getMessage())) {
                    LogUtils.e(e.toString());
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
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
                if(null != value && !TextUtils.isEmpty(value.getCash())) {
                    tvMeCash.setText("￥" + value.getCash());
                    mCardNum = value.getCardNum();
                }else{
                    tvMeCash.setText("获取失败");
                }
            }
            @Override
            public void onError(Throwable e) {
                if(e != null && !TextUtils.isEmpty(e.getMessage())) {
                    LogUtils.e(e.toString());
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                tvMeCash.setText("获取失败");
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

        Bitmap bitmap = FileUtils.loadAvatarBitmap(getActivity());
        if(bitmap != null){ //读取本地图片头像
            tvMeIcon.setImageBitmap(bitmap);
        }else{              //网络加载头像
            workApiFactory.getAvatarIcon(""+value.getName().charAt(0), new Observer<ResponseBody>() {
                @Override
                public void onSubscribe(Disposable d) {
                }
                @Override
                public void onNext(ResponseBody value) {
                    Bitmap bitmap = BitmapFactory.decodeStream(value.byteStream());
                    FileUtils.saveAvatarImage(getActivity(),bitmap);
                    tvMeIcon.setImageBitmap(bitmap);
                }
                @Override
                public void onError(Throwable e) {
                    if(tvMeIcon != null) {  //模拟器测试时的不知名原因 低概率出现
                        tvMeIcon.setBackgroundResource(R.mipmap.avatar_h);
                    }
                }
                @Override
                public void onComplete() {
                }
            });
        }

        //如果是公历生日那天打开，会弹窗表示生日祝福
        if( !TextUtils.isEmpty(value.getBirthday())
                && value.getBirthday().length() == 8
                && value.getBirthday().substring(4, 8) .equals( TimeUtils.getDateStringWithFormat("MMdd"))){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage( " 生日快乐 (๑•̀ㅂ•́)و✧");
            builder.setTitle(value.getName());
            builder.setPositiveButton("蟹蟹",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                }
            });
            builder.create().show();;
        }

        if(AppConfig.schoolmateSno.equals(AppConfig.sno)) {
            tvMeSno.setText("88888888888");
            tvMeName.setText("校友");
            tvMeClass.setText("广东财经大学毕业生班");
        }

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

    @OnClick(R.id.tv_me_share) void clickShare() {
        android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(getActivity());
        builder.setIcon(R.mipmap.app_icon);
        builder.setTitle("爱分享的人运气总不会差");
        builder.setItems(R.array.shareList, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                int scene = SendMessageToWX.Req.WXSceneSession;
                if(which == 1) {
                    scene = SendMessageToWX.Req.WXSceneTimeline;
                }else if(which == 2){
                    scene = SendMessageToWX.Req.WXSceneFavorite;
                }
                ShareUtils.install(getActivity());
                ShareUtils.shareWeb(getActivity(),getResources().getString(R.string.app_name)+"，你的掌上校园伴侣","广财专用APP，学生开发，课表饭卡校园网，一样不少",AppConfig.WXSHARE_URL, scene);
            }
        });
        builder.create().show();
    }

    @OnClick(R.id.tv_me_about) void clickAbout(){
        startActivity(new Intent(getActivity(), AboutActivity.class));
    }
    @OnClick(R.id.tv_me_feedback) void feedback(){
        startActivity(new Intent(getActivity(), FeedbackActivity.class));
    }

    @OnClick(R.id.tv_me_exit) void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("确定退出（切换）账号？");
        builder.setMessage("通常情况下按返回键就好了");
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                startActivity(new Intent(getActivity(), LoginActivity.class));
                FileUtils.expireStoredAccount(getActivity());//防止点退出后重新打开APP会进入旧帐号
//                FileUtils.expireTipsNeverShowAgain(getActivity());/// 重新登陆/切换用户后登陆提示不给看到
                DrcomFileUtils.expireStoredAccount(getActivity());  //drcom信息
                FileUtils.clearAvatarImage(getActivity());  //清除头像
                DataSupport.deleteAll(Schedule.class);  //清空课程表
                DataSupport.deleteAll(BasicInfo.class);
                MobclickAgent.onProfileSignOff();//友盟统计用户退出
                //服务器端的退出登陆，这个成功与否不影响上面操作
                workApiFactory.allLogout(new Observer<StrObjectResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(StrObjectResponse value) {
                        //不需要做处理
                    }
                    @Override
                    public void onError(Throwable e) {
                        if(null != e.getMessage()){
                            LogUtils.e(e.getMessage());
                        }
                    }
                    @Override
                    public void onComplete() {
                    }
                });

                getActivity().finish();
            }
        });
        builder.create().show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
