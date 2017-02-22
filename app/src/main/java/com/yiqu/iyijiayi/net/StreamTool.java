package com.yiqu.iyijiayi.net;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by L on 2017/2/22.
 */

public class StreamTool

{

    /**

     * 从输入流读取数据

     * @param inStream

     * @return

     * @throws Exception

     */

    public static byte[] readInputStream(InputStream inStream) throws Exception{

        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];

        int len = 0;

        while( (len = inStream.read(buffer)) !=-1 ){

            outSteam.write(buffer, 0, len);

        }

        outSteam.close();

        inStream.close();

        return outSteam.toByteArray();

    }

}