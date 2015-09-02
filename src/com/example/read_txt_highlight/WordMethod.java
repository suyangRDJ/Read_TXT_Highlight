package com.example.read_txt_highlight;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordMethod {
	public static final int UNIT = 0;
	public static final int LESSON = 1;
	public static final int CONTENT = 2;

	public TxtMatch regularGroup(String patternUnit, String patternLesson,
			String matcher) {
		TxtMatch txtMatch = new TxtMatch();
		Pattern p = Pattern.compile(patternUnit, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(matcher);
		if (m.find()) { // 如果读到
			txtMatch.setMatch(UNIT);
			txtMatch.setContent(m.group());
			return txtMatch;// 返回捕获的数据
		} else {
			Pattern pL = Pattern.compile(patternLesson,
					Pattern.CASE_INSENSITIVE);
			Matcher mL = pL.matcher(matcher);
			if (mL.find()) {
				txtMatch.setMatch(LESSON);
				txtMatch.setContent(mL.group());
				return txtMatch;// 返回捕获的数据
			} else {
				txtMatch.setMatch(CONTENT);
				txtMatch.setContent(matcher);
				return txtMatch;
			}
		}
	}
}
