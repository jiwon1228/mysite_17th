package com.study.mysite.question;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

@RequestMapping("/question")//들어오는 요청은 /question로 시작한다
@RequiredArgsConstructor
@Controller
public class QuestionController {
	
	//private final QuestionRepository questionRepository;
	private final QuestionService questionService;
	
	
	//게시판 리스트로 이동
	//localhost:8080/question/list 이 요청이 오면 게시판 목록 페이지가 떠야 한다.
	@GetMapping("/list")
	public String list(Model model) {
		//List<Question> questionList = this.questionRepository.findAll();
		List<Question> questionList=this.questionService.getList(); //결과는 같으나 서비스를 이용해 유지보수가 편하게 수정
		model.addAttribute("questionList",questionList);
		
		return "question_list";
		//자바 코드를 삽입할 수 있는 html 형식의 파일인 템플릿이 필요=>타임 리프 Thymeleaf 사용예쩡
	}
	
	//상세페이지 이동
	@GetMapping(value="/detail/{userid}")
	public String detail(Model model, @PathVariable("userid") Integer id) { 
		//Integer type의 id 컬럼값과 연결하여 @PathVariable("변수명")으로 변경한다. 
		//=> 사용자 요청 url이 변수명으로 사용가능하다 
		Question question = this.questionService.getQuestion(id);
		model.addAttribute("question",question);
		return "question_detail";
	}
}
