import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Random;

/**
 * Хранит информацию о книге.
 * @param title Название.
 * @param description Описание.
 * @param authors Авторы.
 * @param yearOfPublication Год публикации.
 */
public record Book(String title,
                   String description,
                   String authors,
                   Date yearOfPublication) {

    /**
     * Формат для преобразования книги в строку.
     */
    private static final String FORMAT = """
            Книга "%s":
            Описание: "%s"
            Авторы: "%s"
            Год публикации "%s"
            """;

    /**
     * Возвращает случайную книгу, используя данный генератор случайных слов.
     * @param randomWords Генератор случайных слов.
     * @return Случайная книга.
     */
    public static Book randomBook(RandomWords randomWords) {
        Random random = new Random(new Date().getTime());
        String title = randomWords.nextText(random.nextInt(3) + 1);
        String description = randomWords.nextText(random.nextInt(11) + 10);
        String authors = randomWords.nextText(random.nextInt(5) + 1);
        Date date = new Date(random.nextLong(100000000));
        return new Book(title, description, authors, date);
    }

    /**
     * Преобразует книгу в красивую строку.
     * @return Строковое представление книги.
     */
    @Override
    public String toString() {
        SimpleDateFormat formatForDate = new SimpleDateFormat("dd.MM.yyyy");
        return String.format(FORMAT,
                title, description, authors, formatForDate.format(yearOfPublication));
    }
}
