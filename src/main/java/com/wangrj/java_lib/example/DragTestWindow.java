package com.wangrj.java_lib.example;

import java.awt.Container;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * by 王荣俊 on 2016/8/30.
 */
public class DragTestWindow extends JFrame {

    public static void main(String[] args) {
        new DragTestWindow();
    }

    public DragTestWindow() {
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

    class TextDropTargetListener extends DropTargetAdapter {

        private JTextArea textArea;

        public TextDropTargetListener(JTextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void drop(DropTargetDropEvent event) {

            event.acceptDrop(DnDConstants.ACTION_COPY);

            Transferable transferable = event.getTransferable();
            DataFlavor[] flavors = transferable.getTransferDataFlavors();
            for (DataFlavor flavor : flavors) {
                if (flavor.equals(DataFlavor.javaFileListFlavor)) {
                    try {

                        List fileList = (List) transferable.getTransferData(flavor);
                        for (Object file : fileList) {
                            File f = (File) file;
                            textArea.append(f + "\n");
                        }

                    } catch (UnsupportedFlavorException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
