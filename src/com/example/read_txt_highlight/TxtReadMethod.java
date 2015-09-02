package com.example.read_txt_highlight;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;

@SuppressLint("NewApi")
public class TxtReadMethod {
	public static final int UNIT=0;
	public static final int LESSON=1;
	public static final int CONTENT=2;

	/**
	 * 通过一个InputStream获取内容
	 * 
	 * @param inputStream
	 * @return
	 */
	public static ArrayList<UnitData> getString(InputStream inputStream) {
		ArrayList<UnitData> unitDataList=new ArrayList<UnitData>();
		InputStreamReader inputStreamReader = null;
		try {
			inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(inputStreamReader);
		StringBuffer sb = new StringBuffer("");
		StringBuffer titleSb = new StringBuffer("");
		String line;

		String regularUnit = "Unit\\s*\\d";
		String regularLesson = "Lesson\\s*\\d";
		int unitId = 0;
		int lessonId = 0;
		boolean upLesson = false;
		boolean firstContent = false;
		
		int matchId;
		String matchContent;
		UnitData unitData=new UnitData();
		ArrayList<Article> articleList=new ArrayList<Article>();
		Article article=new Article();
		try {
			
			while ((line = reader.readLine()) != null) {

				TxtMatch matchGet = regularGroup(regularUnit,
						regularLesson, line);
				matchId = matchGet.getMatch();
				matchContent=matchGet.getContent();
				
				switch (matchId) {
				case UNIT:
					
					if(unitId!=0){
						unitData.setArticle(articleList);
						unitDataList.add(unitData);
						articleList=new ArrayList<Article>();
					}
					unitId++;
					
					unitData=new UnitData();
					unitData.setUnit(unitId);

					break;
				case LESSON:
					
					if(lessonId!=0){
						article.setContent(sb.toString());
						articleList.add(article);
						sb.delete(0,sb.length()-1);
					}
					lessonId++;
					//创建新的Article
					article=new Article();
					upLesson=true;
					article.setLessonId(lessonId);
					break;
				case CONTENT:
					//赋值给标题或者正文
					if(upLesson&&firstContent){
						titleSb.append(" ");
						titleSb.append(matchContent.replaceAll(" ",""));
						article.setTitle(titleSb.toString());
						titleSb.delete(0,titleSb.length());
						upLesson=false;
						firstContent=false;
					}else if(upLesson){
						titleSb.append(matchContent.trim());
						firstContent=true;
					}else{
						sb.append(matchContent);
						sb.append("\n");
						upLesson=false;
					}
					
					break;

				default:
					break;
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		unitData.setArticle(articleList);
		unitDataList.add(unitData);
		return unitDataList;
	}
	
	//解析
	private static TxtMatch regularGroup(String patternUnit,String patternLesson, String matcher) {
		TxtMatch txtMatch =new TxtMatch();
		Pattern p = Pattern.compile(patternUnit, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(matcher);
		if (m.find()) { // 如果读到
			txtMatch.setMatch(UNIT);
			txtMatch.setContent(m.group());
			return txtMatch;// 返回捕获的数据
		}else{
			Pattern pL = Pattern.compile(patternLesson, Pattern.CASE_INSENSITIVE);
			Matcher mL = pL.matcher(matcher);
			if(mL.find()){
				txtMatch.setMatch(LESSON);
				txtMatch.setContent(mL.group());
				return txtMatch;// 返回捕获的数据
			}else{
				txtMatch.setMatch(CONTENT);
				txtMatch.setContent(matcher);
				return txtMatch;
			}
		}
	}

}
