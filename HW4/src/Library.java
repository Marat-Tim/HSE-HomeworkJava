import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Library {
    private final Set<Book> books = new HashSet<>();

    public Library() {
    }

    public Library(String path) throws IOException {
        RandomWords randomWords = new RandomWords(path);
        Random random = new Random(new Date().getTime());
        for (int i = 0; i < random.nextInt(100); ++i) {
            books.add(Book.randomBook(randomWords));
        }
    }

    public Set<Book> getAllBooks() {
        return (Set<Book>) ((HashSet<Book>) books).clone();
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    public int size() {
        return books.size();
    }

    public List<Book> getByTitle(String title) {
        return books.stream().filter(book -> book.title().equals(title)).toList();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public Collection<String> getAllTitles() {
        return books.stream().map(Book::title).toList();
    }
}
