package com.jukta.jtahoe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Sergey Sidorov
 */
public interface DataHandler {

    Attrs getData(Attrs attrs, HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse);

}
