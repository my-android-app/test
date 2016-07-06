package com.shoushuzhitongche.app.view.question.bean;

import java.util.List;

public class QuestionEntity {
	public static final String TEXT_TYPE = "1";//文本框
	public static final String SINGLE_TYPE = "2";//单选
	public static final String MULTI_TYPE = "3";//多选
	public static final String SECTION_TYPE = "4";//区间
	
	private String num;
	private String question;
	private String type;
	private List<QuestionChoiceItemEntity> choice;
	private String checkInput;
	private String attach;//题目说明（例如可多选）
	private String warning;//（例如提交时没有填写是的错误提示）
	private String units;//区间问题单位如多少元
	
	private int columnNum;
	
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<QuestionChoiceItemEntity> getChoice() {
		return choice;
	}
	public void setChoice(List<QuestionChoiceItemEntity> choice) {
		this.choice = choice;
	}
	public String getCheckInput() {
		return checkInput;
	}
	public void setCheckInput(String checkInput) {
		this.checkInput = checkInput;
	}
	public int getColumnNum() {
		return columnNum;
	}
	public void setColumnNum(int columnNum) {
		this.columnNum = columnNum;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getWarning() {
		return warning;
	}
	public void setWarning(String warning) {
		this.warning = warning;
	}
	
	
}
