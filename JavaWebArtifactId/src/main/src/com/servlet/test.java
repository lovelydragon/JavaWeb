package com.servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(filterName = "test",urlPatterns = {"/filters"})
public class test implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("doFilter");
        servletRequest.getRequestDispatcher("/index.jsp").forward(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
        System.out.println("destroy");
    }
}
