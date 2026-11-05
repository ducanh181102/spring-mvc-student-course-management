package org.example.school;

import org.example.school.config.WebConfig;
import org.example.school.dto.EnrollmentView;
import org.example.school.model.Course;
import org.example.school.model.Student;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * MVC-to-database flow tests. Uses an isolated in-memory H2 database so the
 * developer's local school database and generated sample data are untouched.
 */
class ApplicationFlowTest {
    private static AnnotationConfigWebApplicationContext context;
    private static MockMvc mockMvc;
    private static JdbcTemplate jdbc;

    @BeforeAll
    static void startApplicationContext() {
        context = new AnnotationConfigWebApplicationContext();
        context.setServletContext(new MockServletContext("src/main/webapp"));
        context.register(TestInfrastructure.class, WebConfig.class);
        context.refresh();
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        jdbc = context.getBean(JdbcTemplate.class);
    }

    @AfterAll
    static void closeApplicationContext() {
        if (context != null) context.close();
    }

    @BeforeEach
    void resetDatabase() {
        jdbc.update("DELETE FROM enrollment");
        jdbc.update("DELETE FROM course");
        jdbc.update("DELETE FROM student");

        Object[][] students = IntStream.rangeClosed(1, 50)
                .mapToObj(id -> new Object[]{id, "Student " + id})
                .toArray(Object[][]::new);
        jdbc.batchUpdate("INSERT INTO student (id, full_name) VALUES (?, ?)", List.of(students));

        Object[][] courses = IntStream.range(0, 20)
                .mapToObj(index -> new Object[]{100 + index, "Course " + (100 + index), 50})
                .toArray(Object[][]::new);
        jdbc.batchUpdate("INSERT INTO course (id, title, capacity) VALUES (?, ?, ?)", List.of(courses));
        jdbc.update("INSERT INTO course (id, title, capacity) VALUES (200, 'One-seat course', 1)");
    }

    @ParameterizedTest(name = "Create student id={0}")
    @MethodSource("studentCreationCases")
    void createsStudentThroughPostRoute(long id, String fullName) throws Exception {
        MvcResult result = mockMvc.perform(post("/students")
                        .param("id", Long.toString(id))
                        .param("fullName", fullName))
                .andReturn();

        assertEquals(302, result.getResponse().getStatus());
        assertEquals("/students", result.getResponse().getRedirectedUrl());
        assertEquals(fullName, jdbc.queryForObject(
                "SELECT full_name FROM student WHERE id = ?", String.class, id));
    }

    static Stream<Arguments> studentCreationCases() {
        String[] names = {
                "Nguyễn Minh Anh", "Trần Hải Nam", "Lê Thu Hà", "Phạm Quốc Bảo",
                "Đặng Gia Linh", "Võ Nhật Minh", "Huỳnh Ngọc Mai", "Bùi Thanh Tùng",
                "Phan Đức Anh", "Vũ Khánh An", "Đỗ Hoàng Long", "Dương Thảo Vy"
        };
        return IntStream.range(0, names.length)
                .mapToObj(index -> Arguments.of(1001L + index, names[index]));
    }

    @ParameterizedTest(name = "Update student id={0}")
    @MethodSource("studentUpdateCases")
    void updatesStudentThroughPostRoute(long id, String fullName) throws Exception {
        MvcResult result = mockMvc.perform(post("/students/{id}/update", id)
                        .param("fullName", fullName))
                .andReturn();

        assertEquals(302, result.getResponse().getStatus());
        assertEquals("/students/" + id, result.getResponse().getRedirectedUrl());
        assertEquals(fullName, jdbc.queryForObject(
                "SELECT full_name FROM student WHERE id = ?", String.class, id));
    }

    static Stream<Arguments> studentUpdateCases() {
        String[] names = {
                "Nguyễn Minh Anh", "Trần Hải Nam", "Lê Thu Hà", "Phạm Quốc Bảo"
        };
        return IntStream.range(0, names.length)
                .mapToObj(index -> Arguments.of((long) index + 1, names[index]));
    }

    @ParameterizedTest(name = "Reject invalid student operation: {0}")
    @MethodSource("invalidStudentCases")
    void rejectsInvalidStudentOperations(String scenario, String operation, long id) throws Exception {
        MvcResult result;
        if (operation.equals("create")) {
            result = mockMvc.perform(post("/students")
                            .param("id", Long.toString(id))
                            .param("fullName", "Duplicate student"))
                    .andReturn();
        } else if (operation.equals("update")) {
            result = mockMvc.perform(post("/students/{id}/update", id)
                            .param("fullName", "Missing student"))
                    .andReturn();
        } else {
            result = mockMvc.perform(post("/students/{id}/delete", id)).andReturn();
        }

        assertEquals(400, result.getResponse().getStatus(), scenario);
        assertEquals("error/error", result.getModelAndView().getViewName());
        assertNotNull(result.getModelAndView().getModel().get("errorMessage"));
    }

