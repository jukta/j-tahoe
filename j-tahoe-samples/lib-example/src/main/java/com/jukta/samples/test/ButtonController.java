package com.jukta.samples.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Sergey on 10/26/2017.
 */
@Controller
public class ButtonController {

    @ResponseBody
    @RequestMapping(value = "/__event/clk",method = RequestMethod.POST)
    public String clk(HttpServletResponse response) {
        response.setHeader("response-type", "clkResponse");
        return "Hello from server!";
    }

}
