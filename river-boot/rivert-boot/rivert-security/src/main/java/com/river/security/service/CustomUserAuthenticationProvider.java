package com.river.security.service;

import com.river.common.core.exception.BusinessServiceException;
import com.river.security.util.PasswordDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *  自定义身份验证
 * @author river
 */
@Component
@Slf4j
public class CustomUserAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private RiverUserDetailsServiceImpl customUserDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 加解密秘钥，必须16位字符
     */
    @Value("${security.encode.key:river_key354282p}")
    private  String encodeKey;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        UserDetails user = null;
        String username = authentication.getName();
        String password = (String)authentication.getCredentials();
        try {
            user = this.customUserDetailsService.loadUserByUsername(username);
        } catch (Exception e) {
            throw new BusinessServiceException(e.getMessage());
        }

        try {
            password = PasswordDecoder.decryptAES(password,encodeKey);
        } catch (Exception e) {
            log.error("AES 密码解密失败:{}", password);
        }

        if (!this.bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new BusinessServiceException("账号或密码不正确!");
        }

        return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());

    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
