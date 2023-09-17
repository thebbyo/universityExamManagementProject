create table students(
  studentID int primary key,
  studentName varchar(255) not null
);
create table courses(
  courseID int primary key,
  courseName varchar(255) not null,
  courseCredit double not null
);
create table exams(
  examID int primary key,
  studentID int,
  courseID int,
  incourseMarks double,
  finalMarks double,
  totalMarks double,
  isRetake boolean default false,
  isImprovement boolean default false,
  examDate date not null,
  foreign key(studentID) references students(studentID),
  foreign key(courseID) references courses(courseID)
);
