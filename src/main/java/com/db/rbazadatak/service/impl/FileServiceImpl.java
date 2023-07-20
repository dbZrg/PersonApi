package com.db.rbazadatak.service.impl;

import com.db.rbazadatak.payload.FileRequestDTO;
import com.db.rbazadatak.service.FileService;
import com.db.rbazadatak.utils.FileUtils;
import com.db.rbazadatak.utils.OibValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    public ResponseEntity<String> deleteFiles(FileRequestDTO fileRequestDTO) {
        OibValidation.checkOIB(fileRequestDTO.getOib());
        FileUtils.deleteFiles(fileRequestDTO.getOib(),fileRequestDTO.getStatus());
        return ResponseEntity.ok().body("Success");
    }

}
