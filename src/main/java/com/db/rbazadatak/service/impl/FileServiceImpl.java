package com.db.rbazadatak.service.impl;

import com.db.rbazadatak.model.File;
import com.db.rbazadatak.model.Person;
import com.db.rbazadatak.model.Status;
import com.db.rbazadatak.payload.FileRequestDTO;
import com.db.rbazadatak.repository.FileRepository;
import com.db.rbazadatak.service.FileService;
import com.db.rbazadatak.utils.FileUtils;
import com.db.rbazadatak.utils.OibValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService {

    private final static String DELIMITER = ",";
    private static String FILE_DIR;

    @Value("${file.path}")
    public void setFileDir(String fileDir) {
        FILE_DIR = fileDir;
    }

    private final FileRepository fileRepository;

    @Autowired
    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Transactional
    public File createFile(File file) throws IOException {
        String path = FILE_DIR + file.getOib() + System.currentTimeMillis() + ".txt";
        file.setFilePath(path);

        file = fileRepository.save(file);

        generateFile(file);

        return file;
    }

    private void generateFile(File file) throws IOException {
        String content = file.getFirstName() + DELIMITER + file.getLastName() + DELIMITER + file.getOib() + DELIMITER + file.getStatus();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.getFilePath()))) {
            writer.write(content);
        }
    }

    @Transactional
    public void setFileStatusToInactive(Person person) throws IOException {
        File file = person.getFile();

        file.setStatus(Status.INACTIVE);
        fileRepository.save(file);

        updateFileStatus(file);
    }

    private void updateFileStatus(File file) throws IOException {
        Path filePath = Paths.get(file.getFilePath());
        if (Files.exists(filePath)) {
            String content = file.getFirstName() + "," + file.getLastName() + "," + file.getOib() + "," + file.getStatus();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.getFilePath(), false))) { //false indicates overwrite
                writer.write(content);
            }
        } else {
            throw new FileNotFoundException("The file for the entity with file path:  " + file.getFilePath() + " does not exist.");
        }
    }

    public ResponseEntity<String> deleteFiles(FileRequestDTO fileRequestDTO) {
        OibValidation.checkOIB(fileRequestDTO.getOib());
        FileUtils.deleteFiles(fileRequestDTO.getOib(),fileRequestDTO.getStatus());
        return ResponseEntity.ok().body("Success");
    }
}