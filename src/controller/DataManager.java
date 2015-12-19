/**
 * 전체적인 DB를 관리하는 클래스
 * Created by ChoiJinYoung on 2015. 12. 06..
 */
package controller;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Answer;
import model.Assignment;
import model.Grade;
import model.Lecture;
import model.LectureOutline;
import model.Notification;
import model.Question;
import model.RecommendedLecture;
import model.Submit;
import model.Version;

public class DataManager {	
	Connection conn = null; 		// DB 연결을 위한 객체
	PreparedStatement pstmt = null; // 동적 query문을 위한 객체
	
	/**
	 * DB에 접속하여 DB open
	 * @param 
	 * @return 
	 */
	public boolean openDB(){ 
		String myUrl = "jdbc:mysql://localhost:3306/eclass?useUnicode=true&characterEncoding=utf8"; // 사용하려는 데이터베이스명을 포함한 URL 기술
		String id = "root"; // 사용자 계정
		String pw = "5721"; // 사용자 계정의 패스워드
//		String myUrl = "jdbc:mysql:// localhost:3306/front_eclass?useUnicode=true&characterEncoding=utf8"; // 사용하려는 데이터베이스명을 포함한 URL 기술
//		String id = "root"; // 사용자 계정
//		String pw = "1234"; // 사용자 계정의 패스워드
		
		try { 
			  Class.forName("com.mysql.jdbc.Driver"); // DB와 연동하기 위해 DriverManager에 등록  
		}catch(ClassNotFoundException e){
			  System.out.println("드라이버 검색 실패"); // 예외처리
			  return false;
		}
		try{
			conn = DriverManager.getConnection(myUrl, id, pw); // DriverManager 객체로부터 Connection 객체를 얻어온다
		}catch(SQLException e){
			System.out.println("DB 접속 실패"); // 예외처리
			return false;
		}
		return true;
	}
	
	/**
	 * Version DB에 version 삽입
	 * @param lectureId
	 * @return
	 */
	public boolean insertVersionDB(String lectureId){
		pstmt = null;	
		String sql = "insert into version values (?, ?, ?)"; 
		try{	  
			pstmt = conn.prepareStatement(sql);		// 동적 query문
			pstmt.setString(1, lectureId); 			// 학수번호
			pstmt.setInt(2, 0);  					// 공지 version
			pstmt.setInt(3, 0); 					// 과제 version
			int n = pstmt.executeUpdate();   		// query문 실행
			if(n<=0){
				System.out.println("insert 실패");
			}
		} catch(SQLException e){ // 예외처리
			e.printStackTrace();	
			return false; // 삽입 실패 시 false 반환
		}
		return true; // 삽입이 성공하면 true 반환
	}
	
	/**
	 * 동기화를 위해 Version DB에서 Version 검색
	 * @param lectureId
	 * @return Version
	 */
	public Version selectVersionDB(String lectureId){
		Version version = null;
		pstmt = null;	
		String sql = "select * from version where lectureId=\"" + lectureId + "\""; 	
		try{ 
	        pstmt = conn.prepareStatement(sql);			// 동적 query문
	        ResultSet result = pstmt.executeQuery(sql); // ResultSet 객체를 통한 query 결과 처리
	        if (result.next()) { // 결과 저장
	        	version = new Version("", 0, 0); 
		        version.setLectureId(result.getString(1)); 	// 학수번호
		        version.notiVersion = result.getInt(2); 	// 공지 version
		        version.assignVersion= result.getInt(3);	// 과제 version
	        }
		}
	    catch (Exception e) {  // 예외처리
	        System.out.println(e.getMessage()); 
	        e.printStackTrace();
	    }
		return version; // 검색된 version 반환
	}
	
	/**
	 * Version DB에서 version 수정
	 * @param lectureId
	 * @param notiVersion
	 * @param assignVersion
	 * @return
	 */
	public boolean updateVersionDB(String lectureId, int notiVersion, int assignVersion){
//		양자택일
//		notiVersion - 공지 version [optional]
//		assignVersion - 과제 version [optional]
		int index = 1;
		pstmt = null;
		String sql = "update version set "; 
		if(notiVersion != -1){	
			sql = sql + "notiVer=?";	// 공지 version을 수정할 경우
		}
		if(assignVersion !=-1){	
			if(notiVersion != -1){
				sql = sql + ", ";	// 공지 version과 과제 version을 수정할 경우
			}
			sql = sql + "assignVer=?";	// 과제 version을 수정할 경우
		}
		sql = sql + " where lectureId=?";
		try{	  
			pstmt = conn.prepareStatement(sql);	// 동적 query문
			if(notiVersion != -1){ 
				pstmt.setString(index++, String.valueOf(notiVersion)); 	// 공지 version을 수정할 경우
			}
			if(assignVersion != -1){
				pstmt.setString(index++, String.valueOf(assignVersion)); // 과제 version을 수정할 경우
			}
			pstmt.setString(index, lectureId);	
			int n = pstmt.executeUpdate(); // query문 실행
			if(n<=0){
				System.out.println("insert 실패"); // 예외처리
			}
		} catch (Exception e){ // 예외처리
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false; //수정 실패 시 false 반환
		}
		return true; //수정 성공 시 true 반환
	}
	
	
	/**
	 * Grade DB에 성적정보 삽입
	 * @param gradeId
	 * @param lectureId
	 * @param submitId
	 * @param studentId
	 * @param score
	 * @return
	 */
	public boolean insertGradeDB(int gradeId, String lectureId, int submitId, String studentId, double score){
		pstmt = null;	//동적 query문
		String sql = "insert into grade values (?, ?, ?, ?, ?)";
		try{	  
			pstmt = conn.prepareStatement(sql);	// 동적 query문
			pstmt.setInt(1, gradeId);			// 학점 Id
			pstmt.setString(2, lectureId); 		// 학수번호
			pstmt.setInt(3, submitId); 			// 제출 Id
			pstmt.setString(4, studentId);		// 학생 Id
			pstmt.setDouble(5, score);			// 점수
			int n = pstmt.executeUpdate();   	// query문 실행
			if(n<=0){
				System.out.println("insert 실패"); // 예외처리
			}
		} catch(SQLException e){ // 예외처리
			e.printStackTrace();
			return false; //삽입 실패 시 false 반환
		}
		return true; //삽입 성공 시 true 반환
	}
	
