package com.lxg.root.imageviewer;

import java.io.Serializable;
import java.util.List;

/**
 * Created by root on 15-10-20.
 */
public class DATA implements Serializable
{
    private int position;
    private String path;
    private List<String> names;

    public List<String> getNames() {
        return names;
    }

    public String getPath() {
        return path;
    }

    public int getPosition() {
        return position;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
