package com.sigeg.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperties {
    private String secret = "sigeg-chave-secreta-alterar-em-producao-minimo-256bits";
    private long expiration = 86400000L;
    private long refreshExpiration = 604800000L;
}
