package com.db.rbazadatak.utils;

import com.db.rbazadatak.model.Person;
import com.db.rbazadatak.model.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Component
public class FileUtils {

    private final static String DELIMITER = ",";
    private static String FILE_DIR;

    @Value("${file.path}")
    public void setFileDir(String fileDir) {
        FILE_DIR = fileDir;
    }

    /**
     * Generates a file for a Person. If an active file for the person already exists, it logs an info message
     * and does not generate a new file. If an active file doesn't exist, it generates a new one with a timestamp,
     * and logs the success operation.
     *
     * @param person The person for whom the file is to be generated.
     */
    public static void generateFile(Person person) {
        Optional<File> activeFile = findActiveFile(person.getOib());

        if (activeFile.isPresent()) {
            log.debug("An active file already exists for this OIB");
            return;
        }

        String fileName = person.getOib() + "_" + System.currentTimeMillis() + ".txt";
        File file = new File(FILE_DIR + fileName);

        try {
            if (file.createNewFile()) {
                writePersonDetailsToFile(file, person);
                log.info("New person file created with filename: " + fileName);
            }
        } catch (IOException e) {
            log.error("File creation failed", e);
        }
    }

    /**
     * Writes the details of the person into the specified file.
     *
     * @param file   The file where the details should be written.
     * @param person The person whose details are to be written.
     */
    private static void writePersonDetailsToFile(File file, Person person) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            String personDetails = person.getFirstName() + DELIMITER +
                    person.getLastName() + DELIMITER +
                    person.getOib() + DELIMITER +
                    person.getStatus();

            writer.write(personDetails);
        }
    }

    /**
     * Sets the status of a person to INACTIVE in the person's file.
     * If no active file exists for the person, it logs an info message and does not update the status.
     *
     * @param person The person whose status is to be updated.
     */
    public static void setInactiveStatus(Person person) {
        Optional<File> activeFile = findActiveFile(person.getOib());

        if (activeFile.isEmpty()) {
            log.debug("No active file exists for OIB: " + person.getOib());
            return;
        }

        try {
            updatePersonStatusToFile(activeFile.get(), Status.INACTIVE);
            log.info("File status set to INACTIVE: " + activeFile.get().getName());
        } catch (IOException e) {
            log.error("File status update failed", e);
        }
    }

    /**
     * Updates the status of the person in the specified file.
     *
     * @param file   The file where the status should be updated.
     * @param status The new status to be set.
     */
    private static void updatePersonStatusToFile(File file, Status status) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();

            if (line != null) {
                String[] fileData = line.split(DELIMITER);
                if (fileData.length >= 4) {
                    fileData[3] = status.toString();
                    String newLine = String.join(DELIMITER, fileData);
                    try (FileWriter writer = new FileWriter(file, false)) {
                        writer.write(newLine);
                    }
                }
            }
        }
    }

    /**
     * Searches through files in the specified directory, and returns the first file
     * that starts with the person's OIB and contains the 'ACTIVE' status.
     *
     * @param oib The person for whom the active file is sought.
     * @return Optional containing the active file, or an empty Optional if no such file is found.
     */
    public static Optional<File> findActiveFile(String oib) {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(FILE_DIR), oib + "*")) {
            for (Path path : directoryStream) {
                File file = path.toFile();
                if (containsStatus(file, Status.ACTIVE)) {
                    return Optional.of(file);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading directory: " + FILE_DIR, e);
        }
        return Optional.empty();
    }

    /**
     * Checks if a file contains a specific status.
     *
     * @param file the file to check
     * @param status the status to look for
     * @return true if the status is found in the file, false otherwise
     */
    public static boolean containsStatus(File file, Status status) {
        try (Stream<String> lines = Files.lines(file.toPath(), StandardCharsets.UTF_8)) {
            return lines.anyMatch(line -> {
                String[] splitData = line.split(DELIMITER);
                return splitData.length >= 4 && status.toString().equals(splitData[3]);
            });
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + file.getName(), e);
        }
    }


    /**
     * Searches a specified directory for a file whose name starts with a specific identification number (oib).
     *
     * @param oib The specific identification number that the desired file's name should start with.
     * @return An Optional containing the first file found whose name starts with the given 'oib'. If no such file is found, the Optional is empty.
     * @throws IOException if an I/O error occurs
     */
    public static Optional<File> findFileByOib(String oib) throws IOException {
        Path directoryPath = Paths.get(FILE_DIR);

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directoryPath, oib + "*")) {
            Iterator<Path> iterator = directoryStream.iterator();
            return iterator.hasNext() ? Optional.of(iterator.next().toFile()) : Optional.empty();
        }
    }

    /**
     * Deletes files based on given oib and status parameters.
     *
     * @param oib the oib to match; if null, matches any oib
     * @param status the status to match; if null, matches any status
     */
    public static void deleteFiles(String oib, String status) {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(FILE_DIR))) {
            for (Path path : directoryStream) {
                if (isTextFile(path)) {
                    processFileDeletion(path, oib, status);
                }
            }
        } catch (IOException e) {
            log.error("Error accessing directory: " + FILE_DIR, e);
            throw new RuntimeException("Error accessing files", e);
        }
    }

    private static boolean isTextFile(Path filePath) {
        return Files.isRegularFile(filePath) && filePath.toString().endsWith(".txt");
    }

    private static void processFileDeletion(Path filePath, String oib, String status) {
        try (Stream<String> lines = Files.lines(filePath)) {
            boolean shouldDelete = lines.anyMatch(line -> {
                String[] parts = line.split(","); // assuming comma as delimiter
                if (parts.length <= 3) {
                    return false;
                }
                boolean oibMatches = oib == null || oib.equals(parts[2]);
                boolean statusMatches = status == null || status.equals(parts[3]);
                return oibMatches && statusMatches;
            });
            if (shouldDelete) {
                Files.delete(filePath);
            }
        } catch (IOException e) {
            log.error("Error processing file: " + filePath, e);
            throw new RuntimeException("Error deleting files", e);
        }
    }

}
