package com.yanyan.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * Title: 宏变量工具
 * </p>
 * <p>
 * Description: 以$(macro_name)形式的宏变量查找、解析与替换
 * </p>
 *
 * @author Saintcy Don
 * @version 1.0
 */
public class MacroUtils {
    public static final String START_TAG = "$(";
    public static final String END_TAG = ")";

    /**
     * 查找变量
     *
     * @param expression
     * @return
     */
    public static List<String> find(String expression) {
        List<String> exprs = new ArrayList<String>();
        if (expression == null) {
            return exprs;
        }
        int strLen = expression.length();
        if (strLen == 0) {
            return exprs;
        }

        Stack<Variable> stack = new Stack<Variable>();
        char[] aContent = expression.toCharArray();

        for (int i = 0; i < aContent.length; i++) {
            switch (aContent[i]) {
                case '$':
                    if (aContent[i + 1] == '(') {// 是起始标记
                        stack.push(new Variable(-1));// 起始标记
                        stack.push(new Variable(0));// 变量
                        i++;
                    } else {
                        if (!stack.isEmpty()) {// 是变量的一部分
                            stack.peek().id.append(aContent[i]);
                        }
                    }
                    break;
                case ')':
                    if (!stack.isEmpty()) { // 没有匹配的，当作普通字符
                        String var = stack.pop().id.toString();

                        stack.pop();// 清掉起始标记
                        exprs.add("$(" + var + ")");

                        if (!stack.isEmpty()) {// 是变量中的变量
                            if (stack.peek().flag == 0) {
                                stack.peek().id.append("$(" + var + ")");
                            } else {
                                stack.push(new Variable("$(" + var + ")"));
                            }
                        }
                    }
                    break;
                default:
                    if (!stack.isEmpty()) {
                        stack.peek().id.append(aContent[i]);
                    }

                    break;
            }
        }

        return exprs;
    }

    /**
     * 替换变量，如果不存在变量，则不替换
     *
     * @param expression
     * @param params
     * @return
     */
    public static String replace(String expression, Map<String, String> params) {
        return replace(expression, params, false, "");
    }

    public static String replace(String expression, Map<String, String> params, boolean replaceNull) {
        return replace(expression, params, replaceNull, "");
    }

    /**
     * 替换变量 $(var)普通变量，默认值为空 $(var, def) 带默认值的变量 $(var, "def") 默认值含有特殊符号的变量
     *
     * @param expression  表达式
     * @param params      变量集
     * @param replaceNull 是否替换变量集中没有的变量
     * @param nullValue   变量集没有的变量的默认值
     * @return
     */
    public static String replace(String expression, Map<String, String> params, boolean replaceNull, String nullValue) {
        Map<String, String> innerParams = params;
        if (innerParams == null) {
            innerParams = new HashMap<String, String>();
        }

        if (expression == null) {
            return null;
        }
        int strLen = expression.length();
        if (strLen == 0) {
            return "";
        }

        Stack<Variable> stack = new Stack<Variable>();
        char[] aContent = expression.toCharArray();
        StringBuffer sbRealExpr = new StringBuffer();

        for (int i = 0; i < aContent.length; i++) {
            switch (aContent[i]) {
                case '$':
                    if (aContent[i + 1] == '(') {// 是起始标记
                        stack.push(new Variable(-1));// 起始标记
                        stack.push(new Variable(0));// 变量
                        i++;
                    } else {
                        if (!stack.isEmpty()) {// 是变量的一部分
                            stack.peek().id.append(aContent[i]);
                        } else {
                            sbRealExpr.append(aContent[i]);
                        }
                    }
                    break;
                case ')':
                    if (stack.isEmpty()) { // 没有匹配的，当作普通字符
                        sbRealExpr.append(aContent[i]);
                    } else {
                        // System.out.println(stack.peek().flag+stack.peek().id.toString());
                        String var = stack.pop().id.toString();

                        stack.pop();// 清掉起始标记
                        int pos = var.lastIndexOf(",");
                        String def = nullValue;
                        if (!replaceNull) {
                            def = "$(" + var + ")";
                        }
                        if (pos >= 0) {// $(var, def)--有默认值
                            // System.out.println(var);
                            def = StringUtils.substring(var, pos + 1);
                            def = def.trim();
                            if (def.length() > 0) {
                                if (def.startsWith("\"") && def.endsWith("\"")) {// 去掉双引号
                                    def = StringUtils.substring(def, 1, def.length() - 1);
                                }
                            }
                            var = StringUtils.substring(var, 0, pos);
                            // System.out.println(def);
                        }
                        // System.out.println(var);
                        var = StringUtils.defaultString(innerParams.get(var), def);

                        if (stack.isEmpty()) {
                            sbRealExpr.append(var);
                        } else {// 是变量中的变量
                            if (stack.peek().flag == 0) {
                                stack.peek().id.append(var);
                            } else {
                                stack.push(new Variable(var));
                            }
                        }
                    }
                    break;
                default:
                    if (stack.isEmpty()) {
                        sbRealExpr.append(aContent[i]);
                    } else {
                        stack.peek().id.append(aContent[i]);
                    }

                    break;
            }
        }

        while (!stack.isEmpty()) {
            sbRealExpr.append(stack.pop().id);
        }

        return sbRealExpr.toString();
    }

    private static class Variable {
        int flag = 0;
        StringBuffer id = new StringBuffer();

        Variable(int flag) {
            this.flag = flag;
        }

        Variable(String id) {
            this.id.append(id);
        }
    }
}
