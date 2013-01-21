package org.free.jse.ui;

import java.awt.Component;
import java.awt.GridLayout;
import java.io.File;
import javax.swing.Icon;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.free.jse.model.FileSelectorModel;
import org.free.jse.utils.FileUtil;

public class FileTreePanel extends JPanel {

    private static final long serialVersionUID = 869344186012597994L;
    private JTree tree;

    public FileTreePanel(String directory) {
        tree = new JTree(new FileSelectorModel(directory));
        initialize(directory);
        setLayout(new GridLayout(1, 1));
        add(tree);
    }

    public void initialize(String directory) {
        DefaultTreeCellRenderer dtcr = new DefaultTreeCellRenderer() {

            private static final long serialVersionUID = -8477048655725437222L;

            public Component getTreeCellRendererComponent(
                    JTree tree,
                    Object value,
                    boolean sel,
                    boolean expanded,
                    boolean leaf,
                    int row,
                    boolean hasFocus) {

                super.getTreeCellRendererComponent(
                        tree, value, sel,
                        expanded, leaf, row,
                        hasFocus);

                File currentFile = (File) value;
                if (currentFile.isDirectory()
                        && (currentFile.list() == null
                        || currentFile.list().length == 0)) {
                    if (expanded) {
                        setIcon(openIcon);
                    } else {
                        setIcon(closedIcon);
                    }
                }

                return this;
            }
        };

        dtcr.setLeafIcon(FileUtil.createImageIcon("text-file.png"));
        
        tree.setCellRenderer(dtcr);
    }

    public JTree getTree() {
        return this.tree;
    }

    /*
    public static void main(String[] args) {
        File cur = new File(".");
        FileTreePanel panel = new FileTreePanel(cur.getAbsolutePath());
        JFrame frame = new JFrame("tree");

        frame.setSize(600, 480);
        frame.getContentPane().add(panel);

        frame.setVisible(true);
    }
     */
}
