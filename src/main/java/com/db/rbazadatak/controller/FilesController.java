package com.db.rbazadatak.controller;

import com.db.rbazadatak.payload.FileRequestDTO;
import com.db.rbazadatak.service.impl.FileServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/file/")
public class FilesController {

    final FileServiceImpl fileService;

    @Autowired
    public FilesController(FileServiceImpl fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    @Operation(summary = "Delete all files, or specific depending on oib and status")
    public ResponseEntity<String> deleteFiles(@RequestBody FileRequestDTO fileRequestDTO) {
        return fileService.deleteFiles(fileRequestDTO);
    }

}
