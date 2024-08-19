package com.study.mysite.question;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.study.mysite.answer.AnswerForm;
import com.study.mysite.user.SiteUser;
import com.study.mysite.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/question")//들어오는 요청은 /question로 시작한다
@RequiredArgsConstructor
@Controller
public class QuestionController {
	
	//private final QuestionRepository questionRepository;
	private final QuestionService questionService;
	private final UserService userService;
	
	
	//게시판 리스트로 이동
	//localhost:8080/question/list 이 요청이 오면 게시판 목록 페이지가 떠야 한다.
	@GetMapping("/list")
	public String list(Model model,@RequestParam(value="page",defaultValue="0") int page) {
		//List<Question> questionList = this.questionRepository.findAll();
		//.List<Question> questionList=this.questionService.getList(); //결과는 같으나 서비스를 이용해 유지보수가 편하게 수정
		
		Page<Question> paging = this.questionService.getList(page);
		//model.addAttribute("questionList",questionList);
		model.addAttribute("paging",paging);
		
		return "question_list";
		//자바 코드를 삽입할 수 있는 html 형식의 파일인 템플릿이 필요=>타임 리프 Thymeleaf 사용예쩡
	}
	
	//상세페이지 이동
	@GetMapping(value="/detail/{userid}")
	public String detail(Model model, @PathVariable("userid") Integer id, AnswerForm answerForm) { 
		//Integer type의 id 컬럼값과 연결하여 @PathVariable("변수명")으로 변경한다. 
		//=> 사용자 요청 url이 변수명으로 사용가능하다 
		Question question = this.questionService.getQuestion(id);
		model.addAttribute("question",question);
		return "question_detail";
	}
	
	//질문 등록 페이지로 이동
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String questionCreate(QuestionForm questionForm) {
		return "question_form"; //파일로 이동하게됨
	}
	
//	public String questionCreate(@RequestParam(value="subject") String subject,@RequestParam(value="content") String content) { //매개방식을 다르게 줄거라서 같은 메소드 이름을 줄 수 있음
//		this.questionService.create(subject, content);
//		return	"redirect:/question/list";
//	}
	
	
	@PreAuthorize("isAuthenticated()")
	//isAuthenticated() : 인증 / 사용자가 누구인지를 확인 하는 것
	//Authorize : 인가 / 인증된 사용자가 특정 자원에 접근할 권한이 있는지 확인
	@PostMapping("/create")
	public String questionCreate(@Valid QuestionForm questionForm,BindingResult bindingResult,Principal principal) { //매개방식을 다르게 줄거라서 같은 메소드 이름을 줄 수 있음
		if(bindingResult.hasErrors()) {
			return "question_form";
		}
		SiteUser siteUser=this.userService.getUser(principal.getName());
		this.questionService.create(questionForm.getSubject(),questionForm.getContent(),siteUser);
		return	"redirect:/question/list";
		
	}
}
