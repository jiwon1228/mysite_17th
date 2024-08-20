package com.study.mysite;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

@Component
public class CommonUtil {
	public String markdown(String markdown) { //텍스트를 받아서 html로 변환해주는 코드 만들어주기
		Parser parser = Parser.builder().build();
		Node document = parser.parse(markdown);
		HtmlRenderer renderer= HtmlRenderer.builder().build(); //텍스트를 웹페이지처럼 만들어줌
		return renderer.render(document);
	}
}
