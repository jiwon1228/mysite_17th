package com.study.mysite.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.study.mysite.DataNotFoundException;
import com.study.mysite.answer.Answer;
import com.study.mysite.user.SiteUser;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    public Question getQuestion(Integer id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    public void create(String subject, String content, SiteUser user) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(user);
        this.questionRepository.save(q);
    }

    public Page<Question> getList(int page,String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate")); // 최신순으로 정렬
        Pageable pageable = PageRequest.of(page, 5, Sort.by(sorts));
        Specification<Question> spec=search(kw);
        return this.questionRepository.findAll(pageable);
    }

    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }

    public void delete(Question question) {
        this.questionRepository.delete(question);
    }

    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser); // 질문 추천한 사람 추가
        this.questionRepository.save(question);
    }

    // 검색 기능 추가
    private Specification<Question> search(String kw) {
        return new Specification<Question>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true); // 중복 제거
                ///toPredicate()는 pecification이 JPA API 쿼리로 변환될 때 PRedicate 객체를 반환하여 쿼리의 조건을 정의한다.
                //PRedicate 객체는 쿼리의 where 조건식을 나타낸다.
    			//CriteriaBuilder는 복잡한 쿼리의 다양한 구성요소를 생성하는데 사용한다.
                //Root: 쿼리 기준이 되는 엔티티를 정의한다.
                //CriteriaQuery는 쿼리 기본 구조를 만들 때 사용 CriteriaQuery<T>
                //CriteriaBuilder는 복잡한 쿼리의 다양한 구성요소를 생성하는데 사용한다.

                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);

                return cb.or(
                        cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"), // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"), // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"), // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%") // 답변 작성자
                );
            }
        };
    }
}
