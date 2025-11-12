package com.schnofiticationbe.config.filter;
import com.schnofiticationbe.config.CustomHttpResponseWrapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ResponseWrapperFilter implements Filter {
    ///  응답 데이터를 가로채기 위한 필터
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (response instanceof HttpServletResponse) {
            CustomHttpResponseWrapper responseWrapper = new CustomHttpResponseWrapper((HttpServletResponse) response);
            chain.doFilter(request, responseWrapper);

            // 응답 데이터가 null이거나 비어있지 않은 경우에만 클라이언트로 출력
            byte[] responseData = responseWrapper.getResponseData();
            String responseBody = new String(responseWrapper.getResponseData());
            if (responseBody!=null&&!responseBody.isEmpty()){
                response.getOutputStream().write(responseData);
            }

        }else {
            chain.doFilter(request, response);
        }
    }
}
