import java.util.ArrayList;
import java.util.Scanner;

/**
 * Student Grade Management System - PSP Assignment Part 2 (ii)
 *
 * Menu based console program. The user adds students with their marks,
 * the program calculates each student's average and letter grade, and
 * shows a results table with the class average.
 *
 * Compile and run:  javac GradeManager.java  then  java GradeManager
 */
public class GradeManager {

    static Scanner sc = new Scanner(System.in);
    static ArrayList<Student> students = new ArrayList<>();
    static int subjectCount;

    public static void main(String[] args) {
        System.out.println("===== Student Grade Management System =====");
        subjectCount = readInt("How many subjects does each student take? (1-8): ", 1, 8);

        int choice;
        do {
            System.out.println();
            System.out.println("1. Add student");
            System.out.println("2. Show results");
            System.out.println("3. Exit");
            choice = readInt("Choose an option: ", 1, 3);
            if (choice == 1) {
                addStudent();
            } else if (choice == 2) {
                showResults();
            }
        } while (choice != 3);
        System.out.println("Goodbye!");
    }

    static void addStudent() {
        String name = "";
        while (name.isEmpty()) {
            System.out.print("Student name: ");
            name = sc.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("  Name cannot be empty.");
            }
        }
        double[] marks = new double[subjectCount];
        for (int i = 0; i < subjectCount; i++) {
            marks[i] = readMark("  Mark for subject " + (i + 1) + ": ");
        }
        Student s = new Student(name, marks);
        students.add(s);
        System.out.printf("Saved. Average = %.2f, Grade = %s%n", s.average, s.grade);
    }

    static void showResults() {
        // without this check the class average was printed as NaN (bug found in TC09)
        if (students.isEmpty()) {
            System.out.println("No students added yet.");
            return;
        }
        System.out.println("------------------------------------------");
        System.out.printf("%-4s %-20s %8s %6s%n", "No", "Name", "Average", "Grade");
        System.out.println("------------------------------------------");
        double total = 0;
        Student top = students.get(0);
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            System.out.printf("%-4d %-20s %8.2f %6s%n", i + 1, s.name, s.average, s.grade);
            total += s.average;
            if (s.average > top.average) {
                top = s;
            }
        }
        System.out.println("------------------------------------------");
        System.out.printf("Class average: %.2f%n", total / students.size());
        System.out.println("Top student: " + top.name);
    }

    // reads the whole line and converts it, because nextInt() left the newline
    // behind and the next name was skipped (bug found in TC03)
    static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                int n = Integer.parseInt(line);
                if (n >= min && n <= max) {
                    return n;
                }
                System.out.println("  Enter a number from " + min + " to " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input, enter a whole number.");
            }
        }
    }

    static double readMark(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                double m = Double.parseDouble(line);
                if (m >= 0 && m <= 100) {
                    return m;
                }
                System.out.println("  Mark must be between 0 and 100.");
            } catch (NumberFormatException e) {
                System.out.println("  Invalid mark, enter a number like 78 or 65.5");
            }
        }
    }

    // average rounded to 2 decimals so the grade matches the printed average
    static double average(double[] marks) {
        double sum = 0;
        for (double m : marks) {
            sum += m;
        }
        return Math.round(sum / marks.length * 100) / 100.0;
    }

    // simplified university scale (no + / - grades)
    static String letterGrade(double avg) {
        if (avg >= 85) {
            return "A";
        } else if (avg >= 70) {
            return "B";
        } else if (avg >= 50) {
            return "C";
        } else if (avg >= 40) {
            return "D";
        } else {
            return "F";
        }
    }
}

class Student {
    String name;
    double[] marks;
    double average;
    String grade;

    Student(String name, double[] marks) {
        this.name = name;
        this.marks = marks;
        this.average = GradeManager.average(marks);
        this.grade = GradeManager.letterGrade(this.average);
    }
}
