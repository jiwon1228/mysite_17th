package com.study.mysite.question;

import java.time.LocalDateTime;
import java.util.List;

import com.study.mysite.answer.Answer;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Question {
	@Id
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 100) //글자의 갯수를 100개로 지정
	private String subject;
	
	@Column(columnDefinition = "TEXT") //글자의 갯수를 무한대
	private String content;
	
	private LocalDateTime createDate;//DB에서는 crate_date로 만들어짐
	
	@OneToMany(mappedBy= "question", cascade = CascadeType.REMOVE) //부모(질문)가 삭제되면 자식(관련답변)도 삭제가 되겠다
	private List<Answer> answerList;
}
