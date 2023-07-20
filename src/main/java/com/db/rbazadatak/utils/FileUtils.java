package com.db.rbazadatak.utils;

import com.db.rbazadatak.model.Person;
import com.db.rbazadatak.model.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Scanner;

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
        File activeFile = findActiveFile(person);

        if (activeFile != null) {
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
        File activeFile = findActiveFile(person);

        if (activeFile == null) {
            log.debug("No active file exists for OIB: " + person.getOib());
            return;
        }

        try {
            updatePersonStatusToFile(activeFile, Status.INACTIVE);
            log.info("File status set to INACTIVE: " + activeFile.getName());
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
     * Deletes all files for a person that start with the person's OIB.
     * It checks each file in the directory, and if the file name starts with the person's OIB, it deletes the file.
     *
     * @param person The person whose files are to be deleted.
     */
    public static void deleteAllFilesForPerson(Person person) {
        File directory = new File(FILE_DIR);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.getName().startsWith(person.getOib())) {
                    if (file.delete()) {
                        log.info("File deleted: " + file.getName());
                    } else {
                        log.error("Failed to delete file: " + file.getName());
                    }
                }
            }
        }
    }

    /**
     * Searches through files in the specified directory, and returns the first file
     * that starts with the person's OIB and contains the 'ACTIVE' status.
     *
     * @param person The person for whom the active file is sought.
     * @return The active file, or null if no such file is found.
     */
    public static File findActiveFile(Person person) {
        File directory = new File(FILE_DIR);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.getName().startsWith(person.getOib()) && containsStatus(file, Status.ACTIVE)) {
                    return file;
                }
            }
        }
        return null;
    }

    /**
     * Checks if a file contains the 'ACTIVE' status.
     *
     * @param file The file to be checked.
     * @return True if the file contains the 'ACTIVE' status, false otherwise.
     */
    public static boolean containsStatus(File file, Status status) {
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                String[] splitData = data.split(DELIMITER);
                if (splitData.length >= 4 && status.toString().equals(splitData[3])) {
                    return true;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + file.getName(), e);
        }
        return false;
    }


    /**
     * This method searches a specified directory for a file whose name starts with a specific identification number (oib).
     *
     * @param oib The specific identification number that the desired file's name should start with.
     * @return The first file found whose name starts with the given 'oib'. If no such file is found, the method returns null.
     */
    public static File findFileByOib(String oib) throws IOException {
        Path directoryPath = Paths.get(FILE_DIR);

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(
                directoryPath,
                path -> path.toFile().getName().startsWith(oib)
        )) {

            Iterator<Path> iterator = directoryStream.iterator();
            if (iterator.hasNext()) {
                return iterator.next().toFile();
            } else {
                return null;
            }
        }
    }


}
