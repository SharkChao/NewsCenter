package com.newscenter.first.ui;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.newscenter.first.R;
import com.newscenter.first.annotation.ContentView;
import com.newscenter.first.base.BaseActivity;
import com.newscenter.first.model.User;
import com.newscenter.first.util.ActivityStack;
import com.newscenter.first.util.CommonUtil;
import com.newscenter.first.util.Constants;
import com.newscenter.first.util.FileRequestBody;
import com.newscenter.first.util.RetrofitCallBack;
import com.newscenter.first.viewmodel.MyInfoViewModel;
import com.newscenter.first.viewmodel.base.BaseViewModel;
import com.newscenter.first.views.PhotoPopupWindow;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by Admin on 2018/2/8.
 */
@ContentView(R.layout.wn_my_info_activity)
public class MyInfoActivity extends BaseActivity<MyInfoViewModel> implements TakePhoto.TakeResultListener, InvokeListener {

    Button btnLoginOut;
    TextView tvName;
    TextView tvSex;
    TextView tvPhoneNumber;
    TextView tvJobTitle;
    private User mUser;
    private PhotoPopupWindow mPhotoPopupWindow;
    private TakePhoto takePhoto;
    private CompressConfig mCompressConfig;
    private InvokeParam invokeParam;
    private SimpleDraweeView mDraweeView;
    private SimpleDraweeView mDraweeViewActivity;
    private String picUrl;
    private MyInfoViewModel mViewModel;
    private AlertDialog mAlertDialog;

    @Override
    public void initTitle() {
        isShowToolBar(true);
        setCenterTitle("基础信息");
    }

    @Override
    public void initView(ViewDataBinding viewDataBinding) {
        tvName = findViewById(R.id.tvName);
        tvSex = findViewById(R.id.tvSex);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvJobTitle = findViewById(R.id.tvJobTitle);
        mDraweeViewActivity = findViewById(R.id.ivUserAvatar);
        btnLoginOut = findViewById(R.id.btnLoginOut);
    }

