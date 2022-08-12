package utils;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;

/**
 * pdkdf2_sha256 加密验证算法
 * @author qzz
 */
public class Pbkdf2Sha256 {


    /**
     * 默认迭代次数
     */
    private static final Integer DEFAULT_ITERATIONS=30000;
    /**
     * 算法名称
     */
    private static final String algorithm="pdkdf2_sha256";


    /**
     * 获取密文
     * @param password 密码明文
     * @param salt 加盐
     * @param iterations 迭代次数
     * @return
     */
    public static String getEncodedHash(String password,String salt,int iterations){
        SecretKeyFactory keyFactory=null;
        try {
            keyFactory=SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            //无法检索 PBKDF2WithHmacSHA256 算法
            System.err.println("Could not retrieve PBKDF2WithHmacSHA256 algorithm");
            System.exit(1);
        }
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(),salt.getBytes(StandardCharsets.UTF_8),iterations,256);
        SecretKey secretKey = null;
        try {
           secretKey = keyFactory.generateSecret(keySpec);
        } catch (InvalidKeySpecException e) {
            //无法生成密钥
            System.out.println("Could not generate secret key");
            e.printStackTrace();
        }

        byte[] rawHash=secretKey.getEncoded();
        byte[] hashBase64 = Base64.getEncoder().encode(rawHash);

        return new String(hashBase64);
    }

    /**
     * 密文加盐
     * @return
     */
    private static String getSalt(){
        int length = 12;
        Random rand = new Random();
        char[] rs = new char[length];
        for(int i = 0; i < length; i++){
            int t = rand.nextInt(3);
            if (t == 0) {
                rs[i] = (char)(rand.nextInt(10)+48);
            } else if (t == 1) {
                rs[i] = (char)(rand.nextInt(26)+65);
            } else {
                rs[i] = (char)(rand.nextInt(26)+97);
            }
        }
        return new String(rs);
    }

    /**
     * rand salt
     * iterations is default 30000
     * @param password
     * @return
     */
    public static String encode(String password){
        return encode(password,getSalt());
    }

    /**
     * iterations is default 30000
     * @param password
     * @param salt
     * @return
     */
    public static String encode(String password,String salt){
        return encode(password,salt,DEFAULT_ITERATIONS);
    }

    /**
     *
     * @param password 密码明文
     * @param salt 加盐
     * @param iterations 迭代次数
     * @return
     */
    public static String encode(String password,String salt,int iterations){
        //获取密文
        String hash=getEncodedHash(password,salt,iterations);
        //返回字符串组装后的结果集：算法$迭代次数$盐值$密文$
        return String.format("%s$%d$%s$%s",algorithm,iterations,salt,hash);
    }

    /**
     * rand salt
     * @param password
     * @param iterations
     * @return
     */
    public static String encode(String password,int iterations){
       return encode(password,getSalt(),iterations);
    }

    /**
     * 校验密码是否合法
     * @param password 明文
     * @param hashedPassword 密文
     * @return
     */
    public static boolean verification(String password,String hashedPassword){
        //将密文截取  parts[0]：算法 parts[1]：迭代次数 parts[2]：盐值 parts[3]：密文
        String[] parts = hashedPassword.split("\\$");
        if(parts.length!=4){
            return false;
        }
        //迭代次数
        Integer iterations = Integer.parseInt(parts[1]);
        //盐值
        String salt=parts[2];
        //待校验的明文转密文
        String hash=encode(password,salt,iterations);
        //密文比对
        return hash.equals(hashedPassword);
    }

    public static void main(String[] args) {
        String password="111111";
        System.out.println("明文密码："+password);

        //迭代次数
        Integer iterations =1000;
        //明文转密文（密文由四部分组成）
        String hashedPassword=Pbkdf2Sha256.encode(password,iterations);
        System.out.println("生成的可以保存的数据:"+hashedPassword);

        //获取盐值
        String[] parts = hashedPassword.split("\\$");
        String salt=parts[2];
        System.out.println("盐值:"+salt);

        //待验证的明文
        String password1="111111";
        //密文校验
        String hash=Pbkdf2Sha256.encode(password1,salt,iterations);
        boolean isSuccess = hash.equals(hashedPassword);
        System.out.println("验证结果:"+isSuccess);
    }

}
