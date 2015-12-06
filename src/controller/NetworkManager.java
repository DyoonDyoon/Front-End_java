/**
 * Created by kweonminjun on 2015. 11. 30..
 */
package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
	private static final String LECTURE_OUTLINE = "/lecture_outline";
	private static final String CONFIG_FIlE = "./config.json";

	private String accessToken;
	private int version;
	
	public NetworkManager() {
		accessToken = null;
	}
	
	public int needsUpdateLectureOutline() throws IOException{
		File textFile = new File(CONFIG_FIlE);
		JsonObject config = null;
		if (textFile.exists()) {
	    	BufferedReader bufferedReader = new BufferedReader(new FileReader(textFile));	// path
	        String line = "";
	        String fileString = "";
	        while((line = bufferedReader.readLine()) != null) {	// 텍스트파일을 모두 읽어서
	        	// 배열에 저장한다
	        	fileString += line;
	        }
	        bufferedReader.close();
	        config = new JsonParser().parse(fileString).getAsJsonObject();
		}
		JsonObject serverConfig = getLectureOutlineConfig();
		boolean flag = false;
		if (serverConfig != null) {
			if(!textFile.exists()) {
				flag = true;
	        } else {
	        	boolean isUpToDate = config.get("version").getAsInt() != serverConfig.get("version").getAsInt();
		        if (isUpToDate) {
		        	flag = true;
		        }
	        }
	        if (flag) {
				FileOutputStream outputStream = new FileOutputStream(CONFIG_FIlE, false);	// if exist
	            String serverConfigStr = serverConfig.toString();
	            outputStream.write(serverConfigStr.getBytes(), 0, serverConfigStr.getBytes().length);
	            outputStream.close();
	        	return serverConfig.get("version").getAsInt();
	        }
        }
		return -1;
	}
	
	public JsonObject getLectureOutlineConfig() throws IOException {
		String url = API_HOST + LECTURE_OUTLINE;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		// optional default is GET
		con.setRequestMethod("GET");
		int responseCode = con.getResponseCode();
		if (responseCode != 200)
			return null;
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine = in.readLine();
		in.close();
		JsonObject config = new JsonParser().parse(inputLine).getAsJsonObject();
		return config;
	}
	
	public static final String LECTURE_OUTLINE_URL(int version) {
		return "https://rawgit.com/DyoonDyoon/Back-End/master/script/json/" + version + ".json";
	}
	
	public User login(String id, String pw) throws Exception {
		String url = API_HOST + LOGIN;
		String params = "?id=" + id + "&pw=" + pw;
		
		url = url + params;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		
		int responseCode = con.getResponseCode();
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine = in.readLine();
		in.close();
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
	
	public ArrayList<LectureOutline> getLectureOutline(int version) throws IOException {
		URL obj = new URL(LECTURE_OUTLINE_URL(version));
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		// optional default is GET
		con.setRequestMethod("GET");
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine = in.readLine();
		in.close();
		
		
		ArrayList<LectureOutline> lectureOutlines = new ArrayList<LectureOutline>();
		
		JsonArray jsonArray = new JsonParser().parse(inputLine).getAsJsonArray();

		for (JsonElement i : jsonArray) {
			JsonObject object = i.getAsJsonObject();
			LectureOutline lectureOutline = new LectureOutline(object);
			lectureOutlines.add(lectureOutline);
		}
		return lectureOutlines;
	}
}