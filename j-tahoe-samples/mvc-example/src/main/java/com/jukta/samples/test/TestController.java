package com.jukta.samples.test;

import com.jukta.jtahoe.springmvc.aloha.AlohaController;
import com.jukta.jtahoe.springmvc.aloha.RemoteEvent;
import com.jukta.jtahoe.springmvc.aloha.RemoteMethod;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Sergey Sidorov
 */
@Controller
@AlohaController
public class TestController {

    @RequestMapping("/")
    public String index(HttpServletResponse response, Model model) {
        DataModel m = new DataModel("Overview", "JTahoe is a powerfull object oriented engine for html templates. The main aim is to provide ability to make big and extedable front-end projects with high reusability of html parts (blocks). JTahoe helps to develop blocks in object-oriented manner.");
        model.addAttribute("model", m);
        return "com.jukta.mvc.test.Main";
    }

    @RemoteMethod
    public String getMessage(String req, HttpServletRequest request, HttpServletResponse response) {
        return "Hello " + req + "!";
    }

    @RemoteEvent("test-event")
    public void event(DataModel dataModel) {

    }

    public static class DataModel {
        private String title;
        private String text;

        public DataModel(String title, String text) {
            this.title = title;
            this.text = text;
        }

        public String getTitle() {
            return title;
        }

        public String getText() {
            return text;
        }
    }

}
