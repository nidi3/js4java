package stni.js4java.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import shared.Calc;
import shared.Validation;

/**
 *
 */
@Controller
public class MainController {
    @Autowired
    private Validation validation;
    @Autowired
    private Calc calc;

    @RequestMapping("/send")
    @ResponseBody
    public Response send(@RequestBody Model model) {
        return new Response(validation.email(model.getEmail()), calc.growthPerYear(model.getAge(), model.getHeight()));
    }
}
