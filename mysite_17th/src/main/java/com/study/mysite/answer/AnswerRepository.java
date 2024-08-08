package com.study.mysite.answer;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.mysite.question.Question;

public interface AnswerRepository extends JpaRepository<Answer, Integer>{

}
