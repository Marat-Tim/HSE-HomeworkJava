package first_version;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Loader implements Closeable {
    private static final int BYTE_COUNT_PER_TIME = 500;

    private String path;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public Loader(String path) {
        Path pathObject = Path.of(path);
        if (Files.exists(pathObject) && Files.isDirectory(pathObject)) {
            this.path = path;
        } else {
            throw new IllegalArgumentException("Аргумент path должен указывать на существующую на компьютере папку");
        }
    }

    public void load(String[] urls) throws MalformedURLException {
        for (var url : urls) {
            URL urlObject = new URL(url);
            executorService.submit(() -> load(urlObject));
        }
        System.out.println("DEBUG");
    }

    public void changePath(String newPath) {
        Path pathObject = Path.of(newPath);
        if (Files.exists(pathObject) && Files.isDirectory(pathObject)) {
            this.path = newPath;
        } else {
            throw new IllegalArgumentException("Аргумент path должен указывать на существующую на компьютере папку");
        }
    }

    public String getPath() {
        return path;
    }

    private void load(URL urlObject) {
        try {
            String fileName = generateNewFileName(urlObject);
            Path filePath = new File(path, fileName).toPath();
            Files.createFile(filePath);
            try (OutputStream outputStream = new FileOutputStream(filePath.toString());
                 InputStream inputStream = urlObject.openStream()) {
                while (inputStream.available() >= BYTE_COUNT_PER_TIME) {
                    if (Thread.currentThread().isInterrupted()) {
                        Files.delete(filePath);
                        return;
                    }
                    outputStream.write(inputStream.readNBytes(BYTE_COUNT_PER_TIME));
                }
                outputStream.write(inputStream.readAllBytes());
            }
        } catch (IOException ignored) {
        }
    }

    private String generateNewFileName(URL url) throws MalformedURLException {
        String decodedUrl = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8);
        int lastSlash = decodedUrl.lastIndexOf('/');
        int lastDot = decodedUrl.lastIndexOf('.');
        String fileName;
        String fileExtension;
        if (lastSlash > 0 && lastDot > lastSlash) {
            fileName = decodedUrl.substring(
                    decodedUrl.lastIndexOf('/'),
                    decodedUrl.lastIndexOf('.'));
            fileExtension = decodedUrl.substring(decodedUrl.lastIndexOf('.'));
        } else {
            fileName = "file";
            fileExtension = "";
        }
        int i = 0;
        while (new File(path, fileName + (i == 0 ? "" : i) + fileExtension).exists()) {
            i++;
        }
        return fileName + (i == 0 ? "" : i) + fileExtension;
    }

    @Override
    public void close() {
        executorService.shutdownNow();
        executorService.close();
    }
}
