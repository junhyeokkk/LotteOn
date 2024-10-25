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