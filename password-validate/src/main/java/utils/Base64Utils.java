package utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * base64加解码
 *
 * Java8 之后，JDK 工具包中提供了 Base64 特性，可以直接使用来完成编码和解码操作。
 * @author qzz
 */
public class Base64Utils {

    /**
     * base64 编码
     * @param text
     * @return
     */
    public static String encode(String text){
      return new String(Base64.getEncoder().encode(text.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * base64 解码
     * @param encodedText
     * @return
     */
    public static String decode(String encodedText){
        return new String(Base64.getDecoder().decode(encodedText.getBytes(StandardCharsets.UTF_8)));
    }

    public static void main(String[] args) {

        String str="123456";

        //编码
        String encodedText=Base64Utils.encode(str);
        System.out.println("编码后的字符串为："+encodedText);

        //解码
        String text=Base64Utils.decode(encodedText);
        System.out.println("解码后的字符串为："+text);
    }
}
