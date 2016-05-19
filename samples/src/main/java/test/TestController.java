package test;

import com.jukta.jtahoe.Attrs;
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

    public static void main(String[] args) {
        Attrs attrs = new Attrs();
        attrs.set("message", "Hello World!");
        String[] arr = new String[] {"arr1", "arr2"};
        attrs.set("name", "my name");
        attrs.set("arr", arr);
        attrs.set("checked", true);

//        Main m = new Main();
//        System.out.println(m.body(attrs).toHtml());
    }

}
