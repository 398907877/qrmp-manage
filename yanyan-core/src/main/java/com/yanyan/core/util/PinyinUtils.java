package com.yanyan.core.util;

import com.github.stuxuhai.jpinyin.ChineseHelper;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

/**
 * 拼音工具类
 * User: Saintcy
 * Date: 2016/8/18
 * Time: 11:37
 */
public class PinyinUtils {
    public enum PinyinFormat {
        WITH_TONE_MARK, WITHOUT_TONE, WITH_TONE_NUMBER
    }

    private PinyinUtils() {
    }

    /**
     * 将单个汉字转换为相应格式的拼音
     *
     * @param c            需要转换成拼音的汉字
     * @param pinyinFormat 拼音格式：WITH_TONE_NUMBER--数字代表声调，WITHOUT_TONE--不带声调，WITH_TONE_MARK--带声调
     * @return 汉字的拼音
     */
    public static List<String> getPinyin(char c, PinyinFormat pinyinFormat) {
        if (pinyinFormat == PinyinFormat.WITH_TONE_MARK) {
            return Arrays.asList(PinyinHelper.convertToPinyinArray(c, com.github.stuxuhai.jpinyin.PinyinFormat.WITH_TONE_NUMBER));
        } else if (pinyinFormat == PinyinFormat.WITHOUT_TONE) {
            return Arrays.asList(PinyinHelper.convertToPinyinArray(c, com.github.stuxuhai.jpinyin.PinyinFormat.WITHOUT_TONE));
        } else if (pinyinFormat == PinyinFormat.WITH_TONE_NUMBER) {
            return Arrays.asList(PinyinHelper.convertToPinyinArray(c, com.github.stuxuhai.jpinyin.PinyinFormat.WITH_TONE_NUMBER));
        } else {
            return Lists.newArrayList();
        }
    }

    /**
     * 将单个汉字转换成带声调格式的拼音
     *
     * @param c 需要转换成拼音的汉字
     * @return 字符串的拼音
     */
    public static List<String> getPinyin(char c) {
        return getPinyin(c, PinyinFormat.WITH_TONE_MARK);
    }

    /**
     * 将字符串转换成相应格式的拼音
     *
     * @param str          需要转换的字符串
     * @param pinyinFormat 拼音格式：WITH_TONE_NUMBER--数字代表声调，WITHOUT_TONE--不带声调，WITH_TONE_MARK--带声调
     * @return 字符串的拼音
     * @throws PinyinException
     */
    public static List<List<String>> getPinyin(String str, PinyinFormat pinyinFormat) {
        str = ChineseHelper.convertToSimplifiedChinese(str);
        List<List<String>> wordPinyinListList = Lists.newArrayList();//单字的拼音列表的列表
        StringBuffer sbNotChn = new StringBuffer();
        int i = 0;
        int strLen = str.length();
        while (i < strLen) {
            char c = str.charAt(i);
            // 判断是否为汉字或者〇
            if (ChineseHelper.isChinese(c) || c == '〇') {
                //处理中文单词
                handleNotChinese(sbNotChn, wordPinyinListList);

                List<String> pinyinList = getPinyin(c, pinyinFormat);
                wordPinyinListList.add(pinyinList);
            } else {
                sbNotChn.append(c);//非汉字先缓存
            }
            i++;
        }
        //处理中文单词
        handleNotChinese(sbNotChn, wordPinyinListList);

        return Lists.cartesianProduct(wordPinyinListList);//多音字经过笛卡尔乘积后，形成短语的拼音字的列表
    }

    private static void handleNotChinese(StringBuffer sbNotChn, List<List<String>> wordPinyinListList) {
        if (sbNotChn.length() > 0) {
            String[] aNotChnWord = StringUtils.split(sbNotChn.toString());//用空格分割
            for (String ncw : aNotChnWord) {//对于非中文单词，直接加入到拼音列表中
                if (StringUtils.isNotBlank(ncw)) {
                    List<String> wordPinyinList = Lists.newArrayList();
                    wordPinyinList.add(ncw);
                    wordPinyinListList.add(wordPinyinList);
                }
            }

            sbNotChn.setLength(0);//清空缓存
        }
    }

    /**
     * 将字符串转换成相应格式的拼音，并用分隔符分割
     *
     * @param str          需要转换的字符串
     * @param pinyinFormat 拼音格式：WITH_TONE_NUMBER--数字代表声调，WITHOUT_TONE--不带声调，WITH_TONE_MARK--带声调
     * @return 字符串的拼音
     * @throws PinyinException
     */
    public static List<String> getPinyin(String str, PinyinFormat pinyinFormat, String separator) {
        List<List<String>> wordSeparatedStringPinyinList = getPinyin(str, pinyinFormat);
        //将每行的拼音字合并后，形成短语的拼音列表
        List<String> stringPinyinList = Lists.newArrayList();
        for (List<String> pinyinWordList : wordSeparatedStringPinyinList) {
            stringPinyinList.add(StringUtils.join(pinyinWordList, separator));
        }

        return stringPinyinList;
    }

    /**
     * 判断一个汉字是否为多音字
     *
     * @param c 汉字
     * @return 判断结果，是汉字返回true，否则返回false
     */
    public static boolean hasMultiPinyin(char c) {
        List<String> pinyinList = getPinyin(c);
        if (pinyinList != null && pinyinList.size() > 1) {
            return true;
        }
        return false;
    }

    /**
     * 获取字符串对应拼音的首字母
     *
     * @param str 需要转换的字符串
     * @return 对应拼音的首字母
     * @throws PinyinException
     */
    public static List<String> getShortPinyin(String str) {
        return getShortPinyin(str, "");
    }

    /**
     * 获取字符串对应拼音的首字母
     *
     * @param str       需要转换的字符串
     * @param separator 拼音分隔符
     * @return 对应拼音的首字母
     * @throws PinyinException
     */
    public static List<String> getShortPinyin(String str, String separator) {
        List<List<String>> wordSeparatedStringPinyinList = getPinyin(str, PinyinFormat.WITHOUT_TONE);
        List<String> shortPinyinList = Lists.newArrayList();
        for (List<String> pinyinWordList : wordSeparatedStringPinyinList) {
            List<String> initialLetterList = Lists.newArrayList();
            for (String pinyinWord : pinyinWordList) {
                if (pinyinWord != null && pinyinWord.length() > 0) {
                    initialLetterList.add(String.valueOf(pinyinWord.charAt(0)));
                }
            }
            String shortPinyin = StringUtils.join(initialLetterList, separator);
            if (!shortPinyinList.contains(shortPinyin)) {
                shortPinyinList.add(shortPinyin);
            }
        }

        return shortPinyinList;
    }

    public static void addPinyinDict(String path) throws FileNotFoundException {
        PinyinHelper.addPinyinDict(path);
    }

    public static void addMutilPinyinDict(String path) throws FileNotFoundException {
        PinyinHelper.addPinyinDict(path);
    }
}
