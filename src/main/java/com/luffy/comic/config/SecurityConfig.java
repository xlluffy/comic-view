package com.luffy.comic.config;

import com.luffy.comic.common.utils.CookieUtil;
import com.luffy.comic.component.JwtAuthenticationTokenFilter;
import com.luffy.comic.component.RestfulAccessDeniedHandler;
import com.luffy.comic.dto.AdminUserDetails;
import com.luffy.comic.model.Permission;
import com.luffy.comic.model.User;
import com.luffy.comic.service.UserAdminService;
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
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.List;


/**
 * SpringSecurity的配置
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    private UserService userService;
    private UserAdminService userAdminService;
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf()// 由于使用的是JWT，我们这里不需要csrf
                .disable()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
                .formLogin().loginPage("/login").successHandler(loginSuccessHandler())
//                .failureForwardUrl("/login?error=1")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/login").logoutSuccessHandler(logoutSuccessHandler())
                .and()
                .anonymous().key("anonymous").authorities("ROLE_ANONYMOUS")
                .and()
                .rememberMe().tokenValiditySeconds(24 * 60 * 60).tokenRepository(getPersistentTokenRepository())
                .and()
                .authorizeRequests().antMatchers(HttpMethod.OPTIONS)//跨域请求会先进行一次options请求
                .permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/**").permitAll();
        // 禁用缓存
        httpSecurity.headers().cacheControl();
        // 添加JWT filter
//        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        // 添加自定义未授权和未登录结果返回
        httpSecurity.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler);
        httpSecurity.sessionManagement().maximumSessions(10).expiredUrl("/login");
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
            User user = userService.getUserByUsername(username);
            if (user != null) {
                List<Permission> permissionList = userAdminService.getPermissionList(user.getId());
                List<String> roleList = userAdminService.getRoles(user.getId());
                return new AdminUserDetails(user, permissionList, roleList);
            }
            throw new UsernameNotFoundException("用户名或密码错误");
        };
    }

    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return (request, response, authentication) -> {
            try {
                User user = (User) authentication.getPrincipal();
                logger.info("USER: " + user.getUsername() + " LOGIN SUCCESS.");
//                session.setMaxInactiveInterval(10);
                userService.loginLog(request, user);
                response.sendRedirect("/");
            } catch (Exception e) {
                logger.info("LOGIN EXCEPTION, e :" + e.getMessage());
            }
        };
    }

    @Bean
    public AuthenticationFailureHandler loginFailureHandler() {
        return (httpServletRequest, httpServletResponse, e) -> {
            logger.info(e.getMessage());
            httpServletResponse.sendRedirect("/login/error=1");
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
                httpServletResponse.sendRedirect("/login");
        };
    }

    @Bean
    public PersistentTokenRepository getPersistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl=new JdbcTokenRepositoryImpl();
        jdbcTokenRepositoryImpl.setDataSource(dataSource);
        return jdbcTokenRepositoryImpl;
    }
//    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setUserAdminService(UserAdminService userAdminService) {
        this.userAdminService = userAdminService;
    }

    @Autowired
    public void setRestfulAccessDeniedHandler(RestfulAccessDeniedHandler restfulAccessDeniedHandler) {
        this.restfulAccessDeniedHandler = restfulAccessDeniedHandler;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
