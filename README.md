# DataPicker
时间选择器

# 项目运行效果图
![项目运行效果](https://github.com/1181631922/DataPicker/blob/master/ScreenShots/datapicker.gif)

# 简单看一下代码，这里用的是dialog+回调实现日期的选择

```
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
```

# update 添加新方法，日期选择器的最后的时间是否为今天

# 项目运行效果图
![项目运行效果](https://github.com/1181631922/DataPicker/blob/master/ScreenShots/datapicker_1.gif)

# 示例代码：
```
new DatePicker(MainActivity.this, false, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE), true)
                        .setOnDateTimeSetListener(new DatePicker.OnDateTimeSetListener() {
                            @Override
                            public void onDateTimeSet(DatePicker picker) {
                                birthdayTime = picker.getTime();
                                tvDataPicker.setText(formatter.format(birthdayTime.getTime()));
                            }
                        })
                        .show();
```