package org.anooj.hadoop.client.ingestemail.pojo;

public class Email {
	@Override
	public String toString() {
		return "Email [from=" + from + ", to=" + to + ", body=" + body + "]";
	}
	private String from = null;
	private String to = null;
	private String body = null;
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	

}
