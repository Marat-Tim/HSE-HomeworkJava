import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Loader {
    private String path;

    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    public Loader(String path) {
        Path pathObject = Path.of(path);
        if (Files.exists(pathObject) && Files.isDirectory(pathObject)) {
            this.path = path;
        } else {
            throw new IllegalArgumentException(
                    "Аргумент path должен указывать на существующую на компьютере папку");
        }
    }

    public Loader() throws IOException {
        this.path = getDownloadsPath();
    }

    public void load(String[] urls) {
        for (var url : urls) {
            executorService.submit(() -> load(url));
        }
    }

    public void changePath(String newPath) {
        Path pathObject = Path.of(newPath);
        if (Files.exists(pathObject) && Files.isDirectory(pathObject)) {
            this.path = newPath;
        } else {
            throw new IllegalArgumentException(
                    "Аргумент path должен указывать на существующую на компьютере папку");
        }
    }

    public String getPath() {
        return path;
    }

    private void load(String url) {
        try {
            URL urlObject = new URL(url);
            String fileName = generateNewFileName(urlObject);
            Files.copy(
                    urlObject.openStream(),
                    new File(path, fileName).toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
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

    private String getDownloadsPath() throws IOException {
        final String downloadsName = "Downloads";
        if (new File(System.getProperty("user.home"), downloadsName).exists()) {
            return new File(System.getProperty("user.home"), downloadsName).getPath();
        }
        int i = 1;
        Path path = Path.of(downloadsName);
        while (Files.exists(path) &&
                Files.isRegularFile(path)) {
            path = Path.of(downloadsName + i);
            ++i;
        }
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
        return new File(System.getProperty("user.dir"), path.toString()).getPath();
    }
}
