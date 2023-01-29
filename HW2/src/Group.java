import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;

public class Group {
    private final ArrayList<Student> students = new ArrayList<>();
    private final String path;

    Group(String path) throws IOException {
        this.path = path;
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.isEmpty()) {
                students.add(Student.parseStudent(line));
            }
        }
        reader.close();
    }

    void save() throws IOException {
        StringJoiner joiner = new StringJoiner("\n");
        for (Student student : students) {
            joiner.add(student.serialize());
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        writer.write(joiner.toString());
        writer.close();
    }

    Student randomNotMissingStudent() {
        boolean allStudentsAreProcessed = true;
        for (Student student : students) {
            if (student.isPresent == null) {
                allStudentsAreProcessed = false;
                break;
            }
        }
        if (allStudentsAreProcessed) {
            return null;
        }
        Random rand = new Random();
        Student student;
        do {
            student = students.get(rand.nextInt(students.size()));
        } while (student.isPresent != null);
        return student;
    }

    List<Student> getStudentsWithMark() {
        return students.stream().
                filter(student -> (student.isPresent != null &&student.isPresent)).toList();
    }
}
