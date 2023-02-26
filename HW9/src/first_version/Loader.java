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

/**
 * Скачивает файлы по url асинхронно.
 * Метод load не является блокирующим.
 * При закрытии остановит скачивание файлов.
 */
public class Loader implements Closeable {
    /**
     * Количество байтов, которые будут считаны по url и записаны в файл между проверками,
     * что поток прерван.
     */
    private static final int BYTE_COUNT_PER_TIME = 500;

    /**
     * Путь к папке, в которую надо скачивать файлы.
     */
    private String path;

    /**
     * Пул из 1 потока. Нужен, чтобы можно было запускать скачивание новых файлов
     * и не ждать пока они скачаются.
     */
    private final ExecutorService executorService;

    /**
     * Создает новый Loader, который будет скачивать файлы по данному пути.
     *
     * @throws IllegalArgumentException Если папки по такому пути не существует.
     */
    public Loader(String path) {
        Path pathObject = Path.of(path);
        if (Files.exists(pathObject) && Files.isDirectory(pathObject)) {
            this.path = path;
            executorService = Executors.newSingleThreadExecutor();
        } else {
            throw new IllegalArgumentException("Аргумент path должен указывать на существующую на компьютере папку");
        }
    }

    /**
     * Скачивает файлы по списку url в папку. Скачивание происходит асинхронно.
     * Если скачивание не удалось, то ничего об этом не сообщает.
     *
     * @param urls url, по которым надо скачать файлы.
     * @throws MalformedURLException Если формат url неверный.
     */
    public void load(String[] urls) throws MalformedURLException {
        for (var url : urls) {
            URL urlObject = new URL(url);
            executorService.submit(() -> load(urlObject));
        }
    }

    /**
     * Меняет путь к папке, в которую должны скачиваться файлы.
     * Если до этого выполнялся метод load и не все файлы еще скачаны,
     * то некоторые файлы могут скачаться по новому пути.
     *
     * @param newPath Путь к папке, в которую должны скачиваться файлы.
     */
    public void changePath(String newPath) {
        Path pathObject = Path.of(newPath);
        if (Files.exists(pathObject) && Files.isDirectory(pathObject)) {
            this.path = newPath;
        } else {
            throw new IllegalArgumentException("Аргумент path должен указывать на существующую на компьютере папку");
        }
    }

    /**
     * Возвращает путь к папке, в которую должны скачиваться файлы.
     *
     * @return Путь к папке, в которую должны скачиваться файлы.
     */
    public String getPath() {
        return path;
    }

    /**
     * Скачивает файл по данному url.
     *
     * @param urlObject url по которому нужно скачать файл.
     */
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

    /**
     * Генерирует по данному url имя для файла, отличное от других имен файлов в папке.
     *
     * @param url url, по которому нужно сгенерировать имя.
     * @return Имя для файла.
     */
    private String generateNewFileName(URL url) {
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

    /**
     * Завершает скачивание файлов(почти сразу).
     */
    @Override
    public void close() {
        executorService.shutdownNow();
        executorService.close();
    }
}
