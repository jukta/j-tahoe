package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.Attrs;
import com.jukta.jtahoe.Block;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Sergey Sidorov
 */
public class JTahoeView implements View {
    private String contentType = "text/html;charset=ISO-8859-1";

    private String viewName;

    public JTahoeView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public void render(Map<String, ?> map, HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse) throws Exception {
        Block block = (Block) Class.forName(viewName).newInstance();
        String s = block.body(new Attrs().set("x", 123)).toHtml();
        httpservletresponse.getWriter().write(s);
    }
}
