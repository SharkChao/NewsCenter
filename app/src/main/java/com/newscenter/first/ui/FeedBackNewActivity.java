package com.newscenter.first.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.newscenter.first.R;
import com.newscenter.first.annotation.ContentView;
import com.newscenter.first.base.BaseActivity;
import com.newscenter.first.databinding.ActivityFeedbackNewBinding;
import com.newscenter.first.model.FeedBack;
import com.newscenter.first.model.HttpResult;
import com.newscenter.first.model.User;
import com.newscenter.first.util.CommonUtil;
import com.newscenter.first.util.Constants;
import com.newscenter.first.viewmodel.FeebackNewViewModel;
import com.newscenter.first.viewmodel.base.BaseViewModel;

/**
 * Created by Admin on 2018/2/8.
 */
@Route(path = "/center/FeedBackNewActivity")
@ContentView(R.layout.activity_feedback_new)
public class FeedBackNewActivity extends BaseActivity<FeebackNewViewModel>{
    private static final String FEEDBACK = "feedback";
    Button btnCancel;
    Button btnSave;
    EditText etFeedBack;
    TextView tvFeedBack;
    private FeedBack mFeedBack;
    private FeebackNewViewModel mViewModel;

    @Override
    public void initTitle() {
        isShowToolBar(true);
        setCenterTitle("我的反馈");
    }

    @Override
    public void initView(ViewDataBinding viewDataBinding) {
        ActivityFeedbackNewBinding binding = (ActivityFeedbackNewBinding) viewDataBinding;
         btnCancel = binding.btnCancel;
         btnSave = binding.btnSave;
         etFeedBack = binding.etFeedBack;
         tvFeedBack = binding.tvFeedBack;
    }

    @Override
    public void initData(ViewModel baseViewModel) {
        mFeedBack = (FeedBack) getIntent().getExtras().getSerializable(FEEDBACK);
        if (CommonUtil.isNotEmpty(mFeedBack)){
            etFeedBack.setText(mFeedBack.getFknr());
            tvFeedBack.setText(CommonUtil.isStrEmpty(mFeedBack.getFknr())?"暂无回复内容，我们正在紧急改进中":"回复内容： "+mFeedBack.getHfnr());
            etFeedBack.setEnabled(false);
            btnSave.setVisibility(View.GONE);
        }else{
            tvFeedBack.setVisibility(View.INVISIBLE);
        }

        mViewModel = (FeebackNewViewModel) baseViewModel;
        mViewModel.registerData.observe(this, new Observer<HttpResult>() {
            @Override
            public void onChanged(@Nullable HttpResult httpResult) {
                if (httpResult != null && httpResult.getCode() > 0){
//                    CommonUtil.showSnackBar(etFeedBack,"谢谢你的反馈，我们将积极改进");
                    Toast.makeText(FeedBackNewActivity.this, "谢谢你的反馈，我们将积极改进", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    @Override
    protected void initEvent() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = etFeedBack.getText().toString();
                if (CommonUtil.isStrEmpty(content) || content.length() > 300){
                    CommonUtil.showSnackBar(etFeedBack,"你还未填写反馈或者字数太多啦");
                    return;
                }
                String json = CommonUtil.getShardPStringByKey(Constants.IS_LOGIN);
                User user = new Gson().fromJson(json,User.class);
                mViewModel.setFeedBackNewController(user.getU_name(),content);
            }
        });
    }
}
