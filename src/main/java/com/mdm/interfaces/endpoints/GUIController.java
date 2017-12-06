package com.mdm.interfaces.endpoints;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//@Controller
public class GUIController {
	
//	@RequestMapping("/gui")
    public String greeting() {
		System.out.println("=============GREETING");
        return "index";
    }

}