    @Override
    public void initData(ViewModel baseViewModel) {
        mViewModel = (MyInfoViewModel) baseViewModel;
        mViewModel.userData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if (user != null) {
                    CommonUtil.setShardPString(Constants.IS_LOGIN, new Gson().toJson(user));
                    tvName.setText(CommonUtil.isStrEmpty(user.getXm()) ? "未知" : user.getXm());
                    tvSex.setText(CommonUtil.isStrEmpty(user.getSex()) ? "未知" : user.getSex());
                    tvJobTitle.setText(CommonUtil.isStrEmpty(user.getJob_title()) ? "未知" : user.getJob_title());
                    tvPhoneNumber.setText(CommonUtil.isStrEmpty(user.getPhone_number()) ? "未知" : user.getPhone_number());
                    if (!CommonUtil.isStrEmpty(user.getPic_url())) {
                        mDraweeViewActivity.setImageURI(Constants.BASE_URL+user.getPic_url());
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String json = CommonUtil.getShardPStringByKey(Constants.IS_LOGIN);
        if (!CommonUtil.isStrEmpty(json)) {
            mUser = new Gson().fromJson(json, User.class);
        }
        if (mUser != null) {
            tvName.setText(CommonUtil.isStrEmpty(mUser.getXm()) ? "未知" : mUser.getXm());
            tvSex.setText(CommonUtil.isStrEmpty(mUser.getSex()) ? "未知" : mUser.getSex());
            tvJobTitle.setText(CommonUtil.isStrEmpty(mUser.getJob_title()) ? "未知" : mUser.getJob_title());
            tvPhoneNumber.setText(CommonUtil.isStrEmpty(mUser.getPhone_number()) ? "未知" : mUser.getPhone_number());
            if (!CommonUtil.isStrEmpty(mUser.getPic_url())) {
                mDraweeViewActivity.setImageURI(Constants.BASE_URL+mUser.getPic_url());
            }
        }
    }

    @Override
    protected void initEvent() {
        btnLoginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyInfoActivity.this);
                builder.setTitle("退出");
                builder.setMessage("您确定要退出吗?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityStack.create().finishAll();
                        ARouter.getInstance()
                                .build("/center/LoginActivity")
                                .navigation();
                        CommonUtil.setShardPString(Constants.IS_LOGIN, "");
                    }
                });
                builder.setNegativeButton("我再想想", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
            }
        });

        setRightTitle("编辑", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyInfoActivity.this);
                View content = LayoutInflater.from(MyInfoActivity.this).inflate(R.layout.activity_myinfo_edit, null);
                RelativeLayout rvPhoto = content.findViewById(R.id.rvPhoto);
                mDraweeView = content.findViewById(R.id.ivUserAvatar);
                EditText etXM = content.findViewById(R.id.edit_name);
                EditText etSex = content.findViewById(R.id.edit_sex);
                EditText etPhone = content.findViewById(R.id.edit_phone);
                EditText etTitle = content.findViewById(R.id.edit_title);
                etXM.setText(CommonUtil.isStrEmpty(mUser.getXm()) ? "" : mUser.getXm());
                etSex.setText(CommonUtil.isStrEmpty(mUser.getSex()) ? "" : mUser.getSex());
                etTitle.setText(CommonUtil.isStrEmpty(mUser.getJob_title()) ? "" : mUser.getJob_title());
                etPhone.setText(CommonUtil.isStrEmpty(mUser.getPhone_number()) ? "" : mUser.getPhone_number());

                mDraweeView.setImageURI(Constants.BASE_URL+mUser.getPic_url());
                rvPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPhotoPopupWindow = new PhotoPopupWindow(MyInfoActivity.this, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPhotoPopupWindow.dismiss();
                                // 进入相册选择
                                getTakePhoto().onPickFromGallery();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPhotoPopupWindow.dismiss();
                                // 拍照
                                File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
                                if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
                                Uri imageUri = Uri.fromFile(file);
                                getTakePhoto().onPickFromCapture(imageUri);
                            }
                        });
                        View rootView = LayoutInflater.from(MyInfoActivity.this)
                                .inflate(R.layout.wn_my_info_activity, null);
                        mPhotoPopupWindow.showAtLocation(content,
                                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    }

                });
                builder.setView(content);
                builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (CommonUtil.isStrEmpty(etXM.getText().toString()) || CommonUtil.isStrEmpty(etPhone.getText().toString()) || CommonUtil.isStrEmpty(etSex.getText().toString()) || CommonUtil.isStrEmpty(etTitle.getText().toString())) {
                            Toast.makeText(MyInfoActivity.this, "不能为空!", Toast.LENGTH_SHORT).show();
                            Field field = null;
                            try {
                                field = mAlertDialog.getClass().getSuperclass().getDeclaredField("mShowing");
                                field.setAccessible(true);
                                field.set(mAlertDialog, false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else if (CommonUtil.isStrEmpty(picUrl)) {
                            Toast.makeText(MyInfoActivity.this, "请先选择一张头像!", Toast.LENGTH_SHORT).show();
                            Field field = null;
                            try {
                                field = mAlertDialog.getClass().getSuperclass().getDeclaredField("mShowing");
                                field.setAccessible(true);
                                field.set(mAlertDialog, false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                Field field = mAlertDialog.getClass().getSuperclass().getDeclaredField("mShowing");
                                field.setAccessible(true);
                                field.set(mAlertDialog, true);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            mUser.setXm(etXM.getText().toString());
                            mUser.setSex(etSex.getText().toString());
                            mUser.setPhone_number(etPhone.getText().toString());
                            mUser.setJob_title(etTitle.getText().toString());
                            mUser.setPic_url(picUrl);
                            LinkedHashMap<String, RequestBody> params = new LinkedHashMap<>();
                            File file = new File(picUrl);
                            RetrofitCallBack<ResponseBody> call = new RetrofitCallBack<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                }
                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                }
                                @Override
                                public void onLoading(long total, long progress) {
                                    super.onLoading(total, progress);
                                }
                            };
                            params.put("photos\"; filename=\"" + file.getName(), new FileRequestBody<>(RequestBody.create(MediaType.parse("image/*"), file), call));
                            params.put("user",RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(mUser)));
                            mViewModel.saveInfoController(params);
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                mAlertDialog = builder.create();
                mAlertDialog.show();
            }
        });
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        mCompressConfig = new CompressConfig.Builder().setMaxSize(50 * 1024).setMaxPixel(800).create();
        takePhoto.onEnableCompress(mCompressConfig, true);
        return takePhoto;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void takeSuccess(TResult result) {
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_FILE_SCHEME)
                .path(result.getImage().getCompressPath())
                .build();
        mDraweeView.setImageURI(uri);
        picUrl = result.getImage().getCompressPath();
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }
}
