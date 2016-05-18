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
        String[] arr = new String[] {"arr1", "arr2"};
        model.addAttribute("name", "my name");
        model.addAttribute("arr", arr);
        model.addAttribute("checked", true);
        return "test.Main";
    }

}
