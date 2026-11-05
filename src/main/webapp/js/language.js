(function () {
    const STORAGE_KEY = "student-course-language";
    const originalTexts = new WeakMap();
    const originalAttributes = new WeakMap();
    const originalTitle = document.title;
    let activeLanguage = "vi";

    const english = {
        "QUẢN LÝ": "MANAGEMENT", "Menu chính": "Main menu",
        "Không gian quản trị /": "Admin workspace /",
        "Sinh viên": "Students", "Khóa học": "Courses", "Đăng ký học": "Enrollments",
        "Cài đặt": "Settings", "Quản trị viên": "Administrator",
        "Điều hành hệ thống": "System administration", "Thông báo": "Notifications",
        "Tài khoản quản trị viên": "Administrator account",
        "Danh sách sinh viên": "Students", "Danh sách khóa học": "Courses",
        "Đăng ký khóa học": "Course registration",
        "Thông báo lỗi": "Error",
        "Quản lý tài khoản và tùy chọn giao diện.": "Manage your account and appearance preferences.",
        "Lưu thay đổi": "Save changes", "Nhóm cài đặt": "Settings sections",
        "Tài khoản": "Account", "Giao diện": "Appearance",
        "Thông tin tài khoản": "Account information",
        "Thông tin hiển thị của người quản trị.": "Administrator profile information.",
        "Họ và tên": "Full name", "Vai trò": "Role", "Quản trị hệ thống": "System administrator",
        "Ngôn ngữ": "Language", "Tiếng Việt": "Vietnamese", "Chủ đề màu": "Color theme",
        "Tối": "Dark", "Sáng": "Light",
        "Tùy chỉnh cách hiển thị trang quản trị.": "Customize how the dashboard looks.",
        "Chọn giao diện phù hợp với bạn.": "Choose the appearance that works for you.",
        "Chọn những thông báo bạn muốn nhận.": "Choose which notifications you want to receive.",
        "Thông báo đăng ký mới": "New enrollment notifications",
        "Nhận thông báo khi có sinh viên đăng ký khóa học.": "Get notified when a student enrolls in a course.",
        "Chọn hồ sơ để xem thông tin và khóa học đã đăng ký.": "Select a student to view their profile and enrolled courses.",
        "Quản lý thông tin và sức chứa của từng khóa học.": "Manage information and capacity for each course.",
        "Thêm sinh viên": "Add student", "Tìm sinh viên theo mã hoặc tên": "Search students by ID or name",
        "Tìm theo mã hoặc tên": "Search by ID or name", "Lọc": "Filter", "Lọc sinh viên": "Filter students",
        "Mã từ": "ID from", "Đến": "To", "Không giới hạn": "No limit",
        "KHÓA HỌC": "COURSE",
        "Tình trạng đăng ký": "Enrollment status", "Tất cả": "All",
        "Đã đăng ký khóa học": "Enrolled in a course", "Chưa đăng ký khóa học": "Not enrolled in a course",
        "Xóa lọc": "Clear filters", "Áp dụng": "Apply", "DỮ LIỆU MẪU": "SAMPLE DATA",
        "Chưa có sinh viên nào.": "There are no students yet.", "Mã sinh viên:": "Student ID:",
        "Đang học": "Enrolled", "Chưa đăng ký": "Not enrolled",
        "Chọn sinh viên để xem chi tiết.": "Select a student to view details.",
        "Thao tác hồ sơ": "Profile actions", "Thao tác hồ sơ sinh viên": "Student profile actions",
        "Chỉnh sửa thông tin": "Edit information", "Xóa sinh viên": "Delete student",
        "Thông tin sinh viên": "Student information", "Ngày nhập học": "Admission date",
        "Chương trình": "Program", "Công nghệ phần mềm": "Software engineering",
        "Niên khóa": "Academic years", "Khóa học đã đăng ký": "Enrolled courses",
        "Sinh viên chưa đăng ký học phần.": "This student is not enrolled in any courses.",
        "Học kỳ 1": "Semester 1", "SINH VIÊN": "STUDENT",
        "Nhập thông tin cơ bản cho sinh viên mới.": "Enter basic information for the new student.",
        "Mã sinh viên": "Student ID", "Ví dụ: 24056": "Example: 24056",
        "Nhập họ và tên": "Enter full name", "Hủy": "Cancel", "Đóng": "Close",
        "Chỉnh sửa hồ sơ": "Edit student profile", "Cập nhật họ tên sinh viên.": "Update the student's name.",
        "Tìm khóa học theo mã hoặc tên": "Search courses by ID or title",
        "Tìm theo mã hoặc tên khóa học": "Search by ID or title",
        "Lọc khóa học": "Filter courses", "Sức chứa từ": "Capacity from",
        "Tình trạng chỗ": "Availability", "Còn chỗ": "Available", "Đã đủ chỗ": "Full",
        "Chưa có khoá học nào.": "There are no courses yet.",
        "Chọn một khóa học để xem chi tiết": "Select a course to view details",
        "Mã khóa học:": "Course ID:", "Sức chứa:": "Capacity:",
        "Chi tiết khóa học Lập trình Java": "Course details",
        "Chọn khoá học để xem chi tiết.": "Select a course to view details.",
        "KHÓA HỌC": "COURSE", "Thao tác khóa học": "Course actions",
        "Sửa khóa học": "Edit course", "Xóa khóa học": "Delete course",
        "Thông tin khóa học": "Course information",
        "Thông tin cơ bản và sức chứa tối đa của khóa học.": "Basic information and maximum course capacity.",
        "Mã khóa học": "Course ID", "Sức chứa tối đa": "Maximum capacity",
        "Số lượng sinh viên đăng ký": "Enrolled students", "sinh viên": "students",
        "Thêm khóa học": "Add course", "Nhập mã, tên và sức chứa của khóa học.": "Enter the course ID, title, and capacity.",
        "Ví dụ: 302": "Example: 302", "Tên khóa học": "Course title",
        "Nhập tên khóa học": "Enter course title", "Ví dụ: 30 sinh viên": "Example: 30 students",
        "Chỉnh sửa khóa học": "Edit course", "Cập nhật tên khóa học và sức chứa.": "Update the course title and capacity.",
        "Ghi danh sinh viên vào khóa học và theo dõi các lượt đăng ký.": "Enroll students in courses and track registrations.",
        "Tìm lượt đăng ký theo sinh viên hoặc khóa học": "Search enrollments by student or course",
        "Tìm theo sinh viên hoặc khóa học": "Search by student or course",
        "Lọc lượt đăng ký": "Filter enrollments", "Tất cả sinh viên": "All students",
        "Tất cả khóa học": "All courses", "Tạo lượt đăng ký": "Create enrollment",
        "Chọn sinh viên và khóa học cần ghi danh.": "Choose a student and course to enroll.",
        "Chọn sinh viên": "Select a student", "Chọn khóa học": "Select a course",
        "Tạo đăng ký": "Create enrollment", "Quy tắc đăng ký": "Enrollment rules",
        "Để đăng ký thành công, hãy kiểm tra:": "Before enrolling, make sure:",
        "Sinh viên và khóa học phải tồn tại.": "The student and course must exist.",
        "Không đăng ký trùng khóa học.": "A student cannot enroll in the same course twice.",
        "Chọn đúng sinh viên và khóa học trước khi tạo lượt đăng ký.": "Choose the correct student and course before creating an enrollment.",
        "Danh sách đăng ký": "Enrollment list", "Các cặp sinh viên và khóa học đã ghi danh.": "Students and their enrolled courses.",
        "Chưa có đăng ký nào.": "There are no enrollments yet.", "Thao tác": "Actions",
        "Không tìm thấy lượt đăng ký phù hợp.": "No matching enrollments found.",
        "Không tìm thấy sinh viên phù hợp.": "No matching students found.",
        "Không tìm thấy khóa học phù hợp.": "No matching courses found.",
        "Không thể hoàn tất yêu cầu": "Unable to complete the request",
        "YÊU CẦU CHƯA THÀNH CÔNG": "REQUEST FAILED",
        "Không thể hoàn tất thao tác": "Unable to complete the action",
        "Hãy kiểm tra lại thông tin và thử lại.": "Check the information and try again.",
        "Chi tiết": "Details", "Đã xảy ra lỗi khi xử lý yêu cầu. Vui lòng thử lại.": "An error occurred while processing the request. Please try again.",
        "Quay lại": "Go back", "Về trang sinh viên": "Back to students"
    };

    function normalize(value) {
        return value.replace(/\s+/g, " ").trim();
    }

    function translate(value) {
        const phrase = normalize(value);
        let match = phrase.match(/^(\d+) khóa học$/i);
        if (match) return `${match[1]} ${match[1] === "1" ? "course" : "courses"}`;
        match = phrase.match(/^(\d+) lượt đăng ký$/i);
        if (match) return `${match[1]} ${match[1] === "1" ? "enrollment" : "enrollments"}`;
        match = phrase.match(/^(\d+) sinh viên$/i);
        if (match) return `${match[1]} ${match[1] === "1" ? "student" : "students"}`;
        match = phrase.match(/^Xóa khóa học "(.+)" và (\d+) lượt đăng ký liên quan\?$/);
        if (match) return `Delete course "${match[1]}" and its ${match[2]} related enrollments?`;
        match = phrase.match(/^Xóa khóa học "(.+)"\?$/);
        if (match) return `Delete course "${match[1]}"?`;
        match = phrase.match(/^Xóa sinh viên "(.+)" và các đăng ký khóa học của sinh viên này\?$/);
        if (match) return `Delete student "${match[1]}" and this student's course enrollments?`;
        match = phrase.match(/^Bạn chắc chắn muốn xóa khóa học '(.+)' của sinh viên '(.+)'\?$/);
        if (match) return `Are you sure you want to remove course '${match[1]}' from student '${match[2]}'?`;
        match = phrase.match(/^Hồ sơ (.+)$/);
        if (match) return `Profile: ${match[1]}`;
        match = phrase.match(/^Hủy đăng ký của (.+)$/);
        if (match) return `Cancel enrollment for ${match[1]}`;
        if (phrase.startsWith("Không gian quản trị / ")) {
            const section = phrase.substring("Không gian quản trị / ".length);
            return `Admin workspace / ${english[section] || section}`;
        }
        return english[phrase] || value;
    }

    function applyLanguage(language) {
        activeLanguage = language === "en" ? "en" : "vi";
        const isEnglish = activeLanguage === "en";
        document.documentElement.lang = isEnglish ? "en" : "vi";
        document.title = isEnglish ? translate(originalTitle) : originalTitle;

        const walker = document.createTreeWalker(document.body, NodeFilter.SHOW_TEXT);
        let textNode;
        while ((textNode = walker.nextNode())) {
            if (textNode.parentElement?.closest("script, style, noscript, textarea")) continue;
            if (!originalTexts.has(textNode)) originalTexts.set(textNode, textNode.nodeValue);
            const original = originalTexts.get(textNode);
            const leading = original.match(/^\s*/)[0];
            const trailing = original.match(/\s*$/)[0];
            const content = original.slice(leading.length, original.length - trailing.length);
            if (!content) continue;
            textNode.nodeValue = isEnglish ? leading + translate(content) + trailing : original;
        }

        document.querySelectorAll("[placeholder], [aria-label], [title]").forEach((element) => {
            let originals = originalAttributes.get(element);
            if (!originals) {
                originals = {};
                ["placeholder", "aria-label", "title"].forEach((attribute) => {
                    if (element.hasAttribute(attribute)) originals[attribute] = element.getAttribute(attribute);
                });
                originalAttributes.set(element, originals);
            }
            Object.entries(originals).forEach(([attribute, value]) => {
                element.setAttribute(attribute, isEnglish ? translate(value) : value);
            });
        });

        const selector = document.getElementById("language-select");
        if (selector) selector.value = isEnglish ? "en" : "vi";
        document.dispatchEvent(new CustomEvent("app-language-changed", {
            detail: {language: activeLanguage}
        }));
    }

    window.translateAppText = (value) => activeLanguage === "en" ? translate(value) : value;

    document.addEventListener("DOMContentLoaded", () => {
        const savedLanguage = localStorage.getItem(STORAGE_KEY) === "en" ? "en" : "vi";
        applyLanguage(savedLanguage);

        document.getElementById("language-select")?.addEventListener("change", (event) => {
            const language = event.target.value === "en" ? "en" : "vi";
            localStorage.setItem(STORAGE_KEY, language);
            applyLanguage(language);
        });
    });
})();
