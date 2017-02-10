package com.yiqu.iyijiayi.utils;

import android.media.MediaPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/5/17.
 */
public class ItemListJavaScriptHandler
{
    private MediaPlayer player;
    private InputStream      m_videoInputStream;
    private File             m_fileCacheDir;

    private File tempMp3  = null;
    private String m_strFileUrl = null;
    public void setResPath(InputStream videoInputStream)
    {
        m_videoInputStream = videoInputStream;
    }

    public void getFileCacheDir(File fileCacheDir)
    {
        m_fileCacheDir = fileCacheDir;

    }
    public String getUrl()
    {
        return m_strFileUrl;
    }

    public void goItem(String url, String parentId, String type) throws IOException
    {
        m_strFileUrl = url;
        tempMp3 = File.createTempFile("kurchina", "mp3", m_fileCacheDir );
        FileOutputStream fos = new FileOutputStream(tempMp3,true);
        int c = m_videoInputStream.read();
        while(c != -1)
        {
            c = m_videoInputStream.read();
            fos.write(c);
        }

        fos.close();
        m_videoInputStream.close();

        if (player != null) {
            player.stop();
            player = null;
        }

        FileInputStream fis = null;
        if (type.equalsIgnoreCase("audio"))
        {
            try
            {
                player = new MediaPlayer();

                fis = new FileInputStream(tempMp3);
                player.setDataSource(fis.getFD());
                player.prepare();
                player.start()                                                                                                                                                                     ;
            }
            catch (IllegalArgumentException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IllegalStateException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}