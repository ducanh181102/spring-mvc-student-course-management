<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng ký khóa học</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/students.css?v=<%= System.currentTimeMillis() %>">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/enrollments.css?v=<%= System.currentTimeMillis() %>">
    <script src="${pageContext.request.contextPath}/js/theme.js?v=<%= System.currentTimeMillis() %>"></script>
</head>
<body>
<div class="app-layout">
    <jsp:include page="/WEB-INF/views/fragments/sidebar.jsp"/>

    <div class="main-column">
        <jsp:include page="/WEB-INF/views/fragments/header.jsp"/>

        <main class="page-content enrollment-page">
            <div class="header-content">
                <div class="title-page">
                    <label class="medium" name="text">Đăng ký khóa học</label>
                    <label class="small-x2" name="caption">Ghi danh sinh viên vào khóa học và theo dõi các lượt đăng
                        ký.</label>
                </div>
            </div>

            <div class="filter-content">
                <label class="search-field">
                    <svg viewBox="0 0 24 24" aria-hidden="true">
                        <circle cx="11" cy="11" r="8"/>
                        <path d="m21 21-4.35-4.35"/>
                    </svg>
                    <input id="enrollment-search-input" type="search"
                           aria-label="Tìm lượt đăng ký theo sinh viên hoặc khóa học"
                           placeholder="Tìm theo sinh viên hoặc khóa học">
                </label>
                <button class="filter-action" id="enrollment-filter-button" type="button"
                        aria-expanded="false" aria-controls="enrollment-filter-panel">
                    <svg viewBox="0 0 24 24" aria-hidden="true">
                        <path d="M4 7h16M7 12h10m-7 5h4"/>
                    </svg>
                    <span>Lọc</span>
                </button>
                <div class="catalog-filter-panel enrollment-filter-panel" id="enrollment-filter-panel" hidden>
                    <h2>Lọc lượt đăng ký</h2>
                    <label for="enrollment-student-filter">Sinh viên</label>
                    <select id="enrollment-student-filter">
                        <option value="all">Tất cả sinh viên</option>
                        <c:forEach var="student" items="${students}">
                            <option value="${student.id}"><c:out value="${student.fullName}"/> · <c:out
                                    value="${student.id}"/></option>
                        </c:forEach>
                    </select>
                    <label for="enrollment-course-filter">Khóa học</label>
                    <select id="enrollment-course-filter">
                        <option value="all">Tất cả khóa học</option>
                        <c:forEach var="course" items="${courses}">
                            <option value="${course.id}"><c:out value="${course.title}"/> · <c:out
                                    value="${course.id}"/></option>
                        </c:forEach>
                    </select>
                    <div class="catalog-filter-actions">
                        <button type="button" id="reset-enrollment-filters">Xóa lọc</button>
                        <button type="button" class="apply" id="apply-enrollment-filters">Áp dụng</button>
                    </div>
                </div>
            </div>

            <section class="main-content enrollment-main-content">
                <div class="enrollment-section-heading">
                    <label class="small-x2" name="caption">DỮ LIỆU MẪU</label>
                    <span id="enrollment-total-count"><c:out value="${empty enrollments ? 0 : enrollments.size()}"/> lượt đăng ký</span>
                </div>

                <div class="enrollment-workspace">
                    <section class="enrollment-form-card" id="enrollment-form" aria-labelledby="enrollment-form-title">
                        <div class="enrollment-card-heading">
                            <span class="enrollment-card-icon" aria-hidden="true">
                                <svg viewBox="0 0 24 24">
                                    <path d="M16 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                                    <circle cx="10" cy="7" r="4"/>
                                    <path d="M20 21v-2a4 4 0 0 0-3-3.87M16 3.13a4 4 0 0 1 0 7.75"/>
                                </svg>
                            </span>
                            <div>
                                <h2 id="enrollment-form-title">Tạo lượt đăng ký</h2>
                                <p>Chọn sinh viên và khóa học cần ghi danh.</p>
                            </div>
                        </div>

                        <form class="enrollment-form" method="post"
                              action="${pageContext.request.contextPath}/enrollments">
                            <label for="student-choice">Sinh viên</label>
                            <div class="enrollment-select">
                                <select required id="student-choice" name="studentId">
                                    <option value="" selected disabled>Chọn sinh viên</option>
                                    <c:forEach var="student" varStatus="loop" items="${students}">
                                        <option value="${student.id}"><c:out value="${student.fullName}"/> · <c:out
                                                value="${student.id}"/></option>
                                    </c:forEach>
                                </select>
                                <svg viewBox="0 0 24 24" aria-hidden="true">
                                    <path d="m7 10 5 5 5-5"/>
                                </svg>
                            </div>

                            <label for="course-choice">Khóa học</label>
                            <div class="enrollment-select">
                                <select required id="course-choice" name="courseId">
                                    <option value="" selected disabled>Chọn khóa học</option>
                                    <c:forEach items="${courses}" varStatus="loop" var="course">
                                        <option value="${course.id}"><c:out value="${course.title}"/> · <c:out
                                                value="${course.id}"/></option>
                                    </c:forEach>
                                </select>
                                <svg viewBox="0 0 24 24" aria-hidden="true">
                                    <path d="m7 10 5 5 5-5"/>
                                </svg>
                            </div>

                            <button class="enrollment-submit" type="submit">
                                <svg viewBox="0 0 24 24" aria-hidden="true">
                                    <path d="M12 5v14M5 12h14"/>
                                </svg>
                                <span>Tạo đăng ký</span>
                            </button>
                        </form>

                        <aside class="enrollment-note" aria-labelledby="enrollment-note-title">
                            <div class="enrollment-note-heading">
                                <svg viewBox="0 0 24 24" aria-hidden="true">
                                    <circle cx="12" cy="12" r="10"/>
                                    <path d="M12 11v5M12 8h.01"/>
                                </svg>
                                <strong id="enrollment-note-title">Quy tắc đăng ký</strong>
                            </div>
                            <p class="enrollment-note-intro">Để đăng ký thành công, hãy kiểm tra:</p>
                            <ul>
                                <li>Sinh viên và khóa học phải tồn tại.</li>
                                <li>Không đăng ký trùng khóa học.</li>
                            </ul>
                            <p class="enrollment-note-footer">Chọn đúng sinh viên và khóa học trước khi tạo lượt đăng
                                ký.</p>
                        </aside>
                    </section>

                    <section class="enrollment-list-card" aria-labelledby="enrollment-list-title">
                        <div class="enrollment-card-heading enrollment-list-heading">
                            <div>
                                <h2 id="enrollment-list-title">Danh sách đăng ký</h2>
                                <p>Các cặp sinh viên và khóa học đã ghi danh.</p>
                            </div>
                            <span class="enrollment-count" id="enrollment-card-count"><c:out
                                    value="${empty enrollments ? 0 : enrollments.size()}"/></span>
                        </div>

                        <div class="enrollment-table-wrap">
                            <c:if test="${empty enrollments}">
                                <label class="small-x3" name="caption">Chưa có đăng ký nào.</label>
                            </c:if>
                            <c:if test="${!empty enrollments}">
                                <table class="enrollment-table">
                                    <thead>
                                    <tr>
                                        <th>Sinh viên</th>
                                        <th>Khóa học</th>
                                        <th><span class="visually-hidden">Thao tác</span></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="enrollment" varStatus="loop" items="${enrollments}">
                                        <tr data-student-id="${enrollment.student.id}"
                                            data-student-name="<c:out value='${enrollment.student.fullName}'/>"
                                            data-course-id="${enrollment.course.id}"
                                            data-course-title="<c:out value='${enrollment.course.title}'/>">
                                            <td>
                                                <div class="enrollment-person">
                                                    <span class="enrollment-avatar"><c:out
                                                            value="${enrollment.student.initials}"/></span>
                                                    <span><strong><c:out
                                                            value="${enrollment.student.fullName}"/></strong><small><c:out
                                                            value="${enrollment.student.id}"/> </small></span>
                                                </div>
                                            </td>
                                            <td><strong><c:out value="${enrollment.course.title}"/> </strong>
                                                <span style="flex-direction: row; display: flex; gap: 2px"> <small>Mã khóa học:</small> <small> <c:out
                                                        value="${enrollment.course.id}"/> </small></span></td>
                                            <td>
                                                <form class="delete-enrollment-form"
                                                      action="${pageContext.request.contextPath}/enrollments/${enrollment.student.id}/${enrollment.course.id}/delete"
                                                      method="post"
                                                      data-student-name="<c:out value='${enrollment.student.fullName}'/>"
                                                      data-course-title="<c:out value='${enrollment.course.title}'/>">
                                                    <button class="enrollment-remove" type="submit"
                                                            aria-label="Hủy đăng ký của ${enrollment.student.fullName}">
                                                        <svg viewBox="0 0 24 24" aria-hidden="true">
                                                            <path d="M18 6 6 18M6 6l12 12"/>
                                                        </svg>
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <tr class="enrollment-filter-empty" id="enrollment-filter-empty" hidden>
                                        <td colspan="3">Không tìm thấy lượt đăng ký phù hợp.</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </c:if>
                        </div>
                    </section>
                </div>
            </section>
        </main>
    </div>
