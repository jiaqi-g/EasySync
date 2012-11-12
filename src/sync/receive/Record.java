package sync.receive;

import java.io.Serializable;

public class Record implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2960028059663556626L;
	
	private long uid;
	//private String fileName;
	
	public Record(long uid/*, String fileName*/) {
		super();
		this.uid = uid;
		//this.fileName = fileName;
	}
	
	public void setUid(long uid) {
		this.uid = uid;
	}
	public long getUid() {
		return uid;
	}
	/*
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileName() {
		return fileName;
	}
	*/
	
	public String toString() {
		return "uid: " + uid; //+ " fileName: " + fileName;
	}
}
