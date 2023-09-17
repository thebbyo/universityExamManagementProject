import com.mysql.cj.util.DnsSrv;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class Main {

    void addCources(String url, String userName, String password){
        Scanner sc = new Scanner(System.in);
        try {

            Connection connection = DriverManager.getConnection(url, userName, password);
            System.out.println("Database connected");
            Statement statement = connection.createStatement();
            String countSQL = "SELECT COUNT(*) FROM courses";
            ResultSet resultSet = statement.executeQuery(countSQL);
            int cntCourse = 0;
            while (resultSet.next()) {
                cntCourse = resultSet.getInt(1);
            }
            cntCourse++;
            System.out.print("Number of courses to add: ");
            int numOfCourses = sc.nextInt();
            sc.nextLine();
            for (int i = cntCourse; i <= cntCourse+numOfCourses-1; i++) {
                System.out.print("Enter course name: ");
                String courseName = sc.nextLine();
                System.out.print("Enter course credit: ");
                double courseCredit = sc.nextDouble();
                sc.nextLine();
                String insertSQL = "INSERT INTO courses  VALUES (?, ?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                preparedStatement.setInt(1,i);
                preparedStatement.setString(2, courseName);
                preparedStatement.setDouble(3, courseCredit);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Data inserted successfully.");
                } else {
                    System.out.println("Data insertion failed.");
                }

                preparedStatement.close();
            }

            connection.close();

        } catch (SQLException e) {
            System.out.println("Database operation failed");
            e.printStackTrace();
        }
    }

    void addStudents(String url, String userName, String password) {
        Scanner sc = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url, userName, password);
            System.out.println("Database connected");
            Statement statement = connection.createStatement();
            String countSQL = "SELECT COUNT(*) FROM students";
            ResultSet resultSet = statement.executeQuery(countSQL);
            int cntStudent = 0;
            while (resultSet.next()) {
                cntStudent = resultSet.getInt(1);
            }
            cntStudent++;
            System.out.print("Number of students to add: ");
            int numOfStudent = sc.nextInt();
            sc.nextLine();
            for (int i = cntStudent; i <= cntStudent + numOfStudent - 1; i++) {
                System.out.print("Enter student name: ");
                String studentName = sc.nextLine();
                String insertSQL = "INSERT INTO students (studentID, studentName) VALUES (?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                preparedStatement.setInt(1, i);
                preparedStatement.setString(2, studentName);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Data inserted successfully.");
                } else {
                    System.out.println("Data insertion failed.");
                }
                preparedStatement.close();
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Failed connection");
            e.printStackTrace();
        }
    }
    void addExam(String url, String userName, String  password){
        Scanner sc = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url, userName, password);
            System.out.println("Database connected");
            Statement statement = connection.createStatement();
            String countSQL = "SELECT COUNT(*) FROM exams";
            ResultSet resultSet = statement.executeQuery(countSQL);
            int cntExam = 0;
            while (resultSet.next()) {
                cntExam = resultSet.getInt(1);
            }
            cntExam++;
            System.out.print("Number of students to add: ");
            int numOfexam = sc.nextInt();
            sc.nextLine();
            for (int i = cntExam; i <= cntExam + numOfexam - 1; i++) {
                System.out.print("Enter studentID : ");
                int studentID = sc.nextInt();
                System.out.print("Enter courseID :");
                      int  courseID = sc.nextInt();
                System.out.print("Enter incourseMarks : ");
                       int incourseMarks = sc.nextInt();
                       if(incourseMarks > 30) {
                           System.out.println("incourse marks have to <=30");
                           incourseMarks = sc.nextInt();
                       }
                System.out.print("Enter finalMarks : ");
                       int finalMarks = sc.nextInt();
                       if(finalMarks > 70){
                           System.out.println("final marks have to <=70");
                           finalMarks = sc.nextInt();
                       }
                String insertSQL = "INSERT INTO exams (examID, studentID, courseID, incourseMarks, finalMarks, totalMarks,isRetake, isImprovement, examDate) VALUES (?, ?,?,?,?,?,?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
                preparedStatement.setInt(1, i);
                preparedStatement.setInt(2, studentID);
                preparedStatement.setInt(3,courseID);
                preparedStatement.setInt(4,incourseMarks);
                preparedStatement.setInt(5,finalMarks);
                int totalMarks = incourseMarks+finalMarks;
                preparedStatement.setInt(6,totalMarks);
                sc.nextLine();
                System.out.print("Enter examDate (yyyy-MM-dd): ");
                String examDateStr = sc.nextLine();
                if(totalMarks<40) preparedStatement.setBoolean(7,true);
                else preparedStatement.setBoolean(7,false);
                if(totalMarks >= 40 && totalMarks <80) preparedStatement.setBoolean(8,true);
                else preparedStatement.setBoolean(8,false);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date examDate = dateFormat.parse(examDateStr);
                preparedStatement.setDate(9, new java.sql.Date(examDate.getTime()));
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Data inserted successfully.");
                } else {
                    System.out.println("Data insertion failed.");
                }
                preparedStatement.close();
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Failed connection");
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            throw new RuntimeException(e);
        }
    }
    void checkRetake(String url, String userName, String password) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter studentID : ");

        try {
            Connection connection = DriverManager.getConnection(url, userName, password);
            System.out.println("Database connected");
            Statement statement = connection.createStatement();
            int ID = sc.nextInt();
            String SQL = "WITH retake (studentID, courseID) AS " +
                    "(SELECT studentID, courseID FROM exams WHERE studentID = " + ID + " AND isRetake = 1), " +
                    "dummy (studentName, studentID, courseID) AS " +
                    "(SELECT studentName, students.studentID, courseID FROM students, retake " +
                    "WHERE students.studentID = retake.studentID) " +
                    "SELECT dummy.studentID, studentName, dummy.courseID, courseName " +
                    "FROM dummy, courses " +
                    "WHERE dummy.courseID = courses.courseID";

            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                int studentID = resultSet.getInt("studentID");
                String studentName = resultSet.getString("studentName");
                int courseID = resultSet.getInt("courseID");
                String courseName = resultSet.getString("courseName");
                System.out.println(studentID + " " + studentName + " " + courseID + " " + courseName);
            }
            resultSet.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Connection failed");
            e.printStackTrace();
        }
    }
    void checkImprovement(String url, String userName, String password){
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter studentID : ");

        try {
            Connection connection = DriverManager.getConnection(url, userName, password);
            System.out.println("Database connected");
            Statement statement = connection.createStatement();
            int ID = sc.nextInt();
            String SQL = "WITH retake (studentID, courseID) AS " +
                    "(SELECT studentID, courseID FROM exams WHERE studentID = " + ID + " AND isImprovement = 1), " +
                    "dummy (studentName, studentID, courseID) AS " +
                    "(SELECT studentName, students.studentID, courseID FROM students, retake " +
                    "WHERE students.studentID = retake.studentID) " +
                    "SELECT dummy.studentID, studentName, dummy.courseID, courseName " +
                    "FROM dummy, courses " +
                    "WHERE dummy.courseID = courses.courseID";

            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                int studentID = resultSet.getInt("studentID");
                String studentName = resultSet.getString("studentName");
                int courseID = resultSet.getInt("courseID");
                String courseName = resultSet.getString("courseName");
                System.out.println(studentID + " " + studentName + " " + courseID + " " + courseName);
            }
            resultSet.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Connection failed");
            e.printStackTrace();
        }
    }
    void giveRetake(String url, String userName, String password){
        Scanner sc = new Scanner(System.in);
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String url = "jdbc:mysql://10.33.4.30/db2020315638";
        String userName = "s2020315638";
        String password = "iam4yearsolddbms";
        System.out.print("press 1 for add students , 2 for add course, 3 for add exams , 4 for check retake, 5 for check improvement : ");


        Main main = new Main();

        int choice = sc.nextInt();
        switch (choice){
            case 1:
                main.addStudents(url, userName, password);
                break;
            case 2:
                main.addCources(url, userName, password);
                break;
            case 3:
                main.addExam(url,userName,password);
                break;
            case 4:
                main.checkRetake(url,userName, password);
                break;
            case 5:
                main.checkImprovement(url, userName, password);
                break;
            default:
                System.out.println("Invalid Choice");
                break;
        }
    }
}
