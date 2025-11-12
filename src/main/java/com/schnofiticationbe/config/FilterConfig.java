package com.schnofiticationbe.config;
import com.schnofiticationbe.config.filter.RequestWrapperFilter;
import com.schnofiticationbe.config.filter.ResponseWrapperFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    //LoggingInterceptor가 제 기능을 할 수 있도록 사전 작업을 해주는 필터들을 등록

    /// RequestWrapperFilter: 요청 본문을 여러 번 읽을 수 있도록 래핑

    public FilterRegistrationBean<RequestWrapperFilter> requestWrapperFilter() {
        FilterRegistrationBean<RequestWrapperFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestWrapperFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
    /// ResponseWrapperFilter: 응답 본문을 캡처하고 로깅할
    public FilterRegistrationBean<ResponseWrapperFilter> responseWrapperFilter() {
        FilterRegistrationBean<ResponseWrapperFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ResponseWrapperFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}