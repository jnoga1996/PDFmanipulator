package com.jacek.pdfmanipulator.scheduler;

import com.jacek.pdfmanipulator.util.FileRemoverUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

@Component
public class FileRemoveScheduler {

    private final static long INTERVAL_ONE_MINUTE = 1000 * 60;
    private final static long INTERVAL_IN_MINUTES = 5 * INTERVAL_ONE_MINUTE;
    private final static Path FILE_DIR = Paths.get("storage");

    @Scheduled(fixedRate = INTERVAL_IN_MINUTES)
    public void tick() throws IOException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime currentDateTime = LocalDateTime.now();
        String formattedDateTime = dateTimeFormatter.format(currentDateTime);
        System.out.println("FileRemover started work at: " + formattedDateTime);
        Stream<Path> paths = Files.walk(FILE_DIR);
        paths.filter(p -> p != null)
                .filter(Files::isRegularFile)
                .filter(FileRemoverUtil::shouldBeRemoved)
                .forEach(FileRemoveScheduler::remove);
    }

    private static void remove(Path path) {
        try {
            System.out.println("Removing file: " + path.getFileName());
            Files.delete(path);
        } catch (Exception ex) {
            System.out.println("Removal of file: " + path.getFileName() + " failed!");
        }
    }

}
