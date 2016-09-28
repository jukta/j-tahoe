package com.jukta.jtahoe.servlet;

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

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        try {

            String blocksFolder = filterConfig.getServletContext().getInitParameter("blocksDir");
            if (blocksFolder == null) {
                blocksFolder = "blocks";
            }

            name = filterConfig.getInitParameter("name");
            libraryResources = new LibraryResources();
            cssContent = join(libraryResources, ResourceType.CSS, blocksFolder);
            jsContent = join(libraryResources, ResourceType.JS, blocksFolder);

        } catch (IOException e) {
            throw new ServletException(e);
        }
    }

    private String join(LibraryResources libraryResources, ResourceType resourceType, String blocksFolder) throws IOException {
        List<Resource> files = libraryResources.getFiles(resourceType);
        files.addAll(new Resources(blocksFolder).getResources(new ResourceExtensionFilter(resourceType)));
        return ResourceAppender.append(files).toString();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String path = ((HttpServletRequest) request).getRequestURI();
        String content = null;
        if (path.equals("/" + name + ".js")) {
            content = jsContent;
        } else if (path.equals("/" + name + ".css")) {
            content = cssContent;
        } else {
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
