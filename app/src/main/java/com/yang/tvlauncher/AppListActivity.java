package com.yang.tvlauncher;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;

import com.yang.tvlauncher.utils.AppInfoBean;

public class AppListActivity extends Activity {

    AppListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        fragment = (AppListFragment) getFragmentManager().findFragmentById(R.id.app_list_fragment);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                AppInfoBean bean = (AppInfoBean) fragment.getSelectedRowViewHolder().getSelectedItem();
                Uri uri = Uri.fromParts("package", bean.getPackageName(), null);
                Intent intent = new Intent(Intent.ACTION_DELETE, uri);
                startActivity(intent);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
