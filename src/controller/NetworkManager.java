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

/**
 *
 */
public class NetworkManager {
	private static final String API_HOST = "http://localhost:3001";

//	private static final String API_HOST = "http://www.feonfun.com:8808";
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
	
	private static final String CONFIG_FIlE = "./config.json";

	private DataManager dataManager = new DataManager();
	private String accessToken;
	LoginPage loginpage;
	MainPage mainpage;

    /**
     * 강의 정보를 가져올 URL 정보
     * @param version 강의 정보 버전
     * @return 강의 정보 버전에 따른 URL
     */
	private static final String LECTURE_OUTLINE_URL(int version) {
		return "https://rawgit.com/DyoonDyoon/Back-End/master/script/json/" + version + ".json";
	}

    /**
     * 생성자. 오류를 출력하기 위한 Frame들을 받음
     * @param loginpage 로그인 프레임
     * @param mainpage 메인 프레임
     */
	public NetworkManager(LoginPage loginpage, MainPage mainpage) {
		accessToken = null;
		dataManager = new DataManager();
		this.loginpage = loginpage;
		this.mainpage = mainpage;
	}

    /**
     * accessToken을 다시 갱신하는 메소드
     * @param response 갱신할 accessToken을 가진 JsonObject
     */
	private void updateAccessToken(JsonObject response) {
		accessToken = response.get("accessToken").getAsJsonObject().get("token").getAsString();
	}

