package com.study.event.api.auth.filter;



import com.study.event.api.auth.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//클라이언트가 요청에 포함한 토큰정보를 검사하는 필터
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {


    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            //요청 메세지에서 토큰을 파싱
            //ㅌ큰정보는 요청
            String token = parseBearerToken(request);

                log.info("코튼 위조검사 필터 작동");
            if (token != null) {
                //토큰 위조 검사
                tokenProvider.validateAndGetTokenInfo(token);
            }

        } catch (Exception e){
            log.warn("토큰이 위조되었습니다.");
            e.printStackTrace();
        }

        //필터체인에 내가만든 커스텀 필터를 실행하도록 명령
        // 필터체인 : 필터는 여러개임 우리가 체인에 걸어놓은필터를
        // 실행명령
        filterChain.doFilter(request,response);
    }

    private String parseBearerToken(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");

        //톻큰에 붙어잇는  Bearer라는 문자열 제거
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7);
        }

        return null;
    }


}
