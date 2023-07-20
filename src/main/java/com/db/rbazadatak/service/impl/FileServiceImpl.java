package com.db.rbazadatak.service.impl;

import com.db.rbazadatak.model.Person;
import com.db.rbazadatak.payload.FileRequestDTO;
import com.db.rbazadatak.payload.PersonDTO;
import com.db.rbazadatak.repository.PersonRepository;
import com.db.rbazadatak.service.FileService;
import com.db.rbazadatak.service.PersonService;
import com.db.rbazadatak.utils.FileUtils;
import com.db.rbazadatak.utils.OibValidation;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    public ResponseEntity<String> deleteFiles(FileRequestDTO fileRequestDTO) {
        OibValidation.checkOIB(fileRequestDTO.getOib());
        FileUtils.deleteFiles(fileRequestDTO.getOib(),fileRequestDTO.getStatus());
        return ResponseEntity.ok().body("Success");
    }

}
