package com.tool.general.excel.util;

import java.math.BigDecimal;


/**
 * @author mengqiang
 */
public class GenExcelDecimalUtil {

    /**
     * 获得BigDecimal型变量
     *
     * @param intNum 整形变量
     * @return BigDecimal型变量
     */
    public static BigDecimal valueOf(int intNum) {
        return new BigDecimal(intNum);
    }

    /**
     * 获得BigDecimal型变量
     *
     * @param logNum 长整形变量
     * @return BigDecimal型变量
     */
    public static BigDecimal valueOf(long logNum) {
        return new BigDecimal(logNum);
    }

    /**
     * 获得BigDecimal型变量
     *
     * @param dolNum 双精度变量
     * @return BigDecimal型变量
     */
    public static BigDecimal valueOf(double dolNum) {
        return new BigDecimal(dolNum);
    }

    /**
     * 获得BigDecimal型变量 空---返回0 非数字类型---返回0 数字类型----正常返回
     *
     * @param strNum 字符串型变量
     * @return BigDecimal型变量
     */
    public static BigDecimal valueOf(String strNum) {
        if (null == strNum || "".equals(strNum)) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(strNum);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    /**
     * 加法
     *
     * @param bigNum1 被加数
     * @param bigNum2 加数
     * @return 结果
     */
    public static BigDecimal add(BigDecimal bigNum1, BigDecimal bigNum2) {
        if (bigNum1 == null) {
            bigNum1 = BigDecimal.ZERO;
        }
        if (bigNum2 == null) {
            bigNum2 = BigDecimal.ZERO;
        }
        return bigNum1.add(bigNum2);
    }

    /**
     * 减法
     *
     * @param bigNum1 被减数
     * @param bigNum2 减数
     * @return 结果
     */
    public static BigDecimal subtract(BigDecimal bigNum1, BigDecimal bigNum2) {
        if (bigNum1 == null) {
            bigNum1 = BigDecimal.ZERO;
        }
        if (bigNum2 == null) {
            bigNum2 = BigDecimal.ZERO;
        }
        return bigNum1.subtract(bigNum2);
    }

    /**
     * 乘法
     *
     * @param bigNum1 乘数
     * @param bigNum2 乘数
     * @return 结果
     */
    public static BigDecimal multiply(BigDecimal bigNum1, BigDecimal bigNum2) {
        return bigNum1.multiply(bigNum2);
    }

    /**
     * 除法
     *
     * @param bigNum1 被除数
     * @param bigNum2 除数
     * @return 结果
     */
    public static BigDecimal divide(BigDecimal bigNum1, BigDecimal bigNum2) {
        if (BigDecimal.ZERO.equals(bigNum2)) {
            return BigDecimal.ZERO;
        }
        return bigNum1.divide(bigNum2, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * 除法
     *
     * @param bigNum1 被除数
     * @param bigNum2 除数
     * @return 结果
     */
    public static BigDecimal divide(BigDecimal bigNum1, BigDecimal bigNum2, int scale) {
        if (BigDecimal.ZERO.equals(bigNum2)) {
            return BigDecimal.ZERO;
        }
        return bigNum1.divide(bigNum2, scale, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * 保留小数
     *
     * @param bigNum   数字
     * @param intDigit 小数位数
     * @return 结果
     */
    public static BigDecimal setScale(BigDecimal bigNum, int intDigit) {
        return bigNum.setScale(intDigit, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 比较大小， 参数1>参数2 返回1， 参数1=参数2 返回0 参数1<参数2 返回-1
     *
     * @param bigNum1 参数1
     * @param bigNum2 参数2
     * @return 结果
     */
    public static int compareTo(BigDecimal bigNum1, BigDecimal bigNum2) {
        if (bigNum1 == null) {
            bigNum1 = BigDecimal.ZERO;
        }
        if (bigNum2 == null) {
            bigNum2 = BigDecimal.ZERO;
        }
        return bigNum1.compareTo(bigNum2);
    }

    /**
     * 比较大小，参数1>参数2 返回true
     *
     * @param bigNum1
     * @param bigNum2
     * @return
     */
    public static boolean isGreater(BigDecimal bigNum1, BigDecimal bigNum2) {
        if (bigNum1 == null) {
            bigNum1 = BigDecimal.ZERO;
        }
        if (bigNum2 == null) {
            bigNum2 = BigDecimal.ZERO;
        }
        return bigNum1.compareTo(bigNum2) > 0;
    }

    /**
     * 比较大小，参数1>=参数2 返回true
     *
     * @param bigNum1
     * @param bigNum2
     * @return
     */
    public static boolean isGreaterOrEqual(BigDecimal bigNum1, BigDecimal bigNum2) {
        if (bigNum1 == null) {
            bigNum1 = BigDecimal.ZERO;
        }
        if (bigNum2 == null) {
            bigNum2 = BigDecimal.ZERO;
        }
        return bigNum1.compareTo(bigNum2) >= 0;
    }

    /**
     * 比较大小，参数1<参数2 返回true
     *
     * @param bigNum1
     * @param bigNum2
     * @return
     */
    public static boolean isLess(BigDecimal bigNum1, BigDecimal bigNum2) {
        if (bigNum1 == null) {
            bigNum1 = BigDecimal.ZERO;
        }
        if (bigNum2 == null) {
            bigNum2 = BigDecimal.ZERO;
        }
        return bigNum1.compareTo(bigNum2) < 0;
    }

    /**
     * 比较大小，参数1<=参数2 返回true
     *
     * @param bigNum1
     * @param bigNum2
     * @return
     */
    public static boolean isLessOrEqual(BigDecimal bigNum1, BigDecimal bigNum2) {
        if (bigNum1 == null) {
            bigNum1 = BigDecimal.ZERO;
        }
        if (bigNum2 == null) {
            bigNum2 = BigDecimal.ZERO;
        }
        return bigNum1.compareTo(bigNum2) <= 0;
    }

    /**
     * 比较大小，相等 返回true
     *
     * @param bigNum1
     * @param bigNum2
     * @return
     */
    public static boolean isEqual(BigDecimal bigNum1, BigDecimal bigNum2) {
        if (bigNum1 == null) {
            bigNum1 = BigDecimal.ZERO;
        }
        if (bigNum2 == null) {
            bigNum2 = BigDecimal.ZERO;
        }
        return bigNum1.compareTo(bigNum2) == 0;
    }

    /**
     * 空转0
     *
     * @param decimal
     * @return
     */
    public static BigDecimal null2Zero(BigDecimal decimal) {
        return decimal == null ? BigDecimal.ZERO : decimal;
    }

    /**
     * 分转元
     *
     * @param moneyByPoints
     * @return
     */
    public static BigDecimal points2Yuan(Integer moneyByPoints) {
        if (moneyByPoints == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal pointsMoneyBig = GenExcelDecimalUtil.valueOf(moneyByPoints);
        return GenExcelDecimalUtil.divide(pointsMoneyBig, GenExcelDecimalUtil.valueOf(100), 2);
    }

    /**
     * 元转分
     *
     * @param yuan
     * @return
     */
    public static BigDecimal yuan2points(BigDecimal yuan) {
        if (yuan == null) {
            return BigDecimal.ZERO;
        }

        return GenExcelDecimalUtil.multiply(yuan, new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 判断bigDecimal在0和1之间
     *
     * @param bigDecimal
     * @return
     */
    public static Boolean greaterBigDecimal(BigDecimal bigDecimal) {
        if (bigDecimal.compareTo(BigDecimal.ZERO) >= 0 && bigDecimal.compareTo(BigDecimal.ONE) < 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 保留两位小数 - 百分率
     * 示例：8% 、0.89%
     *
     * @param bigDecimal
     * @return
     */
    public static String percentage(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return String.valueOf(BigDecimal.ZERO).concat(GenExcelStringPool.PERCENT);
        }
        // 1.乘以100
        // 2.保留两位小数
        // 3.去除末尾多余的0
        // 4.拼接百分号
        return GenExcelDecimalUtil.setScale(bigDecimal.multiply(new BigDecimal(100)), 2)
                .stripTrailingZeros().toPlainString()
                .concat(GenExcelStringPool.PERCENT);
    }

    /**
     * 保留两位小数 - 百分率
     * 示例：8 、0.89
     *
     * @param bigDecimal
     * @return
     */
    public static BigDecimal percentageB(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return BigDecimal.ZERO;
        }
        // 1.乘以100
        // 2.保留两位小数
        // 3.去除末尾多余的0
        return GenExcelDecimalUtil.setScale(bigDecimal.multiply(new BigDecimal(100)), 2)
                .stripTrailingZeros();
    }

}
