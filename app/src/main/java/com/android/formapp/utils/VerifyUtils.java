package com.android.formapp.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验工具类
 * Created by zhuofeng on 2015/4/10.
 */
public class VerifyUtils {

    /**
     * 手机号位数
     */
    private static final int PHONE_LENGTH = 11;

    /**
     * 简单手机正则校验
     *
     * @param MobileNo 手机号
     * @return
     */
    public static boolean isValidMobileNo(@NonNull String MobileNo) {
        MobileNo = MobileNo.replace("-", "");
        String regPattern = "^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9]|19[8|9]|16[6])\\d{8}$";//"^1[3-9]\\d{9}$";
        return Pattern.matches(regPattern, MobileNo);

    }

    /**
     * 验证手机号码
     * <p>
     * 移动号码段:139、138、137、136、135、134、150、151、152、157、158、159、182、183、187、188、147、198
     * 联通号码段:130、131、132、136、185、186、145、166
     * 电信号码段:133、153、180、189、199
     *
     * @param cellphone 手机号码
     * @return false，格式错误；true，格式正确
     * @author caoyoulin
     */
    public static boolean checkCellphone(String cellphone) {
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9])|(19[8|9]|(16[6])))\\d{8}$";
        if (cellphone.length() != PHONE_LENGTH) {
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(cellphone);
            boolean isMatch = m.matches();
            if (isMatch) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 8-12位数字和字母混合密码正则校验
     *
     * @param Pwd 密码
     * @return
     */
    public static boolean isValidPwd(@NonNull String Pwd) {

        Pattern pat = Pattern.compile("[\\da-zA-Z]{8,12}");
        Pattern patno = Pattern.compile(".*\\d.*");
        Pattern paten = Pattern.compile(".*[a-zA-Z].*");
        Matcher mat = pat.matcher(Pwd);
        Matcher matno = patno.matcher(Pwd);
        Matcher maten = paten.matcher(Pwd);
        return (matno.matches() && maten.matches() && mat.matches());
    }
//^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$
    /**
     * 8-12位数字和字母混合密码正则校验
     *
     * @param Pwd 密码
     * @return
     */
    public static boolean isLoginPwd(@NonNull String Pwd) {

        Pattern pat = Pattern.compile("[\\da-zA-Z]{6,18}");
        Pattern patno = Pattern.compile(".*\\d.*");
        Pattern paten = Pattern.compile(".*[a-zA-Z].*");
        Matcher mat = pat.matcher(Pwd);
        Matcher matno = patno.matcher(Pwd);
        Matcher maten = paten.matcher(Pwd);
        return (matno.matches() && maten.matches() && mat.matches());
    }

    /**
     * 判断邮箱格式是否正确：
     */
    public static boolean isValidEmail(@NonNull String email) {
        //"^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        if (!email.contains("@")) {
            return false;
        }

        String str = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * Luhn算法验证银行卡卡号是否有效
     */
    public static boolean isValidBankcardNo(String number) {
        int s1 = 0, s2 = 0;
        String reverse = new StringBuffer(number).reverse().toString();
        for (int i = 0; i < reverse.length(); i++) {
            int digit = Character.digit(reverse.charAt(i), 10);
            if (i % 2 == 0) {//this is for odd digits, they are 1-indexed in the algorithm
                s1 += digit;
            } else {//add 2 * digit for 0-4, add 2 * digit - 9 for 5-9
                s2 += 2 * digit;
                if (digit >= 5) {
                    s2 -= 9;
                }
            }
        }
        return (s1 + s2) % 10 == 0;
    }

    /**
     * 判断是否是姓名
     *
     * @param name
     * @return
     */
    public static boolean isValidChineseName(String name) {

        // FIXME: 2017/2/28 新需求 客户端只需要校验客户是否填写了姓名。至于姓名的有效性，长度等均由服务端来做校验
        return !(null == name || "".equals(name.trim()));

//        if (isValidChineseCode(name)) {
//            //判断中文名字长度2-6个汉字
//            boolean bIsValidChineseName = name.length() < 2 || name.length() > 6;
//            return !bIsValidChineseName;
//        }
//        return false;
    }

    /**
     * 判断是否为中文
     *
     * @param strCode
     * @return
     */
    public static boolean isValidChineseCode(String strCode) {
        if (TextUtils.isEmpty(strCode)) {
            return false;
        }

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = null;

        char[] chars = strCode.toCharArray();
        boolean isGB2312 = true;
        for (int i = 0; i < chars.length; i++) {
            m = p.matcher(chars[i] + "");
            if (!m.matches()) {
                isGB2312 = false;
                break;
            }
        }
        return isGB2312;
    }

    /**
     * 判断是否为数字
     *
     * @param checkStr
     * @return
     */
    public static boolean isNumberic(String checkStr) {
        if (checkStr != null && !"".equals(checkStr.trim())) {
            return checkStr.matches("^[0-9]*$");
        }

        return false;
    }


    /**
     * 身份证格式的正则校验
     */
    public static boolean isValidIdCard(String num) {
        String reg = "^\\d{15}$|^\\d{17}[0-9Xx]$";
        if (!num.matches(reg)) {
            return false;
        }
        return true;
    }

    /**
     * 身份证最后一位的校验算法
     */
    public static boolean isValidIdCardLastNumber(char[] id) {
        int sum = 0;
        int w[] = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char[] ch = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        for (int i = 0; i < id.length - 1; i++) {
            sum += (id[i] - '0') * w[i];
        }
        int c = sum % 11;
        char code = ch[c];
        char last = id[id.length - 1];
        last = last == 'x' ? 'X' : last;
        return last == code;
    }
}
