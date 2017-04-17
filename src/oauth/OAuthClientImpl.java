package oauth;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import model.HttpRequestType;
import model.LoginInfo;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class OAuthClientImpl extends BaseRestClient implements OAuthClient {

	private String	accessToken;
	private String	refreshToken;

	public LoginInfo login(String username, String password) {
		LoginInfo loginInfo = new LoginInfo();
		HttpURLConnection conn = null;
		BufferedWriter writer = null;
		OutputStream os = null;
		BufferedReader in = null;
		try {
			conn = getHttpURLConnectionLogin("/oauth/token");
			conn.setRequestMethod("POST");

			HashMap<String, String> postDataParams = new HashMap<String, String>();
			postDataParams.put("grant_type", "password");
			postDataParams.put("username", username);
			postDataParams.put("password", password);

			StringBuilder result = getPostData(postDataParams);

			os = conn.getOutputStream();
			writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			writer.write(result.toString());

			writer.flush();
			conn.connect();

			String msg = conn.getResponseMessage();
			int code = conn.getResponseCode();
			loginInfo.setResponseCode(code);
			loginInfo.setResponseMessage(msg);

			logger.info("Rest Client IDSLogin Response: " + msg + " , code: " + code);

			if (code == HttpURLConnection.HTTP_OK) {

				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

				String responseBodyStr = "";
				String line;
				while ((line = in.readLine()) != null) {
					responseBodyStr += line;
				}

				// {"scope":"minsu","token_type":"Bearer","expires_in":3600,"refresh_token":"2d2cff22ad036ae779e549b9c6a45d60","access_token":"7d97441074dab834daa0c1420d51d198"}
				logger.info("Response body Str: " + responseBodyStr);

				JSONObject jsonObj = new JSONObject(responseBodyStr);

				String loginResponse = jsonObj.getString("access_token");

				loginInfo.setError(false);
				loginInfo.setResponseJsonObject(jsonObj);
			} else if (code == HttpURLConnection.HTTP_BAD_REQUEST) {
				in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));

				String responseBodyStr = "";
				String line;
				while ((line = in.readLine()) != null) {
					responseBodyStr += line;
				}
				JSONObject jsonObj = new JSONObject(responseBodyStr);
				String error = jsonObj.getString("error");
				String errorDescription = jsonObj.getString("error_description");
				loginInfo.setError(true);
				loginInfo.setErrorMsg(error);
				loginInfo.setErrorDesciption(errorDescription);
			} else {
				loginInfo.setError(true);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("UnsupportedEncodingException: " + e.getMessage());
			loginInfo.setError(true);
			loginInfo.setResponseMessage("UnsupportedEncodingException");
		} catch (IOException e) {
			logger.error("Exception: " + e.getMessage());
			loginInfo.setError(true);
			loginInfo.setResponseMessage("IOException");
		} finally {
			try {
				writer.close();
				os.close();
				in.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		return loginInfo;
	}

	public void logout(String accessToken) {
		HashMap<String, String> propLost = getProperties();
		String path = propLost.get("base_url") + "/oauth/revoke";
		String json = null;
		Map<String, String> parameters = null;
		commonRequest(HttpRequestType.POST, path, parameters, accessToken, false);

	}

	private LoginInfo refreshToken(String refreshToken) {
		boolean verified;
		LoginInfo loginInfo = new LoginInfo();
		HttpURLConnection conn = null;
		OutputStream os = null;
		try {

			HashMap<String, String> postDataParams = new HashMap<String, String>();
			postDataParams.put("grant_type", "refresh_token");
			postDataParams.put("refresh_token", refreshToken);

			StringBuilder result = getPostData(postDataParams);

			logger.info("url " + result.toString());
			conn = getHttpURLConnectionLogin("/oauth/token");

			os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			writer.write(result.toString());

			writer.flush();
			writer.close();
			os.close();

			conn.connect();

			String msg = conn.getResponseMessage();
			int code = conn.getResponseCode();
			loginInfo.setResponseCode(code);
			loginInfo.setResponseMessage(msg);
			logger.info("Rest Client IDSLogin Response: " + msg + " , code: " + code);

			if (code == HttpURLConnection.HTTP_OK) {

				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

				String responseBodyStr = "";
				String line;
				while ((line = in.readLine()) != null) {
					responseBodyStr += line;
				}
				in.close();

				// {"scope":"minsu","token_type":"Bearer","expires_in":3600,"refresh_token":"2d2cff22ad036ae779e549b9c6a45d60","access_token":"7d97441074dab834daa0c1420d51d198"}
				logger.info("Response body Str: " + responseBodyStr);

				JSONObject jsonObj = new JSONObject(responseBodyStr);

				String loginResponse = jsonObj.getString("access_token");

				verified = loginResponse == null ? false : true;

				logger.info("isVerified: " + verified);

				loginInfo.setError(!verified);
				loginInfo.setResponseJsonObject(jsonObj);
			} else {
				loginInfo.setError(true);
			}

		} catch (MalformedURLException e) {
			logger.error("UnsupportedEncodingException: " + e.getMessage());
			loginInfo.setError(true);
			loginInfo.setResponseMessage("UnsupportedEncodingException");
		} catch (IOException e) {
			logger.error("UnsupportedEncodingException: " + e.getMessage());
			loginInfo.setError(true);
			loginInfo.setResponseMessage("UnsupportedEncodingException");
		} finally {
			try {
				os.close();
			} catch (IOException e) {
			} finally {
			}
		}

		return loginInfo;
	}

	public Object commonRequest(HttpRequestType requestRype, String path, Map<String, String> postDataParams,
			String accessToken, boolean isApplicationTypeRequest) {
		Object jsonResult = null;
		HttpURLConnection conn = null;
		BufferedWriter writer = null;
		OutputStream os = null;
		BufferedReader in = null;
		try {
			if (isApplicationTypeRequest) {
				accessToken = getApplicationlevelToken();
			}
			System.out.println("therrsrsr" + accessToken);
			conn = getHttpURLConnection(path, requestRype, accessToken);
			conn.setRequestMethod(requestRype.name());
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

			if (postDataParams != null) {
				StringBuilder result = getPostData(postDataParams);
				os = conn.getOutputStream();
				writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
				writer.write(result.toString());
				writer.flush();
			}

			conn.connect();

			String msg = conn.getResponseMessage();
			int code = conn.getResponseCode();
			// logger.info("Rest Client IDSLogin Response: " + msg + " , code: "
			// + code);

			if (code == HttpURLConnection.HTTP_OK) {
				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

				String responseBodyStr = "";
				String line;
				while ((line = in.readLine()) != null) {
					responseBodyStr += line;
				}
				logger.info("Response body Str: " + responseBodyStr);
				if(responseBodyStr.isEmpty()){
					return null;
				}
				Object obj = new JSONParser().parse(responseBodyStr);
				if (obj instanceof org.json.simple.JSONObject) {
					jsonResult = (org.json.simple.JSONObject) obj;
				} else {
					jsonResult = (org.json.simple.JSONArray) obj;
				}

			} else if (code == HttpURLConnection.HTTP_UNAUTHORIZED) {
				LoginInfo loginInfo = refreshToken(refreshToken);
				if (loginInfo.getResponseCode() == HttpURLConnection.HTTP_OK) {
					JSONObject jsonObj = loginInfo.getResponseJsonObject();
					accessToken = jsonObj.get("access_token").toString();
					refreshToken = jsonObj.get("refresh_token").toString();
					jsonResult = commonRequest(requestRype, path, postDataParams, accessToken,
							isApplicationTypeRequest);
				}
			} else {
				logger.error("HttpURLConnection  Error Code :: " + code + " Message :" + msg);
			}

		} catch (IOException e) {
			logger.error("IOException: " + e.getMessage());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("ParseException: " + e.getMessage());
		} finally {
			try {
				writer.close();
				os.close();
				in.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return jsonResult;
	}

	public Object commonRequest(HttpRequestType requestRype, String path, String json, String accessToken,
			boolean isApplicationTypeRequest) {
		Object jsonResult = null;
		HttpURLConnection conn = null;
		BufferedWriter writer = null;
		OutputStream os = null;
		BufferedReader in = null;
		try {
			if (isApplicationTypeRequest) {
				accessToken = getApplicationlevelToken();
			}
			conn = getHttpURLConnection(path, requestRype, accessToken);
			conn.setRequestMethod(requestRype.name());
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

			os = conn.getOutputStream();

			os.write(json.toString().getBytes("UTF-8"));
			os.flush();
			os.close();

			conn.connect();

			String msg = conn.getResponseMessage();
			int code = conn.getResponseCode();
			logger.info("Rest Client IDSLogin Response: " + msg + " , code: " + code);

			if (code == HttpURLConnection.HTTP_OK) {
				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

				String responseBodyStr = "";
				String line;
				while ((line = in.readLine()) != null) {
					responseBodyStr += line;
				}
				logger.info("Response body Str: " + responseBodyStr);
				if(responseBodyStr.isEmpty()){
					return null;
				}
				Object obj = new JSONParser().parse(responseBodyStr);
				if (obj instanceof org.json.simple.JSONObject) {
					jsonResult = (org.json.simple.JSONObject) obj;
				} else {
					jsonResult = (org.json.simple.JSONArray) obj;
				}
			} else if (code == HttpURLConnection.HTTP_UNAUTHORIZED) {
				LoginInfo loginInfo = refreshToken(refreshToken);
				if (loginInfo.getResponseCode() == HttpURLConnection.HTTP_OK) {
					JSONObject jsonObj = loginInfo.getResponseJsonObject();
					accessToken = jsonObj.get("access_token").toString();
					refreshToken = jsonObj.get("refresh_token").toString();
					jsonResult = commonRequest(requestRype, path, json, accessToken, isApplicationTypeRequest);
				}
			} else {
				logger.error("HttpURLConnection  Error Code :: " + code + " Message :" + msg);
			}

		} catch (IOException e) {
			logger.error("IOException: " + e.getMessage());
		} catch (ParseException e) {
			logger.error("ParseException: " + e.getMessage());
		} finally {
			try {
				writer.close();
				os.close();
				in.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return jsonResult;
	}

	public String getApplicationlevelToken() {
		boolean verified;
		LoginInfo loginInfo = new LoginInfo();
		HttpURLConnection conn = null;
		BufferedWriter writer = null;
		OutputStream os = null;
		BufferedReader in = null;
		String token = null;
		try {
			conn = getHttpURLConnectionLogin("/oauth/token");
			conn.setRequestMethod("POST");

			HashMap<String, String> postDataParams = new HashMap<String, String>();
			postDataParams.put("grant_type", "client_credentials");
			postDataParams.put("scope", "openid");

			StringBuilder result = getPostData(postDataParams);

			logger.info("url " + result.toString());

			os = conn.getOutputStream();
			writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			writer.write(result.toString());

			writer.flush();
			conn.connect();

			String msg = conn.getResponseMessage();
			int code = conn.getResponseCode();
			loginInfo.setResponseCode(code);
			loginInfo.setResponseMessage(msg);
			logger.info("Rest Client IDSLogin Response: " + msg + " , code: " + code);

			if (code == HttpURLConnection.HTTP_OK) {

				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

				String responseBodyStr = "";
				String line;
				while ((line = in.readLine()) != null) {
					responseBodyStr += line;
				}

				// {"scope":"minsu","token_type":"Bearer","expires_in":3600,"refresh_token":"2d2cff22ad036ae779e549b9c6a45d60","access_token":"7d97441074dab834daa0c1420d51d198"}
				logger.info("Response body Str: " + responseBodyStr);

				JSONObject jsonObj = new JSONObject(responseBodyStr);

				String loginResponse = jsonObj.getString("access_token");

				verified = loginResponse == null ? false : true;

				logger.info("isVerified: " + verified);

				token = jsonObj.getString("access_token");
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("UnsupportedEncodingException: " + e.getMessage());
			loginInfo.setError(true);
			loginInfo.setResponseMessage("UnsupportedEncodingException");
		} catch (IOException e) {
			logger.error("Exception: " + e.getMessage());
			loginInfo.setError(true);
			loginInfo.setResponseMessage("UnsupportedEncodingException");
		} finally {
			try {
				writer.close();
				os.close();
				in.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		return token;
	}

}
