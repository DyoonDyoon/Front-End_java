package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataManager {	
	Connection conn = null; 	
	PreparedStatement pstmt = null;
	ResultSet result = null;
	
	public void OpenDB(){
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
	
	public void CloseDB(){
		if(pstmt != null) 
			try{pstmt.close();}catch(SQLException sqle){} // PreparedStatement 객체 해제
		
		if(conn != null) 
			try{conn.close();}catch(SQLException sqle){} // Connection 해제
	}
}
