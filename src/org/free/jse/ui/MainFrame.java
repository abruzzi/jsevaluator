package org.free.jse.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.tree.TreePath;

import org.free.jse.model.FileNode;
import org.free.jse.utils.FileUtil;
import org.free.jse.utils.ScriptEvaluator;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = -3400121443659063868L;
    private FileTreePanel explorer;
    private EditorPanel editor;
    private ConsolePanel console;

    public MainFrame() {
        initComponents();
    }

    private void initComponents() {
        File init = new java.io.File(".");
        explorer = new FileTreePanel(init.getAbsolutePath());
        editor = new EditorPanel();
        console = new ConsolePanel();

        final JTree tree = explorer.getTree();
        tree.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    TreePath tp = tree.getClosestPathForLocation(evt.getX(), evt.getY());
                    FileNode selectedNode = (FileNode) tp.getLastPathComponent();
                    if (selectedNode.isFile()) {
                        editor.openNewFile(selectedNode);
                    }
                }
            }
        });
    }

    public void render() {
        JSplitPane env = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                editor, console);

        env.setContinuousLayout(true);
        env.setOneTouchExpandable(true);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(explorer), env);

        split.setContinuousLayout(true);
        split.setOneTouchExpandable(true);

        getContentPane().add(split);

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Simple file editor");
        setVisible(true);

        env.setDividerLocation(0.7);
        split.setDividerLocation(0.3);
    }
    
    private NewFileAction newFileAction = new NewFileAction();
    private LoadAction loadAction = new LoadAction();
    private CloseAction closeAction = new CloseAction();
    private SaveAction saveAction = new SaveAction();
    private EvalAction evalAction = new EvalAction();
    private ClearAction clearAction = new ClearAction();
    private ExitAction exitAction = new ExitAction();
    private CopyAction copyAction = new CopyAction();
    private CutAction cutAction = new CutAction();
    private PasteAction pasteAction = new PasteAction();
    private AboutAction aboutAction = new AboutAction();

    class SaveAction extends AbstractAction {
        public SaveAction() {
            super("Save", FileUtil.createImageIcon("save.png"));
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            editor.saveActiveFile();
        }
    }

    class CopyAction extends AbstractAction {
        public CopyAction() {
            super("Copy", FileUtil.createImageIcon("copy.png"));
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    class CutAction extends AbstractAction {
        public CutAction() {
            super("Cut", FileUtil.createImageIcon("cut.png"));
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    class PasteAction extends AbstractAction {
        public PasteAction() {
            super("Paste", FileUtil.createImageIcon("paste.png"));
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    class AboutAction extends AbstractAction {
        public AboutAction() {
            super("About", FileUtil.createImageIcon("about.png"));
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            AboutDialog about = new AboutDialog(null, true);
            about.setLocationRelativeTo(null);
            about.setVisible(true);
        }
    }

    public JMenuBar getMenubar() {
        return createMenubar();
    }

    class TMenuItem extends JMenuItem{
        TMenuItem(Action action, KeyStroke keyStroke){
            super(action);
            setAccelerator(keyStroke);
        }
    }

    private Map<String, AbstractAction> actionMap = new HashMap(){
        {
            put("CTRL O", new LoadAction());
            put("CTRL S", new SaveAction());
            put("ALT F4", new ExitAction());
            put("F5", new EvalAction());
            put("F7", new ClearAction());
        }
    };
    
    //        new HashMap<Integer, AbstractAction>();

    private JMenuBar createMenubar() {
        JMenuBar mb = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.add(new TMenuItem(newFileAction, KeyStroke.getKeyStroke("ctrl N")));
        fileMenu.addSeparator();
        fileMenu.add(new TMenuItem(loadAction, KeyStroke.getKeyStroke("ctrl O")));
        fileMenu.add(new TMenuItem(saveAction, KeyStroke.getKeyStroke("ctrl S")));
        fileMenu.add(new TMenuItem(closeAction, KeyStroke.getKeyStroke("ctrl Q")));
        fileMenu.add(new TMenuItem(evalAction, KeyStroke.getKeyStroke("F5")));
        fileMenu.add(new TMenuItem(clearAction, KeyStroke.getKeyStroke("alt C")));
        fileMenu.addSeparator();
        fileMenu.add(new TMenuItem(exitAction, KeyStroke.getKeyStroke("alt F4")));

        JMenu editMenu = new JMenu("Edit");
        editMenu.add(copyAction);
        editMenu.add(cutAction);
        editMenu.add(pasteAction);


        JMenu helpMenu = new JMenu("Help");
        helpMenu.add(new TMenuItem(aboutAction, KeyStroke.getKeyStroke("F1")));

        mb.add(fileMenu);
        mb.add(editMenu);
        mb.add(helpMenu);

        return mb;
    }

    public JToolBar getTooblar() {
        return createToolbar();
    }

    private JToolBar createToolbar() {
        JToolBar toolbar = new JToolBar();

        toolbar.add(newFileAction);
        toolbar.addSeparator();

        toolbar.add(loadAction);
        toolbar.add(closeAction);
        toolbar.add(evalAction);
        toolbar.add(clearAction);

        toolbar.addSeparator();

        return toolbar;
    }

    class CloseAction extends AbstractAction {

        public CloseAction() {
            super("close", FileUtil.createImageIcon("close.png"));
        }

        public void actionPerformed(ActionEvent e) {
            editor.closeCurrent();
        }
    }

    class LoadAction extends AbstractAction {

        public LoadAction() {
            super("Load", FileUtil.createImageIcon("open.png"));
        }

        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new java.io.File("."));
            chooser.setDialogTitle("choose a javascript file");
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);

            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File newFile = chooser.getSelectedFile();
                editor.openNewFile(newFile);
            }
        }
    }

    class NewFileAction extends AbstractAction {

        public NewFileAction() {
            super("New", FileUtil.createImageIcon("new.png"));
        }

        public void actionPerformed(ActionEvent e) {
            editor.openNewFile("__noname__");
        }
    }

    class EvalAction extends AbstractAction {

        public EvalAction() {
            super("Eval", FileUtil.createImageIcon("terminal.png"));
        }

        public void actionPerformed(ActionEvent e) {
            String script = editor.getActiveFileContent();
            if(script == null){
                return;
            }
            ScriptEvaluator.evalScript(editor.getActiveFileContent());
        }
    }

    class ClearAction extends AbstractAction {

        public ClearAction() {
            super("Clear", FileUtil.createImageIcon("clear.png"));
        }

        public void actionPerformed(ActionEvent e) {
            console.clear();
        }
    }

    class ExitAction extends AbstractAction {

        public ExitAction() {
            super("Exit", FileUtil.createImageIcon("exit.png"));
        }

        public void actionPerformed(ActionEvent e) {
            int y = JOptionPane.showConfirmDialog(
                    null,
                    "Confirm exit",
                    "Confirm Exit Dialog",
                    JOptionPane.YES_NO_OPTION);
            if (y == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception exc) {
            System.err.println("Error loading L&F: " + exc);
        }

        MainFrame frame = new MainFrame();
        frame.setJMenuBar(frame.getMenubar());
        frame.add(frame.getTooblar(), BorderLayout.NORTH);
        frame.render();
    }
}
