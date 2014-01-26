package stni.js4java.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import shared.Calc;
import shared.Validation;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 */
@Controller
public class MainController {
    @Autowired
    private Validation validation;
    @Autowired
    private Calc calc;
    @Autowired
    private MessageSource messageSource;

    @RequestMapping("/send")
    @ResponseBody
    public Response send(@RequestBody @Valid Model model, BindingResult result) {
        List<String> messages = new ArrayList<>();
        for (ObjectError error : result.getAllErrors()) {
            messages.add(messageSource.getMessage(error, Locale.ENGLISH));
        }
        return new Response(messages, validation.isValidEmail(model.getEmail()), calc.growthPerYear(model.getAge(), model.getHeight()));
    }
}
