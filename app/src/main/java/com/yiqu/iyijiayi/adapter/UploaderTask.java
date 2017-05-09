package com.yiqu.iyijiayi.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2017/3/13.
 */

public class UploaderTask extends AsyncTask<Void, Integer, String> {

    private final String TAG = "UpLoaderTask";
    private Map<String, String> ps;
    private File file;
    private String RequestURL;
    private Context context;
    private static final int TIME_OUT = 10 * 1000; // 超时时间
    private static final String CHARSET = "utf-8"; // 设置编码
//        private DialogHelper dialogHelper;

    public UploaderTask(Context context, String RequestURL, Map<String, String> params, File file) {
        super();
        this.ps = params;
        this.file = file;
        this.RequestURL = RequestURL;
        this.context = context;

    }

    @Override
    protected String doInBackground(Void... params) {

        String result = null;
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);

            // 首先组拼文本类型的参数
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry :  ps.entrySet()) {
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINE_END);
                sb.append("Content-Type: text/plain; charset=" + CHARSET + LINE_END);
                sb.append("Content-Transfer-Encoding: 8bit" + LINE_END);
                sb.append(LINE_END);
                sb.append(entry.getValue());
                sb.append(LINE_END);
            }
            DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
            outStream.write(sb.toString().getBytes());


            if (file != null) {

                //设置对话框描述文字为接口的语言配置

                /**
                 * 当文件不为空，把文件包装并且上传
                 */
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                StringBuffer sb1 = new StringBuffer();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY);
                sb1.append(LINE_END);
                /**
                 * 这里重点注意： name里面的值为服务端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的 比如:abc.png
                 */
                sb1.append("Content-Disposition: form-data; name=\"Upload[file]\"; filename=\""
                        + file.getName() + "\"" + LINE_END);
                sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
                sb1.append(LINE_END);
                dos.write(sb1.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                long progress = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                    progress = progress + 1024;

                    long percent = progress * 100 / file.length();
                    if (percent > 100) {
                        percent = 100;
                    }
                    publishProgress((int)percent);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();
                /**
                 * 获取响应码 200=成功 当响应成功，获取响应的流
                 */
                int res = conn.getResponseCode();
//                Log.e(TAG, "response code:" + res);
                if (res == 200) {
                    Log.w(TAG, "request success");
                    InputStream input = conn.getInputStream();
                    StringBuffer sb2 = new StringBuffer();
                    int ss;
                    while ((ss = input.read()) != -1) {
                        sb2.append((char) ss);
                    }
                    result = sb2.toString();
                    Log.d(TAG, "result : " + result);
                } else {
                    Log.e(TAG, "request error");
                }

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String bool = jsonObject.getString("bool");
                    String data = jsonObject.getString("data");
                    String re = jsonObject.getString("result");
                    if (bool.equals("1")) {
                        return new JSONObject(data).getString("filepath");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

        super.onProgressUpdate(values);
    }
}
