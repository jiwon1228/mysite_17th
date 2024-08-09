package com.study.mysite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.study.mysite.answer.Answer;
import com.study.mysite.answer.AnswerRepository;
import com.study.mysite.question.Question;
import com.study.mysite.question.QuestionRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
class Mysite17thApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;
	//테스트 코드에서는 생성자를 통한 객체 주입방식을 지원하지않아서 @autowried를사용했지만 순환참조 이슈가 발생 할 수 있으므로 
	//실제코드에서는 setter나 생성자 주입방식을 사용하는것을 권장한다. 
	
	@Autowired
	private AnswerRepository answerRepository;
	
	@Transactional
	@Test
	void testJpa(){
		/*
		Question q1 = new Question();
		q1.setSubject("JPA가 무엇인가요?");
		q1.setContent("알고 싶어요");
		q1.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q1); //첫번째 질문 저장
		
		Question q2 = new Question();
		q2.setSubject("ORM이 무엇인가요?");
		q2.setContent("궁금합니다");
		q2.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q2); //두번째 질문 저정
		
		Question q3 = new Question();
		q3.setSubject("hibernate란 무엇인가요?");
		q3.setContent("궁금합니다");
		q3.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q3); //두번째 질문 저장
		*/
		/*
		List<Question> all = this.questionRepository.findAll();
		assertEquals(3, all.size());
		
		Question q = all.get(0);
		assertEquals("JPA가 무엇인가요?", q.getSubject());
		Question q = this.questionRepository.findBySubject("JAP가 무엇인가요?");
		assertEquals(3,q.getId());
		
		Question q = this.questionRepository.findBySubjectAndContent("ORM이 무엇인가요?","궁금합니다");
		assertEquals(3, q.getId());
		
		//데이터찾기
		List<Question> qList = this.questionRepository.findBySubjectLike("%무엇%");
		this.questionRepository.findAll();
		
		//데이터 수정하기
		Optional<Question> oq =this.questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q=oq.get();
		q.setSubject("무엇이든 궁금하면 물어보세요");
		this.questionRepository.save(q);
		
		
		//데이터 삭제하기
		assertEquals(4,this.questionRepository.count());
		Optional<Question> oq =this.questionRepository.findById(2);
		assertTrue(oq.isPresent()); //해당하는 객체가 있으면 트루를 반환
		Question q=oq.get();
		this.questionRepository.delete(q);
		assertEquals(3,this.questionRepository.count());
		
		//-----------------------------------------------
		 
		//answer 질문에 대답하기
		Optional<Question> oq =this.questionRepository.findById(3);
		assertTrue(oq.isPresent());
		Question q =oq.get();
		
		Answer a= new Answer();
		a.setContent("Object-Relational Mapping의 약자");
		a.setQuestion(q); //어떤 질문에 답변인지 설정해야하기때문에 question이란 객체가 필요함
		a.setCreateDate(LocalDateTime.now());
		this.answerRepository.save(a);*/
		
		
		//답변 데이터를 통해서 
		Optional<Question> oq =this.questionRepository.findById(3);
		assertTrue(oq.isPresent());
		Question q =oq.get();
		
		List<Answer> answerList = q.getAnswerList();
		//테스트 코드를 통과하지 못한 이유는 answerLists는 q객체를 조회할때가 아니라 getAnswerList()메소드를 호출하는 시점에 가져오기때문에 이렇게 데이터를 필요한 시점에 가져오는 방식을 지연 방식이라고한다
		//위와 반대인 방식을 즉시방식이라고한다 q객체를 조회할때 answer리스트를 모두 가져오는 방식을 즉시(eager)방식이라고한다.
		//테스트 코드 시 이런 오류를 방지할 수 있는 가장 간단한 방법으로 @Transactionnal애너테이션을 사용하여 DB세션이 끊기지않고 계속 유지가 되어 해결
		assertEquals(1, answerList.size());
		assertEquals("Object-Relational Mapping의 약자", answerList.get(0).getContent());
	}

}
