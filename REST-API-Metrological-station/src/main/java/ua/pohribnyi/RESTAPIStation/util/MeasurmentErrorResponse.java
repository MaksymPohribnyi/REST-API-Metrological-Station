package ua.pohribnyi.RESTAPIStation.util;

public class MeasurmentErrorResponse {

	private String message;

	private long timeStamp;

	public MeasurmentErrorResponse(String message, long timeStamp) {
		this.message = message;
		this.timeStamp = timeStamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

}
