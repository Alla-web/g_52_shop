package de.aittr.g_52_shop.security.sec_dto;

import java.util.Objects;

public class RefreshRequestDto {

    //при помощи этой ДТО клиент будет присылать нам рефреш-токен
    // на получение нового аксес-токена

    private String refreshToken;

    //конструктор не нужен никакой, кроме пустого по умолчанию, т.к.
    // запросы нам будет присылать клиент

    //геттер и сеттер

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    //methods

    @Override
    public String toString() {
        return String.format("Refresh Request Dto: refresh token - %s",
                refreshToken);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RefreshRequestDto that = (RefreshRequestDto) o;
        return Objects.equals(refreshToken, that.refreshToken);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(refreshToken);
    }
}
