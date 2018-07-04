package com.wangrj.java_lib.java_program.password_encoder;

import com.wangrj.java_lib.java_util.DataUtil;

import java.util.Base64;

/**
 * by wangrongjun on 2018/7/4.
 */
public class PasswordEncoderController {

    public String encode(String encryptionKey, String clear) {
        try {
            byte[] encrypt = DataUtil.desEncrypt(clear.getBytes(), encryptionKey);
            byte[] encode = Base64.getEncoder().encode(encrypt);
            return new String(encode);
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    public String decode(String encryptionKey, String cipher) {
        try {
            byte[] decode = Base64.getDecoder().decode(cipher.getBytes());
            byte[] bytes = DataUtil.desDecrypt(decode, encryptionKey);
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

}
