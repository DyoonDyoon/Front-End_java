/**
 *  Created by ChoiJinYoung on 2015. 12. 06..
 */
package model;

import com.google.gson.JsonObject;

public class Answer {
	private int answerId;
	private int questionId;
	public String content;
	
	public Answer() {}
	public Answer(JsonObject json) {
		this.answerId = json.get("answerId").getAsInt();
		this.questionId = json.get("questionId").getAsInt();
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
	
	public String toString() {
		return "{\n\t'answerId' : " + answerId + ",\n\t'questionId' : " + questionId + ",\n\t'content' : " + content + "\n}";
	}
}
