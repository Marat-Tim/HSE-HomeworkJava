import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Student {
    private List<Integer> markHistory;
    private final String name;
    Boolean isPresent;
    private Integer mark;

    private Student(String name, ArrayList<Integer> markHistory) {
        this.name = name;
        this.markHistory = markHistory;
    }

    static Student parseStudent(String str) {
        String name = str.substring(0, str.indexOf(':'));
        ArrayList<Integer> markHistory = new ArrayList<>();
        try {
            for (String markStr : str.substring(str.indexOf(':') + 2).split(", ")) {
                markHistory.add(Integer.parseInt(markStr));
            }
        } catch (Exception ignored) {}
        return new Student(name, markHistory);
    }

    public List<Integer> getMarkHistory() {
        return (List<Integer>)((ArrayList<Integer>)markHistory).clone();
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public String serialize() {
        StringJoiner joiner = new StringJoiner(", ");
        for (Integer i : markHistory) {
            joiner.add(i.toString());
        }
        if (mark == null) {
            return name + ": " + joiner.toString();
        }
        if (joiner.length() == 0) {
            return name + ": " + mark;
        }
        return name + ": " + joiner.toString() + ", " + mark;
    }

    @Override
    public String toString() {
        if (isPresent != null && isPresent)
            return String.format("%s: %d", name, mark);
        return name.toString();
    }
}
