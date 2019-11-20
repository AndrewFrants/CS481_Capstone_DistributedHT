package data;

import java.util.ArrayList;

public class Document {
private String title;
private String file;


 public Document(String title) {
	this.title = title;
	setFile();
	
 }
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getFile() {
	return file;
}
private void setFile() {
	file = "this is file " + title;
}

 
 }
