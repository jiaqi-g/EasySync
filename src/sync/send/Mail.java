package sync.send;
  

import it.sauronsoftware.base64.Base64;

import java.util.*;  
import java.io.*;  

import javax.mail.*;  
import javax.mail.internet.*;  
import javax.activation.*;

public class Mail {
	
	//define sender, receiver, SMTP server, username, password, subject and content
	private String displayName;
	private String to;  
	private String from;  
	private String smtpServer;  
	private String username;  
	private String password;  
	private String subject;  
	private String content;  
	private boolean ifAuth; //if the authentication is needed on the server side
	private String filename="";
	private Vector file = new Vector(); //vector for saving sending attachments' filenames
	
	public void setSmtpServer(String smtpServer){  
		this.smtpServer=smtpServer;  
	}

	public void setFrom(String from){  
		this.from=from;  
	}
	
	public void setDisplayName(String displayName){  
		this.displayName=displayName;  
	}
	
	public void setIfAuth(boolean ifAuth){  
		this.ifAuth=ifAuth;  
	}

	public void setUserName(String username){  
		this.username=username;  
	}

	public void setPassword(String password){  
		this.password=password;  
	}

	public void setTo(String to){  
		this.to=to;  
	}

	public void setSubject(String subject){  
		this.subject=subject;  
	}

	public void setContent(String content){  
		this.content=content;  
	}
	
	public void addAttachfile(String fname){  
		file.addElement(fname);  
	} 

	public Mail(){  

	}  

	public Mail(String smtpServer,String from,String displayName,String username,String password,String to,String subject,String content){  
		this.smtpServer=smtpServer;  
		this.from=from;  
		this.displayName=displayName;  
		this.ifAuth=true;  
		this.username=username;  
		this.password=password;  
		this.to=to;  
		this.subject=subject;  
		this.content=content;  
	}  

	public Mail(String smtpServer,String from,String displayName,String to,String subject,String content){  
		this.smtpServer=smtpServer;  
		this.from=from;  
		this.displayName=displayName;  
		this.ifAuth=false;  
		this.to=to;  
		this.subject=subject;  
		this.content=content;  
	}  

	//send mail
	public HashMap send(){
		HashMap map=new HashMap();
		map.put("state", "success");
		String message="E-mail sent successfully!";
		Session session=null;
		Properties props = System.getProperties();
		props.put("mail.smtp.host", smtpServer);
		
		if(ifAuth){
			props.put("mail.smtp.auth","true");     
			SmtpAuth smtpAuth = new SmtpAuth(username, password);  
			session = Session.getDefaultInstance(props, smtpAuth);   
		}else{  
			props.put("mail.smtp.auth","false");  
			session = Session.getDefaultInstance(props, null);  
		}  

		session.setDebug(true);
		Transport trans = null;
		try {
			Message msg = new MimeMessage(session);   
			try{  
				Address from_address = new InternetAddress(from, displayName);  
				msg.setFrom(from_address);  
			}catch(java.io.UnsupportedEncodingException e){  
				e.printStackTrace();  
			}  
			InternetAddress[] address={new InternetAddress(to)};  
			msg.setRecipients(Message.RecipientType.TO, address);  
			msg.setSubject(subject);
			
			Multipart mp = new MimeMultipart();
			MimeBodyPart mbp = new MimeBodyPart();
			
			mbp.setContent(content.toString(), "text/html;charset=gb2312");
			mp.addBodyPart(mbp);
			
			if(!file.isEmpty()){//有附件  
				Enumeration efile = file.elements();  
				while(efile.hasMoreElements()){   
					mbp = new MimeBodyPart();  
					filename = efile.nextElement().toString(); //选择出每一个附件名  
					mbp.attachFile(filename);
					
					FileDataSource fds = new FileDataSource(filename); //得到文件数据源
					mbp.setDataHandler(new DataHandler(fds)); //得到附件本身并至入BodyPart  
					mbp.setFileName("=?GBK?B?" + Base64.encode(fds.getName()) + "?=");  //得到文件名同样至入BodyPart
					
					//System.err.println(fds.getName());
					mp.addBodyPart(mbp);  
				}
				file.removeAllElements();      
			}
			
			msg.setContent(mp); //Multipart加入到信件  
			msg.setSentDate(new Date());     //设置信件头的发送日期  
			
			//发送信件  
			msg.saveChanges();   
			trans = session.getTransport("smtp");  
			trans.connect(smtpServer, username, password);  
			trans.sendMessage(msg, msg.getAllRecipients());  
			trans.close();  

		}catch(AuthenticationFailedException e){     
			map.put("state", "failed");  
			message="邮件发送失败！错误原因：\n"+"身份验证错误!";  
			e.printStackTrace();   
		}catch (MessagingException e) {  
			message="邮件发送失败！错误原因：\n"+e.getMessage();  
			map.put("state", "failed");  
			e.printStackTrace();  
			Exception ex = null;  
			if ((ex = e.getNextException()) != null) {  
				System.out.println(ex.toString());  
				ex.printStackTrace();  
			}   
		} catch (IOException e) {
			e.printStackTrace();
		}  
		//System.out.println("\n提示信息:"+message);  
		map.put("message", message);  
		return map;  
	}
}