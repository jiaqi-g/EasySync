package sync.receive;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.MimeUtility;


import com.sun.mail.imap.IMAPFolder;

public class IMAPReceiver {

	public static ArrayList<Record> records = null;

	static {
		records = MetaDataUtility.read();
	}

	//Test if this mail contains attachment or not
	public static boolean isContainAttach(Part part) throws Exception {
		boolean attachflag = false;
		// String contentType = part.getContentType();
		if (part.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) part.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				BodyPart mpart = mp.getBodyPart(i);
				String disposition = mpart.getDisposition();
				if ((disposition != null) && ((disposition.equals(Part.ATTACHMENT)) || (disposition.equals(Part.INLINE)))) {
					attachflag = true;
				}
				else if (mpart.isMimeType("multipart/*")) {
					attachflag = isContainAttach((Part) mpart);
				}
				else {
					String contype = mpart.getContentType();
					if (contype.toLowerCase().indexOf("application") != -1)
						attachflag = true;
					if (contype.toLowerCase().indexOf("name") != -1)
						attachflag = true;
				}
			}
		} else if (part.isMimeType("message/rfc822")) {
			attachflag = isContainAttach((Part) part.getContent());
		}

		return attachflag;

	}

	//Save mail
	public static void saveAttachMent(Part part) throws Exception {
		String fileName = "";
		if (part.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) part.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				BodyPart mpart = mp.getBodyPart(i);
				String disposition = mpart.getDisposition();
				if ((disposition != null) && ((disposition.equals(Part.ATTACHMENT)) || (disposition.equals(Part.INLINE)))) {
					fileName = mpart.getFileName();
					if (fileName.toLowerCase().indexOf("gb2312") != -1) {
						fileName = MimeUtility.decodeText(fileName);
					}
					saveFile(fileName, mpart.getInputStream());
				} else if (mpart.isMimeType("multipart/*")) {
					saveAttachMent(mpart);
				} else {
					fileName = mpart.getFileName();
					if ((fileName != null) && (fileName.toLowerCase().indexOf("GB2312") != -1)) {
						fileName = MimeUtility.decodeText(fileName);
						saveFile(fileName, mpart.getInputStream());
					}
				}
			}
		} else if (part.isMimeType("message/rfc822")) {
			saveAttachMent((Part) part.getContent());
		}
	}

	//Save attachment in the specified directory
	private static void saveFile(String fileName, InputStream in) throws Exception {

		try {
			fileName = MimeUtility.decodeText(fileName);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		String osName = System.getProperty("os.name");

		String storedir = null;
		//String storedir = getAttachPath();
		String separator = "";
		if (osName == null) osName = "";
		if (osName.toLowerCase().indexOf("win") != -1) {
			separator = "/";
			if (storedir == null || storedir.equals(""))
				storedir = Arguments.fileSavedPath;
		} else {
			separator = "/";
			storedir = "/tmp";
		}
		File storefile = new File(storedir + separator + fileName);
		System.out.println("storefile's¡¡path:¡¡" + storefile.toString());
		// for(int i=0;storefile.exists();i++){
		// storefile = new File(storedir+separator+fileName+i);
		// }
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;

		try {
			bos = new BufferedOutputStream(new FileOutputStream(storefile));
			bis = new BufferedInputStream(in);
			int c;
			while ((c = bis.read()) != -1) {
				bos.write(c);
				bos.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("ÎÄ¼þ±£´æÊ§°Ü!");
		} finally {
			bos.close();
			bis.close();
		}

	}

	public static void main(String[] args) {
		try {
			Properties pro = new Properties();
			Session session = Session.getInstance(pro);
			Store store = session.getStore("imap");
			store.connect("imap.163.com", 143, "mailtest10000@163.com", "qwerty");
			IMAPFolder f = (IMAPFolder)store.getFolder("INBOX");
			f.open(Folder.READ_WRITE);
			for(Message m : f.getMessages()){
				// has attachment
				if (isContainAttach(m)) {
					long uid = f.getUID(m);
					// not yet saved
					if (!MetaDataUtility.containUid(records, uid)) {
						System.out.println(uid);
						System.out.println(m.getSubject());
						//m.setFlag(Flags.Flag.DELETED, true);
						saveAttachMent(m);

						Record r = new Record(uid);
						records.add(r);
					}
				}
			}
			//f.expunge();
			//System.out.println(3);
			f.close(true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		MetaDataUtility.write(records);
	}

}
