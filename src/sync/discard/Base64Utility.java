package sync.discard;

import it.sauronsoftware.base64.Base64;
import java.io.File;
import java.io.IOException;

public class Base64Utility {

	/**
	 * The string to be decoded must have the form ¡°=?charset?B?xxxxxxxx?=¡±
	 * @return
	 */
	public static String decodeAttachmentName(String s) throws Exception {
		String charset = "";
		int i = 2;
		if ((s.charAt(0) == '=') && (s.charAt(1) == '?')) {
			while (true) {
				if (s.charAt(i) == '?') break;
				charset += s.charAt(i);
				i++;
			}
			i += 3;
		}

		StringBuffer sb = new StringBuffer();
		while (true) {
			if (s.charAt(i) == '?') break;
			sb.append(s.charAt(i));
			i++;
		}

		return Base64.decode(sb.toString(), charset);
	}

	public static void main(String[] args) {
		//File source = new File("E:/source.jpg");
		//File encoded = new File("E:/encoded.b64");
		//File decoded = new File("E:/decoded.b64");

		String out = Base64.decode("=CD=BC=C6=D7=B2=E3Orz.txt");
		System.out.println(out);
		/*
		try {
			Base64.encode(source, encoded);
			Base64.decode(encoded, decoded);
		} catch (IOException e) {
			e.printStackTrace();
		}
		 */
		try {
			System.out.println(decodeAttachmentName("=?GBK?Q?=CD=BC=C6=D7=B2=E3Orz.txt?="));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}