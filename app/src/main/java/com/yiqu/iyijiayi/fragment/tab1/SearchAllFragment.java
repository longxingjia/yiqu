package com.yiqu.iyijiayi.fragment.tab1;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.ui.abs.AbsFragment;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.adapter.SearchMusicAdapter;
import com.yiqu.iyijiayi.adapter.SearchSoundAdapter;
import com.yiqu.iyijiayi.adapter.SearchUserAdapter;
import com.yiqu.iyijiayi.fragment.Tab1Fragment;
import com.yiqu.iyijiayi.model.GlobleSearch;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.LogUtils;

/**
 * Created by Administrator on 2017/3/17.
 */

public class SearchAllFragment extends AbsFragment implements View.OnClickListener {

    private EditText et_search_content;
    private TextView btn_cancel;
    private ListView musicListView;
    private SearchMusicAdapter searchMusicAdapter;
    private ListView sound1List;
    private ListView sound2List;
    private ListView userList;
    private View headerView1;
    private View headerView2;
    private View headerView3;
    private View headerView4;
    private LinearLayout search_ll;

    @Override
    protected int getContentView() {
        return R.layout.find_all_fragment;
    }

    @Override
    protected void initView(View v) {
        et_search_content = (EditText) v.findViewById(R.id.et_search_content);
        btn_cancel = (TextView) v.findViewById(R.id.btn_cancel);


        musicListView = (ListView) v.findViewById(R.id.musicList);
        sound1List = (ListView) v.findViewById(R.id.sound1List);
        sound2List = (ListView) v.findViewById(R.id.sound2List);
        userList = (ListView) v.findViewById(R.id.userList);
        search_ll = (LinearLayout) v.findViewById(R.id.search_ll);
        searchMusicAdapter = new SearchMusicAdapter(getActivity());
        musicListView.setAdapter(searchMusicAdapter);
        musicListView.setOnItemClickListener(searchMusicAdapter);
        v.findViewById(R.id.search_music).setOnClickListener(this);
        v.findViewById(R.id.search_user).setOnClickListener(this);
        v.findViewById(R.id.search_sound1).setOnClickListener(this);
        v.findViewById(R.id.search_sound2).setOnClickListener(this);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        et_search_content.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    RestNetCallHelper.callNet(
                            getActivity(),
                            MyNetApiConfig.searchByText,
                            MyNetRequestConfig.searchByText(getActivity(), et_search_content.getText().toString().trim()),
                            "searchByText", new NetCallBack() {
                                @Override
                                public void onNetNoStart(String id) {

                                }

                                @Override
                                public void onNetStart(String id) {

                                }

                                @Override
                                public void onNetEnd(String id, int type, NetResponse netResponse) {
                                    LogUtils.LOGE("a", netResponse.toString());
                                    if (type == NetCallBack.TYPE_SUCCESS) {

                                        GlobleSearch globleSearch = new Gson().fromJson(netResponse.data, GlobleSearch.class);
                                        if (globleSearch != null) {
                                            search_ll.setVisibility(View.GONE);
                                        }
                                        if (globleSearch.music != null && globleSearch.music.size() > 0) {
                                            searchMusicAdapter.setData(globleSearch.music);
                                            if (headerView1 == null) {
                                                headerView1 = getActivity().getLayoutInflater().inflate(R.layout.search_headview, null);
                                                musicListView.addHeaderView(headerView1);

                                                TextView music = (TextView) headerView1.findViewById(R.id.music);
                                                music.setText("伴奏");
                                            }
                                        }
                                        if (globleSearch.sound1 != null && globleSearch.sound1.size() > 0) {
                                            SearchSoundAdapter searchSoundAdapter = new SearchSoundAdapter(getActivity());
                                            sound1List.setAdapter(searchSoundAdapter);
                                            sound1List.setOnItemClickListener(searchSoundAdapter);
                                            searchSoundAdapter.setData(globleSearch.sound1);
                                            if (headerView2 == null) {
                                                headerView2 = getActivity().getLayoutInflater().inflate(R.layout.search_headview, null);
                                                sound1List.addHeaderView(headerView2);
                                                TextView music = (TextView) headerView2.findViewById(R.id.music);
                                                music.setText("问题和作品（根据名称）");
                                            }
                                        }

                                        if (globleSearch.sound2 != null && globleSearch.sound2.size() > 0) {
                                            SearchSoundAdapter searchSoundAdapter = new SearchSoundAdapter(getActivity());
                                            sound2List.setAdapter(searchSoundAdapter);
                                            searchSoundAdapter.setData(globleSearch.sound2);
                                            if (headerView3 == null) {
                                                headerView3 = getActivity().getLayoutInflater().inflate(R.layout.search_headview, null);
                                                sound2List.addHeaderView(headerView3);
                                                TextView music = (TextView) headerView3.findViewById(R.id.music);
                                                music.setText("问题和作品（根据描述）");
                                            }
                                        }

                                        if (globleSearch.user != null && globleSearch.user.size() > 0) {
                                            SearchUserAdapter searchUserAdapter = new SearchUserAdapter(getActivity());
                                            userList.setAdapter(searchUserAdapter);
                                            userList.setOnItemClickListener(searchUserAdapter);
                                            searchUserAdapter.setData(globleSearch.user);
                                            if (headerView4 == null) {
                                                headerView4 = getActivity().getLayoutInflater().inflate(R.layout.search_headview, null);
                                                userList.addHeaderView(headerView4);
                                                TextView music = (TextView) headerView4.findViewById(R.id.music);
                                                music.setText("老师/学生");

                                            }
                                        }

                                    }

                                }
                            }, false, true);
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), StubActivity.class);
        intent.putExtra("fragment", SearchFragment.class.getName());
        switch (v.getId()) {

            case R.id.search_music:

                intent.putExtra("data","search_music");
                getActivity().startActivity(intent);
                getActivity().finish();
                break;
            case R.id.search_user:
                intent.putExtra("data","search_user");
                getActivity().startActivity(intent);
                getActivity().finish();
                break;
            case R.id.search_sound1:
                intent.putExtra("data","search_sound1");
                getActivity().startActivity(intent);
                getActivity().finish();
                break;
            case R.id.search_sound2:
                intent.putExtra("data","search_sound2");
                getActivity().startActivity(intent);
                getActivity().finish();
                break;


        }

    }
}
