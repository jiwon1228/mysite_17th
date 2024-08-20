package com.study.mysite.answer;

import java.security.Principal;
import java.text.Format;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.study.mysite.question.Question;
import com.study.mysite.question.QuestionService;
import com.study.mysite.user.SiteUser;
import com.study.mysite.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;


@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {
	private final QuestionService questionService;
	private final AnswerService answerService;
	private final UserService userService;
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{questionId}")
	public String createAnswer(Model model, @PathVariable("questionId") Integer id,
			@Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal ) {
		//현재 로그인한 사용자의 정보를 알려면 스프링 시큐리티가 제공하는 Principal이라고 하는 객체를 사용해야 한다.
		Question question = this.questionService.getQuestion(id);
		
		//사용자ID 가져오기
		SiteUser siteUser=this.userService.getUser(principal.getName());
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("question",question);
			return "question_detail";
		}
		Answer answer = this.answerService.create(question, answerForm.getContent(),siteUser);
		return String.format("redirect:/question/detail/%s#answer_%s", id,answer.getId());

	}

	public String getMethodName(@RequestParam String param) {
		return new String();
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String answerModify(AnswerForm answerForm,@PathVariable("id") Integer id, Principal principal) {
		Answer answer = this.answerService.getAnswer(id);
		if(!answer.getAuthor().getUsername().equals(principal.getName())){	
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정 권한이 없습니다.");
		}
		answerForm.setContent(answer.getContent());
		return "answer_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String answerModify(@Valid AnswerForm answerForm,BindingResult bindingResult,@PathVariable("id") Integer id, Principal principal) {
		if(bindingResult.hasErrors()) {
			return "answer_form";
		}
		
		Answer answer = this.answerService.getAnswer(id);
		if(!answer.getAuthor().getUsername().equals(principal.getName())){	
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정 권한이 없습니다.");
		}
		this.answerService.modify(answer, answerForm.getContent());
		
		return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(),answer.getId());
		//답변 수정 완료 후 질문 상세 페이지로 리다이렉트
	}
	
	@PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(@PathVariable("id") Integer id, Principal principal) {
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.answerService.delete(answer);
        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
    }
	
	//좋아요 클릭 시 
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vote/{id}")
	public String answerVote(Principal principal, @PathVariable("id") Integer id) {
		Answer answer = this.answerService.getAnswer(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		
		this.answerService.vote(answer, siteUser);
		
		return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(),answer.getId());
	}

}
