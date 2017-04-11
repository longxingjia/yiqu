package com.yiqu.iyijiayi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;

public class CommentActivity extends Activity implements NetCallBack {


    private Button butt2;
    private EditText edit;
    private TextView test_emoji;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        setFinishOnTouchOutside(true);//
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
        p.height = (int) (d.getHeight() * 0.2); // 高度设置为屏幕的0.3
        p.width = (int) (d.getWidth()); // 宽度设置为屏幕
        p.alpha = 1.0f;      //设置本身透明度
        p.dimAmount = 0.5f;      //设置黑暗度
        getWindow().setAttributes(p);
        getWindow().setGravity(Gravity.BOTTOM);       //设置靠右对齐
        edit = (EditText) findViewById(R.id.edit_text);
        test_emoji = (TextView) findViewById(R.id.test_emoji);

//        edit.setFocusable(true);
//        edit.setFocusableInTouchMode(true);
//        edit.requestFocus();
//        InputMethodManager inputManager = (InputMethodManager) edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//   //     inputManager.showSoftInput(edit, 0);
//        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//        try{
//            Typeface typefaceDroidSansFallback= Typeface.createFromAsset(getResources().getAssets(), "fonts/NotoColorEmoji.ttf");
//            edit.setTypeface(typefaceDroidSansFallback);
////                mContentTextView.setTypeface(typefaceDroidSansFallback);
//        }catch(Exception ex){
//            Log.i("EmojiTest", "Catch Exception!");
//        }


        Intent intent = getIntent();
        final String sid = intent.getStringExtra("sid");
        final String fromuid = intent.getStringExtra("fromuid");
        final String touid = intent.getStringExtra("touid");
        final String toname = intent.getStringExtra("toname");

        if (!TextUtils.isEmpty(toname)) {

            edit.setHint("回复 " + toname + ":");
            edit.setHintTextColor(getResources().getColor(R.color.dd_gray));

        }
        String s =AppShare.getLastComment(this);
        if (!TextUtils.isEmpty(s)){
            edit.setText(s);
        }


        butt2 = (Button) findViewById(R.id.butt2);
        butt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                test_emoji.setText(edit.getText().toString());
//                String s = EmojiFilter.filterEmoji(edit.getText().toString());
                String s = edit.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    ToastManager.getInstance(CommentActivity.this).showText("请填写你的文字后再提交");
                    return;
                }

                RestNetCallHelper.callNet(CommentActivity.this,
                        MyNetApiConfig.addComment, MyNetRequestConfig
                                .addComment(CommentActivity.this, sid, fromuid, touid, s),
                        "addComment", CommentActivity.this, false, true);
//                去除软键盘显示
//                edit.clearFocus();
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
            }
        });


//        View decorView = getWindow().getDecorView();
//        View contentView = findViewById(Window.ID_ANDROID_CONTENT);
//        decorView.getViewTreeObserver().addOnGlobalLayoutListener(getGlobalLayoutListener(decorView, contentView));
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("评论"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("评论"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
        AppShare.setLastComment(this,edit.getText().toString());
    }

    private ViewTreeObserver.OnGlobalLayoutListener getGlobalLayoutListener(final View decorView, final View contentView) {
        return new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                decorView.getWindowVisibleDisplayFrame(r);

                int height = decorView.getContext().getResources().getDisplayMetrics().heightPixels;
                int diff = (height - r.bottom) / 2;

                if (diff != 0) {
                    if (contentView.getPaddingBottom() != diff) {
                        contentView.setPadding(0, 0, 0, diff);
                    }
                } else {
                    if (contentView.getPaddingBottom() != 0) {
                        contentView.setPadding(0, 0, 0, 0);
                    }
                }
            }
        };
    }


    @Override
    public void onNetNoStart(String id) {

    }

    @Override
    public void onNetStart(String id) {

    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {

        if (type == TYPE_SUCCESS) {
            ToastManager.getInstance(CommentActivity.this).showText(netResponse.result.toString());
            edit.setText("");
            finish();
        }

    }
}
