package com.fanyafeng.datapicker;

import android.os.Bundle;
import android.widget.TextView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected Toolbar toolbar;
    protected TextView toolbar_center_title;
    protected boolean isShowToolbar = true;
    protected boolean isSetNavigationIcon = true;
    protected boolean isSetLogo = false;
    protected boolean isShowEmail = true;
    protected String title;
    protected String centertitle;
    protected String subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_center_title = (TextView) findViewById(R.id.toolbar_center_title);
        if (toolbar != null) {
            if (isShowToolbar) {
                setSupportActionBar(toolbar);
            } else {
                toolbar.setVisibility(View.GONE);
            }

            if (title != null && !title.equals("")) {
                toolbar.setTitle(title);
            } else {
                toolbar.setTitle("");
            }

            if (subtitle != null && !subtitle.equals("")) {
                toolbar.setSubtitle(subtitle);
            }
            if (isSetNavigationIcon) {
//                由于要兼容低版本，所以采用这个划杠的方法，需要自己根据需求替换图片
                toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_launcher));
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
            if (isSetLogo) {
                //需要自己根据需求进行替换图片
                toolbar.setLogo(getResources().getDrawable(R.mipmap.ic_launcher));
            }

            if (toolbar_center_title != null) {
                if (centertitle != null && !centertitle.equals("")) {
                    toolbar_center_title.setText(centertitle);
                } else {
                    toolbar_center_title.setText("");
                }

            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_base, menu);
        //        默认隐藏setting按钮
        if (toolbar != null) {
            MenuItem menuItem = toolbar.getMenu().getItem(0);
            if (menuItem != null) {
                menuItem.setVisible(false);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
