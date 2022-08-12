package utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * PBKDF2加密工具
 * @author qzz
 */
public class PBKDF2Util {

    /**
     * 算法
     */
    public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";
    /**
     * 盐的长度
     */
    public static final int SALT_BYTE_SIZE = 16;
    /**
     * 生成密文的长度
     */
    public static final int HASH_BIT_SIZE = 128 * 4;
    /**
     * 迭代次数
     */
    public static final int PBKDF2_ITERATIONS = 1000;

    /**
     * @describe: 对输入的密码进行验证
     * @param: [attemptedPassword(待验证密码), encryptedPassword(密文), salt(盐值)]
     * @return: boolean
     */
    private static boolean authenticate(String attemptedPassword, String encryptedPassword, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 用相同的盐值对用户输入的密码进行加密
        String encryptedAttemptedPassword = getEncryptedPassword(attemptedPassword, salt);
        // 把加密后的密文和原密文进行比较，相同则验证成功，否则失败
        return encryptedAttemptedPassword.equals(encryptedPassword);
    }

    /**
     * @describe: 生成密文
     * @param: [password(明文密码), salt(盐值)]
     * @return: java.lang.String
     */
    private static String getEncryptedPassword(String password, String salt) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        //参数 ：明文密码 ，盐值，和迭代次数和长度生成密文
        KeySpec spec = new PBEKeySpec(password.toCharArray(), fromHex(salt), PBKDF2_ITERATIONS, HASH_BIT_SIZE);
        //返回转换指定算法的秘密密钥的 SecretKeyFactory 对象
        //此方法从首选 Provider 开始遍历已注册安全提供者列表。返回一个封装 SecretKeyFactorySpi 实现的新 SecretKeyFactory 对象，该实现取自支持指定算法的第一个 Provider。
        SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        //根据提供的密钥规范（密钥材料）生成 SecretKey 对象。 然后以主要编码格式返回键，最后转换为16进制字符串
        return toHex(factory.generateSecret(spec).getEncoded());
    }

    /**
     * @describe: 通过加密的强随机数生成盐(最后转换为16进制)
     * @return: java.lang.String
     */
    private static String generateSalt() throws NoSuchAlgorithmException {
        //返回一个实现指定的 SecureRandom 对象
        //随机数生成器 (RNG) 算法。
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        //生成盐
        byte[] salt = new byte[SALT_BYTE_SIZE];
        random.nextBytes(salt);
        //返回16进制的字符串
        return toHex(salt);
    }

    /**
     * @describe: 十六进制字符串转二进制字符串
     * @param: [hex]
     * @return: byte[]
     */
    private static byte[] fromHex(String hex) {
        byte[] binary = new byte[hex.length() / 2];
        for (int i = 0; i < binary.length; i++) {
            binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return binary;
    }

    /**
     * @describe: 二进制字符串转十六进制字符串
     * @param: [array]
     * @return: java.lang.String
     */
    private static String toHex(byte[] array) {
        BigInteger bigInteger = new BigInteger(1, array);
        String hex = bigInteger.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    /**
     * 生成可以保存的数据
     * @param password 明文
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static String generateStorngPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //先获取盐值
        String salt = PBKDF2Util.generateSalt();
        //生成密文
        String hash =PBKDF2Util.getEncryptedPassword(password,salt);
        return   salt + ":" + hash;
    }

    /**
     * 明文密码和数据库保存的值比较
     * @param originalPassword 明文密码
     * @param storedPassword 数据库保存的值
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static boolean validatePassword(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = storedPassword.split(":");
        String salt = parts[0];
        String hash = parts[1];
        return PBKDF2Util.authenticate(originalPassword,hash,salt);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //---------------------------加密-------------------------------
        //原始密码
        String password="123456";
        System.out.println("原始密码:"+password);
        //生成的可以保存的数据
        String storedPassword=PBKDF2Util.generateStorngPasswordHash(password);
        System.out.println("生成的可以保存的数据:"+storedPassword);

        String[] parts = storedPassword.split(":");
        String salt = parts[0];
        String hash = parts[1];
        System.out.println("盐值:"+salt);
        System.out.println("加密后的密码:"+hash);
        //---------------------------加密-------------------------------

        //---------------------------校验-------------------------------
        //明文密码
        String originalPassword="1234156";
        //数据库保存的值
        String storedPassword2=storedPassword;
        boolean isSuccess=PBKDF2Util.validatePassword(originalPassword,storedPassword2);
        System.out.println("验证结果:"+isSuccess);
    }
}
