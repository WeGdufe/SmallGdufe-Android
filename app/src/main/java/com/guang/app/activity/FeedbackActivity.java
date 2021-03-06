package com.guang.app.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.guang.app.R;
import com.guang.app.api.WorkApiFactory;
import com.guang.app.model.StrObjectResponse;
import com.guang.app.util.SystemUtil;

import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 反馈
 * Created by xiaoguang on 2017/2/13.
 */
public class FeedbackActivity extends QueryActivity {

    @Bind(R.id.ed_feedback_contact)
    EditText edFeedbackContact;
    @Bind(R.id.ed_feedback_content)
    EditText edFeedbackContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_feedback);
        addTitleBackBtn();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean shouldHideLoadingIcon() {
        return true;
    }

    private long oldClickSubmitTimes;
    @OnClick(R.id.btn_feedback_submit) void submitFeedback() {
        long curTimes = System.currentTimeMillis();
        if( (curTimes - oldClickSubmitTimes)/1000 <= 1){
            Toast.makeText(this, "请不要频繁提交", Toast.LENGTH_SHORT).show();
            return;
        }
        oldClickSubmitTimes = curTimes;

        String contact = edFeedbackContact.getText().toString();
        String content = edFeedbackContent.getText().toString();
        if(TextUtils.isEmpty(content)){
            Toast.makeText(this, "内容还没填呢", Toast.LENGTH_SHORT).show();
            return;
        }
        WorkApiFactory factory = WorkApiFactory.getInstance();

        factory.submitFeedback(contact, content,SystemUtil.getDeviceBrand(),SystemUtil.getSystemModel(),
                SystemUtil.getSystemVersion(),new Observer<StrObjectResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(StrObjectResponse value) {
                Toast.makeText(FeedbackActivity.this, "非常感谢", Toast.LENGTH_SHORT).show();
                edFeedbackContent.setText("");
                edFeedbackContact.setText("");
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(FeedbackActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
            }
        });
    }
}
