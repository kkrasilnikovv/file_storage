package ru.yandex.school.fileStorage.model;

import java.util.ArrayList;
public class Folder extends Item{
    public Folder(String type, String url, String id, String parentId, Integer size, String dateOfUpdate,
                  ArrayList<String> children) {
        super(type, url, id, parentId, size, dateOfUpdate, children);
    }

}
