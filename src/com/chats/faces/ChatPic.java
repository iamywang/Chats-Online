package com.chats.faces;

import javax.swing.*;
import java.net.URL;

public class ChatPic extends ImageIcon {

    private static final long serialVersionUID = 1L;
    int im;

    public int getIm() {
        return im;
    }

    public void setIm(int im) {
        this.im = im;
    }

    public ChatPic(URL url, int im) {
        super(url);
        this.im = im;
    }
}
