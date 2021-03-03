package com.river.security.config;

import com.river.security.component.PermitAllUrlProperties;
import com.river.security.component.ResourceAuthExceptionEntryPoint;
import com.river.security.handler.FormAuthenticationFailureHandler;
import com.river.security.handler.RiverAuthenticationSuccessHandler;
import com.river.security.service.CustomUserAuthenticationProvider;
import com.river.security.service.RiverUserDetailsServiceImpl;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * @author river
 * @date 2019/2/1 认证相关配置
 */
@Primary
@Order(90)
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

	@Autowired
	private RiverUserDetailsServiceImpl userDetailsService;

	/**
	 * security 登录成功处理器
	 */
	@Autowired
	private RiverAuthenticationSuccessHandler riverAuthenticationSuccessHandler;

	/**
	 * 自定义身份验证
	 */
	@Autowired
	private CustomUserAuthenticationProvider customUserAuthenticationProvider;

	/**
	 * 表单登录失败处理器
	 */
	@Autowired
	private AuthenticationFailureHandler formAuthenticationFailureHandler;

	/**
	 * 认证失败处理类
	 */
	@Autowired
	private ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint;

	@Autowired
	private PermitAllUrlProperties permitAllUrl;

	@Value("${river.security.defaultSuccessUrl:https://www.baidu.com}")
	private String defaultSuccessUrl;

	@Override
	@SneakyThrows
	protected void configure(HttpSecurity http) {
		http
			//对应表单认证相关的配置
			.formLogin()
				//登录页面
				.loginPage("/rauth/login")
				//登录表单 action = /rauth/form
				.loginProcessingUrl("/rauth/form")
				//登录失败处理器
				.failureHandler(formAuthenticationFailureHandler)
				//默认登录成功跳转地址
				.defaultSuccessUrl(defaultSuccessUrl)
				//登录成功处理器
				.successHandler(riverAuthenticationSuccessHandler)
				.and()

			// 认证失败处理类
			.exceptionHandling().authenticationEntryPoint(resourceAuthExceptionEntryPoint).and()
			// 基于token，所以不需要session
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

			//basic登录 （弹出输入用户密码窗口）
			//.httpBasic().and()
			// 允许使用iframe 嵌套，避免swagger-ui 不被加载的问题
			.headers().frameOptions().disable().and()
			// CRSF禁用，因为不使用session
			.csrf().disable();

		//配置路径拦截，表明路径访问所对应的权限，角色，认证信息
		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
		//不拦截的路径
		//不拦截的路径
		registry.antMatchers("/rauth/**", "/actuator/**", "/oauth/**").permitAll();
		permitAllUrl.getUrls().forEach(url -> registry.antMatchers(url).permitAll());
		//所有请求都需要通过认证
		registry.anyRequest().authenticated().and();

	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/css/**");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		/*auth.inMemoryAuthentication()
				// 在内存中创建用户并为密码加密
				.withUser("user").password(passwordEncoder().encode("123")).roles("USER")
				.and()
				.withUser("admin").password(passwordEncoder().encode("123")).roles("ADMIN");*/

		//auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());

		//自定义身份验证
		auth.authenticationProvider(customUserAuthenticationProvider);

	}

	@Bean
	@Override
	@SneakyThrows
	public AuthenticationManager authenticationManagerBean() {
		return super.authenticationManagerBean();
	}

	/**
	 * 表单登录失败处理器
	 * @return
	 */
	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return new FormAuthenticationFailureHandler();
	}

	/**
	 * 配置密码加密方式 BCrypt
	 * @return PasswordEncoder
	 */
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		// 设置默认的加密方式
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		String client_secret = new BCryptPasswordEncoder().encode("123");
		System.err.println(client_secret);
	}

}
