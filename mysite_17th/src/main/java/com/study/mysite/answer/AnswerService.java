package com.study.mysite.answer;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.study.mysite.DataNotFoundException;
import com.study.mysite.question.Question;
import com.study.mysite.user.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnswerService {
	private final AnswerRepository answerRepository;
	
	public Answer create(Question question, String content,SiteUser author) {
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setCreateDate(LocalDateTime.now());
		answer.setQuestion(question);
		answer.setAuthor(author);
		this.answerRepository.save(answer);
		
		return answer;
		
	}
	//답변조회
	public Answer getAnswer(Integer id) {
		Optional<Answer> answer = this.answerRepository.findById(id);
		
		if(answer.isPresent()) {
			return answer.get();
		}else {
			throw new DataNotFoundException("답변이 없습니다.");
		}
	}
	//답변수정
	public void modify(Answer answer, String content) {
		answer.setContent(content);
		answer.setModifyDate(LocalDateTime.now());
		this.answerRepository.save(answer);
	}
	
	//답변 삭제
	public void delete(Answer answer) {
		this.answerRepository.delete(answer);
	}
	
	public void vote(Answer answer,SiteUser siteUser) {
		answer.getVoter().add(siteUser);//질문 추천한 사람을 가져오기
		this.answerRepository.save(answer);
	}
}
