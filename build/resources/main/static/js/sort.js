function sortTable(columnIndex) {
    const table = document.querySelector('.orderData');
    const tbody = table.querySelector('tbody');
    const rows = Array.from(tbody.rows);
    const isAscending = table.dataset.sortDirection === 'asc';

    rows.sort((a, b) => {
        const valueA = a.cells[columnIndex].innerText.toLowerCase();
        const valueB = b.cells[columnIndex].innerText.toLowerCase();
        return valueA.localeCompare(valueB, undefined, { numeric: true, sensitivity: 'base' });
    });

    if (!isAscending) {
        rows.reverse();
        table.dataset.sortDirection = 'asc';
    } else {
        table.dataset.sortDirection = 'desc';
    }

    tbody.innerHTML = '';
    rows.forEach(row => tbody.appendChild(row));
}