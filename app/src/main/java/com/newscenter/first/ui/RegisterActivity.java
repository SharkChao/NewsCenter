package com.newscenter.first.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.newscenter.first.R;
import com.newscenter.first.annotation.ContentView;
import com.newscenter.first.base.BaseActivity;
import com.newscenter.first.databinding.ActivityRegisterBinding;
import com.newscenter.first.model.User;
import com.newscenter.first.util.CommonUtil;
import com.newscenter.first.viewmodel.RegisterViewModel;

@Route(path = "/center/RegisterActivity")
@ContentView(R.layout.activity_register)
public class RegisterActivity extends BaseActivity<RegisterViewModel>{

    private ActivityRegisterBinding mBinding;
    private EditText mEdtAccount;
    private EditText mEdtPassword;
    private Button mBtnRegister;
    private RegisterViewModel mViewModel;

    @Override
    public void initTitle() {
        isShowToolBar(false);
    }

    @Override
    public void initView(ViewDataBinding viewDataBinding) {
        mBinding = (ActivityRegisterBinding) viewDataBinding;
        mEdtAccount = mBinding.edtAccount;
        mEdtPassword = mBinding.edtPassword;
        mBtnRegister = mBinding.btnRegister;

    }

    @Override
    public void initData(ViewModel baseViewModel) {
        mViewModel = (RegisterViewModel) baseViewModel;
        mViewModel.registerData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if (user != null){
                    Toast.makeText(RegisterActivity.this, "注册成功!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    @Override
    protected void initEvent() {
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mEdtAccount.getText().toString();
                String password = mEdtPassword.getText().toString();
                if (CommonUtil.isStrEmpty(username)){
                    CommonUtil.showSnackBar(mEdtAccount,"账号不能为空");
                    return;
                }
                if (CommonUtil.isStrEmpty(password)){
                    CommonUtil.showSnackBar(mEdtAccount,"密码不能为空");
                    return;
                }
                User user = new User();
                user.setU_name(username);
                user.setU_password(password);
                mViewModel.getRegisterController(user);
            }
        });
    }

    @Override
    public void onRepsonseError(Throwable throwable) {
        super.onRepsonseError(throwable);
    }
}
