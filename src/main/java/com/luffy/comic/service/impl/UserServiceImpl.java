package com.luffy.comic.service.impl;

import com.luffy.comic.common.utils.JwtTokenUtil;
import com.luffy.comic.mapper.UserMapper;
import com.luffy.comic.mapper.UserRoleRelationMapper;
import com.luffy.comic.model.Permission;
import com.luffy.comic.model.User;
import com.luffy.comic.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * UmsAdminService的实现类
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserMapper userMapper;
    private UserDetailsService userDetailService;
    private UserRoleRelationMapper adminRoleRelationMapper;
    private JwtTokenUtil jwtTokenUtil;
    private PasswordEncoder passwordEncoder;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    public UserServiceImpl(UserMapper userMapper, UserDetailsService userDetailService,
                           UserRoleRelationMapper adminRoleRelationMapper,
                           JwtTokenUtil jwtTokenUtil, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.userDetailService = userDetailService;
        this.adminRoleRelationMapper = adminRoleRelationMapper;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getAdminByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public User register(User user) {
        if (userMapper.findByUsername(user.getUsername()) != null) {
            return null;
        }
        user.setStatus(1);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insert(user);
        adminRoleRelationMapper.addRoleToAdmin(user.getId(), 4);
//        logger.warn("You cannot register an account now.");
        return user;
    }

    @Override
    public void login(String username, String password) {
        try {
            UserDetails userDetails = userDetailService.loadUserByUsername(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (AuthenticationException e) {
            logger.warn("登陆异常: {}", e.getMessage());
        }
    }

    @Override
    public String loginAuth(String username, String password) {
        String token = null;
        try {
            UserDetails userDetails = userDetailService.loadUserByUsername(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            token = jwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            logger.warn("登陆异常: {}", e.getMessage());
        }
        return token;
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }

    @Override
    public void update(User user) {
        /*User origin = umsAdminMapper.findByUsername(user.getUsername());
        if (origin == null) {
            logger.warn("{} not exists.", user.getUsername());
            return;
        }*/
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.update(user);
    }

    @Override
    public List<Permission> getPermissionList(int adminId) {
        return adminRoleRelationMapper.findPermissionByAdminId(adminId);
    }
}
