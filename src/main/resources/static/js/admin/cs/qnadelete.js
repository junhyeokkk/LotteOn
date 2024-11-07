// 공통 삭제 요청 함수
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
                window.location.href = "/admin/cs/qna/list"; // 목록 페이지로 리다이렉트
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
function deleteInquiry(inquiryId) {
    if (confirm('정말로 이 문의하기를 삭제하시겠습니까?')) {
        fetch(`/api/admin/qna/delete/${inquiryId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then(response => {
                if (response.ok) {
                    alert('문의하기가 삭제되었습니다.');
                    window.location.href = "/admin/cs/qna/list"; // 삭제 후 목록으로 이동
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
function deleteSelectedInquiries() {
    const selectedIds = [];
    document.querySelectorAll('input[name="RowCheck"]:checked').forEach((checkbox) => {
        const inquiryId = checkbox.getAttribute('value');
        selectedIds.push(Number(inquiryId));
    });

    if (selectedIds.length > 0) {
        if (confirm('정말로 선택된 항목을 삭제하시겠습니까?')) {
            fetch('/admin/cs/qna/deleteSelected', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(selectedIds)
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        alert('선택된 문의하기가 삭제되었습니다.');
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

// 답변 등록 후 성공 메시지 팝업
document.addEventListener("DOMContentLoaded", function() {
    const replySuccessMessage = document.getElementById("replySuccessMessage");
    if (replySuccessMessage) {
        alert(replySuccessMessage.textContent);
    }
});
