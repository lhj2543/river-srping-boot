package com.river.test.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

/*
    @Autowired
    private PasswordDecoderFilter passwordDecoderFilter;
*/

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 自定义拦截器，密码解密拦截器
        /*registry.addInterceptor(passwordDecoderFilter).addPathPatterns(SecurityConstants.OAUTH_TOKEN_URL);*/
    }


    /**
     * 添加资源全局拦截器
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

    }
}
