package com.study.mysite;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.study.mysite.user.SiteUser;
import com.study.mysite.user.UserCreateForm;
import com.study.mysite.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MainController {
	
	private final UserService userService;
	
	@GetMapping("/study")
	@ResponseBody
	public String index() {
		return "킴쌤 클래스에 오신것을 환영합니다!!";
	}
	
	@GetMapping("/")
	public String root(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		//return "redirect:/question/list"
		if(userDetails != null) {
			SiteUser user = userService.getUser(userDetails.getUsername());
			model.addAttribute("profileImage",user.getImageUrl());
		}
		return "index";
	}
	@GetMapping("/boxoffice")
	public String movie() {
		
		return "movie_boxOffice";
	}
	
	
}