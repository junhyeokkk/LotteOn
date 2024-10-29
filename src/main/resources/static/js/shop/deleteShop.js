window.onload = function() {
    // 전체 선택 / 해제 기능
    const allCheckbox = document.querySelector("input[name='allCheck']");
    if (allCheckbox) {
        allCheckbox.addEventListener("click", function() {
            toggleAll(this);
        });
    }

    // 선택 삭제 버튼이 있는 경우 클릭 이벤트 추가
    const deleteButton = document.querySelector(".delete_btn");
    if (deleteButton) {
        deleteButton.addEventListener("click", confirmDelete);  // JS에서만 이벤트를 연결
    }
};

function toggleAll(source) {
    const checkboxes = document.getElementsByName("RowCheck");
    checkboxes.forEach(checkbox => checkbox.checked = source.checked);
}

function confirmDelete() {
    const selectedIds = Array.from(document.getElementsByName("RowCheck"))
        .filter(checkbox => checkbox.checked)
        .map(checkbox => checkbox.value);

    if (selectedIds.length === 0) {
        alert("삭제할 항목을 선택하세요.");
        return;
    }

    if (confirm("선택된 항목을 삭제하시겠습니까?")) {
        fetch('/admin/shop/delete', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(selectedIds)
        })
            .then(response => {
                if (response.ok) {
                    alert("삭제가 완료되었습니다.");
                    location.reload();
                } else {
                    alert("삭제 중 오류가 발생했습니다.");
                }
            })
            .catch(error => console.error("Error:", error));
    }
}
