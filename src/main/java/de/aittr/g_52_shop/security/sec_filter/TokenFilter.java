package de.aittr.g_52_shop.security.sec_filter;

import de.aittr.g_52_shop.security.AuthInfo;
import de.aittr.g_52_shop.security.sec_service.TokenService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
public class TokenFilter extends GenericFilterBean {

    //объект этого фильтра будет использоваться для проверки фильтрации чттп-запросов

    private final TokenService service;

    public TokenFilter(TokenService service) {
        this.service = service;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = getTokenFromRequest((HttpServletRequest) servletRequest);

        if (token != null && service.validateAccessToken(token)){
            Claims claims = service.getAccessClaims(token);
            AuthInfo authInfo = service.mapClaimsToAuthInfo(claims);
            authInfo.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(authInfo);
        }
        //чтобы наш запрос шёл дальше по цепочке фильтров
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getTokenFromRequest(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        //токен в виде
        // Bearer jhklfgjhkaldjglkdjfadlfkjga;ldkgf
        //нам нужно отрезать слово Bearer
        //2-е -токена может быть в заросе и не будет

        if (token != null && token.startsWith("Bearer ")){
            return token.substring(7);
        }
        return null;
    }
}
