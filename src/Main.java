import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class Course{
    private String course;
    private double inCourseMarks, finalExamMarks, totalMarks;
    private Date examDate;
    private boolean needsRetake, canGiveImprovement;
    Course(String course, double inCourseMarks, double finalExamMarks, Date examDate){
        this.course = course;
        this.totalMarks = 0;
        this.needsRetake = false;
        this.canGiveImprovement = false;
        if(inCourseMarks<=30 && finalExamMarks <= 70){
            this.inCourseMarks = inCourseMarks;
            this.finalExamMarks = finalExamMarks;
            this.examDate = examDate;
        }
        else{
            System.out.println("Invalid Input");
            this.inCourseMarks = 0;
            this.finalExamMarks = 0;
            this.examDate = null;
        }
    }
    void setFinalExamMarks(double finalExamMarks){
        this.finalExamMarks = finalExamMarks;
    }
    void setInCourseMarks(double inCourseMarks){
        this.inCourseMarks = inCourseMarks;
    }
    String getCourse(){
        return course;
    }
    double getInCourseMarks(){
        return inCourseMarks;
    }
    double getFinalExamMarks(){
        return finalExamMarks;
    }
    Date getExamDate(){
        return examDate;
    }
    void setNeedsRetake(boolean needsRetake){
        this.needsRetake = needsRetake;
    }
    void setTotalMarks(double totalMarks){
        this.totalMarks = totalMarks;
    }
    void setCanGiveImprovement(boolean canGiveImprovement){
        this.canGiveImprovement = canGiveImprovement;
    }
    void setExamDate(Date examDate){
        this.examDate = examDate;
    }

}
class Student{
    private String studentID, studentName;
    private List<Course> courses;
    Student(String studentID, String studentName){
        this.studentID = studentID;
        this.studentName = studentName;
        this.courses = new ArrayList<>();
    }
    String getStudentID(){
        return studentID;
    }
    String getStudentName(){
        return studentName;
    }
    void addCourse(Course course){
        courses.add(course);
    }
    double totalMarks(String courseName){
        for(Course course:courses){
            if(course.getCourse().equals(courseName)){
                course.setTotalMarks(course.getFinalExamMarks()+course.getInCourseMarks());
                return course.getInCourseMarks()+course.getFinalExamMarks();
            }
        }
        return 0;
    }
    boolean isFailed(String courseName){
        for(Course course:courses){
            if(course.getCourse().equals(courseName) && totalMarks(courseName) <40){
                course.setNeedsRetake(true);
                return true;
            }
            else course.setNeedsRetake(false);
        }
        return false;
    }
    boolean canGiveimprovement(String courseName){
        for(Course course:courses){
            if(course.getCourse().equals(courseName) && totalMarks(courseName) >=40 && totalMarks(courseName)<80){
                course.setCanGiveImprovement(true);
                return true;
            }
        }
        return false;
    }
    void giveImprovement(String courseName, double finalMarks, Date examDate) {
        if (!canGiveimprovement(courseName)) {
            System.out.println(studentName + " can't give improvement exam in " + courseName);
            return;
        }
        if (finalMarks > 70) {
            System.out.println("final marks should be <= 70");
            return;
        }
            for (Course course : courses) {
                if (course.getCourse().equals(courseName) && totalMarks(courseName) < finalMarks) {
                    if (course.getExamDate() == null || examDate.after(course.getExamDate())) {
                        course.setFinalExamMarks(finalMarks);
                        totalMarks(courseName);
                        canGiveimprovement(courseName);
                        course.setExamDate(examDate);
                    } else {
                        System.out.println("Improvement exam date should be after the previous exam date.");
                    }
                }
            }

    }
    void giveRetake(String courseName, double incourseMarks, double finalMarks, Date examDate){
        if(!isFailed(courseName)){
            System.out.println("You passed the exam with " + totalMarks(courseName));
            if(canGiveimprovement(courseName)) System.out.println("You can give improvement");
            else System.out.println("You can't also able to give improvement");
            return;
        }
        if(incourseMarks > 30 || finalMarks  >= 70){
            System.out.println("incourse and finalmarks should be <=30 and <=70");
            return;
        }
        for(Course course:courses){
            if(course.getCourse().equals(courseName)){
                if(course.getExamDate() == null || examDate.after(course.getExamDate()))
                    course.setFinalExamMarks(finalMarks);
                    course.setInCourseMarks(incourseMarks);
                    totalMarks(courseName);
                    if(isFailed(courseName)){
                        System.out.println("you failed again");
                        return;
                    }
                System.out.println("you have passed");
                    canGiveimprovement(courseName);
                    course.setExamDate(examDate);

            }
            else System.out.println("Improvement exam date should be after the previous exam date.");
        }

    }
}

public class Main {
    public static void main(String[] args) throws ParseException {
        Student student = new Student("2020315638", "Dibbyo Roy");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date examDate = dateFormat.parse("23-05-2023");
        student.addCourse(new Course("Math", 10, 2, examDate));
        System.out.println(student.canGiveimprovement("Math"));
        System.out.println(student.isFailed("Math"));
        examDate = dateFormat.parse("24-05-2023");
        student.giveRetake("Math", 21,20, examDate);
        System.out.println(student.totalMarks("Math"));
        System.out.println(student.isFailed("Math"));
        System.out.println(student.canGiveimprovement("Math"));
//        System.out.println(student.totalMarks("Math"));
//        System.out.println(student.canGiveimprovement("Math"));

    }
}