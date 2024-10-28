// 모달 창 열기
function openUserChangeModal(element) {
    const uid = element.getAttribute('data-user-id'); // uid를 동적으로 가져옴
    fetch(`/api/admin/member/${uid}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("회원 정보를 불러오는 데 실패했습니다.");
            }
            return response.json();
        })
        .then(data => {
            console.log("API 응답 데이터:", data); // API 응답 데이터를 출력하여 확인

            function setInputValue(selector, value, label) {
                const input = document.querySelector(selector);
                if (input) {
                    input.value = value || ''; // 데이터가 없을 경우 빈 문자열로 설정
                } else {
                    console.error(`${label} 입력란을 찾을 수 없습니다.`);
                }
            }

            // 각 필드에 값 설정
            setInputValue('input[name="uid"]', data.uid, '아이디');
            setInputValue('input[name="name"]', data.name, '이름');

            // 성별 선택 설정
            const genderInput = document.querySelector(`input[name="gender"][value="${data.gender}"]`);
            if (genderInput) {
                genderInput.checked = true;
            } else {
                console.error('성별 입력란을 찾을 수 없습니다.');
            }

            setInputValue('input[name="grade"]', data.grade, '등급');
            setInputValue('input[name="status"]', data.status, '상태');
            setInputValue('input[name="email"]', data.email, '이메일');
            setInputValue('input[name="ph"]', data.ph, '전화번호');

            // 우편번호 및 주소 설정
            setInputValue('input[name="zip"]', data.zip, '우편번호');
            setInputValue('input[name="addr1"]', data.addr1, '기본주소');
            setInputValue('input[name="addr2"]', data.addr2, '상세주소');

            // 가입일 및 최근 로그인 날짜 포맷 설정
            setInputValue('input[name="createdAt"]', formatDateTime(data.createdAt), '가입일');
            setInputValue('input[name="lastLoginDate"]', formatDateTime(data.lastLoginDate), '최근 로그인 날짜');

            // 기타 정보 설정
            const etcTextarea = document.querySelector('textarea[name="etc"]');
            if (etcTextarea) {
                etcTextarea.value = data.etc || ''; // etc가 없을 경우 빈 문자열로 설정
            } else {
                console.error('기타 입력란을 찾을 수 없습니다.');
            }

            // 모달 창 열기
            document.getElementById('userchangemodal').style.display = 'block';
        })
        .catch(error => {
            console.error('회원 데이터 가져오기 오류:', error);
            alert(error.message);
        });
}


// 날짜 형식 변환 함수
function formatDateTime(dateTime) {
    if (!dateTime) return '';
    const date = new Date(dateTime);
    return date.toLocaleString(); // YYYY-MM-DD HH:mm:ss 형식으로 변환
}

// 모달 창 닫기
function closeModal() {
    document.getElementById('userchangemodal').style.display = 'none';
}

// 폼 제출
function submitForm() {
    const uid = document.querySelector('input[name="uid"]').value;
    const data = {
        name: document.querySelector('input[name="name"]').value,
        gender: document.querySelector('input[name="gender"]:checked').value,
        email: document.querySelector('input[name="email"]').value,
        ph: document.querySelector('input[name="ph"]').value,
        zip: document.querySelector('input[name="zip"]').value,
        addr1: document.querySelector('input[name="addr1"]').value,
        addr2: document.querySelector('input[name="addr2"]').value,
        etc: document.querySelector('textarea[name="etc"]').value
    };

    fetch(`/api/admin/member/${uid}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("회원 정보를 수정하는 데 실패했습니다.");
            }
            return response.json();
        })
        .then(data => {
            alert('회원 정보가 성공적으로 수정되었습니다.');
            closeModal();
        })
        .catch(error => {
            console.error('회원 데이터 수정 오류:', error);
            alert(error.message);
        });

    return false;
}
