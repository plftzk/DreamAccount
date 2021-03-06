package com.gao.dreamaccount.activity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.gao.dreamaccount.R;
import com.gao.dreamaccount.abs.AbsActivity;
import com.gao.dreamaccount.fragment.FragmentYearAccount;
import com.gao.dreamaccount.views.SlidingTabLayout;
import com.melnykov.fab.FloatingActionButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

/**
 * Created by Gao on 2014/11/15.
 */
public class ActivityAccountYearPager_tmp extends AbsActivity implements SwipeBackActivityBase {

    @InjectView(R.id.sliding_tabs)
    SlidingTabLayout mSlidingTabLayout;
    @InjectView(R.id.fab)
    FloatingActionButton floatingActionButton;
    @InjectView(R.id.viewpager)
    ViewPager mViewPager;
    @InjectView(R.id.toolbar_actionbar)
    Toolbar toolbar;


    private SwipeBackActivityHelper swipeBackActivityHelper;
    private String yearStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dream_pager);
        ButterKnife.inject(this);
        swipeBackActivityHelper = new SwipeBackActivityHelper(this);
        swipeBackActivityHelper.onActivityCreate();
        yearStr = getIntent().getStringExtra("year");
        initView();
    }

    private void initView() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (TextUtils.isEmpty(yearStr)) {
            toolbar.setTitle("本年账单");
            toolbar.inflateMenu(R.menu.menu_more);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (menuItem.getItemId() == R.id.action_more) {
                        launchActivity(ActivityAccountTotalPager.class);
                    }
                    return false;
                }
            });
        } else {
            toolbar.setTitle(yearStr + "年账单");
        }
        mViewPager.setAdapter(new SamplePagerAdapter(getSupportFragmentManager(), this));
        mSlidingTabLayout.setSelectedIndicatorColors(R.color.white);
        MTabColorizer mTabColorizer = new MTabColorizer();
        mTabColorizer.setIndicatorColors(getResources().getColor(R.color.white));
        mSlidingTabLayout.setCustomTabColorizer(mTabColorizer);
        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);


        Resources res = getResources();
        mSlidingTabLayout.setSelectedIndicatorColors(res.getColor(R.color.tab_selected_strip));
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    @OnClick(R.id.fab)
    void goAddAccount() {
        launchActivity(ActivityNewAccount.class);
    }

    public void attchListView(ListView listView) {
        if (floatingActionButton != null) {
            floatingActionButton.attachToListView(listView);
            floatingActionButton.show();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        swipeBackActivityHelper.onPostCreate();
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return swipeBackActivityHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        getSwipeBackLayout().scrollToFinishActivity();
    }

    public class SamplePagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {getResources().getString(R.string.hint_income), getResources().getString(R.string.hint_expense)};
        private final int FRAGMENT_COUNT = 2;

        public SamplePagerAdapter(FragmentManager manager, Context context) {
            super(manager);
        }

        @Override
        public int getCount() {
            return FRAGMENT_COUNT;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public Fragment getItem(int i) {
            return FragmentYearAccount.newInstance((i + 1), yearStr);
        }
    }


    class MTabColorizer implements SlidingTabLayout.TabColorizer {
        private int[] mIndicatorColors;

        @Override
        public final int getIndicatorColor(int position) {
            return mIndicatorColors[position % mIndicatorColors.length];
        }

        void setIndicatorColors(int... colors) {
            mIndicatorColors = colors;
        }
    }
}
