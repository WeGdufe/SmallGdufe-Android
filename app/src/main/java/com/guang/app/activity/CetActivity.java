package com.guang.app.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guang.app.R;
import com.guang.app.api.JwcApiFactory;
import com.guang.app.model.Cet;

import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by xiaoguang on 2017/2/22.
 */
public class CetActivity extends QueryActivity {
    private static JwcApiFactory factory = JwcApiFactory.getInstance();

    private int cetMaxScore = 710;
    private int cetFailScore = 425;
    private int cetListeningMaxScore = 249;
    private int cetReadingMaxScore = 429;
    private int cetWritingMaxScore = 212;

    //排名信息来源： http://www.cet.edu.cn/cet2011.htm
    private int cetBaodaoScore[] = {0, 330, 350, 370, 390, 410, 430, 450, 470, 490, 510, 530, 550, 570, 590, 610, 630, 650, 710};
    private int cet4Rank[] = {0, 1, 2, 4, 7, 11, 17, 25, 34, 44, 55, 65, 74, 83, 90, 95, 98, 99, 100};
    private int cet6Rank[] = {0, 1, 2, 4, 6, 10, 16, 23, 33, 42, 53, 66, 76, 84, 90, 95, 98, 99, 100};

    @Bind(R.id.ed_cet_zkzh)
    EditText edCetZkzh;
    @Bind(R.id.ed_cet_name)
    EditText edCetName;
    @Bind(R.id.tv_cet_level)
    TextView tvCetLevel;
    @Bind(R.id.tv_cet_score)
    TextView tvCetScore;
    @Bind(R.id.tv_cet_listen)
    TextView tvCetListen;
    @Bind(R.id.tv_cet_read)
    TextView tvCetRead;
    @Bind(R.id.tv_cet_writing)
    TextView tvCetWriting;
    @Bind(R.id.table_cet)
    TableLayout tabelCet;
    @Bind(R.id.tv_cet_rank)
    TextView tvCetRank;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addTitleBackBtn();
        setTitle(R.string.title_cet);
        setContentView(R.layout.activity_cet);

        //防止自动弹出键盘
        edCetName.clearFocus();
        tvCetLevel.requestFocus();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    @OnClick(R.id.btn_cet_query)
    void queryCet() {
        String zkzh = edCetZkzh.getText().toString().trim();
        String name = edCetName.getText().toString().trim();
        if(TextUtils.isEmpty(zkzh)||TextUtils.isEmpty(name)){
            Toast.makeText(this, "输完再查，别急", Toast.LENGTH_SHORT).show();
            return;
        }
        factory.getCet(zkzh, name, new Observer<Cet>() {
            @Override
            public void onSubscribe(Disposable d) {
                startLoadingProgess();
            }

            @Override
            public void onNext(Cet value) {
                tvCetLevel.setText(value.getLevel());
                int score = Integer.parseInt(value.getScore());
                if (score < cetFailScore) {
                    tvCetScore.setTextColor(getResources().getColor(R.color.goal_item_failed_color));
                }
                tvCetScore.setText(value.getScore() + " / " + cetMaxScore);
                tvCetListen.setText(value.getListenScore() + " / " + cetListeningMaxScore);
                tvCetRead.setText(value.getReadScore() + " / " + cetReadingMaxScore);
                tvCetWriting.setText(value.getWriteScore() + " / " + cetWritingMaxScore);
                tabelCet.setVisibility(View.VISIBLE);

                for (int i = 0; i < cetBaodaoScore.length; i++) {
                    if (score >= cetBaodaoScore[i] && score <= cetBaodaoScore[i + 1]) {
                        if (value.getLevel().substring(2,3).equals('六')) {
                            Toast.makeText(CetActivity.this, value.getLevel().substring(2, 3), Toast.LENGTH_SHORT).show();
                            tvCetRank.setText("前" + cet6Rank[i] + "%~" + cet6Rank[i + 1] + "%");
                        }else{
                            tvCetRank.setText("前" + cet4Rank[i] + "%~" + cet4Rank[i + 1] + "%");
                        }
                    }
                }
                if(score == 0){
                    Toast.makeText(CetActivity.this, "零分为总分小于220分或者有违规行为", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(CetActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                stopLoadingProgess();
            }
        });
    }

}
