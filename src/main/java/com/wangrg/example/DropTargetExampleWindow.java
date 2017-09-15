package com.wangrg.example;

import java.awt.Container;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.InputStream;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * by 王荣俊 on 2016/8/30.
 * http://bbs.csdn.net/topics/50488367
 * 各位老大，如何实现从java程序以外拖拽一个文件进入java程序的窗口，并得到文件的信息？
 */
public class DropTargetExampleWindow extends JFrame {

    public static void main(String[] args) {
        new DropTargetExampleWindow();
    }

    public DropTargetExampleWindow() {
        setTitle("DropTarget");
        setSize(300, 300);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        Container contentPane = getContentPane();
        JTextArea textArea
                = new JTextArea("Drop items into this text area.\n");
        new DropTarget(textArea, new TextDropTargetListener(textArea));
        contentPane.add(new JScrollPane(textArea), "Center");

        setVisible(true);

    }

    class TextDropTargetListener implements DropTargetListener {
        public TextDropTargetListener(JTextArea ta) {
            textArea = ta;
        }

        public void dragEnter(DropTargetDragEvent event) {
            int a = event.getDropAction();
            if ((a & DnDConstants.ACTION_COPY) != 0)
                textArea.append("ACTION_COPY\n");
            if ((a & DnDConstants.ACTION_MOVE) != 0)
                textArea.append("ACTION_MOVE\n");
            if ((a & DnDConstants.ACTION_LINK) != 0)
                textArea.append("ACTION_LINK\n");

            if (!isDragAcceptable(event)) {
                event.rejectDrag();
                return;
            }
        }

        public void dragExit(DropTargetEvent event) {
        }

        public void dragOver(DropTargetDragEvent event) {  // you can provide visual feedback here
        }

        public void dropActionChanged(DropTargetDragEvent event) {
            if (!isDragAcceptable(event)) {
                event.rejectDrag();
                return;
            }
        }

        public void drop(DropTargetDropEvent event) {
            if (!isDropAcceptable(event)) {
                event.rejectDrop();
                return;
            }

            event.acceptDrop(DnDConstants.ACTION_COPY);

            Transferable transferable = event.getTransferable();
            DataFlavor[] flavors = transferable.getTransferDataFlavors();
            for (int i = 0; i < flavors.length; i++) {
                DataFlavor flavor = flavors[i];
                textArea.append("MIME type=" + flavor.getMimeType() + "\n");

                try {
                    if (flavor.equals(DataFlavor.javaFileListFlavor)) {
                        java.util.List fileList = (java.util.List)
                                transferable.getTransferData(flavor);
                        Iterator iterator = fileList.iterator();
                        while (iterator.hasNext()) {
                            File f = (File) iterator.next();
                            textArea.append(f + "\n");
                        }
                    } else if (flavor.equals(DataFlavor.stringFlavor)) {
                        String s = (String)
                                transferable.getTransferData(flavor);
                        textArea.append(s + "\n");
                    } else if (flavor.isMimeTypeEqual("text/plain")) {
                        String charset = flavor.getParameter("charset");
                        InputStream in = (InputStream)
                                transferable.getTransferData(flavor);

                        boolean more = true;
                        int ch;
                        if (charset.equals("ascii")) {
                            do {
                                ch = in.read();
                                if (ch != 0 && ch != -1)
                                    textArea.append("" + (char) ch);
                                else more = false;
                            } while (more);
                        } else if (charset.equals("unicode")) {
                            boolean littleEndian = true;
                            // if no byte ordering mark, we assume
                            // Windows is the culprit
                            do {
                                ch = in.read();
                                int ch2 = in.read();
                                if (ch != -1 && littleEndian)
                                    ch = (ch & 0xFF) | ((ch2 & 0xFF) << 8);
                                if (ch == 0xFFFE)
                                    littleEndian = false;
                                else if (ch != 0 && ch != -1)
                                    textArea.append("" + (char) ch);
                                else more = false;
                            } while (more);
                        }

                        textArea.append("\n");
                    }
                } catch (Exception e) {
                    textArea.append("Error: " + e + "\n");
                }
            }
            event.dropComplete(true);
        }

        public boolean isDragAcceptable(DropTargetDragEvent event) {  // usually, you check the available data flavors here
            // in this program, we accept all flavors
            return (event.getDropAction()
                    & DnDConstants.ACTION_COPY_OR_MOVE) != 0;
        }

        public boolean isDropAcceptable(DropTargetDropEvent event) {  // usually, you check the available data flavors here
            // in this program, we accept all flavors
            return (event.getDropAction()
                    & DnDConstants.ACTION_COPY_OR_MOVE) != 0;
        }

        private JTextArea textArea;
    }

}
