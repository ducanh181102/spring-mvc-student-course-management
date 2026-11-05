package org.example.school.controller;

import org.example.school.dto.EnrollmentView;
import org.example.school.model.Course;
import org.example.school.model.Enrollment;
import org.example.school.model.Student;
import org.example.school.service.CourseService;
import org.example.school.service.EnrollmentService;
import org.example.school.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final StudentService studentService;
    private final CourseService courseService;

    public EnrollmentController(EnrollmentService enrollmentService, StudentService studentService, CourseService courseService) {
        this.enrollmentService = enrollmentService;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    private void initPage(Model model) {
        List<EnrollmentView> enrollments = enrollmentService.getEnrollmentViewList();
        List<Course> courses = courseService.getCourseList();
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("enrollments", enrollments);
        model.addAttribute("courses", courses);
        model.addAttribute("students", students);
        model.addAttribute("breadcrumb", "Đăng ký khóa học");
        model.addAttribute("activeNav", "enrollments");
    }

    @GetMapping
    public String listEnrollments(Model model) {
        initPage(model);
        return "enrollments/list";
    }

    @PostMapping
    public String addEnrollment(@RequestParam("studentId") long studentId,
                                @RequestParam("courseId") long courseId) {
        enrollmentService.addEnrollment(new Enrollment(studentId, courseId));
        return "redirect:/enrollments";
    }

    @PostMapping("/{studentId}/{courseId}/delete")
    public String deleteCourse(@PathVariable("studentId") long studentId, @PathVariable("courseId") long courseId) {
        enrollmentService.cancelEnrollment(studentId, courseId);
        return "redirect:/enrollments";
    }

}
