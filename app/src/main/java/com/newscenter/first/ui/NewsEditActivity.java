package com.newscenter.first.ui;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import com.newscenter.first.databinding.ActivityEditNewsBinding;
import com.newscenter.first.model.HttpResult;
import com.newscenter.first.model.KeyValueObject;
import com.newscenter.first.model.News;
import com.newscenter.first.util.CommonUtil;
import com.newscenter.first.util.Constants;
import com.newscenter.first.util.DropDownView;
import com.newscenter.first.util.FileRequestBody;
import com.newscenter.first.util.RetrofitCallBack;
import com.newscenter.first.util.YearMonthDatePickerDialog;
import com.newscenter.first.viewmodel.NewsViewModel;
import com.newscenter.first.viewmodel.base.BaseViewModel;
import com.newscenter.first.views.PhotoPopupWindow;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

@ContentView(R.layout.activity_edit_news)
@Route(path = "/center/NewsEditActivity")
public class NewsEditActivity extends BaseActivity<NewsViewModel> implements TakePhoto.TakeResultListener, InvokeListener {

    private EditText mTvTitle;
    private EditText mTvFrom;
    private EditText mTvDate;
    private EditText mTvUrl;
    private SimpleDraweeView mTvPic1;
    private SimpleDraweeView mTvPic2;
    private SimpleDraweeView mTvPic3;
    private Button mBtnNew;
    private NewsViewModel mViewModel;
    private PhotoPopupWindow mPhotoPopupWindow;
    private TakePhoto takePhoto;
    private CompressConfig mCompressConfig;
    private InvokeParam invokeParam;
    private int position;
    private String picUrl1;
    private String picUrl2;
    private String picUrl3;
    private DropDownView mTvType;
    private int mType;

    @Override
    public void initTitle() {
        isShowToolBar(true);
    }

    @Override
    public void initView(ViewDataBinding viewDataBinding) {
        ActivityEditNewsBinding binding = (ActivityEditNewsBinding) viewDataBinding;
        mTvTitle = binding.tvTitle;
        mTvFrom = binding.tvFrom;
        mTvDate = binding.tvDate;
        mTvUrl = binding.tvUrl;
        mTvPic1 = binding.tvPic1;
        mTvPic2 = binding.tvPic2;
        mTvPic3 = binding.tvPic3;
        mBtnNew = binding.btnNew;
        mTvType = binding.tvType;
    }

