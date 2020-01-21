package com.luffy.comic.config;

import com.luffy.comic.common.utils.CookieUtil;
import com.luffy.comic.component.JwtAuthenticationTokenFilter;
import com.luffy.comic.component.RestfulAccessDeniedHandler;
import com.luffy.comic.dto.AdminUserDetails;
import com.luffy.comic.model.Permission;
import com.luffy.comic.model.User;
import com.luffy.comic.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.util.List;


/**
 * SpringSecurity的配置
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    private UserService adminService;
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf()// 由于使用的是JWT，我们这里不需要csrf
                .disable()
//                .sessionManagement()// 基于token，所以不需要session
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
                .formLogin().loginPage("/admin/login")
                .successForwardUrl("/comic/index").failureForwardUrl("/admin/login")
                .and()
                .logout().logoutUrl("/admin/logout").logoutSuccessUrl("/admin/login").logoutSuccessHandler(logoutSuccessHandler())
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, // 允许对于网站静态资源的无授权访问
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/swagger-resources/**",
                        "/v2/api-docs/**"
                )
                .permitAll()
                .antMatchers( "/admin/loginAuth", "/admin/login", "/admin/register")// 对登录注册要允许匿名访问
                .permitAll()
                .antMatchers(HttpMethod.OPTIONS)//跨域请求会先进行一次options请求
                .permitAll()
//                 .antMatchers("/**")//测试时全部运行访问
//                 .permitAll();
               .anyRequest()// 除上面外的所有请求全部需要鉴权认证
               .authenticated();
        // 禁用缓存
        httpSecurity.headers().cacheControl();
        // 添加JWT filter
//        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        // 添加自定义未授权和未登录结果返回
        httpSecurity.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler);
        httpSecurity.sessionManagement().maximumSessions(10).expiredUrl("/admin/login");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
        auth.eraseCredentials(false);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        //获取登录用户信息
        return username -> {
            User admin = adminService.getAdminByUsername(username);
            if (admin != null) {
                List<Permission> permissionList = adminService.getPermissionList(admin.getId());
                return new AdminUserDetails(admin, permissionList);
            }
            throw new UsernameNotFoundException("用户名或密码错误");
        };
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (httpServletRequest, httpServletResponse, authentication) -> {
            try {
                User user = (User) authentication.getPrincipal();
                logger.info("USER: " + user.getUsername() + " LOGOUT SUCCESS.");
                CookieUtil.removeCookie(httpServletRequest, httpServletResponse, "JSESSIONID");
            } catch (Exception e) {
                logger.info("LOGOUT EXCEPTION , e : " + e.getMessage());
            }
                httpServletResponse.sendRedirect("/admin/login");
        };
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void setAdminService(UserService adminService) {
        this.adminService = adminService;
    }

    @Autowired
    public void setRestfulAccessDeniedHandler(RestfulAccessDeniedHandler restfulAccessDeniedHandler) {
        this.restfulAccessDeniedHandler = restfulAccessDeniedHandler;
    }
}
