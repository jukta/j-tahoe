package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.Resources;
import com.jukta.jtahoe.file.JTahoeXml;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import sun.misc.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FilenameFilter;
import java.util.List;

/**
 * @author Sergey Sidorov
 */
public class CssInterceptor extends HandlerInterceptorAdapter implements InitializingBean {

    private String blocksFolder = "blocks";
    private String content;

    public void setBlocksFolder(String blocksFolder) {
        this.blocksFolder = blocksFolder;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.getWriter().write(content);
        return false;
    }

    protected FilenameFilter getFilter() {
        return new Resources.ExtensionFilter("css");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<JTahoeXml> files = new Resources(blocksFolder).getFiles(getFilter());
        StringBuilder sb = new StringBuilder();
        for (JTahoeXml f : files) {
            String cont = new String(IOUtils.readFully(f.getInputSource().getByteStream(), -1, false));
            sb.append(cont);
        }
        content = sb.toString();
    }
}
