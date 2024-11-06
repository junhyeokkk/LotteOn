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
function deleteNotice(noticeId) {
    console.log("Deleting Notice with ID:", noticeId); // ID 확인용 콘솔 로그
    if (confirm('정말로 이 공지사항을 삭제하시겠습니까?')) {
        fetch(`/api/admin/notice/delete/${noticeId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then(response => {
                if (response.ok) {
                    alert('공지사항이 삭제되었습니다.');
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
function deleteSelectedNotices() {
    const selectedIds = [];
    document.querySelectorAll('input[name="RowCheck"]:checked').forEach((checkbox) => {
        const noticeId = checkbox.getAttribute('value'); // 체크된 항목의 ID 가져오기
        selectedIds.push(Number(noticeId));
    });

    if (selectedIds.length > 0) {
        if (confirm('정말로 선택된 항목을 삭제하시겠습니까?')) {
            fetch('/admin/cs/notice/deleteSelected', {
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
