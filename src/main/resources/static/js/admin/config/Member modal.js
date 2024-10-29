// 모달 창 열기
function openUserChangeModal(element) {
    // 열릴 때 초기화 (기존 값 제거)
    document.querySelector('input[name="zip"]').value = '';
    document.querySelector('input[name="addr1"]').value = '';
    document.querySelector('input[name="addr2"]').value = '';

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
            setInputValue('input[name="zip"]', data.zip || '', '우편번호');
            setInputValue('input[name="addr1"]', data.addr1 || '', '기본주소');
            setInputValue('input[name="addr2"]', data.addr2 || '', '상세주소');


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

/**
 *
 */

// DOM이 완전히 로드된 후 실행
document.addEventListener("DOMContentLoaded", function () {
    // postcode2 함수를 window 객체에 추가
    window.postcode2 = function () {
        // daum Postcode API가 로드되지 않았으면 오류 로그 출력
        if (typeof daum === 'undefined' || !daum.Postcode) {
            console.error("Daum Postcode API가 로드되지 않았습니다.");
            return;
        }

        new daum.Postcode({
            oncomplete: function (data) {
                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                var addr = ''; // 주소 변수
                var extraAddr = ''; // 참고항목 변수

                // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === 'R') { // 도로명 주소 선택
                    addr = data.roadAddress;
                } else { // 지번 주소 선택
                    addr = data.jibunAddress;
                }

                // 도로명 주소일 경우 추가 정보를 조합
                if (data.userSelectedType === 'R') {
                    // 법정동명이 있을 경우 추가
                    if (data.bname && /[동|로|가]$/g.test(data.bname)) {
                        extraAddr += data.bname;
                    }
                    // 건물명이 있고, 공동주택일 경우 추가
                    if (data.buildingName && data.apartment === 'Y') {
                        extraAddr += (extraAddr ? ', ' + data.buildingName : data.buildingName);
                    }
                    // 참고항목이 있을 경우 괄호로 추가
                    if (extraAddr) {
                        extraAddr = ' (' + extraAddr + ')';
                    }
                }

                // 우편번호와 주소 정보를 필드에 넣기
                document.querySelector('input[name="zip"]').value = data.zonecode;
                document.querySelector('input[name="addr1"]').value = addr;
                // 커서를 상세주소 필드로 이동
                document.querySelector('input[name="addr2"]').focus();
            }
        }).open();
    };
});

document.addEventListener("DOMContentLoaded", function () {
    const insertBtn = document.querySelector(".insert_btn");

    if (insertBtn) {
        insertBtn.addEventListener("click", function () {
            let selectedMembers = [];  // 여기에서 선언

            const checkboxes = document.querySelectorAll("input[name='RowCheck']:checked");
            const validGrades = ["vvip", "vip", "gold", "silver", "family"];

            checkboxes.forEach(checkbox => {
                const memberRow = checkbox.closest("tr");
                const memberId = checkbox.value;
                const gradeSelect = memberRow.querySelector("select[name='grade']");

                if (gradeSelect) {
                    const grade = gradeSelect.value;
                    if (!validGrades.includes(grade)) {
                        alert("유효하지 않은 등급이 선택되었습니다.");
                        return;
                    }
                    selectedMembers.push({ uid: memberId, grade: grade });
                }
            });

            if (selectedMembers.length === 0) {
                alert("선택된 회원이 없습니다.");
                return;
            }

            // Ajax 요청
            fetch("/api/admin/member/update-grade", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(selectedMembers)
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error("서버 응답 오류: " + response.status);
                    }
                    return response.text();
                })
                .then(text => {
                    if (!text.trim()) {
                        throw new Error("서버에서 빈 응답이 수신되었습니다.");
                    }

                    let data;
                    try {
                        data = JSON.parse(text);
                    } catch (error) {
                        console.error("응답 데이터가 JSON 형식이 아닙니다:", text);
                        throw new Error("응답 데이터가 유효하지 않습니다.");
                    }

                    if (data.success) {
                        alert("회원 등급이 성공적으로 수정되었습니다.");
                        selectedMembers.forEach(member => {
                            const memberRow = document.querySelector(`tr[data-member-id="${member.uid}"]`);
                            const gradeSelect = memberRow.querySelector("select[name='grade']");
                            if (gradeSelect) {
                                gradeSelect.value = member.grade;
                            }
                        });
                    } else {
                        alert(data.message || "등급 수정에 실패했습니다.");
                    }
                })
                .catch(error => {
                    console.error("오류:", error);
                    alert("오류가 발생했습니다: " + error.message);
                });
        });
    } else {
        console.error("insert_btn 요소를 찾을 수 없습니다.");
    }
});
