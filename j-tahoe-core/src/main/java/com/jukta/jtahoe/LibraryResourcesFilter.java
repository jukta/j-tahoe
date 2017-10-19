package com.jukta.jtahoe;

import com.jukta.jtahoe.resource.Resource;
import com.jukta.jtahoe.resource.ResourceAppender;
import com.jukta.jtahoe.resource.ResourceType;
import com.jukta.jtahoe.resource.Resources;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @since 1.0
 */
public class LibraryResourcesFilter implements Filter {

    private String jsContent;
    private String cssContent;
    private String name;

    public LibraryResourcesFilter() {
    }

    public LibraryResourcesFilter(String name) {
        setName(name);
    }

    public void setName(String name) {
        if (!name.startsWith("/")) {
            name = "/" + name;
        }
        this.name = name;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            if (name == null) {
                setName(filterConfig.getInitParameter("name"));
            }
            cssContent = join(ResourceType.CSS);
            jsContent = join(ResourceType.JS);

        } catch (IOException e) {
            throw new ServletException(e);
        }
    }

    private String join(ResourceType resourceType) throws IOException {
        List<Resource> files = new ArrayList<>();
        files.addAll(Optional.ofNullable(new Resources().getResources(resourceType)).orElse(new ArrayList<>()));
        return ResourceAppender.append(files).toString();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String path = ((HttpServletRequest) request).getRequestURI();
        String content = null;
        if (path.equals(name + ".js")) {
            content = jsContent;
            response.setContentType("text/javascript");
        } else if (path.equals(name + ".css")) {
            content = cssContent;
            response.setContentType("text/css");
        }
        if (content == null) {
            chain.doFilter(request, response);
        } else {
            response.getOutputStream().print(content);
        }
    }

    @Override
    public void destroy() {

    }

}
