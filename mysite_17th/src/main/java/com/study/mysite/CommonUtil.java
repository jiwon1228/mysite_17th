package com.study.mysite;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

@Component
public class CommonUtil {
    public String markdown(String markdown) { //텍스트를 받아서 HTML로 변환해주는 코드
        Parser parser = Parser.builder().build(); // 마크다운 텍스트를 파싱하기 위한 파서
        Node document = parser.parse(markdown); // 파싱된 문서를 Node 객체로 변환
        HtmlRenderer renderer = HtmlRenderer.builder().build(); // Node 객체를 HTML로 변환하기 위한 렌더러
        return renderer.render(document); // HTML로 변환된 결과를 반환
    }
}