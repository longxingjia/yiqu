package com.yiqu.iyijiayi.fragment.tab1;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.ui.abs.AbsFragment;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.adapter.SearchMusicAdapter;
import com.yiqu.iyijiayi.fragment.Tab1Fragment;
import com.yiqu.iyijiayi.model.GlobleSearch;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.LogUtils;

/**
 * Created by Administrator on 2017/3/17.
 */

public class FindAllFragment extends AbsFragment {

    private EditText et_search_content;
    private TextView btn_cancel;
    private ListView musicListView;
    private SearchMusicAdapter searchMusicAdapter;


    @Override
    protected int getContentView() {
        return R.layout.find_all_fragment;
    }

    @Override
    protected void initView(View v) {
        et_search_content = (EditText) v.findViewById(R.id.et_search_content);
        btn_cancel = (TextView) v.findViewById(R.id.btn_cancel);


        musicListView = (ListView) v.findViewById(R.id.musicList);
        searchMusicAdapter = new SearchMusicAdapter(getActivity());
        musicListView.setAdapter(searchMusicAdapter);


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
                                        if (globleSearch.music != null) {
                                            searchMusicAdapter.setData(globleSearch.music);

                                            View headerView = getActivity().getLayoutInflater().inflate(R.layout.search_headview, null);
                                            musicListView.addHeaderView(headerView);
                                            TextView music = (TextView) headerView.findViewById(R.id.music);
                                            music.setText("伴奏");

                                        }


                                    }

                                }
                            }, false, true);
                }
                return false;
            }
        });

    }
}
