package oauth;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.codec.binary.Base64;
import model.HttpRequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseRestClient {
public static String accessToken;
	protected static final Logger	logger				= LoggerFactory.getLogger(BaseRestClient.class);

	protected Integer				registrationTimeout	= 2000;

	protected HashMap<String, String> getProperties() {
		Properties prop = new Properties();
		InputStream input = null;

		
      
			ResourceBundle props = ResourceBundle.getBundle("resources.config");
			HashMap<String, String> propLost = new HashMap<String, String>();
			propLost.put("base_url", props.getString("base_url"));
			propLost.put("client_id", props.getString("client_id"));
			propLost.put("client_secret", props.getString("client_secret"));
			return propLost;

	
	}

	public HttpURLConnection getHttpURLConnection(String path, HttpRequestType requsetType, String bearerToken)
			throws IOException {
		URL restUrl;
		restUrl = new URL(path);

		HttpURLConnection conn = (HttpURLConnection) restUrl.openConnection();

		conn.setRequestProperty("Authorization", "Bearer " + bearerToken);
		conn.setConnectTimeout(registrationTimeout);
		conn.setRequestMethod(requsetType.name());
		conn.setDoInput(true);
		conn.setDoOutput(true);

		return conn;
	}

	public HttpURLConnection getHttpURLConnectionLogin(String path) throws IOException {
		URL restUrl;
		HashMap<String, String> propLost = getProperties();
		restUrl = new URL(propLost.get("base_url") + path);

		HttpURLConnection conn = (HttpURLConnection) restUrl.openConnection();

		String authString = propLost.get("client_id") + ":" + propLost.get("client_secret");
		String authStringEnc = Base64.encodeBase64String(authString.getBytes());

		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		conn.setRequestProperty("Authorization", "Basic " + authStringEnc);
		conn.setConnectTimeout(registrationTimeout);
		conn.setRequestMethod("POST");
		conn.setDoInput(true);
		conn.setDoOutput(true);

		return conn;
	}

	protected StringBuilder getPostData(Map<String, String> postDataParams) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (Map.Entry<String, String> entry : postDataParams.entrySet()) {
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8"));
		}
		return result;
	}
	
	protected StringBuilder getPostData(Map<String, String> postDataParams ,boolean first) throws UnsupportedEncodingException{
		StringBuilder result = new StringBuilder();
		
		for (Map.Entry<String, String> entry : postDataParams.entrySet()) {
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(String.valueOf(entry.getValue()) , "UTF-8"));
		}
		return result;
	}


}
