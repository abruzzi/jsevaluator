package org.free.jse.ui;

import java.awt.GridLayout;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ConsolePanel extends JPanel {

    private static final long serialVersionUID = -8518732344904274626L;
    PipedInputStream piOut;
    PipedInputStream piErr;
    PipedOutputStream poOut;
    PipedOutputStream poErr;
    JTextArea textArea = new JTextArea();

    public ConsolePanel(){
        PrintStream con = new PrintStream(new TextAreaOutputStream(textArea));
        System.setOut(con);
        System.setErr(con);

        textArea.setEditable(true);

        setLayout(new GridLayout(1, 1));
        add(new JScrollPane(textArea));

    }

    public void clear() {
        textArea.setText("");
    }
}
