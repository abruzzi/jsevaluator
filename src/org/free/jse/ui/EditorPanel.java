package org.free.jse.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;
import jsyntaxpane.DefaultSyntaxKit;

import org.free.jse.utils.FileUtil;

public class EditorPanel extends JPanel {

    class Pair{
        private String display;
        private String real;

        public Pair(String display, String real){
            this.display = display;
            this.real = real;
        }

        public String getDisplay(){
            return this.display;
        }

        public String getReal(){
            return this.real;
        }

        public String getRealByDisplay(String display){
            if(this.display.equals(display)){
                return this.real;
            }else{
                return null;
            }
        }
    }
    
    private static final long serialVersionUID = 6580449884049559621L;
    private JTabbedPane tabs;
    private Icon icon;

    private Map<Pair, Component> mapping;
    //private Hashtable<String, Component> mapping;

    public EditorPanel() {
        tabs = new JTabbedPane();
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        icon = FileUtil.createImageIcon("javascript.png");
        mapping = new HashMap<Pair, Component>();
        setLayout(new GridLayout(1, 1));
        add(tabs);
    }

    public void newTab(String file) {
        newTab(new File(file));
    }

    public void newTab(File file) {
    }

    public void openNewFile(String file) {
        openNewFile(new File(file));

    }

    public void saveActiveFile(){
        final Pair pair = getActivePair();
        final String content = getActiveFileContent();

        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                FileWriter fw = null;

                try {
                    File f = new File(pair.getReal());
                    fw = new FileWriter(f);
                    fw.write(content);
                    fw.close();
                } catch (IOException ex) {
                    Logger.getLogger(EditorPanel.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        fw.close();
                    } catch (IOException ex) {
                        Logger.getLogger(EditorPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }
    
    public void openNewFile(File file) {
        int current = -1;
        if(file == null){
            current = -1;
        }else{
            current = tabs.indexOfTab(file.getName());
        }

        if (current >= 0) {
            tabs.setSelectedIndex(current);
            return;
        }
        
        JPanel editorPanel = new JPanel(false);
        
        DefaultSyntaxKit.initKit();

        JEditorPane codeEditor = new JEditorPane();
        JScrollPane scrPane = new JScrollPane(codeEditor);
        
        codeEditor.setContentType("text/javascript;charset=utf-8");
        codeEditor.setFont(new java.awt.Font("YaHei Consolas Hybrid", 0, 14));

        if(file.getName().equals("__noname__")){
            codeEditor.setText("");
        }else{
            codeEditor.setText(FileUtil.getContents(file));
        }
        
        editorPanel.setLayout(new GridLayout(1, 1));
        editorPanel.add(scrPane);
        
        tabs.addTab(file.getName(), icon, editorPanel);
        tabs.setSelectedComponent(editorPanel);

        mapping.put(new Pair(file.getName(), file.getAbsolutePath()), codeEditor);
        //mapping.put(file.getName(), codeEditor);
    }

    private void closeFile(Pair pair) {
        //check if saved or not
        int current = tabs.indexOfTab(pair.getDisplay());
        if (current >= 0) {
            mapping.remove(pair);
            tabs.removeTabAt(current);
        }
    }

    public void closeCurrent(){
        Pair pair = getActivePair();
        if(pair == null){
            return;
        }

        closeFile(pair);
    }

    private Pair getActivePair(){
        Iterator<Pair> it = mapping.keySet().iterator();
        while (it.hasNext()) {
            Pair pair = it.next();
            if (tabs.indexOfTab(pair.getDisplay()) == tabs.getSelectedIndex()) {
                return pair;
            }
        }
        return null;
    }
    
    public String getActiveFileContent() {
        Pair pair = getActivePair();
        if(pair == null){
            return null;
        }

        return ((JTextComponent)mapping.get(pair)).getText();
    }

    /*
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception exc) {
            System.err.println("Error loading L&F: " + exc);
        }

        JFrame frame = new JFrame("editor tester");
        frame.setBackground(Color.lightGray);

        EditorPanel editor = new EditorPanel();
        editor.openNewFile("json.js");
        editor.openNewFile("style.js");
        editor.openNewFile("system.js");

        editor.openNewFile("style.js");
        
        frame.add(editor);
        frame.getContentPane().add("Center", editor);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        System.err.println(editor.getActiveFileContent());
        frame.setSize(600, 480);
        //frame.pack();
        //frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    */
}
