/*
    날짜 : 2024/10/23
    이름 : 최준혁
    내용 : 버전 js 파일 생성
*/
window.onload = function () {

    // 모달 닫기
    function closeModal() {
        document.getElementById('versioninsertModal').style.display = 'none';
    }
    //버전등록
    document.getElementById('versionForm').addEventListener('submit', function(event) {
        event.preventDefault(); // 기본 폼 제출 방지

        const formData = new FormData(this);
        const versionData = {
            version: formData.get('version'),
            content: formData.get('content'),
            uid: 'your_uid' // UID 값을 적절하게 설정
        };

        fetch('/admin/config/api/version', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(versionData)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                console.log('버전등록 성공', data);
                alert('버전등록 성공');
                closeModal();
                // 추가적인 성공 처리 (예: 알림 메시지 표시)
            })
            .catch(error => {
                console.error('문제가 발생했습니다:', error);
                // 추가적인 오류 처리
            });
    });

    // 모달 링크 클릭 이벤트
    document.querySelectorAll('.version-check').forEach(function (link) {
        link.addEventListener('click', function (event) {
            event.preventDefault(); // 기본 링크 클릭 동작 방지

            // 데이터 속성으로부터 버전 정보와 내용을 가져옴
            const version = this.getAttribute('data-version');
            const content = this.getAttribute('data-content');

            // 모달에 데이터 설정
            document.getElementById('modalVersion').innerText = version;
            document.getElementById('modalContent').value = content;

            // 모달 열기
            document.getElementById('versioncheckModal').style.display = 'block';
        });
    });

    // 모달 닫기 버튼 클릭 이벤트
    document.getElementById('closeModal').addEventListener('click', function () {
        document.getElementById('versioncheckModal').style.display = 'none';
    });

    // 모달 닫기 버튼 클릭 이벤트
    document.getElementById('modalcanclebutton').addEventListener('click', function () {
        document.getElementById('versioncheckModal').style.display = 'none';
    });

};

// 전체 선택 및 해제 기능
function toggleSelectAll(selectAllCheckbox) {
    const checkboxes = document.querySelectorAll('.versionCheckbox');
    checkboxes.forEach(checkbox => {
        checkbox.checked = selectAllCheckbox.checked;
    });
}
// 선택된 버전을 삭제하는 기능
function deleteSelectedVersions() {
    // 선택된 버전 ID들을 수집
    const selectedIds = Array.from(document.querySelectorAll('.versionCheckbox:checked'))
        .map(checkbox => checkbox.value);

    if (selectedIds.length === 0) {
        alert("삭제할 버전을 선택하세요.");
        return;
    }

    // 서버로 삭제 요청
    fetch('/admin/config/api/version/delete', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ ids: selectedIds })
    })
        .then(response => {
            if (response.ok) {
                alert("선택된 버전이 삭제되었습니다.");
                // 삭제된 항목을 화면에서 제거
                selectedIds.forEach(id => {
                    const row = document.querySelector(`.versionCheckbox[value="${id}"]`).closest('tr');
                    row.parentNode.removeChild(row);
                });
            } else {
                alert("버전 삭제에 실패했습니다.");
            }
        })
        .catch(error => console.error("삭제 요청 중 오류:", error));
}
