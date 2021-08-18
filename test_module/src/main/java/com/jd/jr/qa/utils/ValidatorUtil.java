package com.jd.jr.qa.utils;


import com.jd.jr.qa.constants.enums.ResponseEnum;
import com.jd.jr.qa.constants.exception.CheckException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * Created by guanchenglong on 2016/9/3.
 */
@Component
public final class ValidatorUtil {
    /**
     * 正则表达式：验证手机号
     */
//    public static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
    public static final String REGEX_MOBILE = "^((1[0-9][0-9]))\\d{8}$";

    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 正则表达式：验证IP地址
     */
    public static final String REGEX_IP_ADDR = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
    /**
     * 正则表达式：数字
     */
    public static final String REGEX_IS_NUMBER = "^[0-9]*$";
    /**
     * 授权验证：只能是0与1
     */
    public static final String REGEX_IS_AUTH = "^[0-1]*$";
    /**
     * 身份证验证：
     */
    public static final String REGEX_CERT_ID = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";
    public static final String REGEX_CERT_ID_18 = "(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";
    /**
     * 护照规则：
     */
    public static final String REGEX_PASSPORT_1 = "^[a-zA-Z]{5,17}$";
    public static final String REGEX_PASSPORT_2 = "^[a-zA-Z0-9]{5,17}$";
    /**
     * 港澳通行证规则：
     */
    public static final String REGEX_HKMACAO = "^[HMhm]{1}([0-9]{10}|[0-9]{8})$";

    /**
     * 回乡证规则：
     */
    public static final String REGEX_HOME_RETURN = "^[HMhm]{1}([0-9]{1,10})$";
    /**
     * 台湾通行证验证
     */
    public static final String REGEX_TAIWAN_1 = "^[0-9]{8}$";
    public static final String REGEX_TAIWAN_2 = "^[0-9]{10}$";
    /**
     * 驾照
     */
    public static final String REGEX_PASSPORT = "^[\\u4e00-\\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$";
    /**
     * 外国人永久居留身份证
     */
    public static final String REGEX_FOREIGNER = "^[a-zA-Z]{3}\\d{12}$";

    /**
     * 校验必填参数
     *
     * @param c 检验入参中是否包含空字符串
     * @return 都不为空时返回true，任意一个为空就会返回false
     */
    public static boolean isAnyEmpty(CharSequence... c) throws Exception {
        boolean isAnyEmpty = StringUtils.isAnyEmpty( c );
        if (isAnyEmpty) {
            throw new Exception( ResponseEnum.ARG_IS_NULL_ERROR.toJson() );
        }
        return false;
    }


    public static void isNull(Object obj, ResponseEnum resultCode, String message) throws Exception {
        if (obj == null) {
            throw new Exception( message, new CheckException( resultCode, message ) );
        }
    }


    public static void isBlank(String value, ResponseEnum resultCode, String message) throws Exception {
        if (StringUtils.isBlank( value )) {
//            throw new Exception( message,new CheckException(resultCode,message) );
            throw new Exception( message, new CheckException( resultCode, message ) );
        }
    }

    public static void isSame(String expect, String actual, ResponseEnum resultCode, String message) throws Exception {
        if (expect.equals( actual )) {
//            throw new Exception( message,new CheckException(resultCode,message) );
            throw new Exception( message, new CheckException( resultCode, message ) );
        }
    }

    public static void isNotBlack(String value, ResponseEnum resultCode, String message) throws Exception {
        if (StringUtils.isNotBlank( value )) {
            throw new Exception( message, new CheckException( resultCode, message ) );
        }
    }

    public static void isFalse(boolean condition, ResponseEnum resultCode, String message) throws Exception {
        if (condition) {
            throw new Exception( message, new CheckException( resultCode, message ) );
        }
    }

    public static void isTrue(boolean condition, ResponseEnum resultCode, String message) throws Exception {
        if (!condition) {
            throw new Exception( message, new CheckException( resultCode, message ) );
        }
    }

    public static void isNotBothBlank(String value1, String value2, ResponseEnum resultCode, String message) throws Exception {
        if (StringUtils.isBlank( value1 ) && StringUtils.isBlank( value2 )) {
            throw new Exception( message, new CheckException( resultCode, message ) );
        }
    }

    /**
     * 非空时，不超过指定长度
     *
     * @param value      要校验的字符串
     * @param length     限制长度（大于该长度将校验失败）
     * @param resultCode 错误码
     * @param message    错误描述
     */
    public static void isLessLength(String value, int length, ResponseEnum resultCode, String message) throws Exception {
        if (!StringUtils.isBlank( value ) && value.length() > length) {
            throw new Exception( message, new CheckException( resultCode, message ) );
        }
    }

    /**
     * 验证手机号
     *
     * @param mobile
     * @param resultCode
     * @param message
     */
    public static void isMobile(String mobile, ResponseEnum resultCode, String message) throws Exception {
        if (StringUtils.isNotBlank( mobile ) && !Pattern.matches( REGEX_MOBILE, mobile )) {
            throw new Exception( message, new CheckException( resultCode, message ) );
        }
    }