    static Stream<Arguments> invalidStudentCases() {
        return Stream.of(
                Arguments.of("duplicate student ID", "create", 1L),
                Arguments.of("duplicate student ID", "create", 2L),
                Arguments.of("update missing student", "update", 9001L),
                Arguments.of("delete missing student", "delete", 9002L)
        );
    }

    @ParameterizedTest(name = "Delete student id={0}, linked enrollments={1}")
    @MethodSource("studentDeletionCases")
    void deletesStudentAndItsEnrollments(long studentId, int linkedEnrollments) throws Exception {
        for (int index = 0; index < linkedEnrollments; index++) {
            jdbc.update("INSERT INTO enrollment (student_id, course_id) VALUES (?, ?)",
                    studentId, 100 + index);
        }

        MvcResult result = mockMvc.perform(post("/students/{id}/delete", studentId)).andReturn();

        assertEquals(302, result.getResponse().getStatus());
        assertEquals("/students", result.getResponse().getRedirectedUrl());
        assertEquals(0, count("SELECT COUNT(*) FROM student WHERE id = ?", studentId));
        assertEquals(0, count("SELECT COUNT(*) FROM enrollment WHERE student_id = ?", studentId));
    }

    static Stream<Arguments> studentDeletionCases() {
        return Stream.of(
                Arguments.of(1L, 0), Arguments.of(2L, 1),
                Arguments.of(3L, 3), Arguments.of(4L, 10)
        );
    }

    @ParameterizedTest(name = "Load student profile id={0}, enrollments={1}")
    @MethodSource("studentDetailCases")
    void loadsStudentProfileAndRelatedCourses(long studentId, int enrollmentCount) throws Exception {
        for (int index = 0; index < enrollmentCount; index++) {
            jdbc.update("INSERT INTO enrollment (student_id, course_id) VALUES (?, ?)",
                    studentId, 100 + index);
        }

        MvcResult result = mockMvc.perform(get("/students/{id}", studentId)).andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("students/list", result.getModelAndView().getViewName());
        assertEquals(studentId, ((Student) result.getModelAndView().getModel()
                .get("selectedStudent")).getId());
        assertEquals(enrollmentCount, ((List<?>) result.getModelAndView().getModel()
                .get("selectedStudentOfCourses")).size());
    }

    static Stream<Arguments> studentDetailCases() {
        return Stream.of(
                Arguments.of(1L, 0), Arguments.of(2L, 1),
                Arguments.of(3L, 2), Arguments.of(4L, 3)
        );
    }

    @ParameterizedTest(name = "Create course id={0}")
    @MethodSource("courseCreationCases")
    void createsCourseThroughPostRoute(long id, String title, int capacity) throws Exception {
        MvcResult result = mockMvc.perform(post("/courses")
                        .param("id", Long.toString(id))
                        .param("title", title)
                        .param("capacity", Integer.toString(capacity)))
                .andReturn();

        assertEquals(302, result.getResponse().getStatus());
        assertEquals("/courses", result.getResponse().getRedirectedUrl());
        assertEquals(title, jdbc.queryForObject(
                "SELECT title FROM course WHERE id = ?", String.class, id));
        assertEquals(capacity, jdbc.queryForObject(
                "SELECT capacity FROM course WHERE id = ?", Integer.class, id));
    }

    static Stream<Arguments> courseCreationCases() {
        String[] titles = {
                "Java cơ bản", "Cấu trúc dữ liệu", "Cơ sở dữ liệu", "Spring MVC",
                "Phát triển Web", "Kiểm thử phần mềm", "Mạng máy tính", "Lập trình Python",
                "Thiết kế phần mềm", "An toàn thông tin", "Phân tích dữ liệu", "Đồ án Java"
        };
        return IntStream.range(0, titles.length)
                .mapToObj(index -> Arguments.of(301L + index, titles[index], index + 1));
    }

