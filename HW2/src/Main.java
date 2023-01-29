import java.io.IOException;
import java.util.Scanner;

public class Main {
    static Group group;

    public static void main(String[] args) throws IOException {
        try {
            group = new Group("HW2/src/group.txt");
            int command;
            do {
                printMenu();
                command = getCommand(3, "Введите команду");
                switch (command) {
                    case 1 -> setMarkToRandomStudent();
                    case 2 -> printStudentWithMark();
                }
            } while (command != 3);
        } catch (Exception ex) {
            System.out.println("В приложении произошла ошибка:");
            System.out.println(ex.getMessage());
        }
        group.save();
    }

    private static void printStudentWithMark() {
        System.out.println("Список студентов с оценками:");
        for (Student student : group.getStudentsWithMark()) {
            System.out.println(student);
        }
        System.out.println();
    }

    private static void setMarkToRandomStudent() {
        Student randomStudent = group.randomNotMissingStudent();
        if (randomStudent != null) {
            System.out.printf("Случайный студент: %s\n\n", randomStudent);
            System.out.print(
                    """
                            Присутсвует ли на паре?
                            1. Да
                            2. Нет
                                            
                            """);
            int command = getCommand(2, "Введите команду");
            if (command == 1) {
                int mark = getCommand(10, "Введите оценку");
                randomStudent.isPresent = true;
                randomStudent.setMark(mark);
                System.out.println();
            } else {
                randomStudent.isPresent = false;
                System.out.println();
            }
        } else {
            System.out.println("Все студенты уже обработаны\n");
        }
    }

    private static int getCommand(Integer commandCount, String whatToAsk) {
        int command = -1;
        Scanner in = new Scanner(System.in);
        System.out.printf("%s(число от 1 до %d):\n> ", whatToAsk, commandCount);
        command = in.nextInt();
        while (command > commandCount || command < 1) {
            System.out.println("Неправильный формат входных данных!");
            System.out.printf("%s(число от 1 до %d):\n> ", whatToAsk, commandCount);
            command = in.nextInt();
        }
        return command;
    }

    private static void printMenu() {
        System.out.println(
                """
                Меню:
                1. Поставить оценку случайному студенту
                2. Показать список студентов с оценками
                3. Выход
                """);
    }
}