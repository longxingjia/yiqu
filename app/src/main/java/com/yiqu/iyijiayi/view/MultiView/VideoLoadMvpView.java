package com.yiqu.iyijiayi.view.MultiView;

import android.media.MediaPlayer;


/**
 * @author Wayne
 */
public interface VideoLoadMvpView {

//    TextureVideoView getVideoView();

    void videoBeginning();

    void videoStopped();

    void videoPrepared(MediaPlayer player);

    void videoResourceReady(String videoPath);
}
