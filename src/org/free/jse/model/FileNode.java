package org.free.jse.model;
public class FileNode extends java.io.File {

    public FileNode(String directory) {
        super(directory);
    }

    public FileNode(FileNode parent, String child) {
        super(parent, child);
    }

    public String toString() {
        return (getName().length() == 0 ? getPath() : getName() );
    }
}