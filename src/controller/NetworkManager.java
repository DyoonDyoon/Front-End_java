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
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.swing.JOptionPane;

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
import model.Version;
import view.LoginPage;
import view.MainPage;

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
//	private static final String USER = "/user";
	
	private static final String CONFIG_FIlE = "./config.json";

	private DataManager dataManager = new DataManager();
	private String accessToken;
	LoginPage loginpage;
	MainPage mainpage;
	
	private static final String LECTURE_OUTLINE_URL(int version) {
		return "https://rawgit.com/DyoonDyoon/Back-End/master/script/json/" + version + ".json";
	}
	
	public NetworkManager(LoginPage loginpage, MainPage mainpage) {
		accessToken = null;
		dataManager = new DataManager();
		this.loginpage = loginpage;
		this.mainpage = mainpage;
	}
	
	private void updateAccessToken(JsonObject response) {
		accessToken = response.get("accessToken").getAsJsonObject().get("token").getAsString();
	}
	
	public int needsUpdateLectureOutline() {
		File textFile = new File(CONFIG_FIlE);
		JsonObject config = null;
		if (textFile.exists()) {
			String fileString = "";
			try {
		    	BufferedReader bufferedReader = new BufferedReader(new FileReader(textFile));	// path
		        String line = "";
		        while((line = bufferedReader.readLine()) != null) {	// 텍스트파일을 모두 읽어서
		        	// 배열에 저장한다
		        	fileString += line;
		        }
		        bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
				return -1;
			}
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
	        	try {
					FileOutputStream outputStream = new FileOutputStream(CONFIG_FIlE, false);	// if exist
		            String serverConfigStr = serverConfig.toString();
		            outputStream.write(serverConfigStr.getBytes(), 0, serverConfigStr.getBytes().length);
		            outputStream.close();
	        	} catch (IOException e) {
	        		e.printStackTrace();
	        		return -1;
	        	}
	        	return serverConfig.get("version").getAsInt();
	        }
        }
		return -1;
	}
	
	private JsonObject getLectureOutlineConfig() {
		int responseCode = 0;
		String inputLine = "";
		String url = API_HOST + LECTURE_OUTLINE;
		try {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		// optional default is GET
		con.setRequestMethod("GET");
		responseCode = con.getResponseCode();
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		inputLine = in.readLine();
		in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		if (responseCode != 200)
			return null;
		
		JsonObject config = new JsonParser().parse(inputLine).getAsJsonObject();
		return config;
	}
	
	public User login(String id, String pw) {
		int responseCode = 0;
		String inputLine = "";
		String url = API_HOST + LOGIN;
		String params = "?userId=" + id + "&password=" + pw;
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			
			responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			int errorCode = 0;
			if (!response.get("code").isJsonNull())
				errorCode = response.get("code").getAsInt();
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return null;
		}
		updateAccessToken(response);
		JsonObject userJson = response.getAsJsonObject("user");
		User user = new User(userJson);
		return user;
	}
	
	public boolean join(String id, String pw, String name, String major, int type) {
		int responseCode = 0;
		String inputLine = "";
		String url = API_HOST + JOIN;
		String params = "?userId="+id+"&password="+pw+"&name="+name+"&major="+major+"&type="+type;
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			
			responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
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
	
	public boolean applyLecture(String userId, String lectureId) {
		int responseCode = 0;
		String inputLine = "";
		String url = API_HOST + LECTURE;
		String params = "?token="+accessToken+"&userId="+userId+"&lectureId="+lectureId;
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			
			responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			int errorCode = 0;
			if (!response.get("code").isJsonNull())
				errorCode = response.get("code").getAsInt();
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}
		dataManager.openDB();
		if (!dataManager.existsRecordAtVersion(lectureId)) {	// 버전테이블이 없을 경우 생성
			dataManager.insertVersionDB(lectureId);
		}
		dataManager.insertLectureDB(lectureId, userId);
		dataManager.closeDB();
		return true;
	}
	
	public boolean syncLectures(String userId) {
		int responseCode = 0;
		String inputLine = "";
		String url = API_HOST + LECTURE;
		String params = "?token="+accessToken+"&userId="+userId;
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			
			responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			int errorCode = 0;
			if (!response.get("code").isJsonNull())
				errorCode = response.get("code").getAsInt();
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}

		JsonArray content = response.get("content").getAsJsonArray();
		dataManager.openDB();
		if (content.size() < dataManager.selectLectureDB(userId).size()) {
			dataManager.deleteAllLectureDB();
		}
		for (JsonElement e : content) {
			JsonObject lectureJson = e.getAsJsonObject();
			Lecture lecture = new Lecture(lectureJson);
			if (!dataManager.existsRecordAtLectureDB(lecture.getLectureId(), lecture.getUserId())) {	// 없는 레코드일 경우
				if (!dataManager.existsRecordAtVersion(lecture.getLectureId())) {	// 버전테이블이 없을 경우 생성
					dataManager.insertVersionDB(lecture.getLectureId());
				}
				dataManager.insertLectureDB(lecture.getLectureId(), lecture.getUserId());
			}
		}
		dataManager.closeDB();
		return true;
	}
	
	public boolean dropLecture(String userId, String lectureId) {
		int responseCode = 0;
		String inputLine = "";
		String url = API_HOST + LECTURE;
		String params = "?token="+accessToken+"&userId="+userId+"&lectureId="+lectureId;
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("DELETE");
			
			responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			int errorCode = 0;
			if (!response.get("code").isJsonNull())
				errorCode = response.get("code").getAsInt();
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}
		dataManager.openDB();
		dataManager.deleteLectureDB(lectureId, userId);
		dataManager.closeDB();
		return true;
	}
	
	// notification
	public boolean postNotification(String lectureId, String title, String description) {
		int responseCode = 0;
		String inputLine = "";
		String url = API_HOST + NOTIFICATION;
		String params = "?token="+accessToken+"&lectureId="+lectureId;
		
		try {
			params = params + "&title="+ URLEncoder.encode(title, "UTF-8");
			if (description != null) {
				params = params + "&description=" + URLEncoder.encode(description, "UTF-8");
			}
			url = url + params;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			
			responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			int errorCode = 0;
			if (!response.get("code").isJsonNull())
				errorCode = response.get("code").getAsInt();
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}
		dataManager.openDB();
		Version version = dataManager.selectVersionDB(lectureId);
		dataManager.closeDB();
		syncNotification(version.getLectureId(), version.notiVersion);
		return true;
	}
	
	public boolean syncNotification(String lectureId, int version) {
		int responseCode = 0;
		String inputLine = "";
		String url = API_HOST + NOTIFICATION;
		String params = "?token="+accessToken+"&lectureId="+lectureId+"&version="+version;
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			
			responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();

		if (responseCode != 200) {
			int errorCode = 0;
			if (!response.get("code").isJsonNull())
				errorCode = response.get("code").getAsInt();
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}
		int notiVer = response.get("notiVer").getAsInt();
		JsonArray content = response.get("content").getAsJsonArray();
		dataManager.openDB();
		dataManager.updateVersionDB(lectureId, notiVer, -1);
		for (JsonElement e : content) {
			JsonObject json = e.getAsJsonObject();
			int type = json.get("type").getAsInt();
			int notiId;
			String title = null;
			String description = null;
			if (!json.get("title").isJsonNull()) {
				title = json.get("title").getAsString();
			}
			if (!json.get("description").isJsonNull()) {
				description = json.get("description").getAsString();
			}
			
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

	public boolean updateNotification(String lectureId, int notiId, String title, String description) {
		int responseCode = 0;
		String inputLine = "";
		String url = API_HOST + NOTIFICATION;
		String params = "?token="+accessToken+"&lectureId="+lectureId+"&notiId="+notiId;
		
		try {
			if (title != null) {
				params = params + "&title=" + URLEncoder.encode(title, "UTF-8");
			}
			if (description != null) {
				params = params + "&description=" + URLEncoder.encode(description, "UTF-8");
			}
			url = url + params;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("PUT");
			
			responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			int errorCode = 0;
			if (!response.get("code").isJsonNull())
				errorCode = response.get("code").getAsInt();
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}
		dataManager.openDB();
		Version version = dataManager.selectVersionDB(lectureId);
		dataManager.closeDB();
		syncNotification(version.getLectureId(), version.notiVersion);
		return true;
	}
	
	public boolean cancelNotification(String lectureId, int notiId) {
		int responseCode = 0;
		String inputLine = "";
		String url = API_HOST + NOTIFICATION;
		String params = "?token="+accessToken+"&lectureId="+lectureId+"&notiId="+notiId;
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("DELETE");
			
			responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			int errorCode = 0;
			if (!response.get("code").isJsonNull())
				errorCode = response.get("code").getAsInt();
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}
		dataManager.openDB();
		Version version = dataManager.selectVersionDB(lectureId);
		dataManager.closeDB();
		syncNotification(version.getLectureId(), version.notiVersion);
		return true;
	}
	

	// asssignment
	public boolean postAssignment(String lectureId, String title, String description, String filePath, String startDate, String endDate) {
		int responseCode = 0;
		String inputLine = "";
		String url = API_HOST + ASSIGNMENT;
		String params = "?token="+accessToken+"&lectureId="+lectureId;
		
		try {
			params = params + "&title="+URLEncoder.encode(title, "UTF-8");
			if (description != null) {
				params = params + "&description=" + URLEncoder.encode(description, "UTF-8");
			}
			if (filePath != null) {
				params = params + "&filePath=" + URLEncoder.encode(filePath, "UTF-8");
			}
			if (startDate != null) {
				params = params + "&startDate=" + URLEncoder.encode(startDate, "UTF-8");
			}
			if (endDate != null) {
				params = params + "&endDate=" + URLEncoder.encode(endDate, "UTF-8");
			}
			url = url + params;
			
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			
			responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			int errorCode = 0;
			if (!response.get("code").isJsonNull())
				errorCode = response.get("code").getAsInt();
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}

		dataManager.openDB();
		Version version = dataManager.selectVersionDB(lectureId);
		dataManager.closeDB();
		syncAssignment(version.getLectureId(), version.assignVersion);
		return true;
	}
	
	public boolean syncAssignment(String lectureId, int version) {
		int responseCode = 0;
		String inputLine = "";
		String url = API_HOST + ASSIGNMENT;
		String params = "?token="+accessToken+"&lectureId="+lectureId+"&version="+version;
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			
			responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			int errorCode = 0;
			if (!response.get("code").isJsonNull())
				errorCode = response.get("code").getAsInt();
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}
		int assignVer = response.get("assignVer").getAsInt();
		JsonArray content = response.get("content").getAsJsonArray();
		dataManager.openDB();
		dataManager.updateVersionDB(lectureId, -1, assignVer);
		for (int i = 0; i < content.size(); ++i) {
			JsonObject json = content.get(i).getAsJsonObject();
			int type = json.get("type").getAsInt();
			int assignId;
			String title = null;
			String description = null;
			String filePath = null;
			String startDate = null;
			String endDate = null;
			if (!json.get("title").isJsonNull()) {
				title = json.get("title").getAsString();
			}
			if (!json.get("description").isJsonNull()) {
				description = json.get("description").getAsString();
			}
			if (!json.get("filePath").isJsonNull()) {
				filePath = json.get("filePath").getAsString();
			}
			if (!json.get("startDate").isJsonNull()) { 
				startDate = json.get("startDate").getAsString();
			}
			if (!json.get("endDate").isJsonNull()) {
				endDate = json.get("endDate").getAsString();
			}
			
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
	
	public boolean updateAssignment(String lectureId, int assignId, String title, String description, String filePath, String startDate, String endDate) {
		int responseCode = 0;
		String inputLine = "";
		String url = API_HOST + ASSIGNMENT;
		String params = "?token="+accessToken+"&lectureId="+lectureId+"&assignId="+assignId;
		try {
			if (title != null) {
				params = params + "&title=" + URLEncoder.encode(title, "UTF-8");
			}
			if (description != null) {
				params = params + "&description=" + URLEncoder.encode(description, "UTF-8");
			}
			if (filePath != null) {
				params = params + "&filePath=" + URLEncoder.encode(filePath, "UTF-8");
			}
			if (startDate != null) {
				params = params + "&startDate=" + URLEncoder.encode(startDate, "UTF-8");
			}
			if (endDate != null) {
				params = params + "&endDate=" + URLEncoder.encode(endDate, "UTF-8");
			}
			url = url + params;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("PUT");
			
			responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			int errorCode = 0;
			if (!response.get("code").isJsonNull())
				errorCode = response.get("code").getAsInt();
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}

		dataManager.openDB();
		Version version = dataManager.selectVersionDB(lectureId);
		dataManager.closeDB();
		syncAssignment(version.getLectureId(), version.assignVersion);
		return true;
	}
	
	public boolean cancelAssignment(String lectureId, int assignId) {
		int responseCode = 0;
		String inputLine = "";
		String url = API_HOST + ASSIGNMENT;
		String params = "?token="+accessToken+"&lectureId="+lectureId+"&assignId="+assignId;
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("DELETE");
			
			responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			if (!response.get("message").isJsonNull()) {
				return false;
			}
			int errorCode = 0;
			if (!response.get("code").isJsonNull())
				errorCode = response.get("code").getAsInt();
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}

		dataManager.openDB();
		Version version = dataManager.selectVersionDB(lectureId);
		dataManager.closeDB();
		syncAssignment(version.getLectureId(), version.assignVersion);
		return true;
	}
	
	// submit
	public boolean submitReport(String lectureId, int assignId, String stuId, String filePath) {
		int responseCode = 0;
		String inputLine = "";
		String url = API_HOST + SUBMIT;
		String params = "?token="+accessToken+"&lectureId="+lectureId+"&assignId="+assignId+"&stuId="+stuId;
		
		try {
			if (filePath != null) {
				params = params + "&filePath=" + URLEncoder.encode(filePath, "UTF-8");
			}
			url = url + params;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			
			responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			int errorCode = 0;
			if (!response.get("code").isJsonNull())
				errorCode = response.get("code").getAsInt();
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}
		if (syncReport(lectureId, assignId, stuId))
			return true;
		return false;
	}
	
	public boolean syncReport(String lectureId, int assignId, String stuId) {
//		stuId : (학생용) 학생 아이디 넘겨주어 학생의 것만 받아오기 [optional]
		int responseCode = 0;
		String inputLine = "";
		String url = API_HOST + SUBMIT;
		String params = "?token="+accessToken+"&lectureId="+lectureId;
		if (assignId != -1) {
			params = params + "&assignId=" + assignId;
		}
		if (stuId != null) {
			params = params + "&stuId=" + stuId;
		}
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			responseCode = con.getResponseCode();
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			int errorCode = 0;
			if (!response.get("code").isJsonNull())
				errorCode = response.get("code").getAsInt();
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}
		JsonArray array = response.get("content").getAsJsonArray();
		dataManager.openDB();
		if (array.size() < dataManager.selectSubmitDB(lectureId, assignId, stuId).size()) {
			dataManager.deleteAllLectureDB();
		}
		for (JsonElement e : array) {
			Submit submit = new Submit(e.getAsJsonObject());
			if (!dataManager.existsRecordAtSubmitDB(submit.getSubmitId())) {
				dataManager.insertSubmitDB(submit.getSubmitId(),
						submit.getLectureId(),
						submit.getAssignId(),
						submit.getStudentId(),
						submit.getFilePath());
			}
		}
		dataManager.closeDB();
		return true;
	}
	
	public boolean updateReport(int submitId, String filePath) {
		int responseCode = 0;
		String inputLine = "";
		String url = API_HOST + SUBMIT;
		String params = "?token="+accessToken+"&submitId="+submitId;
		
		
		
		try {
			params = params + "&filePath=" + URLEncoder.encode(filePath, "UTF-8");
			url = url + params;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("PUT");
			responseCode = con.getResponseCode();	
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			int errorCode = 0;
			if (!response.get("code").isJsonNull())
				errorCode = response.get("code").getAsInt();
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}
		dataManager.openDB();
		dataManager.updateSubmitDB(submitId, filePath);
		dataManager.closeDB();
		return true;
	}
	
	public boolean cancelReport(int submitId) {
		int responseCode = 0;
		String inputLine = "";
		String url = API_HOST + SUBMIT;
		String params = "?token="+accessToken+"&submitId="+submitId;
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("DELETE");
			
			responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			int errorCode = 0;
			if (!response.get("code").isJsonNull())
				errorCode = response.get("code").getAsInt();
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}
		dataManager.openDB();
		dataManager.deleteSubmitDB(submitId);
		dataManager.closeDB();
		return true;
	}
	
	// grade
	public boolean giveGrade(String lectureId, int submitId, String stuId, double score) {
		int responseCode = 0;
		String inputLine = "";
		String url = API_HOST + GRADE;
		String params = "?token="+accessToken+"&lectureId="+lectureId+"&submitId="+submitId+"&stuId="+stuId;
		if(score != -1) {
			params = params + "&score=" + score;
		}
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			
			responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			int errorCode = 0;
			if (!response.get("code").isJsonNull())
				errorCode = response.get("code").getAsInt();
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}
		if (syncGrade(stuId, lectureId))
			return true;
		return false;
	}
	
	public boolean syncGrade(String stuId, String lectureId) {
//		양자택일
//		stuId - 성적을 찾는 학생 아이디 [optional]
//		lectureId - 성적을 가진 강의 아이디 [optional]
		if ((stuId == null && lectureId == null)) {
			System.out.println("Get Grade : at least ONE parameter");
			return false;
		}
		int responseCode = 0;
		String inputLine = "";
		String url = API_HOST + GRADE;
		String params = "?token="+accessToken;
		if (lectureId != null) {
			params = params + "&lectureId=" + lectureId;
		}
		if(stuId != null) {
			params = params + "&stuId=" + stuId;
		}
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			
			responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			int errorCode = 0;
			if (!response.get("code").isJsonNull())
				errorCode = response.get("code").getAsInt();
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}
		JsonArray array = response.get("content").getAsJsonArray();
		dataManager.openDB();
		for (JsonElement json : array) {
			Grade grade = new Grade(json.getAsJsonObject());
			if (!dataManager.existsRecordAtGradeDB(grade.getGradeId())) {
				dataManager.insertGradeDB(grade.getGradeId(),
						grade.getLectureId(),
						grade.getSubmitId(),
						grade.getStudentId(),
						grade.getScore());
			}
		}
		dataManager.closeDB();
		return true;
	}
	
	public boolean updateGrade(int gradeId, double score) {
		int responseCode = 0;
		String inputLine = "";
		String url = API_HOST + GRADE;
		String params = "?token="+accessToken+"&gradeId="+gradeId+"&score="+score;
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("PUT");
			
			responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			int errorCode = 0;
			if (!response.get("code").isJsonNull())
				errorCode = response.get("code").getAsInt();
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}
		dataManager.openDB();
		dataManager.updateGradeDB(gradeId, score);
		dataManager.closeDB();
		return true;
	}
	
	public boolean cancelGrade(int gradeId) {
		int responseCode = 0;
		String inputLine = "";
		String url = API_HOST + GRADE;
		String params = "?token="+accessToken+"&gradeId="+gradeId;
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("DELETE");
			
			responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			int errorCode = 0;
			if (!response.get("code").isJsonNull())
				errorCode = response.get("code").getAsInt();
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}
		dataManager.openDB();
		dataManager.deleteGradeDB(gradeId);
		dataManager.closeDB();
		return true;
	}
	
	// question
	public boolean makeQuestion(String lectureId, String stuId, String content) {
		int responseCode = 0;
		String inputLine = "";
		String url = API_HOST + QUESTION;
		String params = "?token="+accessToken+"&lectureId="+lectureId+"&stuId="+stuId;
		
		try {
			params = params + "&content=" + URLEncoder.encode(content,"UTF-8");
			url = url + params;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			
			responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			int errorCode = 0;
			if (!response.get("code").isJsonNull())
				errorCode = response.get("code").getAsInt();
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}
		if (syncQuestion(stuId, lectureId))
			return true;
		return false;
	}
	
	public boolean syncQuestion(String stuId, String lectureId) {
//		양자택일
//		stuId - 질문자 아이디 (학생) [optional]
//		lectureId - 질문한 강의 [optional]
		int responseCode = 0;
		String inputLine = "";
		String url = API_HOST + QUESTION;
		String params = "?token="+accessToken;
		if (stuId != null) {
			params = params + "&stuId="+stuId;
		}
		if (lectureId != null) {
			params = params + "&lectureId="+lectureId;
		}
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			
			responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			int errorCode = 0;
			if (!response.get("code").isJsonNull())
				errorCode = response.get("code").getAsInt();
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}
		
		JsonArray array = response.get("content").getAsJsonArray();
		dataManager.openDB();
		if (array.size() < dataManager.selectQuestionDB(stuId, lectureId).size()) {
			dataManager.deleteAllQuestionDB();
		}
		for (JsonElement json : array) {
			Question question = new Question(json.getAsJsonObject());
			if (!dataManager.existsRecordAtQuestionDB(question.getQuestionId())) {
				dataManager.insertQuestionDB(question.getQuestionId(),
						question.getLectureId(),
						question.getStudentId(),
						question.content);
			}
		}
		dataManager.closeDB();
		return true;
	}
	
	public boolean updateQuestion(int questionId, String content) {
		int responseCode = 0;
		String inputLine = "";
		String url = API_HOST + QUESTION;
		String params = "?token="+accessToken+"&questionId="+questionId;
		
		try {
			params = params + "&content=" + URLEncoder.encode(content, "UTF-8");
			url = url + params;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("PUT");
			
			responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			int errorCode = 0;
			if (!response.get("code").isJsonNull())
				errorCode = response.get("code").getAsInt();
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}
		dataManager.openDB();
		dataManager.updateQuestionDB(questionId, content);
		dataManager.closeDB();
		return true;
	}
	
	public boolean removeQuestion(int questionId) {
		int responseCode = 0;
		String inputLine = "";
		String url = API_HOST + QUESTION;
		String params = "?token="+accessToken+"&questionId="+questionId;
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("DELETE");
			
			responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			int errorCode = 0;
			if (!response.get("code").isJsonNull())
				errorCode = response.get("code").getAsInt();
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}
		dataManager.openDB();
		dataManager.deleteQuestionDB(questionId);
		dataManager.closeDB();
		return true;
	}
	
	// answer
	public boolean makeAnswer(int questionId, String content) {
		int responseCode = 0;
		String inputLine = "";
		String url = API_HOST + ANSWER;
		String params = "?token="+accessToken+"&questionId="+questionId;
		
		try {
			params = params +"&content="+URLEncoder.encode(content, "UTF-8");
			url = url + params;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			
			responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			int errorCode = 0;
			if (!response.get("code").isJsonNull())
				errorCode = response.get("code").getAsInt();
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}
		if (syncAnswer(questionId)) 
			return true;
		return false;
	}
	
	public boolean syncAnswer(int questionId) {
		int responseCode = 0;
		String inputLine = "";
		String url = API_HOST + ANSWER;
		String params = "?token="+accessToken+"&questionId="+questionId;
		url = url + params;
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			
			responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			int errorCode = 0;
			if (!response.get("code").isJsonNull())
				errorCode = response.get("code").getAsInt();
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}
		JsonElement content = response.get("content");
		if (!content.isJsonObject()) {
			System.out.println("get answer : no answer");
			return false;
		}
		Answer answer = new Answer(content.getAsJsonObject());
		dataManager.openDB();
		if(!dataManager.existsRecordAtAnswerDB(answer.getAnswerId())) {
			dataManager.insertAnswerDB(answer.getAnswerId(),
					answer.getQuestionId(),
					answer.content);
		} else {
			Answer localAnswer = dataManager.selectAnswerDB(questionId);
			if (!localAnswer.isEqual(answer)) {
				if (dataManager.deleteAnswerDB(localAnswer.getAnswerId())) {
					dataManager.insertAnswerDB(answer.getAnswerId(),
							answer.getQuestionId(),
							answer.content);
				}
			}
		}
		dataManager.closeDB();
		return true;
	}
	
	public boolean updateAnswer(int answerId, String content) {
		int responseCode = 0;
		String inputLine = "";
		String url = API_HOST + ANSWER;
		String params = "?token="+accessToken+"&answerId="+answerId;
		
		try {
			params = params +"&content="+URLEncoder.encode(content, "UTF-8");
			url = url + params;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("PUT");
			
			responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			int errorCode = 0;
			if (!response.get("code").isJsonNull())
				errorCode = response.get("code").getAsInt();
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}
		dataManager.openDB();
		dataManager.updateAnswerDB(answerId, content);
		dataManager.closeDB();
		return true;
	}
	
	public boolean removeAnswer(int answerId) {
		int responseCode = 0;
		String inputLine = "";
		String url = API_HOST + ANSWER;
		String params = "?token="+accessToken+"&answerId="+answerId;
		url = url + params;
		
		try{
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("DELETE");
			
			responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();
		if (responseCode != 200) {
			int errorCode = 0;
			if (!response.get("code").isJsonNull())
				errorCode = response.get("code").getAsInt();
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}
		
		dataManager.openDB();
		dataManager.deleteAnswerDB(answerId);
		dataManager.closeDB();
		return true;
	}
	
	// outline
	public ArrayList<LectureOutline> getLectureOutline(int version) {
		String inputLine = "";
		try {
			URL obj = new URL(LECTURE_OUTLINE_URL(version));
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			// optional default is GET
			con.setRequestMethod("GET");
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
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