//정렬
let ascendingOrder = true;      //정렬순서 true->오름차순 false->내림차순
let clickCount = 0;
function clickDate() {
    let table, rows, switching, i, x, y, shouldSwitch;
    table = document.getElementById("orderData");
    switching = true;
    while (switching) {

        switching = false;
        console.log("1111111111111111");
        console.log(table.getElementsByTagName("tr"));
        rows = table.getElementsByTagName("tr");
        console.log("tr = " + rows);
        console.log(rows.length);
        for (i = 1; i < rows.length - 1; i++) {
            shouldSwitch = false;
            console.log("----------------");
            x = rows[i].getElementsByTagName("td")[3].valueOf();
            console.log(x);
            y = rows[i + 1].getElementsByTagName("td")[3].valueOf();
            const dateX = new Date(x.textContent);
            const dateY = new Date(y.textContent);
            console.log("dateX = " +dateX);
            console.log("dateY = " +dateY);
            console.log(dateY>dateX);
            if (ascendingOrder) {
                if (dateX > dateY) {    //오름차순인 경우
                    shouldSwitch = true;
                    break;
                }
            } else {
                if (dateX < dateY) {    //내림차순 정렬인 경우
                    shouldSwitch = true;
                    break;
                }
            }
        }
        if (shouldSwitch) {
            // 정렬 시 번호 열도 함께 이동
            let numberX = rows[i].getElementsByTagName("td")[0].textContent;
            let numberY = rows[i + 1].getElementsByTagName("td")[0].textContent;
            rows[i].getElementsByTagName("td")[0].textContent = numberY;
            rows[i + 1].getElementsByTagName("td")[0].textContent = numberX;

            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
        }
    }
    ascendingOrder = !ascendingOrder;   //다음클릭하면 정렬방향 반전
}

function clickNum() {
    console.log("11111111111111");
    clickCount++;
    let table, rows, switching, i, x, y, shouldSwitch;
    table = document.getElementById("orderData");
    switching = true;
    while (switching) {
        switching = false;
        rows = table.getElementsByTagName("tr");
        for (i = 1; i < rows.length - 2; i++) {
            shouldSwitch = false;
            x = rows[i].getElementsByTagName("td")[4];
            y = rows[i + 1].getElementsByTagName("td")[4];
            const numX = parseInt(x.textContent.match(/\d+/)[0]);   //숫자부분만 추출
            const numY = parseInt(y.textContent.match(/\d+/)[0]);

            if (ascendingOrder) {
                if (numX > numY) {
                    shouldSwitch = true;
                    break;
                }
            } else {
                if (numX < numY) {
                    shouldSwitch = true;
                    break;
                }
            }
        }
        if (shouldSwitch) {
            // 정렬 시 번호 열도 함께 이동
            let numberX = rows[i].getElementsByTagName("td")[0].textContent;
            let numberY = rows[i + 1].getElementsByTagName("td")[0].textContent;
            rows[i].getElementsByTagName("td")[0].textContent = numberY;
            rows[i + 1].getElementsByTagName("td")[0].textContent = numberX;

            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
        }
    }

    // 클릭 횟수가 짝수일 때 내림차순으로 정렬
    if (clickCount % 2 === 0) {
        ascendingOrder = !ascendingOrder;
    }
}


