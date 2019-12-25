package com.fishkj.starter.term.utils;

import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;

public class ScriptsUtils {

	/**
     * 将指定的JavaScript代码进行混淆。
     * @param script 指定的JavaScript代码
     * @return 混淆后的JavaScript代码
     */
    public static String obfuscateScript(CharSequence script) {
      if (script == null) {
        return "";
      }
   
      // String.fromCharCode中参数最大个数
      final int stringFromCharCodeLimit = 100;
      // 每行的参数个数
      final int parametersPerLine = 10;
      // 使用xor函数的比例
      final float xorRate = 0.1f;
      // 字符缓冲
      StringBuilder buff = new StringBuilder(script.length() * 10 + 500);
      // 格式化输出到字符串缓冲
      Formatter formatter = new Formatter(buff);
   
      // 输出String.fromCharCode的别名定义，并返回其别名
      String stringFromCharCode = stringFromCharCode(formatter);
      // 输出xor函数，并返回函数名列表及对应的xor阈值
      Map<String, Integer> xorFunctions = xorFunctions(formatter);
      // xor函数名称
      String[] xorFuncNames = xorFunctions.keySet().toArray(new String[0]);
   
      // eval函数开始，其中第一个使用String.fromCharCode(32)即空格
      formatter.format("\\u0065\\u0076\\u0061\\u006c(%1$s(32", formatArguments(3, stringFromCharCode));
   
      // 遍历代码中的所有字符
      for (int i = 0; i < script.length(); i++) {
        // 当前字符
        int code = script.charAt(i);
   
        if (i % stringFromCharCodeLimit == 0) {
          // 结束旧的String.fromCharCode，
          formatter.format(")");
          // 开始新的String.fromCharCode
          formatter.format("+%1$s(", formatArguments(2, stringFromCharCode));
        } else {
          // 一般的String.fromCharCode参数之间使用逗号分隔
          buff.append(",");
          if (i % parametersPerLine == 0) {
            // 当前字符结束后需要换行
            formatter.format("");
          }
        }
   
        // 根据xorRate确定的比例，输出当前字符参数
        if (RandomUtils.nextFloat() < xorRate) {
          // 使用xor参数的名称
          String xorFunc = xorFuncNames[i % xorFuncNames.length];
          // 对应的异或计算阈值
          int xor = xorFunctions.get(xorFunc);
          // 进行过异或计算后的结果
          int xorCode = code ^ xor;
          // 输出函数调用
          formatter.format("%1$s(", xorFunc);
          // 输出函数参数
          formatter.format(numberFormat(i), xorCode);
          // 调用结束
          buff.append(")");
        } else {
          // 正常输出
          formatter.format(numberFormat(i), code);
        }
      }
   
      // 最后一个String.fromCharCode和eval函数的结尾
      formatter.format("));", formatArguments(3));
   
      // 返回混淆代码
      return buff.toString();
    }
    
    /**
     * 输出String.fromCharCode的别名定义，并返回其别名。
     * @param formatter 格式化输出
     * @return String.fromCharCode别名
     */
    private static String stringFromCharCode(Formatter formatter) {
      String stringFromCharCode = "__" + randomAlphanumeric(3, 10);
      formatter.format("var %1$s=\\u0053\\u0074\\u0072\\u0069\\u006e\\u0067.\\u0066r\\u006fm\\u0043ha\\u0072C\\u006fde;",formatArguments(7, stringFromCharCode));
      return stringFromCharCode;
    }
   
