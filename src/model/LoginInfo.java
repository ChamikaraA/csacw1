package model;

import org.json.JSONObject;

public class LoginInfo {
	private String ResponseMessage;
	private int ResponseCode;
	private JSONObject ResponseJsonObject;
	private boolean error;
	private String errorMsg;
	private String errorDesciption;
	
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public String getResponseMessage() {
		return ResponseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		ResponseMessage = responseMessage;
	}
	public int getResponseCode() {
		return ResponseCode;
	}
	public void setResponseCode(int responseCode) {
		ResponseCode = responseCode;
	}
	public JSONObject getResponseJsonObject() {
		return ResponseJsonObject;
	}
	public void setResponseJsonObject(JSONObject responseJsonObject) {
		ResponseJsonObject = responseJsonObject;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getErrorDesciption() {
		return errorDesciption;
	}
	public void setErrorDesciption(String errorDesciption) {
		this.errorDesciption = errorDesciption;
	}
	
}