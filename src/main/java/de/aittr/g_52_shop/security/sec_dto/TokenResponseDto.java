package de.aittr.g_52_shop.security.sec_dto;

import java.util.Objects;

public class TokenResponseDto {

    private String accessToken;
    private String refreshToken;

    //constructor
    //пустой - для получения
    public TokenResponseDto() { }

    //для генерации повторного токена после его истечения - для отправления
    public TokenResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }

    //для первой генерации токенов
    public TokenResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    //getters

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    //сеттеры - для получения

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    //methods

    @Override
    public String toString() {
        return String.format("Token Response Dto: access token - %s; refresh token - %s",
                accessToken, refreshToken);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TokenResponseDto that = (TokenResponseDto) o;
        return Objects.equals(accessToken, that.accessToken) && Objects.equals(refreshToken, that.refreshToken);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(accessToken);
        result = 31 * result + Objects.hashCode(refreshToken);
        return result;
    }
}
