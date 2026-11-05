<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Cài đặt</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/students.css?v=<%= System.currentTimeMillis() %>">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/settings.css?v=<%= System.currentTimeMillis() %>">
    <script src="${pageContext.request.contextPath}/js/theme.js?v=<%= System.currentTimeMillis() %>"></script>
</head>
<body>
<div class="app-layout">
    <jsp:include page="/WEB-INF/views/fragments/sidebar.jsp" />

    <div class="main-column">
        <jsp:include page="/WEB-INF/views/fragments/header.jsp" />

        <main class="page-content settings-page">
            <div class="header-content">
                <div class="title-page">
                    <label class="medium" name="text">Cài đặt</label>
                    <label class="small-x2" name="caption">Quản lý tài khoản và tùy chọn giao diện.</label>
                </div>
                <button class="addBtn settings-save-top" type="button">
                    <svg viewBox="0 0 24 24" aria-hidden="true">
                        <path d="M19 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11l5 5v11a2 2 0 0 1-2 2Z" />
                        <path d="M17 21v-8H7v8M7 3v5h8" />
                    </svg>
                    <span>Lưu thay đổi</span>
                </button>
            </div>

            <section class="main-content settings-main-content">
                <div class="settings-workspace">
                    <nav class="settings-nav" aria-label="Nhóm cài đặt">
                        <a class="settings-tab is-active" href="#settings-profile">
                            <svg viewBox="0 0 24 24" aria-hidden="true">
                                <circle cx="12" cy="8" r="4" />
                                <path d="M5 21a7 7 0 0 1 14 0" />
                            </svg>
                            <span>Tài khoản</span>
                        </a>
                        <a class="settings-tab" href="#settings-appearance">
                            <svg viewBox="0 0 24 24" aria-hidden="true">
                                <circle cx="12" cy="12" r="9" />
                                <path d="M12 3a9 9 0 0 0 0 18z" />
                            </svg>
                            <span>Giao diện</span>
                        </a>
                        <a class="settings-tab" href="#settings-notifications">
                            <svg viewBox="0 0 24 24" aria-hidden="true">
                                <path d="M18 8a6 6 0 0 0-12 0c0 7-3 7-3 9h18c0-2-3-2-3-9" />
                                <path d="M10 21h4" />
                            </svg>
                            <span>Thông báo</span>
                        </a>
                    </nav>

                    <div class="settings-content">
                        <section class="settings-panel" id="settings-profile" aria-labelledby="settings-profile-title">
                            <div class="settings-panel-heading">
                                <div class="settings-panel-icon" aria-hidden="true">
                                    <svg viewBox="0 0 24 24">
                                        <circle cx="12" cy="8" r="4" />
                                        <path d="M5 21a7 7 0 0 1 14 0" />
                                    </svg>
                                </div>
                                <div>
                                    <h2 id="settings-profile-title">Thông tin tài khoản</h2>
                                    <p>Thông tin hiển thị của người quản trị.</p>
                                </div>
                            </div>

                            <div class="settings-fields">
                                <label>
                                    Họ và tên
                                    <input type="text" value="Quản trị viên">
                                </label>
                                <label>
                                    Email
                                    <input type="email" value="admin@example.com">
                                </label>
                                <label>
                                    Vai trò
                                    <input type="text" value="Quản trị hệ thống" readonly>
                                </label>
                                <label>
                                    Ngôn ngữ
                                <select id="language-select" aria-label="Ngôn ngữ">
                                        <option value="vi" selected>Tiếng Việt</option>
                                        <option value="en">English</option>
                                    </select>
                                </label>
                            </div>
                        </section>

                        <section class="settings-panel" id="settings-appearance" aria-labelledby="settings-appearance-title">
                            <div class="settings-panel-heading">
                                <div class="settings-panel-icon" aria-hidden="true">
                                    <svg viewBox="0 0 24 24">
                                        <circle cx="12" cy="12" r="9" />
                                        <path d="M12 3a9 9 0 0 0 0 18z" />
                                    </svg>
                                </div>
                                <div>
                                    <h2 id="settings-appearance-title">Giao diện</h2>
                                    <p>Tùy chỉnh cách hiển thị trang quản trị.</p>
                                </div>
                            </div>

                            <div class="settings-option-row">
                                <div>
                                    <strong>Chủ đề màu</strong>
                                    <p>Chọn giao diện phù hợp với bạn.</p>
                                </div>
                                <select id="theme-select" aria-label="Chủ đề màu">
                                    <option value="dark" selected>Tối</option>
                                    <option value="light">Sáng</option>
                                </select>
                            </div>
                        </section>

                        <section class="settings-panel" id="settings-notifications" aria-labelledby="settings-notifications-title">
                            <div class="settings-panel-heading">
                                <div class="settings-panel-icon" aria-hidden="true">
                                    <svg viewBox="0 0 24 24">
                                        <path d="M18 8a6 6 0 0 0-12 0c0 7-3 7-3 9h18c0-2-3-2-3-9" />
                                        <path d="M10 21h4" />
                                    </svg>
                                </div>
                                <div>
                                    <h2 id="settings-notifications-title">Thông báo</h2>
                                    <p>Chọn những thông báo bạn muốn nhận.</p>
                                </div>
                            </div>

                            <label class="settings-toggle-row">
                                <span>
                                    <strong>Thông báo đăng ký mới</strong>
                                    <small>Nhận thông báo khi có sinh viên đăng ký khóa học.</small>
                                </span>
                                <input type="checkbox" checked>
                            </label>
                        </section>
                    </div>
                </div>
            </section>
        </main>
    </div>
</div>
<script>
    const settingsTabs = document.querySelectorAll(".settings-tab");

    settingsTabs.forEach((tab) => {
        tab.addEventListener("click", () => {
            settingsTabs.forEach((item) => item.classList.remove("is-active"));
            tab.classList.add("is-active");
        });
    });
</script>
</body>
</html>
