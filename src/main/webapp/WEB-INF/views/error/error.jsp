<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Không thể hoàn tất yêu cầu</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/students.css?v=<%= System.currentTimeMillis() %>">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/error.css?v=<%= System.currentTimeMillis() %>">
    <script src="${pageContext.request.contextPath}/js/theme.js?v=<%= System.currentTimeMillis() %>"></script>
</head>
<body>
<div class="app-layout">
    <jsp:include page="/WEB-INF/views/fragments/sidebar.jsp"/>

    <div class="main-column">
        <jsp:include page="/WEB-INF/views/fragments/header.jsp"/>

        <main class="page-content error-page">
            <section class="error-card" aria-labelledby="error-title">
                <div class="error-card-heading">
                    <span class="error-icon" aria-hidden="true">
                        <svg viewBox="0 0 24 24">
                            <circle cx="12" cy="12" r="9"/>
                            <path d="M12 8v5M12 16h.01"/>
                        </svg>
                    </span>
                    <div>
                        <span class="error-eyebrow">YÊU CẦU CHƯA THÀNH CÔNG</span>
                        <h1 id="error-title">Không thể hoàn tất thao tác</h1>
                        <p>Hãy kiểm tra lại thông tin và thử lại.</p>
                    </div>
                </div>

                <div class="error-message" role="alert" aria-live="assertive">
                    <strong>Chi tiết</strong>
                    <p>
                        <c:choose>
                            <c:when test="${not empty errorMessage}">
                                <c:out value="${errorMessage}"/>
                            </c:when>
                            <c:otherwise>
                                Đã xảy ra lỗi khi xử lý yêu cầu. Vui lòng thử lại.
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>

                <div class="error-actions">
                    <button class="error-button secondary" type="button" onclick="window.history.back()">
                        Quay lại
                    </button>
                    <a class="error-button primary" href="${pageContext.request.contextPath}/students">
                        Về trang sinh viên
                    </a>
                </div>
            </section>
        </main>
    </div>
</div>
</body>
</html>
