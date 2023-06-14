package com.Application.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @RequestMapping({ "/validate" })
    public String firstPage() {
        return "Welcome User";
    }
}
