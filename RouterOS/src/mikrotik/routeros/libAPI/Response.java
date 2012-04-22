package mikrotik.routeros.libAPI;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Response {

	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS z");
	
	private long timestamp;
	private String text;
	
	public Response(String text) {
		timestamp = System.currentTimeMillis();
		this.text = text;
	}
	
	public Response(long time, String text) {
		this.timestamp = time;
		this.text = text;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long time) {
		this.timestamp = time;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "Response[time=" + dateFormat.format(new Date(timestamp)) + ", text=" + text + "]";
	}
}
