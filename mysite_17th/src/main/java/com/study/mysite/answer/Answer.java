package com.study.mysite.answer;

import java.time.LocalDateTime;
import java.util.Set;

import com.study.mysite.question.Question;
import com.study.mysite.user.SiteUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
	
	private LocalDateTime modifyDate;//질문 수정 일시
	
	@ManyToMany
	Set<SiteUser> voter; //다대다 관계 (하나의 질문에 여러명이 좋아요를 클릭가능하다. 한명의 유저는 여러 질문에 좋아요를 클릭 가능하다.
	//하나의 질문에 좋아요를 클릭하면 중복클릭이 안된다.)
	
}
