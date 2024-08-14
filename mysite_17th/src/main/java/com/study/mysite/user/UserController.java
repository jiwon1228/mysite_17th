package com.study.mysite.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/user")
@RequiredArgsConstructor
@Controller
public class UserController {
	private final UserService userService;

	@GetMapping("/signup")
	public String signup(UserCreateForm userCreateForm) {
		return "signup_form";
	}

	@PostMapping("/signup")
	public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {

		if(bindingResult.hasErrors()) {
			return "signup_form";
		}

		if(!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
			bindingResult.rejectValue("password2", "passwordInCorrect","2개의 패스워드가 서로 일치하지 않습니다.");
			//rejectValue(필드명, 오류 코드, 오류 메세지)
			return "signup_form";
		}
		
		try {
			userService.create(userCreateForm.getUsername(), 			userCreateForm.getEmail(), userCreateForm.getPassword1());

		}catch(DataIntegrityViolationException e) {
			//DataIntegrityViolationException : unique으로 설정한 값에 같은 데이터가 들어갈 때 발생하는 예외 클래스
			e.printStackTrace();
			bindingResult.reject("signupFailed","이미 등록된 사용자입니다.");
			//reject(오류 코드,오류 메세지)
			return "signup_form";
		}catch(Exception e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed",e.getMessage());
			return "signup_form";
		}
		


		return "redirect:/";
	}

	@GetMapping("/login")
	public String login() {
		return "login_form";
	}
//	@PostMapping("/login") public String login() {
//	
//	이 부분은 security가 실행해서 우리가 구현할 필요가 없다
}
