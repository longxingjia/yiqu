package com.yiqu.iyijiayi.fragment.tab3;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.widget.TextView;

import com.ui.abs.AbsFragment;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.utils.NoScollViewPager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/15.
 */

public class SoundsFragment extends AbsFragment implements View.OnClickListener {

    private TextView buttonOne;
    private TextView buttonTwo;
    private TextView buttonThree;
    private NoScollViewPager mViewPager;
    private ArrayList<Fragment> fragmentList;
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_one:
                buttonOne.setTextColor(getResources().getColor(R.color.redMain));
                buttonTwo.setTextColor(getResources().getColor(R.color.black));
                buttonThree.setTextColor(getResources().getColor(R.color.black));
                changeView(0);
                break;
            case R.id.btn_two:
                buttonOne.setTextColor(getResources().getColor(R.color.black));
                buttonTwo.setTextColor(getResources().getColor(R.color.redMain));
                buttonThree.setTextColor(getResources().getColor(R.color.black));
                changeView(1);
                break;
            case R.id.btn_three:
                buttonOne.setTextColor(getResources().getColor(R.color.black));
                buttonTwo.setTextColor(getResources().getColor(R.color.black));
                buttonThree.setTextColor(getResources().getColor(R.color.redMain));
                changeView(2);
                break;

            default:
                break;
        }
    }
    //手动设置ViewPager要显示的视图
    private void changeView(int desTab)
    {
        mViewPager.setCurrentItem(desTab, true);
    }

    @Override
    protected int getContentView() {
        return R.layout.sounds_fragment;
    }

    @Override
    protected void initView(View v) {
        buttonOne = (TextView) v.findViewById(R.id.btn_one);
        buttonTwo = (TextView) v.findViewById(R.id.btn_two);
        buttonThree = (TextView) v.findViewById(R.id.btn_three);
        buttonOne.setOnClickListener(this);
        buttonTwo.setOnClickListener(this);
        buttonThree.setOnClickListener(this);
        mViewPager=(NoScollViewPager) v.findViewById(R.id.viewpager);

        fragmentList=new ArrayList<Fragment>();
        SoundsTab2fragment soundsTab2Fragment = new SoundsTab2fragment();
        SoundTab1Fragment soundTab1Fragment = new SoundTab1Fragment();
        SoundsTab3fragment soundsTab3Fragment = new SoundsTab3fragment();

        fragmentList.add(soundTab1Fragment);
        fragmentList.add(soundsTab2Fragment);
        fragmentList.add(soundsTab3Fragment);

        mViewPager.setAdapter(new MyFrageStatePagerAdapter(getChildFragmentManager()));
        mViewPager.setCurrentItem(0);
    }

    /**
     * 定义自己的ViewPager适配器。
     * 也可以使用FragmentPagerAdapter。关于这两者之间的区别，可以自己去搜一下。
     */
    class MyFrageStatePagerAdapter extends FragmentStatePagerAdapter
    {

        public MyFrageStatePagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }


    }
}
