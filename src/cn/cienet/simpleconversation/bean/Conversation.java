package cn.cienet.simpleconversation.bean;

public class Conversation {

	private String author;
	private String content;
	private String date;
	
	public Conversation(String author, String content, String date) {
		// TODO Auto-generated constructor stub
		this.author=author;
		this.content=content;
		this.date=date;
	}
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	
}
