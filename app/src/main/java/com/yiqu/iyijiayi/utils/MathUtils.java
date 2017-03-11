package com.yiqu.iyijiayi.utils;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/3/10.
 */

public class MathUtils {

    /**
     * 四舍五入
     * @param totalTime 秒
     * @param currentTime
     * @return
     */
    public static int sub(int totalTime, int currentTime) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(totalTime));
        BigDecimal b2 = new BigDecimal(Double.valueOf(currentTime) / 1000);
        return b1.subtract(b2).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }
}
