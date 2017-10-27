package com.wangrj.java_lib.java_util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * by Administrator on 2016/3/1.
 */
public class ClipboardUtil {

    private static Timer timer;
    private static String previousClipboardString;

    public interface OnClipboardStringChangeListener {
        void onClipboardStringChange(String text);
    }

    public static void setOnClipboardStringChangeListener(final OnClipboardStringChangeListener listener) {
        try {
            previousClipboardString = getSystemClipboard();
        } catch (Exception e) {
            previousClipboardString = e.toString();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String currentClipboardString;
                try {
                    currentClipboardString = getSystemClipboard();
                } catch (Exception e) {
                    currentClipboardString = e.toString();
                }
                if (!previousClipboardString.equals(currentClipboardString)) {
                    previousClipboardString = currentClipboardString;
                    listener.onClipboardStringChange(previousClipboardString);
                }
            }
        }, 500, 500);
    }

    public static void removeClipboardStringChangeListener() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public static void setSystemClipboard(String text) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = new StringSelection(text);
        clipboard.setContents(contents, null);
    }

    public static String getSystemClipboard() throws Exception {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable t = clipboard.getContents(null);

        if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            String data = (String) t.getTransferData(DataFlavor.stringFlavor);
            return data;
        } else {
            throw new Exception("not string");
        }

    }
}
