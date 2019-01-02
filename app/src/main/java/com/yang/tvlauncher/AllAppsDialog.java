package com.yang.tvlauncher;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import com.yang.tvlauncher.utils.ScreenUtil;

/**
 * Created by
 * yangshuang on 2019/1/2.
 */

public class AllAppsDialog extends DialogFragment {

    AppListFragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_all_apps, container);
        fragment = (AppListFragment) getFragmentManager().findFragmentById(R.id.app_list_fragment);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fragment != null)
            getFragmentManager().beginTransaction().remove(fragment).commit();

    }

    public void show(FragmentManager manager) {
        super.show(manager, "ChooseShortCutDialog");
    }

}