    /**
     * 输出xor函数，并返回函数名列表及对应的xor阈值。
     * @param formatter 格式化输出
     * @return xor函数名列表及对应的xor阈值
     */
    private static Map<String, Integer> xorFunctions(Formatter formatter) {
      int[] xorArray = new int[5];
      for (int i = 0; i < xorArray.length; i++) {
        xorArray[i] = RandomUtils.nextInt(4096);
      }
      String xorArrayName = "_x_" + randomAlphanumeric(3);
      formatter.format("var %1$s = [", formatArguments(3, xorArrayName));
      for (int i = 0; i < xorArray.length; i++) {
        formatter.format("%d,", xorArray[i]);
      }
      formatter.format("];", formatArguments(2));
   
      Map<String, Integer> functions = new HashMap<String, Integer>();
      for (int i = 0; i < xorArray.length; i++) {
        String func = "_$" + randomAlphanumeric(3, 5);
        formatter.format("var %1$s=function(){", formatArguments(5, func));
        formatter.format("return arguments[0]^", formatArguments(4));
        formatter.format("%1$s[%2$d];};", formatArguments(6, xorArrayName, i));
        functions.put(func, xorArray[i]);
      }
      return functions;
    }
    
    /**
     * 获取格式化输出参数。
     * @param count 参数个数
     * @param firstFixedParameters 最初的固定参数
     * @return 根据给定的参数个数，在最初的固定参数后生成随机字符串作为格式化输出参数
     */
    private static Object[] formatArguments(int count,
                                            Object... firstFixedParameters) {
      if (count < firstFixedParameters.length) {
        throw new IllegalArgumentException("length < codes.length");
      }
      Object[] args = new Object[count];
      System.arraycopy(firstFixedParameters,
                       0,
                       args,
                       0,
                       firstFixedParameters.length);
      for (int i = firstFixedParameters.length; i < args.length; i++) {
        args[i] = randomAlphanumeric(5, 20);
      }
      return args;
    }
    
    /**
     * 生成由字母及数字组成的随机字符串。
     * @param length 随机字符串长度
     * @return 由字母及数字组成的随机字符串
     */
    private static String randomAlphanumeric(int length) {
      return randomAlphanumeric(length, length);
    }
    
    /**
     * 生成由字母及数字组成的随机字符串。
     * @param minLength 随机字符串最小长度
     * @param maxLength 随机字符串最大长度
     * @return 由字母及数字组成的随机字符串
     */
    private static String randomAlphanumeric(int minLength, int maxLength) {
      if (minLength <= 0) {
        throw new IllegalArgumentException("minLength <= 0");
      }
      if (maxLength <= 0) {
        throw new IllegalArgumentException("maxLength <= 0");
      }
      if (minLength > maxLength) {
        throw new IllegalArgumentException("minLength > maxLength");
      } else if (minLength == maxLength) {
        return RandomStringUtils.randomAlphanumeric(minLength);
      } else {
        int length = minLength + RandomUtils.nextInt(maxLength - minLength);
        return RandomStringUtils.randomAlphanumeric(length);
      }
    }
    
    /**
     * 随机的格式化输出格式。
     * @param seed 随机种子
     * @return 随机的格式化输出格式
     */
    private static String numberFormat(int seed) {
      int rnd = RandomUtils.nextInt(Math.abs(seed) + 100);
      switch (rnd % 17) {
        case 0:
          return "0x%x";
        case 1:
          return String.format("-1-~(0x%%x^0)", randomAlphanumeric(2, 5));
        case 2:
          return String.format("%%d%d/0xA", RandomUtils.nextInt(10));
        case 3:
          return "Math.abs(%d)&-1";
        case 4:
          return "0%o";
        case 5:
          return "%d&(-1^0x00)";
        case 6:
          return "0x0|0x%x";
        case 7:
          return String.format("~~%%d", formatArguments(2));
        case 8:
          return String.format("~(0x%%x^-1)", randomAlphanumeric(2, 5));
        case 9:
          return String.format("0x%%x%d%d/0400", RandomUtils.nextInt(10), RandomUtils.nextInt(10));
        case 10:
          return String.format("0x%%x%d%d>>4>>4", formatArguments(3, RandomUtils.nextInt(10), RandomUtils.nextInt(10)));
        case 11:
          return String.format("%%d", randomAlphanumeric(2));
        default:
          return "%d";
      }
    }
}
