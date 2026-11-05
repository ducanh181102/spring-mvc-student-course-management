<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Danh sách sinh viên</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/students.css?v=<%= System.currentTimeMillis() %>">
    <script src="${pageContext.request.contextPath}/js/theme.js?v=<%= System.currentTimeMillis() %>"></script>
</head>
<body>
<div class="app-layout">
    <jsp:include page="/WEB-INF/views/fragments/sidebar.jsp"/>

    <div class="main-column">
        <jsp:include page="/WEB-INF/views/fragments/header.jsp"/>

        <main class="page-content">
            <div class="header-content">
                <div class="title-page">
                    <label class="medium" name="text">${message}</label>
                    <label class="small-x2" name="caption">Chọn hồ sơ để xem thông tin và khóa học đã đăng ký.</label>
                </div>
                <button class="addBtn" id="open-student-modal" type="button"
                        aria-haspopup="dialog" aria-controls="add-student-modal">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none"
                         stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                         data-lucide="plus" aria-hidden="true" class="lucide lucide-plus">
                        <path d="M5 12h14"></path>
                        <path d="M12 5v14"></path>
                    </svg>
                    <label name="button" class="small-x3">Thêm sinh viên</label>
                </button>
            </div>
            <div class="filter-content">
                <label class="search-field">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none"
                         stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                         data-lucide="search" aria-hidden="true" class="lucide lucide-search">
                        <path d="m21 21-4.34-4.34"></path>
                        <circle cx="11" cy="11" r="8"></circle>
                    </svg>
                    <input id="student-search-input" type="search"
                           aria-label="Tìm sinh viên theo mã hoặc tên"
                           placeholder="Tìm theo mã hoặc tên">
                </label>
                <button class="filter-action" id="filter-students-button" type="button"
                        aria-expanded="false" aria-controls="student-filter-panel">
                    <svg viewBox="0 0 24 24" aria-hidden="true">
                        <path d="M4 7h16M7 12h10m-7 5h4"/>
                    </svg>
                    <span class="small-x3">Lọc</span>
                </button>
                <div class="student-filter-panel" id="student-filter-panel" hidden>
                    <h2>Lọc sinh viên</h2>
                    <div class="student-filter-range">
                        <label for="student-id-from">Mã từ</label>
                        <input id="student-id-from" type="number" min="0" inputmode="numeric"
                               placeholder="Không giới hạn">
                        <label for="student-id-to">Đến</label>
                        <input id="student-id-to" type="number" min="0" inputmode="numeric"
                               placeholder="Không giới hạn">
                    </div>
                    <label for="student-enrollment-filter">Tình trạng đăng ký</label>
                    <select id="student-enrollment-filter">
                        <option value="all">Tất cả</option>
                        <option value="enrolled">Đã đăng ký khóa học</option>
                        <option value="not-enrolled">Chưa đăng ký khóa học</option>
                    </select>
                    <p class="student-filter-error" id="student-filter-error" role="alert" hidden>
                        Mã bắt đầu phải nhỏ hơn hoặc bằng mã kết thúc.
                    </p>
                    <div class="student-filter-actions">
                        <button type="button" id="reset-student-filters">Xóa lọc</button>
                        <button type="button" class="apply" id="apply-student-filters">Áp dụng</button>
                    </div>
                </div>
            </div>
            <div class="main-content">
                <label class="small-x2" name="caption">DỮ LIỆU MẪU</label>
                <div class="split-layout">
                    <div class="student-list" aria-label="Danh sách sinh viên">
                        <c:if test="${empty students}">
                            <label class="small-x3" name="caption">Chưa có sinh viên nào.</label>
                        </c:if>
                        <c:forEach var="student" items="${students}" varStatus="loop">
                            <a class="student-row ${student.id == selectedStudent.id ? "is-selected" : ""}"
                               data-student-id="${student.id}"
                               data-student-name="<c:out value='${student.fullName}'/>"
                               data-enrolled="${enrollmentStatusByStudentId[student.id]}"
                               type="button"
                               href="${pageContext.request.contextPath}/students/${student.id}"
                               aria-current="${loop.first}"
                               aria-pressed="${loop.first}">
                                <span class="avatar"><c:out value="${student.initials}"/></span>
                                <span class="student-summary">
                                    <span class="student-name"><c:out value="${student.fullName}"/></span>
                                    <div style="display: flex; flex-direction: row"><small>Mã sinh viên</small><small><c:out
                                            value=": ${student.id}"/></small></div>
                                </span>
                                <c:if test="${student.id == selectedStudent.id}">
                                    <c:if test="${!empty selectedStudentOfCourses}">
                                        <span class="status">⚫︎ Đang học</span>
                                    </c:if>
                                    <c:if test="${empty selectedStudentOfCourses}">
                                        <span class="status-non-enrollment">⚫︎ Chưa đăng ký</span>
                                    </c:if>
                                </c:if>
                            </a>
                            <c:if test="${not loop.last}">
                                <div class="divider"></div>
                            </c:if>
                        </c:forEach>
                        <p class="student-search-empty" id="student-search-empty" role="status" hidden>
                            Không tìm thấy sinh viên phù hợp.
                        </p>
                    </div>
                    <section class="student-detail" aria-label="Hồ sơ ${selectedStudent.fullName}">
                        <c:if test="${empty selectedStudent}">
                            <label class="small-x3" name="caption">Chọn sinh viên để xem chi tiết.</label>
                        </c:if>
                        <c:if test="${!empty selectedStudent}">
                        <div class="profile-heading">
                            <span class="avatar"><c:out value="${selectedStudent.initials}"/> </span>
                            <div class="student-summary">
                                <div class="student-name"><c:out value="${selectedStudent.fullName}"/></div>
                                <div class="student-info">
                                    <small><c:out value="${selectedStudent.id}"/> · </small>
                                    <c:if test="${!empty selectedStudentOfCourses}"> <span
                                            class="status">⚫︎ Đang học</span> </c:if>
                                    <c:if test="${empty selectedStudentOfCourses}"><span
                                            class="status-non-enrollment">⚫︎ Chưa đăng ký</span>
                                    </c:if>
                                </div>
                            </div>
                            <div class="profile-actions" id="student-profile-actions">
                                <button class="action-button" id="student-action-trigger" type="button"
                                        aria-label="Thao tác hồ sơ"
                                        aria-expanded="false" aria-controls="student-action-menu">
                                    <svg viewBox="0 0 24 24" aria-hidden="true">
                                        <circle cx="5" cy="12" r="1"/>
                                        <circle cx="12" cy="12" r="1"/>
                                        <circle cx="19" cy="12" r="1"/>
                                    </svg>
                                </button>
                                <div class="student-action-menu" id="student-action-menu" hidden role="group"
                                     aria-label="Thao tác hồ sơ sinh viên">
                                    <button type="button" data-edit-student>Chỉnh sửa thông tin</button>
                                    <form id="delete-student-form"
                                          action="${pageContext.request.contextPath}/students/${selectedStudent.id}/delete"
                                          method="post"
                                          data-student-name="<c:out value='${selectedStudent.fullName}'/>">
                                        <button class="danger" type="submit">Xóa sinh viên</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                        <div class="divider"></div>
                        <section class="detail-section">
                            <label class="small-x2" name="text">Thông tin sinh viên</label>
                            <div class="detail-grid">
                                <label class="small-x3" name="text"><small>Email</small>minhanh@example.com</label>
                                <label class="small-x3" name="text"><small>Ngày nhập học</small>12/09/2024</label>
                                <label class="small-x3" name="text"><small>Chương trình</small>Công nghệ phần
                                    mềm</label>
                                <label class="small-x3" name="text"><small>Niên khóa</small>2024–2028</label>
                            </div>
                        </section>
                        <div class="divider"></div>
                        <section class="detail-section">
                            <label class="small-x2" name="text">Khóa học đã đăng ký</label>
                            <c:if test="${empty selectedStudentOfCourses}">
                                <label class="small-x3" name="caption">Sinh viên chưa đăng ký học phần.</label>
                            </c:if>
                            <div class="course-list">
                                <c:forEach var="course" items="${selectedStudentOfCourses}" varStatus="loop">
                                    <div class="course-row">
                                        <label class="small-x3" name="text"><c:out value="${course.title}"/>
                                            <span style="flex-direction: row"><small>Mã khóa học</small><small>: <c:out
                                                    value="${course.id}"/></small></span>
                                        </label><span
                                            class="status active">⚫︎ Đang học</span>
                                    </div>
                                    <c:if test="${not loop.last}">
                                        <div class="divider"></div>
                                    </c:if>
                                </c:forEach>
                            </div>
                        </section>
                    </section>
                    </c:if>
                </div>
            </div>
        </main>

        <dialog class="entity-modal" id="add-student-modal" aria-labelledby="add-student-title">
            <div class="entity-modal-content">
                <div class="entity-modal-heading">
                    <div>
                        <span class="entity-modal-eyebrow">SINH VIÊN</span>
                        <h2 id="add-student-title">Thêm sinh viên</h2>
                        <p>Nhập thông tin cơ bản cho sinh viên mới.</p>
                    </div>
                    <button class="entity-modal-close" type="button" aria-label="Đóng"
                            data-close-student-modal>
                        <svg viewBox="0 0 24 24" aria-hidden="true">
                            <path d="m18 6-12 12M6 6l12 12"/>
                        </svg>
                    </button>
                </div>

                <form class="entity-modal-form" id="student-create-form" method="post"
                      action="${pageContext.request.contextPath}/students">
                    <label for="new-student-id">Mã sinh viên</label>
                    <input id="new-student-id" name="id" type="number" min="1" inputmode="numeric" readonly
                           value="${nextId}"
                           placeholder="Ví dụ: 24056" required>

                    <label for="new-student-name">Họ và tên</label>
                    <input id="new-student-name" name="fullName" type="text" placeholder="Nhập họ và tên"
                           autocomplete="name" required>

                    <div class="entity-modal-actions">
                        <button class="entity-modal-button secondary" type="button" data-close-student-modal>
                            Hủy
                        </button>
                        <button class="entity-modal-button primary" type="submit">Thêm sinh viên</button>
                    </div>
                    <p class="entity-modal-feedback" id="student-modal-feedback" role="status" hidden>
                        Biểu mẫu đã hợp lệ; thao tác lưu dữ liệu chưa được kết nối.
                    </p>
                </form>
            </div>
        </dialog>

        <c:if test="${!empty selectedStudent}">
            <dialog class="entity-modal" id="edit-student-modal" aria-labelledby="edit-student-title">
                <div class="entity-modal-content">
                    <div class="entity-modal-heading">
                        <div>
                            <span class="entity-modal-eyebrow">SINH VIÊN</span>
                            <h2 id="edit-student-title">Chỉnh sửa hồ sơ</h2>
                            <p>Cập nhật họ tên sinh viên.</p>
                        </div>
                        <button class="entity-modal-close" type="button" aria-label="Đóng"
                                data-close-edit-student>
                            <svg viewBox="0 0 24 24" aria-hidden="true">
                                <path d="m18 6-12 12M6 6l12 12"/>
                            </svg>
                        </button>
                    </div>

                    <form class="entity-modal-form" method="post"
                          action="${pageContext.request.contextPath}/students/${selectedStudent.id}/update">
                        <label for="edit-student-id">Mã sinh viên</label>
                        <input id="edit-student-id" type="number" value="${selectedStudent.id}" readonly>

                        <label for="edit-student-name">Họ và tên</label>
                        <input id="edit-student-name" name="fullName" type="text"
                               value="<c:out value='${selectedStudent.fullName}'/>"
                               autocomplete="name" required>

                        <div class="entity-modal-actions">
                            <button class="entity-modal-button secondary" type="button" data-close-edit-student>
                                Hủy
                            </button>
                            <button class="entity-modal-button primary" type="submit">Lưu thay đổi</button>
                        </div>
                    </form>
                </div>
            </dialog>
        </c:if>
    </div>
