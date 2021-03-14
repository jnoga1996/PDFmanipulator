package com.jacek.pdfmanipulator.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class FileRemoverUtil {

    private static int intervalInMinutes;

    @Autowired
    public FileRemoverUtil(@Value("${scheduler.intervalInMinutes}") int intervalInMinutes) {
        this.intervalInMinutes = intervalInMinutes;
    }

    public static boolean isOlderThan(Path path) {
        System.out.println("Interval = " + intervalInMinutes);
        LocalDateTime currentTime = LocalDateTime.now();
        return path.toFile().lastModified() > currentTime.minusMinutes(intervalInMinutes).toEpochSecond(ZoneOffset.UTC);
    }

    public static boolean isPDF(Path path) {
        return path.toFile().getName().toUpperCase().contains(".PDF");
    }

    public static boolean shouldBeRemoved(Path path) {
        return isOlderThan(path) && isPDF(path);
    }
}
