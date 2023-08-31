document.addEventListener('DOMContentLoaded', () => {
    // 엑셀로 내보내기 버튼 클릭 이벤트 처리
    const exportButton = document.querySelector('#fileSave');
    exportButton.addEventListener('click', exportToExcel);

    // 전체 선택 버튼 클릭 이벤트 처리
    const allSelectButton = document.querySelector('.allSelect');
    allSelectButton.addEventListener('click', selectAllRows);
});

// 전체 버튼 클릭
function selectAllRows() {
    const table = document.querySelector('.orderData');
    const checkboxes = table.querySelectorAll('input[type="checkbox"]');
    const allSelectButton = document.querySelector('.allSelect');

    const isChecked = allSelectButton.classList.toggle('selected');

    checkboxes.forEach(checkbox => {
        checkbox.checked = isChecked;
        const row = checkbox.closest('tr');
        changeColor(row, isChecked);
    });
}

function changeColor(row, isChecked) {
    if (isChecked) {
        row.style.backgroundColor = '#DDEAFD';
    } else {
        row.style.backgroundColor = '';
    }
}
