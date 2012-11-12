package sync.ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.util.List;
import javax.swing.JTextArea;

import sync.receive.Arguments;

/**
 * @author Administrator
 */
public class DropDragSupportTextArea extends JTextArea implements DropTargetListener {

	static final int MAXSIZE = 20 *1024 * 1024;
	
	private DropTarget dropTarget;

	public DropDragSupportTextArea() {
		//ע��DropTarget������������������������ĸ����������
		//����ͨ�������һ��this����Listener(�ڶ���this)
		dropTarget = new DropTarget(this,DnDConstants.ACTION_COPY_OR_MOVE, this, true);
	}

	/**
	 * �����ļ����ַ���,����ֻ˵������ק����δ���ļ�����ʾ���ı�������
	 */
	public void dragEnter(DropTargetDragEvent dtde) {
		DataFlavor[] dataFlavors = dtde.getCurrentDataFlavors();
		if(dataFlavors[0].match(DataFlavor.javaFileListFlavor)){
			try {
				Transferable tr = dtde.getTransferable();
				Object obj = tr.getTransferData(DataFlavor.javaFileListFlavor);
				List<File> files = (List<File>) obj;
				for(int i = 0; i < files.size(); i++){
					if (!files.get(i).isDirectory() && files.get(i).length() < MAXSIZE) {
						String fileName = files.get(i).getAbsolutePath();
						if (!Arguments.fileNameSet.contains(fileName)){
							append(fileName + "\r\n");
							Arguments.fileNameSet.add(fileName);
							//System.out.println(Arguments.fileNameSet);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void dragOver(DropTargetDragEvent dtde) {

	}

	public void dropActionChanged(DropTargetDragEvent dtde) {

	}

	public void dragExit(DropTargetEvent dte) {

	}

	public void drop(DropTargetDropEvent dtde) {

	}
}