	/**
	 * Grade DB에서 성적정보 검색
	 * @param stuId
	 * @param lectureId
	 * @return
	 */
	public ArrayList<Grade> selectGradeDB(String stuId, String lectureId){
//		양자택일
//		stuId - 성적을 찾는 학생 아이디 [optional]
//		lectureId - 성적을 가진 강의 아이디 [optional]
		if ((stuId == null && lectureId == null)) {
			System.out.println("Get Grade : at least ONE parameter");
			return null;
		}
		
		ArrayList<Grade> grades =  new ArrayList<Grade>(); // 결과를 저장할 ArrayList 선언
		pstmt = null;	
		String sql = "select * from grade where ";
		if (stuId != null) { 
			sql = sql + "stuId=\"" + stuId + "\""; // 학생Id로 검색하는 경우
		}
		if (lectureId != null) {
			if (stuId != null) {
				sql = sql + " && "; // 학생 Id와 학수번호로 검색하는 경우
			}
			sql = sql + "lectureId=\"" + lectureId + "\""; // 학수번호로 검색하는 경우
		}
		try{ 
	        pstmt = conn.prepareStatement(sql);  // 동적 query문		
	        ResultSet result = pstmt.executeQuery(sql); // ResultSet 객체를 통한 query 결과 처리
	        while (result.next()){ // 결과 저장
	        	Grade grade = new Grade();
	        	grade.setGradeId(result.getInt(1)); 		// primary key
	        	grade.setLectureId(result.getString(2)); 	// 학수번호
	        	grade.setSubmitId(result.getInt(3));		// 제출 Id
	        	grade.setStudentId(result.getString(4));	// 학생 Id
	        	grade.setScore(result.getDouble(5));		// 점수
	        	grades.add(grade);	// ArrayList에 결과 저장
	        }
	    } catch (Exception e){ // 예외처리
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return grades; // 검색된 성적 반환
	}
	
	/**
	 * Grade DB에서 성적정보 수정
	 * @param gradeId
	 * @param score
	 * @return
	 */
	public boolean updateGradeDB(int gradeId, double score) {
		pstmt = null;	
		String sql = "update grade set score=? where gradeId=?";
		try {
			pstmt = conn.prepareStatement(sql);			// 동적 query문
			pstmt.setString(1, String.valueOf(score)); 	// 점수
			pstmt.setInt(2, gradeId); 					// 학점 Id
			int n = pstmt.executeUpdate(); 				// 쿼리문 실행
			if(n<=0){
				System.out.println("insert 실패");		// 예외처리
			}
		} catch (SQLException e) { // 예외처리
			e.printStackTrace();
			return false; // 수정 실패 시 false 반환
		}
		return true; // 수정 성공 시 true 반환
	}
	
	/**
	 * Grade DB에서 성적정보 삭제
	 * @param gradeId
	 * @return
	 */
	public boolean deleteGradeDB(int gradeId){ 
		pstmt = null;	
		String sql = "delete from grade where gradeId=?";
		try {
			pstmt = conn.prepareStatement(sql);	// 동적 query문
			pstmt.setInt(1, gradeId);			// primary key
			pstmt.executeUpdate();				// query문 실행
		} catch (Exception e) {	// 예외처리
	        System.out.println(e.getMessage());
	        e.printStackTrace();
			return false; // 삭제 실패 시 false 반환
		}
		return true; // 삭제 성공 시 true 반환
	}
	
	/**
	 * Grade DB에서 성적정보 모두 삭제
	 * @return
	 */
	public boolean deleteAllGradeDB() {
		pstmt = null;	
		String sql = "delete from grade";
		try {
			pstmt = conn.prepareStatement(sql); // 동적 query문
			pstmt.executeUpdate(); 				// query문 실행
		} catch (Exception e) { // 예외처리
	        System.out.println(e.getMessage());
	        e.printStackTrace();
			return false; // 삭제 실패 시 false 반환
		}
		return true; // 삭제 성공 시 true 반환
	}
	
	/**
	 * Submit DB에 제출정보 삽입
	 * @param submitId
	 * @param lectureId
	 * @param assignId
	 * @param studentId
	 * @param filePath
	 * @return
	 */
	public boolean insertSubmitDB(int submitId, String lectureId, int assignId, String studentId, String filePath){
		pstmt = null;
		String sql = "insert into submit values (?, ?, ?, ?, ?)";
		try{	  
			pstmt = conn.prepareStatement(sql);	// 동적 query문
			pstmt.setInt(1, submitId); 			// 제출 Id
			pstmt.setString(2, lectureId);		// 학수번호
			pstmt.setInt(3, assignId); 			// 과제 Id
			pstmt.setString(4, studentId);		// 학생Id
			pstmt.setString(5, filePath);		// 파일경로
			int n = pstmt.executeUpdate();   	// query문 실행
			if(n<=0){
				System.out.println("insert 실패"); // 예외처리
			}
		} catch(SQLException e){ // 예외처리
			e.printStackTrace();
			return false; // 삽입 실패 시 false 반환
		}
		return true; // 삽입 성공 시 true 반환
	}
	
	/**
	 * Submit DB에서 하나의 제출정보 검색
	 * @param submitId
	 * @return
	 */
	public Submit selectSubmit(int submitId) {
		Submit submit = null;
		pstmt = null;			
		String sql = "select * from submit where submitId=\"" + submitId + "\"";
		try{ 
	        pstmt = conn.prepareStatement(sql);				// 동적 query문
	        ResultSet result = pstmt.executeQuery(sql);		// ResultSet 객체를 통한 query 결과처리
	        if (result.next()){ // 결과 처리
	        	submit = new Submit();
	        	submit.setSubmitId(result.getInt(1)); 		// primary key
	        	submit.setLectureId(result.getString(2));	// 학수번호
	        	submit.setAssignId(result.getInt(3));		// 과제 Id
	        	submit.setStudentId(result.getString(4));	// 학생 Id
	        	submit.setFilePath(result.getString(5));	// 파일 path
	        }
	    } catch (Exception e){  // 예외처리
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return submit; //겅색된 submit 반환
	}
	
	/**
	 * Submit DB에서 제출정보 검색
	 * @param lectureId
	 * @param assignId
	 * @param stuId
	 * @return
	 */
	public ArrayList<Submit> selectSubmitDB(String lectureId, int assignId, String stuId){
//		양자택일
//		assignId - 과제 아이디 [optional]
//		stuId - 과제를 제출한 학생 아이디 [optional]
		ArrayList<Submit> submits =  new ArrayList<Submit>(); // 제출 결과를 저장할 ArrayList 선언
		pstmt = null;			
		String sql = "select * from submit where lectureId=\"" + lectureId + "\"";
		if (assignId != -1) {
			sql = sql + " && assignId=\"" + assignId + "\""; // 과제 Id로 검색하는 경우
		}
		
		if (stuId != null) {
			sql = sql + " && stuId=\"" + stuId + "\""; // 학생 Id로 검색하는 경우
		}
		try{ 
	        pstmt = conn.prepareStatement(sql); 		// 동적 query문
	        ResultSet result = pstmt.executeQuery(sql);	// ResultSet 객체를 통한 query 결과처리
	        while (result.next()){ // 결과 저장
	        	Submit submit = new Submit();
	        	submit.setSubmitId(result.getInt(1)); 		// primary key
	        	submit.setLectureId(result.getString(2));	// 학수번호
	        	submit.setAssignId(result.getInt(3));		// 과제 Id
	        	submit.setStudentId(result.getString(4));	// 학생 Id
	        	submit.setFilePath(result.getString(5));	// 파일 path
	        	submits.add(submit); //검색된 submit을 ArrayList에 저장
	        }
	    } catch (Exception e){ // 예외처리  
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return submits; //검색된 submits 반환
	}
	
	/**
	 * Submit DB에서 제출정보 수정	
	 * @param submitId
	 * @param filePath
	 * @return
	 */
	public boolean updateSubmitDB(int submitId, String filePath) { 
		pstmt = null;	
		String sql = "update submit set filePath=? where submitId=?";
		try {
			pstmt = conn.prepareStatement(sql);	// 동적 query문
			pstmt.setString(1, filePath); 		// 수정할 파일경로
			pstmt.setInt(2, submitId); 			// 제출 Id
			int n = pstmt.executeUpdate(); 		// query문 실행
			if(n<=0){
				System.out.println("insert 실패"); // 예외처리
			}
		} catch (SQLException e) { // 예외처리
			e.printStackTrace();
			return false; //수정 실패 시 false 반환
		}
		return true; //수정 성공 시 true 반환
	}
	
	/**
	 * Submit DB에서 제출정보 삭제
	 * @param submitId
	 * @return
	 */
	public boolean deleteSubmitDB(int submitId){
		pstmt = null;	
		String sql = "delete from submit where submitId=?";
		try {
			pstmt = conn.prepareStatement(sql);	// 동적 query문
			pstmt.setInt(1, submitId);			// 과제 Id로 검색
			pstmt.executeUpdate(); 				// query문 실행
		} catch (Exception e) { //예외처리
	        System.out.println(e.getMessage()); 
	        e.printStackTrace();
			return false; //삭제 실패 시 false 반환
		}
		return true; //삭제 성공 시 true 반환
	}
	
	/**
	 * Submit DB에서 제출정보 모두 삭제
	 * @param submitId
	 * @return
	 */
	public boolean deleteAllSubmitDB(int submitId){
		pstmt = null;	
		String sql = "delete from submit";
		try {
			pstmt = conn.prepareStatement(sql);	// 동적 query문
			pstmt.executeUpdate();				// query문 실행
		} catch (Exception e) {	// 예외처리
	        System.out.println(e.getMessage());
	        e.printStackTrace();
			return false; // 삭제 실패 시 false 반환
		}
		return true; // 삭제 성공 시 true 반환
	}
	
	/**
	 * Assignment DB에 과제정보 삽입
	 * @param assignId
	 * @param lectureId
	 * @param title
	 * @param description
	 * @param filePath
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public boolean insertAssignmentDB(int assignId, String lectureId, String title, String description, String filePath, String startDate, String endDate){
		pstmt = null;	
		String sql = "insert into assignment(assignId, lectureId, title, description, filePath, startDate, endDate)"
				+ "values (?, ?, ?, ?, ?, ?, ?)";
		try{	  
			pstmt = conn.prepareStatement(sql);	// 동적 query문
			pstmt.setInt(1, assignId); 			// 과제Id
			pstmt.setString(2, lectureId); 		// 학수번호
			pstmt.setString(3, title);			// 제목
			pstmt.setString(4, description); 	// 내용
			pstmt.setString(5, filePath); 		// 파일경로
			pstmt.setString(6, startDate);		// 시작일
			pstmt.setString(7, endDate);		// 마감일
			int n = pstmt.executeUpdate();   	// 쿼리문 실행
			if(n<=0){
				System.out.println("insert 실패");
			}
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
		
	/**
	 * Assignment DB에서 과제 검색
	 * @param key
	 * @return
	 */
	public ArrayList<Assignment> selectAssignmentDB(String key){	  
		ArrayList<Assignment> assignments =  new ArrayList<Assignment>(); //검색 결과를 저장할 ArrayList 선언
		pstmt = null;		
		String sql = "select * from assignment where lectureId=\"" + key + "\"";
		try{ 
	        pstmt = conn.prepareStatement(sql);  		// 동적 query문	
	        ResultSet result = pstmt.executeQuery(sql);	// ResultSet 객체를 통한 query문 결과처리
	        while (result.next()){ //결과 저장
	        	Assignment assignment = new Assignment();
	        	assignment.setAssignId(result.getInt(2));		// primary key
	        	assignment.setLectureId(result.getString(3));	// 학수번호
	        	assignment.title = result.getString(4);			// 제목
	        	assignment.description = result.getString(5);	// 내용
	        	assignment.filePath = result.getString(6);		// 파일 path
	        	assignment.startDate = result.getString(7);		// 시작일
	        	assignment.endDate = result.getString(8);		// 마감일
	        	assignments.add(assignment); // ArrayList에 assignment 저장
	        }
	    } catch (Exception e){ // 예외처리
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return assignments; // 검색된 assignments 반환
	}
	
	/**
	 * Assignment DB에서 과제정보 수정
	 * @param assignId
	 * @param title
	 * @param description
	 * @return
	 */
	public boolean updateAssignmentDB(int assignId, String title, String description) {
//		양자택일
//		title - 수정할 과제 제목 [optional]
//		description - 수정할 과제 내용 [optional]
		pstmt = null;	
		int index = 1;
		String sql = "update assignment set ";
		if(title != null){
			sql = sql + "title=?"; // 제목을 수정할 경우
		}
		if(description != null){
			if(title != null){
				sql = sql+", "; // 제목과 내용을 수정할 경우
			}
			sql = sql + "description=?"; // 내용을 수정할 경우		
		}
		sql = sql + " where assignId=?";
		try {
			pstmt = conn.prepareStatement(sql);	 //동적 query문
			if(title != null){
				pstmt.setString(index++, title); // 수정할 제목
			}
			if(description != null){
				pstmt.setString(index++, description);	// 수정할 내용
			}
			pstmt.setInt(index, assignId); 	// 과제 Id
			int n = pstmt.executeUpdate(); 	// query문 실행
			if(n<=0){
				System.out.println("insert 실패"); // 예외처리
			}
		} catch (SQLException e) { //예외처리
			e.printStackTrace();
			return false; // 수정 실패 시 false 반환
		}
		return true; // 수정 성공 시 true 반환
	}
	
	/**
	 * Assignment DB에서 과제정보 삭제
	 * @param assignId
	 * @param lectureId
	 * @return
	 */
	public boolean deleteAssignmentDB(int assignId, String lectureId){
		pstmt = null;
		String sql = "delete from assignment where assignId=? && lectureId=?";
		try {
			pstmt = conn.prepareStatement(sql);	// 동적 query문
			pstmt.setInt(1, assignId);			// primary key
			pstmt.setString(2, lectureId);		// 학수번호
			pstmt.executeUpdate();				// query문 실행
		} catch (Exception e) {	// 예외처리
	        System.out.println(e.getMessage());
	        e.printStackTrace();
			return false; // 삭제 실패 시 false 반환
		}
		return true; // 삭제 성공 시 true 반환
	}
	
	/**
	 * Question DB에서 질의정보 삽입
	 * @param questionId
	 * @param lectureId
	 * @param studentId
	 * @param content
	 * @return
	 */
	public boolean insertQuestionDB(int questionId, String lectureId, String studentId, String content){
		pstmt = null;	
		String sql = "insert into question values (?, ?, ?, ?)";
		try{	  
			pstmt = conn.prepareStatement(sql);	// 동적 query문
			pstmt.setInt(1, questionId); 		// 질의Id
			pstmt.setString(2, lectureId); 		// 학수번호
			pstmt.setString(3, studentId); 		// 학생Id
			pstmt.setString(4, content);		// 내용
			int n = pstmt.executeUpdate();   	// query문 실행
			if(n<=0){
				System.out.println("insert 실패"); // 예외처리
			}
		} catch(SQLException e){ // 예외처리
			e.printStackTrace();
			return false; //삽입 실패 시 false 반환
		}
		return true; //삽입 성공 시 true 반환
	}
	
	/**
	 * Question DB에서 질의정보 검색
	 * @param stuId
	 * @param lectureId
	 * @return
	 */
	public ArrayList<Question> selectQuestionDB(String stuId, String lectureId){	 
//		양자택일
//		stuId - 질문자 아이디 (학생) [optional]
//		lectureId - 질문한 강의의 학수번호 [optional]
		if ((stuId == null && lectureId == null)) {
			System.out.println("Get Grade : at least ONE parameter");
			return null;
		}
		
		ArrayList<Question> questions =  new ArrayList<Question>(); // 결과를 저장할 ArrayList 선언
		pstmt = null;			
		String sql = "select * from question where ";
		if (stuId != null) {
			sql = sql + "stuId=\"" + stuId + "\""; // 학생Id로 검색할 경우
		}
		if (lectureId != null) {
			if (stuId != null) {
				sql = sql + " && ";	// 학생Id와 학수번호로 검색할 경우
			}
			sql = sql + "lectureId=\"" + lectureId + "\""; // 학수번호로 검색할 경우
		}
		try{ 
	        pstmt = conn.prepareStatement(sql);  // 동적 query문
	        ResultSet result = pstmt.executeQuery(sql); // ResultSet 객체를 이용한 결과처리
	        while (result.next()){ // 결과 저장
	        	Question question = new Question();
	        	question.setQuestionId(result.getInt(1)); 	// primary key
	        	question.setLectureId(result.getString(2));	// 학수번호
	        	question.setStudentId(result.getString(3)); // 학생Id
	        	question.content = result.getString(4);		// 내용
	        	questions.add(question);	// ArrayList에 question 저장
	        }
	    } catch (Exception e){  // 예외처리
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return questions; // 검색된 questions 반환
	}
	
	/**
	 * Question DB에서 질의정보 수정
	 * @param questionId
	 * @param content
	 * @return
	 */
	public boolean updateQuestionDB(int questionId, String content) {
		pstmt = null;	
		String sql = "update question set content=? where questionId=?";
		try {
			pstmt = conn.prepareStatement(sql);	// 동적 query문
			pstmt.setString(1, content); 		// 수정할 내용
			pstmt.setInt(2, questionId); 		// 질의 Id
			int n = pstmt.executeUpdate(); 		// 쿼리문 실행
			if(n<=0){
				System.out.println("insert 실패");
			}
		} catch (SQLException e) { // 예외처리
			e.printStackTrace();
			return false; // 수정 실패 시 false 반환
		}
		return true; // 수정 성공 시 true 반환
	}
	
	/**
	 * Question DB에서 질의정보 삭제
	 * @param questionId
	 * @return
	 */
	public boolean deleteQuestionDB(int questionId){
		pstmt = null;	
		String sql = "delete from question where questionId=?";
		try {
			pstmt = conn.prepareStatement(sql);	// 동적 query문
			pstmt.setInt(1, questionId);		// primary key
			pstmt.executeUpdate();				// query문 실행
		} catch (Exception e) {	// 예외처리
	        System.out.println(e.getMessage());
	        e.printStackTrace();
			return false;	// 삭제 실패 시 false 반환
		}
		return true;	// 삭제 성공 시 true 반환
	}

	/**
	 * Question DB에서 질의정보 모두 삭제
	 * @return
	 */
	public boolean deleteAllQuestionDB(){
		pstmt = null;	
		String sql = "delete from question";
		try {
			pstmt = conn.prepareStatement(sql);	// 동적 query문
			pstmt.executeUpdate();				// query문 실행
		} catch (Exception e) { // 예외처리
	        System.out.println(e.getMessage());
	        e.printStackTrace();
			return false; // 삭제 실패 시 false 반환
		}
		return true; // 삭제 성공 시 true 반환
	}
	
	/**
	 * Answer DB에 답변정보 삽입
	 * @param answerId
	 * @param questionId
	 * @param content
	 * @return
	 */
	public boolean insertAnswerDB(int answerId, int questionId, String content){
		pstmt = null;
		String sql = "insert into answer values (?, ?, ?)";
		try{	  
			pstmt = conn.prepareStatement(sql);	// 동적 query문
			pstmt.setInt(1, answerId); 			// 답변Id
			pstmt.setInt(2, questionId); 		// 학수번호
			pstmt.setString(3, content);		// 내용
			int n = pstmt.executeUpdate();   	// query문 실행
			if(n<=0){
				System.out.println("insert 실패"); // 예외처리
			}
		} catch(SQLException e){ // 예외처리
			e.printStackTrace();
			return false; // 삽입 실패 시 false 반환
		}
		return true; // 삽입 성공 시 true 반환
	}
	
	/**
	 * Answer DB에서 답변정보 검색
	 * @param key
	 * @return
	 */
	public Answer selectAnswerDB(int key){	 
		Answer answer =  new Answer();
		pstmt = null;			
		String sql = "select * from answer where questionId=\"" + key + "\"";
		try{ 
	        pstmt = conn.prepareStatement(sql);			// 동적 query문
	        ResultSet result = pstmt.executeQuery(sql); // ResultSet 객체를 통한 query문 결과처리
	        result.next();								// 검색 결과
	    	answer.setAnswerId(result.getInt(1)); 		// primary key
	    	answer.setQuestionId(result.getInt(2));		// 답변할 질의 Id
	    	answer.content = result.getString(3);		// 내용
	    } catch (Exception e){   // 예외처리
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return answer; //검색된 답변 정보 반환
	}
	
	/**
	 * Answer DB에서 답변정보 수정
	 * @param answerId
	 * @param content
	 * @return
	 */
	public boolean updateAnswerDB(int answerId, String content) {
		pstmt = null;	
		String sql = "update answer set content=? where answerId=?";
		try {
			pstmt = conn.prepareStatement(sql);	// 동적 query문
			pstmt.setString(1, content); 		// 수정할 내용
			pstmt.setInt(2, answerId); 			// 답변 Id
			int n = pstmt.executeUpdate(); 		// query문 실행
			if(n<=0){
				System.out.println("insert 실패"); // 예외처리
			}
		} catch (SQLException e) { // 예외처리
			e.printStackTrace();
			return false; // 수정 실패 시 false 반환
		}
		return true; // 수정 성공 시 true 반환
	}
		
	/**
	 * Answer DB에서 답변정보 삭제
	 * @param answerId
	 * @return
	 */
	public boolean deleteAnswerDB(int answerId){
		pstmt = null;	
		String sql = "delete from answer where answerId=?";
		try {
			pstmt = conn.prepareStatement(sql);	// 동적 query문
			pstmt.setInt(1, answerId);			// primary key
			pstmt.executeUpdate();				// query문 실행
		} catch (Exception e) {	// 예외처리
	        System.out.println(e.getMessage());
	        e.printStackTrace();
			return false;	// 삭제 실패 시 false 반환
		}
		return true;	// 삭제 성공 시 true 반환
	}
	
	/**
	 * Notification DB에 공지정보 삽입
	 * @param notiId
	 * @param lectureId
	 * @param title
	 * @param description
	 * @return
	 */
	public boolean insertNotificationDB(int notiId, String lectureId, String title, String description){
		pstmt = null;	
		String sql = "insert into notification(notificationId, lectureId, title, description) values (?, ?, ?, ?)";
		try{	  
			pstmt = conn.prepareStatement(sql);	//동적 query문
			pstmt.setInt(1, notiId); 			// 공지Id
			pstmt.setString(2, lectureId); 		// 학수번호
			pstmt.setString(3, title); 			// 강의명
			pstmt.setString(4, description);	// 내용
			int n = pstmt.executeUpdate();   	// query문 실행
			if(n<=0){
				System.out.println("insert 실패");	// 예외처리
			}
		} catch(SQLException e){	// 예외처리
			e.printStackTrace();
			return false; // 삽입 실패 시 false 반환
		}
		return true; // 삽입 성공 시 true 반환
	}
	
	/**
	 * Notification DB에서 공지정보 검색
	 * @param key
	 * @return
	 */
	public ArrayList<Notification> selectNotificationDB(String key){	
		ArrayList<Notification> notifications =  new ArrayList<Notification>(); // 검색 결과를 저장할 ArrayList 선언
		pstmt = null;			
		String sql = "select * from notification where lectureId=\"" + key + "\"";
		try{ 
	        pstmt = conn.prepareStatement(sql);  			// 동적 query문
	        ResultSet result = pstmt.executeQuery(sql);		// ResultSet 객체를 통해 query문 결과 처리
	        while (result.next()){ // 결과 저장
	        	Notification notification = new Notification();
	        	notification.setNotificationId(result.getInt(2));	// primary key
	        	notification.setLectureId(result.getString(3));		// 학수번호
	        	notification.title = result.getString(4); 			// 제목
	        	notification.description = result.getString(5);		// 내용
	        	notifications.add(notification);	//ArrayList에 notification 저장
	        }
	    } catch (Exception e){   // 예외처리
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return notifications;	// 검색된 notifications 반환
	}
	
	/**
	 * Notification DB에서 공지정보 수정
	 * @param notiId
	 * @param lectureId
	 * @param title
	 * @param description
	 * @return
	 */
	public boolean updateNotificationDB(int notiId, String lectureId, String title, String description) {
//		양자택일
//		title - 수정할 제목 [optional]
//		description - 수정할 내용 [optional]
		pstmt = null;	
		int index = 1;
		String sql = "update notification set ";
		if(title != null){
			sql = sql + "title=?"; // 제목을 수정할 경우
		}
		if(description != null){
			if(title!=null){
				sql = sql + ", ";	// 제목과 내용을 수정할 경우
			}
			sql = sql + "description=?"; // 내용을 수정할 경우
		}
		sql = sql + "  where notificationId=? && lectureId=?";
		try {
			pstmt = conn.prepareStatement(sql);		//동적 query문
			if(title != null){
				pstmt.setString(index++, title); 	// 수정할 제목
			}
			if(description != null){
				pstmt.setString(index++, description); 	// 수정할 내용
			}
			pstmt.setInt(index++, notiId); 			// 공지Id
			pstmt.setString(index++, lectureId); 	// 공지Id
			int n = pstmt.executeUpdate(); 			// query문 실행
			if(n<=0){
				System.out.println("insert 실패"); // 예외처리
			}
		} catch (SQLException e) { // 예외처리
			e.printStackTrace();
			return false; // 수정 실패 시 false 반환
		}
		return true; // 수정 성공 시 true 반환
	}
	
	/**
	 * Notification DB에서 공지정보 삭제
	 * @param notiId
	 * @param lectureId
	 * @return
	 */
	public boolean deleteNotificationDB(int notiId, String lectureId){
		pstmt = null;	
		String sql = "delete from notification where notificationId=? && lectureId=?";
		try {
			pstmt = conn.prepareStatement(sql);	// 동적 query문
			pstmt.setInt(1, notiId);			// primary key
			pstmt.setString(2, lectureId);		// 학수번호
			pstmt.executeUpdate();				// query문 실행
		} catch (Exception e) {	// 예외처리
	        System.out.println(e.getMessage());
	        e.printStackTrace();
			return false;	// 삭제 실패 시 false 반환
		}
		return true;	// 삭제 성공 시 true 반환
	}

	/**
	 * Lecture DB에 강의정보 삽입	
	 * @param lectureId
	 * @param userId
	 * @return
	 */
	public boolean insertLectureDB(String lectureId, String userId){
		pstmt = null;	
		String sql = "insert into lecture SET lectureId=?, userId=?";
		try{	  
			pstmt = conn.prepareStatement(sql);	// 동적 query문
			pstmt.setString(1, lectureId); 		// 학수번호
			pstmt.setString(2, userId); 		// userId
			int n = pstmt.executeUpdate();   	// query문 실행
			if(n<=0){
				System.out.println("insert 실패"); // 예외처리
			}
		} catch(SQLException e){ // 예외처리
			e.printStackTrace();
			return false; // 삽입 실패 시 false 반환
		}
		return true; // 삽입 성공 시 true 반환
	}
	
	/**
	 * Lecture DB에서 강의정보 검색
	 * @param key
	 * @return
	 */
	public ArrayList<Lecture> selectLectureDB(String key){	 
		ArrayList<Lecture> lectures = new ArrayList<Lecture>();	// 결과를 저장할 ArrayList 선언
		pstmt = null;	
		String sql = "select * from lecture where userId=\"" + key + "\"";
		try{ 
	        pstmt = conn.prepareStatement(sql); 		// 동적 query문		
	        ResultSet result = pstmt.executeQuery(sql);	// ResultSet 객체를 통해 query문 결과처리
	        while (result.next()){ // 검색 결과
	        	Lecture lecture = new Lecture();
	        	//result.getString(1)은 id로 primary key라서 저장안함
                lecture.setLectureId(result.getString(2)); 	// 학수 번호
                lecture.setUserId(result.getString(3));		// User Id
                lectures.add(lecture);	// ArrayList에 lecture 저장
	        }
	    } catch (Exception e){  // 예외처리
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return lectures; //검색된 lectures 반환
	}
	
	/**
	 * Lecture DB에서 강의정보 삭제
	 * @param lectureId
	 * @param userId
	 * @return
	 */
	public boolean deleteLectureDB(String lectureId, String userId){
		pstmt = null;			
		String sql = "delete from lecture where lectureId=? && userId =?";
		try {
			pstmt = conn.prepareStatement(sql);	// 동적 query문
			pstmt.setString(1, lectureId);		// primary key
			pstmt.setString(2, userId);			// userId
			pstmt.executeUpdate();				// query문 실행
		} catch (Exception e) {	// 예외처리
	        System.out.println(e.getMessage());
	        e.printStackTrace();
			return false;	// 삭제 실패 시 false 반환
		}
		return true;	// 삭제 성고 시 true 반환
	}
	
	/**
	 * Lecture DB에서 강의정보 모두 삭제
	 * @return
	 */
	public boolean deleteAllLectureDB() { 
		pstmt = null;			
		String sql = "delete from lecture";
		try {
			pstmt = conn.prepareStatement(sql);	// 동적 query문
			pstmt.executeUpdate();				// query문 실행
		} catch (Exception e) {	// 예외처리
	        System.out.println(e.getMessage());
	        e.printStackTrace();
			return false; // 삭제 실패 시 false 반환
		}
		return true;	// 삭제 성공 시 true 반환
	}
	
	/**
	 * LectureOutline DB에 강의개요 삽입
	 * @param lectureOutline
	 * @return
	 */
	public boolean insertLectureOutlineDB(ArrayList<LectureOutline> lectureOutline){
		pstmt = null;
		String sql = "insert into lecture_outline VALUES (?, ?, ?, ?, ?)";
		try{	  
			for (LectureOutline object : lectureOutline) {
				pstmt = conn.prepareStatement(sql);			// 동적 query문
				pstmt.setString(1, object.getLectureId()); 	// 학수번호
				pstmt.setString(2, object.professorName); 	// 교수명
				pstmt.setString(3, object.title); 			// 강의명
				pstmt.setString(4, object.curriculum); 		// 교과과정
				pstmt.setString(5, String.valueOf(object.getPoint())); // 학점
				int n = pstmt.executeUpdate();   			// query문 실행
				if(n<=0){
					System.out.println("insert 실패"); // 예외처리
				}
			}
		} catch(SQLException e){ // 예외처리
			e.printStackTrace();
			return false; // 삽입 실패 시 false 반환
		}
		return true; // 삽입 성공 시 true 반환
	}
	
	/**
	 * LectureOutline DB에서 강의개요 검색
	 * @param key
	 * @return
	 */
	public LectureOutline selectLectureOutlineDB(String key){		
		LectureOutline lectureOutlines = null;
		pstmt = null;	
		String sql = "select * from lecture_outline where lectureId=\"" + key + "\"";
		try{ 
	        pstmt = conn.prepareStatement(sql);	// 동적 query문		
	        ResultSet result = pstmt.executeQuery(sql); 	// ResultSet 객체를 통해 query문 결과처리 
	        if (result.next()) { 	// 결과 저장
		        lectureOutlines = new LectureOutline("", "", "", "", 0); 
		        lectureOutlines.setLectureId(result.getString(1));		// primary key
		        lectureOutlines.professorName = result.getString(2);	// 교수명
		        lectureOutlines.title = result.getString(3);			// 제목
		        lectureOutlines.curriculum = result.getString(4);		// 교과과정
		        lectureOutlines.setPoint(Integer.parseInt((result.getString(5))));	// 학점
	        }
		}
	    catch (Exception e) // 예외처리
	    {            
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return lectureOutlines; // 검색된 강의개요 반환
	}
	
	/**
	 * LectureOutline DB에서 강의개요 수정
	 * @param lectureOutline
	 * @param key
	 * @return
	 */
	public boolean updateLectureOutlineDB(LectureOutline lectureOutline, String key) {
		pstmt = null;	
		String sql = "update lecture_outline set lectureId=?, professorName=?, title=?, curriculum=?, point=? where lectureId=?";
		try {
			pstmt = conn.prepareStatement(sql);					// 동적 query문
			pstmt.setString(1, lectureOutline.getLectureId()); 	// 학수번호
			pstmt.setString(2, lectureOutline.professorName); 	// 교수명
			pstmt.setString(3, lectureOutline.title); 			// 강의명
			pstmt.setString(4, lectureOutline.curriculum); 		// 교과과정
			pstmt.setString(5, String.valueOf(lectureOutline.getPoint())); // 학점
			pstmt.setString(6, key); 							// primary key
			int n = pstmt.executeUpdate();   					// query문 실행
			if(n<=0){
				System.out.println("insert 실패");	// 예외처리
			}
		} catch (SQLException e) { // 예외처리
			e.printStackTrace();
			return false; // 수정 실패 시 false 반환
		}
		return true; // 수정 성공 시 true 반환
	}
	
	/**
	 * Lecture DB에서 강의개요 삭제
	 * @return
	 */
	public boolean deleteLectureOutlineDB(){
		pstmt = null;	
		String sql = "delete from lecture_outline";
		try {
			pstmt = conn.prepareStatement(sql);	// 동적 query문
			pstmt.executeUpdate();				// query문 실행
		} catch (SQLException e) { // 예외처리
			e.printStackTrace();
			return false; //삭제 실패 시 false 반환
		}
		return true; // 삭제 성공 시 true 반환
	}
	
	/**
	 * LectureOutlineDB에서 1학점이나 2학점짜리 교양과목을 검색 (추천강의)
	 * @return
	 */
	public ArrayList<RecommendedLecture> selectRecommendedLecture(){ 
		ArrayList<RecommendedLecture> recommendedLectures = new ArrayList<RecommendedLecture>(); //결과를 저장할 ArrayList 선언
		pstmt = null;	
		String sql = "select * from lecture_outline where curriculum like '%교양%' && point in (1, 2)"; //query문
		try{	  
			pstmt = conn.prepareStatement(sql); 		// 동적 query문
			ResultSet result = pstmt.executeQuery(sql);	// ResultSet 객체를 통해 query문 결과 처리
			while (result.next()){	// 결과 저장
				RecommendedLecture recommendedLecture = new RecommendedLecture();
				recommendedLecture.setLectureId(result.getString(1)); 	// 학수번호
				recommendedLecture.professorName = result.getString(2); // 교수명
				recommendedLecture.title = result.getString(3);			// 제목
				recommendedLecture.curriculum = result.getString(4);	// 교과과정
				recommendedLecture.setPoint(result.getInt(5));			// 학점
				recommendedLectures.add(recommendedLecture);	// ArrayList에 recommendedLecture 저장
			}
		} 
		catch(SQLException e){ // 예외처리
			e.printStackTrace();
		}
		return recommendedLectures; // 검색된 recommendedLectures 반환
	}
	
	/**
	 * Notification DB에 레코드가 존재하는지 확인
	 * @param lectureId
	 * @param notiId
	 * @return
	 */
	public boolean existsRecordAtNotificationDB(String lectureId, String notiId){
		pstmt = null;
		String sql = "select lectureId, notificationId from notification";
		try{ 
	        pstmt = conn.prepareStatement(sql);  		// 동적 query문
	        ResultSet result = pstmt.executeQuery(sql);	// ResultSet 객체를 통해 query문 결과처리
	        while (result.next()){	// 결과 저장
	        	String LectureId = result.getString(1); // 학수번호 
	        	String NotiId = result.getString(2);	// 공지 Id
	        	
	        	if(LectureId.equals(lectureId) && NotiId.equals(notiId))
	        		return true;  // 해당 공지가 이미 존재하면 true 반환
	        }
		} catch (Exception e){  // 예외처리
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return false; //해당 공지가 존재하지 않으면 false 반환
	}
	
	/**
	 * Assignment DB에 해당 레코드가 존재하는지 확인
	 * @param lectureId
	 * @param assignId
	 * @return
	 */
	public boolean existsRecordAtAssignmentDB(String lectureId, String assignId){
		pstmt = null;
		String sql = "select lectureId, assignId from assignment";
		try{ 
	        pstmt = conn.prepareStatement(sql); 		// 동적 query문
	        ResultSet result = pstmt.executeQuery(sql);	// ResultSet 객체를 통해 query문 결과처리
	        while (result.next()){ // 결과 저장
	        	String LectureId = result.getString(1); // 학수번호
	        	String AssignId = result.getString(2);	// 과제 Id
	            	
	        	if(LectureId.equals(lectureId) && AssignId.equals(assignId)) 
	        		return true;	// 해당 과제가 이미 존재하면 true 반환
	        }
		} catch (Exception e){  // 예외처리 
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return false; // 해당 과제가 존재하지 않으면 false 반환
	}
	
	/**
	 * LectureDB에 해당 레코드가 존재하는지 확인 
	 * @param lectureId
	 * @param userId
	 * @return
	 */
	public boolean existsRecordAtLectureDB(String lectureId, String userId){
		pstmt = null;
		String sql = "select lectureId, userId from Lecture";
		try{ 
	        pstmt = conn.prepareStatement(sql);   		// 동적 query문
	        ResultSet result = pstmt.executeQuery(sql);	// ResultSet 객체를 통해 query문 결과처리 
	        while (result.next()){ // 결과 처리
	        	String LectureId = result.getString(1); // 학수번호
	        	String UserId = result.getString(2);	// UserId
	        	if(LectureId.equals(lectureId) && UserId.equals(userId)) { 
	        		return true;	// 해당 과목이 이미 존재하면 true 반환
	        	}
	        }
		} catch (Exception e){   // 예외처리     
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return false; // 해당 과목이 존재하지 않으면 false 반환
	}
	
	/**
	 * LectureOutline DB에 해당 레코드가 존재하는지 확인
	 * @param lectureId
	 * @return
	 */
	public boolean existsRecordAtLectureOutlineDB(String lectureId){
		pstmt = null;
		String sql = "select lectureId from lectureOutline";
		try{ 
	        pstmt = conn.prepareStatement(sql);  		// 동적 query문
	        ResultSet result = pstmt.executeQuery(sql);	// ResultSet 객체를 통해 query문 결과처리  
	        while (result.next()){	// 결과 저장
	        	String LectureId = result.getString(1); // 학수번호
	        
	        	if(LectureId.equals(lectureId)) 
	        		return true; // 해당 강의개요가 이미 존재하면 true 반환
	        }
		} catch (Exception e){  // 예외처리
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return false; // 해당 강의개요가 존재하지 않으면 false 반환
	}
	
	/**
	 * Question DB에서 해당 레코드가 존재하는지 확인
	 * @param questionId
	 * @return
	 */
	public boolean existsRecordAtQuestionDB(int questionId){
		pstmt = null;
		String sql = "select questionId from question";
		try{ 
	        pstmt = conn.prepareStatement(sql);  		// 동적 query문
	        ResultSet result = pstmt.executeQuery(sql); // ResultSet 객체를 통해 query문 결과처리 
	        while (result.next()){ // 결과 저장
	        	int QuestionId = result.getInt(1);  // 질의 Id
	        
	        	if(QuestionId == questionId) 
	        		return true;	// 해당 질의가 이미 존재하면 true 반환
	        }
		} catch (Exception e){            
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return false;	// 해당 강의개요가 존재하지 않으면 false 반환
	}
	
	/**
	 * Answer DB에서  해당 레코드가 존재하는지 확인
	 * @param answerId
	 * @return
	 */
	public boolean existsRecordAtAnswerDB(int answerId){
		pstmt = null;
		String sql = "select answerId from answer";
		try{ 
	        pstmt = conn.prepareStatement(sql);  		// 동적 query문
	        ResultSet result = pstmt.executeQuery(sql); // ResultSet 객체를 통해 query문 결과처리 
	        while (result.next()){	// 결과 저장
	        	int AnswerId = result.getInt(1);		// 답변 Id 
	        
	        	if(AnswerId == answerId)
	        		return true; // 해당 답변이 이미 존재하면 true 반환
	        }
		} catch (Exception e){   // 예외처리
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return false; // 해당 답변이 존재하지 않으면 false 반환
	}
	
	/**
	 * SubmitDB에 해당 레코드가 존재하는지 확인
	 * @param submitId
	 * @return
	 */
	public boolean existsRecordAtSubmitDB(int submitId){
		pstmt = null;
		String sql = "select submitId from submit";
		try{ 
	        pstmt = conn.prepareStatement(sql);  		// 동적 query문
	        ResultSet result = pstmt.executeQuery(sql); // ResultSet 객체를 통해 query문 결과처리 
	        while (result.next()){	// 결과 저장
	        	int SubmitId = result.getInt(1); // 제출 Id
	        
	        	if(SubmitId == submitId) 
	        		return true; // 해당 제출이 이미 존재하면 true 반환
	        }
		} catch (Exception e){            
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return false; // 해당 제출이 존재하지 않으면 false 반환
	}
	
	/**
	 * Grade DB에서 해당 레코드가 존재하는지 확인
	 * @param gradeId
	 * @return
	 */
	public boolean existsRecordAtGradeDB(int gradeId){
		pstmt = null;
		String sql = "select gradeId from grade";
		try{ 
	        pstmt = conn.prepareStatement(sql);  		// 동적 query문
	        ResultSet result = pstmt.executeQuery(sql); // ResultSet 객체를 통해 query문 결과처리 
	        while (result.next()){ // 결과 저장
	        	int GradeId = result.getInt(1); 
	        
	        	if(GradeId == gradeId) 
	        		return true; // 해당 성적이 이미 존재하면 true 반환
	        }
		} catch (Exception e){   // 예외처리
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return false; // 해당 성적이 존재하지 않으면 false 반환
	}
	
	/**
	 * Version DB에서 해당 레코드가 존재하는지 확인
	 * @param lectureId
	 * @return
	 */
	public boolean existsRecordAtVersion(String lectureId){
		pstmt = null;
		String sql = "select lectureId from version";
		try{ 
	        pstmt = conn.prepareStatement(sql);  // 동적 query문
	        ResultSet result = pstmt.executeQuery(sql); // ResultSet 객체를 통해 query문 결과처리 
	        while (result.next()){ // 결과 저장
	        	String LectureId = result.getString(1);  // 학수번호
	        
	        	if(LectureId.equals(lectureId)) 
	        		return true; // 해당 version이 이미 존재하면 true 반환
	        }
		} catch (Exception e){   // 예외처리         
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return false; // 해당 version이 존재하지 않으면 false 반환
	}
	
	/**
	 * DB와의 연결을 끊고 객체 해체
	 * @return
	 */
	public boolean closeDB(){
		if(pstmt != null) 
			try{
				pstmt.close();	// PreparedStatement 객체 해제
			}catch(SQLException sqle){ // 예외처리
				return false;
			} 
		
		if(conn != null) 
			try{
				conn.close();	// Connection 해제
			}catch(SQLException sqle){ // 예외처리
				return false; 
			} 
		return true;
	}
}