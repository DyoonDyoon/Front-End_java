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
import model.Submit;
import model.Version;

public class DataManager {	
	Connection conn = null; 	
	PreparedStatement pstmt = null;
			
	public boolean openDB(){
		String myUrl = "jdbc:mysql://localhost:3306/eclass"; // 사용하려는 데이터베이스명을 포함한 URL 기술
		String id = "root"; // 사용자 계정
		String pw = "5721"; //사용자 계정의 패스워드
		
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
		Version version = new Version("", 0, 0);
		pstmt = null;	//동적 query문		
		String sql = "select * from lecture_outline where lectureId=\"" + lectureId + "\"";
		ResultSet result;
		try{ 
	        pstmt = conn.prepareStatement(sql);
	        result = pstmt.executeQuery(sql); // execute select SQL statement  
	        result.next();	// 시작
	        
	        version.setLectureId(result.getString(1));
	        version.notiVersion = Integer.parseInt((result.getString(2)));
	        version.assignVersion= Integer.parseInt(result.getString(3));
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
		String sql = "insert into notification values (?, ?, ?)";
		try{	  
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, lectureId); 			// 학수번호
			pstmt.setString(2, String.valueOf(0)); 	// 
			pstmt.setString(3, String.valueOf(0)); 	// 강의명
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
		// 0인 column은 update를 하지 않음
		
			
		}
		
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
	        	//notification.setNotificationId(result.getString(1));는 공지 Id라서 뺌
	        	notification.setLectureId(result.getString(2));
	        	notification.title = result.getString(3); 
	        	notification.description = result.getString(4);
	        	
	        	notifications.add(notification);
	        }
	    } catch (Exception e){            
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return notifications;
	}
	
	public boolean insertNotificationDB(String notiId, String lectureId, String title, String description){
		pstmt = null;	//동적 query문
		String sql = "insert into notification values (?, ?, ?, ?)";
		try{	  
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, notiId); 		// 공지Id
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
	
	public boolean updateNotificationDB(String notiId, String lectureId, String title, String description) {
		pstmt = null;	//동적 query문
		String sql = "update notification set title=?, description=? where notificationId=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title); 			// 수정할 제목
			pstmt.setString(2, description); 	// 수정할 내용
			pstmt.setString(3, notiId); 		// 공지Id
			pstmt.setString(4, lectureId); 		// 학수번호
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
		
	public boolean deleteNotificationDB(String notiId){
		pstmt = null;	//동적 query문
		String sql = "delete from notification where notificationId=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, notiId);
			pstmt.executeUpdate();
		} catch (Exception e) {
	        System.out.println(e.getMessage());
	        e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public ArrayList<Grade> selectGradeDB(String key){	  //grade Id로 검색
		ArrayList<Grade> grades =  new ArrayList<Grade>();
		pstmt = null;	//동적 query문		
		String sql = "select * from grade where gradeId=\"" + key + "\"";
		try{ 
	        pstmt = conn.prepareStatement(sql);  
	        ResultSet result = pstmt.executeQuery(sql);
	        while (result.next()){
	        	Grade grade = new Grade();
	        	grade.setGradeId(result.getInt(1)); //primary key
	        	grade.setLectureId(result.getString(2));
	        	grade.setSubmitId(result.getInt(3));
	        	grade.setStudentId(result.getString(4));
	        	grade.setScore(Integer.parseInt((result.getString(5))));
	        	
	        	grades.add(grade);
	        }
	    } catch (Exception e){            
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return grades;
	}
	
	public boolean insertGradeDB(String gradeId, String lectureId, String submitId, String studentId, double score){
		pstmt = null;	//동적 query문
		String sql = "insert into grade values (?, ?, ?, ?, ?)";
		try{	  
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, gradeId);				// 학점 Id
			pstmt.setString(2, lectureId); 				// 학수번호
			pstmt.setString(3, submitId); 				// 제출 Id
			pstmt.setString(4, studentId);				// 학생 Id
			pstmt.setString(5, String.valueOf(score));	// 점수
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
	
	public boolean updateGradeDB(String gradeId, double score) { //점수 수정. grade Id로 검색
		pstmt = null;	//동적 query문
		String sql = "update grade set score=? where gradeId=?";
		try {
			pstmt = conn.prepareStatement(sql);			
			pstmt.setString(1, String.valueOf(score)); 	// 점수
			pstmt.setString(2, gradeId); 				// 학점 Id
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
		
	public boolean deleteGradeDB(String gradeId){ //grade Id로 검색
		pstmt = null;	//동적 query문
		String sql = "delete from grade where gradeId=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, gradeId);
			pstmt.executeUpdate();
		} catch (Exception e) {
	        System.out.println(e.getMessage());
	        e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public ArrayList<Submit> selectSubmitDB(String key){	  //submitId로 검색
		ArrayList<Submit> submits =  new ArrayList<Submit>();
		pstmt = null;	//동적 query문		
		String sql = "select * from submit where submitId=\"" + key + "\"";
		try{ 
	        pstmt = conn.prepareStatement(sql);  
	        ResultSet result = pstmt.executeQuery(sql);
	        while (result.next()){
	        	Submit submit = new Submit();
	        	submit.setSubmitId(result.getInt(1)); //primary key
	        	submit.setAssignId(result.getInt(2));
	        	submit.setStudentId(result.getString(3));
	        	submit.setFilePath(result.getString(4));
	        		        	
	        	submits.add(submit);
	        }
	    } catch (Exception e){            
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return submits;
	}
	
	public boolean insertSubmitDB(String submitId, String assignId, String studentId, String filePath){
		pstmt = null;	//동적 query문
		String sql = "insert into submit values (?, ?, ?, ?)";
		try{	  
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, submitId); 			// 제출 Id
			pstmt.setString(2, assignId); 			// 과제 Id
			pstmt.setString(3, studentId);			// 학생Id
			pstmt.setString(4, filePath);			// 파일경로
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
	
	public boolean updateSubmitDB(String submitId, String filePath) { //filePath 수정
		pstmt = null;	//동적 query문
		String sql = "update submit set filePath=? where submitId=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, filePath); 			// 수정할 파일경로
			pstmt.setString(2, submitId); 		// 제출 Id
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
	
	public boolean deleteSubmitDB(String submitId){
		pstmt = null;	//동적 query문
		String sql = "delete from submit where submitId=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, submitId);
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
		String sql = "select * from assignment where assignId=\"" + key + "\"";
		try{ 
	        pstmt = conn.prepareStatement(sql);  
	        ResultSet result = pstmt.executeQuery(sql);
	        while (result.next()){
	        	Assignment assignment = new Assignment();
	        	assignment.setAssignId(result.getString(1)); //primary key
	        	assignment.setLectureId(result.getString(2));
	        	assignment.title = result.getString(3);
	        	assignment.description = result.getString(4);
	        	assignment.filePath = result.getString(5);
	        	assignment.startDate = result.getString(6);
	        	assignment.endDate = result.getString(7);
	        	
	        	assignments.add(assignment);
	        }
	    } catch (Exception e){            
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return assignments;
	}
	
	public boolean insertAssignmentDB(String assignId, String lectureId, String title, String description, String filePath, String startDate, String endDate){
		pstmt = null;	//동적 query문
		String sql = "insert into assignment values (?, ?, ?, ?, ?, ?, ?)";
		try{	  
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, assignId); 		// 과제Id
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
	
	//파일경로랑 시작일 마감일은 수정안하는 거지?
	public boolean updateAssignmentDB(String assignId, String title, String description) {
		pstmt = null;	//동적 query문
		String sql = "update assignment set title=?, description=? where assignId=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title); 			// 수정할 제목
			pstmt.setString(2, description); 	// 수정할 내용
			pstmt.setString(3, assignId); 		// 과제 Id
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
		
	public boolean deleteAssignmentDB(String assignId){
		pstmt = null;	//동적 query문
		String sql = "delete from assignment where assignId=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, assignId);
			pstmt.executeUpdate();
		} catch (Exception e) {
	        System.out.println(e.getMessage());
	        e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public ArrayList<Answer> selectAnswerDB(String key){	  //answerId로 검색
		ArrayList<Answer> answers =  new ArrayList<Answer>();
		pstmt = null;	//동적 query문		
		String sql = "select * from answer where answerId=\"" + key + "\"";
		try{ 
	        pstmt = conn.prepareStatement(sql);  
	        ResultSet result = pstmt.executeQuery(sql);
	        while (result.next()){
	        	Answer answer = new Answer();
	        	answer.setAnswerId(result.getInt(1)); //primary key
	        	answer.setQuestionId(result.getInt(2));
	        	answer.content = result.getString(3);
	        	
	        	answers.add(answer);
	        }
	    } catch (Exception e){            
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return answers;
	}
	
	public boolean insertAnswerDB(String answerId, String questionId, String content){
		pstmt = null;	//동적 query문
		String sql = "insert into answer values (?, ?, ?)";
		try{	  
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, answerId); 			// 답변Id
			pstmt.setString(2, questionId); 		// 학수번호
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
	
	public boolean updateAnswerDB(String answerId, String content) {
		pstmt = null;	//동적 query문
		String sql = "update answer set content=? where answerId=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, content); 		// 수정할 내용
			pstmt.setString(2, answerId); 		// 답변 Id
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
		
	public boolean deleteAnswerDB(String answerId){
		pstmt = null;	//동적 query문
		String sql = "delete from answer where answerId=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, answerId);
			pstmt.executeUpdate();
		} catch (Exception e) {
	        System.out.println(e.getMessage());
	        e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public ArrayList<Question> selectQuestionDB(String key){	  //학수번호로 검색
		ArrayList<Question> questions =  new ArrayList<Question>();
		pstmt = null;	//동적 query문		
		String sql = "select * from question where lectureId=\"" + key + "\"";
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
	
	public boolean insertQuestionDB(String questionId, String lectureId, String studentId, String content){
		pstmt = null;	//동적 query문
		String sql = "insert into question values (?, ?, ?, ?)";
		try{	  
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, questionId); 		// 질의Id
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
	
	public boolean updateQuestionDB(String questionId, String content) {
		pstmt = null;	//동적 query문
		String sql = "update question set content=? where questionId=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, content); 		// 수정할 내용
			pstmt.setString(2, questionId); 	// 질의 Id
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
		
	public boolean deleteQuestionDB(String questionId){
		pstmt = null;	//동적 query문
		String sql = "delete from question where questionId=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, questionId);
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
		String sql = "select * from lecture where lectureId=\"" + key + "\"";
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
	
	public LectureOutline selectLectureOutlineDB(String key){	 //학수번호로 검색	
		LectureOutline lectureOutlines = null;
		pstmt = null;	//동적 query문		
		String sql = "select * from lecture_outline where lectureId=\"" + key + "\"";
		ResultSet result;
		try{ 
	        pstmt = conn.prepareStatement(sql);
	        result = pstmt.executeQuery(sql); // execute select SQL statement  
	        result.next();	// 시작
	        
	        lectureOutlines = new LectureOutline("", "", "", "", 0);
	        lectureOutlines.setLectureId(result.getString(1));
	        lectureOutlines.professorName = result.getString(2);
	        lectureOutlines.title = result.getString(3);
	        lectureOutlines.curriculum = result.getString(4);
	        lectureOutlines.setPoint(Integer.parseInt((result.getString(5))));
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
	
	public boolean deleteLectureOutlineDB(String lectureId){
		pstmt = null;	//동적 query문
		String sql = "delete from lecture_outline where lectureId=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, lectureId);
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