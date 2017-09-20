package com.gmsboilerplatesbng.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController implements BaseController{

    @RequestMapping("/index")
    public String index(){
        return "GMS - Boilerplate";
    }

}
