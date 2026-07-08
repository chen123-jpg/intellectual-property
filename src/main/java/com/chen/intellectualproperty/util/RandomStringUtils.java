package com.chen.intellectualproperty.util;

import java.security.SecureRandom;
import java.util.Random;

/**
 * 随机字符串生成工具类
 */
public class RandomStringUtils {

    private static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()_+-=[]{}|;:,.<>?";
    private static final String ALPHANUMERIC = UPPER_CASE + LOWER_CASE + DIGITS;

    private static final Random RANDOM = new SecureRandom();

    /**
     * 生成随机字符串（可指定是否包含字母和数字）
     * @param length 长度
     * @param letters 是否包含字母
     * @param numbers 是否包含数字
     * @return 随机字符串
     */
    public static String random(int length, boolean letters, boolean numbers) {
        if (!letters && !numbers) {
            throw new IllegalArgumentException("至少需要包含字母或数字中的一种");
        }

        StringBuilder chars = new StringBuilder();
        if (letters) {
            chars.append(UPPER_CASE).append(LOWER_CASE);
        }
        if (numbers) {
            chars.append(DIGITS);
        }

        return random(chars.toString(), length);
    }

    /**
     * 生成随机字符串（可指定是否包含字母、数字和特殊字符）
     * @param length 长度
     * @param letters 是否包含字母
     * @param numbers 是否包含数字
     * @param specialChars 是否包含特殊字符
     * @return 随机字符串
     */
    public static String random(int length, boolean letters, boolean numbers, boolean specialChars) {
        if (!letters && !numbers && !specialChars) {
            throw new IllegalArgumentException("至少需要包含字母、数字或特殊字符中的一种");
        }

        StringBuilder chars = new StringBuilder();
        if (letters) {
            chars.append(UPPER_CASE).append(LOWER_CASE);
        }
        if (numbers) {
            chars.append(DIGITS);
        }
        if (specialChars) {
            chars.append(SPECIAL_CHARS);
        }

        return random(chars.toString(), length);
    }

    /**
     * 生成指定字符集的随机字符串
     * @param chars 字符集
     * @param length 长度
     * @return 随机字符串
     */
    public static String random(String chars, int length) {
        if (chars == null || chars.isEmpty()) {
            throw new IllegalArgumentException("字符集不能为空");
        }
        if (length <= 0) {
            throw new IllegalArgumentException("长度必须大于0");
        }

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(RANDOM.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * 生成随机字符串（包含字母、数字和特殊字符）
     * @param length 长度
     * @return 随机字符串
     */
    public static String random(int length) {
        return random(length, true, true, true);
    }

    /**
     * 生成随机数字字符串
     * @param length 长度
     * @return 随机数字字符串
     */
    public static String randomNumeric(int length) {
        return random(length, false, true);
    }

    /**
     * 生成随机字母字符串（包含大小写）
     * @param length 长度
     * @return 随机字母字符串
     */
    public static String randomAlphabetic(int length) {
        return random(length, true, false);
    }

    /**
     * 生成随机字母数字字符串
     * @param length 长度
     * @return 随机字母数字字符串
     */
    public static String randomAlphanumeric(int length) {
        return random(length, true, true);
    }

    /**
     * 生成随机大写字母字符串
     * @param length 长度
     * @return 随机大写字母字符串
     */
    public static String randomUpperCase(int length) {
        return random(UPPER_CASE, length);
    }

    /**
     * 生成随机小写字母字符串
     * @param length 长度
     * @return 随机小写字母字符串
     */
    public static String randomLowerCase(int length) {
        return random(LOWER_CASE, length);
    }

    /**
     * 生成指定位数的随机整数（不以0开头）
     * @param length 位数
     * @return 随机整数字符串
     */
    public static String randomPositiveNumber(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("长度必须大于0");
        }
        if (length == 1) {
            return String.valueOf(RANDOM.nextInt(10));
        }

        StringBuilder sb = new StringBuilder(length);
        // 第一位不能为0
        sb.append(RANDOM.nextInt(9) + 1);
        // 后续位可以是0-9
        for (int i = 1; i < length; i++) {
            sb.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
        }
        return sb.toString();
    }

    /**
     * 生成固定格式的验证码（数字+字母混合，易读字符）
     * @param length 长度
     * @return 验证码字符串
     */
    public static String randomVerificationCode(int length) {
        // 排除容易混淆的字符：0Oo1IiLl
        String safeChars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789";
        return random(safeChars, length);
    }

    /**
     * 生成UUID风格的随机字符串（32位）
     * @return UUID风格字符串
     */
    public static String randomUUID() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成指定位数的随机密码（包含大小写字母、数字和特殊字符）
     * @param length 密码长度
     * @return 随机密码
     */
    public static String randomPassword(int length) {
        if (length < 4) {
            throw new IllegalArgumentException("密码长度至少为4位");
        }

        StringBuilder password = new StringBuilder(length);

        // 确保至少包含每种类型的字符
        password.append(UPPER_CASE.charAt(RANDOM.nextInt(UPPER_CASE.length())));
        password.append(LOWER_CASE.charAt(RANDOM.nextInt(LOWER_CASE.length())));
        password.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
        password.append(SPECIAL_CHARS.charAt(RANDOM.nextInt(SPECIAL_CHARS.length())));

        // 剩余位置随机填充
        for (int i = 4; i < length; i++) {
            password.append(random(i, true, true, true).charAt(0));
        }

        // 打乱顺序
        char[] chars = password.toString().toCharArray();
        for (int i = chars.length - 1; i > 0; i--) {
            int j = RANDOM.nextInt(i + 1);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }

        return new String(chars);
    }

    /**
     * 生成指定范围内的随机整数
     * @param min 最小值（包含）
     * @param max 最大值（包含）
     * @return 随机整数
     */
    public static int randomInt(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("最小值不能大于最大值");
        }
        return RANDOM.nextInt(max - min + 1) + min;
    }

