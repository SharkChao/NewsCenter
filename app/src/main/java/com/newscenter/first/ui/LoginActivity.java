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
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.newscenter.first.R;
import com.newscenter.first.annotation.ContentView;
import com.newscenter.first.base.BaseActivity;
import com.newscenter.first.databinding.ActivityLoginBinding;
import com.newscenter.first.model.HttpResult;
import com.newscenter.first.model.User;
import com.newscenter.first.util.CommonUtil;
import com.newscenter.first.util.Constants;
import com.newscenter.first.viewmodel.LoginViewModel;

@ContentView(R.layout.activity_login)
@Route(path = "/center/LoginActivity")
public class LoginActivity extends BaseActivity<LoginViewModel>{

    private EditText mEdName;
    private EditText mEdPassword;
    private Button mBtnLogin;
    private LoginViewModel mLoginViewModel;
    private TextView mBtnRegister;

    @Override
    public void initTitle() {
        isShowToolBar(false);
    }

    @Override
    public void initView(ViewDataBinding viewDataBinding) {
        ActivityLoginBinding binding = (ActivityLoginBinding) viewDataBinding;
        mEdName = binding.edtAccount;
        mEdPassword = binding.edtPassword;
        mBtnLogin = binding.btnLogin;
        mBtnRegister = binding.btnRegister;

    }

    @Override
    public void initData(ViewModel baseViewModel) {
        mLoginViewModel = (LoginViewModel) baseViewModel;
        mLoginViewModel.userData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                    if (user != null){
                        Toast.makeText(LoginActivity.this, "登录成功!", Toast.LENGTH_SHORT).show();
                        CommonUtil.setShardPString(Constants.IS_LOGIN,new Gson().toJson(user));
                        ARouter.getInstance()
                                .build("/center/HomeActivity")
                                .navigation();
                        finish();
                    }else {
                        Toast.makeText(LoginActivity.this, "登录失败,请重新尝试!", Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }

    @Override
    protected void initEvent() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mEdName.getText().toString();
                String password = mEdPassword.getText().toString();
                if (CommonUtil.isStrEmpty(name)){
                    CommonUtil.showSnackBar(mEdName,"请先输入用户名");
                    return;
                }
                if (CommonUtil.isStrEmpty(password)){
                    CommonUtil.showSnackBar(mEdPassword,"请输入密码");
                    return;
                }
                User user = new User();
                user.setU_name(name);
                user.setU_password(password);
                mLoginViewModel.loginController(user);
            }
        });
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance()
                        .build("/center/RegisterActivity")
                        .navigation();
            }
        });
    }
}
