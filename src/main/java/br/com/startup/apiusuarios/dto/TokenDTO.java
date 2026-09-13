package br.com.startup.apiusuarios.dto;

import lombok.Getter;

@Getter
public class TokenDTO {

    private final String token;
    private final String tipo = "Bearer";

    public TokenDTO(String token) {
        this.token = token;
    }
}