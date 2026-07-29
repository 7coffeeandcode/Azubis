// ==========================================
// Theme-Steuerung (Dark / Light Mode)
// ==========================================

function initTheme() {
    const savedTheme = localStorage.getItem('theme');

    if (savedTheme === 'dark' || (!savedTheme && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
        document.documentElement.classList.add('dark');
        updateThemeUI(true);
    } else {
        document.documentElement.classList.remove('dark');
        updateThemeUI(false);
    }
}

function toggleTheme() {
    const isDark = document.documentElement.classList.toggle('dark');
    localStorage.setItem('theme', isDark ? 'dark' : 'light');
    updateThemeUI(isDark);

    if (typeof updateMapTheme === 'function') {
        updateMapTheme(isDark);
    }
}

function updateThemeUI(isDark) {
    const themeIcon = document.getElementById('themeIcon');
    const themeText = document.getElementById('themeText');

    if (isDark) {
        if (themeIcon) themeIcon.className = 'fa-solid fa-moon text-blue-400';
        if (themeText) themeText.innerText = 'Dunkel';
    } else {
        if (themeIcon) themeIcon.className = 'fa-solid fa-sun text-amber-500';
        if (themeText) themeText.innerText = 'Hell';
    }
}