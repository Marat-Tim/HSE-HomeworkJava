package first_version;

/**
 * Нужен чтобы можно было возвращать из метода пару из команды и ее аргументов.
 * @param command Команда.
 * @param args Аргументы команды.
 */
public record Command(String command, String[] args) {
}


