// 상세정보 열기/닫기 함수
function toggleDetails() {
    const detailTable = document.querySelector('.detail-table');
    const icon = document.querySelector('.toggle-icon');

    // 테이블 표시 상태를 토글
    if (detailTable.style.display === "none" || detailTable.style.display === "") {
        detailTable.style.display = "table";
        icon.textContent = "▲";
    } else {
        detailTable.style.display = "none";
        icon.textContent = "▼";
    }
}