    /**
     * 验证邮箱地址
     *
     * @param email
     * @param resultCode
     * @param message
     */
    public static void isEmail(String email, ResponseEnum resultCode, String message) throws Exception {
        if (StringUtils.isNotBlank( email ) && !Pattern.matches( REGEX_EMAIL, email )) {
            throw new Exception( message, new CheckException( resultCode, message ) );
        }
    }

    /**
     * 验证IP地址
     *
     * @param ipAddr
     * @param resultCode
     * @param message
     */
    public static void isIPAddr(String ipAddr, ResponseEnum resultCode, String message) throws Exception {
        if (StringUtils.isNotBlank( ipAddr ) && !Pattern.matches( REGEX_IP_ADDR, ipAddr )) {
            throw new Exception( message, new CheckException( resultCode, message ) );
        }
    }

    /**
     * 验证外国人永久居留身份证
     *
     * @param foreigner
     * @param resultCode
     * @param message
     */
    public static void isForeigner(String foreigner, ResponseEnum resultCode, String message) throws Exception {
        if (StringUtils.isNotBlank( foreigner ) && !Pattern.matches( REGEX_FOREIGNER, foreigner )) {
            throw new Exception( message, new CheckException( resultCode, message ) );
        }
    }

    public static void isNotStartWithBlankChar(String str, ResponseEnum resultCode, String message) throws Exception {
        if (str != null && Character.isWhitespace( str.charAt( 0 ) )) {
            throw new Exception( message, new CheckException( resultCode, message ) );
        }
    }

    public static void isNotEndWithBlankChar(String str, ResponseEnum resultCode, String message) throws Exception {
        if (str != null && Character.isWhitespace( str.charAt( str.length() - 1 ) )) {
            throw new Exception( message, new CheckException( resultCode, message ) );
        }
    }

    /**
     * 验证数字输入
     *
     * @param number
     * @param resultCode
     * @param message
     */
    public static void isNumber(String number, ResponseEnum resultCode, String message) throws Exception {
        if (StringUtils.isNotBlank( number ) && !Pattern.matches( REGEX_IS_NUMBER, number )) {
            throw new Exception( message, new CheckException( resultCode, message ) );
        }
    }

    /**
     * 验证0与1
     *
     * @param number
     * @param resultCode
     * @param message
     */
    public static void isAuth(String number, ResponseEnum resultCode, String message) throws Exception {
        if (StringUtils.isNotBlank( number ) && !Pattern.matches( REGEX_IS_AUTH, number )) {
            throw new Exception( message, new CheckException( resultCode, message ) );
        }
    }

    /**
     * 验证身份证件号
     * 说明：兼容15位与18位。
     *
     * @param certNo
     * @param resultCode
     * @param message
     */
    public static void isIdCard1518(String certNo, ResponseEnum resultCode, String message) throws Exception {
        if (StringUtils.isNotBlank( certNo ) && !Pattern.matches( REGEX_CERT_ID, certNo )) {
            throw new Exception( message, new CheckException( resultCode, message ) );
        }
    }

    /**
     * 护照验证
     *
     * @param value
     * @param resultCode
     * @param message
     */
    public static void isPassport(String value, ResponseEnum resultCode, String message) throws Exception {
        if (StringUtils.isNotBlank( value ) && !Pattern.matches( REGEX_PASSPORT_1, value ) && !Pattern.matches( REGEX_PASSPORT_2, value )) {
            throw new Exception( message, new CheckException( resultCode, message ) );
        }
    }

    /**
     * 港澳通行证验证
     *
     * @param value
     * @param resultCode
     * @param message
     */
    public static void isHKMacao(String value, ResponseEnum resultCode, String message) throws Exception {
        if (StringUtils.isNotBlank( value ) && !Pattern.matches( REGEX_HKMACAO, value )) {
            throw new Exception( message, new CheckException( resultCode, message ) );
        }
    }

    /**
     * 证验证
     *
     * @param value
     * @param resultCode
     * @param message
     */
    public static void isHomeReturn(String value, ResponseEnum resultCode, String message) throws Exception {
        if (StringUtils.isNotBlank( value ) && !Pattern.matches( REGEX_HOME_RETURN, value )) {
            throw new Exception( message, new CheckException( resultCode, message ) );
        }
    }

    /**
     * 台湾通行证验证
     *
     * @param value
     * @param resultCode
     * @param message
     */
    public static void isTaiwan(String value, ResponseEnum resultCode, String message) throws Exception {
        if (StringUtils.isNotBlank( value ) && !Pattern.matches( REGEX_TAIWAN_1, value ) && !Pattern.matches( REGEX_TAIWAN_2, value )) {
            throw new Exception( message, new CheckException( resultCode, message ) );
        }
    }

    /**
     * 驾照
     *
     * @param value
     * @param resultCode
     * @param message
     */
    public static void isDriverLicense(String value, ResponseEnum resultCode, String message) throws Exception {
        if (StringUtils.isNotBlank( value ) && !Pattern.matches( REGEX_PASSPORT, value )) {
            throw new Exception( message, new CheckException( resultCode, message ) );
        }
    }

    /**
     * 固定位数限制
     *
     * @param value
     * @param resultCode
     * @param message
     */
    public static void isEqualsLength(String value, int length, ResponseEnum resultCode, String message) throws Exception {
        if (StringUtils.isNotBlank( value ) && !(value.length() == length)) {
            throw new Exception( message, new CheckException( resultCode, message ) );
        }
    }

}
