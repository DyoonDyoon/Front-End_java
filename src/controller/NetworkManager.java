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
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import model.Answer;
import model.Grade;
import model.Lecture;
import model.LectureOutline;
import model.Question;
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
	private static final String GRADE = "/grade";
	private static final String ASSIGNMENT = "/assignment";
	private static final String NOTIFICATION = "/notification";
	private static final String QUESTION = "/question";
	private static final String SUBMIT = "/submit";
	private static final String USER = "/user";
	
	private static final String CONFIG_FIlE = "./config.json";

	private DataManager dataManager = new DataManager();
	private String accessToken;
	
	private static final String LECTURE_OUTLINE_URL(int version) {
		return "https://rawgit.com/DyoonDyoon/Back-End/master/script/json/" + version + ".json";
	}
	
	public NetworkManager() {
		accessToken = null;
		dataManager = new DataManager();
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
		User user = new User(userJson);
		return user;
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
	public boolean postNotification(String lectureId, String title, String description) throws IOException {
		String url = API_HOST + NOTIFICATION;
		String params = "?token="+accessToken+"&lectureId="+lectureId+"&title="+title;
		if (description != null) {
			params = params + "&description=" + description;
		}
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
	
	public boolean syncNotification(String lectureId, int version) throws IOException {
		String url = API_HOST + NOTIFICATION;
		String params = "?token="+accessToken+"&lectureId="+lectureId+"&version="+version;
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
			return false;
		}
		int notiVer = response.get("notiVer").getAsInt();
		JsonArray content = response.get("content").getAsJsonArray();
		dataManager.openDB();
		// notiVer 넣는 구문	
		for (JsonElement e : content) {
			JsonObject json = e.getAsJsonObject();
			int type = json.get("type").getAsInt();
			int notiId;
			String title = json.get("title").getAsString();
			String description = json.get("description").getAsString();
			if (type == 0) {
				// insert
				notiId = json.get("notiId").getAsInt();
				dataManager.insertNotificationDB(notiId, lectureId, title, description);
			} else if (type == 1) {
				// update
				notiId = json.get("targetId").getAsInt();
				dataManager.updateNotificationDB(notiId, lectureId, title, description);
			} else {
				// delete
				notiId = json.get("targetId").getAsInt();
				dataManager.deleteNotificationDB(notiId, lectureId);
			}
		}
		
		dataManager.closeDB();
		return true;
	}

	public boolean updateNotification(String lectureId, int notiId, String title, String description) throws IOException {
		String url = API_HOST + NOTIFICATION;
		String params = "?token="+accessToken+"&lectureId="+lectureId+"&notiId="+notiId;
		if (title != null) {
			params = params + "&title=" + title;
		}
		if (description != null) {
			params = params + "&description=" + description;
		}
		
		url = url + params;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("PUT");
		
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
	
	public boolean cancelNotification(String lectureId, int notiId) throws IOException {
		String url = API_HOST + NOTIFICATION;
		String params = "?token="+accessToken+"&lectureId="+lectureId+"&notiId="+notiId;
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
	

	// asssignment
	public boolean postAssignment(String lectureId, String title, String description, String filePath, String startDate, String endDate) throws IOException {
		String url = API_HOST + ASSIGNMENT;
		String params = "?token="+accessToken+"&lectureId="+lectureId+"&title="+title;
		if (description != null) {
			params = params + "&description=" + description;
		}
		if (filePath != null) {
			params = params + "&filePath=" + filePath;
		}
		if (startDate != null) {
			params = params + "&startDate=" + startDate;
		}
		if (endDate != null) {
			params = params + "&endDate=" + endDate;
		}
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
	
	public boolean syncAssignment(String lectureId, int version) throws IOException {
		String url = API_HOST + ASSIGNMENT;
		String params = "?token="+accessToken+"&lectureId="+lectureId+"&version="+version;
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
			return false;
		}
		int assignVer = response.get("assignVer").getAsInt();
		JsonArray content = response.get("content").getAsJsonArray();
		dataManager.openDB();
		// notiVer 넣는 구문	
		for (JsonElement e : content) {
			JsonObject json = e.getAsJsonObject();
			int type = json.get("type").getAsInt();
			int assignId;
			String title = json.get("title").getAsString();
			String description = json.get("description").getAsString();
			String filePath = json.get("filePath").getAsString();
			String startDate = json.get("startDate").getAsString();
			String endDate = json.get("endDate").getAsString();
			if (type == 0) {
				// insert
				assignId = json.get("assignId").getAsInt();
				dataManager.insertAssignmentDB(assignId, lectureId, title, description, filePath, startDate, endDate);
			} else if (type == 1) {
				// update
				assignId = json.get("targetId").getAsInt();
				dataManager.updateAssignmentDB(assignId, title, description);
			} else {
				// delete
				assignId = json.get("targetId").getAsInt();
				dataManager.deleteAssignmentDB(assignId, lectureId);
			}
		}
		
		dataManager.closeDB();
		return true;
	}
	
	public boolean updateAssignment(String lectureId, int assignId, String title, String description, String filePath, String startDate, String endDate) throws IOException {
		String url = API_HOST + ASSIGNMENT;
		String params = "?token="+accessToken+"&lectureId="+lectureId+"&assignId="+assignId;
		if (title != null) {
			params = params + "&title=" + title;
		}
		if (description != null) {
			params = params + "&description=" + description;
		}
		if (filePath != null) {
			params = params + "&filePath=" + filePath;
		}
		if (startDate != null) {
			params = params + "&startDate=" + startDate;
		}
		if (endDate != null) {
			params = params + "&endDate=" + endDate;
		}
		url = url + params;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("PUT");
		
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
	
	public boolean cancelAssignment(String lectureId, int assignId) throws IOException {
		String url = API_HOST + ASSIGNMENT;
		String params = "?token="+accessToken+"&lectureId="+lectureId+"&assignId="+assignId;
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
	
	// submit
	public boolean submitReport(String lectureId, int assignId, String stuId, String filePath) throws IOException {
		String url = API_HOST + SUBMIT;
		String params = "?token="+accessToken+"&lectureId="+lectureId+"&assignId="+assignId+"&stuId="+stuId;
		if (filePath != null) {
			params = params + "&filePath=" + filePath;
		}
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
	
	public ArrayList<Submit> getReport(String lectureId, int assignId, String stuId) throws IOException {
//		stuId : (학생용) 학생 아이디 넘겨주어 학생의 것만 받아오기 [optional]
		String url = API_HOST + SUBMIT;
		String params = "?token="+accessToken+"&lectureId="+lectureId+"&assignId="+assignId+"&stuId="+stuId;
		url = url + params;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		
		int responseCode = con.getResponseCode();
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine = in.readLine();
		in.close();
		
		ArrayList<Submit> submits = new ArrayList<Submit>();
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			System.out.println(response.get("message").toString());
			return null;
		}
		JsonArray array = response.get("content").getAsJsonArray();
		for (JsonElement e : array) {
			Submit submit = new Submit(e.getAsJsonObject());
			submits.add(submit);
		}
		return submits;
	}
	
	public boolean updateReport(int submitId, String filePath) throws IOException {
		String url = API_HOST + SUBMIT;
		String params = "?token="+accessToken+"&submitId="+submitId+"&filePath="+filePath;
		url = url + params;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("PUT");
		
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
	
	public boolean cancelReport(int submitId) throws IOException {
		String url = API_HOST + SUBMIT;
		String params = "?token="+accessToken+"&submitId="+submitId;
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
	
	// grade
	public boolean giveGrade(String lectureId, int submitId, String stuId, double score) throws IOException {
		String url = API_HOST + GRADE;
		
		String params = "?token="+accessToken+"&lectureId="+lectureId+"&submitId"+submitId+"&stuId="+stuId;
		if(score != -1) {
			params = params + "&score=" + score;
		}
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
	
	public ArrayList<Grade> getGrade(String stuId, String lectureId) throws IOException {
//		양자택일
//		stuId - 성적을 찾는 학생 아이디 [optional]
//		lectureId - 성적을 가진 강의 아이디 [optional]
		if ((stuId == null && lectureId == null)) {
			System.out.println("Get Grade : at least ONE parameter");
			return null;
		}
		String url = API_HOST + GRADE;
		String params = "?token="+accessToken;
		if (lectureId != null) {
			params = params + "&lectureId=" + lectureId;
		}
		if(stuId != null) {
			params = params + "&stuId" + stuId;
		}
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
		ArrayList<Grade> grades = new ArrayList<Grade>();
		JsonArray array = response.get("content").getAsJsonArray();
		for (JsonElement json : array) {
			Grade grade = new Grade(json.getAsJsonObject());
			grades.add(grade);
		}
		return grades;
	}
	
	public boolean updateGrade(int gradeId, double score) throws IOException {
		String url = API_HOST + GRADE;
		String params = "?token="+accessToken+"&gradeId="+gradeId+"&score="+score;
		url = url + params;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("PUT");
		
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
	
	public boolean cancelGrade(double gradeId) throws IOException {
		String url = API_HOST + GRADE;
		String params = "?token="+accessToken+"&gradeId="+gradeId;
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
	
	// question
	public boolean makeQuestion(String lectureId, String stuId, String content) throws IOException {
		String url = API_HOST + QUESTION;
		String params = "?token="+accessToken+"&lectureId="+lectureId+"&stuId="+stuId+"&content="+content;
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
	
	public ArrayList<Question> getQuestion(String stuId, String lectureId) throws IOException {
//		양자택일
//		stuId - 질문자 아이디 (학생) [optional]
//		lectureId - 질문한 강의 [optional]
		String url = API_HOST + QUESTION;
		String params = "?token="+accessToken;
		if (stuId != null) {
			params = params + "&stuId="+stuId;
		}
		if (lectureId != null) {
			params = params + "&lectureId="+lectureId;
		}
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
		ArrayList<Question> questions = new ArrayList<Question>();
		JsonArray array = response.get("content").getAsJsonArray();
		for (JsonElement json : array) {
			Question question = new Question(json.getAsJsonObject());
			questions.add(question);
		}
		return questions;
	}
	
	public boolean updateQuestion(int questionId, String content) throws IOException {
		String url = API_HOST + QUESTION;
		String params = "?token="+accessToken+"&questionId="+questionId+"&content="+content;
		url = url + params;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("PUT");
		
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
	
	public boolean removeQuestion(int questionId) throws IOException {
		String url = API_HOST + QUESTION;
		String params = "?token="+accessToken+"&questionId="+questionId;
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
	
	// answer
	public boolean makeAnswer(int questionId, String content) throws IOException {
		String url = API_HOST + ANSWER;
		String params = "?token="+accessToken+"&questionId="+questionId+"&content="+content;
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
	
	public Answer getAnswer(int questionId) throws IOException {
		String url = API_HOST + ANSWER;
		String params = "?token="+accessToken+"&questionId="+questionId;
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
		JsonElement content = response.get("content");
		if (content.getAsString().equals("No answer")) {
			System.out.println("get answer : no answer");
			return null;
		}
		Answer answer = new Answer(content.getAsJsonObject());
		return answer;
	}
	
	public boolean updateAnswer(int answerId, String content) throws IOException {
		String url = API_HOST + ANSWER;
		String params = "?token="+accessToken+"&answerId="+answerId+"&content="+content;
		url = url + params;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("PUT");
		
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
	
	public boolean removeAnswer(int answerId) throws IOException {
		String url = API_HOST + ANSWER;
		String params = "?token="+accessToken+"&answerId="+answerId;
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