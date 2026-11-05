package org.example.school.controller;

import org.example.school.model.Course;
import org.example.school.model.Enrollment;
import org.example.school.service.CourseService;
import org.example.school.service.EnrollmentService;
import org.example.school.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.example.school.model.Student;

import java.util.*;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    public StudentController(StudentService studentService, CourseService courseService, EnrollmentService enrollmentService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
    }

    private void initPage(Model model) {
        List<Student> students = studentService.getAllStudents();
        Set<Long> enrolledStudentIds = enrollmentService.getEnrollmentAll().stream()
                .map(Enrollment::getStudentId)
                .collect(java.util.stream.Collectors.toSet());
        Map<Long, Boolean> enrollmentStatusByStudentId = new HashMap<>();
        students.forEach(student -> enrollmentStatusByStudentId.put(
                student.getId(), enrolledStudentIds.contains(student.getId())));

        model.addAttribute("students", students);
        model.addAttribute("enrollmentStatusByStudentId", enrollmentStatusByStudentId);
        model.addAttribute("message", "Danh sách sinh viên");
        model.addAttribute("breadcrumb", "Sinh viên");
        model.addAttribute("activeNav", "students");
        model.addAttribute("nextId", studentService.getNextId());
    }

    @GetMapping
    public String listStudents(Model model) {
        initPage(model);
        return "students/list";
    }

    @GetMapping("/{id}")
    public String student(@PathVariable("id") long id, Model model) {
        /*Student student = studentService.getAllStudents().stream().filter(student1 -> student1.getId() == id).findFirst().orElse(null);*/
        initPage(model);
        Student student = studentService.getStudent(id);
        List<Course> courses = enrollmentService.getCoursesByStudentId(id);
        model.addAttribute("selectedStudent", student);
        model.addAttribute("selectedStudentOfCourses", courses);
        return "students/list";
    }

    @PostMapping
    public String addStudent(
            @RequestParam("id") long id,
            @RequestParam("fullName") String fullName
    ) {
        Student student = new Student(id, fullName);
        studentService.addStudent(student);
        return "redirect:/students";
    }

    @PostMapping("/{id}/update")
    public String updateStudent(
            @PathVariable("id") long id,
            @RequestParam("fullName") String fullName
    ) {
        studentService.updateStudent(id, fullName);
        return "redirect:/students/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteStudent(@PathVariable("id") long id) {
        studentService.deleteStudent(id);
        return "redirect:/students";
    }
}
