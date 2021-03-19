package com.jacek.pdfmanipulator.scheduler;

import com.jacek.pdfmanipulator.util.FileRemoverUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.stream.Stream;

@Component
public class FileRemoveScheduler {

    private static final long INTERVAL = 1000 * 60;

    @Scheduled(fixedRate = INTERVAL)
    public void tick() throws IOException {
        System.out.println("FileRemover started work at:" + LocalDateTime.now());
        Stream<Path> paths = Files.walk(Paths.get("src/main/resources/out"));
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
