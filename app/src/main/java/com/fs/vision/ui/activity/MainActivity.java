package com.fs.vision.ui.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.fs.vision.R;
import com.fs.vision.adapter.FragmentAdapter;
import com.fs.vision.dialog.CardPickerDialog;
import com.fs.vision.net.VideoNetApi;
import com.fs.vision.ui.fragment.PageFragment;
import com.fs.vision.utils.ThemeHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements CardPickerDialog.ClickListener {

    private DrawerLayout mDrawerLayout;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private long firstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.mipmap.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_main_drawer);
        NavigationView navigationView =
                (NavigationView) findViewById(R.id.nv_main_navigation);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Snackbar comes out", Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(
                                        MainActivity.this,
                                        "Toast comes out",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });*/

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_video, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_playhis:
                Toast.makeText(MainActivity.this, "id:" + item.getItemId(), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_search:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_grade:
                Toast.makeText(MainActivity.this, "id:" + item.getItemId(), Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager() {
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        List<String> titles = new ArrayList<>();
        titles.add("电影");
        titles.add("电视剧");
        titles.add("动漫");
        titles.add("综艺");
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(2)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(3)));

        List<Fragment> fragments = new ArrayList<>();
        PageFragment filmFragemnt = new PageFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("type", VideoNetApi.typeFilm);
        filmFragemnt.setArguments(bundle1);
        fragments.add(filmFragemnt);

        PageFragment tvFragment = new PageFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("type", VideoNetApi.typeTv);
        tvFragment.setArguments(bundle2);
        fragments.add(tvFragment);

        PageFragment animationFragment = new PageFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putString("type", VideoNetApi.typeCartoon);
        animationFragment.setArguments(bundle3);
        fragments.add(animationFragment);

        PageFragment artsFragment = new PageFragment();
        Bundle bundle4 = new Bundle();
        bundle4.putString("type", VideoNetApi.typeVariety);
        artsFragment.setArguments(bundle4);
        fragments.add(artsFragment);
        FragmentAdapter adapter =
                new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_home:
                                Toast.makeText(MainActivity.this, "id:" + menuItem.getItemId(), Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.nav_cloudVideo:
                                Toast.makeText(MainActivity.this, "id:" + menuItem.getItemId(), Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.nav_skinLoad:
                                CardPickerDialog dialog = new CardPickerDialog();
                                dialog.setClickListener(MainActivity.this);
                                dialog.show(getSupportFragmentManager(), CardPickerDialog.TAG);
                                break;

                            case R.id.nav_setting:
                                Toast.makeText(MainActivity.this, "id:" + menuItem.getItemId(), Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.nav_disclaimer:
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this, DisclaimerActivity.class);
                                startActivity(intent);
                                break;
                        }
                        mDrawerLayout.closeDrawers();
                        return false;
                    }
                });
    }

    @Override
    public void onConfirm(int currentTheme) {
        if (ThemeHelper.getTheme(this) != currentTheme) {
            ThemeHelper.setTheme(this, currentTheme);
            ThemeUtils.refreshUI(this, new ThemeUtils.ExtraRefreshable() {
                        @Override
                        public void refreshGlobal(Activity activity) {
                            //for global setting, just do once
                            if (Build.VERSION.SDK_INT >= 21) {
                                final MainActivity context = MainActivity.this;
                                ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(null, null, ThemeUtils.getThemeAttrColor(context, android.R.attr.colorPrimary));
                                setTaskDescription(taskDescription);
                                getWindow().setStatusBarColor(ThemeUtils.getColorById(context, R.color.colorPrimaryDark));
                            }
                        }

                        @Override
                        public void refreshSpecificView(View view) {
                            //TODO: will do this for each traversal
                        }
                    }
            );
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
                mDrawerLayout.closeDrawers();
                return true;
            }else {
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 1000) {//如果两次按键时间间隔大于2秒，则不退出
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;//更新firstTime
                    return true;
                } else {//两次按键小于2秒时，退出应用
                    finish();
                    System.exit(0);
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}