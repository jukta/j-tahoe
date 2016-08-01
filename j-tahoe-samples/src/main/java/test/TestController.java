package test;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

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

    @RequestMapping("/index")
    public String index(Model model) {
        List<TradeModel> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            TradeModel m = new TradeModel();
            m.setAccount("ACC16" + i);
            m.setSide("BUY");
            m.setQuantity(20+i);
            m.setPrice("838.675");
            m.setPriceType("Average");
            m.setProduct("2CD");
            m.setExchange("NYMEX");
            m.setProductType("FUT");
            m.setOrder("");
            m.setExecBroker("DPT");
            m.setStatus("DPT");
            m.setType("ALLEGED");
            list.add(m);
        }
        model.addAttribute("list", list);
        return "pages.IndexPage";
    }

}
