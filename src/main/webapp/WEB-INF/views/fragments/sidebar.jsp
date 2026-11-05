<%@ page pageEncoding="UTF-8" %>
<aside class="sidebar">
    <div class="brand">
        <div class="logo">
            <label name="logo" class="small-x1">SC</label>
        </div>
        <div class="brand-name">
            <label name="text" class="small-x1">Student Course</label>
            <label name="caption" class="small-x2">Management</label>
        </div>
    </div>
    <div class="divider"></div>
    <div class="sidebar-content">
        <label name="caption" class="small-x2">QUẢN LÝ</label>
        <nav aria-label="Menu chính">
            <a class="sidebar-item ${activeNav eq 'students' ? 'is-selected' : ''}"
               href="${pageContext.request.contextPath}/students">
                <svg class="nav-icon" viewBox="0 0 24 24" aria-hidden="true">
                    <path d="M16 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
                    <circle cx="10" cy="7" r="4" />
                    <path d="M20 21v-2a4 4 0 0 0-3-3.87M16 3.13a4 4 0 0 1 0 7.75" />
                </svg>
                <span>Sinh viên</span>
            </a>
            <a class="sidebar-item ${activeNav eq 'courses' ? 'is-selected' : ''}"
               href="${pageContext.request.contextPath}/courses">
                <svg class="nav-icon" viewBox="0 0 24 24" aria-hidden="true">
                    <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20" />
                    <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2Z" />
                    <path d="M8 7h8M8 11h8" />
                </svg>
                <span>Khóa học</span>
            </a>
            <a class="sidebar-item ${activeNav eq 'enrollments' ? 'is-selected' : ''}"
               href="${pageContext.request.contextPath}/enrollments">
                <svg class="nav-icon" viewBox="0 0 24 24" aria-hidden="true">
                    <path d="M9 5H5a2 2 0 0 0-2 2v13a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V7a2 2 0 0 0-2-2h-4" />
                    <path d="M9 3h6v4H9zM8 13h8M8 17h5" />
                    <path d="m8 9 1 1 2-2" />
                </svg>
                <span>Đăng ký học</span>
            </a>
            <a class="sidebar-item ${activeNav eq 'settings' ? 'is-selected' : ''}"
               href="${pageContext.request.contextPath}/settings">
                <svg class="nav-icon" viewBox="0 0 24 24" aria-hidden="true">
                    <circle cx="12" cy="12" r="3" />
                    <path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 1 1-2.83 2.83l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 1 1-4 0v-.09a1.65 1.65 0 0 0-1.08-1.51 1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 1 1-2.83-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 1 1 0-4h.09a1.65 1.65 0 0 0 1.51-1.08 1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 1 1 2.83-2.83l.06.06a1.65 1.65 0 0 0 1.82.33h.09A1.65 1.65 0 0 0 10 3.09V3a2 2 0 1 1 4 0v.09a1.65 1.65 0 0 0 1.08 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 1 1 2.83 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82v.09a1.65 1.65 0 0 0 1.51 1H21a2 2 0 1 1 0 4h-.09a1.65 1.65 0 0 0-1.51 1z" />
                </svg>
                <span>Cài đặt</span>
            </a>
        </nav>
    </div>

    <div class="user-mini">
        <div class="logo">
            <label name="logo" class="small-x2">AD</label>
        </div>
        <div class="brand-name">
            <label name="text" class="small-x1">Quản trị viên</label>
            <label name="caption" class="small-x2">Điều hành hệ thống</label>
        </div>
    </div>
</aside>
