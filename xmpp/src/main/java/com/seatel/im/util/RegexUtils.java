package com.seatel.im.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by eldorado on 17-4-11.
 *
 * 匹配工具类
 */
public class RegexUtils {

    public static boolean isIpDomain(String domain) {
        String IPADDRESS_PATTERN =
                "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

        Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
        Matcher matcher = pattern.matcher(domain);
        return matcher.find();
    }
}
