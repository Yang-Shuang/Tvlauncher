package com.yang.tvlauncher;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.yang.tvlauncher.presenter.DialogAppInfoViewHolder;
import com.yang.tvlauncher.bean.AppInfoBean;

public class AppListActivity extends Activity {

    AppListFragment fragment;
    DialogAppInfoViewHolder holder;
    DialogPlus p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        fragment = (AppListFragment) getFragmentManager().findFragmentById(R.id.app_list_fragment);

        holder = new DialogAppInfoViewHolder(getLayoutInflater().inflate(R.layout.dialog_app_info, null));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                final AppInfoBean bean = (AppInfoBean) fragment.getSelectedRowViewHolder().getSelectedItem();
                holder.setData(bean);
                p = DialogPlus.newDialog(this)
                        .setBackgroundColorResId(Color.TRANSPARENT)
                        .setContentHolder(holder)
                        .setCancelable(true)
                        .setInAnimation(R.anim.slide_in_right)
                        .setOutAnimation(R.anim.slide_out_right)
                        .setGravity(Gravity.RIGHT)
                        .setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(DialogPlus dialog, View view) {
                                if (view.getId() == R.id.dialog_app_uninstall_btn) {
                                    Uri uri = Uri.fromParts("package", bean.getPackageName(), null);
                                    Intent intent = new Intent(Intent.ACTION_DELETE, uri);
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                            }
                        })
                        .create();
                p.show();
                p.getHolderView().findViewById(R.id.dialog_app_uninstall_btn).requestFocus();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if(p !=null && p.isShowing()){
            p.dismiss();
        } else {
            finish();
        }
    }
}
