package itacad.aliaksandrkryvapust.productmicroservice.controller.filter;

import itacad.aliaksandrkryvapust.productmicroservice.core.Constants;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter
public class ContentTypeAndEncodingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
// This method intentionally left blank
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding(Constants.ENCODING);
        response.setContentType(Constants.CONTENT_TYPE);
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {
// This method intentionally left blank
    }
}