    /**
     * 강의 정보를 업데이트가 할 필요있는지 확인하는 메소드
     * @return 필요하지 않다면 -1, 필요하다면 최신 version을 반환
     */
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
		JsonObject serverConfig = getLectureOutlineConfig();	// 서버에서 응답하는 최신버전 내용
		boolean flag = false;
		if (serverConfig != null) {
			if(!textFile.exists()) {	// 버전 파일이 없을 경우 (프로그램을 새로 설치했을 경우)
				flag = true;
	        } else {
	        	boolean isUpToDate = config.get("version").getAsInt() != serverConfig.get("version").getAsInt();
		        if (isUpToDate) {
		        	flag = true;
		        }
	        }
	        if (flag) {	// update가 필요
	        	try {
					FileOutputStream outputStream = new FileOutputStream(CONFIG_FIlE, false);	// if exist
		            String serverConfigStr = serverConfig.toString();
		            outputStream.write(serverConfigStr.getBytes(), 0, serverConfigStr.getBytes().length);
		            outputStream.close();
	        	} catch (IOException e) {
	        		e.printStackTrace();
	        		return -1;	// 파일을 쓰는 데 오류가 생겼을 경우 업데이트를 하지 않음
	        	}
	        	return serverConfig.get("version").getAsInt();	// 최신 version 내용
	        }
        }
		return -1;
	}

    /**
     * 서버로부터 최신정보를 불러오기
     * @return 최신정보 JsonObject
     */
	private JsonObject getLectureOutlineConfig() {
		int responseCode = 0; // 서버로부터 응답받은 응답코드
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
		String url = API_HOST + LECTURE_OUTLINE;	// 접속할 URL
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			responseCode = con.getResponseCode();

			BufferedReader in = new BufferedReader(
					new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();	// 서버의 응답내용 읽기
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		if (responseCode != 200)
			return null;
		
		JsonObject config = new JsonParser().parse(inputLine).getAsJsonObject();	// 읽은 내용을 JsonObject로 변환
		return config;
	}

    /**
     * 로그인 메소드
     * @param id 로그인할 아이디
     * @param pw 로그인할 비밀번호
     * @return 서버로부터 받은 User 객체
     */
	public User login(String id, String pw) {
		int responseCode = 0;	// 서버로부터 응답받은 응답코드
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
		String url = API_HOST + LOGIN;
		String params = "?userId=" + id + "&password=" + pw;	// 파라미터
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");	// POST 방식을 사용
			
			responseCode = con.getResponseCode();
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			inputLine = in.readLine();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();	// 받은 Response를 JsonObject로 변경
		if (responseCode != 200) {
			int errorCode = 0;
			if (!response.get("code").isJsonNull())
				errorCode = response.get("code").getAsInt();
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {	// 에러코드가 3, 4, 5 일 경우 로그인 실패
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return null;
		}
		updateAccessToken(response);	// accessToken 갱신
		JsonObject userJson = response.getAsJsonObject("user");
		User user = new User(userJson);
		return user;	// user 객체 반환
	}

    /**
     * 회원가입 메소드
     * @param id 유저 아이디
     * @param pw 비밀번호
     * @param name 유저이름
     * @param major 유저 전공
     * @param type 유저 타입 (0: 학생, 1: 교수)
     * @return 성공시 true 실패시 false
     */
	public boolean join(String id, String pw, String name, String major, int type) {
		int responseCode = 0;	// 서버로부터 응답받은 응답코드
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
		String url = API_HOST + JOIN;
		String params = "?userId="+id+"&password="+pw+"&name="+name+"&major="+major+"&type="+type; // 파라미터
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");	// POST 방식 사용
			
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
			JsonObject response = new JsonParser().parse(inputLine).getAsJsonObject();	// 실패했을 경우 에러코드를 위한 JSON
			System.out.println(response.get("message").toString());
			return false; // 실패
		}
		return true;	// 성공
	}

    /**
     * 수강신청 메소드
     * @param userId 수강하는 학생
     * @param lectureId 강의 학수번호
     * @return 성공시 true, 실패시 false
     */
	public boolean applyLecture(String userId, String lectureId) {
		int responseCode = 0;	// 서버로부터 응답받은 응답코드
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
		String url = API_HOST + LECTURE;
		String params = "?token="+accessToken+"&userId="+userId+"&lectureId="+lectureId;	// 파라미터
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");	// POST 방식 사용
			
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
		dataManager.insertLectureDB(lectureId, userId);	// LectureDB에 저장
		dataManager.closeDB();
		return true;
	}

    /**
     * 서버의 Lecture 정보로 동기화
     * @param userId Lecture 정보를 가져올 유저 아이디
     * @return 성공시 true
     */
	public boolean syncLectures(String userId) {
		int responseCode = 0;	// 서버로부터 응답받은 응답코드
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
		String url = API_HOST + LECTURE;
		String params = "?token="+accessToken+"&userId="+userId;	// 파라미터
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");	// GET 방식 사용
			
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
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {	// 에러코드 3, 4, 5의 경우 재로그인 요구 (세션)
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}

		JsonArray content = response.get("content").getAsJsonArray();
		dataManager.openDB();
		if (content.size() < dataManager.selectLectureDB(userId).size()) {	// 삭제된 내용이 있을 경우
			dataManager.deleteAllLectureDB();	// 다시 새로 레코드를 저장하기 위한 레코드 모두 삭제
		}
		for (JsonElement e : content) {
			JsonObject lectureJson = e.getAsJsonObject();
			Lecture lecture = new Lecture(lectureJson);
			if (!dataManager.existsRecordAtLectureDB(lecture.getLectureId(), lecture.getUserId())) {	// 없는 레코드일 경우
				if (!dataManager.existsRecordAtVersion(lecture.getLectureId())) {	// 버전테이블이 없을 경우 생성
					dataManager.insertVersionDB(lecture.getLectureId());
				}
				dataManager.insertLectureDB(lecture.getLectureId(), lecture.getUserId());	// 강의 넣기
			}
		}
		dataManager.closeDB();
		return true;
	}

    /**
     * 수강 포기 메소드
     * @param userId 포기할 유저 아이디
     * @param lectureId 강의 학수번호
     * @return 성공시 true
     */
	public boolean dropLecture(String userId, String lectureId) {
		int responseCode = 0;	// 서버로부터 응답받은 응답코드
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
		String url = API_HOST + LECTURE;
		String params = "?token="+accessToken+"&userId="+userId+"&lectureId="+lectureId;
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("DELETE");	// DELETE 사용
			
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
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {	// 세션 만료
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}
		dataManager.openDB();
		dataManager.deleteLectureDB(lectureId, userId);	// Local DB에서 삭제
		dataManager.closeDB();
		return true;
	}

    /**
     * 공지 올리기
     * @param lectureId 강의 학수번호
     * @param title 공지 제목
     * @param description 공지 내용
     * @return 성공시 true
     */
	public boolean postNotification(String lectureId, String title, String description) {
		int responseCode = 0;	// 서버로부터 응답받은 응답코드
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
		String url = API_HOST + NOTIFICATION;
		String params = "?token="+accessToken+"&lectureId="+lectureId;
		
		try {
			params = params + "&title="+ URLEncoder.encode(title, "UTF-8");	// UTF-8로 인코딩
			if (description != null) {	// 내용이 있을 경우 파라미터에 추가
				params = params + "&description=" + URLEncoder.encode(description, "UTF-8");
			}
			url = url + params;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");	// POST 방식 사용
			
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
		Version version = dataManager.selectVersionDB(lectureId);	// 버전 초기화를 위한 Version 객체
		dataManager.closeDB();
		syncNotification(version.getLectureId(), version.notiVersion);
		return true;
	}

    /**
     * 공지 동기화
     * @param lectureId 동기화할 공지의 학수번호
     * @param version 공지 버전
     * @return 성공시 true
     */
	public boolean syncNotification(String lectureId, int version) {
		int responseCode = 0;	// 서버로부터 응답받은 응답코드
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
		String url = API_HOST + NOTIFICATION;
		String params = "?token="+accessToken+"&lectureId="+lectureId+"&version="+version;
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");	// GET 방식 사용
			
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
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {	// 세션 만료
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

    /**
     * 공지 변경
     * @param lectureId 학수번호
     * @param notiId 변경할 공지 아이디
     * @param title 변경할 제목 (optional)
     * @param description 변경할 내용 (optional)
     * @return 성공시 true
     */
	public boolean updateNotification(String lectureId, int notiId, String title, String description) {
		int responseCode = 0;	// 서버로부터 응답받은 응답코드
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
		String url = API_HOST + NOTIFICATION;
		String params = "?token="+accessToken+"&lectureId="+lectureId+"&notiId="+notiId;
		
		try {
			// 각 파라미터가 있을 경우 URL 파라미터에 추가
			if (title != null) {
				params = params + "&title=" + URLEncoder.encode(title, "UTF-8");	// UTF-8로 인코딩
			}
			if (description != null) {
				params = params + "&description=" + URLEncoder.encode(description, "UTF-8");	// UTF-8로 인코딩
			}
			url = url + params;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("PUT"); // PUT 방식 사용
			
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
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {	// 세션 만료
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

    /**
     * 공지 삭제
     * @param lectureId 삭제할 공지 강의 아이디
     * @param notiId 삭제할 공지 아이디
     * @return 성공시 true
     */
	public boolean cancelNotification(String lectureId, int notiId) {
		int responseCode = 0;	// 서버로부터 응답받은 응답코드
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
		String url = API_HOST + NOTIFICATION;
		String params = "?token="+accessToken+"&lectureId="+lectureId+"&notiId="+notiId;	// 파라미터
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("DELETE"); // DELETE 사용
			
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
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {	// 세션 만료
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


    /**
     * 과제 생성
     * @param lectureId 강의 학수번호
     * @param title 과제 제목
     * @param description 과제 내용
     * @param filePath 첨부파일
     * @param startDate 과제 시작일
     * @param endDate 과제 마감일
     * @return 성공시 true
     */
    public boolean postAssignment(String lectureId, String title, String description, String filePath, String startDate, String endDate) {
		int responseCode = 0;	// 서버로부터 응답받은 응답코드
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
		String url = API_HOST + ASSIGNMENT;
		String params = "?token="+accessToken+"&lectureId="+lectureId; // 파라미터
		
		try {
			// 각 파라미터들의 내용을 옵셔널하게 추가
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
			con.setRequestMethod("POST");	// POST 방식 사용
			
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
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) { // 세션만료
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

    /**
     * 과제 동기화
     * @param lectureId 동기화할 강의 학수번호
     * @param version 동기화 버전
     * @return 성공시 true
     */
	public boolean syncAssignment(String lectureId, int version) {
		int responseCode = 0;	// 서버로부터 응답받은 응답코드
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
		String url = API_HOST + ASSIGNMENT;
		String params = "?token="+accessToken+"&lectureId="+lectureId+"&version="+version;	// 파라미터
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET"); // GET방식 사용
			
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
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {	// 세션 만료
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

			// 각 내용들 optional 하게 추가 없으면 null
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

    /**
     * 과제 변경
     * @param lectureId 학수번호
     * @param assignId 과제 아이디
     * @param title 과제 제목
     * @param description 과제 내용
     * @param filePath 과제 첨부파일
     * @param startDate 과제 시작일
     * @param endDate 과제 마감일
     * @return 성공시 true
     */
	public boolean updateAssignment(String lectureId, int assignId, String title, String description, String filePath, String startDate, String endDate) {
		int responseCode = 0;	// 서버로부터 응답받은 응답코드
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
		String url = API_HOST + ASSIGNMENT;
		String params = "?token="+accessToken+"&lectureId="+lectureId+"&assignId="+assignId;	// 파라미터
		try {
			// 각 파라미터들은 optional 하다
			// 개행과 같은 문자때문에 UTF-8로 인코딩
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
			con.setRequestMethod("PUT"); // PUT 방식 사용
			
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
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {	 // 세션만료
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

    /**
     * 과제 삭제
     * @param lectureId 학수번호
     * @param assignId 과제 아이디
     * @return 성공시 true
     */
	public boolean cancelAssignment(String lectureId, int assignId) {
		int responseCode = 0;	// 서버로부터 응답받은 응답코드
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
		String url = API_HOST + ASSIGNMENT;
		String params = "?token="+accessToken+"&lectureId="+lectureId+"&assignId="+assignId; 	// 파라미터
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("DELETE");	// DELETE 사용
			
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
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {	// 세션만료
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

    /**
     * 레포트 제출
     * @param lectureId 제출할 레포트의 과제를 가진 강의 학수번호
     * @param assignId 과제아이디
     * @param stuId 제출자 아이디
     * @param filePath 첨부파일 (레포트)
     * @return 성공시 true
     */
	public boolean submitReport(String lectureId, int assignId, String stuId, String filePath) {
		int responseCode = 0;	// 서버로부터 응답받은 응답코드
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
		String url = API_HOST + SUBMIT;
		String params = "?token="+accessToken+"&lectureId="+lectureId+"&assignId="+assignId+"&stuId="+stuId; // 파라미터
		
		try {
			if (filePath != null) {
				params = params + "&filePath=" + URLEncoder.encode(filePath, "UTF-8");
			}
			url = url + params;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");	// POST 방식 사용
			
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
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {	// 세션만료
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

    /**
     * 레포트 가져오기
     * @param lectureId 학수번호
     * @param assignId 과제아이디
     * @param stuId 가져올 학생 [optional]
     * @return 성공시 true
     */
	public boolean syncReport(String lectureId, int assignId, String stuId) {
//		stuId : (학생용) 학생 아이디 넘겨주어 학생의 것만 받아오기 [optional]
		int responseCode = 0;	// 서버로부터 응답받은 응답코드
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
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
			con.setRequestMethod("GET");	// GET 방식 사용
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
		if (array.size() < dataManager.selectSubmitDB(lectureId, assignId, stuId).size()) {	// 삭제된 내용이 있을 경우 초기화
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

    /**
     * 레포트 수정
     * @param submitId 제출한 레포트 아이디
     * @param filePath 첨부파일
     * @return 성공시 true
     */
	public boolean updateReport(int submitId, String filePath) {
		int responseCode = 0;	// 서버로부터 응답받은 응답코드
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
		String url = API_HOST + SUBMIT;
		String params = "?token="+accessToken+"&submitId="+submitId;	// 파라미터
		
		try {
			params = params + "&filePath=" + URLEncoder.encode(filePath, "UTF-8");	// UTF-8 인코딩
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
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {	// 세션만료
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

    /**
     * 레포트 삭제
     * @param submitId 삭제할 레포트
     * @return 성공시 true
     */
	public boolean cancelReport(int submitId) {
		int responseCode = 0;	// 서버로부터 응답받은 응답코드
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
		String url = API_HOST + SUBMIT;
		String params = "?token="+accessToken+"&submitId="+submitId;
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("DELETE");	// DELETE 사용
			
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
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) { // 세션 만료
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

    /**
     * 점수 부여
     * @param lectureId 강의 아이디 (학수번호)
     * @param submitId 레포트 아이디
     * @param stuId 레포트를 낸 학생 아이디
     * @param score 부여할 점수
     * @return 성공시 true
     */
	public boolean giveGrade(String lectureId, int submitId, String stuId, double score) {
		int responseCode = 0;	// 서버로부터 응답받은 응답코드
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
		String url = API_HOST + GRADE;
		String params = "?token="+accessToken+"&lectureId="+lectureId+"&submitId="+submitId+"&stuId="+stuId;	// 파라미터
		if(score != -1) {
			params = params + "&score=" + score;
		}
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");	// POST 방식 사용
			
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
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) { // 세션만료
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

    /**
     * 점수 동기화
	 * 두 파라미터 중 양자 택일
     * @param stuId 성적을 찾는 학생 아이디
     * @param lectureId 성적을 가진 강의 아이디
     * @return 성공시 true
     */
	public boolean syncGrade(String stuId, String lectureId) {
//		양자택일
//		stuId - 성적을 찾는 학생 아이디 [optional]
//		lectureId - 성적을 가진 강의 아이디 [optional]
		if ((stuId == null && lectureId == null)) {
			System.out.println("Get Grade : at least ONE parameter");
			return false;
		}
		int responseCode = 0;	// 서버로부터 응답받은 응답코드
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
		String url = API_HOST + GRADE;
		String params = "?token="+accessToken;
		// 옵셔널하게 파라미터 추가
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
			con.setRequestMethod("GET");	// GET 방식 사용
			
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
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) { // 세션만료
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}
		// 내용 저장
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

    /**
     * 성적 변경
     * @param gradeId 변경할 성적 아이디
     * @param score 변경할 점수
     * @return 성공시 true
     */
	public boolean updateGrade(int gradeId, double score) {
		int responseCode = 0;	// 서버로부터 응답받은 응답코드
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
		String url = API_HOST + GRADE;
		String params = "?token="+accessToken+"&gradeId="+gradeId+"&score="+score; // 파라미터
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("PUT"); // PUT 방식 사용
			
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
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) { // 세션만료
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

    /**
     * 성적 삭제
     * @param gradeId 삭제할 성적 아이디
     * @return 성공시 true
     */
	public boolean cancelGrade(int gradeId) {
		int responseCode = 0;	// 서버로부터 응답받은 응답코드
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
		String url = API_HOST + GRADE;
		String params = "?token="+accessToken+"&gradeId="+gradeId;	// 파라미터
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("DELETE");	// DELETE 방식 사용
			
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
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {	// 세션 만료
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

    /**
     * 질문하기
     * @param lectureId 질문할 강의 아이디
     * @param stuId 질문자 아이디
     * @param content 질문 내용
     * @return 성공시 true
     */
    public boolean makeQuestion(String lectureId, String stuId, String content) {
		int responseCode = 0;	// 서버로부터 응답받은 응답코드
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
		String url = API_HOST + QUESTION;
		String params = "?token="+accessToken+"&lectureId="+lectureId+"&stuId="+stuId; // 파라미터
		
		try {
			params = params + "&content=" + URLEncoder.encode(content,"UTF-8");	//  파라미터 인코딩해서 추가
			url = url + params;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST"); // POST 방식 사용
			
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
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) { // 세션 만료
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

    /**
     * 질문 동기화
	 * 파라미터 양자 택일
     * @param stuId 질문자 아이디 (학생) 질문자 아이디 (학생)
     * @param lectureId 질문한 강의
     * @return 성공시 true
     */
	public boolean syncQuestion(String stuId, String lectureId) {
//		양자택일
//		stuId - 질문자 아이디 (학생) [optional]
//		lectureId - 질문한 강의 [optional]
		int responseCode = 0;	// 서버로부터 응답받은 응답코드
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
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
			con.setRequestMethod("GET"); // GET 방식 사용
			
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
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) { // 세션 만료
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}

		// 내용 업데이트
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

    /**
     * 변경할 질문
     * @param questionId 질문 아이디
     * @param content 질문 내용
     * @return 성공시 true
     */
	public boolean updateQuestion(int questionId, String content) {
		int responseCode = 0;	// 서버로부터 응답받은 응답코드
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
		String url = API_HOST + QUESTION;
		String params = "?token="+accessToken+"&questionId="+questionId;	// 파라미터
		
		try {
			params = params + "&content=" + URLEncoder.encode(content, "UTF-8");
			url = url + params;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("PUT"); // PUT 방식 사용
			
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
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) { // 세션만료
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

    /**
     * 질문 삭제
     * @param questionId 삭제할 질문 아이디
     * @return 성공시 true
     */
	public boolean removeQuestion(int questionId) {
		int responseCode = 0;	// 서버로부터 응답받은 응답코드
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
		String url = API_HOST + QUESTION;
		String params = "?token="+accessToken+"&questionId="+questionId; // 파라미터
		url = url + params;
		
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("DELETE"); // DELETE 방식 사용
			
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
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) {	// 세션만료
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

    /**
     * 답변 달기
     * @param questionId 답변 달 질문 아이디
     * @param content 답변 내용
     * @return 성공시 true
     */
	public boolean makeAnswer(int questionId, String content) {
		int responseCode = 0;	// 서버로부터 응답받은 응답코드
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
		String url = API_HOST + ANSWER;
		String params = "?token="+accessToken+"&questionId="+questionId; // 파라미터
		
		try {
			params = params +"&content="+URLEncoder.encode(content, "UTF-8"); // 파라미터 인코딩
			url = url + params;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST"); // POST 방식 사용
			
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
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) { // 세션 만료
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

    /**
     * 답변 동기화
     * @param questionId
     * @return
     */
	public boolean syncAnswer(int questionId) {
		int responseCode = 0;	// 서버로부터 응답받은 응답코드
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
		String url = API_HOST + ANSWER;
		String params = "?token="+accessToken+"&questionId="+questionId; // 파라미터
		url = url + params;
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET"); // GET 방식 사용
			
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
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) { // 세션 만료
				JOptionPane.showMessageDialog(null, "다시 로그인 해주세요");
				mainpage.setVisible(false);
				loginpage.setVisible(true);
			}
			return false;
		}
		JsonElement content = response.get("content");
		if (!content.isJsonObject()) { // 답변이 없을 경우
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
			if (!localAnswer.isEqual(answer)) { // 답변이 다를 경우 갱신
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

    /**
     * 답변 변경
     * @param answerId 변경할 답변 아이디
     * @param content 변경할 내용
     * @return 성공시 true
     */
	public boolean updateAnswer(int answerId, String content) {
		int responseCode = 0;	// 서버로부터 응답받은 응답코드
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
		String url = API_HOST + ANSWER;
		String params = "?token="+accessToken+"&answerId="+answerId; // 파라미터
		
		try {
			params = params +"&content="+URLEncoder.encode(content, "UTF-8"); // 파라미터 추가
			url = url + params;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("PUT"); // PUT 방식 사용
			
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
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) { // 세션만료
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

    /**
     * 답변 삭제
     * @param answerId 삭제할 답변
     * @return 성공시 true
     */
	public boolean removeAnswer(int answerId) {
		int responseCode = 0;	// 서버로부터 응답받은 응답코드
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
		String url = API_HOST + ANSWER;
		String params = "?token="+accessToken+"&answerId="+answerId; // 파라미터
		url = url + params;
		
		try{
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("DELETE");	// DELETE 방식 사용
			
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
			if (errorCode == 3 || errorCode == 4 || errorCode == 5) { // 세션만료
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

    /**
     * 서버로부터 강의 정보 받아오기
     * @param version 받아올 버전
     * @return 받아온 LectureOutline 모델 어레이 반환
     */
	public ArrayList<LectureOutline> getLectureOutline(int version) {
		String inputLine = "";	// 서버로부터 응답받을 response를 읽어 저장할 String 객체
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