// 전체 선택 함수
function toggleAllChecks(checkbox) {
    // 모든 개별 체크박스를 선택
    const checkboxes = document.querySelectorAll("input[name='RowCheck']");

    // 전체 선택 체크박스 상태에 따라 모든 체크박스를 선택 또는 해제
    checkboxes.forEach((RowCheck) => {
        RowCheck.checked = checkbox.checked;
    })
}

