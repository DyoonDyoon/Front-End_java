/**
 *  Created by ChoiJinYoung on 2015. 12. 06..
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
	Connection conn = null; 	
	PreparedStatement pstmt = null;
	
	public ArrayList<RecommendedLecture> selectRecommendedLecture(){
		ArrayList<RecommendedLecture> recommendedLectures = new ArrayList<RecommendedLecture>();
		pstmt = null;	//동적 query문
		String sql = "select * from lecture_outline where curriculum like '%교양%' && point in (1, 2)";
		try{	  
			pstmt = conn.prepareStatement(sql);
			ResultSet result = pstmt.executeQuery(sql);
			while (result.next()){
				RecommendedLecture recommendedLecture = new RecommendedLecture();
				recommendedLecture.setLectureId(result.getString(1));
				recommendedLecture.professorName = result.getString(2);
				recommendedLecture.title = result.getString(3);
				recommendedLecture.curriculum = result.getString(4);
				recommendedLecture.setPoint(result.getInt(5));
				
				recommendedLectures.add(recommendedLecture);
			}
		} 
		catch(SQLException e){
			e.printStackTrace();
		}
		return recommendedLectures;
	}
	
	public boolean existsRecordAtNotificationDB(String lectureId, String notiId){
		pstmt = null;
		String sql = "select lectureId, notificationId from notification";
		try{ 
	        pstmt = conn.prepareStatement(sql);  
	        ResultSet result = pstmt.executeQuery(sql);
	        while (result.next()){
	        	String LectureId = result.getString(1); 
	        	String NotiId = result.getString(2);
	            	
	        	if(LectureId.equals(lectureId) && NotiId.equals(notiId)) //lectureId와 notiId가 이미 존재
	        		return true;
	        }
		} catch (Exception e){            
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return false; //primary key 존재 안함
	}
	
	public boolean existsRecordAtAssignmentDB(String lectureId, String assignId){
		pstmt = null;
		String sql = "select lectureId, assignId from assignment";
		try{ 
	        pstmt = conn.prepareStatement(sql);  
	        ResultSet result = pstmt.executeQuery(sql);
	        while (result.next()){
	        	String LectureId = result.getString(1); 
	        	String AssignId = result.getString(2);
	            	
	        	if(LectureId.equals(lectureId) && AssignId.equals(assignId)) //lectureId와 assignId가 이미 존재
	        		return true;
	        }
		} catch (Exception e){            
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return false; //primary key 존재 안함
	}
	
	public boolean existsRecordAtLectureDB(String lectureId, String userId){
		pstmt = null;
		String sql = "select lectureId, userId from Lecture";
		try{ 
	        pstmt = conn.prepareStatement(sql);  
	        ResultSet result = pstmt.executeQuery(sql);
	        while (result.next()){
	        	String LectureId = result.getString(1); 
	        	String UserId = result.getString(2);
	        	if(LectureId.equals(lectureId) && UserId.equals(userId)) { //lectureId와 userId가 이미 존재
	        		return true;
	        	}
	        }
		} catch (Exception e){            
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return false; //primary key 존재 안함
	}
	
	public boolean existsRecordAtLectureOutlineDB(String lectureId){
		pstmt = null;
		String sql = "select lectureId from lectureOutline";
		try{ 
	        pstmt = conn.prepareStatement(sql);  
	        ResultSet result = pstmt.executeQuery(sql);
	        while (result.next()){
	        	String LectureId = result.getString(1); 
	        
	        	if(LectureId.equals(lectureId)) //lectureId가 이미 존재
	        		return true;
	        }
		} catch (Exception e){            
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return false; //primary key 존재 안함
	}
	
	public boolean existsRecordAtQuestionDB(int questionId){
		pstmt = null;
		String sql = "select questionId from question";
		try{ 
	        pstmt = conn.prepareStatement(sql);  
	        ResultSet result = pstmt.executeQuery(sql);
	        while (result.next()){
	        	int QuestionId = result.getInt(1); 
	        
	        	if(QuestionId == questionId) //questionId가 이미 존재
	        		return true;
	        }
		} catch (Exception e){            
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return false; //primary key 존재 안함
	}
	
	public boolean existsRecordAtAnswerDB(int answerId){
		pstmt = null;
		String sql = "select answerId from answer";
		try{ 
	        pstmt = conn.prepareStatement(sql);  
	        ResultSet result = pstmt.executeQuery(sql);
	        while (result.next()){
	        	int AnswerId = result.getInt(1); 
	        
	        	if(AnswerId == answerId) //answerId가 이미 존재
	        		return true;
	        }
		} catch (Exception e){            
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return false; //primary key 존재 안함
	}
	
	public boolean existsRecordAtSubmitDB(int submitId){
		pstmt = null;
		String sql = "select submitId from submit";
		try{ 
	        pstmt = conn.prepareStatement(sql);  
	        ResultSet result = pstmt.executeQuery(sql);
	        while (result.next()){
	        	int SubmitId = result.getInt(1); 
	        
	        	if(SubmitId == submitId) //submitId가 이미 존재
	        		return true;
	        }
		} catch (Exception e){            
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return false; //primary key 존재 안함
	}
	
	public boolean existsRecordAtGradeDB(int gradeId){
		pstmt = null;
		String sql = "select gradeId from grade";
		try{ 
	        pstmt = conn.prepareStatement(sql);  
	        ResultSet result = pstmt.executeQuery(sql);
	        while (result.next()){
	        	int GradeId = result.getInt(1); 
	        
	        	if(GradeId == gradeId) //gradeId가 이미 존재
	        		return true;
	        }
		} catch (Exception e){            
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return false; //primary key 존재 안함
	}
	
	public boolean existsRecordAtVersion(String lectureId){
		pstmt = null;
		String sql = "select lectureId from version";
		try{ 
	        pstmt = conn.prepareStatement(sql);  
	        ResultSet result = pstmt.executeQuery(sql);
	        while (result.next()){
	        	String LectureId = result.getString(1); 
	        
	        	if(LectureId.equals(lectureId)) //lectureId가 이미 존재
	        		return true;
	        }
		} catch (Exception e){            
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return false; //primary key 존재 안함
	}
	
	public boolean openDB(){
		String myUrl = "jdbc:mysql://localhost:3306/eclass?useUnicode=true&characterEncoding=utf8"; // 사용하려는 데이터베이스명을 포함한 URL 기술
		String id = "root"; // 사용자 계정
		String pw = "5721"; //사용자 계정의 패스워드
//		String myUrl = "jdbc:mysql://localhost:3306/front_eclass?useUnicode=true&characterEncoding=utf8"; // 사용하려는 데이터베이스명을 포함한 URL 기술
//		String id = "root"; // 사용자 계정
//		String pw = "1234"; //사용자 계정의 패스워드
		
		try { 
			  Class.forName("com.mysql.jdbc.Driver"); // 데이터베이스와 연동하기 위해 DriverManager에 등록  
		}catch(ClassNotFoundException e){
			  System.out.println("드라이버 검색 실패");
			  return false;
		}

		try{
			conn = DriverManager.getConnection(myUrl, id, pw); // DriverManager 객체로부터 Connection 객체를 얻어온다
		}catch(SQLException e){
			System.out.println("DB 접속 실패");
			return false;
		}
		return true;
	}
	
	public Version selectVersionDB(String lectureId){
		Version version = null;
		pstmt = null;	//동적 query문		
		String sql = "select * from version where lectureId=\"" + lectureId + "\"";
		try{ 
	        pstmt = conn.prepareStatement(sql);
	        ResultSet result = pstmt.executeQuery(sql); // execute select SQL statement  
	        
	        if (result.next()) {
	        	version = new Version("", 0, 0);
		        version.setLectureId(result.getString(1));
		        version.notiVersion = result.getInt(2);
		        version.assignVersion= result.getInt(3);
	        }
		}
	    catch (Exception e)
	    {            
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return version;
	}
	
	public boolean insertVersionDB(String lectureId){
		pstmt = null;	//동적 query문
		String sql = "insert into version values (?, ?, ?)";
		try{	  
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, lectureId); 			// 학수번호
			pstmt.setInt(2, 0);  
			pstmt.setInt(3, 0); 
			int n = pstmt.executeUpdate();   		// 쿼리문 실행
			if(n<=0){
				System.out.println("insert 실패");
			}
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean updateVersionDB(String lectureId, int notiVersion, int assignVersion){
		int index = 1;
		pstmt = null;	//동적 query문		
		String sql = "update version set ";
		if(notiVersion != -1){
			sql = sql + "notiVer=?";
		}
		if(assignVersion !=-1){
			if(notiVersion != -1){
				sql = sql + ", ";
			}
			sql = sql + "assignVer=?";
		}
		sql = sql + " where lectureId=?";
		System.out.println(sql);
		try{	  
			pstmt = conn.prepareStatement(sql);
			if(notiVersion != -1){
				pstmt.setString(index++, String.valueOf(notiVersion)); 
			}
			if(assignVersion != -1){
				pstmt.setString(index++, String.valueOf(assignVersion));
			}
			pstmt.setString(index, lectureId);	
			int n = pstmt.executeUpdate(); // 쿼리문 실행
			if(n<=0){
				System.out.println("insert 실패");
			}
		} catch (Exception e){            
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}
		
	public ArrayList<Grade> selectGradeDB(String stuId, String lectureId){	  //grade Id로 검색
//		양자택일
//		stuId - 성적을 찾는 학생 아이디 [optional]
//		lectureId - 성적을 가진 강의 아이디 [optional]
		if ((stuId == null && lectureId == null)) {
			System.out.println("Get Grade : at least ONE parameter");
			return null;
		}
		
		ArrayList<Grade> grades =  new ArrayList<Grade>();
		pstmt = null;	//동적 query문		
		String sql = "select * from grade where ";
		if (stuId != null) {
			sql = sql + "stuId=\"" + stuId + "\"";
		}
		if (lectureId != null) {
			if (stuId != null) {
				sql = sql + " && ";
			}
			sql = sql + "lectureId=\"" + lectureId + "\"";
		}
		try{ 
	        pstmt = conn.prepareStatement(sql);  
	        ResultSet result = pstmt.executeQuery(sql);
	        while (result.next()){
	        	Grade grade = new Grade();
	        	grade.setGradeId(result.getInt(1)); //primary key
	        	grade.setLectureId(result.getString(2));
	        	grade.setSubmitId(result.getInt(3));
	        	grade.setStudentId(result.getString(4));
	        	grade.setScore(result.getDouble(5));
	        	
	        	grades.add(grade);
	        }
	    } catch (Exception e){            
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return grades;
	}
	
	public boolean insertGradeDB(int gradeId, String lectureId, int submitId, String studentId, double score){
		pstmt = null;	//동적 query문
		String sql = "insert into grade values (?, ?, ?, ?, ?)";
		try{	  
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, gradeId);				// 학점 Id
			pstmt.setString(2, lectureId); 				// 학수번호
			pstmt.setInt(3, submitId); 				// 제출 Id
			pstmt.setString(4, studentId);				// 학생 Id
			pstmt.setDouble(5, score);	// 점수
			int n = pstmt.executeUpdate();   			// 쿼리문 실행
			if(n<=0){
				System.out.println("insert 실패");
			}
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean updateGradeDB(int gradeId, double score) { //점수 수정. grade Id로 검색
		pstmt = null;	//동적 query문
		String sql = "update grade set score=? where gradeId=?";
		try {
			pstmt = conn.prepareStatement(sql);			
			pstmt.setString(1, String.valueOf(score)); 	// 점수
			pstmt.setInt(2, gradeId); 				// 학점 Id
			int n = pstmt.executeUpdate(); 				// 쿼리문 실행
			if(n<=0){
				System.out.println("insert 실패");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
		
	public boolean deleteGradeDB(int gradeId){ //grade Id로 검색
		pstmt = null;	//동적 query문
		String sql = "delete from grade where gradeId=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, gradeId);
			pstmt.executeUpdate();
		} catch (Exception e) {
	        System.out.println(e.getMessage());
	        e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean deleteAllGradeDB() {
		pstmt = null;	//동적 query문
		String sql = "delete from grade";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (Exception e) {
	        System.out.println(e.getMessage());
	        e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public ArrayList<Submit> selectSubmitDB(String lectureId, int submitId, String stuId){
		ArrayList<Submit> submits =  new ArrayList<Submit>();
		pstmt = null;	//동적 query문		
		String sql = "select * from submit where lectureId=\"" + lectureId + "\" && submitId="+ submitId;
		if (stuId != null) {
			sql = sql + " && stuId=\"" + stuId + "\"";
		}
		try{ 
	        pstmt = conn.prepareStatement(sql);  
	        ResultSet result = pstmt.executeQuery(sql);
	        while (result.next()){
	        	Submit submit = new Submit();
	        	submit.setSubmitId(result.getInt(1)); //primary key
	        	submit.setLectureId(result.getString(2));
	        	submit.setAssignId(result.getInt(3));
	        	submit.setStudentId(result.getString(4));
	        	submit.setFilePath(result.getString(5));
	        		        	
	        	submits.add(submit);
	        }
	    } catch (Exception e){            
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return submits;
	}
	
	public boolean insertSubmitDB(int submitId, String lectureId, int assignId, String studentId, String filePath){
		pstmt = null;	//동적 query문
		String sql = "insert into submit values (?, ?, ?, ?, ?)";
		try{	  
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, submitId); 			// 제출 Id
			pstmt.setString(2, lectureId);
			pstmt.setInt(3, assignId); 			// 과제 Id
			pstmt.setString(4, studentId);			// 학생Id
			pstmt.setString(5, filePath);			// 파일경로
			int n = pstmt.executeUpdate();   		// 쿼리문 실행
			if(n<=0){
				System.out.println("insert 실패");
			}
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean updateSubmitDB(int submitId, String filePath) { //filePath 수정
		pstmt = null;	//동적 query문
		String sql = "update submit set filePath=? where submitId=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, filePath); 			// 수정할 파일경로
			pstmt.setInt(2, submitId); 		// 제출 Id
			int n = pstmt.executeUpdate(); 		// 쿼리문 실행
			if(n<=0){
				System.out.println("insert 실패");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean deleteSubmitDB(int submitId){
		pstmt = null;	//동적 query문
		String sql = "delete from submit where submitId=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, submitId);
			pstmt.executeUpdate();
		} catch (Exception e) {
	        System.out.println(e.getMessage());
	        e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean deleteAllSubmitDB(int submitId){
		pstmt = null;	//동적 query문
		String sql = "delete from submit";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (Exception e) {
	        System.out.println(e.getMessage());
	        e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public ArrayList<Assignment> selectAssignmentDB(String key){	  //assignId로 검색
		ArrayList<Assignment> assignments =  new ArrayList<Assignment>();
		pstmt = null;	//동적 query문		
		String sql = "select * from assignment where lectureId=\"" + key + "\"";
		try{ 
	        pstmt = conn.prepareStatement(sql);  
	        ResultSet result = pstmt.executeQuery(sql);
	        while (result.next()){
	        	Assignment assignment = new Assignment();
	        	assignment.setAssignId(result.getInt(2));
	        	assignment.setLectureId(result.getString(3));
	        	assignment.title = result.getString(4);
	        	assignment.description = result.getString(5);
	        	assignment.filePath = result.getString(6);
	        	assignment.startDate = result.getString(7);
	        	assignment.endDate = result.getString(8);
	        	
	        	assignments.add(assignment);
	        }
	    } catch (Exception e){            
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return assignments;
	}
	
	public boolean insertAssignmentDB(int assignId, String lectureId, String title, String description, String filePath, String startDate, String endDate){
		pstmt = null;	//동적 query문
		String sql = "insert into assignment(assignId, lectureId, title, description, filePath, startDate, endDate)"
				+ "values (?, ?, ?, ?, ?, ?, ?)";
		try{	  
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, assignId); 		// 과제Id
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
	
	public boolean updateAssignmentDB(int assignId, String title, String description) {
		pstmt = null;	//동적 query문
		int index = 1;
		String sql = "update assignment set ";
		if(title != null){
			sql = sql + "title=?";
		}
		if(description != null){
			if(title != null){
				sql = sql+", ";
			}
			sql = sql + "description=?";			
		}
		sql = sql + " where assignId=?";
		try {
			pstmt = conn.prepareStatement(sql);
			if(title != null){
				pstmt.setString(index++, title);	// 수정할 제목
			}
			if(description != null){
				pstmt.setString(index++, description);	// 수정할 내용
			}
			pstmt.setInt(index, assignId); 		// 과제 Id
			int n = pstmt.executeUpdate(); 		// 쿼리문 실행
			if(n<=0){
				System.out.println("insert 실패");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
		
	public boolean deleteAssignmentDB(int assignId, String lectureId){
		pstmt = null;	//동적 query문
		String sql = "delete from assignment where assignId=? && lectureId";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, assignId);
			pstmt.setString(2, lectureId);
			pstmt.executeUpdate();
		} catch (Exception e) {
	        System.out.println(e.getMessage());
	        e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public Answer selectAnswerDB(int key){	  //questionId로 검색
		Answer answer =  new Answer();
		pstmt = null;	//동적 query문		
		String sql = "select * from answer where questionId=\"" + key + "\"";
		try{ 
	        pstmt = conn.prepareStatement(sql);  
	        ResultSet result = pstmt.executeQuery(sql);
	        result.next();
	       
	    	answer.setAnswerId(result.getInt(1)); //primary key
	    	answer.setQuestionId(result.getInt(2));
	    	answer.content = result.getString(3);
	    } catch (Exception e){            
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return answer;
	}
	
	public boolean insertAnswerDB(int answerId, int questionId, String content){
		pstmt = null;	//동적 query문
		String sql = "insert into answer values (?, ?, ?)";
		try{	  
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, answerId); 			// 답변Id
			pstmt.setInt(2, questionId); 		// 학수번호
			pstmt.setString(3, content);			// 내용
			int n = pstmt.executeUpdate();   		// 쿼리문 실행
			if(n<=0){
				System.out.println("insert 실패");
			}
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean updateAnswerDB(int answerId, String content) {
		pstmt = null;	//동적 query문
		String sql = "update answer set content=? where answerId=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, content); 		// 수정할 내용
			pstmt.setInt(2, answerId); 		// 답변 Id
			int n = pstmt.executeUpdate(); 		// 쿼리문 실행
			if(n<=0){
				System.out.println("insert 실패");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
		
	public boolean deleteAnswerDB(int answerId){
		pstmt = null;	//동적 query문
		String sql = "delete from answer where answerId=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, answerId);
			pstmt.executeUpdate();
		} catch (Exception e) {
	        System.out.println(e.getMessage());
	        e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public ArrayList<Question> selectQuestionDB(String stuId, String lectureId){	  //학수번호로 검색
//		양자택일
//		stuId - 질문자 아이디 (학생) [optional]
//		lectureId - 질문한 강의 [optional]
		if ((stuId == null && lectureId == null)) {
			System.out.println("Get Grade : at least ONE parameter");
			return null;
		}
		ArrayList<Question> questions =  new ArrayList<Question>();
		pstmt = null;	//동적 query문		
		String sql = "select * from question where ";
		if (stuId != null) {
			sql = sql + "stuId=\"" + stuId + "\"";
		}
		if (lectureId != null) {
			if (stuId != null) {
				sql = sql + " && ";
			}
			sql = sql + "lectureId=\"" + lectureId + "\"";
		}
		try{ 
	        pstmt = conn.prepareStatement(sql);  
	        ResultSet result = pstmt.executeQuery(sql);
	        while (result.next()){
	        	Question question = new Question();
	        	question.setQuestionId(result.getInt(1));
	        	question.setLectureId(result.getString(2));
	        	question.setStudentId(result.getString(3)); 
	        	question.content = result.getString(4);
	        	
	        	questions.add(question);
	        }
	    } catch (Exception e){            
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return questions;
	}
	
	public boolean insertQuestionDB(int questionId, String lectureId, String studentId, String content){
		pstmt = null;	//동적 query문
		String sql = "insert into question values (?, ?, ?, ?)";
		try{	  
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, questionId); 		// 질의Id
			pstmt.setString(2, lectureId); 			// 학수번호
			pstmt.setString(3, studentId); 			// 학생Id
			pstmt.setString(4, content);			// 내용
			int n = pstmt.executeUpdate();   		// 쿼리문 실행
			if(n<=0){
				System.out.println("insert 실패");
			}
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean updateQuestionDB(int questionId, String content) {
		pstmt = null;	//동적 query문
		String sql = "update question set content=? where questionId=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, content); 		// 수정할 내용
			pstmt.setInt(2, questionId); 	// 질의 Id
			int n = pstmt.executeUpdate(); 		// 쿼리문 실행
			if(n<=0){
				System.out.println("insert 실패");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
		
	public boolean deleteQuestionDB(int questionId){
		pstmt = null;	//동적 query문
		String sql = "delete from question where questionId=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, questionId);
			pstmt.executeUpdate();
		} catch (Exception e) {
	        System.out.println(e.getMessage());
	        e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean deleteAllQuestionDB(){
		pstmt = null;	//동적 query문
		String sql = "delete from question";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (Exception e) {
	        System.out.println(e.getMessage());
	        e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public ArrayList<Notification> selectNotificationDB(String key){	  //학수번호로 검색
		ArrayList<Notification> notifications =  new ArrayList<Notification>();
		pstmt = null;	//동적 query문		
		String sql = "select * from notification where lectureId=\"" + key + "\"";
		try{ 
	        pstmt = conn.prepareStatement(sql);  
	        ResultSet result = pstmt.executeQuery(sql);
	        while (result.next()){
	        	Notification notification = new Notification();
	        	notification.setNotificationId(result.getInt(2));
	        	notification.setLectureId(result.getString(3));
	        	notification.title = result.getString(4); 
	        	notification.description = result.getString(5);
	        	
	        	notifications.add(notification);
	        }
	    } catch (Exception e){            
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return notifications;
	}
	
	public boolean insertNotificationDB(int notiId, String lectureId, String title, String description){
		pstmt = null;	//동적 query문
		String sql = "insert into notification(notificationId, lectureId, title, description) values (?, ?, ?, ?)";
		try{	  
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, notiId); 		// 공지Id
			pstmt.setString(2, lectureId); 		// 학수번호
			pstmt.setString(3, title); 			// 강의명
			pstmt.setString(4, description);	// 내용
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
	
	public boolean updateNotificationDB(int notiId, String lectureId, String title, String description) {
		pstmt = null;	//동적 query문
		int index = 1;
		String sql = "update notification set ";
		if(title != null){
			sql = sql + "title=?";
		}
		if(description != null){
			if(title!=null){
				sql = sql + ", ";
			}
			sql = sql + "description=?";
		}
		sql = sql + "  where notificationId=? && lectureId=?";
		try {
			pstmt = conn.prepareStatement(sql);
			if(title != null){
				pstmt.setString(index++, title); 		// 수정할 제목
			}
			if(description != null){
				pstmt.setString(index++, description); 	// 수정할 내용
			}
			pstmt.setInt(index++, notiId); 		// 공지Id
			pstmt.setString(index++, lectureId); 		// 공지Id
			int n = pstmt.executeUpdate(); 		// 쿼리문 실행
			if(n<=0){
				System.out.println("insert 실패");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
		
	public boolean deleteNotificationDB(int notiId, String lectureId){
		pstmt = null;	//동적 query문
		String sql = "delete from notification where notificationId=? && lectureId=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, notiId);
			pstmt.setString(2, lectureId);
			pstmt.executeUpdate();
		} catch (Exception e) {
	        System.out.println(e.getMessage());
	        e.printStackTrace();
			return false;
		}
		return true;
	}

	public ArrayList<Lecture> selectLectureDB(String key){	  //학수번호로 검색
		ArrayList<Lecture> lectures = new ArrayList<Lecture>();
		pstmt = null;	//동적 query문		
		String sql = "select * from lecture where userId=\"" + key + "\"";
		try{ 
	        pstmt = conn.prepareStatement(sql);  
	        ResultSet result = pstmt.executeQuery(sql);
	        while (result.next()){
	        	Lecture lecture = new Lecture();
	        	//result.getString(1)은 id로 primary key라서 저장안함
                lecture.setLectureId(result.getString(2)); 
                lecture.setUserId(result.getString(3));
     
                lectures.add(lecture);
	        }
	    } catch (Exception e){            
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return lectures;
	}
	
	public boolean insertLectureDB(String lectureId, String userId){
		pstmt = null;	//동적 query문
		String sql = "insert into lecture SET lectureId=?, userId=?";
		try{	  
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, lectureId); //LectureId
			pstmt.setString(2, userId); //UserId
			int n = pstmt.executeUpdate();   // 쿼리문 실행
			if(n<=0){
				System.out.println("insert 실패");
			}
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean deleteLectureDB(String lectureId, String userId){
		pstmt = null;	//동적 query문		
		String sql = "delete from lecture where lectureId=? && userId =?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, lectureId);
			pstmt.setString(2, userId);
			pstmt.executeUpdate();
		} catch (Exception e) {
	        System.out.println(e.getMessage());
	        e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean deleteAllLectureDB() {
		pstmt = null;	//동적 query문		
		String sql = "delete from lecture";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (Exception e) {
	        System.out.println(e.getMessage());
	        e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public LectureOutline selectLectureOutlineDB(String key){	 //학수번호로 검색	
		LectureOutline lectureOutlines = null;
		pstmt = null;	//동적 query문		
		String sql = "select * from lecture_outline where lectureId=\"" + key + "\"";
		ResultSet result;
		try{ 
	        pstmt = conn.prepareStatement(sql);
	        result = pstmt.executeQuery(sql); // execute select SQL statement  
	        
	        if (result.next()) {
		        lectureOutlines = new LectureOutline("", "", "", "", 0);
		        lectureOutlines.setLectureId(result.getString(1));
		        lectureOutlines.professorName = result.getString(2);
		        lectureOutlines.title = result.getString(3);
		        lectureOutlines.curriculum = result.getString(4);
		        lectureOutlines.setPoint(Integer.parseInt((result.getString(5))));
	        }
		}
	    catch (Exception e)
	    {            
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return lectureOutlines;
	}
	
	public boolean insertLectureOutlineDB(ArrayList<LectureOutline> lectureOutline){
		pstmt = null;	//동적 query문
		String sql = "insert into lecture_outline VALUES (?, ?, ?, ?, ?)";
		try{	  
			for (LectureOutline object : lectureOutline) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, object.getLectureId()); //학수번호
				pstmt.setString(2, object.professorName); //교수명
				pstmt.setString(3, object.title); //강의명
				pstmt.setString(4, object.curriculum); //교과과정
				pstmt.setString(5, String.valueOf(object.getPoint())); //학점
				int n = pstmt.executeUpdate();   // 쿼리문 실행
				if(n<=0){
					System.out.println("insert 실패");
				}
			}
		} catch(SQLException e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean updateLectureOutlineDB(LectureOutline lectureOutline, String key) {
		pstmt = null;	//동적 query문
		String sql = "update lecture_outline set lectureId=?, professorName=?, title=?, curriculum=?, point=? where lectureId=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, lectureOutline.getLectureId()); //학수번호
			pstmt.setString(2, lectureOutline.professorName); //교수명
			pstmt.setString(3, lectureOutline.title); //강의명
			pstmt.setString(4, lectureOutline.curriculum); //교과과정
			pstmt.setString(5, String.valueOf(lectureOutline.getPoint())); //학점
			pstmt.setString(6, key); //학점
			int n = pstmt.executeUpdate();   // 쿼리문 실행
			if(n<=0){
				System.out.println("insert 실패");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean deleteLectureOutlineDB(){
		pstmt = null;	//동적 query문
		String sql = "delete from lecture_outline";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean closeDB(){
		if(pstmt != null) // PreparedStatement 객체 해제
			try{
				pstmt.close();
			}catch(SQLException sqle){
				return false;
			} 
		
		if(conn != null) // Connection 해제
			try{
				conn.close();
			}catch(SQLException sqle){
				return false;
			} 
		return true;
	}
}