package com.db.rbazadatak.controller;

import com.db.rbazadatak.model.Person;
import com.db.rbazadatak.payload.FileRequestDTO;
import com.db.rbazadatak.service.FileService;
import com.db.rbazadatak.service.impl.FileServiceImpl;
import com.db.rbazadatak.service.impl.PersonServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
