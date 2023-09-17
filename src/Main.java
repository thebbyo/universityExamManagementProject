import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


public class _28_DiponkerRoy {

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
    boolean isFailed(String url, String userName, String password, int studentID, int courseID){
        boolean isfailed = false;
        try {
            Connection connection = DriverManager.getConnection(url, userName, password);
            System.out.printf("database connected");
            Statement statement = connection.createStatement();
            String SQL = "select isRetake"+
                          " from exams "+
                          "where studentID = "+studentID+
                          " and courseID = "+ courseID;
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()){
                isfailed = resultSet.getBoolean("isRetake");
            }
            connection.close();
            resultSet.close();
            statement.close();

        }
        catch (SQLException e){
            System.out.printf("Connection failed");
            e.printStackTrace();
        }
        return isfailed;
    }
    void giveRetake(String url, String userName, String password) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter student id: ");
        int studentID = sc.nextInt();
        System.out.print("Enter course id: ");
        int courseID = sc.nextInt();

        if (!isFailed(url, userName, password, studentID, courseID)) {
            System.out.println("Can't give retake");
            return;
        }

        try {
            Connection connection = DriverManager.getConnection(url, userName, password);
            System.out.println("Database connected");
            String qq = "SELECT totalMarks, examDate FROM exams WHERE studentID = ? AND courseID = ?";

            PreparedStatement selectStatement = connection.prepareStatement(qq);
            selectStatement.setInt(1, studentID);
            selectStatement.setInt(2, courseID);

            ResultSet resultSet = selectStatement.executeQuery();
            int prevTotalMarks = 0;
            Date prevDate = null;

            while (resultSet.next()) {
                prevTotalMarks = resultSet.getInt("totalMarks");
                prevDate = resultSet.getDate("examDate");
            }

            resultSet.close();
            System.out.print("Enter incourseMarks: ");
            int incourseMarks = sc.nextInt();

            if (incourseMarks > 30) {
                System.out.println("incourse marks have to be <= 30");
                incourseMarks = sc.nextInt();
            }

            System.out.print("Enter finalMarks: ");
            int finalMarks = sc.nextInt();

            if (finalMarks > 70) {
                System.out.println("final marks have to be <= 70");
                finalMarks = sc.nextInt();
            }

            sc.nextLine();
            System.out.print("Enter examDate (yyyy-MM-dd): ");
            String examDateStr = sc.nextLine();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date examDate = dateFormat.parse(examDateStr);

            if (prevTotalMarks > incourseMarks + finalMarks || (incourseMarks + finalMarks < 40 && examDate.before(prevDate))) {
                System.out.println("You have failed again or check the date again");
                connection.close();
                selectStatement.close();
                return;
            }

            int totalMarks = incourseMarks + finalMarks;
            qq = "UPDATE exams " +
                    "SET incourseMarks = ?, finalMarks = ?, totalMarks = ?, isRetake = ?, isImprovement = ?, examDate = ? " +
                    "WHERE studentID = ? AND courseID = ?";

            PreparedStatement updateStatement = connection.prepareStatement(qq);
            updateStatement.setInt(1, incourseMarks);
            updateStatement.setInt(2, finalMarks);
            updateStatement.setInt(3, totalMarks);
            updateStatement.setBoolean(4, false); // Set isRetake to false
            updateStatement.setBoolean(5, totalMarks >= 40 && totalMarks < 80); // Set isImprovement
            updateStatement.setDate(6, new java.sql.Date(examDate.getTime()));
            updateStatement.setInt(7, studentID);
            updateStatement.setInt(8, courseID);

            int rowsAffected = updateStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Data updated successfully.");
            } else {
                System.out.println("Data update failed.");
            }

            connection.close();
            selectStatement.close();
            updateStatement.close();

        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            System.out.println("Connection failed");
            e.printStackTrace();
        }
    }
    boolean isImprovement(String url, String userName, String password, int studentID, int courseID){
        boolean isfailed = false;
        try {
            Connection connection = DriverManager.getConnection(url, userName, password);
            System.out.printf("database connected");
            Statement statement = connection.createStatement();
            String SQL = "select isImprovement"+
                    " from exams "+
                    "where studentID = "+studentID+
                    " and courseID = "+ courseID;
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()){
                isfailed = resultSet.getBoolean("isImprovement");
            }
            connection.close();
            resultSet.close();
            statement.close();

        }
        catch (SQLException e){
            System.out.printf("Connection failed");
            e.printStackTrace();
        }
        return isfailed;
    }
    void giveImprovement(String url, String userName, String password) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter student id: ");
        int studentID = sc.nextInt();
        System.out.print("Enter course id: ");
        int courseID = sc.nextInt();

        if (!isImprovement(url, userName, password, studentID, courseID)) {
            System.out.println("Can't give improvement");
            return;
        }

        try {
            Connection connection = DriverManager.getConnection(url, userName, password);
            System.out.println("Database connected");
            String qq = "SELECT incourseMarks,totalMarks, examDate FROM exams WHERE studentID = ? AND courseID = ?";

            PreparedStatement selectStatement = connection.prepareStatement(qq);
            selectStatement.setInt(1, studentID);
            selectStatement.setInt(2, courseID);

            ResultSet resultSet = selectStatement.executeQuery();
            int prevTotalMarks = 0;
            Date prevDate = null;
            int prevIncourseMarks = 0;
            while (resultSet.next()) {
                prevIncourseMarks = resultSet.getInt("incourseMarks");
                prevTotalMarks = resultSet.getInt("totalMarks");
                prevDate = resultSet.getDate("examDate");
            }

            resultSet.close();


            System.out.print("Enter finalMarks: ");
            int finalMarks = sc.nextInt();

            if (finalMarks > 70) {
                System.out.println("final marks have to be <= 70");
                finalMarks = sc.nextInt();
            }

            sc.nextLine();
            System.out.print("Enter examDate (yyyy-MM-dd): ");
            String examDateStr = sc.nextLine();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date examDate = dateFormat.parse(examDateStr);

            if (prevTotalMarks > finalMarks  && examDate.before(prevDate)) {
                System.out.println("You have not improved or check the date");
                connection.close();
                selectStatement.close();
                return;
            }

            qq = "UPDATE exams " +
                    "SET finalMarks = ?, totalMarks = ?,  isImprovement = ?, examDate = ? " +
                    "WHERE studentID = ? AND courseID = ?";

            PreparedStatement updateStatement = connection.prepareStatement(qq);
            updateStatement.setInt(1, finalMarks);
            updateStatement.setInt(2, finalMarks+prevIncourseMarks);
            updateStatement.setBoolean(3, finalMarks +prevIncourseMarks< 80); // Set isImprovement
            updateStatement.setDate(4, new java.sql.Date(examDate.getTime()));
            updateStatement.setInt(5, studentID);
            updateStatement.setInt(6, courseID);

            int rowsAffected = updateStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Data updated successfully.");
            } else {
                System.out.println("Data update failed.");
            }

            connection.close();
            selectStatement.close();
            updateStatement.close();

        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            System.out.println("Connection failed");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String url = "jdbc:mysql://10.33.4.30/db2020315638";
        String userName = "s2020315638";
        String password = "iam4yearsolddbms";
        System.out.println("press 1 for add student");
        System.out.println("press 2 for add course");
        System.out.println("press 3 for add exam");
        System.out.println("press 4 for check retake");
        System.out.println("press 5 for check improvement");
        System.out.println("press 6 for give retake");
        System.out.println("press 7 for give improvement");
        _28_DiponkerRoy main = new _28_DiponkerRoy();

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
            case 6:
                main.giveRetake(url,userName, password);
                break;
            case 7:
                main.giveImprovement(url, userName, password);
                break;
            default:
                System.out.println("Invalid Choice");
                break;
        }
    }
}
