package com.db.rbazadatak.service;

import com.db.rbazadatak.model.File;
import com.db.rbazadatak.model.Person;
import com.db.rbazadatak.payload.FileRequestDTO;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface FileService {
    ResponseEntity<String> deleteFiles(FileRequestDTO fileRequestDTO) throws IOException;

    File createFile(File file) throws IOException;

    void setFileStatusToInactive(Person person) throws IOException;
}
