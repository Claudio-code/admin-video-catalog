package com.admin.catalog.infrastructure;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

public class ApiTest {
    static JwtRequestPostProcessor ADMIN_JWT =
        jwt().authorities(new SimpleGrantedAuthority("ROLE_CATALOGO_ADMIN"));

    static JwtRequestPostProcessor CATEGORIES_JWT =
        jwt().authorities(new SimpleGrantedAuthority("ROLE_CATALOGO_CATEGORIES"));

    static JwtRequestPostProcessor CAST_MEMBERS_JWT =
        jwt().authorities(new SimpleGrantedAuthority("ROLE_CATALOGO_CAST_MEMBERS"));

    static JwtRequestPostProcessor GENRES_JWT =
        jwt().authorities(new SimpleGrantedAuthority("ROLE_CATALOGO_GENRES"));

    static JwtRequestPostProcessor VIDEOS_JWT =
        jwt().authorities(new SimpleGrantedAuthority("ROLE_CATALOGO_VIDEOS"));
}
