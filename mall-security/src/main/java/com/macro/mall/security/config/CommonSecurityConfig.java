package com.macro.mall.security.config;

import com.macro.mall.security.component.*;
import com.macro.mall.security.util.JwtTokenUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

/** SpringSecurity通用配置 包括通用Bean、Security通用Bean及动态权限通用Bean Created by macro on 2022/5/20. */
@Configuration
public class CommonSecurityConfig {

  /**
   * 【性能优化备注】这里降低了安全性以提高性能，原始写法为：
   *
   * <pre>
   *   return new BCryptPasswordEncoder();
   * </pre>
   *
   * <pre>
   *   注意，修改之前，数据库中对应的密码字段也需要修改为 SCrypt 加密后的密码，否则会导致登录失败。
   *   比如，密码明文为 member123 ，
   *   原 BCryptPasswordEncoder 密码：$2a$10$Q08uzqvtPj61NnpYQZsVvOnyilJ3AU4VdngAcJFGvPhEeqhhC.hhS
   *   新 SCryptPasswordEncoder 密码：$10101$U/VsB0L+cfaJd0F9K8+tqg==$L7/ZjLHhqbg=
   *   执行 SQL 语句：
   *   update ums_member as t
   *     set t.password = replace(t.password,
   *       '$2a$10$Q08uzqvtPj61NnpYQZsVvOnyilJ3AU4VdngAcJFGvPhEeqhhC.hhS',
   *       '$10101$U/VsB0L+cfaJd0F9K8+tqg==$L7/ZjLHhqbg=')
   *   where t.password = '$2a$10$Q08uzqvtPj61NnpYQZsVvOnyilJ3AU4VdngAcJFGvPhEeqhhC.hhS';
   * </pre>
   *
   * @return 密码加密器
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new SCryptPasswordEncoder(2, 1, 1, 8, 16);
  }

  @Bean
  public IgnoreUrlsConfig ignoreUrlsConfig() {
    return new IgnoreUrlsConfig();
  }

  @Bean
  public JwtTokenUtil jwtTokenUtil() {
    return new JwtTokenUtil();
  }

  @Bean
  public RestfulAccessDeniedHandler restfulAccessDeniedHandler() {
    return new RestfulAccessDeniedHandler();
  }

  @Bean
  public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
    return new RestAuthenticationEntryPoint();
  }

  @Bean
  public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
    return new JwtAuthenticationTokenFilter();
  }

  @ConditionalOnBean(name = "dynamicSecurityService")
  @Bean
  public DynamicAccessDecisionManager dynamicAccessDecisionManager() {
    return new DynamicAccessDecisionManager();
  }

  @ConditionalOnBean(name = "dynamicSecurityService")
  @Bean
  public DynamicSecurityMetadataSource dynamicSecurityMetadataSource() {
    return new DynamicSecurityMetadataSource();
  }

  @ConditionalOnBean(name = "dynamicSecurityService")
  @Bean
  public DynamicSecurityFilter dynamicSecurityFilter() {
    return new DynamicSecurityFilter();
  }
}
