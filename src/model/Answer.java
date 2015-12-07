/**
 *  Created by ChoiJinYoung on 2015. 12. 06..
 */
package model;

import com.google.gson.JsonObject;

public class Answer {
	private int answerId;
	private int questionId;
	public String content;
	
	public Answer() {
		this.answerId = 0;
		this.questionId = 0;
		this.content = null;
	}
	public Answer(JsonObject json) {
		this();
		if (!json.get("answerId").isJsonNull())
			this.answerId = json.get("answerId").getAsInt();
		if (!json.get("questionId").isJsonNull())
			this.questionId = json.get("questionId").getAsInt();
		if (!json.get("content").isJsonNull())
			this.content = json.get("content").getAsString();
	}
	
	public int getAnswerId(){
		return answerId;
	}
	
	public void setAnswerId(int answerId){
		this.answerId= answerId;
	}
	
	public int getQuestionId(){
		return questionId;
	}
	
	public void setQuestionId(int questionId){
		this.questionId = questionId;
	}
	
	public boolean isEqual(Answer answer) {
		if (this.answerId == answer.getAnswerId()
				&& this.questionId == answer.getQuestionId()
				&& this.content == answer.content)
			return true;
		return false;
	}
	
	public String toString() {
		return "{\n\t'answerId' : " + answerId + ",\n\t'questionId' : " + questionId + ",\n\t'content' : " + content + "\n}";
	}
}
