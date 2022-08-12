package utils;

import org.apache.commons.codec.digest.DigestUtils;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * ND5加密
 * @author qzz
 */
public class MD5Util {

    /**
     * MD5加密方式一：借助apache工具类DigesUtils实现(推荐使用)
     *
     * 此方式需要 安装 commons-codec 依赖
     * @param str 待加密字符串
     * @return    16进制加密字符串(32位MD5码)
     */
    public static String encryptToMD5(String str){
        //MD5 加密，返回 32 位
        return DigestUtils.md5Hex(str);
    }

    /**
     * MD5加密方式二：使用JDK自带的java.security.MessageDigest下的MessageDigest类
     * @param str 待加密字符串
     * @return    16进制加密字符串(32位MD5码)
     */
    public static String encrypt2ToMD5(String str) throws NoSuchAlgorithmException {
        //MD5 加密（使用MD5算法）
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] md5Bytes = md.digest(str.getBytes(StandardCharsets.UTF_8));
        //为了使用方便，将输出值转换为十六进制保存
        return bytesToHexString(md5Bytes);
    }

    /**
     * MD5加密方式三：Spring核心包（Spring Boot 自带MD5加密）
     * @param str 待加密字符串
     * @return    16进制加密字符串(32位MD5码 小写)
     */
    public static String encrypt3ToMD5(String str){
         return org.springframework.util.DigestUtils.md5DigestAsHex(str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 将输出值转换为十六进制保存
     * @param bytes
     * @return
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        for (byte aByte : bytes) {
            int v = aByte & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String str="123456";
        System.out.println("MD5待加密字符串："+str);

        String md5Str=MD5Util.encryptToMD5(str);
        System.out.println("方式一：MD5加密结果："+md5Str);

        String md5Str2=MD5Util.encrypt2ToMD5(str);
        System.out.println("方式二：MD5加密结果："+md5Str2);

        String md5Str3=MD5Util.encrypt3ToMD5(str);
        System.out.println("方式三：MD5加密结果："+md5Str3);
    }
}
