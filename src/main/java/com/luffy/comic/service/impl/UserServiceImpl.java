package com.luffy.comic.service.impl;

import com.luffy.comic.common.utils.JwtTokenUtil;
import com.luffy.comic.common.utils.SecurityUtil;
import com.luffy.comic.dto.UserProfileParamForm;
import com.luffy.comic.mapper.UserLoginLogMapper;
import com.luffy.comic.mapper.UserMapper;
import com.luffy.comic.mapper.UserRoleRelationMapper;
import com.luffy.comic.model.User;
import com.luffy.comic.model.UserLoginLog;
import com.luffy.comic.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * UmsAdminService的实现类
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserMapper userMapper;
    private UserLoginLogMapper userLoginLogMapper;
    private UserDetailsService userDetailService;
    private UserRoleRelationMapper userRoleRelationMapper;
    private JwtTokenUtil jwtTokenUtil;
    private PasswordEncoder passwordEncoder;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    public UserServiceImpl(UserMapper userMapper, UserLoginLogMapper userLoginLogMapper,
                           UserRoleRelationMapper userRoleRelationMapper,
                           JwtTokenUtil jwtTokenUtil) {
        this.userMapper = userMapper;
        this.userLoginLogMapper = userLoginLogMapper;
        this.userRoleRelationMapper = userRoleRelationMapper;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public User getById(Integer id) {
        return userMapper.findById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public User getUserByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    @Override
    public User register(User user) {
        if (userMapper.findByUsername(user.getUsername()) != null) {
            return null;
        }
        user.setStatus(1);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getIcon() == null) {
            user.setIcon("/images/face/default.jpg");
        }
        userMapper.insert(user);
        userRoleRelationMapper.addRoleToUser(user.getId(), "ROLE_USER");
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
    public void loginLog(HttpServletRequest request, User user) {
        if (!"0:0:0:0:0:0:0:1".equals(request.getRemoteAddr())) {
            UserLoginLog loginLog = new UserLoginLog();
            loginLog.setUserId(user.getId());
            loginLog.setIp(request.getRemoteAddr());
            loginLog.setUserAgent(request.getHeader("User-Agent"));
            userLoginLogMapper.insert(loginLog);
        }
    }

    @Override
    public String loginAuth(String username, String password, boolean rememberMe) {
        String token = null;
        try {
            UserDetails userDetails = userDetailService.loadUserByUsername(username);
            /*if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }*/
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            token = jwtTokenUtil.generateToken(userDetails, rememberMe);
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
    public void update(UserProfileParamForm user) {
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userMapper.update(new User(user));

        // 这方法也太笨了，什么时候修一下
        User currentUser = SecurityUtil.getCurrentUserNotNull();
        if (user.getNickName() != null) {
            currentUser.setNickName(user.getNickName());
        }
        if (user.getNote() != null) {
            currentUser.setNote(user.getNote());
        }
        if (user.getIcon() != null) {
            currentUser.setIcon(user.getIcon());
        }
    }

    @Override
    public boolean updateEmail(String oldEmail, String newEmail) {
        if (oldEmail == null || newEmail == null || oldEmail.equals(newEmail))
            return false;
        User user = SecurityUtil.getCurrentUserNotNull();
        if (!oldEmail.equals(user.getEmail())) {
            return false;
        }
        if (!Pattern.matches("^[\\w\\u4e00-\\u9fa5]+@\\w+\\.\\w+", newEmail)) {
            return false;
        }
        user.setEmail(newEmail);
        userMapper.update(user); // 似乎有数据库更新失败，但session修改成功的风险
        return true;
    }

    @Override
    public boolean updatePwd(String oldPwd, String newPwd) {
        if (oldPwd == null || newPwd == null || oldPwd.equals(newPwd))
            return false;
        User user = SecurityUtil.getCurrentUserNotNull();
        if (!passwordEncoder.matches(oldPwd, user.getPassword())) {
            return false;
        }
        if (!Pattern.matches("^[\\x21-\\x7e]{6,16}$", newPwd)) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(newPwd));
        userMapper.update(user);
        return true;
    }

    @Autowired
    public void setUserDetailService(@Qualifier("userDetailsService") UserDetailsService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
