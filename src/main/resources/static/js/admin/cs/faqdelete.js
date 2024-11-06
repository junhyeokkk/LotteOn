// 공통 함수로 삭제 요청 처리
function fetchDeleteRequest(url, successMessage) {
    fetch(url, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => {
            if (response.ok) {
                alert(successMessage);
                window.location.reload();
            } else {
                alert('삭제에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('삭제 중 오류가 발생했습니다.', error);
            alert('삭제 중 오류가 발생했습니다.');
        });
}

// 개별 삭제 기능
function deleteFaq(faqId) {
    console.log("Deleting Faq with ID:", faqId); // ID 확인용 콘솔 로그
    if (confirm('정말로 이 자주묻는질문을 삭제하시겠습니까?')) {
        fetch(`/api/admin/faq/delete/${faqId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then(response => {
                if (response.ok) {
                    alert('자주묻는질문이 삭제되었습니다.');
                    window.location.reload();
                } else {
                    alert('삭제에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('삭제 중 오류가 발생했습니다.', error);
                alert('삭제 중 오류가 발생했습니다.');
            });
    }
}

// 선택 삭제 기능
function deleteSelectedFaqs() {
    const selectedIds = [];
    document.querySelectorAll('input[name="RowCheck"]:checked').forEach((checkbox) => {
        const faqId = checkbox.getAttribute('value'); // 체크된 항목의 ID 가져오기
        selectedIds.push(Number(faqId));
    });

    if (selectedIds.length > 0) {
        if (confirm('정말로 선택된 항목을 삭제하시겠습니까?')) {
            fetch('/admin/cs/faq/deleteSelected', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(selectedIds)
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        alert('선택된 공지사항이 삭제되었습니다.');
                        window.location.reload();
                    } else {
                        alert('삭제 실패: ' + data.message);
                    }
                })
                .catch(error => {
                    console.error('삭제 중 오류 발생:', error);
                    alert('삭제 중 오류가 발생했습니다.');
                });
        }
    } else {
        alert('삭제할 항목을 선택하세요.');
    }

}
document.querySelector("input[name='allCheck']").addEventListener("change", function() {
    const isChecked = this.checked;
    document.querySelectorAll("input[name='RowCheck']").forEach((checkbox) => {
        checkbox.checked = isChecked;
    });
});