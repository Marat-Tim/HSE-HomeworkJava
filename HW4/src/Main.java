import java.io.IOException;
import java.util.*;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);

    private static final String PATH_TO_FILE = "HW4\\src\\input.txt";

    private static final String MENU = """
            Напишите команду:
            %s - список ваших книг
            %s - список книг в библиотеке
            %s - взять книгу из библиотеки
            %s - вернуть книгу в библиотеку
            %s - выйти
            """;

    private static final List<String> COMMAND_LIST = List.of("/list", "/all", "/get", "/put", "/exit");

    public static void main(String[] args) {
        Library library;
        try {
            library = new Library(PATH_TO_FILE);
        } catch (IOException ex) {
            System.out.printf("Проблема при работе с файлом %s\n%s\n", PATH_TO_FILE, ex.getMessage());
            return;
        }
        Library myLibrary = new Library();
        String command;
        do {
            System.out.printf(MENU + "\n", COMMAND_LIST.toArray());
            command = getStringCommand(COMMAND_LIST);
            if (COMMAND_LIST.get(0).equals(command)) {
                System.out.println("Ваши книги:");
                printAllBooks(myLibrary);
            }
            if (COMMAND_LIST.get(1).equals(command)) {
                System.out.println("Книги в библиотеке:");
                printAllBooks(library);
            }
            if (COMMAND_LIST.get(2).equals(command)) {
                moveBook(library, myLibrary);
            }
            if (COMMAND_LIST.get(3).equals(command)) {
                moveBook(myLibrary, library);
            }
        } while (!COMMAND_LIST.get(4).equals(command));
    }

    private static void printAllBooks(Library library) {
        int i = 1;
        System.out.println("------------------------------------");
        for (Book book : library.getAllBooks()) {
            System.out.printf("%d. %s\n", i++, book);
        }
        System.out.println("------------------------------------");
    }

    private static void moveBook(Library from, Library to) {
        System.out.println("Введите название книги");
        String title = getStringCommand(from.getAllTitles());
        List<Book> booksWithThisTitle = from.getByTitle(title);
        if (booksWithThisTitle.size() == 1) {
            to.addBook(booksWithThisTitle.get(0));
            from.removeBook(booksWithThisTitle.get(0));
            return;
        }
        System.out.println("Книг с таким названием несколько:");
        int i = 1;
        for (Book book : from.getByTitle(title)) {
            System.out.printf("%d. %s\n", i++, book);
        }
        System.out.println("Выберите одну из них");
        int command = getIntCommand(1, booksWithThisTitle.size());
        to.addBook(booksWithThisTitle.get(command - 1));
        from.removeBook(booksWithThisTitle.get(command - 1));
    }

    private static int getIntCommand(int from, int to) {
        do {
            try {
                System.out.printf("Введите число от %d до %d\n> ", from, to);
                int command = SCANNER.nextInt();
                if (command > to || command < from) {
                    System.out.printf("Число должно быть от %d до %d\n", from, to);
                    continue;
                }
                return command;
            } catch (InputMismatchException ex) {
                System.out.println("Неправильный формат ввода");
            } catch (Exception ex) {
                System.out.println("Непредвиденная ошибка");
            }
        } while (true);
    }

    private static String getStringCommand(Collection<String> commandList) {
        do {
            try {
                System.out.print("> ");
                String command = SCANNER.nextLine();
                if (!commandList.contains(command)) {
                    System.out.println("Такой команды не существует");
                    continue;
                }
                return command;
            } catch (Exception ex) {
                System.out.println("Непредвиденная ошибка");
            }
        } while (true);
    }
}