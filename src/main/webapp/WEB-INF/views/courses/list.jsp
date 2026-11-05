<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Danh sách khóa học</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/students.css?v=<%= System.currentTimeMillis() %>">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/courses.css?v=<%= System.currentTimeMillis() %>">
    <script src="${pageContext.request.contextPath}/js/theme.js?v=<%= System.currentTimeMillis() %>"></script>
</head>
<body>
<div class="app-layout">
    <jsp:include page="/WEB-INF/views/fragments/sidebar.jsp"/>

    <div class="main-column">
        <jsp:include page="/WEB-INF/views/fragments/header.jsp"/>

        <main class="page-content course-page">
            <div class="header-content">
                <div class="title-page">
                    <label class="medium" name="text">Danh sách khóa học</label>
                    <label class="small-x2" name="caption">Quản lý thông tin và sức chứa của từng khóa học.</label>
                </div>
                <button class="addBtn" id="open-course-modal" type="button"
                        aria-haspopup="dialog" aria-controls="add-course-modal">
                    <svg viewBox="0 0 24 24" aria-hidden="true">
                        <path d="M12 5v14M5 12h14"/>
                    </svg>
                    <span>Thêm khóa học</span>
                </button>
            </div>

            <div class="filter-content">
                <label class="search-field">
                    <svg viewBox="0 0 24 24" aria-hidden="true">
                        <circle cx="11" cy="11" r="8"/>
                        <path d="m21 21-4.35-4.35"/>
                    </svg>
                    <input id="course-search-input" type="search" aria-label="Tìm khóa học theo mã hoặc tên"
                           placeholder="Tìm theo mã hoặc tên khóa học">
                </label>
                <button class="filter-action" id="course-filter-button" type="button"
                        aria-expanded="false" aria-controls="course-filter-panel">
                    <svg viewBox="0 0 24 24" aria-hidden="true">
                        <path d="M4 7h16M7 12h10m-7 5h4"/>
                    </svg>
                    <span>Lọc</span>
                </button>
                <div class="catalog-filter-panel" id="course-filter-panel" hidden>
                    <h2>Lọc khóa học</h2>
                    <div class="catalog-filter-range">
                        <label for="course-capacity-from">Sức chứa từ</label>
                        <input id="course-capacity-from" type="number" min="0" inputmode="numeric"
                               placeholder="Không giới hạn">
                        <label for="course-capacity-to">Đến</label>
                        <input id="course-capacity-to" type="number" min="0" inputmode="numeric"
                               placeholder="Không giới hạn">
                    </div>
                    <label for="course-availability-filter">Tình trạng chỗ</label>
                    <select id="course-availability-filter">
                        <option value="all">Tất cả</option>
                        <option value="available">Còn chỗ</option>
                        <option value="full">Đã đủ chỗ</option>
                    </select>
                    <p class="catalog-filter-error" id="course-filter-error" role="alert" hidden>
                        Sức chứa bắt đầu phải nhỏ hơn hoặc bằng sức chứa kết thúc.
                    </p>
                    <div class="catalog-filter-actions">
                        <button type="button" id="reset-course-filters">Xóa lọc</button>
                        <button type="button" class="apply" id="apply-course-filters">Áp dụng</button>
                    </div>
                </div>
            </div>

            <section class="main-content course-main-content">
                <div class="course-section-heading">
                    <label class="small-x2" name="caption">DỮ LIỆU MẪU</label>
                    <span class="course-count"
                          id="course-total-count">${empty courses ? 0 : courses.size()} khóa học</span>
                </div>

                <div class="course-workspace">
                    <section class="course-list-panel" aria-label="Danh sách khóa học">
                        <div class="course-panel-heading">
                            <div>
                                <h2>Khóa học</h2>
                                <p>Chọn một khóa học để xem chi tiết</p>
                            </div>
                            <span class="course-count"
                                  id="course-panel-count">${empty courses ? 0 : courses.size()}</span>
                        </div>
                        <div class="course-panel-main">
                            <c:if test="${empty courses}">
                                <label class="small-x3" name="caption">Chưa có khoá học nào.</label>
                            </c:if>
                            <c:forEach varStatus="loop" items="${courses}" var="course">
                                <a href="${pageContext.request.contextPath}/courses/${course.id}" methods="get"
                                   class="course-list-item ${selectedCourse.id == course.id ? "is-selected" : ""}"
                                   data-course-id="${course.id}"
                                   data-course-title="<c:out value='${course.title}'/>"
                                   data-course-capacity="${course.capacity}"
                                   data-course-enrollment-count="${enrollmentCounts[course.id]}"
                                   type="button" aria-current="${loop.first}">
                                    <span class="course-list-icon" aria-hidden="true"><c:out
                                            value="${course.initials}"/></span>
                                    <span class="course-list-copy">
                                        <span class="course-list-title"><c:out value="${course.title}"/></span>
                                        <span><span class="course-list-meta">Mã khóa học</span><span
                                                class="course-list-meta">: <c:out
                                                value="${course.id}"/></span></span>
                                    </span>
                                    <span style="flex-direction: row"> <span
                                            class="course-list-capacity">Sức chứa:</span><span
                                            class="course-list-capacity"> <c:out
                                            value="${course.capacity}"/></span></span>
                                </a>
                                <c:if test="${not loop.last}">
                                    <div class="divider"></div>
                                </c:if>
                            </c:forEach>
                            <p class="catalog-filter-empty" id="course-filter-empty" role="status" hidden>
                                Không tìm thấy khóa học phù hợp.
                            </p>
                        </div>
                    </section>

                    <section class="course-detail-panel" aria-label="Chi tiết khóa học Lập trình Java">
                        <c:if test="${empty courses}">
                            <label class="small-x3" name="caption">Chưa có khoá học nào.</label>
                        </c:if>
                        <c:if test="${!empty courses}">
                            <c:if test="${empty selectedCourse}">
                                <label class="small-x3" name="caption">Chọn khoá học để xem chi tiết.</label>
                            </c:if>
                            <c:if test="${!empty selectedCourse}">
                                <div class="course-detail-heading">
                                    <div class="course-detail-title-group">
                                        <span class="course-detail-icon" aria-hidden="true"><c:out
                                                value="${selectedCourse.initials}"/></span>
                                        <div>
                                            <span class="course-detail-eyebrow">KHÓA HỌC</span><span
                                                class="course-detail-eyebrow"> <c:out
                                                value="${selectedCourse.id}"/> </span>
                                            <h1><c:out value="${selectedCourse.title}"/></h1>
                                        </div>
                                    </div>
                                    <div class="course-actions" id="course-actions">
                                        <button class="course-more-button" id="course-action-trigger" type="button"
                                                aria-label="Thao tác khóa học"
                                                aria-expanded="false" aria-controls="course-action-menu">
                                            <svg viewBox="0 0 24 24" aria-hidden="true">
                                                <circle cx="5" cy="12" r="1"/>
                                                <circle cx="12" cy="12" r="1"/>
                                                <circle cx="19" cy="12" r="1"/>
                                            </svg>
                                        </button>
                                        <div class="course-action-menu" id="course-action-menu" hidden role="group"
                                             aria-label="Thao tác khóa học">
                                            <button type="button" data-edit-course>Sửa khóa học</button>
                                            <form id="delete-course-form"
                                                  action="${pageContext.request.contextPath}/courses/${selectedCourse.id}/delete"
                                                  method="post"
                                                  data-course-title="<c:out value='${selectedCourse.title}'/>"
                                                  data-enrollment-count="${enrollmentCountCurrent}">
                                                <button class="danger" type="submit">Xóa khóa học</button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                                <div class="divider"></div>
                                <section class="course-detail-section">
                                    <h2>Thông tin khóa học</h2>
                                    <p class="course-detail-description">Thông tin cơ bản và sức chứa tối đa của khóa
                                        học.</p>
                                    <div class="course-facts">
                                        <div class="course-fact">
                                            <span>Mã khóa học</span>
                                            <strong><c:out value="${selectedCourse.id}"/> </strong>
                                        </div>
                                        <div class="course-fact">
                                            <span>Sức chứa tối đa</span>
                                            <strong><c:out value="${selectedCourse.capacity}"/> sinh viên</strong>
                                        </div>
                                    </div>
                                </section>
                                <div class="divider"></div>
                                <section class="course-capacity-card">
                                <span class="course-capacity-symbol" aria-hidden="true">
                                    <svg viewBox="0 0 24 24">
                                        <path d="M16 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                                        <circle cx="10" cy="7" r="4"/>
                                        <path d="M20 21v-2a4 4 0 0 0-3-3.87M16 3.13a4 4 0 0 1 0 7.75"/>
                                    </svg>
                                </span>
                                    <div>
                                        <span>Số lượng sinh viên đăng ký</span>
                                        <strong><c:out value="${enrollmentCountCurrent}"/> <small>sinh
                                            viên</small></strong>
                                    </div>
                                </section>
                            </c:if>
                        </c:if>
                    </section>
                </div>
            </section>
        </main>

        <dialog class="entity-modal" id="add-course-modal" aria-labelledby="add-course-title">
            <div class="entity-modal-content">
                <div class="entity-modal-heading">
                    <div>
                        <span class="entity-modal-eyebrow">KHÓA HỌC</span>
                        <h2 id="add-course-title">Thêm khóa học</h2>
                        <p>Nhập mã, tên và sức chứa của khóa học.</p>
                    </div>
                    <button class="entity-modal-close" type="button" aria-label="Đóng"
                            data-close-course-modal>
                        <svg viewBox="0 0 24 24" aria-hidden="true">
                            <path d="m18 6-12 12M6 6l12 12"/>
                        </svg>
                    </button>
                </div>

                <form class="entity-modal-form"
                      id="course-create-form" method="post"
                      action="${pageContext.request.contextPath}/courses">
                    <label for="new-course-id">Mã khóa học</label>
                    <input id="new-course-id" value="${nextId}" readonly name="id" type="number" min="1"
                           placeholder="Ví dụ: 302" required>

                    <label for="new-course-title">Tên khóa học</label>
                    <input id="new-course-title" name="title" type="text" placeholder="Nhập tên khóa học" required>

                    <label for="new-course-capacity">Sức chứa tối đa</label>
                    <input id="new-course-capacity" name="capacity" type="number" min="1"
                           placeholder="Ví dụ: 30 sinh viên" required>

                    <div class="entity-modal-actions">
                        <button class="entity-modal-button secondary" type="button" data-close-course-modal>
                            Hủy
                        </button>
                        <button class="entity-modal-button primary" type="submit">Thêm khóa học</button>
                    </div>
                    <p class="entity-modal-feedback" id="course-modal-feedback" role="status" hidden>
                        Biểu mẫu đã hợp lệ; thao tác lưu dữ liệu chưa được kết nối.
                    </p>
                </form>
            </div>
        </dialog>

        <c:if test="${!empty selectedCourse}">
            <dialog class="entity-modal" id="edit-course-modal" aria-labelledby="edit-course-title">
                <div class="entity-modal-content">
                    <div class="entity-modal-heading">
                        <div>
                            <span class="entity-modal-eyebrow">KHÓA HỌC</span>
                            <h2 id="edit-course-title">Chỉnh sửa khóa học</h2>
                            <p>Cập nhật tên khóa học và sức chứa.</p>
                        </div>
                        <button class="entity-modal-close" type="button" aria-label="Đóng"
                                data-close-edit-course>
                            <svg viewBox="0 0 24 24" aria-hidden="true">
                                <path d="m18 6-12 12M6 6l12 12"/>
                            </svg>
                        </button>
                    </div>

                    <form class="entity-modal-form" method="post"
                          action="${pageContext.request.contextPath}/courses/${selectedCourse.id}/update">
                        <label for="edit-course-id">Mã khóa học</label>
                        <input id="edit-course-id" type="number" value="${selectedCourse.id}" readonly>

                        <label for="edit-course-title-input">Tên khóa học</label>
                        <input id="edit-course-title-input" name="title" type="text"
                               value="<c:out value='${selectedCourse.title}'/>" required>

                        <label for="edit-course-capacity">Sức chứa tối đa</label>
                        <input id="edit-course-capacity" name="capacity" type="number"
                               min="${enrollmentCountCurrent}" value="${selectedCourse.capacity}" required>
                        <p class="course-edit-hint">
                            Sức chứa không thể thấp hơn số sinh viên đã đăng ký hiện tại
                            (<c:out value="${enrollmentCountCurrent}"/>).
                        </p>

                        <div class="entity-modal-actions">
                            <button class="entity-modal-button secondary" type="button" data-close-edit-course>
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
    const addCourseModal = document.getElementById("add-course-modal");
    const openCourseModal = document.getElementById("open-course-modal");
    const courseCreateForm = document.getElementById("course-create-form");

    openCourseModal.addEventListener("click", () => {
        courseCreateForm.reset();
        document.getElementById("course-modal-feedback").hidden = true;
        addCourseModal.showModal();
        document.getElementById("new-course-title").focus();
    });

    addCourseModal.querySelectorAll("[data-close-course-modal]").forEach((button) => {
        button.addEventListener("click", () => addCourseModal.close());
    });

    addCourseModal.addEventListener("click", (event) => {
        if (event.target === addCourseModal) addCourseModal.close();
    });

    const courseRows = document.querySelectorAll('.course-list-item');

    courseRows.forEach((courseRow) => {
        courseRow.addEventListener('click', (e) => {
            courseRows.forEach((row) => {
                row.classList.remove('is-selected');
                row.setAttribute("aria-current", "false");
            })

            courseRow.classList.add('is-selected');
            courseRow.setAttribute("aria-current", "true");
        })
    })

    const courseSearchInput = document.getElementById("course-search-input");
    const courseFilterButton = document.getElementById("course-filter-button");
    const courseFilterPanel = document.getElementById("course-filter-panel");
    const courseCapacityFrom = document.getElementById("course-capacity-from");
    const courseCapacityTo = document.getElementById("course-capacity-to");
    const courseAvailabilityFilter = document.getElementById("course-availability-filter");
    const courseFilterError = document.getElementById("course-filter-error");
    const courseFilterEmpty = document.getElementById("course-filter-empty");
    const courseResultCounts = [
        document.getElementById("course-total-count"),
        document.getElementById("course-panel-count")
    ];
    const activeCourseFilters = {minCapacity: null, maxCapacity: null, availability: "all"};

    function normalizeCourseSearch(value) {
        return value.trim().toLocaleLowerCase("vi")
            .normalize("NFD")
            .replace(/[\u0300-\u036f]/g, "")
            .replace(/đ/g, "d");
    }

    function filterCourses() {
        const query = normalizeCourseSearch(courseSearchInput.value);
        const visibleRows = [];

        courseRows.forEach((row) => {
            const searchableText = normalizeCourseSearch(
                "KH-" + row.dataset.courseId + " "
                + row.dataset.courseId + " "
                + row.dataset.courseTitle
            );
            const capacity = Number(row.dataset.courseCapacity);
            const enrolled = Number(row.dataset.courseEnrollmentCount);
            const isFull = enrolled >= capacity;
            const matchesQuery = searchableText.includes(query);
            const matchesMinCapacity = activeCourseFilters.minCapacity === null
                || capacity >= activeCourseFilters.minCapacity;
            const matchesMaxCapacity = activeCourseFilters.maxCapacity === null
                || capacity <= activeCourseFilters.maxCapacity;
            const matchesAvailability = activeCourseFilters.availability === "all"
                || (activeCourseFilters.availability === "available" && !isFull)
                || (activeCourseFilters.availability === "full" && isFull);
            const matches = matchesQuery && matchesMinCapacity
                && matchesMaxCapacity && matchesAvailability;

            row.hidden = !matches;
            const divider = row.nextElementSibling;
            if (divider?.classList.contains("divider")) divider.hidden = true;
            if (matches) visibleRows.push(row);
        });

        visibleRows.slice(0, -1).forEach((row) => {
            const divider = row.nextElementSibling;
            if (divider?.classList.contains("divider")) divider.hidden = false;
        });

        courseFilterEmpty.hidden = visibleRows.length > 0 || courseRows.length === 0;
        courseResultCounts.forEach((count) => {
            if (!count) return;
            if (count.id !== "course-total-count") {
                count.textContent = String(visibleRows.length);
                return;
            }
            const countLabel = visibleRows.length + " khóa học";
            count.textContent = typeof window.translateAppText === "function"
                ? window.translateAppText(countLabel)
                : countLabel;
        });
    }

    function closeCourseFilterPanel(restoreFocus = false) {
        courseFilterPanel.hidden = true;
        courseFilterButton.setAttribute("aria-expanded", "false");
        if (restoreFocus) courseFilterButton.focus();
    }

    courseFilterButton.addEventListener("click", () => {
        const willOpen = courseFilterPanel.hidden;
        courseFilterPanel.hidden = !willOpen;
        courseFilterButton.setAttribute("aria-expanded", String(willOpen));
        if (willOpen) courseCapacityFrom.focus();
    });

    function applyCourseFilters() {
        const minCapacity = courseCapacityFrom.value === "" ? null : Number(courseCapacityFrom.value);
        const maxCapacity = courseCapacityTo.value === "" ? null : Number(courseCapacityTo.value);
        if (minCapacity !== null && maxCapacity !== null && minCapacity > maxCapacity) {
            courseFilterError.hidden = false;
            courseCapacityFrom.focus();
            return;
        }

        courseFilterError.hidden = true;
        activeCourseFilters.minCapacity = minCapacity;
        activeCourseFilters.maxCapacity = maxCapacity;
        activeCourseFilters.availability = courseAvailabilityFilter.value;
        filterCourses();
        closeCourseFilterPanel();
    }

    document.getElementById("apply-course-filters").addEventListener("click", applyCourseFilters);
    [courseCapacityFrom, courseCapacityTo].forEach((input) => {
        input.addEventListener("keydown", (event) => {
            if (event.key === "Enter") applyCourseFilters();
        });
    });
    document.getElementById("reset-course-filters").addEventListener("click", () => {
        courseSearchInput.value = "";
        courseCapacityFrom.value = "";
        courseCapacityTo.value = "";
        courseAvailabilityFilter.value = "all";
        activeCourseFilters.minCapacity = null;
        activeCourseFilters.maxCapacity = null;
        activeCourseFilters.availability = "all";
        courseFilterError.hidden = true;
        filterCourses();
        closeCourseFilterPanel();
    });

    document.addEventListener("click", (event) => {
        if (!event.target.closest(".course-page .filter-content")) closeCourseFilterPanel();
    });
    document.addEventListener("keydown", (event) => {
        if (event.key === "Escape" && !courseFilterPanel.hidden) closeCourseFilterPanel(true);
    });
    courseSearchInput.addEventListener("input", filterCourses);
    courseSearchInput.addEventListener("keydown", (event) => {
        if (event.key === "Enter") filterCourses();
    });
    document.addEventListener("app-language-changed", filterCourses);
    filterCourses();

    const courseActions = document.getElementById("course-actions");
    const courseActionTrigger = document.getElementById("course-action-trigger");
    const courseActionMenu = document.getElementById("course-action-menu");
    const editCourseButton = document.querySelector("[data-edit-course]");
    const editCourseModal = document.getElementById("edit-course-modal");
    const editCourseTitle = document.getElementById("edit-course-title-input");
    const deleteCourseForm = document.getElementById("delete-course-form");

    function closeCourseActionMenu(restoreFocus = false) {
        if (!courseActionMenu || !courseActionTrigger) return;
        courseActionMenu.hidden = true;
        courseActionTrigger.setAttribute("aria-expanded", "false");
        if (restoreFocus) courseActionTrigger.focus();
    }

    if (courseActions && courseActionTrigger && courseActionMenu) {
        courseActionTrigger.addEventListener("click", () => {
            const willOpen = courseActionMenu.hidden;
            courseActionMenu.hidden = !willOpen;
            courseActionTrigger.setAttribute("aria-expanded", String(willOpen));
            if (willOpen) courseActionMenu.querySelector("button")?.focus();
        });

        document.addEventListener("click", (event) => {
            if (!courseActions.contains(event.target)) closeCourseActionMenu();
        });

        document.addEventListener("keydown", (event) => {
            if (event.key === "Escape" && !courseActionMenu.hidden) {
                closeCourseActionMenu(true);
            }
        });
    }

    if (editCourseButton && editCourseModal) {
        editCourseButton.addEventListener("click", () => {
            closeCourseActionMenu();
            editCourseModal.showModal();
            editCourseTitle?.focus();
        });

        editCourseModal.querySelectorAll("[data-close-edit-course]").forEach((button) => {
            button.addEventListener("click", () => editCourseModal.close());
        });

        editCourseModal.addEventListener("click", (event) => {
            if (event.target === editCourseModal) editCourseModal.close();
        });

        editCourseModal.addEventListener("close", () => courseActionTrigger?.focus());
    }

    if (deleteCourseForm) {
        deleteCourseForm.addEventListener("submit", (event) => {
            const title = deleteCourseForm.dataset.courseTitle;
            const enrollmentCount = Number(deleteCourseForm.dataset.enrollmentCount);
            const message = enrollmentCount > 0
                ? "Xóa khóa học \"" + title + "\" và " + enrollmentCount + " lượt đăng ký liên quan?"
                : "Xóa khóa học \"" + title + "\"?";
            const localizedMessage = typeof window.translateAppText === "function"
                ? window.translateAppText(message)
                : message;
            if (!window.confirm(localizedMessage)) event.preventDefault();
        });
    }
</script>
</body>
</html>
