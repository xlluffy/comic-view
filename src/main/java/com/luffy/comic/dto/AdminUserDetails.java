package com.luffy.comic.dto;

import com.luffy.comic.model.Permission;
import com.luffy.comic.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SpringSecurity需要的用户详情
 */
public class AdminUserDetails extends User implements UserDetails {
    private List<Permission> permissionList;
    private List<String> roleList;

    public AdminUserDetails() {

    }

    public AdminUserDetails(User user, List<Permission> permissionList, List<String> roleList) {
        super(user);
        this.permissionList = permissionList;
        this.roleList = roleList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> roles = permissionList.stream().
                filter(permission -> permission.getValue() != null).
                map(Permission::getValue).collect(Collectors.toList());
        roles.addAll(roleList);
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.getStatus() == 1;
    }
}
