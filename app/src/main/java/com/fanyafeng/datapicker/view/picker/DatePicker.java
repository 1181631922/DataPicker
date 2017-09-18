package com.fanyafeng.datapicker.view.picker;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;


import com.fanyafeng.datapicker.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zgw on 16/5/14 15:14.
 */
public class DatePicker extends Dialog {
    private WheelView wv_year;
    private WheelView wv_month;
    private WheelView wv_day;

    private boolean isStillNow = false;

    private static int START_YEAR = 1901, END_YEAR = 2048;
    private boolean mSolar;
    private boolean mYearShow;
    private boolean mLeapMonthShow = true;

    private OnDateTimeSetListener mOnDateTimeSetListener;
    private OnAbortPickerListener mOnAbortListener;

    public interface OnDateTimeSetListener {
        void onDateTimeSet(DatePicker picker);
    }

    public interface OnAbortPickerListener {
        void OnAbort(DatePicker picker);
    }

    public DatePicker setOnDateTimeSetListener(OnDateTimeSetListener l) {
        this.mOnDateTimeSetListener = l;
        return this;
    }

    public DatePicker setOnAbortPickerListener(OnAbortPickerListener l) {
        this.mOnAbortListener = l;
        return this;
    }

    /**
     * 最终日期是否是今天
     *
     * @param context
     * @param solar
     * @param year
     * @param month
     * @param day
     * @param isStillNow
     */
    public DatePicker(Context context, boolean solar, int year, int month, int day, boolean isStillNow) {
        super(context);
        this.mYearShow = true;
        this.isStillNow = isStillNow;
        if (isStillNow) {
            END_YEAR = Calendar.getInstance().get(Calendar.YEAR);
        } else {
            END_YEAR = 2048;
        }
        init(solar, year, month, day);

        findViewById(R.id.lunar_select_layout).setVisibility(View.GONE);
        findViewById(R.id.year_select_layout).setVisibility(View.GONE);
    }

    public DatePicker(Context context, boolean solar, int year, int month, int day) {
        super(context);
        this.mYearShow = true;
        init(solar, year, month, day);

        findViewById(R.id.lunar_select_layout).setVisibility(View.GONE);
        findViewById(R.id.year_select_layout).setVisibility(View.GONE);
    }

    public DatePicker(Context context, boolean isYearShow, boolean solar, int year, int month, int day) {
        super(context);
        this.mYearShow = isYearShow;
        init(solar, year, month, day);
        findViewById(R.id.year_select_layout).setVisibility(View.VISIBLE);
    }

    public DatePicker(Context context, boolean isYearShow, boolean solar, int year, int month, int day, boolean isLeapMonthShow) {
        super(context);
        this.mYearShow = isYearShow;
        this.mLeapMonthShow = isLeapMonthShow;
        init(solar, year, month, day);
        findViewById(R.id.year_select_layout).setVisibility(View.VISIBLE);
    }

    private void init(final boolean solar, final int year, final int month, final int day) {
        this.mSolar = solar;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.picker_date_layout);
        Window window = getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wl.gravity = Gravity.BOTTOM;

        ViewGroup check_layout;
        ImageView box;


        check_layout = (ViewGroup) findViewById(R.id.lunar_select_layout);
        box = (ImageView) check_layout.findViewById(R.id.box);
        if (mSolar) {
            box.setImageResource(R.drawable.picker_box);
        } else {
            box.setImageResource(R.drawable.picker_box_checked);
        }
        check_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int year = getYear();
                int month = getMonth();
                int day = getDay();

