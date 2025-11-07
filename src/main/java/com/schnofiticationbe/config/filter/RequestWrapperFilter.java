package com.schnofiticationbe.config.filter;

import com.schnofiticationbe.config.CustomHttpRequestWrapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import jakarta.servlet.*;

import java.io.IOException;


@Component
public class RequestWrapperFilter implements Filter {

    ///  요청이 들어오면 CustomHttpRequestWrapper로 감싸서 body를 여러번 읽을 수 있게 한다.
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest){
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String contentType = httpServletRequest.getContentType();
            if (contentType!=null&& contentType.startsWith("multipart/form-data")){
                chain.doFilter(request, response);
            }else{
                CustomHttpRequestWrapper wrappedRequest = new CustomHttpRequestWrapper(httpServletRequest);
                chain.doFilter(wrappedRequest, response);
            }
        } else {
            chain.doFilter(request, response);
        }

    }
}


