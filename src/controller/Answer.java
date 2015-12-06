package controller;

public class Answer {
	private String answerId;
	private String questionId;
	public String content;
	
	public String getAnswerId(){
		return answerId;
	}
	
	public void setAnswerId(String answerId){
		this.answerId= answerId;
	}
	
	public String getQuestionId(){
		return questionId;
	}
	
	public void setQuestionId(String questionId){
		this.questionId = questionId;
	}
	
	public String toString() {
		return "{\n\t'answerId' : " + answerId + ",\n\t'questionId' : " + questionId + ",\n\t'content' : " + content + "\n}";
	}
}
