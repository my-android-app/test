package com.shoushuzhitongche.app.view.question.bean;

import java.util.ArrayList;
import java.util.List;

public class QuestionDateEntity {
	private List<QuestionEntity> questionGoOutList;
	private List<QuestionEntity> questionReceiveComeList;

	
	public List<QuestionEntity> getQuestionGoOutList() {
		questionGoOutList = new ArrayList<QuestionEntity>();
		
		QuestionEntity questionEntity0 = new QuestionEntity();
		questionEntity0.setType("2");
		questionEntity0.setNum("1");
		questionEntity0.setQuestion("几台手术您愿意外出会诊？");
		
		List<QuestionChoiceItemEntity> choice0 = new ArrayList<QuestionChoiceItemEntity>();
		QuestionChoiceItemEntity questionChoiceItemEntity00 = new QuestionChoiceItemEntity();
		questionChoiceItemEntity00.setChoiceId(0);
		questionChoiceItemEntity00.setContent("1台");
		questionChoiceItemEntity00.setValue("1");
		choice0.add(questionChoiceItemEntity00);
		
		QuestionChoiceItemEntity questionChoiceItemEntity01 = new QuestionChoiceItemEntity();
		questionChoiceItemEntity01.setChoiceId(1);
		questionChoiceItemEntity01.setContent("2台");
		questionChoiceItemEntity01.setValue("2");
		choice0.add(questionChoiceItemEntity01);
		
		QuestionChoiceItemEntity questionChoiceItemEntity02 = new QuestionChoiceItemEntity();
		questionChoiceItemEntity02.setChoiceId(2);
		questionChoiceItemEntity02.setContent("3台");
		questionChoiceItemEntity02.setValue("3");
		choice0.add(questionChoiceItemEntity02);
		
		questionEntity0.setChoice(choice0);
		
		QuestionEntity questionEntity1 = new QuestionEntity();
		questionEntity1.setType("4");
		questionEntity1.setNum("2");
		questionEntity1.setQuestion("每台手术费多少？");
		
		List<QuestionChoiceItemEntity> choice1 = new ArrayList<QuestionChoiceItemEntity>();
		QuestionChoiceItemEntity questionChoiceItemEntity10 = new QuestionChoiceItemEntity();
		questionChoiceItemEntity10.setContent("最低额度");
		choice1.add(questionChoiceItemEntity10);
		
		QuestionChoiceItemEntity questionChoiceItemEntity11 = new QuestionChoiceItemEntity();
		questionChoiceItemEntity11.setContent("最高额度");
		choice1.add(questionChoiceItemEntity11);
		
		questionEntity1.setChoice(choice1);
		
		
		QuestionEntity questionEntity2 = new QuestionEntity();
		questionEntity2.setType("3");
		questionEntity2.setNum("3");
		questionEntity2.setQuestion("时间成本控制要求？");
		questionEntity2.setAttach("可多选");
		
		List<QuestionChoiceItemEntity> choice2 = new ArrayList<QuestionChoiceItemEntity>();
		QuestionChoiceItemEntity questionChoiceItemEntity20 = new QuestionChoiceItemEntity();
		questionChoiceItemEntity20.setChoiceId(0);
		questionChoiceItemEntity20.setContent("高铁3小时内");
		questionChoiceItemEntity20.setValue("train3h");
		choice2.add(questionChoiceItemEntity20);
		
		
		
		QuestionChoiceItemEntity questionChoiceItemEntity21 = new QuestionChoiceItemEntity();
		questionChoiceItemEntity21.setChoiceId(2);
		questionChoiceItemEntity21.setContent("高铁5小时内");
		questionChoiceItemEntity21.setValue("train5h");
		choice2.add(questionChoiceItemEntity21);
		
		QuestionChoiceItemEntity questionChoiceItemEntity22 = new QuestionChoiceItemEntity();
		questionChoiceItemEntity22.setChoiceId(1);
		questionChoiceItemEntity22.setContent("飞机2小时内");
		questionChoiceItemEntity22.setValue("plane2h");
		choice2.add(questionChoiceItemEntity21);
		
		QuestionChoiceItemEntity questionChoiceItemEntity23 = new QuestionChoiceItemEntity();
		questionChoiceItemEntity23.setChoiceId(3);
		questionChoiceItemEntity23.setContent("飞机3小时内");
		questionChoiceItemEntity23.setValue("plane3h");
		choice2.add(questionChoiceItemEntity23);
		
		QuestionChoiceItemEntity questionChoiceItemEntity24 = new QuestionChoiceItemEntity();
		questionChoiceItemEntity24.setChoiceId(3);
		questionChoiceItemEntity24.setContent("无");
		questionChoiceItemEntity24.setValue("none");
		choice2.add(questionChoiceItemEntity24);
		
		questionEntity2.setChoice(choice2);
		
		QuestionEntity questionEntity3 = new QuestionEntity();
		questionEntity3.setType("3");
		questionEntity3.setNum("4");
		questionEntity3.setQuestion("一般周几方便外出会诊？");
		questionEntity3.setAttach("可多选");
		
		List<QuestionChoiceItemEntity> choice3 = new ArrayList<QuestionChoiceItemEntity>();
		QuestionChoiceItemEntity questionChoiceItemEntity30 = new QuestionChoiceItemEntity();
		questionChoiceItemEntity30.setChoiceId(0);
		questionChoiceItemEntity30.setContent("周一");
		questionChoiceItemEntity30.setValue("1");
		choice3.add(questionChoiceItemEntity30);
		
		QuestionChoiceItemEntity questionChoiceItemEntity31 = new QuestionChoiceItemEntity();
		questionChoiceItemEntity31.setChoiceId(1);
		questionChoiceItemEntity31.setContent("周二");
		questionChoiceItemEntity31.setValue("2");
		choice3.add(questionChoiceItemEntity31);
		
		QuestionChoiceItemEntity questionChoiceItemEntity32 = new QuestionChoiceItemEntity();
		questionChoiceItemEntity32.setChoiceId(2);
		questionChoiceItemEntity32.setContent("周三");
		questionChoiceItemEntity32.setValue("3");
		choice3.add(questionChoiceItemEntity32);
		
		QuestionChoiceItemEntity questionChoiceItemEntity33 = new QuestionChoiceItemEntity();
		questionChoiceItemEntity33.setChoiceId(3);
		questionChoiceItemEntity33.setContent("周四");
		questionChoiceItemEntity33.setValue("4");
		choice3.add(questionChoiceItemEntity33);
		
		QuestionChoiceItemEntity questionChoiceItemEntity34 = new QuestionChoiceItemEntity();
		questionChoiceItemEntity34.setChoiceId(4);
		questionChoiceItemEntity34.setContent("周五");
		questionChoiceItemEntity34.setValue("5");
		choice3.add(questionChoiceItemEntity34);
		
		QuestionChoiceItemEntity questionChoiceItemEntity35 = new QuestionChoiceItemEntity();
		questionChoiceItemEntity35.setChoiceId(5);
		questionChoiceItemEntity35.setContent("周六");
		questionChoiceItemEntity35.setValue("6");
		choice3.add(questionChoiceItemEntity35);
		
		QuestionChoiceItemEntity questionChoiceItemEntity36 = new QuestionChoiceItemEntity();
		questionChoiceItemEntity36.setChoiceId(6);
		questionChoiceItemEntity36.setContent("周日");
		questionChoiceItemEntity36.setValue("7");
		choice3.add(questionChoiceItemEntity36);
		
		questionEntity3.setChoice(choice3);
		
		
		QuestionEntity questionEntity4 = new QuestionEntity();
		questionEntity4.setType("1");
		questionEntity4.setNum("5");
		questionEntity4.setQuestion("您希望会诊什么样的病人？");
		
		List<QuestionChoiceItemEntity> choice4 = new ArrayList<QuestionChoiceItemEntity>();
		QuestionChoiceItemEntity questionChoiceItemEntity40 = new QuestionChoiceItemEntity();
		questionChoiceItemEntity40.setContent("如无，可填“无特殊要求”");
		choice4.add(questionChoiceItemEntity40);
		
		questionEntity4.setChoice(choice4);
		
		
		QuestionEntity questionEntity5 = new QuestionEntity();
		questionEntity5.setType("1");
		questionEntity5.setNum("6");
		questionEntity5.setQuestion("您常去哪些地区或医院会诊？");
		
		List<QuestionChoiceItemEntity> choice5 = new ArrayList<QuestionChoiceItemEntity>();
		QuestionChoiceItemEntity questionChoiceItemEntity50 = new QuestionChoiceItemEntity();
		questionChoiceItemEntity50.setContent("");
		choice5.add(questionChoiceItemEntity50);
		
		questionEntity5.setChoice(choice5);
		
		
		
		QuestionEntity questionEntity6 = new QuestionEntity();
		questionEntity6.setType("1");
		questionEntity6.setNum("7");
		questionEntity6.setQuestion("对手术地点/条件有何要求？");
		
		List<QuestionChoiceItemEntity> choice6 = new ArrayList<QuestionChoiceItemEntity>();
		QuestionChoiceItemEntity questionChoiceItemEntity60 = new QuestionChoiceItemEntity();
		questionChoiceItemEntity60.setContent("医院规模是否三甲/二甲、既往手术量、设备条件、手术室条件等等。");
		choice6.add(questionChoiceItemEntity60);
		
		questionEntity6.setChoice(choice6);
		
		questionGoOutList.add(questionEntity0);
		questionGoOutList.add(questionEntity1);
		questionGoOutList.add(questionEntity2);
		questionGoOutList.add(questionEntity3);
		questionGoOutList.add(questionEntity4);
		questionGoOutList.add(questionEntity5);
		questionGoOutList.add(questionEntity6);
		
		return questionGoOutList;
	}
	
	
	
	
	public void setQuestionGoOutList(List<QuestionEntity> questionGoOutList) {
		this.questionGoOutList = questionGoOutList;
	}
	public List<QuestionEntity> getQuestionReceiveComeList() {
		questionReceiveComeList = new ArrayList<QuestionEntity>();
		
		QuestionEntity questionEntity0 = new QuestionEntity();
		questionEntity0.setType("1");
		questionEntity0.setNum("1");
		questionEntity0.setQuestion("对转诊病例有何要求？");
		
		List<QuestionChoiceItemEntity> choice0 = new ArrayList<QuestionChoiceItemEntity>();
		QuestionChoiceItemEntity questionChoiceItemEntity00 = new QuestionChoiceItemEntity();
		questionChoiceItemEntity00.setContent("如无，可填“无特殊要求”");
		choice0.add(questionChoiceItemEntity00);
		
		questionEntity0.setChoice(choice0);
		
		
		
		QuestionEntity questionEntity1 = new QuestionEntity();
		questionEntity1.setType("2");
		questionEntity1.setNum("2");
		questionEntity1.setQuestion("是否需要转诊费？");
		questionEntity1.setColumnNum(3);
		
		List<QuestionChoiceItemEntity> choice1 = new ArrayList<QuestionChoiceItemEntity>();
		QuestionChoiceItemEntity questionChoiceItemEntity10 = new QuestionChoiceItemEntity();
		questionChoiceItemEntity10.setChoiceId(0);
		questionChoiceItemEntity10.setContent("不需要");
		questionChoiceItemEntity10.setValue("0");
		choice1.add(questionChoiceItemEntity10);
		
		QuestionChoiceItemEntity questionChoiceItemEntity11 = new QuestionChoiceItemEntity();
		questionChoiceItemEntity11.setChoiceId(1);
		questionChoiceItemEntity11.setContent("500元");
		questionChoiceItemEntity11.setValue("500");
		choice1.add(questionChoiceItemEntity11);
		
		QuestionChoiceItemEntity questionChoiceItemEntity12 = new QuestionChoiceItemEntity();
		questionChoiceItemEntity12.setChoiceId(2);
		questionChoiceItemEntity12.setContent("1000元");
		questionChoiceItemEntity12.setValue("1000");
		choice1.add(questionChoiceItemEntity12);
		
		questionEntity1.setChoice(choice1);
		
		
		QuestionEntity questionEntity2 = new QuestionEntity();
		questionEntity2.setType("2");
		questionEntity2.setNum("3");
		questionEntity2.setQuestion("您最快多久能够安排床位？");
		questionEntity2.setColumnNum(1);
		
		List<QuestionChoiceItemEntity> choice2 = new ArrayList<QuestionChoiceItemEntity>();
		QuestionChoiceItemEntity questionChoiceItemEntity20 = new QuestionChoiceItemEntity();
		questionChoiceItemEntity20.setChoiceId(0);
		questionChoiceItemEntity20.setContent("三天内");
		questionChoiceItemEntity20.setValue("3d");
		choice2.add(questionChoiceItemEntity20);
		
		QuestionChoiceItemEntity questionChoiceItemEntity21 = new QuestionChoiceItemEntity();
		questionChoiceItemEntity21.setChoiceId(1);
		questionChoiceItemEntity21.setContent("一周内");
		questionChoiceItemEntity21.setValue("1w");
		choice2.add(questionChoiceItemEntity21);
		
		QuestionChoiceItemEntity questionChoiceItemEntity22 = new QuestionChoiceItemEntity();
		questionChoiceItemEntity22.setChoiceId(2);
		questionChoiceItemEntity22.setContent("两周内");
		questionChoiceItemEntity22.setValue("2w");
		choice2.add(questionChoiceItemEntity22);
		
		QuestionChoiceItemEntity questionChoiceItemEntity23 = new QuestionChoiceItemEntity();
		questionChoiceItemEntity23.setChoiceId(3);
		questionChoiceItemEntity23.setContent("三周内");
		questionChoiceItemEntity23.setValue("3w");
		choice2.add(questionChoiceItemEntity23);
		
		questionEntity2.setChoice(choice2);
		
		questionReceiveComeList.add(questionEntity0);
		questionReceiveComeList.add(questionEntity1);
		questionReceiveComeList.add(questionEntity2);
		
		return questionReceiveComeList;
	}
	public void setQuestionReceiveComeList(
			List<QuestionEntity> questionReceiveComeList) {
		this.questionReceiveComeList = questionReceiveComeList;
	}
	
	
}
