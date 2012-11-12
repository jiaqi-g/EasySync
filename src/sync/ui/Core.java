package sync.ui;

import sync.receive.*;
import sync.send.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.swing.*;

import com.sun.mail.imap.IMAPFolder;

public class Core {
	
	private static JTextArea textArea;
	
	private static JButton syncButton = new JButton("Synchronize");
	private static JButton browserButton = new JButton("Browse");
	private static JLabel status = new JLabel("");
	
	static {
		syncButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				/**
				 *  can filter the file in the tmp directory!
				 */
				status.setText("Sending starts!");
				send();
				status.setText("Receiving starts!");
				receive();
				status.setText("Synchronization ends!");
				
				try {
					java.awt.Desktop.getDesktop().open(new java.io.File(Arguments.fileSavedPath));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		});
		
		browserButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					java.awt.Desktop.getDesktop().open(new java.io.File(Arguments.fileSavedPath));
				} catch (IOException e1) {
					e1.printStackTrace();
				}  
			}
			
		});
	}
	
	private static void send() {
		//send
		String smtpServer = Arguments.smtpServer;
		String from = Arguments.from;
		String displayName = Arguments.displayName;
		String username = Arguments.username;
		String password = Arguments.password;
		String to = Arguments.to;
		String subject = Arguments.subject;
		String content = Arguments.content;
		
		Set<String> set = Arguments.fileNameSet;
		
		Iterator<String> it = set.iterator();
		int i = 1;
		System.out.println(set.size());
		while (it.hasNext()) {
			Mail mail = new Mail(smtpServer, from, displayName, username, password, to, subject, content);
			mail.addAttachfile(it.next());
			mail.send();
			status.setText(i + "th project is sent successfully!");
			i++;
		}
		
		Arguments.fileNameSet.clear();
		textArea.setText("");
	}
	
	private static void receive() {
		//receive
		try {
			Properties pro = new Properties();
			Session session = Session.getInstance(pro);
			Store store = session.getStore("imap");
			store.connect(Arguments.imapServer, 143, Arguments.username, Arguments.password);
			IMAPFolder f = (IMAPFolder)store.getFolder("INBOX");
			f.open(Folder.READ_ONLY);
			
			int i = 1;
			for(Message m : f.getMessages()){	
				// has attachment
				if (IMAPReceiver.isContainAttach(m)) {
					long uid = f.getUID(m);
					// not yet saved
					if (!MetaDataUtility.containUid(IMAPReceiver.records, uid)) {
						System.out.println(uid);
						System.out.println(m.getSubject());
						//m.setFlag(Flags.Flag.DELETED, true);
						IMAPReceiver.saveAttachMent(m);

						// add records
						Record r = new Record(uid);
						IMAPReceiver.records.add(r);
					}
				}
				i++;
				status.setText(i + "th project is received successfully!");
			}
			
			//f.expunge();
			//System.out.println(3);
			f.close(true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		//persistant
		MetaDataUtility.write(IMAPReceiver.records);
	}
	
	public static void createInstance() {
		JFrame frame = new JFrame("Cloud Storage " + Arguments.version);
		frame.setSize(500, 600);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createTitledBorder("File Drag&drop Area"));
		textArea = new DropDragSupportTextArea();
		JScrollPane jsp = new JScrollPane();
		jsp.setViewportView(textArea);
		
		textArea.setColumns(40);
		textArea.setRows(20);
		
		JPanel southPanel = new JPanel();
		
		southPanel.add(status);
		southPanel.add(syncButton);
		southPanel.add(browserButton);
		
		panel.add(jsp, BorderLayout.CENTER );
		panel.add(southPanel, BorderLayout.SOUTH );
		
		frame.add(panel);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		createInstance();
	}
	
}