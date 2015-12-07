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

import model.Answer;
import model.Assignment;
import model.Grade;
import model.Lecture;
import model.LectureOutline;
import model.Question;
import model.Student;
import model.Submit;
import model.User;

public class NetworkManager {
//	private static final String API_HOST = "http://localhost:3001";
	
	private static final String API_HOST = "http://www.feonfun.com:8808";
	private static final String LOGIN = "/login";
	private static final String JOIN = "/join";
	private static final String LECTURE_OUTLINE = "/lecture_outline";
	private static final String LECTURE = "/lecture";
	private static final String ANSWER = "/answer";
	private static final String ASSIGNMENT = "/assignment";
	private static final String NOTIFICATION = "/notification";
	private static final String QUESTION = "/question";
	private static final String SUBMIT = "/submit";
	private static final String USER = "/user";
	
	private static final String CONFIG_FIlE = "./config.json";

	private String accessToken;
	
	private static final String LECTURE_OUTLINE_URL(int version) {
		return "https://rawgit.com/DyoonDyoon/Back-End/master/script/json/" + version + ".json";
	}
	
	public NetworkManager() {
		accessToken = null;
	}
	
	private void updateAccessToken(JsonObject response) {
		accessToken = response.get("accessToken").getAsJsonObject().get("token").getAsString();
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
	
	private JsonObject getLectureOutlineConfig() throws IOException {
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
	
	public User login(String id, String pw) throws Exception {
		String url = API_HOST + LOGIN;
		String params = "?userId=" + id + "&password=" + pw;
		
		url = url + params;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		
		int responseCode = con.getResponseCode();
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine = in.readLine();
		in.close();
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			System.out.println(response.get("message").toString());
			return null;
		}
		updateAccessToken(response);
		JsonObject userJson = response.getAsJsonObject("user");
		
		String userId = userJson.get("userId").toString();
		String name = userJson.get("name").toString();
		String major = userJson.get("major").toString();
		Student stu = new Student(userId, name, major);
		return stu;
	}
	
	public boolean join(String id, String pw, String name, String major, int type) throws IOException {
		String url = API_HOST + JOIN;
		String params = "?userId="+id+"&password="+pw+"&name="+name+"&major="+major+"&type="+type;
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
			return false;
		}
		return true;
	}
	
	
	public boolean updateUserInformation() {
		// 미구현
		return false;
	}
	
	public boolean applyLecture(String userId, String lectureId) throws IOException {
		String url = API_HOST + LECTURE;
		String params = "?token="+accessToken+"userId="+userId+"&lectureId="+lectureId;
		url = url + params;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		
		int responseCode = con.getResponseCode();
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine = in.readLine();
		in.close();
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			System.out.println(response.get("message").toString());
			return false;
		}
		return true;
	}
	
	public ArrayList<Lecture> getLectures(String userId) throws IOException {
		String url = API_HOST + LECTURE;
		String params = "?token="+accessToken+"&userId="+userId;
		url = url + params;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		
		int responseCode = con.getResponseCode();
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine = in.readLine();
		in.close();
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			System.out.println(response.get("message").toString());
			return null;
		}

		ArrayList<Lecture> lectures = new ArrayList<Lecture>();
		JsonArray content = response.get("content").getAsJsonArray();
		for (JsonElement e : content) {
			JsonObject lectureJson = e.getAsJsonObject();
			Lecture lecture = new Lecture(lectureJson);
			lectures.add(lecture);
		}

		return lectures;
	}
	
	public boolean dropLecture(String userId, String lectureId) throws IOException {
		String url = API_HOST + LECTURE;
		String params = "?token="+accessToken+"&userId="+userId+"&lectureId="+lectureId;
		url = url + params;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("DELETE");
		
		int responseCode = con.getResponseCode();
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine = in.readLine();
		in.close();
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			System.out.println(response.get("message").toString());
			return false;
		}
		return true;
	}
	
	// notification
	public boolean postNotification(String lectureId, String title, String description) {
		
		return true;
	}
	
	public boolean getNotification(String lectureId, int version) {
		
		return true;
	}
	
	public boolean updateNotification(String lectureId, int notiId, String title, String description) {
		
		return true;
	}
	
	public boolean cancelNotification(String lectureId, int notiId) {
		
		return true;
	}
	

	// asssignment
	public boolean postAssignment(String lectureId, String title, Assignment assignment) {
		
		return true;
	}
	
	public boolean getAssignment(String lectureId, int version) {
		
		return true;
	}
	
	public boolean updateAssignment(String lectureId, int assignId, Assignment assignment) {
		
		return true;
	}
	
	public boolean cancelAssignment(String lectureId, int assignId) {
		
		return true;
	}
	
	// submit
	public boolean submitReport(String lectureId, int assignId, String stuId, String filePath) {
		
		return true;
	}
	
	public ArrayList<Submit> getReport(String lectureId, int assignId, String stuId) {
//		stuId : (학생용) 학생 아이디 넘겨주어 학생의 것만 받아오기 [optional]
		return null;
	}
	
	public boolean updateReport(int submitId, String filePath) {
		
		return true;
	}
	
	public boolean cancelReport(int submitId) {
		
		return true;
	}
	
	// grade
	public boolean giveGrade(String lectureId, int submitId, String stuId, int score) {
		
		return true;
	}
	
	public ArrayList<Grade> getGrade(String stuId, String lectureId) {
//		양자택일
//		stuId - 성적을 찾는 학생 아이디 [optional]
//		lectureId - 성적을 가진 강의 아이디 [optional]
		return null;
	}
	
	public boolean updateGrade(int gradeId, int score) {
		
		return true;
	}
	
	public boolean cancelGrade(int gradeId) {
		
		return true;
	}
	
	// question
	public boolean makeQuestion(String lectureId, String stuId, String content) {
//		lectureId - 질문할 강의
//		stuId - 질문자 아이디 (학생)
//		content - 질문 내용
		return true;
	}
	
	public Question getQuestion(String stuId, String lectureId) {
//		양자택일
//		stuId - 질문자 아이디 (학생) [optional]
//		lectureId - 질문한 강의 [optional]
		return null;
	}
	
	public boolean updateQuestion(int questionId, String content) {
		
		return true;
	}
	
	public boolean removeQuestion(int questionId) {
		
		return true;
	}
	
	// answer
	public boolean makeAnswer(int questionId, String content) {
		
		return true;
	}
	
	public Answer getAnswer(int questionId) {
		
		return null;
	}
	
	public boolean updateAnswer(int answerId, String content) {
		
		return true;
	}
	
	public boolean removeAnswer(int answerId) {
		
		return true;
	}
	
	// outline
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