</div>
<script>
    const enrollmentRows = document.querySelectorAll(
        ".enrollment-table tbody tr[data-student-id]"
    );
    const enrollmentSearchInput = document.getElementById("enrollment-search-input");
    const enrollmentFilterButton = document.getElementById("enrollment-filter-button");
    const enrollmentFilterPanel = document.getElementById("enrollment-filter-panel");
    const enrollmentStudentFilter = document.getElementById("enrollment-student-filter");
    const enrollmentCourseFilter = document.getElementById("enrollment-course-filter");
    const enrollmentFilterEmpty = document.getElementById("enrollment-filter-empty");
    const deleteEnrollmentForms = document.querySelectorAll(".delete-enrollment-form");
    const enrollmentCounts = [
        document.getElementById("enrollment-total-count"),
        document.getElementById("enrollment-card-count")
    ];
    const activeEnrollmentFilters = {studentId: "all", courseId: "all"};

    deleteEnrollmentForms.forEach((form) => {
        form.addEventListener("submit", (event) => {
            const studentName = form.dataset.studentName;
            const courseTitle = form.dataset.courseTitle;
            const confirmed = window.confirm(window.translateAppText(
                "Bạn chắc chắn muốn xóa khóa học '" + courseTitle
                + "' của sinh viên '" + studentName + "'?"
            ));

            if (!confirmed) event.preventDefault();
        });
    });

    function normalizeEnrollmentSearch(value) {
        return value.trim().toLocaleLowerCase("vi")
            .normalize("NFD")
            .replace(/[\u0300-\u036f]/g, "")
            .replace(/đ/g, "d");
    }

    function filterEnrollments() {
        const query = normalizeEnrollmentSearch(enrollmentSearchInput.value);
        let visibleCount = 0;

        enrollmentRows.forEach((row) => {
            const searchableText = normalizeEnrollmentSearch(
                "SV-" + row.dataset.studentId + " "
                + row.dataset.studentId + " " + row.dataset.studentName + " "
                + "KH-" + row.dataset.courseId + " "
                + row.dataset.courseId + " " + row.dataset.courseTitle
            );
            const matchesQuery = searchableText.includes(query);
            const matchesStudent = activeEnrollmentFilters.studentId === "all"
                || row.dataset.studentId === activeEnrollmentFilters.studentId;
            const matchesCourse = activeEnrollmentFilters.courseId === "all"
                || row.dataset.courseId === activeEnrollmentFilters.courseId;
            const matches = matchesQuery && matchesStudent && matchesCourse;

            row.hidden = !matches;
            if (matches) visibleCount++;
        });

        if (enrollmentFilterEmpty) {
            enrollmentFilterEmpty.hidden = enrollmentRows.length === 0 || visibleCount > 0;
        }
        enrollmentCounts.forEach((count) => {
            if (!count) return;
            if (count.id !== "enrollment-total-count") {
                count.textContent = String(visibleCount);
                return;
            }
            const countLabel = visibleCount + " lượt đăng ký";
            count.textContent = typeof window.translateAppText === "function"
                ? window.translateAppText(countLabel)
                : countLabel;
        });
    }

    function closeEnrollmentFilterPanel(restoreFocus = false) {
        enrollmentFilterPanel.hidden = true;
        enrollmentFilterButton.setAttribute("aria-expanded", "false");
        if (restoreFocus) enrollmentFilterButton.focus();
    }

    enrollmentFilterButton.addEventListener("click", () => {
        const willOpen = enrollmentFilterPanel.hidden;
        enrollmentFilterPanel.hidden = !willOpen;
        enrollmentFilterButton.setAttribute("aria-expanded", String(willOpen));
        if (willOpen) enrollmentStudentFilter.focus();
    });

    document.getElementById("apply-enrollment-filters").addEventListener("click", () => {
        activeEnrollmentFilters.studentId = enrollmentStudentFilter.value;
        activeEnrollmentFilters.courseId = enrollmentCourseFilter.value;
        filterEnrollments();
        closeEnrollmentFilterPanel();
    });

    document.getElementById("reset-enrollment-filters").addEventListener("click", () => {
        enrollmentSearchInput.value = "";
        enrollmentStudentFilter.value = "all";
        enrollmentCourseFilter.value = "all";
        activeEnrollmentFilters.studentId = "all";
        activeEnrollmentFilters.courseId = "all";
        filterEnrollments();
        closeEnrollmentFilterPanel();
    });

    document.addEventListener("click", (event) => {
        if (!event.target.closest(".enrollment-page .filter-content")) {
            closeEnrollmentFilterPanel();
        }
    });
    document.addEventListener("keydown", (event) => {
        if (event.key === "Escape" && !enrollmentFilterPanel.hidden) {
            closeEnrollmentFilterPanel(true);
        }
    });
    enrollmentSearchInput.addEventListener("input", filterEnrollments);
    enrollmentSearchInput.addEventListener("keydown", (event) => {
        if (event.key === "Enter") filterEnrollments();
    });
    document.addEventListener("app-language-changed", filterEnrollments);
    filterEnrollments();
</script>
</body>
</html>
