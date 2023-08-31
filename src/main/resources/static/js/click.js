function toggleCheckbox(event) {
  const checkbox = event.target;
  const row = checkbox.closest('tr');
  checkbox.checked = !checkbox.checked;
  if (checkbox.checked) {
    row.style.backgroundColor = '#DDEAFD';
  } else {
    row.style.backgroundColor = '';
  }
}

// function changeColor(row) {
//   row.style.backgroundColor = '#DDEAFD';
// }

//라우팅 배정 버튼 이벤트


var buttons = document.getElementsByClassName("routingBtn");

for (var i = 0; i < buttons.length; i++) {
  buttons[i].addEventListener("click", function() {
    for (var j = 0; j < buttons.length; j++) {
      if (buttons[j] !== this) {
        buttons[j].classList.remove("clicked");
        buttons[j].style.backgroundColor = ""; // 초기 버튼 배경색으로 변경
        buttons[j].style.color = ""; // 초기 폰트색으로 변경
      }
    }

    if (this.classList.contains("clicked")) {
      this.classList.remove("clicked");
      this.style.backgroundColor = ""; // 초기 버튼 배경색으로 변경
      this.style.color = ""; // 초기 폰트색으로 변경
    } else {
      this.classList.add("clicked");
      this.style.backgroundColor = "#022454"; // 색상 변경
      this.style.color = "white"; // 색상 변경
    }
  });
}