package com.chen.intellectualproperty.util;


/**
 * 字符串工具类
 * 提供字符串判空、判空白、格式化等常用操作
 */
public class StringTool {


    /**
     * 判断字符串是否为 null 或空字符串 ""
     * @param str 待检查的字符串
     * @return true-为空，false-不为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 判断字符串是否不为 null 且不为空字符串 ""
     * @param str 待检查的字符串
     * @return true-不为空，false-为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断字符串是否为 null、空字符串 "" 或只包含空白字符（空格、制表符、换行符等）
     * @param str 待检查的字符串
     * @return true-为空白，false-不为空白
     */
    public static boolean isBlank(String str) {
        if (str == null || str.isEmpty()) {
            return true;
        }
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否不为 null、不为空字符串且不只包含空白字符
     * @param str 待检查的字符串
     * @return true-不为空白，false-为空白
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 去除字符串首尾空白字符，如果为 null 则返回 null
     * @param str 待处理的字符串
     * @return 去除空白后的字符串，或 null
     */
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    /**
     * 去除字符串首尾空白字符，如果为 null 则返回空字符串 ""
     * @param str 待处理的字符串
     * @return 去除空白后的字符串，或 ""
     */
    public static String trimToEmpty(String str) {
        return str == null ? "" : str.trim();
    }

    /**
     * 去除字符串首尾空白字符，如果为 null 或空白则返回 null
     * @param str 待处理的字符串
     * @return 去除空白后的字符串，或 null
     */
    public static String trimToNull(String str) {
        if (str == null) {
            return null;
        }
        String trimmed = str.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    /**
     * 将 null 转换为空字符串 ""
     * @param str 待处理的字符串
     * @return 原字符串或 ""
     */
    public static String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

    /**
     * 将空字符串或空白字符串转换为 null
     * @param str 待处理的字符串
     * @return 原字符串或 null
     */
    public static String emptyToNull(String str) {
        return isBlank(str) ? null : str;
    }

    /**
     * 获取字符串长度，null 返回 0
     * @param str 待检查的字符串
     * @return 字符串长度
     */
    public static int length(String str) {
        return str == null ? 0 : str.length();
    }

    /**
     * 比较两个字符串是否相等（处理 null 情况）
     * @param str1 字符串1
     * @param str2 字符串2
     * @return true-相等，false-不相等
     */
    public static boolean equals(String str1, String str2) {
        if (str1 == null) {
            return str2 == null;
        }
        return str1.equals(str2);
    }

    /**
     * 比较两个字符串是否相等（忽略大小写，处理 null 情况）
     * @param str1 字符串1
     * @param str2 字符串2
     * @return true-相等，false-不相等
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == null) {
            return str2 == null;
        }
        return str1.equalsIgnoreCase(str2);
    }

    /**
     * 判断字符串是否以指定前缀开头（处理 null 情况）
     * @param str 待检查的字符串
     * @param prefix 前缀
     * @return true-是，false-否
     */
    public static boolean startsWith(String str, String prefix) {
        if (str == null || prefix == null) {
            return false;
        }
        return str.startsWith(prefix);
    }

    /**
     * 判断字符串是否以指定后缀结尾（处理 null 情况）
     * @param str 待检查的字符串
     * @param suffix 后缀
     * @return true-是，false-否
     */
    public static boolean endsWith(String str, String suffix) {
        if (str == null || suffix == null) {
            return false;
        }
        return str.endsWith(suffix);
    }

    /**
     * 判断字符串是否包含指定子串（处理 null 情况）
     * @param str 待检查的字符串
     * @param searchStr 子串
     * @return true-包含，false-不包含
     */
    public static boolean contains(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        return str.contains(searchStr);
    }

    /**
     * 判断字符串是否包含指定子串（忽略大小写）
     * @param str 待检查的字符串
     * @param searchStr 子串
     * @return true-包含，false-不包含
     */
    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        return str.toLowerCase().contains(searchStr.toLowerCase());
    }

    /**
     * 将字符串转换为小写（处理 null 情况）
     * @param str 待转换的字符串
     * @return 小写字符串或 null
     */
    public static String toLowerCase(String str) {
        return str == null ? null : str.toLowerCase();
    }

    /**
     * 将字符串转换为大写（处理 null 情况）
     * @param str 待转换的字符串
     * @return 大写字符串或 null
     */
    public static String toUpperCase(String str) {
        return str == null ? null : str.toUpperCase();
    }

    /**
     * 字符串截取（从开始到指定位置）
     * @param str 原字符串
     * @param end 结束位置（不包含）
     * @return 截取后的字符串
     */
    public static String substring(String str, int end) {
        if (str == null) {
            return null;
        }
        if (end > str.length()) {
            end = str.length();
        }
        return str.substring(0, end);
    }

    /**
     * 字符串截取（指定起始和结束位置）
     * @param str 原字符串
     * @param start 起始位置（包含）
     * @param end 结束位置（不包含）
     * @return 截取后的字符串
     */
    public static String substring(String str, int start, int end) {
        if (str == null) {
            return null;
        }
        if (start < 0) {
            start = 0;
        }
        if (end > str.length()) {
            end = str.length();
        }
        if (start >= end) {
            return "";
        }
        return str.substring(start, end);
    }

    /**
     * 字符串替换
     * @param str 原字符串
     * @param target 目标字符串
     * @param replacement 替换字符串
     * @return 替换后的字符串
     */
    public static String replace(String str, String target, String replacement) {
        if (str == null || target == null) {
            return str;
        }
        if (replacement == null) {
            replacement = "";
        }
        return str.replace(target, replacement);
    }

    /**
     * 字符串拼接（用分隔符连接）
     * @param delimiter 分隔符
     * @param elements 元素数组
     * @return 拼接后的字符串
     */
    public static String join(String delimiter, String... elements) {
        if (elements == null || elements.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < elements.length; i++) {
            if (i > 0) {
                sb.append(delimiter);
            }
            sb.append(elements[i] == null ? "" : elements[i]);
        }
        return sb.toString();
    }



    /**
     * 重复字符串指定次数
     * @param str 原字符串
     * @param count 重复次数
     * @return 重复后的字符串
     */
    public static String repeat(String str, int count) {
        if (str == null || count <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(str.length() * count);
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 判断字符串是否为纯数字
     * @param str 待检查的字符串
     * @return true-是纯数字，false-不是
     */
    public static boolean isNumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否为纯字母
     * @param str 待检查的字符串
     * @return true-是纯字母，false-不是
     */
    public static boolean isAlpha(String str) {
        if (isEmpty(str)) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否为字母或数字
     * @param str 待检查的字符串
     * @return true-是字母或数字，false-不是
     */
    public static boolean isAlphanumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 反转字符串
     * @param str 原字符串
     * @return 反转后的字符串
     */
    public static String reverse(String str) {
        if (str == null) {
            return null;
        }
        return new StringBuilder(str).reverse().toString();
    }

    /**
     * 首字母大写
     * @param str 原字符串
     * @return 首字母大写后的字符串
     */
    public static String capitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * 首字母小写
     * @param str 原字符串
     * @return 首字母小写后的字符串
     */
    public static String uncapitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * 默认字符串（如果为 null 或空白则返回默认值）
     * @param str 原字符串
     * @param defaultStr 默认字符串
     * @return 原字符串或默认值
     */
    public static String defaultIfBlank(String str, String defaultStr) {
        return isBlank(str) ? defaultStr : str;
    }

    /**
     * 默认字符串（如果为 null 或空则返回默认值）
     * @param str 原字符串
     * @param defaultStr 默认字符串
     * @return 原字符串或默认值
     */
    public static String defaultIfEmpty(String str, String defaultStr) {
        return isEmpty(str) ? defaultStr : str;
    }

}
