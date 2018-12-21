package com.yang.tvlauncher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yang.tvlauncher.utils.TimeUtil;


import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/*
 * MainActivity class that loads {@link MainFragment}.
 */
public class MainActivity extends Activity {

    private RelativeLayout timeLL;
    private TextView timeTv, dateTv;
    private Disposable subscription;
    private MainFragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeLL = findViewById(R.id.main_time_ll);
        timeTv = findViewById(R.id.main_time_tv);
        dateTv = findViewById(R.id.main_date_tv);
        fragment = (MainFragment) getFragmentManager().findFragmentById(R.id.main_browse_fragment);

        subscription = Observable.interval(0, 1, TimeUnit.SECONDS)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return aLong;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        timeTv.setText(TimeUtil.getTimeStr());
                        dateTv.setText(TimeUtil.getDateStr() + "\n农历" + TimeUtil.getLunarStr());
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!subscription.isDisposed()) {
            subscription.dispose();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        fragment.refreshUI();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

//        KeyEvent.KEYCODE_INFO 165
//        KeyEvent.KEYCODE_HOME 3
//        KeyEvent.KEYCODE_MENU 82
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                int type = fragment.isSelectApp() ? MenuFragment.ALL : MenuFragment.ONLY_SETTINGS;
                Intent intent = new Intent(this,
                        MenuActivity.class);
                intent.putExtra(MenuFragment.MENU_TYPE,type);
                startActivity(intent);
                break;
            case KeyEvent.KEYCODE_INFO:
                Intent i = new Intent(this,
                        HomeActivity.class);
                startActivity(i);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        switch (keyCode) {
//
//            case KeyEvent.KEYCODE_ENTER:     //确定键enter
//            case KeyEvent.KEYCODE_DPAD_CENTER:
//                ToastUtil.toast("确定键enter--->");
//
//                break;
//
//            case KeyEvent.KEYCODE_BACK:    //返回键
//                ToastUtil.toast("返回键back--->");
//
//                return true;   //这里由于break会退出，所以我们自己要处理掉 不返回上一层
//
//            case KeyEvent.KEYCODE_SETTINGS: //设置键
//                ToastUtil.toast("设置键setting--->");
//
//                break;
//
//            case KeyEvent.KEYCODE_DPAD_DOWN:   //向下键
//
//                /*    实际开发中有时候会触发两次，所以要判断一下按下时触发 ，松开按键时不触发
//                 *    exp:KeyEvent.ACTION_UP
//                 */
//                if (event.getAction() == KeyEvent.ACTION_DOWN) {
//
//                    ToastUtil.toast("向下键down--->");
//                }
//
//                break;
//
//            case KeyEvent.KEYCODE_DPAD_UP:   //向上键
//                ToastUtil.toast("向上键up--->");
//
//                break;
//
//            case KeyEvent.KEYCODE_0:   //数字键0
//                ToastUtil.toast("数字键00--->");
//
//                break;
//
//            case KeyEvent.KEYCODE_DPAD_LEFT: //向左键
//
//                ToastUtil.toast("向左键left--->");
//
//                break;
//
//            case KeyEvent.KEYCODE_DPAD_RIGHT:  //向右键
//                ToastUtil.toast("向右键right--->");
//                break;
//
//            case KeyEvent.KEYCODE_INFO:    //info键
//                ToastUtil.toast("info键info--->");
//
//                break;
//
//            case KeyEvent.KEYCODE_PAGE_DOWN:     //向上翻页键
//            case KeyEvent.KEYCODE_MEDIA_NEXT:
//                ToastUtil.toast("向上翻页键page down--->");
//
//                break;
//
//
//            case KeyEvent.KEYCODE_PAGE_UP:     //向下翻页键
//            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
//                ToastUtil.toast("向下翻页键page up--->");
//
//                break;
//
//            case KeyEvent.KEYCODE_VOLUME_UP:   //调大声音键
//                ToastUtil.toast("调大声音键voice up--->");
//
//                break;
//
//            case KeyEvent.KEYCODE_VOLUME_DOWN: //降低声音键
//                ToastUtil.toast("降低声音键voice down--->");
//
//                break;
//            case KeyEvent.KEYCODE_VOLUME_MUTE: //禁用声音
//                ToastUtil.toast("禁用声音voice mute--->");
//                break;
//
//            default:
//                break;
//        }
//
//        return super.onKeyDown(keyCode, event);
//
//    }
}
