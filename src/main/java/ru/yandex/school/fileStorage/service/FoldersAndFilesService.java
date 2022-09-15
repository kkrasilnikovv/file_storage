package ru.yandex.school.fileStorage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.school.fileStorage.exception.NotFoundException;
import ru.yandex.school.fileStorage.exception.ValidationException;
import ru.yandex.school.fileStorage.model.File;
import ru.yandex.school.fileStorage.model.Folder;
import ru.yandex.school.fileStorage.model.Item;
import ru.yandex.school.fileStorage.model.UploadObject;
import ru.yandex.school.fileStorage.storage.ItemStorage;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;


@Service
public class FoldersAndFilesService {
    private final ItemStorage storage;

    @Autowired
    public FoldersAndFilesService(ItemStorage storage) {
        this.storage = storage;
    }

    public ResponseEntity<String> divisionIntoObjects(UploadObject uploadObject) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Instant instant = Instant.parse(uploadObject.getUpdateDate());

            for (Item item : uploadObject.getItems()) {
                if (item.getType().equals("FOLDER")) {
                    if (item.getUrl() != null || item.getSize() != null || item.getId() == null) {
                        throw new ValidationException("Validation Failed");
                    }
                    Folder folder = new Folder("FOLDER", null, item.getId(), item.getParentId(), null,
                            uploadObject.getUpdateDate(), new ArrayList<>());
                    if (storage.getItemById(folder.getId()) != null) {
                        storage.updateItem(folder);
                    } else {
                        storage.addItems(folder);
                    }
                } else if (item.getType().equals("FILE")) {
                    if (item.getUrl() == null || item.getChildren() != null || item.getId() == null || item.getSize() <= 0
                            || item.getSize() == null || item.getUrl().length() > 255) {
                        throw new ValidationException("Validation Failed");
                    }
                    File file = new File("FILE", item.getUrl(), item.getId(), item.getParentId(), item.getSize(),
                            uploadObject.getUpdateDate(), null);
                    if (storage.getItemById(file.getId()) != null) {
                        storage.updateItem(file);
                    } else {
                        storage.addItems(file);
                    }
                } else {
                    throw new ValidationException("Validation Failed");
                }
            }
            return new ResponseEntity<>("The addition/update was successful", HttpStatus.OK);
        } catch (DateTimeParseException e) {
            throw new ValidationException("Validation Failed");
        }
    }

    public ResponseEntity<String> deleteItem(String id) {
        if (storage.getItemById(id) != null) {
            storage.removeItem(id);
            return new ResponseEntity<>("The deletion was successful", HttpStatus.OK);
        } else {
            throw new NotFoundException("Item not found");
        }
    }

    public Item getItemById(String id) {
        if (storage.getItemById(id) != null) {
            Item item = new Item(storage.getItemById(id).getType(), storage.getItemById(id).getUrl(),
                    storage.getItemById(id).getId(), storage.getItemById(id).getParentId(),
                    storage.getItemById(id).getSize(), storage.getItemById(id).getDateOfUpdate(),
                    storage.getItemById(id).getChildren());
            if (item.getChildren() != null && !item.getChildren().isEmpty()) {
                // ArrayList<String> children = new ArrayList<>(storage.getItemById(id).getChildren());
                ArrayList<String> temp = new ArrayList<>();
                for (String childrenId : item.getChildren()) {
                    if (storage.getItemById(childrenId).getType().equals("FOLDER")) {
                        temp.add(getItemById(childrenId).toString());
                        item.addSize(getItemById(childrenId).getSize());
                    } else {
                        temp.add(storage.getItemById(childrenId).toString());
                        item.addSize(storage.getItemById(childrenId).getSize());
                    }
                }
                item.setChildren(temp);
            }
            return item;

        } else {
            throw new NotFoundException("Item not found");
        }

    }
}
