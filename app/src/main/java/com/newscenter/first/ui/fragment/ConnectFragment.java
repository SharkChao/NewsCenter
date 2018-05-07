package com.newscenter.first.ui.fragment;

import android.arch.lifecycle.ViewModel;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.widget.Toast;

import com.newscenter.first.R;
import com.newscenter.first.annotation.ContentView;
import com.newscenter.first.base.BaseFragment;
import com.newscenter.first.ui.HomeActivity;
import com.newscenter.first.viewmodel.base.BaseViewModel;


/**
 * Created by Admin on 2018/2/1.
 * 交流fragment
 */
@ContentView(R.layout.fragment_group)
public class ConnectFragment extends BaseFragment<BaseViewModel> {

    @Override
    protected void handleArguments(Bundle arguments) {

    }

    @Override
    protected void initTitle() {
        isShowToolBar(false);
    }

    @Override
    protected void initViews(ViewDataBinding viewDataBinding, Bundle savedInstanceState) {

    }

    @Override
    protected void initData(ViewModel baseViewModel) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void lazyLoad() {

    }
}
