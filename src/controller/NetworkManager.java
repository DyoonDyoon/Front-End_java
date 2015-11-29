/**
 * Created by kweonminjun on 2015. 11. 30..
 */
package controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import model.Student;
import model.User;

public class NetworkManager {
	public static final String API_HOST = "http://eclass.feonfun.com";
	public static final String USERS = "/users";

	private String accessToken;
	
	public NetworkManager() {
		accessToken = null;
	}
	
	public User login(String id, String pw) throws Exception {		
//		URL obj = new URL(API_HOST + USERS);
		URL obj = new URL("https://cdn.rawgit.com/MinJunKweon/dbit/master/static/1-v1.json");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		// optional default is GET
		con.setRequestMethod("GET");
		int responseCode = con.getResponseCode();
		Student stu = new Student("", "", "");
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		inputLine = in.readLine();
		in.close();

		System.out.println("Response Code : " + responseCode);
		
		return stu;
	}
}