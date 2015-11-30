package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LectureOutLine {
	  //private Connection connect = null;
	  //private Statement statement = null;
	  //private PreparedStatement preparedStatement = null;
	  //private ResultSet resultSet = null; 
	
	  public static void main(String[] args) throws Exception {
		  try {
			  Class.forName("com.mysql.jdbc.Driver");  // 데이터베이스와 연동하기 위해 DriverManager에 등록  
		  }catch(ClassNotFoundException e){
			  System.out.println("드라이버 검색 실패");
		  }
		  Connection conn = null; 	// null로 초기화
		  String myUrl = "jdbc:mysql://localhost:3306/eclass"; // 사용하려는 데이터베이스명을 포함한 URL 기술
		  String id = "root"; // 사용자 계정
		  String pw = "5721"; //사용자 계정의 패스워드
		  try{
			  conn = DriverManager.getConnection(myUrl, id, pw);  // DriverManager 객체로부터 Connection 객체를 얻어온다
		  }catch(SQLException e){
			  System.out.println("MySQL 접속 실패");
		  }
		 
		  //Statement stmt = null;	  
		  //stmt.executeUpdate("insert into lectureOutLine values('CES2021-01', '프로그래밍언어와 실습', '1-2학기', '2');");
		  PreparedStatement pstmt = null;	//동적 query문
		  String key = "CSE2021-01";
		  String name = "객체지향언어와 실습"; 
		  String course = "1,2학기";
		  String point = "4";
		  String sql = "insert into topic VALUES (?, ?, ?, ?);";
		  //sql = "insert into  (key,name, course, point) VALUES ('CES2021-01', '프로그래밍언어와 실습', '1-2학기', '2');";
		  //sql = "insert into  (key,name, course, point) VALUES ('CES2013-01', '시스템 소프트웨어와 실습', '1-2학기', '3');";
		  //sql = "insert into  (key,name, course, point) VALUES ('CES2020-01', '주니어 디자인 프로젝트', '1-2학기', '1');";
		  try{	  
			  pstmt = conn.prepareStatement(sql);
			  pstmt.setString(1, key);
			  pstmt.setString(2, name);
			  pstmt.setString(3, course);
			  pstmt.setString(4, point);
			  int n = pstmt.executeUpdate();   // 쿼리문 실행
			  if(n<=0){
				  System.out.println("insert 실패");
			  }
		  }catch(SQLException e){
			e.printStackTrace();
		  }finally{
			  if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}   // PreparedStatement 객체 해제
			  if(conn != null) try{conn.close();}catch(SQLException sqle){}     // Connection 해제 
		  }
		  /*
		  ResultSet rs = null;
		  sql = "select * from topic where id = ?";
		  try{
			  pstmt = conn.prepareStatement(sql);    // prepareStatement에서 해당 쿼리문을 미리 컴파일한다. 
			  rs = pstmt.executeQuery();    // 쿼리를 실행하고 결과를 ResultSet 객체에 담는다.
			  
			  // 결과를 한 행씩 돌아가면서 가져온다.
			  while (rs.next()) {
				  int key = rs.getInt("학수번호");
				  String name = rs.getString("강의명");
				  String course = rs.getString("교과과정");
				  int point = rs.getInt("학점");
		         
				  // print the results
				  System.out.format("%d, %s, %s, %s, %s, %d\n", key, name, course, point);
			  }
		  }
		  catch (Exception e)
		  {
			  e.printStackTrace();
		  }finally{
			  if(rs != null) try{rs.close();}catch(SQLException sqle){}         // Resultset 객체 해제
			  if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}   // PreparedStatement 객체 해제
			  if(conn != null) try{conn.close();}catch(SQLException sqle){}     // Connection 해제 
		  }*/
	  }
}