package com.luffy.comic.service.impl;

import com.luffy.comic.common.utils.JwtTokenUtil;
import com.luffy.comic.mapper.UmsAdminMapper;
import com.luffy.comic.mapper.UmsAdminRoleRelationMapper;
import com.luffy.comic.model.UmsAdmin;
import com.luffy.comic.model.UmsPermission;
import com.luffy.comic.service.UmsAdminService;
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
public class UmsAdminServiceImpl implements UmsAdminService {
    private static final Logger logger = LoggerFactory.getLogger(UmsAdminServiceImpl.class);

    private UmsAdminMapper umsAdminMapper;
    private UserDetailsService userDetailService;
    private UmsAdminRoleRelationMapper adminRoleRelationMapper;
    private JwtTokenUtil jwtTokenUtil;
    private PasswordEncoder passwordEncoder;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    public UmsAdminServiceImpl(UmsAdminMapper umsAdminMapper, UserDetailsService userDetailService,
                               UmsAdminRoleRelationMapper adminRoleRelationMapper,
                               JwtTokenUtil jwtTokenUtil, PasswordEncoder passwordEncoder) {
        this.umsAdminMapper = umsAdminMapper;
        this.userDetailService = userDetailService;
        this.adminRoleRelationMapper = adminRoleRelationMapper;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UmsAdmin getAdminByUsername(String username) {
        return umsAdminMapper.findByUsername(username);
    }

    @Override
    public UmsAdmin register(UmsAdmin umsAdmin) {
        if (umsAdminMapper.findByUsername(umsAdmin.getUsername()) != null) {
            return null;
        }
        umsAdmin.setStatus(1);
        umsAdmin.setPassword(passwordEncoder.encode(umsAdmin.getPassword()));
        umsAdminMapper.insert(umsAdmin);
//        logger.warn("You cannot register an account now.");
        return umsAdmin;
    }

    @Override
    public String login(String username, String password) {
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
    public void update(UmsAdmin umsAdmin) {
        /*UmsAdmin origin = umsAdminMapper.findByUsername(umsAdmin.getUsername());
        if (origin == null) {
            logger.warn("{} not exists.", umsAdmin.getUsername());
            return;
        }*/
        umsAdmin.setPassword(passwordEncoder.encode(umsAdmin.getPassword()));
        umsAdminMapper.update(umsAdmin);
    }

    @Override
    public List<UmsPermission> getPermissionList(int adminId) {
        return adminRoleRelationMapper.findPermissionByAdminId(adminId);
    }
}
