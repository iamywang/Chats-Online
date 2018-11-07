package com.chats.friendlists;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ImageListModel extends AbstractListModel<User> {

    private static final long serialVersionUID = 1L;

    private List<User> imageUser = new ArrayList<User>();

    public void addElement(User user) {
        this.imageUser.add(user);
    }

    @Override
    public int getSize() {
        return imageUser.size();
    }

    @Override
    public User getElementAt(int index) {
        return imageUser.get(index);
    }

}