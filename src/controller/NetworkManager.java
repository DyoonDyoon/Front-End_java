/**
 * Created by kweonminjun on 2015. 11. 30..
 */
package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import model.LectureOutline;
import model.Student;
import model.User;

public class NetworkManager {
	//public static final String API_HOST = "http://localhost:3001";
	
	public static final String API_HOST = "http://www.feonfun.com:8808";
	public static final String LOGIN = "/login";
	public static final String LECTURE_OUTLINE_URL = "https://raw.githubusercontent.com/DyoonDyoon/Back-End/master/script/2015-2.json";

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
	
	public ArrayList<LectureOutline> getLectureOutline() throws IOException {
		URL obj = new URL(LECTURE_OUTLINE_URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		// optional default is GET
		con.setRequestMethod("GET");
		int responseCode = con.getResponseCode();
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine = in.readLine();
		in.close();
		
		ArrayList<LectureOutline> lectureOutlines = new ArrayList<LectureOutline>();
		
		JsonArray jsonArray = new JsonParser().parse(inputLine).getAsJsonArray();

		System.out.println("Response Code : " + responseCode);
		for (JsonElement i : jsonArray) {
			JsonObject object = i.getAsJsonObject();
			LectureOutline lectureOutline = new LectureOutline(object);
			lectureOutlines.add(lectureOutline);
		}
		return lectureOutlines;
	}
}