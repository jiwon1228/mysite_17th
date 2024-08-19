package com.study.mysite.answer;

import java.time.LocalDateTime;

import com.study.mysite.question.Question;
import com.study.mysite.user.SiteUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Answer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(columnDefinition ="TEXT")
	private String content;
	
	private LocalDateTime createDate;
	
	@ManyToOne //하나의 질문에 여러가지 답변을 할 수 있다.
	private Question question; //외래키(FK) Answer:자식N Question:부모1 
	
	@ManyToOne
	private SiteUser author;
	//글쓴이의 아이디값
	
}
