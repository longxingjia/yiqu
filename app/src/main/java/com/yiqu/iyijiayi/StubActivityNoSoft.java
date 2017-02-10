package com.yiqu.iyijiayi;

import android.os.Bundle;
import android.view.WindowManager;

public class StubActivityNoSoft extends StubActivity {

	@Override
	protected void init(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.init(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

}
