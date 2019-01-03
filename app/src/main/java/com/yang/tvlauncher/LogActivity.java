package com.yang.tvlauncher;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.yang.tvlauncher.utils.FileIOUtils;

import static com.yang.tvlauncher.utils.TvExceptionHandler.LOG_FILE_PATH;

public class LogActivity extends Activity {

    private TextView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        view = findViewById(R.id.log_tv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String log = FileIOUtils.readFile2String(LOG_FILE_PATH);

        view.setText(log);
    }
}
