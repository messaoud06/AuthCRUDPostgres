package com.example.authcrudpostgres.config.security;

public class SecurityConstants {
    public static final String AUTH_LOGIN_URL = "/api/login";

    // Signing key for HS512 algorithm
    // You can use the page http://www.allkeysgenerator.com/ to generate all kinds of keys
    public static final String JWT_SECRET = "r4u7x!A%C*F-JaNdRgUkXp2s5v8y/B?E(G+KbPeShVmYq3t6w9z$C&F)J@McQfTj";

    // JWT token defaults
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "AuthCrud-api";
    public static final String TOKEN_AUDIENCE = "AuthCrud-app";
    public static final long TOKEN_EXPIRATION = 5 * 60 * 60;

    private SecurityConstants() {
        throw new IllegalStateException("Cannot create instance of static util class");
    }
}
