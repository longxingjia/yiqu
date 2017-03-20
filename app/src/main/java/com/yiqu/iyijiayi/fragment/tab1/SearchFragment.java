package com.yiqu.iyijiayi.fragment.tab1;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ui.abs.AbsFragment;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.adapter.SearchMusicAdapter;
import com.yiqu.iyijiayi.adapter.SearchSoundAdapter;
import com.yiqu.iyijiayi.adapter.SearchUserAdapter;
import com.yiqu.iyijiayi.model.GlobleSearch;
import com.yiqu.iyijiayi.model.Music;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.LogUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/17.
 */

public class SearchFragment extends AbsFragment implements View.OnClickListener, NetCallBack {

    private EditText et_search_content;
    private TextView btn_cancel;
    private ListView listview;
    private int count = 0;
    private int rows = 1000;
    private View headerView1;
    private SearchMusicAdapter searchMusicAdapter;
    private String data;


    @Override
    protected int getContentView() {
        return R.layout.find_fragment;
    }

    @Override
    protected void initView(View v) {
        et_search_content = (EditText) v.findViewById(R.id.et_search_content);
        btn_cancel = (TextView) v.findViewById(R.id.btn_cancel);

        listview = (ListView) v.findViewById(R.id.listview);
        searchMusicAdapter = new SearchMusicAdapter(getActivity());
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        TextView textView = new TextView(getActivity());
        textView.setHeight(1);
        textView.setVisibility(View.GONE);
        listview.addHeaderView(textView);


        data = getActivity().getIntent().getStringExtra("data");
        switch (data) {
            case "search_music":
                et_search_content.setHint("搜索伴奏");

                break;
            case "search_user":
                et_search_content.setHint("搜索导师/学员");
                break;
            case "search_sound1":
                et_search_content.setHint("搜索关键词");
                break;
            case "search_sound2":
                et_search_content.setHint("搜索声乐/播音");
                break;
        }


        et_search_content.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    if (TextUtils.isEmpty(et_search_content.getText().toString().trim())) {
                        ToastManager.getInstance(getActivity()).showText("请输入搜索的内容");

                    } else {

                        switch (data) {
                            case "search_music":

                                RestNetCallHelper.callNet(
                                        getActivity(),
                                        MyNetApiConfig.searchMusic,
                                        MyNetRequestConfig.searchItems(getActivity(), et_search_content.getText().toString().trim(), count, rows),
                                        "searchMusic", SearchFragment.this, false, true);

                                break;
                            case "search_user":
                                RestNetCallHelper.callNet(
                                        getActivity(),
                                        MyNetApiConfig.searchUser,
                                        MyNetRequestConfig.searchItems(getActivity(), et_search_content.getText().toString().trim(), count, rows),
                                        "searchUser", SearchFragment.this, false, true);
                                break;
                            case "search_sound1":
                                RestNetCallHelper.callNet(
                                        getActivity(),
                                        MyNetApiConfig.searchSoundByText,
                                        MyNetRequestConfig.searchItems(getActivity(), et_search_content.getText().toString().trim(), count, rows),
                                        "searchSoundByText", SearchFragment.this, false, true);
                                break;
                            case "search_sound2":
                                RestNetCallHelper.callNet(
                                        getActivity(),
                                        MyNetApiConfig.searchByMusicName,
                                        MyNetRequestConfig.searchItems(getActivity(), et_search_content.getText().toString().trim(), count, rows),
                                        "searchByMusicName", SearchFragment.this, false, true);


                                break;

                        }

                    }

                }

                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_music:
                break;


        }

    }

    @Override
    public void onNetNoStart(String id) {

    }

    @Override
    public void onNetStart(String id) {

    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        LogUtils.LOGE("", netResponse.toString());
        switch (id) {
            case "searchMusic":
                if (type == TYPE_SUCCESS) {
                    ArrayList<Music> musics = new Gson().fromJson(netResponse.data, (Type) new TypeToken<ArrayList<Music>>() {
                    }.getType());
                    listview.setAdapter(searchMusicAdapter);
                    searchMusicAdapter.setData(musics);
                    listview.setOnItemClickListener(searchMusicAdapter);

                }

                break;
            case "searchUser":
                if (type == TYPE_SUCCESS) {
                    ArrayList<UserInfo> user = new Gson().fromJson(netResponse.data, (Type) new TypeToken<ArrayList<UserInfo>>() {
                    }.getType());
                    SearchUserAdapter searchUserAdapter = new SearchUserAdapter(getActivity());
                    listview.setAdapter(searchUserAdapter);
                    listview.setOnItemClickListener(searchUserAdapter);
                    searchUserAdapter.setData(user);
                }

                break;
            case "searchByMusicName":

                if (type == TYPE_SUCCESS) {
                    ArrayList<Sound> sound1 = new Gson().fromJson(netResponse.data, (Type) new TypeToken<ArrayList<Sound>>() {
                    }.getType());
                    SearchSoundAdapter searchSoundAdapter = new SearchSoundAdapter(getActivity());
                    listview.setAdapter(searchSoundAdapter);
                    listview.setOnItemClickListener(searchSoundAdapter);
                    searchSoundAdapter.setData(sound1);
                }


                break;
            case "searchSoundByText":
                if (type == TYPE_SUCCESS) {
                    ArrayList<Sound> sound1 = new Gson().fromJson(netResponse.data, (Type) new TypeToken<ArrayList<Sound>>() {
                    }.getType());
                    SearchSoundAdapter searchSoundAdapter = new SearchSoundAdapter(getActivity());
                    listview.setAdapter(searchSoundAdapter);
                    listview.setOnItemClickListener(searchSoundAdapter);
                    searchSoundAdapter.setData(sound1);
                }

                break;
        }

    }
}
