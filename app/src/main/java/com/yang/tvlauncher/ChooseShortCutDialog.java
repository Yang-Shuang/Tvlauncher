package com.yang.tvlauncher;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.yang.tvlauncher.presenter.DialogListAdapter;
import com.yang.tvlauncher.utils.AppInfoBean;
import com.yang.tvlauncher.utils.LogUtil;
import com.yang.tvlauncher.utils.ScreenUtil;
import com.yang.tvlauncher.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by
 * yangshuang on 2018/12/18.
 */

public class ChooseShortCutDialog extends DialogFragment {

    private RecyclerView recyclerView;
    private ArrayList<AppInfoBean> beans;
    private onSelectedAppListener onSelectedAppListener;

    public void setOnSelectedAppListener(ChooseShortCutDialog.onSelectedAppListener onSelectedAppListener) {
        this.onSelectedAppListener = onSelectedAppListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_choose_shortcut, container);
        recyclerView = view.findViewById(R.id.dialog_rv);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 6));
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
        params.width = (int) (ScreenUtil.screen_width / 10f * 7);
        params.height = (int) (ScreenUtil.screen_width / 10f * 4);
        recyclerView.setLayoutParams(params);
        recyclerView.setPadding(ScreenUtil.screen_width / 20, 0, ScreenUtil.screen_width / 20, 0);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DialogListAdapter adapter = new DialogListAdapter(beans);
        adapter.setListener(new DialogListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object data) {
                dismiss();
                AppInfoBean bean = (AppInfoBean) data;
                if (onSelectedAppListener != null) {
                    onSelectedAppListener.onSelectedApp(bean);
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    public void setData(ArrayList<AppInfoBean> beans) {
        this.beans = beans;
    }

    public void show(FragmentManager manager) {
        super.show(manager, "ChooseShortCutDialog");
    }

    public interface onSelectedAppListener {
        void onSelectedApp(AppInfoBean bean);
    }
}
