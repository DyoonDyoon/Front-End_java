/**
 *  Created by ChoiJinYoung on 2015. 12. 06..
 */
package model;

import com.google.gson.JsonObject;

public class Answer {
	private int answerId; // 질문에 관련된 답변 ID 
	private int questionId; // 질문 ID
	public String content; // 질문의  내용
	
	public Answer() { 
		this.answerId = 0; // 답변 ID 초기화
		this.questionId = 0; // 질문 ID 초기화
		this.content = null; // 질문의 내용 초기화
	}
	public Answer(JsonObject json) {
		this();
		if (!json.get("answerId").isJsonNull()) // 답변 Id가 있을 경우 
			this.answerId = json.get("answerId").getAsInt(); // 해당 값으로 초기화
		if (!json.get("questionId").isJsonNull()) // 질문 Id가 있을 경우
			this.questionId = json.get("questionId").getAsInt(); // 해당 값으로  초기화
		if (!json.get("content").isJsonNull()) // 질문 내용이 있을 경우 
			this.content = json.get("content").getAsString(); // 해당 값으로 초기화
	}
	/**
	 * private형 변수 answerId를 반환하는 함수
	 * @return
	 */
	public int getAnswerId(){
		return answerId;
	}
	
	/**
	 * private형 변수 answerId를 지정해주는 함수
	 * @param answerId
	 */
	public void setAnswerId(int answerId){
		this.answerId= answerId;
	}
	
	
	/**
	 * private형 questionId를 반환하는 함수
	 * @return
	 */
	public int getQuestionId(){
		return questionId;
	}
	
	/**
	 * private형 questionId를 지정해주는 함수
	 * @param questionId
	 */
	public void setQuestionId(int questionId){
		this.questionId = questionId;
	}
	
	/**
	 * 매개변수로 받은 객채와 현재 객체가 같은지 판단하는 함수
	 * @param answer
	 * @return
	 */
	public boolean isEqual(Answer answer) {
		if (this.answerId == answer.getAnswerId()
				&& this.questionId == answer.getQuestionId()
				&& this.content == answer.content)
			return true;
		return false;
	}
	
	//사용자 지정 toString 함수
	public String toString() {
		return "{\n\t'answerId' : " + answerId + ",\n\t'questionId' : " + questionId + ",\n\t'content' : " + content + "\n}";
	}
}
