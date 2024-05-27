package com.gzx.hotel.core.pojo;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Data
public class LoginUser implements UserDetails {

    private String username;

    private String password;

    private Integer locked;

    private Set<SimpleGrantedAuthority> authorities;

    public LoginUser(String username, String password, Integer locked, Set<SimpleGrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.locked = locked;
        this.authorities = authorities;
    }

    public LoginUser() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void erasePassword() {
        this.password = null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !(locked == 1);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object rhs) {
        return this == rhs || rhs instanceof LoginUser && this.getUsername().equals(((LoginUser) rhs).getUsername());
    }

    @Override
    public int hashCode() {
        return this.getUsername().hashCode();
    }
}
