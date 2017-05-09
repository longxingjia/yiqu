package com.yiqu.Tool.Player;

import com.model.Music;
import com.yiqu.iyijiayi.model.Sound;

/**
 * Created by Administrator on 2017/5/9.
 */

public interface OnPlayMusicClickListener {
    public void onPlayClick(Music sound);
    public void onPauseClick(Music sound);
}