package sync.receive;

import java.util.HashSet;
import java.util.Set;

public final class Arguments {
	
	public static Set<String> fileNameSet;
	public static String smtpServer = "smtp.163.com";
	public static String from = "mailtest10000@163.com";
	public static String displayName = "mailtest10000";
	public static String username = "mailtest10000@163.com";
	public static String password = "qwerty";
	public static String to = "mailtest10000@163.com";
	public static String subject = "auto";
	public static String content = "";
	
	public static String imapServer = "imap.163.com";
	
	public static String fileSavedPath = "c:/tmp";
	
	public static String version = "V1.0";
	
	static {
		fileNameSet = new HashSet<String>();
	}
	
	private Arguments() {
		
	}
}