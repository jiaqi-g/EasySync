package sync.send;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import javax.mail.internet.MimeUtility;

public class Test {
	public static void main(String[] args) {
		/*
		Record a = new Record(88447);
		Record b = new Record(88427);
		ArrayList<Record> list = new ArrayList<Record>();
		list.add(a);
		list.add(b);
		MetaDataUtility.write(list);
		
		ArrayList<Record> records = MetaDataUtility.read();
		for (Record r: records) {
			System.out.println(r);
		}
		*/
		/*
		try {
			System.out.println(MimeUtility.decodeText("abcfdsa.txt"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		*/
		String smtpServer = "smtp.163.com";
		String from = "mailtest10000@163.com";
		String displayName = "mailtest10000";
		String username = "mailtest10000@163.com";
		String password = "qwerty";
		String to = "mailtest10000@163.com";
		String subject = "auto";
		String content = "";
		
		Mail mail = new Mail(smtpServer, from, displayName, username, password, to, subject, content);
		mail.addAttachfile("C:/tmp/tst.xlsx");
		mail.send();
	}
}