                mSolar = !mSolar;
                ImageView box = (ImageView) v.findViewById(R.id.box);
                if (mSolar) {
                    box.setImageResource(R.drawable.picker_box);
                } else {
                    box.setImageResource(R.drawable.picker_box_checked);
                }
                initDateTimePicker(year, month, day);
            }
        });

        check_layout = (ViewGroup) findViewById(R.id.year_select_layout);
        box = (ImageView) check_layout.findViewById(R.id.year_box);
        if (mYearShow) {
            box.setImageResource(R.drawable.picker_box);
        } else {
            box.setImageResource(R.drawable.picker_box_checked);
        }
        check_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int year = getYear();
                int month = getMonth();
                int day = getDay();

                mYearShow = !mYearShow;
                ImageView box = (ImageView) v.findViewById(R.id.box);
                if (mYearShow) {
                    box.setImageResource(R.drawable.picker_box);
                } else {
                    box.setImageResource(R.drawable.picker_box_checked);
                }
                initDateTimePicker(year, month, day);
            }
        });

        View button;
        button = findViewById(R.id.negative_button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mSolar = solar;
                initDateTimePicker(year, month, day);
                dismiss();
            }
        });

        button = findViewById(R.id.positive_button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar c = getTime();
                Calendar n = Calendar.getInstance();

                if (c.get(Calendar.YEAR) > n.get(Calendar.YEAR)
                        || (c.get(Calendar.YEAR) == n.get(Calendar.YEAR) && c.get(Calendar.MONTH) > n.get(Calendar.MONTH))
                        || (c.get(Calendar.YEAR) == n.get(Calendar.YEAR) && c.get(Calendar.MONTH) == n.get(Calendar.MONTH) && c.get(Calendar.DAY_OF_MONTH) > n.get(Calendar.DAY_OF_MONTH))) {
                    Toast.makeText(getContext(), R.string.error_wrong_birthday, Toast.LENGTH_SHORT).show();
                    return;
                }
                dismiss();
                if (mOnDateTimeSetListener != null) {
                    mOnDateTimeSetListener.onDateTimeSet(DatePicker.this);
                }
            }
        });

        initDateTimePicker(year, month, day);
    }

    public void initDateTimePicker(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        LunarItem lunarItem = new LunarItem(cal);

        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
        String[] months_little = {"4", "6", "9", "11"};

        final List<String> list_big = Arrays.asList(months_big);
        final List<String> list_little = Arrays.asList(months_little);

        // 年
        wv_year = (WheelView) findViewById(R.id.year);
        if (!mYearShow) {
            wv_year.setVisibility(View.GONE);
        } else {
            wv_year.setVisibility(View.VISIBLE);
        }
        wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
        wv_year.setCyclic(false);// 可循环滚动
        wv_year.setLabel("年");// 添加文字
        if (mSolar) {
            wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据
        } else {
            wv_year.setCurrentItem(lunarItem.getYear() - START_YEAR);
        }

        // 月
        wv_month = (WheelView) findViewById(R.id.month);
        if (mSolar) {
            wv_month.setAdapter(new NumericWheelAdapter(1, 12));
            wv_month.setCurrentItem(month);
        } else {
            wv_month.setAdapter(new ArrayWheelAdapter<String>(getLunarMonthArray(lunarItem.getYear())));
            int lunarMonth = lunarItem.getMonth() + 1;
            if (mLeapMonthShow) {
                if ((lunarMonth > LunarItem.leapMonth(lunarItem.getYear()) && LunarItem.leapMonth(lunarItem.getYear()) > 0) || lunarItem.isLeep()) {
                    lunarMonth++;
                }
            }
            wv_month.setCurrentItem(lunarMonth - 1);
        }
        wv_month.setCyclic(false);
        wv_month.setLabel("月");

        // 日
        wv_day = (WheelView) findViewById(R.id.day);
        wv_day.setCyclic(false);
        // 判断大小月及是否闰年,用来确定"日"的数据
        if (mSolar) {
            if (list_big.contains(String.valueOf(month + 1))) {
                wv_day.setAdapter(new NumericWheelAdapter(1, 31));
            } else if (list_little.contains(String.valueOf(month + 1))) {
                wv_day.setAdapter(new NumericWheelAdapter(1, 30));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
                    wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                else
                    wv_day.setAdapter(new NumericWheelAdapter(1, 28));
            }
            wv_day.setCurrentItem(day - 1);
        } else {
            wv_day.setAdapter(new ArrayWheelAdapter<String>(getLunarDayArray(lunarItem.getYear(), lunarItem.getMonth() + 1)));
            wv_day.setCurrentItem(lunarItem.getDay() - 1);
        }

        updateDayLabel();

//		wv_hours = (WheelView) findViewById(R.id.hour);
//		wv_mins = (WheelView) findViewById(R.id.min);
//		if (hasSelectTime) {
//			wv_hours.setVisibility(View.VISIBLE);
//			wv_mins.setVisibility(View.VISIBLE);
//
//			wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
//			wv_hours.setCyclic(false);// 可循环滚动
//			wv_hours.setLabel("时");// 添加文字
//			wv_hours.setCurrentItem(h);
//
//			wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
//			wv_mins.setCyclic(false);// 可循环滚动
//			wv_mins.setLabel("分");// 添加文字
//			wv_mins.setCurrentItem(m);
//		} else {
//			wv_hours.setVisibility(View.GONE);
//			wv_mins.setVisibility(View.GONE);
//		}

        // 添加"年"监听
        OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int year_num = newValue + START_YEAR;
                if (mSolar) {
                    if (list_big.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                    } else if (list_little.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                    } else {
                        if ((year_num % 4 == 0 && year_num % 100 != 0) || year_num % 400 == 0)
                            wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                        else
                            wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                    }
                } else {
                    wv_month.setAdapter(new ArrayWheelAdapter<String>(getLunarMonthArray(year_num)));
                    wv_day.setAdapter(new ArrayWheelAdapter<String>(getLunarDayArray(year_num, wv_month.getCurrentItem() + 1)));
                }

                // TODO: 是否截止到今日
                if (mSolar && isStillNow && year_num == END_YEAR) {
                    wv_month.setAdapter(new NumericWheelAdapter(1, Calendar.getInstance().get(Calendar.MONTH)));
                    wv_month.setCurrentItem(wv_month.getAdapter().getItemsCount() - 1, true);
                } else if (!mSolar && isStillNow && year_num == END_YEAR) {
                    // TODO: 17/9/18
                    wv_month.setAdapter(new ArrayWheelAdapter<String>(getLunarMonthArrayIsStillNow(year_num, Calendar.getInstance().get(Calendar.MONTH) - 1)));
                    wv_day.setAdapter(new ArrayWheelAdapter<String>(getLunarDayArrayIsStillNow()));
                }

                if (wv_month.getCurrentItem() >= wv_month.getAdapter().getItemsCount()) {
                    wv_month.setCurrentItem(wv_month.getAdapter().getItemsCount() - 1, true);
                }

                if (wv_day.getCurrentItem() >= wv_day.getAdapter().getItemsCount()) {
                    wv_day.setCurrentItem(wv_day.getAdapter().getItemsCount() - 1, true);
                }
                updateDayLabel();
            }
        };
        wv_year.addChangingListener(wheelListener_year);

        // 添加"月"监听
        OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int month_num = newValue + 1;
                if (mSolar) {
                    if (list_big.contains(String.valueOf(month_num))) {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                    } else if (list_little.contains(String.valueOf(month_num))) {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                    } else {
                        if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year.getCurrentItem() + START_YEAR) % 100 != 0) || (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
                            wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                        else
                            wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                    }
                } else {
                    wv_day.setAdapter(new ArrayWheelAdapter<String>(getLunarDayArray(wv_year.getCurrentItem() + START_YEAR, month_num)));
                }

                // TODO: 是否截止到今日
                if (mSolar && isStillNow && month_num == Calendar.getInstance().get(Calendar.MONTH)) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
                } else if (!mSolar && isStillNow && month_num == Calendar.getInstance().get(Calendar.MONTH)) {
                    wv_day.setAdapter(new ArrayWheelAdapter<String>(getLunarDayArrayIsStillNow()));
                }

                if (wv_day.getCurrentItem() >= wv_day.getAdapter().getItemsCount()) {
                    wv_day.setCurrentItem(wv_day.getAdapter().getItemsCount() - 1, true);
                }

                updateDayLabel();
            }
        };
        wv_month.addChangingListener(wheelListener_month);

        // 添加"日"监听
        OnWheelChangedListener wheelListener_day = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDayLabel();
            }
        };
        wv_day.addChangingListener(wheelListener_day);

    }

    private void updateDayLabel() {
        int year = getYear();
        int month = getMonth();
        int day = getDay();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        wv_day.setLabel(DateFormatManager.DayOfWeekDisplay(calendar.get(Calendar.DAY_OF_WEEK)));
    }

    private String[] getLunarMonthArray(int year) {
        List<String> list = new ArrayList<String>();

        int leapMonth = LunarItem.leapMonth(year);
        if (!mYearShow) {
            leapMonth = 0;
        }

        for (int i = 1; i <= 12; i++) {
            list.add(LunarItem.getChinaMonthString(i, false).replaceAll("月", ""));
            if (mLeapMonthShow && i == leapMonth) {
                list.add(LunarItem.getChinaMonthString(i, true).replaceAll("月", ""));
            }
        }
        String[] result = new String[list.size()];
        list.toArray(result);
        return result;
    }

    private String[] getLunarMonthArrayIsStillNow(int year, int month) {
        List<String> list = new ArrayList<String>();

        int leapMonth = LunarItem.leapMonth(year);
        if (!mYearShow) {
            leapMonth = 0;
        }

        int count = 1;
        String lunarMonth = LunarItem.getChinaMonthString(month, false);
        if (lunarMonth.contains("正")) {
            count = 1;
        } else if (lunarMonth.contains("二")) {
            count = 2;
        } else if (lunarMonth.contains("三")) {
            count = 3;
        } else if (lunarMonth.contains("四")) {
            count = 4;
        } else if (lunarMonth.contains("五")) {
            count = 5;
        } else if (lunarMonth.contains("六")) {
            count = 6;
        } else if (lunarMonth.contains("七")) {
            count = 7;
        } else if (lunarMonth.contains("八")) {
            count = 8;
        } else if (lunarMonth.contains("九")) {
            count = 9;
        } else if (lunarMonth.contains("十")) {
            count = 10;
        } else if (lunarMonth.contains("冬")) {
            count = 11;
        } else if (lunarMonth.contains("腊")) {
            count = 12;
        }

        for (int i = 1; i <= count; i++) {
            list.add(LunarItem.getChinaMonthString(i, false).replaceAll("月", ""));
            if (mLeapMonthShow && i == leapMonth) {
                list.add(LunarItem.getChinaMonthString(i, true).replaceAll("月", ""));
            }
        }
        String[] result = new String[list.size()];
        list.toArray(result);
        return result;
    }

    private String[] getLunarDayArray(int year, int month) {
        List<String> list = new ArrayList<String>();
        for (int i = 1; i <= LunarItem.getMonthDays(year, month); i++) {
            list.add(LunarItem.getChinaDayString(i));
        }
        String[] result = new String[list.size()];
        list.toArray(result);
        return result;
    }

    private String[] getLunarDayArrayIsStillNow() {
        List<String> list = new ArrayList<String>();

        Calendar cal = Calendar.getInstance();
        LunarItem lunarItem = new LunarItem(cal);

        for (int i = 1; i <= lunarItem.getDay(); i++) {
            list.add(LunarItem.getChinaDayString(i));
        }
        // TODO: 17/9/18
        String[] result = new String[list.size()];
        list.toArray(result);
        return result;
    }

    public int getYear() {
        if (mSolar) {
            return wv_year.getCurrentItem() + START_YEAR;
        } else {
            int month = wv_month.getCurrentItem() + 1;
            if (mLeapMonthShow) {
                int leapMonth = LunarItem.leapMonth(wv_year.getCurrentItem() + START_YEAR);
                if (leapMonth > 0 && month > leapMonth) {
                    month--;
                    if (month == leapMonth) {
                        month += 12;
                    }
                }
            }
            return LunarParser.parseToSolar(wv_year.getCurrentItem() + START_YEAR, month, wv_day.getCurrentItem() + 1)[0];
        }
    }

    /**
     * @return month in [0..11]
     */
    public int getMonth() {
        if (mSolar) {
            return wv_month.getCurrentItem();
        } else {
            int month = wv_month.getCurrentItem() + 1;
            if (mLeapMonthShow) {
                int leapMonth = LunarItem.leapMonth(wv_year.getCurrentItem() + START_YEAR);
                if (leapMonth > 0 && month > leapMonth) {
                    month--;
                    if (month == leapMonth) {
                        month += 12;
                    }
                }
            }
            return LunarParser.parseToSolar(wv_year.getCurrentItem() + START_YEAR, month, wv_day.getCurrentItem() + 1)[1] - 1;
        }
    }

    public int getRawMonth() {
        return wv_month.getCurrentItem();
    }

    public int getDay() {
        if (mSolar) {
            return wv_day.getCurrentItem() + 1;
        } else {
            int month = wv_month.getCurrentItem() + 1;
            if (mLeapMonthShow) {
                int leapMonth = LunarItem.leapMonth(wv_year.getCurrentItem() + START_YEAR);
                if (leapMonth > 0 && month > leapMonth) {
                    month--;
                    if (month == leapMonth) {
                        month += 12;
                    }
                }
            }
            return LunarParser.parseToSolar(wv_year.getCurrentItem() + START_YEAR, month, wv_day.getCurrentItem() + 1)[2];
        }
    }

    public int getRawDay() {
        return wv_day.getCurrentItem() + 1;
    }


    public Calendar getTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getYear(), getMonth(), getDay(), 0, 0);
        return calendar;
    }

    public boolean isSolar() {
        return mSolar;
    }

    public boolean isIgnoreYear() {
        return !mYearShow;
    }
}