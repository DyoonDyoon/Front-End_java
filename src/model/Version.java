/**
 *  Created by ChoiJinYoung on 2015. 12. 06..
 */
package model;

public class Version {
	private String lectureId; // 학수 번호
	public int notiVersion; // 공지 version
	public int assignVersion; // 과제 version
	
	// 학수 번호를 반환하는 함수
	public String getLectureId(){
		return lectureId;
	}
	
	// 학수 번호를 지정하는 함수
	public void setLectureId(String lectureId) {
		this.lectureId = lectureId;
		
	}
	
	/**
	 * 매개변수를 통한 version 객체 생성자
	 * @param lectureId
	 * @param notiVersion
	 * @param assignVersion
	 */
	public Version(String lectureId, int notiVersion, int assignVersion){
		// 입력받은 매개변수로 모든 필드를 초기화
		this.lectureId = lectureId;
		this.notiVersion = notiVersion;
		this.assignVersion = assignVersion;
	}	
}
