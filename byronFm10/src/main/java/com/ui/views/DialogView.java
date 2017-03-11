package com.ui.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.byron.framework.R;

/**
 * Created by Administrator on 2017/3/10.
 */

public class DialogView extends Dialog {
    Context context;

    public DialogView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    public DialogView(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog);
    }


}
