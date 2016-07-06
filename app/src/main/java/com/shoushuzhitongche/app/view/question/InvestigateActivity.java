package com.shoushuzhitongche.app.view.question;

import java.util.List;
import com.dixiang.framework.BaseActivity;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.utls.Constants;
import com.shoushuzhitongche.app.view.question.bean.QuestionChoiceItemEntity;
import com.shoushuzhitongche.app.view.question.bean.QuestionDateEntity;
import com.shoushuzhitongche.app.view.question.bean.QuestionEntity;
import com.shoushuzhitongche.app.widget.FlowRadioGroup;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class InvestigateActivity extends BaseActivity implements OnClickListener {
	private String title;
	private Button btn_commit;
	private LinearLayout ll_papers_container;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_investigate);
		initView();
	}

	@Override
	public void initHeaderView() {
		initBackView(R.drawable.left_back_white_icon, 0, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		setTitleViewValue(0, 0, R.color.white);
		setTitleViewValue(title);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_commit:

			break;
		default:
			break;
		}		
	}


	private void initView() {
		title = getIntent().getStringExtra(Constants.PARMS.PAGE_TITLE);
		btn_commit = (Button) findViewById(R.id.btn_commit);
		btn_commit.setOnClickListener( this );
		ll_papers_container = (LinearLayout) findViewById(R.id.ll_papers_container);
		QuestionDateEntity questionDateEntity = new QuestionDateEntity();
		List<QuestionEntity> questionReceiveComeList = questionDateEntity.getQuestionReceiveComeList();
		List<QuestionEntity> questionGoOutList = questionDateEntity.getQuestionGoOutList();
//		if (getString(R.string.sign_go_out).equals(title)) {
			if (questionReceiveComeList!=null) {
				getPaperView(questionGoOutList);
			}
//		}else if (getString(R.string.sign_receive_come).equals(title)) {
//			if (questionReceiveComeList!=null) {
//				getPaperView(questionReceiveComeList);
//			}
//		}


	}
	private void getPaperView(List<QuestionEntity> questionReceiveComeList) {
		for (int i = 0; i < questionReceiveComeList.size(); i++) {
			QuestionEntity questionEntity = questionReceiveComeList.get(i);

			View questionView=LayoutInflater.from(activity).inflate(R.layout.question_layout, null);
			TextView tv_question_num = (TextView) questionView.findViewById(R.id.tv_question_num);
			TextView tv_question = (TextView) questionView.findViewById(R.id.tv_question);
			
//			int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//	        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//	        tv_question.measure(w, h);
//	        int layoutWidth = tv_question.getMeasuredWidth();
//	        Toast.makeText(activity, "tvWidth"+layoutWidth, 2000).show();
	        
			tv_question_num.setText(questionEntity.getNum()+".");
			tv_question.setText(questionEntity.getQuestion());
	
			LinearLayout ll_question_choice_container = (LinearLayout) questionView.findViewById(R.id.ll_question_choice_container);
			
			if (QuestionEntity.TEXT_TYPE.equals(questionEntity.getType())) {

				ll_question_choice_container.addView(getTextTypeChoiceView(questionEntity));

				ll_papers_container.addView(questionView);
			}else if (QuestionEntity.SINGLE_TYPE.equals(questionEntity.getType())) {

				ll_question_choice_container.addView(getSingleTypeChoiceView(questionEntity));

				ll_papers_container.addView(questionView);
			}
			else if (QuestionEntity.MULTI_TYPE.equals(questionEntity.getType())) {
				
				getMultiTypeChoiceView(ll_question_choice_container, questionEntity);
				ll_papers_container.addView(questionView);
			}
			else if (QuestionEntity.SECTION_TYPE.equals(questionEntity.getType())) {
				ll_question_choice_container.addView(getSectionTypeChoiceView(questionEntity));

				ll_papers_container.addView(questionView);
			}

		}

	}

	private View getTextTypeChoiceView(QuestionEntity questionEntity) {
		View choiceView=LayoutInflater.from(activity).inflate(R.layout.question_text_layout, null);
		EditText et_content = (EditText) choiceView.findViewById(R.id.et_content);
		et_content.setHint(questionEntity.getChoice().get(0).getContent());
		return choiceView;
	}

	private View getSingleTypeChoiceView(QuestionEntity questionEntity) {
		FlowRadioGroup flowRadioGroup = new FlowRadioGroup(activity);
		
		for (int i = 0; i < questionEntity.getChoice().size(); i++) {
			QuestionChoiceItemEntity questionChoiceItemEntity = questionEntity.getChoice().get(i);
			RadioButton rb = new RadioButton(activity);
			rb.setTextSize(14f);
			rb.setButtonDrawable(R.drawable.radio_choice_bg);
			rb.setPadding(6, 10, 40, 10);
			rb.setGravity(Gravity.CENTER);
			rb.setId(questionChoiceItemEntity.getChoiceId());
			rb.setText(questionChoiceItemEntity.getContent());
			flowRadioGroup.addView(rb);
		}
		return flowRadioGroup;
	}

	
	
	private LinearLayout itemLayout;
	
	private void getMultiTypeChoiceView(LinearLayout ll_container ,final QuestionEntity questionEntity) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        itemLayout = new LinearLayout(activity);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
        itemLayout.setLayoutParams(getTagParams());
        ll_container.addView(itemLayout);
        int currentRow = 0;
        List<QuestionChoiceItemEntity> choices = questionEntity.getChoice();
        for (int i = 0; i < choices.size(); i++) {
        	final QuestionChoiceItemEntity questionChoiceItemEntity = questionEntity.getChoice().get(i);
             
        	CheckBox checkBox = new CheckBox(activity);
             
        	checkBox.setButtonDrawable(R.drawable.radio_multi_choice_bg);
        	checkBox.setTextSize(14f);
        	checkBox.setGravity(Gravity.CENTER);
        	//checkBox.setLayoutParams(getLableTextParms());
        	checkBox.setPadding(6, 10, 80, 10);
        	checkBox.setId(questionChoiceItemEntity.getChoiceId());
        	checkBox.setText(questionChoiceItemEntity.getContent());
        	
        	checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					
				}
			});
        	
             currentRow = measureTagView(checkBox, w, h, currentRow,ll_container);
        }
   }
	 
	 
   

	private View getSectionTypeChoiceView(QuestionEntity questionEntity) {
		View choiceView=LayoutInflater.from(activity).inflate(R.layout.question_section_layout, null);
		
//		EditText et_section_begin = (EditText) choiceView.findViewById(R.id.et_section_begin);
//		et_section_begin.setHint(questionEntity.getChoice().get(0).getContent());
//		
//		EditText et_section_end = (EditText) choiceView.findViewById(R.id.et_section_end);
//		et_section_end.setHint(questionEntity.getChoice().get(1).getContent());
		
		return choiceView;
	}
	
	 //  计算View尺寸,返回当前行数
    private int measureTagView(CheckBox checkBox,int width, int height, int currentRow,LinearLayout ll_container) {
    	 
       WindowManager wm = ( WindowManager )activity.getSystemService(Context.WINDOW_SERVICE);
       int widthWindowSystem = wm.getDefaultDisplay().getWidth();
       int windowWidth = widthWindowSystem;
    	
         checkBox.measure(width, height);
         itemLayout.measure(width, height);
         int layoutWidth = itemLayout.getMeasuredWidth();
         int tvWidth = checkBox.getMeasuredWidth();
         
//         Toast.makeText(activity, "tvWidth"+tvWidth, 2000).show();
//         Toast.makeText(activity, "layoutWidth+tvWidth"+(layoutWidth+tvWidth), 2000).show();
//         Toast.makeText(activity, "windowWidth"+(windowWidth - 100), 2000).show();
         if((layoutWidth+tvWidth) > (windowWidth - 120)){
              itemLayout = new LinearLayout(activity);
              itemLayout.setOrientation(LinearLayout.HORIZONTAL);
              itemLayout.setLayoutParams(getTagParams());
              ll_container.addView(itemLayout);
              currentRow ++;
         }
         itemLayout.addView(checkBox);

         return currentRow;
    }
	 private LinearLayout.LayoutParams getTagParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 0, 10);
        return params;
   }

   private LinearLayout.LayoutParams getLableTextParms() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 0, 10, 0);
        return params;
   }
   
   public void onCommit(View view){
	   
   }
}
