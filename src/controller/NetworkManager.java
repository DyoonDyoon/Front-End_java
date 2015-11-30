/**
 * Created by kweonminjun on 2015. 11. 30..
 */
package controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import model.Student;
import model.User;

public class NetworkManager {
	//public static final String API_HOST = "http://localhost:3001";
	
	public static final String API_HOST = "http://www.feonfun.com:8808";
	public static final String LOGIN = "/login";

	private String accessToken;
	
	public NetworkManager() {
		accessToken = null;
	}
	
	public User login(String id, String pw) throws Exception {
		String url = API_HOST + LOGIN;
		String params = "?id=" + id + "&pw=" + pw;
		byte[] postData = params.getBytes(StandardCharsets.UTF_8);
		int    postDataLength = postData.length;           
		System.out.println(String.valueOf(postDataLength));

		url = url + params;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		
		int responseCode = con.getResponseCode();
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine = in.readLine();
		in.close();
		System.out.println("Response Code : " + responseCode);
		if (responseCode != 200) {
			JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
			System.out.println(response.get("message").toString());
			return null;
		}
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		JsonObject userJson = response.getAsJsonObject("user");
		
		String userId = userJson.get("userId").toString();
		String name = userJson.get("name").toString();
		String major = userJson.get("major").toString();
		Student stu = new Student(userId, name, major);
		return stu;
	}
	
	public void getLectureOutline() {
		/*
		URL obj = new URL("https://cdn.rawgit.com/MinJunKweon/dbit/master/static/1-v1.json");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		// optional default is GET
		con.setRequestMethod("GET");
		int responseCode = con.getResponseCode();
		Student stu = new Student("", "", "");
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine = in.readLine();
		in.close();
		
		JsonArray jsonArray = new JsonParser().parse(inputLine).getAsJsonArray();

		System.out.println("Response Code : " + responseCode);
		for (JsonElement i : jsonArray) {
			JsonObject o = i.getAsJsonObject();
			System.out.println(o);
		}
		
		return stu;
		*/
	}
}