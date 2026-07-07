// 🌙 Toggle Dark Mode
function toggleDarkMode() {
    const body = document.body;
    const icon = document.getElementById("toggle-icon");

    body.classList.toggle('dark-mode');
    const isDark = body.classList.contains('dark-mode');

    localStorage.setItem("darkMode", isDark ? "enabled" : "disabled");
    icon.textContent = isDark ? "☀️" : "🌙";
}

// ⏰ Update Date & Time
function updateDateTime() {
    const now = new Date();
    const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };

    const dateEl = document.getElementById('current-date');
    const timeEl = document.getElementById('current-time');

    if (dateEl && timeEl) {
        dateEl.textContent = `Date: ${now.toLocaleDateString('en-US', options)}`;
        timeEl.textContent = `Time: ${now.toLocaleTimeString()}`;
    }
}

// 🚀 Initialize
document.addEventListener("DOMContentLoaded", () => {

    // Restore dark mode
    if (localStorage.getItem("darkMode") === "enabled") {
        document.body.classList.add("dark-mode");
        const icon = document.getElementById("toggle-icon");
        if (icon) icon.textContent = "☀️";
    }

    // Start clock if elements exist
    if (document.getElementById('current-time')) {
        updateDateTime();
        setInterval(updateDateTime, 1000);
    }
});