    @Override
    public void initData(ViewModel baseViewModel) {
        mViewModel = (NewsViewModel) baseViewModel;
        mViewModel.setNewsData.observe(this, new Observer<HttpResult>() {
            @Override
            public void onChanged(@Nullable HttpResult httpResult) {
                if (httpResult != null && httpResult.getCode() > 0){
                    Toast.makeText(NewsEditActivity.this, "发布新闻成功!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
         setNewsType();
        mType = getIntent().getIntExtra("type", 0);
        if (mType == 0){
            setCenterTitle("发布新闻");
            mBtnNew.setText("发布新闻");
        }else if (mType == 1){
            setCenterTitle("修改新闻");
            mBtnNew.setText("修改新闻");
            String top = CommonUtil.getShardPStringByKey("top");
            if (!CommonUtil.isStrEmpty(top)){
                List<News>newsList = new Gson().fromJson(top,new TypeToken<List<News>>(){}.getType());
                if (newsList != null && newsList.size() > 0){
                    News news = newsList.get(0);
                    mTvTitle.setText(news.getTitle());
                    mTvFrom.setText(news.getAuthor_name());
                    mTvDate.setText(news.getDate());
                    mTvUrl.setText(news.getUrl());
                    if (!CommonUtil.isStrEmpty(news.getThumbnail_pic_s())){
                        mTvPic1.setImageURI(Uri.parse(news.getThumbnail_pic_s()));
                    }
                    if (!CommonUtil.isStrEmpty(news.getThumbnail_pic_s02())){
                        mTvPic2.setImageURI(Uri.parse(news.getThumbnail_pic_s02()));
                    }
                    if (!CommonUtil.isStrEmpty(news.getThumbnail_pic_s03())){
                        mTvPic3.setImageURI(news.getThumbnail_pic_s03());
                    }
                }
            }
        }else if (mType == 2){
            setCenterTitle("删除新闻");
            mBtnNew.setText("删除新闻");
        }
    }

    private void setNewsType() {
        KeyValueObject k1 = new KeyValueObject("top","头条");
        KeyValueObject k2 = new KeyValueObject("shehui","社会");
        KeyValueObject k3 = new KeyValueObject("guonei","国内");
        KeyValueObject k4 = new KeyValueObject("yule","娱乐");
        KeyValueObject k5 = new KeyValueObject("tiyu","体育");
        KeyValueObject k6 = new KeyValueObject("junshi","军事");
        KeyValueObject k7 = new KeyValueObject("keji","科技");
        KeyValueObject k8 = new KeyValueObject("caijing","财经");
        KeyValueObject k9 = new KeyValueObject("shishang","时尚");
        ArrayList<KeyValueObject>list = new ArrayList<>();
        list.add(k1);
        list.add(k2);
        list.add(k3);
        list.add(k4);
        list.add(k5);
        list.add(k6);
        list.add(k7);
        list.add(k8);
        list.add(k9);
        mTvType.setClickables(true);
        mTvType.setList(list);
    }

    @Override
    protected void initEvent() {
        mTvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                final int year = c.get(Calendar.YEAR);
                final int month = c.get(Calendar.MONTH);
                final int day = c.get(Calendar.DAY_OF_MONTH);


               DatePickerDialog dialog = new DatePickerDialog(NewsEditActivity.this, new DatePickerDialog.OnDateSetListener() {
                   @Override
                   public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                       mTvDate.setText(i + "-" + (i1 + 1) + "-"+i2);
                   }
               },year,month,day);
                dialog.show();
            }
        });
        mBtnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonUtil.isStrEmpty(mTvTitle.getText().toString())||CommonUtil.isStrEmpty(mTvDate.getText().toString())||CommonUtil.isStrEmpty(mTvFrom.getText().toString())||CommonUtil.isStrEmpty(mTvUrl.getText().toString())){
                    CommonUtil.showSnackBar(mTvDate,"数据不能为空!");
                    return;
                }
                if (mType == 0){
                    News news = new News();
                    news.setTitle(mTvTitle.getText().toString());
                    news.setUrl(mTvUrl.getText().toString());
                    news.setAuthor_name(mTvFrom.getText().toString());
                    news.setDate(mTvDate.getText().toString());
                    news.setCategory(mTvType.getText().toString());
                    mViewModel.setNewsController(setNewsMap(news));
                }else if (mType == 1){
                    News news = new News();
                    news.setTitle(mTvTitle.getText().toString());
                    news.setUrl(mTvUrl.getText().toString());
                    news.setAuthor_name(mTvFrom.getText().toString());
                    news.setDate(mTvDate.getText().toString());
                    news.setCategory(mTvType.getText().toString());
                    mViewModel.updateNewsControllser(setNewsMap(news));
                }

            }
        });

        mTvPic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = 0;
               showDialog();
            }
        });
        mTvPic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = 1;
                showDialog();
            }
        });
        mTvPic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = 2;
                showDialog();
            }
        });
    }

    private Map<String, RequestBody> setNewsMap(News news) {
        File file1 = null;
        File file2 = null;
        File file3 = null;
        LinkedHashMap<String, RequestBody> params = new LinkedHashMap<>();
        if (!CommonUtil.isStrEmpty(picUrl1)){
            file1 = new File(picUrl1);
        }
        if (!CommonUtil.isStrEmpty(picUrl2)){
            file2 = new File(picUrl2);
        }
        if (!CommonUtil.isStrEmpty(picUrl3)){
            file3 = new File(picUrl3);
        }
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
        if (file1 != null){
            params.put("photos\"; filename=\"" + file1.getName(), new FileRequestBody<>(RequestBody.create(MediaType.parse("image/*"), file1), call));
        }
        if (file2 != null){
            params.put("photos\"; filename=\"" + file2.getName(), new FileRequestBody<>(RequestBody.create(MediaType.parse("image/*"), file2), call));
        }
        if (file3 != null){
            params.put("photos\"; filename=\"" + file3.getName(), new FileRequestBody<>(RequestBody.create(MediaType.parse("image/*"), file3), call));
        }
        params.put("news",RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(news)));
        return params;
    }

    public void showDialog(){
        mPhotoPopupWindow = new PhotoPopupWindow(NewsEditActivity.this, new View.OnClickListener() {
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
        View rootView = LayoutInflater.from(NewsEditActivity.this)
                .inflate(R.layout.activity_edit_news, null);
        mPhotoPopupWindow.showAtLocation(rootView,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
        if (position == 0){
            mTvPic1.setImageURI(uri);
            picUrl1 = result.getImage().getCompressPath();
        }else if (position == 1){
            mTvPic2.setImageURI(uri);
            picUrl2 = result.getImage().getCompressPath();
        }else if (position == 2){
            mTvPic3.setImageURI(uri);
            picUrl3 = result.getImage().getCompressPath();
        }
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }
}
