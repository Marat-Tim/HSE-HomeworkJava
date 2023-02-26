package first_version;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    static String HELP_TEXT = """
            Файлы будут скачиваться в папку %s \s
            Список команд:
            /help - показ всех команд и справки
            /load URL - загрузка файла по урлу
            /load URL1 URL2 URL3 и т.д - загрузка файлов по урлам
            /dest PATH указать путь куда скачивать файлы
                        
            """;

    static Scanner SCANNER = new Scanner(System.in);

    static String getDownloadsPath() throws IOException {
        final String downloadsName = "Downloads";
        File downloadsFile = new File(System.getProperty("user.home"), downloadsName);
        if (downloadsFile.exists()) {
            return downloadsFile.getPath();
        }
        int i = 1;
        Path path = Path.of(downloadsName);
        while (Files.exists(path) && Files.isRegularFile(path)) {
            path = Path.of(downloadsName + i);
            ++i;
        }
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
        downloadsFile = new File(System.getProperty("user.dir"), path.toString());
        if (downloadsFile.exists()) {
            return downloadsFile.getPath();
        }
        return path.toString();
    }

    static Command getCommand() {
        System.out.print("> ");
        String[] splitInput = SCANNER.nextLine().split(" ");
        return new Command(splitInput[0],
                Arrays.copyOfRange(splitInput, 1, splitInput.length));
    }

    static void printHelp(Loader loader) {
        System.out.printf(HELP_TEXT, loader.getPath());
    }

    static void loadCommand(Loader loader, String[] args) {
        if (args.length == 0) {
            System.out.println("Нужно указать путь до файла, который нужно скачать");
        } else {
            try {
                loader.load(args);
                System.out.println("Попытка скачать файл началась");
            } catch (MalformedURLException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

    static void destCommand(Loader loader, String[] args) {
        if (args.length != 1) {
            System.out.println("Нужно ввести путь до папки");
        } else {
            try {
                loader.changePath(args[0]);
                System.out.println("Путь изменен");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
    }

    public static void main(String[] args) {
        try (Loader loader = new Loader(getDownloadsPath())) {
            Command command;
            printHelp(loader);
            do {
                command = getCommand();
                switch (command.command()) {
                    case "/help" -> printHelp(loader);
                    case "/load" -> loadCommand(loader, command.args());
                    case "/dest" -> destCommand(loader, command.args());
                    case "/exit" -> {
                    }
                    default -> System.out.println("Некорректная команда");
                }
            } while (!"/exit".equals(command.command()));
            System.out.println("Завершение работы...");
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}