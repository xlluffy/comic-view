package com.luffy.comic.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultHeader;
import io.jsonwebtoken.impl.DefaultJwsHeader;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.impl.compression.DefaultCompressionCodecResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    private static final String CLAIM_REFRESH_EXPIRED = "refresh-expired";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.refresh-expired}")
    private int refreshExpired;

    private Claims claims;

    /**
     * 根据负载生成JWT的token
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 从token中获取JWT中的负载
     * 在所有需求claims的方法前执行...
     */
    public void getClaimsFromToken(String token) throws ExpiredJwtException, SignatureException {
//        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        /*} catch (SignatureException e) {
            logger.info(e.getMessage());
            logger.info("JWT格式验证失败：{}", token);
        }*/
    }

    /**
     * 生成token的过期时间
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 从token中获取登录用户名
     */
    public String getUsername() {
        try {
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 验证token是否还有效
     * @param username      token解析出的username
     * @param userDetails   从数据库查询出来的用户信息
     */
    public boolean validateToken(String username, UserDetails userDetails) {
        return username.equals(userDetails.getUsername());
    }

    /**
     * 判断token对应的claims是否已经失效
     */
    public boolean isTokenExpired() {
        try {
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * 根据用户信息生成token
     */
    public String generateToken(UserDetails userDetails, boolean rememberMe) {
        Map<String, Object> claims = new HashMap<>();
        Date now = new Date();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, now);
        claims.put(CLAIM_REFRESH_EXPIRED, rememberMe ? DateUtil.offsetSecond(now, refreshExpired) : now);
        return generateToken(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, false);
    }

    /**
     * 判断token是否可以被刷新
     */
    public boolean canRefresh() {
        Date refresh = new Date((long) claims.get(CLAIM_REFRESH_EXPIRED));
        return claims.getExpiration().before(refresh);
    }

    public void parseJwtPayload(String jwt) {
        String base64UrlEncodedHeader = null;
        String base64UrlEncodedPayload = null;
        String base64UrlEncodedDigest = null;
        int delimiterCount = 0;
        StringBuilder sb = new StringBuilder(128);
        for (char c : jwt.toCharArray()) {
            if (c == '.') {
                CharSequence tokenSeq = io.jsonwebtoken.lang.Strings.clean(sb);
                String token = tokenSeq != null ? tokenSeq.toString() : null;

                if (delimiterCount == 0) {
                    base64UrlEncodedHeader = token;
                } else if (delimiterCount == 1) {
                    base64UrlEncodedPayload = token;
                }

                delimiterCount++;
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        if (delimiterCount != 2) {
            String msg = "JWT strings must contain exactly 2 period characters. Found: " + delimiterCount;
            throw new MalformedJwtException(msg);
        }
        if (sb.length() > 0) {
            base64UrlEncodedDigest = sb.toString();
        }
        if (base64UrlEncodedPayload == null) {
            throw new MalformedJwtException("JWT string '" + jwt + "' is missing a body/payload.");
        }
        // =============== Header =================
        Header header = null;
        CompressionCodec compressionCodec = null;
        if (base64UrlEncodedHeader != null) {
            String origValue = TextCodec.BASE64URL.decodeToString(base64UrlEncodedHeader);
            Map<String, Object> m = readValue(origValue);
            if (base64UrlEncodedDigest != null) {
                header = new DefaultJwsHeader(m);
            } else {
                header = new DefaultHeader(m);
            }
            CompressionCodecResolver codecResolver = new DefaultCompressionCodecResolver();
            compressionCodec = codecResolver.resolveCompressionCodec(header);
        }
        // =============== Body =================
        String payload;
        if (compressionCodec != null) {
            byte[] decompressed = compressionCodec.decompress(TextCodec.BASE64URL.decode(base64UrlEncodedPayload));
            payload = new String(decompressed, io.jsonwebtoken.lang.Strings.UTF_8);
        } else {
            payload = TextCodec.BASE64URL.decodeToString(base64UrlEncodedPayload);
        }
        claims = new DefaultClaims(JSONUtil.parseObj(payload));
    }

    private Map<String, Object> readValue(String val) {
        try {
            return new ObjectMapper().readValue(val, Map.class);
        } catch (IOException e) {
            throw new MalformedJwtException("Unable to read JSON value: " + val, e);
        }
    }

    /**
     * 刷新token
     */
    public String refreshToken() {
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }
}
