package test;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Sergey Sidorov
 */
@Controller
public class TestController {

    @RequestMapping("/hello")
    public String helloWorld(Model model) {
        model.addAttribute("message", "Hello World!");
        return "test.Main";
    }

}
