package com.study.mysite;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
	@GetMapping("/study")
	@ResponseBody
	public void Index() {
		System.out.println("킴쌤 클래스에 오신 것을 환영합니다!");
	}
}
