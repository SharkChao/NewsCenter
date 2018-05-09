package com.newscenter.first.ui;

import android.arch.lifecycle.ViewModel;
import android.content.pm.PackageManager;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.newscenter.first.R;
import com.newscenter.first.annotation.ContentView;
import com.newscenter.first.base.BaseActivity;
import com.newscenter.first.databinding.ActivitySettingBinding;
import com.newscenter.first.util.PermisionUtils;
import com.newscenter.first.viewmodel.base.BaseViewModel;

/**
 * Created by Admin on 2018/2/8.
 */
@ContentView(R.layout.activity_setting)
public class SettingActivity extends BaseActivity<BaseViewModel> {

    LinearLayout llAboutUs;
    LinearLayout llCheckUpdate;
    LinearLayout llClear;

    @Override
    public void initTitle() {
        isShowToolBar(true);
        setCenterTitle("设置");
    }

    @Override
    public void initView(ViewDataBinding viewDataBinding) {
        ActivitySettingBinding binding = (ActivitySettingBinding) viewDataBinding;
         llAboutUs = binding.llAboutUs;
         llCheckUpdate = binding.llCheckUpdate;
         llClear = binding.llClear;
    }

    @Override
    public void initData(ViewModel baseViewModel) {

    }

    @Override
    protected void initEvent() {
        llAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutUsActivity.startAboutUsActivity(SettingActivity.this);
            }
        });
        llCheckUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean per = PermisionUtils.verifyStoragePermissions(SettingActivity.this);
                if (per){
                    Toast.makeText(SettingActivity.this, "您的软件已经是最新版本啦", Toast.LENGTH_SHORT).show();
                }
            }
        });
        llClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActivity.this, "缓存已清除", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "您的软件已经是最新版本啦", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "您的软件将不能升级,请在[设置]-[授权管理]中打开", Toast.LENGTH_SHORT).show();
        }
    }
}
