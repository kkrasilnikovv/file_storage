package ru.yandex.school.fileStorage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.school.fileStorage.model.Item;
import ru.yandex.school.fileStorage.model.UploadObject;
import ru.yandex.school.fileStorage.service.FoldersAndFilesService;

@RestController
@Validated
public class Controller {
    private final FoldersAndFilesService service;

    @Autowired
    private Controller(FoldersAndFilesService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> addNode(@RequestBody UploadObject uploadObject) {
        return service.divisionIntoObjects(uploadObject);
    }

    @DeleteMapping("/{nodeId}")
    public ResponseEntity<String> deleteNode(@PathVariable String nodeId) {
        return service.deleteItem(nodeId);
    }

    @GetMapping("/{nodeId}")
    public Item getItem(@PathVariable String nodeId) {
        return service.getItemById(nodeId);
    }
}
