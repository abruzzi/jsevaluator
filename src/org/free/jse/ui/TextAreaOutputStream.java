package org.free.jse.ui;

import java.io.*;
import java.util.*;
import javax.swing.*;

public class TextAreaOutputStream
        extends OutputStream {

// *****************************************************************************
// INSTANCE PROPERTIES
// *****************************************************************************
    private JTextArea textArea;                               // target text area
    private int maxLines;                               // maximum lines allowed in text area
    private LinkedList lineLengths;                            // length of lines within text area
    private int curLength;                              // length of current line
    private byte[] oneByte;                                // array for write(int val);

// *****************************************************************************
// INSTANCE CONSTRUCTORS/INIT/CLOSE/FINALIZE
// *****************************************************************************
    public TextAreaOutputStream(JTextArea ta) {
        this(ta, 1000);
    }

    public TextAreaOutputStream(JTextArea ta, int ml) {
        /*
        if (ml < 1) {
        throw new IoEscape(IoEscape.GENERAL, "Maximum lines of " + ml + " in TextAreaOutputStream constructor is not permitted");
        }
         * 
         */
        if (ml < 1) {
            ml = 1000;
        }

        textArea = ta;
        maxLines = ml;
        lineLengths = new LinkedList();
        curLength = 0;
        oneByte = new byte[1];
    }

// *****************************************************************************
// INSTANCE METHODS - ACCESSORS
// *****************************************************************************
    public synchronized void clear() {
        lineLengths = new LinkedList();
        curLength = 0;
        textArea.setText("");
    }

    /** Get the number of lines this TextArea will hold. */
    public synchronized int getMaximumLines() {
        return maxLines;
    }

    /** Set the number of lines this TextArea will hold. */
    public synchronized void setMaximumLines(int val) {
        maxLines = val;
    }

// *****************************************************************************
// INSTANCE METHODS
// *****************************************************************************
    public void close() {
        if (textArea != null) {
            textArea = null;
            lineLengths = null;
            oneByte = null;
        }
    }

    public void flush() {
    }

    public void write(int val) {
        oneByte[0] = (byte) val;
        write(oneByte, 0, 1);
    }

    public void write(byte[] ba) {
        write(ba, 0, ba.length);
    }

    public synchronized void write(byte[] ba, int str, int len) {
        try {
            curLength += len;
            if (bytesEndWith(ba, str, len, LINE_SEP)) {
                lineLengths.addLast(new Integer(curLength));
                curLength = 0;
                if (lineLengths.size() > maxLines) {
                    textArea.replaceRange(null, 0, ((Integer) lineLengths.removeFirst()).intValue());
                }
            }
            for (int xa = 0; xa < 10; xa++) {
                try {
                    textArea.append(new String(ba, str, len));
                    break;
                } catch (Throwable thr) {                                                 // sometimes throws a java.lang.Error: Interrupted attempt to aquire write lock
                    if (xa == 9) {
                        thr.printStackTrace();
                    } else {
                        Thread.sleep(200);
                    }
                }
            }
        } catch (Throwable thr) {
            CharArrayWriter caw = new CharArrayWriter();
            thr.printStackTrace(new PrintWriter(caw, true));
            textArea.append(System.getProperty("line.separator", "\n"));
            textArea.append(caw.toString());
        }
    }

    private boolean bytesEndWith(byte[] ba, int str, int len, byte[] ew) {
        if (len < LINE_SEP.length) {
            return false;
        }
        for (int xa = 0, xb = (str + len - LINE_SEP.length); xa < LINE_SEP.length; xa++, xb++) {
            if (LINE_SEP[xa] != ba[xb]) {
                return false;
            }
        }
        return true;
    }
// *****************************************************************************
// STATIC PROPERTIES
// *****************************************************************************
    static private byte[] LINE_SEP = System.getProperty("line.separator", "\n").getBytes();

    /*
    public static void main(String[] args) {
        JTextArea area = new JTextArea();
        PrintStream con = new PrintStream(new TextAreaOutputStream(area));
        System.setOut(con);
        System.setErr(con);

        JFrame frame = new JFrame();
        frame.add(area);
        frame.setSize(400, 400);
        frame.setVisible(true);

        for (int i = 0; i < 10; i++) {
            System.err.println("err : hello, world" + i);
            System.out.println("out : hello, world" + i);
        }
    }
     */
} /* END PUBLIC CLASS */
