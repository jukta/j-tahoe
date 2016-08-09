package com.jukta.jtahoe.taglib;

import com.jukta.jtahoe.Attrs;
import com.jukta.jtahoe.BlockFactory;
import com.jukta.jtahoe.DataHandlerProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class BlockTag extends BodyTagSupport {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int doStartTag() throws JspException {
        PageContext pageContext = this.pageContext;
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        DataHandlerProvider dataHandlerProvider = (DataHandlerProvider) pageContext.getServletContext().getAttribute("_jTahoe_dataHandlerProvider");

        Attrs attrs = new Attrs();
        attrs.setDataHandlerProvider(dataHandlerProvider);
        attrs.setAttribute("session", request.getSession());
        attrs.setAttribute("request", request);

        request.setAttribute("____attrs", attrs);

        return super.doStartTag();
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            PageContext pageContext = this.pageContext;
            HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
            BlockFactory blockFactory = (BlockFactory) pageContext.getServletContext().getAttribute("_jTahoe_blockFactory");
            String html = blockFactory.create(name).body((Attrs) request.getAttribute("____attrs")).toHtml();
            JspWriter out = pageContext.getOut();
            out.println(html);
        } catch (Exception e) {
            throw new JspException(e);
        }
        return EVAL_PAGE;
    }

}
