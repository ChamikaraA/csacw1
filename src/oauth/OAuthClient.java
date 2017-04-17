package oauth;

import java.util.Map;

import model.HttpRequestType;
import model.LoginInfo;

public interface OAuthClient {

	// Can be used to get the access token
	LoginInfo login(String username, String password);

	// Can be used to get the access token
	void logout(String accessToken);

	// Can be used to GET,DELETE Requests
	Object commonRequest(HttpRequestType requestRype, String path, Map<String, String> postDataParams,
			String accessToken, boolean isApplicationTypeRequest);

	// Can be used to POST,PUT Requests
	Object commonRequest(HttpRequestType requestRype, String path, String json, String accessToken,
			boolean isApplicationTypeRequest);

}