    /**
     * 生成指定范围内的随机长整数
     * @param min 最小值（包含）
     * @param max 最大值（包含）
     * @return 随机长整数
     */
    public static long randomLong(long min, long max) {
        if (min > max) {
            throw new IllegalArgumentException("最小值不能大于最大值");
        }
        return min + (long) (RANDOM.nextDouble() * (max - min + 1));
    }

    /**
     * 生成指定范围的随机小数
     * @param min 最小值
     * @param max 最大值
     * @param scale 小数位数
     * @return 随机小数
     */
    public static double randomDouble(double min, double max, int scale) {
        if (min > max) {
            throw new IllegalArgumentException("最小值不能大于最大值");
        }
        double value = min + (max - min) * RANDOM.nextDouble();
        double factor = Math.pow(10, scale);
        return Math.round(value * factor) / factor;
    }

    /**
     * 生成随机布尔值
     * @return true或false
     */
    public static boolean randomBoolean() {
        return RANDOM.nextBoolean();
    }

    /**
     * 从数组中随机选择一个元素
     * @param array 数组
     * @return 随机元素
     */
    public static <T> T randomElement(T[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("数组不能为空");
        }
        return array[RANDOM.nextInt(array.length)];
    }

    /**
     * 从列表中随机选择一个元素
     * @param list 列表
     * @return 随机元素
     */
    public static <T> T randomElement(java.util.List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("列表不能为空");
        }
        return list.get(RANDOM.nextInt(list.size()));
    }

    /**
     * 生成随机手机号（中国大陆）
     * @return 随机手机号
     */
    public static String randomPhoneNumber() {
        String[] prefixes = {"138", "139", "137", "136", "135", "134", "159", "158", "157", "150",
                "151", "152", "188", "187", "182", "183", "184", "178", "198", "130",
                "131", "132", "155", "156", "185", "186", "166", "133", "153", "180",
                "181", "189", "177", "173", "199"};
        String prefix = prefixes[RANDOM.nextInt(prefixes.length)];
        String suffix = randomNumeric(8);
        return prefix + suffix;
    }

    /**
     * 生成随机邮箱
     * @return 随机邮箱
     */
    public static String randomEmail() {
        String[] domains = {"qq.com", "163.com", "gmail.com", "sina.com", "hotmail.com", "outlook.com"};
        String username = randomAlphanumeric(8).toLowerCase();
        String domain = domains[RANDOM.nextInt(domains.length)];
        return username + "@" + domain;
    }
}

