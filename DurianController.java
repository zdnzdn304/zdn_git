package org.csu.shopping.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DurianController {

    @GetMapping("/catalog/category_durian")
    public String viewApple()
    {
        return "catalog/category_durian";
    }

}