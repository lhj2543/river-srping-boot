package com.river.security.util;


import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.river.common.core.constant.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author river
 * @date 2019/2/1 AES + Base64密码解密工具类
 */
@Slf4j
public class PasswordDecoder {

    private static final String PASSWORD = "password";

    private static final String KEY_ALGORITHM = "AES";

    /**
     * AES解密
     * @param data 密码
     * @param pass 加解密秘钥
     * @return
     */
    public static String decryptAES(String data, String pass) {
        AES aes = new AES(Mode.CBC, Padding.NoPadding, new SecretKeySpec(pass.getBytes(), KEY_ALGORITHM),
                new IvParameterSpec(pass.getBytes()));
        byte[] result = aes.decrypt(Base64.decode(data.getBytes(StandardCharsets.UTF_8)));
        return new String(result, StandardCharsets.UTF_8).trim();
    }

    /**
     * AES加密
     * @return
     */
    public static String encryptAES(String data, String pass) {

        AES aes = new AES(Mode.CBC, Padding.ZeroPadding, new SecretKeySpec(pass.getBytes(), KEY_ALGORITHM),new IvParameterSpec(pass.getBytes()));
        byte[] result = aes.encrypt(data);

        String Base64Encode = Base64.encode(result);

		/*//加密为16进制表示
		String encryptHex = aes.encryptHex(result);
		//解密为字符串
		String decryptStr = aes.decryptStr(result, CharsetUtil.CHARSET_UTF_8);*/

        return Base64Encode;
    }

    public static void main(String[] args) {

        try {
            String pasw = PasswordDecoder.encryptAES("123", "river_key354282p");
            System.out.println(pasw);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

}

