package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.resource.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Sergey Sidorov
 */
public class CssInterceptor extends HandlerInterceptorAdapter implements InitializingBean {

    private String blocksFolder = "blocks";
    private String content;
    private String contentType = "text/css";

    public void setBlocksFolder(String blocksFolder) {
        this.blocksFolder = blocksFolder;
    }

    protected String getContentType() {
        return contentType;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setContentType(getContentType());
        response.getWriter().write(content);
        return false;
    }

    protected ResourceType getFilter() {
        return ResourceType.CSS;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LibraryResources lr = new LibraryResources();
        List<Resource> files = lr.getFiles(getFilter());
        files.addAll(new Resources(blocksFolder).getResources(new ResourceExtensionFilter(getFilter())));
        StringBuilder sb = ResourceAppender.append(files);
        content = sb.toString();
    }
}
