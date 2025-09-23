package ru.cr.hw.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Arrays;

public class MyOwnFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        logger.info("Filter triggered for URI: " + request.getRequestURI());
        var requestWrapper = new HttpServletRequestWrapper((HttpServletRequest) servletRequest) {
            @Override
            public String[] getParameterValues(String name) {
                if ("SpecialValue".equals(name)) {
                    logger.info("Replacing SpecialValue parameter");
                    return new String[]{"My dirty secret"};
                }
                String[] originalValues = super.getParameterValues(name);
                logger.info("Parameter " + name + " values: " + Arrays.toString(originalValues));
                return super.getParameterValues(name);
            }
        };

        filterChain.doFilter(requestWrapper, servletResponse);
    }
}
