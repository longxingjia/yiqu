package com.yiqu.iyijiayi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;

import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.ui.abs.AbsFragment;
import com.ui.abs.AbsFragmentActForMainAct;
import com.ui.abs.OnFragmentListener;
import com.ui.views.TabHostView;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.fragment.TabContentFragment;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.utils.LogUtils;

public class MainActivity extends AbsFragmentActForMainAct implements Handler.Callback,
        NetCallBack {

    private String[] classNames;
    private String mCurrentTabFragmentTag;
    private TabHostView mTabHostView;

    @Override
    protected int getContentView() {
        return R.layout.act_main;
    }

    @Override
    protected int getBehindContentView() {
        return R.layout.menu_frame;
    }

    @Override
    protected void initView() {
        mTabHostView = (TabHostView) findViewById(R.id.tabHostView);
        mTabHostView.setOnTabSelectListener(new TabHostView.onTabSelectListener() {
            @Override
            public boolean onTabSelect(int p) {
                if (classNames != null) {
                    if (p >= 0 && p < classNames.length) {
                        changeTabFragment(classNames[p]);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    @Override
    protected void init(Bundle savedInstanceState) {

        classNames = getResources().getStringArray(R.array.main_tab_item_click);

        if (savedInstanceState != null) {
            String currentTabFragmentTag = savedInstanceState
                    .getString("tabtag");
            if (currentTabFragmentTag != null) {
                for (int i = 0; i < classNames.length; i++) {
                    if (currentTabFragmentTag.equals(classNames[i])) {
                        try {
                            mTabHostView.setCurrentTab(i, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        hideMainTab(classNames[i]);
                    }
                }
            }
        } else {
            try {
                mTabHostView.setCurrentTab(0, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String className = getIntent().getStringExtra("fragmentName");
        if (className != null && !className.isEmpty()) {
            changeTabFragment(className);
        }

    }

    @Override
    public boolean handleMessage(Message message) {
        return false;
    }

    @Override
    public void onNetNoStart(String id) {

    }

    @Override
    public void onNetStart(String id) {

    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {

    }

    private void changeTabFragment(String className) {
        hideMainTab(mCurrentTabFragmentTag);
        AbsFragment f = (AbsFragment) getSupportFragmentManager()
                .findFragmentByTag(className);
        // f.changeDot(true);

        if (f != null) {
            FragmentManager ft = getSupportFragmentManager();
            f.setOnFragmentListener(mOnMainFragmentListener);
            ft.beginTransaction().show(f).commit();
            TabContentFragment b = (TabContentFragment) f;
            b.onSelect();
            b.onResume();


        } else {
            f = Model.creatFragment(className);
            if (f != null) {
                FragmentManager ft = getSupportFragmentManager();
                f.setOnFragmentListener(mOnMainFragmentListener);
                ft.beginTransaction().add(R.id.sub_tab_content, f, className)
                        .commit();

            }
        }
        mCurrentTabFragmentTag = className;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            new AlertDialog.Builder(new ContextThemeWrapper(this,
                    R.style.Theme_AppCompat_Dialog))
                    .setNegativeButton("确定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }).setPositiveButton("取消", null)
                    .setMessage("确定退出?").show();
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

    private OnFragmentListener mOnMainFragmentListener = new OnFragmentListener() {
        @Override
        public void onFragmentCreated(Fragment f) {
        }

        @Override
        public void onFragmentBack(Fragment f) {

        }
    };

    private void hideMainTab(String tag) {
        if (tag != null) {
            FragmentManager ft = getSupportFragmentManager();

            Fragment fcuurenttab = ft.findFragmentByTag(tag);
            if (fcuurenttab != null) {
                ft.beginTransaction().hide(fcuurenttab).commit();
                TabContentFragment b = (TabContentFragment) fcuurenttab;
                b.onNoSelect();
            }
        }
    }

}