</div>
<script>
    const addStudentModal = document.getElementById("add-student-modal");
    const openStudentModal = document.getElementById("open-student-modal");
    const studentCreateForm = document.getElementById("student-create-form");

    openStudentModal.addEventListener("click", () => {
        studentCreateForm.reset();
        document.getElementById("student-modal-feedback").hidden = true;
        addStudentModal.showModal();
        document.getElementById("new-student-name").focus();
    });

    addStudentModal.querySelectorAll("[data-close-student-modal]").forEach((button) => {
        button.addEventListener("click", () => addStudentModal.close());
    });

    addStudentModal.addEventListener("click", (event) => {
        if (event.target === addStudentModal) addStudentModal.close();
    });

    const studentRows = document.querySelectorAll(".student-row");
    const studentSearchInput = document.getElementById("student-search-input");
    const filterStudentsButton = document.getElementById("filter-students-button");
    const studentFilterPanel = document.getElementById("student-filter-panel");
    const studentIdFromInput = document.getElementById("student-id-from");
    const studentIdToInput = document.getElementById("student-id-to");
    const enrollmentFilterSelect = document.getElementById("student-enrollment-filter");
    const studentFilterError = document.getElementById("student-filter-error");
    const applyStudentFiltersButton = document.getElementById("apply-student-filters");
    const resetStudentFiltersButton = document.getElementById("reset-student-filters");
    const studentSearchEmpty = document.getElementById("student-search-empty");
    const activeStudentFilters = {minId: null, maxId: null, enrollment: "all"};

    function normalizeStudentSearch(value) {
        return value.trim().toLocaleLowerCase("vi")
            .normalize("NFD")
            .replace(/[\u0300-\u036f]/g, "")
            .replace(/đ/g, "d");
    }

    function filterStudents() {
        const query = normalizeStudentSearch(studentSearchInput.value);
        const visibleRows = [];

        studentRows.forEach((row) => {
            const searchableText = normalizeStudentSearch(
                "SV-" + row.dataset.studentId + " "
                + row.dataset.studentId + " "
                + row.dataset.studentName
            );
            const studentId = Number(row.dataset.studentId);
            const hasEnrollment = row.dataset.enrolled === "true";
            const matchesQuery = searchableText.includes(query);
            const matchesMinId = activeStudentFilters.minId === null
                || studentId >= activeStudentFilters.minId;
            const matchesMaxId = activeStudentFilters.maxId === null
                || studentId <= activeStudentFilters.maxId;
            const matchesEnrollment = activeStudentFilters.enrollment === "all"
                || (activeStudentFilters.enrollment === "enrolled" && hasEnrollment)
                || (activeStudentFilters.enrollment === "not-enrolled" && !hasEnrollment);
            const matches = matchesQuery && matchesMinId && matchesMaxId && matchesEnrollment;
            row.hidden = !matches;

            const divider = row.nextElementSibling;
            if (divider?.classList.contains("divider")) divider.hidden = true;
            if (matches) visibleRows.push(row);
        });

        visibleRows.slice(0, -1).forEach((row) => {
            const divider = row.nextElementSibling;
            if (divider?.classList.contains("divider")) divider.hidden = false;
        });

        studentSearchEmpty.hidden = query.length === 0
            || visibleRows.length > 0
            || studentRows.length === 0;
    }

    function closeStudentFilterPanel(restoreFocus = false) {
        studentFilterPanel.hidden = true;
        filterStudentsButton.setAttribute("aria-expanded", "false");
        if (restoreFocus) filterStudentsButton.focus();
    }

    filterStudentsButton.addEventListener("click", () => {
        const willOpen = studentFilterPanel.hidden;
        studentFilterPanel.hidden = !willOpen;
        filterStudentsButton.setAttribute("aria-expanded", String(willOpen));
        if (willOpen) studentIdFromInput.focus();
    });

    function applyStudentFilters() {
        const minId = studentIdFromInput.value === "" ? null : Number(studentIdFromInput.value);
        const maxId = studentIdToInput.value === "" ? null : Number(studentIdToInput.value);

        if (minId !== null && maxId !== null && minId > maxId) {
            studentFilterError.hidden = false;
            studentIdFromInput.focus();
            return;
        }

        studentFilterError.hidden = true;
        activeStudentFilters.minId = minId;
        activeStudentFilters.maxId = maxId;
        activeStudentFilters.enrollment = enrollmentFilterSelect.value;
        filterStudents();
        closeStudentFilterPanel();
    }

    applyStudentFiltersButton.addEventListener("click", applyStudentFilters);
    [studentIdFromInput, studentIdToInput].forEach((input) => {
        input.addEventListener("keydown", (event) => {
            if (event.key === "Enter") applyStudentFilters();
        });
    });

    resetStudentFiltersButton.addEventListener("click", () => {
        studentIdFromInput.value = "";
        studentIdToInput.value = "";
        enrollmentFilterSelect.value = "all";
        studentSearchInput.value = "";
        activeStudentFilters.minId = null;
        activeStudentFilters.maxId = null;
        activeStudentFilters.enrollment = "all";
        studentFilterError.hidden = true;
        filterStudents();
        closeStudentFilterPanel();
    });

    document.addEventListener("click", (event) => {
        if (!event.target.closest(".filter-content")) closeStudentFilterPanel();
    });
    document.addEventListener("keydown", (event) => {
        if (event.key === "Escape" && !studentFilterPanel.hidden) {
            closeStudentFilterPanel(true);
        }
    });

    studentSearchInput.addEventListener("input", filterStudents);
    studentSearchInput.addEventListener("keydown", (event) => {
        if (event.key === "Enter") filterStudents();
    });
    filterStudents();

    studentRows.forEach((studentRow) => {
        studentRow.addEventListener("click", () => {
            studentRows.forEach((row) => {
                row.classList.remove("is-selected");
                row.removeAttribute("aria-current");
                row.setAttribute("aria-pressed", "false");

                const status = row.querySelector(".status");
                if (status) {
                    status.hidden = true;
                }
            });

            studentRow.classList.add("is-selected");
            studentRow.setAttribute("aria-current", "true");
            studentRow.setAttribute("aria-pressed", "true");

            /*const selectedStatus = studentRow.querySelector(".status");
            if (selectedStatus) {
                selectedStatus.hidden = false;
            }*/
        });
    });

    const studentProfileActions = document.getElementById("student-profile-actions");
    const studentActionTrigger = document.getElementById("student-action-trigger");
    const studentActionMenu = document.getElementById("student-action-menu");
    const editStudentModal = document.getElementById("edit-student-modal");
    const editStudentButton = document.querySelector("[data-edit-student]");
    const editStudentName = document.getElementById("edit-student-name");
    const deleteStudentForm = document.getElementById("delete-student-form");

    function closeStudentActionMenu(restoreFocus = false) {
        if (!studentActionMenu || !studentActionTrigger) return;
        studentActionMenu.hidden = true;
        studentActionTrigger.setAttribute("aria-expanded", "false");
        if (restoreFocus) studentActionTrigger.focus();
    }

    if (studentProfileActions && studentActionTrigger && studentActionMenu) {
        studentActionTrigger.addEventListener("click", () => {
            const willOpen = studentActionMenu.hidden;
            studentActionMenu.hidden = !willOpen;
            studentActionTrigger.setAttribute("aria-expanded", String(willOpen));
            if (willOpen) studentActionMenu.querySelector("button")?.focus();
        });

        document.addEventListener("click", (event) => {
            if (!studentProfileActions.contains(event.target)) {
                closeStudentActionMenu();
            }
        });

        document.addEventListener("keydown", (event) => {
            if (event.key === "Escape" && !studentActionMenu.hidden) {
                closeStudentActionMenu(true);
            }
        });
    }

    if (editStudentButton && editStudentModal) {
        editStudentButton.addEventListener("click", () => {
            closeStudentActionMenu();
            editStudentModal.showModal();
            editStudentName?.focus();
        });

        editStudentModal.querySelectorAll("[data-close-edit-student]").forEach((button) => {
            button.addEventListener("click", () => editStudentModal.close());
        });

        editStudentModal.addEventListener("click", (event) => {
            if (event.target === editStudentModal) editStudentModal.close();
        });

        editStudentModal.addEventListener("close", () => studentActionTrigger?.focus());
    }

    if (deleteStudentForm) {
        deleteStudentForm.addEventListener("submit", (event) => {
            const studentName = deleteStudentForm.dataset.studentName;
            const confirmed = window.confirm(window.translateAppText(
                "Xóa sinh viên \"" + studentName +
                "\" và các đăng ký khóa học của sinh viên này?"
            ));
            if (!confirmed) event.preventDefault();
        });
    }
</script>
</body>
</html>
