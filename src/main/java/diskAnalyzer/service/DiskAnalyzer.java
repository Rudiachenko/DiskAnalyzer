package diskAnalyzer.service;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DiskAnalyzer {
    private static final String LETTER_TO_COUNT = "s";
    private static final String FIRST_LATTER_OF_ALPHABET_UPPERCASE = "A";
    private static final String FIRST_LATTER_OF_ALPHABET_LOWERCASE = "a";
    private static final int MAX_DEPTH = 5;
    private static final long VALUE_IF_CANT_GET_FILE_SIZE = 0;
    private static final int INIT_VALUE = 0;

    public String getPathWithMaximumNumberLetterS(String pathToPackage) {
        try {
            List<Path> paths = Files.walk(Path.of(pathToPackage), MAX_DEPTH)
                    .collect(Collectors.toList());

            int maximumNumberLetter = 0;
            Path resultPath = null;
            for (Path path : paths) {
                Path fileName = path.getFileName();
                int countMatches = StringUtils.countMatches(fileName.toString(), LETTER_TO_COUNT);
                if (countMatches > maximumNumberLetter) {
                    maximumNumberLetter = countMatches;
                    resultPath = path;
                }
            }

            return "The file name with the maximum number of letters ‘s’: " + resultPath;
        } catch (IOException e) {
            System.out.println("Can't process request " + e.getMessage());
            return null;
        }
    }

    public List<Path> getTopLargestFilesInBytes(String pathToPackage) {
        List<Path> pathsToFiles = getPathsToFiles(pathToPackage);

        if (pathsToFiles.isEmpty()) {
            return pathsToFiles;
        }

        Map<Long, Path> sizeFilePairs = getSizePathPairs(pathsToFiles);

        List<Long> keys = new ArrayList<>(sizeFilePairs.keySet());
        List<Path> largestFiles = new ArrayList<>();
        int elementToAdd = keys.size() - 1;
        int counter = 0;
        while (counter < 5 && elementToAdd >= 0) {
            Path path = sizeFilePairs.get(keys.get(elementToAdd));
            largestFiles.add(path);
            counter++;
            elementToAdd--;
        }

        return largestFiles;
    }

    public String getAverageFileSize(String pathToPackage) {
        List<Path> pathsToFiles = getPathsToFiles(pathToPackage);
        long averageFileSize = (long) (pathsToFiles.stream()
                .mapToLong(this::getFileSize)
                .average()
                .orElse(VALUE_IF_CANT_GET_FILE_SIZE));
        return "The average file size in bytes: " + averageFileSize;
    }

    public String getNumberOfFilesAndFoldersDividedByA(String pathToPackage) {
        List<Path> pathsToFiles = getPathsToFilesAndFolders(pathToPackage);
        AtomicInteger folders = new AtomicInteger(INIT_VALUE);
        AtomicInteger files = new AtomicInteger(INIT_VALUE);

        pathsToFiles.stream()
                .filter(path -> path.getFileName().toString().startsWith(FIRST_LATTER_OF_ALPHABET_UPPERCASE)
                        || path.getFileName().toString().startsWith(FIRST_LATTER_OF_ALPHABET_LOWERCASE))
                .forEach(path -> {
                    if (Files.isDirectory(path)) {
                        incrementValue(folders);
                    } else {
                        incrementValue(files);
                    }
                });

        return String.format("%d files and %d folders begin with the letter 'A'", folders.longValue(), files.longValue());
    }

    private void incrementValue(AtomicInteger value) {
        value.incrementAndGet();
    }

    private List<Path> getPathsToFiles(String pathToPackage) {
        try {
            return Files.walk(Path.of(pathToPackage), MAX_DEPTH)
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Can't get files paths");
            return new ArrayList<>();
        }
    }

    private List<Path> getPathsToFilesAndFolders(String pathToPackage) {
        try {
            return Files.walk(Path.of(pathToPackage), MAX_DEPTH)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Can't get files and folders paths");
            return new ArrayList<>();
        }
    }

    private Map<Long, Path> getSizePathPairs(List<Path> pathsToFiles) {
        Map<Long, Path> sizePathPairs = new TreeMap<>();
        for (Path path : pathsToFiles) {
            sizePathPairs.put(getFileSize(path), path);
        }
        return sizePathPairs;
    }

    private long getFileSize(Path path) {
        try {
            return Files.size(path);
        } catch (IOException e) {
            System.out.println("can't get file size " + path);
            return VALUE_IF_CANT_GET_FILE_SIZE;
        }
    }
}
