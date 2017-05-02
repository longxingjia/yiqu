package com.yiqu.iyijiayi.fragment.tab3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.ui.abs.AbsFragmentAct;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.service.MusicService;
import com.yiqu.iyijiayi.utils.NoScollViewPager;

import java.util.ArrayList;


public class Tab3Activity extends AbsFragmentAct implements View.OnClickListener{


    private NoScollViewPager mPager;
    private ArrayList<Fragment> fragmentList;
    private TextView sounds;
    private TextView record;
    private Context mContext;

    @Override
    protected int getContentView() {
        return R.layout.activity_tab3;
    }

    @Override
    protected void initView() {
        mPager = (NoScollViewPager) findViewById(R.id.viewpager);
        sounds = (TextView)findViewById(R.id.sounds);
        record = (TextView)findViewById(R.id.record);
        mContext = this;
        findViewById(R.id.back).setOnClickListener(this);
        sounds.setOnClickListener(this);
        record.setOnClickListener(this);

        Intent intent = new Intent();
        intent.putExtra("choice", "stop");
        // top_play.setVisibility(View.GONE);
        intent.setClass(this, MusicService.class);
     startService(intent);

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        InitViewPager();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);       //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onPublish(int progress) {

    }

    @Override
    public void onChange(int position) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sounds:
                mPager.setCurrentItem(0);
                sounds.setTextColor(getResources().getColor(R.color.white));
                sounds.setBackgroundResource(R.mipmap.sounds_pressed);
                record.setTextColor(getResources().getColor(R.color.redMain));
                record.setBackgroundResource(R.mipmap.record);

                break;
            case R.id.record:
                mPager.setCurrentItem(1);
                sounds.setTextColor(getResources().getColor(R.color.redMain));
                sounds.setBackgroundResource(R.mipmap.sounds);
                record.setTextColor(getResources().getColor(R.color.white));
                record.setBackgroundResource(R.mipmap.record_pressed);


                break;
            case R.id.back:
                finish();

                break;
        }

    }

    /*
 * 初始化ViewPager
 */
    public void InitViewPager(){

        fragmentList = new ArrayList<Fragment>();
        Fragment SoundsFragment= new SoundsFragment();
        Fragment RecordFragment = new RecordFragment();

        fragmentList.add(SoundsFragment);
        fragmentList.add(RecordFragment);

        //给ViewPager设置适配器
        mPager.setAdapter(new MyTab3FragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        mPager.setCurrentItem(0);//设置当前显示标签页为第一页
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());//页面变化时的监听器
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {


        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int arg0) {
            // TODO Auto-generated method stub
//            Animation animation = new TranslateAnimation(currIndex*one,arg0*one,0,0);//平移动画
//            currIndex = arg0;
//            animation.setFillAfter(true);//动画终止时停留在最后一帧，不然会回到没有执行前的状态
//            animation.setDuration(200);//动画持续时间0.2秒
//            image.startAnimation(animation);//是用ImageView来显示动画的
//            int i = arg0 + 1;

//            ToastManager.getInstance(mContext).showText("您选择了第"+i+"个页卡");
        }
    }




}
