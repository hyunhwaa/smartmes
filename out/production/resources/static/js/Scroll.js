window.addEventListener('DOMContentLoaded', adjustSidebarHeight);
window.addEventListener('resize', adjustSidebarHeight);

function adjustSidebarHeight() {
    const sidebar = document.getElementById('sidebar');
    const pageHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);

    sidebar.style.height = `${pageHeight}px`;
}