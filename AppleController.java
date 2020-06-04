package org.csu.shopping.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppleController {

    @GetMapping("/catalog/category_apple")
    public String viewApple()
    {
        return "catalog/category_apple";
    }

}
