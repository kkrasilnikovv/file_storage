package ru.yandex.school.fileStorage.model;

import lombok.Data;
import java.util.ArrayList;

@Data
public class Item {
    private String type;
    private String url;
    private String id;
    private String parentId;
    private Integer size;
    private String dateOfUpdate;
    private ArrayList<String> children;

    public Item(String type, String url, String id, String parentId, Integer size, String dateOfUpdate, ArrayList<String> children) {
        this.type = type;
        this.url = url;
        this.id = id;
        this.parentId = parentId;
        this.size = size;
        this.dateOfUpdate = dateOfUpdate;
        this.children = children;
    }

    public void addChildren(String childrenId) {
        children.add(childrenId);
    }

    public void removeChildren(String childrenId) {
        children.remove(childrenId);
    }

    public void addSize(Integer size) {
        if(size!=null) {
            if (this.getSize() == null) {
                this.setSize(size);
            } else {
                this.setSize(this.getSize() + size);
            }
        }
    }

    public void removeSize(Integer size) {
        if (size != null && this.getSize()!=null) {
            this.setSize(this.getSize() - size);
        }
    }
}