    @ParameterizedTest(name = "Update course title/capacity case {index}")
    @MethodSource("courseUpdateCases")
    void updatesCourseTitleAndCapacity(String title, int capacity) throws Exception {
        MvcResult result = mockMvc.perform(post("/courses/100/update")
                        .param("title", title)
                        .param("capacity", Integer.toString(capacity)))
                .andReturn();

        assertEquals(302, result.getResponse().getStatus());
        assertEquals("/courses/100", result.getResponse().getRedirectedUrl());
        assertEquals(title, jdbc.queryForObject(
                "SELECT title FROM course WHERE id = 100", String.class));
        assertEquals(capacity, jdbc.queryForObject(
                "SELECT capacity FROM course WHERE id = 100", Integer.class));
    }

    static Stream<Arguments> courseUpdateCases() {
        return IntStream.range(1, 8)
                .mapToObj(index -> Arguments.of("Updated course " + index, 20 + index));
    }

    @ParameterizedTest(name = "Delete course id=100, linked enrollments={0}")
    @MethodSource("courseDeletionCases")
    void deletesCourseAndItsEnrollments(int linkedEnrollments) throws Exception {
        for (int index = 0; index < linkedEnrollments; index++) {
            jdbc.update("INSERT INTO enrollment (student_id, course_id) VALUES (?, 100)", index + 1);
        }

        MvcResult result = mockMvc.perform(post("/courses/100/delete")).andReturn();

        assertEquals(302, result.getResponse().getStatus());
        assertEquals("/courses", result.getResponse().getRedirectedUrl());
        assertEquals(0, count("SELECT COUNT(*) FROM course WHERE id = 100"));
        assertEquals(0, count("SELECT COUNT(*) FROM enrollment WHERE course_id = 100"));
    }

    static Stream<Arguments> courseDeletionCases() {
        return Stream.of(Arguments.of(0), Arguments.of(1), Arguments.of(3));
    }

    @ParameterizedTest(name = "Reject invalid course operation: {0}")
    @MethodSource("invalidCourseCases")
    void rejectsInvalidCourseOperations(String scenario, long id, int capacity,
                                        int linkedEnrollments) throws Exception {
        MvcResult result;
        if (scenario.equals("duplicate ID")) {
            result = mockMvc.perform(post("/courses")
                            .param("id", Long.toString(id))
                            .param("title", "Duplicate")
                            .param("capacity", Integer.toString(capacity)))
                    .andReturn();
        } else if (scenario.equals("negative capacity")) {
            result = mockMvc.perform(post("/courses")
                            .param("id", Long.toString(id))
                            .param("title", "Invalid capacity")
                            .param("capacity", Integer.toString(capacity)))
                    .andReturn();
        } else if (scenario.equals("missing course update")) {
            result = mockMvc.perform(post("/courses/{id}/update", id)
                            .param("title", "Missing")
                            .param("capacity", Integer.toString(capacity)))
                    .andReturn();
        } else {
            for (int index = 0; index < linkedEnrollments; index++) {
                jdbc.update("INSERT INTO enrollment (student_id, course_id) VALUES (?, 100)", index + 1);
            }
            result = mockMvc.perform(post("/courses/100/update")
                            .param("title", "Too small")
                            .param("capacity", Integer.toString(capacity)))
                    .andReturn();
        }

        assertEquals(400, result.getResponse().getStatus());
        assertEquals("error/error", result.getModelAndView().getViewName());
        assertNotNull(result.getModelAndView().getModel().get("errorMessage"));
    }

    static Stream<Arguments> invalidCourseCases() {
        return Stream.of(
                Arguments.of("duplicate ID", 100L, 10, 0),
                Arguments.of("duplicate ID", 101L, 10, 0),
                Arguments.of("negative capacity", 301L, -1, 0),
                Arguments.of("negative capacity", 302L, -10, 0),
                Arguments.of("missing course update", 9001L, 10, 0),
                Arguments.of("missing course update", 9002L, 20, 0),
                Arguments.of("capacity below enrollment count", 100L, 0, 1),
                Arguments.of("capacity below enrollment count", 100L, 1, 2)
        );
    }

    @ParameterizedTest(name = "Create enrollment student={0}, course={1}")
    @MethodSource("enrollmentCreationCases")
    void createsEnrollmentThroughPostRoute(long studentId, long courseId) throws Exception {
        MvcResult result = mockMvc.perform(post("/enrollments")
                        .param("studentId", Long.toString(studentId))
                        .param("courseId", Long.toString(courseId)))
                .andReturn();

        assertEquals(302, result.getResponse().getStatus());
        assertEquals("/enrollments", result.getResponse().getRedirectedUrl());
        assertEquals(1, count("SELECT COUNT(*) FROM enrollment WHERE student_id = ? AND course_id = ?",
                studentId, courseId));
    }

    static Stream<Arguments> enrollmentCreationCases() {
        return IntStream.range(0, 12)
                .mapToObj(index -> Arguments.of((long) index + 1, (long) index + 100));
    }

