package com.yiqu.Tool.Function;

import android.app.Application;

import com.utils.Variable;

import java.nio.ByteOrder;

/**
 * Created by  on 2016/05/24.
 */
public class InitFunction {
    public static void TestCPU() {
        if (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) {
            Variable.isBigEnding = true;
        } else {
            Variable.isBigEnding = false;
        }
    }

    public static synchronized void Initialise(Application application) {
        TestCPU();

        //FileFunction.InitStorage(application);

        LogFunction.UpdateErrorOutputStream(application);
    }
}
