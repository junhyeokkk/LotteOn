// 전체 선택 함수
function toggleAllChecks(checkbox) {
    // 모든 개별 체크박스를 선택
    const checkboxes = document.querySelectorAll("input[name='RowCheck']");

    // 전체 선택 체크박스 상태에 따라 모든 체크박스를 선택 또는 해제
    checkboxes.forEach((RowCheck) => {
        RowCheck.checked = checkbox.checked;
    })
}

function deleteSelectedPoints() {
    const selectedIds = Array.from(document.querySelectorAll('input[name="RowCheck"]:checked'))
        .map(checkbox => Number(checkbox.value)); // 포인트 ID를 가져옴

    if (selectedIds.length === 0) {
        alert("삭제할 포인트를 선택해 주세요.");
        return;
    }

    if (confirm("선택된 포인트 내역을 삭제하시겠습니까?")) {
        fetch("/admin/member/delete", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(selectedIds)
        })
            .then(response => {
                if (response.ok) {
                    alert("삭제가 완료되었습니다.");
                    location.reload();
                } else {
                    return response.text().then(text => {
                        alert("삭제 중 오류가 발생했습니다: " + text);
                    });
                }
            })
            .catch(error => {
                console.error("Error:", error);
                alert("서버 통신 중 오류가 발생했습니다.");
            });
    }
}
