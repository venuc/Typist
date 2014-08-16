package org.typist.beans;

public class SourceTextBean {
    // This class will have the text as read from repos file
    private String text;
    private int status;

    public void setText(String s) {
        this.text = s.trim();
    }

    public String getText() {
        return this.text;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public int getStatus() {
        return this.status;
    }

    public String toString() {
        return this.text;
    }
}
