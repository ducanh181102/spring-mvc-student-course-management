(function () {
    const STORAGE_KEY = "student-course-theme";
    const root = document.documentElement;
    const savedTheme = localStorage.getItem(STORAGE_KEY);
    let activeTheme = savedTheme === "light" ? "light" : "dark";

    function applyTheme(theme) {
        activeTheme = theme === "light" ? "light" : "dark";
        root.dataset.theme = activeTheme;
        root.style.colorScheme = activeTheme;

        const selector = document.getElementById("theme-select");
        if (selector) selector.value = activeTheme;
    }

    applyTheme(activeTheme);

    document.addEventListener("DOMContentLoaded", () => {
        const selector = document.getElementById("theme-select");
        if (!selector) return;

        // Reflect the theme loaded from localStorage in the settings control.
        selector.value = activeTheme;
        selector.addEventListener("change", () => {
            const theme = selector.value === "light" ? "light" : "dark";
            localStorage.setItem(STORAGE_KEY, theme);
            applyTheme(theme);
        });
    });
})();
