package com.jukta.jtahoe;

import com.jukta.jtahoe.resource.*;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @since 1.0
 */
public class LibraryResourcesFilter implements Filter {

    private String jsContent;
    private String cssContent;
    private String name;
    private LibraryResources libraryResources;

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
            libraryResources = new LibraryResources();
            cssContent = join(libraryResources, ResourceType.CSS);
            jsContent = join(libraryResources, ResourceType.JS);

        } catch (IOException e) {
            throw new ServletException(e);
        }
    }

    private String join(LibraryResources libraryResources, ResourceType resourceType) throws IOException {
        List<Resource> files = libraryResources.getFiles(resourceType);
        files.addAll(new Resources().getResources(resourceType));
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
        } else if (!path.endsWith("/")) {
            Resource r = libraryResources.getFile(path);
            if (r != null) content = ResourceAppender.append(Collections.singletonList(r)).toString();
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
