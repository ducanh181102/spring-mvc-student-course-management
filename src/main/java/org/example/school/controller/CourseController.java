package org.example.school.controller;

import org.example.school.model.Course;
import org.example.school.model.Enrollment;
import org.example.school.service.CourseService;
import org.example.school.service.EnrollmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/courses")
public class CourseController {
    public CourseController(CourseService courseService, EnrollmentService enrollmentService) {
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
    }

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    private void initPage(Model model) {
        List<Course> courses = courseService.getCourseList();
        Map<Long, Long> enrollmentCounts = enrollmentService.getEnrollmentAll().stream()
                .collect(Collectors.groupingBy(Enrollment::getCourseId, Collectors.counting()));
        model.addAttribute("courses", courses);
        model.addAttribute("enrollmentCounts", enrollmentCounts);
        model.addAttribute("activeNav", "courses");
        model.addAttribute("breadcrumb", "Khóa học");
        model.addAttribute("nextId", courseService.getNextId());
    }

    @GetMapping
    public String listCourses(Model model) {
        initPage(model);
        return "courses/list";
    }

    @GetMapping("/{id}")
    public String course(Model model, @PathVariable("id") long id) {
        initPage(model);
        Course course = courseService.getCourse(id);
        int enrollmentCountCurrent = enrollmentService.countByCourseId(id);

        model.addAttribute("selectedCourse", course);
        model.addAttribute("enrollmentCountCurrent", enrollmentCountCurrent);

        return "courses/list";
    }

    @PostMapping
    public String addCourse(@RequestParam("id") long id,
                            @RequestParam("title") String title,
                            @RequestParam("capacity") int capacity) {
        courseService.addCourse(new Course(id, title, capacity));
        return "redirect:/courses";
    }

    @PostMapping("/{id}/update")
    public String updateCourse(@PathVariable("id") long id,
                              @RequestParam("title") String title,
                              @RequestParam("capacity") int capacity) {
        courseService.updateCapacityCourse(id, capacity);
        courseService.updateTitleCourse(id, title);
        return "redirect:/courses/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteCourse(@PathVariable("id") long id) {
        courseService.deleteCourse(id);
        return "redirect:/courses";
    }

}