    @ParameterizedTest(name = "Reject enrollment: {0}")
    @MethodSource("invalidEnrollmentCases")
    void rejectsInvalidEnrollmentAndKeepsDatabaseConsistent(String scenario, long studentId,
                                                            long courseId, boolean seedPair,
                                                            boolean fillCourse) throws Exception {
        if (seedPair) {
            jdbc.update("INSERT INTO enrollment (student_id, course_id) VALUES (?, ?)", studentId, courseId);
        }
        if (fillCourse) {
            jdbc.update("INSERT INTO enrollment (student_id, course_id) VALUES (1, 200)");
        }

        MvcResult result = mockMvc.perform(post("/enrollments")
                        .param("studentId", Long.toString(studentId))
                        .param("courseId", Long.toString(courseId)))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus(), scenario);
        assertEquals("error/error", result.getModelAndView().getViewName());
        assertNotNull(result.getModelAndView().getModel().get("errorMessage"));
        if (seedPair || fillCourse) {
            assertEquals(1, count("SELECT COUNT(*) FROM enrollment WHERE course_id = ?", courseId));
        } else {
            assertEquals(0, count("SELECT COUNT(*) FROM enrollment WHERE student_id = ?", studentId));
        }
    }

    static Stream<Arguments> invalidEnrollmentCases() {
        return Stream.of(
                Arguments.of("unknown student", 9001L, 100L, false, false),
                Arguments.of("unknown student", 9002L, 101L, false, false),
                Arguments.of("unknown course", 1L, 9001L, false, false),
                Arguments.of("unknown course", 2L, 9002L, false, false),
                Arguments.of("duplicate pair", 1L, 100L, true, false),
                Arguments.of("duplicate pair", 2L, 101L, true, false),
                Arguments.of("full course", 2L, 200L, false, true),
                Arguments.of("full course", 3L, 200L, false, true)
        );
    }

    @ParameterizedTest(name = "Cancel enrollment student={0}")
    @MethodSource("cancellationCases")
    void cancelsExistingEnrollment(long studentId, long courseId) throws Exception {
        jdbc.update("INSERT INTO enrollment (student_id, course_id) VALUES (?, ?)", studentId, courseId);

        MvcResult result = mockMvc.perform(post("/enrollments/{studentId}/{courseId}/delete",
                        studentId, courseId))
                .andReturn();

        assertEquals(302, result.getResponse().getStatus());
        assertEquals("/enrollments", result.getResponse().getRedirectedUrl());
        assertEquals(0, count("SELECT COUNT(*) FROM enrollment WHERE student_id = ? AND course_id = ?",
                studentId, courseId));
    }

    static Stream<Arguments> cancellationCases() {
        return IntStream.range(1, 5)
                .mapToObj(studentId -> Arguments.of((long) studentId, 100L + studentId));
    }

    @ParameterizedTest(name = "Render GET route {0}")
    @MethodSource("pageRouteCases")
    void rendersMainPageRoutes(String route, String expectedView, String expectedModelAttribute)
            throws Exception {
        MvcResult result = mockMvc.perform(get(route)).andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(expectedView, result.getModelAndView().getViewName());
        assertTrue(result.getModelAndView().getModel().containsKey(expectedModelAttribute));
    }

    static Stream<Arguments> pageRouteCases() {
        return Stream.of(
                Arguments.of("/students", "students/list", "students"),
                Arguments.of("/courses", "courses/list", "courses"),
                Arguments.of("/enrollments", "enrollments/list", "enrollments"),
                Arguments.of("/settings", "settings/index", "activeNav")
        );
    }

    @Test
    void courseDetailShowsCurrentEnrollmentCount() throws Exception {
        jdbc.update("INSERT INTO enrollment (student_id, course_id) VALUES (1, 100), (2, 100)");

        MvcResult result = mockMvc.perform(get("/courses/100")).andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(2, result.getModelAndView().getModel().get("enrollmentCountCurrent"));
    }

    @Test
    void enrollmentListUsesJoinedStudentAndCourseDetails() throws Exception {
        jdbc.update("INSERT INTO enrollment (student_id, course_id) VALUES (1, 100)");

        MvcResult result = mockMvc.perform(get("/enrollments")).andReturn();

        List<?> rows = (List<?>) result.getModelAndView().getModel().get("enrollments");
        assertEquals(1, rows.size());
        EnrollmentView view = (EnrollmentView) rows.getFirst();
        assertEquals("Student 1", view.getStudent().getFullName());
        assertEquals("Course 100", view.getCourse().getTitle());
    }

    @Test
    void studentProfileContainsActualCourseObjects() throws Exception {
        jdbc.update("INSERT INTO enrollment (student_id, course_id) VALUES (1, 100), (1, 101)");

        MvcResult result = mockMvc.perform(get("/students/1")).andReturn();

        List<?> courses = (List<?>) result.getModelAndView().getModel().get("selectedStudentOfCourses");
        assertEquals(2, courses.size());
        assertEquals("Course 100", ((Course) courses.get(0)).getTitle());
        assertEquals("Course 101", ((Course) courses.get(1)).getTitle());
    }

    @Test
    void studentAndCourseListsExposeNextIds() throws Exception {
        MvcResult students = mockMvc.perform(get("/students")).andReturn();
        MvcResult courses = mockMvc.perform(get("/courses")).andReturn();

        assertEquals(51L, students.getModelAndView().getModel().get("nextId"));
        assertEquals(201L, courses.getModelAndView().getModel().get("nextId"));
    }

    @Test
    void settingsPageContainsBothWorkingThemeOptions() throws Exception {
        String jsp = Files.readString(Path.of("src/main/webapp/WEB-INF/views/settings/index.jsp"));
        assertTrue(jsp.contains("id=\"theme-select\""));
        assertTrue(jsp.contains("value=\"dark\""));
        assertTrue(jsp.contains("value=\"light\""));
    }

    @Test
    void themeScriptPersistsChoiceAndUpdatesColorScheme() throws Exception {
        String script = Files.readString(Path.of("src/main/webapp/js/theme.js"));
        assertTrue(script.contains("localStorage.setItem(STORAGE_KEY, theme)"));
        assertTrue(script.contains("root.style.colorScheme = activeTheme"));
        assertTrue(script.contains("student-course-theme"));
    }

    @Test
    void allPagesLoadThemeScriptBeforeRendering() throws Exception {
        List<String> pages = List.of(
                "students/list.jsp", "courses/list.jsp", "enrollments/list.jsp",
                "settings/index.jsp", "error/error.jsp"
        );
        for (String page : pages) {
            String source = Files.readString(Path.of("src/main/webapp/WEB-INF/views", page));
            assertTrue(source.contains("/js/theme.js"), page);
        }
    }

    @Test
    void settingsLanguageSelectorHasPersistentLanguageOptions() throws Exception {
        String jsp = Files.readString(Path.of("src/main/webapp/WEB-INF/views/settings/index.jsp"));
        String script = Files.readString(Path.of("src/main/webapp/js/language.js"));
        assertTrue(jsp.contains("id=\"language-select\""));
        assertTrue(jsp.contains("value=\"vi\""));
        assertTrue(jsp.contains("value=\"en\""));
        assertTrue(script.contains("localStorage.setItem(STORAGE_KEY, language)"));
    }

    @Test
    void courseEditMenuInitializationDoesNotDependOnDeferredTranslator() throws Exception {
        String jsp = Files.readString(Path.of("src/main/webapp/WEB-INF/views/courses/list.jsp"));
        assertTrue(jsp.contains("id=\"course-action-trigger\""));
        assertTrue(jsp.contains("data-edit-course"));
        assertTrue(jsp.contains("typeof window.translateAppText === \"function\""));
        assertTrue(jsp.contains("courseActionTrigger.addEventListener(\"click\""));
    }

    private static int count(String sql, Object... args) {
        Integer value = jdbc.queryForObject(sql, Integer.class, args);
        return value == null ? 0 : value;
    }

    @Configuration
    @ComponentScan({"org.example.school.repository", "org.example.school.service"})
    @EnableTransactionManagement
    static class TestInfrastructure {
        @Bean
        DataSource dataSource() {
            DriverManagerDataSource source = new DriverManagerDataSource();
            source.setDriverClassName("org.h2.Driver");
            source.setUrl("jdbc:h2:mem:application_flow_" + UUID.randomUUID().toString().replace("-", "")
                    + ";DB_CLOSE_DELAY=-1");
            source.setUsername("sa");
            source.setPassword("");
            return source;
        }

        @Bean
        JdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }

        @Bean
        PlatformTransactionManager transactionManager(DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }

        @Bean
        DataSourceInitializer databaseInitializer(DataSource dataSource) {
            DataSourceInitializer initializer = new DataSourceInitializer();
            initializer.setDataSource(dataSource);
            initializer.setDatabasePopulator(
                    new ResourceDatabasePopulator(new org.springframework.core.io.ClassPathResource("schema.sql"))
            );
            return initializer;
        }
    }
}
