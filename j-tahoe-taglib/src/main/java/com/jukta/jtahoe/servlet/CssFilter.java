package com.jukta.jtahoe.servlet;

import com.jukta.jtahoe.resource.*;

import javax.servlet.*;
import java.io.IOException;
import java.util.List;

/**
 * @since 1.0
 */
public class CssFilter implements Filter {

    private String content;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            LibraryResources lr = new LibraryResources();
            List<Resource> files = lr.getFiles(getFilter());
            files.addAll(new Resources().getResources(new ResourceExtensionFilter(getFilter())));
            StringBuilder sb = ResourceAppender.append(files);
            content = sb.toString();
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }

    protected ResourceType getFilter() {
        return ResourceType.CSS;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.getOutputStream().print(content);
    }

    @Override
    public void destroy() {

    }

}
