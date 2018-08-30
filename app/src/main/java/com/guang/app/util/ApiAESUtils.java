package com.guang.app.util;

/**
 * https://blog.csdn.net/fyq201749/article/details/81504251
 * Created by wintercoder on 2018/9/2.
 * 经过各种折腾得到的 JAVA 支持 PKCS7 填充方案，起因是 JAVA不支持 PKCS7，但 PHP7的 openssl_encrypt 也跟PHP5的 mcrypt_decrypt 不一致
 * 坑爹
 */



import android.util.Base64;

import com.apkfuns.logutils.LogUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ApiAESUtils
{

    //此函数是pkcs7padding填充函数
    private static String pkcs7padding(String data) {
        int bs = 16;
        int padding = bs - (data.length() % bs);
        String padding_text = "";
        for (int i = 0; i < padding; i++) {
            padding_text += (char)padding;
        }
        return data+padding_text;
    }
    /**
     * AES加密
     * @param content 要加密的内容
     * @param key 密钥
     * @param iv iv
     * @return
     */
    public static String encrypt(String content, String key, String iv)
    {
        if(key == null || key.length() != 16)
        {
            System.err.println("AES key 的长度必须是16位！");
            return null;
        }
        if(iv == null || iv.length() != 16)
        {
            System.err.println("AES iv 的长度必须是16位！");
            return null;
        }
        try
        {
            content = pkcs7padding(content);        //JAVA不支持，所以手动进行PKCS7Padding填充
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = content.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0)
            {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);

            String base64ed = Base64.encodeToString(encrypted, Base64.DEFAULT);
            //                    new BASE64Encoder().encode(encrypted);

            //坑爹base64超过一定长度会换行，会导致各种报错
            base64ed = base64ed.replaceAll("\r\n", "").replaceAll("\r", "")
                    .replaceAll("\n", "");
            return base64ed;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES加密，key和iv一样
     * @param content 要加密的内容
     * @param key 密钥
     * @return
     */
    public static String encrypt(String content, String key)
    {
        return encrypt(content, key, key);
    }

    /**
     * AES解密
     * @param content 要解密的内容
     * @param key 密钥
     * @param iv iv
     * @return
     */
    public static String decrypt(String content, String key, String iv)
    {

        try
        {
//            byte[] encrypted = new BASE64Decoder().decodeBuffer(content);
            byte[] encrypted = Base64.decode(content,Base64.DEFAULT);
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] original = cipher.doFinal(encrypted);
            return new String(original).trim();     //此处trim()避开填充，如果有问题，需要写一个 pkcs7padding 的反函数
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String content, String key)
    {
        return decrypt(content, key, key);
    }
    
}