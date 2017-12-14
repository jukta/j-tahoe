package com.jukta.samples.test;

import com.jukta.jtahoe.springmvc.aloha.AlohaController;
import com.jukta.jtahoe.springmvc.aloha.RemoteEvent;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Sergey on 10/26/2017.
 */
@Controller
@AlohaController
public class ButtonController {

    @RemoteEvent(value = "clk", responseEvent = "clkResponse")
    public String clk(HttpServletResponse response, HttpServletRequest request) {
        return "Hello from server!";
    }

}
