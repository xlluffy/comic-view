package com.luffy.comic.component;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.luffy.comic.common.api.CommonResult;
import com.luffy.comic.common.utils.JwtTokenUtil;
import com.luffy.comic.service.RedisService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RedisService redisService;

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(this.tokenHeader);
        if (authHeader != null && authHeader.startsWith(this.tokenHead)) {
            String authToken = authHeader.substring(this.tokenHead.length());
            boolean refresh = false;
            try {
                jwtTokenUtil.getClaimsFromToken(authToken);
            } catch (ExpiredJwtException e) {
                jwtTokenUtil.parseJwtPayload(authToken);
                if (jwtTokenUtil.canRefresh()) {
                    refresh = true;
                } else {
                    tokenExceptionHandler(response);
                    return;
                }
            } catch (SignatureException e) {
               tokenExceptionHandler(response);
                return;
            }

            String username = jwtTokenUtil.getUsername();
            if (StrUtil.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    logger.info("authenticated user:{}", username);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    if (refresh) {
                        response.setHeader(tokenHeader, tokenHead + jwtTokenUtil.refreshToken());
//                        response.setHeader("Access-Control-Expose-Headers", tokenHeader);
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private void tokenExceptionHandler(HttpServletResponse response) throws IOException{
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        response.setStatus(401);
        response.getWriter().print(JSONUtil.parse(CommonResult.unauthorized("token已过期")));
        response.getWriter().flush();
    }
}
