package com.fanyafeng.datapicker.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fanyafeng.datapicker.R;
import com.fanyafeng.datapicker.BaseActivity;
import com.fanyafeng.datapicker.view.picker.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

//需要搭配baseactivity，这里默认为baseactivity,并且默认Baseactivity为包名的根目录
public class MainActivity extends BaseActivity {
    private Button btnDataPicker;
    private TextView tvDataPicker;
    private Calendar birthdayTime = Calendar.getInstance();
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//这里默认使用的是toolbar的左上角标题，如果需要使用的标题为中心的采用下方注释的代码，将此注释掉即可
        title = getString(R.string.title_activity_main);

        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //toolbar_center_title.setText(getString(R.string.title_activity_main));
    }

    //初始化UI空间
    private void initView() {
        btnDataPicker = (Button) findViewById(R.id.btnDataPicker);
        btnDataPicker.setOnClickListener(this);
        tvDataPicker = (TextView) findViewById(R.id.tvDataPicker);
    }

    //初始化数据
    private void initData() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btnDataPicker:
                Calendar now = Calendar.getInstance();
                now.set(Calendar.YEAR, 1985);

                new DatePicker(MainActivity.this, true, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE))
                        .setOnDateTimeSetListener(new DatePicker.OnDateTimeSetListener() {
                            @Override
                            public void onDateTimeSet(DatePicker picker) {

                                birthdayTime = picker.getTime();
                                tvDataPicker.setText(formatter.format(birthdayTime.getTime()));
                            }
                        })
                        .show();
                break;
        }
    }
}
