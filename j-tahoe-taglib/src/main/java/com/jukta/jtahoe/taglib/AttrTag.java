package com.jukta.jtahoe.taglib;

import com.jukta.jtahoe.Attrs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class AttrTag extends SimpleTagSupport {
    private String name;
    private Object value;

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void doTag() throws JspException, IOException {
        try {
            PageContext pageContext = (PageContext) getJspContext();
            HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

            Attrs attrs = (Attrs) request.getAttribute("____attrs");
            attrs.set(name, value);

        } catch (Exception e) {
            throw new JspException(e);
        }
    }
}
