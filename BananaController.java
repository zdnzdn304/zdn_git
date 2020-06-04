package org.csu.shopping.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BananaController {

    @GetMapping("/catalog/category_banana")
    public String viewApple()
    {
        return "catalog/category_banana";
    }

}