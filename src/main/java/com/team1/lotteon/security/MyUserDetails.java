//package com.team1.lotteon.security;
//
//import com.farmstory.entity.User;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//@Getter
//@Setter
//@ToString
//@Builder
//public class MyUserDetails implements UserDetails , OAuth2User {
//
//    //User 엔티티 선언
//    private User user;
//
//    private Map<String, Object> attributes;
//    private String accessToken;
//
//    @Override
//    public List<GrantedAuthority> getAuthorities() {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getUserRole()));
//        return authorities;
//    }
//
//    @Override
//    public Map<String, Object> getAttributes() {
//        return attributes;
//    }
//    @Override
//    public String getName() {
//        return user.getUserName();
//    }
//
////    @Override
////    public Collection<? extends GrantedAuthority> getAuthorities() {
////        //계정이 갖는 권한 목록 생성
////        List<GrantedAuthority> authorities = new ArrayList<>();
////        authorities.add(new SimpleGrantedAuthority("ROLE_"+user.getUserRole())); //계정권한 앞에 접두어 ROLE_ 붙여야 됨
////        return authorities;
////    }
//
//    @Override
//    public String getPassword() {
//        return user.getUserPass();
//    }
//
//    @Override
//    public String getUsername() {
//        return user.getUserUid();
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        //계정 만료 여부(true: 만료안됨, false:만료)
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        //계정 잠김 여부(true : 잠김아님, false : 잠김)
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        // 비밀번호 만료 여부(true : 만료안됨, false : 만료)
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        //계정 활성 여부(true : 활성화, false : 비활성화)
//        return true;
//    }
//}
