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
		//注册DropTarget，并将它与组件相连，处理哪个组件的相连
		//即连通组件（第一个this）和Listener(第二个this)
		dropTarget = new DropTarget(this,DnDConstants.ACTION_COPY_OR_MOVE, this, true);
	}

	/**
	 * 拖入文件或字符串,这里只说明能拖拽，并未打开文件并显示到文本区域中
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