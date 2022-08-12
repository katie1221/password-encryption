package utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SHA256加密
 * @author qzz
 */
public class SHAUtil {

    /**
     * 加密方式一：借助apache工具类DigesUtils实现(推荐使用)
     *
     * 此方式需要 安装 commons-codec 依赖
     * @param str 待加密字符串
     * @return    16进制加密字符串(64位)
     */
    public static String encryptToSHA256(String str){
        //SHA-256 加密，返回 64 位
        return DigestUtils.sha256Hex(str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 加密方式二：使用JDK自带的java.security.MessageDigest下的MessageDigest类
     *
     * 此方式需要 安装 commons-codec 依赖
     * @param str 待加密字符串
     * @return    16进制加密字符串(64位)
     */
    public static String encrypt2ToSHA256(String str) throws NoSuchAlgorithmException {
        //SHA-256 加密，返回 64 位
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] md5Bytes = md.digest(str.getBytes(StandardCharsets.UTF_8));
        //为了使用方便，将输出值转换为十六进制保存
        return bytesToHexString(md5Bytes);
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
        System.out.println("SHA-256待加密字符串："+str);

        String sha256Str=SHAUtil.encryptToSHA256(str);
        System.out.println("方式一：SHA-256加密结果："+sha256Str);

        String sha256Str2=SHAUtil.encrypt2ToSHA256(str);
        System.out.println("方式二：SHA-256加密结果："+sha256Str2);
    }
}
