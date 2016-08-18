package com.jukta.jtahoe.servlet;

import sun.misc.IOUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * @since 1.0
 */
public class CpResourceFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(((HttpServletRequest) request).getRequestURI());
        if (is == null) {
            chain.doFilter(request, response);
        } else {
            String cont = new String(IOUtils.readFully(is, -1, false));
            response.getOutputStream().print(cont);
        }
    }

    @Override
    public void destroy() {

    }
}
