package com.river.site;

import com.river.security.annotation.EnableRiverResourceServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * river 站点管理
 * @author 17822
 */
@SpringBootApplication(scanBasePackages = "com.river.*")
//springSecurity 资源服务
@EnableRiverResourceServer
@MapperScan("com.river.*.mapper")
public class SiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(SiteApplication.class,args);
    }

}
