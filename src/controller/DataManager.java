package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.LectureOutline;

public class DataManager {	
	Connection conn = null; 	
	PreparedStatement pstmt = null;
	ResultSet result = null;
		
	/*
	public int getLectureVersion(){
		
		
	}*/
	
	public void openDB(){
		String myUrl = "jdbc:mysql://localhost:3306/eclass"; // 사용하려는 데이터베이스명을 포함한 URL 기술
		String id = "root"; // 사용자 계정
		String pw = "5721"; //사용자 계정의 패스워드
		
		try {
			  Class.forName("com.mysql.jdbc.Driver");  // 데이터베이스와 연동하기 위해 DriverManager에 등록  
		}catch(ClassNotFoundException e){
			  System.out.println("드라이버 검색 실패");
		}

		try{
			conn = DriverManager.getConnection(myUrl, id, pw);  // DriverManager 객체로부터 Connection 객체를 얻어온다
		}catch(SQLException e){
			System.out.println("DB 접속 실패");
		}
	}
	/*
	public ArrayList<ResultSet> SelectLectureDB(){		
		try{ 
	        String SQL = "select * from lecture where student_id"; //데이터베이스 eclass의 테이블 lecture에 레코드 조회 
	        pstmt = conn.prepareStatement(SQL); //질의를 할 Statement 만들기 
	        result = pstmt.executeQuery(SQL);
	        while (result.next())
	        {
	            lecture.add(result);
	        }
	    }
	    catch (Exception e)
	    {            
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return lecture;
	}
	
	
	
	public void SelectDB(String table){		
		try{ 
	        pstmt = conn.prepareStatement("select * from " + table); //질의를 할 Statement 만들기 
	        String SQL = "select * from " + table; //데이터베이스 eclass의 테이블 student에 레코드 조회 
	        result = pstmt.executeQuery(SQL);
	        while (result.next())
	        {
	            System.out.print(result.getString(1) + "\t");
	            System.out.print(result.getString(2) + "\t");
	            System.out.print(result.getInt(3) + "\t");
	            System.out.println(result.getString(4));
	        }
	    }
	    catch (Exception e)
	    {            
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }	
	}
	
	public void InsertDB(String table, String headers, String values){		
		try{	
			pstmt = conn.prepareStatement("insert into" + table + "(" + headers + ") values(?,?)");
			pstmt.setString(1, values.split(",")[0]);
			pstmt.setString(2, values.split(",")[1]);
			int n = pstmt.executeUpdate();   // 쿼리문 실행
			if(n<=0){
				System.out.println("Insert 실패");
			}
		}catch(SQLException e){
			e.printStackTrace();
		}	
	}
	
	public void updateDB(String table, String headers, String values, String target) {
		try {
			pstmt = conn.prepareStatement("update" + table + " set " + headers.split(",")[0]+" =?, " + headers.split(",")[1]+" = ? where " + target.split("=")[0] + " = ?");
			pstmt.setString(1, values.split(",")[0]);
			pstmt.setString(2, values.split(",")[1]);
			pstmt.setString(3, target.split("=")[1]);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void DeleteDB(String table, String target){
		try {
			pstmt = conn.prepareStatement("delete from " + table + " where " + target.split("=")[0] + " =?");
			pstmt.setString(1, target.split("=")[1]);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	*/
	public ResultSet selectLectureOutlineDB(String key){	 //학수번호로 검색	
		pstmt = null;	//동적 query문		
		String sql = "select * from lecture_outline where lectureId=?";
		try{ 
	        pstmt = conn.prepareStatement(sql);  
	        pstmt.setString(1, key);
	        result = pstmt.executeQuery(sql);
	       
	        System.out.println(result.getString(0));
	        System.out.println(result.getString(1));
	        System.out.println(result.getString(2));
	        System.out.println(result.getString(3));
	        System.out.println(result.getString(4));
	        System.out.println(result.getInt(5));
        
	    }
	    catch (Exception e)
	    {            
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
		return result;
	}
	
	public void insertLectureOutlineDB(ArrayList<LectureOutline> lectureOutline){
		pstmt = null;	//동적 query문
		String sql = "insert into lecture_outline VALUES (?, ?, ?, ?, ?);";
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
		}
	}

	public void updateLectureOutlineDB(LectureOutline lectureOutline, String key) {
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
		}
	}
	
	public void deleteLectureOutlineDB(String lectureId) throws Exception {
		pstmt = conn.prepareStatement("delete from lecture_outline where lectureId=?;");
		pstmt.setString(1, lectureId);
		pstmt.executeUpdate();
	}
	
	public void closeDB(){
		if(pstmt != null) 
			try{pstmt.close();}catch(SQLException sqle){} // PreparedStatement 객체 해제
		
		if(conn != null) 
			try{conn.close();}catch(SQLException sqle){} // Connection 해제
	}
}
