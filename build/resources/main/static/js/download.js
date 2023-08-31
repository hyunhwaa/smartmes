
// 클릭한 테이블도 엑셀저장 가능
// 클라이언트 측 JavaScript
document.addEventListener('DOMContentLoaded', () => {
    // 엑셀로 내보내기 버튼 클릭 이벤트 처리
    const exportButton = document.querySelector('#fileSave');
    exportButton.addEventListener('click', exportToExcel);

    // 전체 선택 버튼 클릭 이벤트 처리
    const allSelectButton = document.querySelector('.allSelect');
    allSelectButton.addEventListener('click', selectAllRows);

    // 테이블 내 각 행 클릭 이벤트 처리
    const table = document.querySelector('.orderData');
    table.addEventListener('click', toggleRowColor);

    function selectAllRows() {
        const checkboxes = table.querySelectorAll('input[type="checkbox"]');
        const isChecked = allSelectButton.classList.toggle('selected');

        checkboxes.forEach(checkbox => {
            checkbox.checked = isChecked;
            const row = checkbox.closest('tr');
            changeColor(row, isChecked);
        });

        if (!isChecked) {
            resetRowColors(table);
        }
    }
});

function toggleRowColor(event) {
    const row = event.target.closest('tr');
    if (!row || event.target.tagName === 'INPUT') {
        return; // 체크박스를 클릭한 경우 무시
    }
    const checkbox = row.querySelector('input[type="checkbox"]');
    checkbox.checked = !checkbox.checked;
    changeColor(row, checkbox.checked);
}

//전체버튼 클릭
function selectAllRows() {
    console.log("dhlsdofjsd")
    const table = document.querySelector('.orderData');
    const checkboxes = table.querySelectorAll('input[type="checkbox"]');
    const allSelectButton = document.querySelector('.allSelect');

    const isChecked = allSelectButton.classList.toggle('selected');

    checkboxes.forEach(checkbox => {
        checkbox.checked = isChecked;
        const row = checkbox.closest('tr');
        changeColor(row, isChecked);
    });

    // 전체 선택 버튼을 다시 눌렀을 때 원래 색상으로 돌아오도록 처리
    if (!isChecked) {
        resetRowColors(table);
    }
}

function changeColor(row, isChecked) {
    if (isChecked) {
        row.style.backgroundColor = '#DDEAFD';
    } else {
        row.style.backgroundColor = ''; // 기본값으로 돌아가도록 함
    }
}

function resetRowColors(table) {
    const rows = table.querySelectorAll('tr');
    rows.forEach(row => {
        row.style.backgroundColor = ''; // 기본값으로 돌아가도록 함
    });
}

function exportToExcel() {
    const tables = document.querySelectorAll('.orderData'); // 모든 테이블 요소 선택
    const workbook = XLSX.utils.book_new();

    tables.forEach((table, index) => {
        // 선택한 행 데이터 추출
        const selectedRows = getSelectedRows(table);

        // 선택한 행 데이터로 엑셀 워크시트 생성
        const worksheet = createWorksheet(selectedRows, table);

        // 워크시트를 워크북에 추가
        const sheetName = `SmartMes Data ${index + 1}`;
        XLSX.utils.book_append_sheet(workbook, worksheet, sheetName);
    });

    // 엑셀 파일 생성 및 다운로드
    const excelBuffer = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });
    saveAsExcelFile(excelBuffer, 'SmartMes.xlsx');
}

function getSelectedRows(table) {
    const checkboxes = table.querySelectorAll('input[type="checkbox"]:checked');
    const selectedRows = Array.from(checkboxes).map(checkbox => checkbox.closest('tr'));
    return selectedRows;
}

function createWorksheet(rows, table) {
    const worksheet = XLSX.utils.aoa_to_sheet([]);
    const headerRow = table.querySelector('thead tr');
    const headerCells = headerRow.querySelectorAll('td');
    const header = Array.from(headerCells).map(cell => cell.innerText);
    XLSX.utils.sheet_add_aoa(worksheet, [header], { origin: 'A1' });

    rows.forEach((row, index) => {
        const rowData = [];
        const cells = row.querySelectorAll('td');
        cells.forEach(cell => {
            rowData.push(cell.innerText);
        });
        XLSX.utils.sheet_add_aoa(worksheet, [rowData], { origin: `A${index + 2}` });
    });

    return worksheet;
}

function saveAsExcelFile(buffer, fileName) {
    const data = new Blob([buffer], { type: 'application/octet-stream' });
    saveAs(data, fileName);
}





