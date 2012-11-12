package sync.receive;

import java.io.*;
import java.util.ArrayList;

public class MetaDataUtility {
	private static String separator = "/";
	private static String storedir = "c:/tmp";
	private static File storeFile = new File(storedir + separator + "metadata");
	//private static ArrayList<Record> list = new ArrayList<Record>();
	
	public static boolean containUid(ArrayList<Record> list, long uid) {
		for (Record r : list) {
			if (r.getUid() == uid) {
				return true;
			}
		}
		return false;
	}
	
	public static ArrayList<Record> read() {
		if (!storeFile.exists()) {
			return new ArrayList<Record>();
		}
		
		ArrayList<Record> list = new ArrayList<Record>();;
		try {
			ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(storeFile)));
			list = (ArrayList<Record>) in.readObject();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("read metadata successfully!");
		
		return list;
	}
	
	public static void write(ArrayList<Record> list) {
		if (!storeFile.exists()) {
			try {
				storeFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(storeFile)));
			out.writeObject(list);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Write metadata successfully!");
	}
}
