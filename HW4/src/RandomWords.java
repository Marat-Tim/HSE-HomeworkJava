import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Отвечает за генерацию случайных слов. Коллекцию слов берет из файла.
 */
public class RandomWords {
    /**
     * Штука, отвечающая за рандом.
     */
    private final Random random = new Random(new Date().getTime());

    /**
     * Массив слов.
     */
    private final ArrayList<String> words = new ArrayList<>();

    /**
     * Сохраняет все слова из заданного файла.
     * @param path Путь к файлу.
     * @throws IOException Если произошла ошибка при работе с файлом.
     */
    public RandomWords(String path) throws IOException {
        String text = Files.readString(Paths.get(path));
        words.addAll(Arrays.asList(text.split("\\s")));
    }

    /**
     * Возвращает случайное слово из файла.
     * @return Случайное слово из файла.
     */
    public String nextWord() {
        return words.get(random.nextInt(words.size()));
    }

    /**
     * Возвращает текст заданной длины из случайных слов.
     * @param wordCount Длина текста.
     * @return Текст из случайных слов.
     */
    public String nextText(int wordCount) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        for (int i = 0; i < wordCount; ++i) {
            stringJoiner.add(nextWord());
        }
        return stringJoiner.toString();
    }
}
