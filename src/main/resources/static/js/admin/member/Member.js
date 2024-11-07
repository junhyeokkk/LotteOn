/*
     날짜 : 2024/10/28
     이름 : 박서홍
     내용 : 멤버 js 파일 생성

     수정내역
     - 2024/11/07 이도영 관리자 변경 기능 추가
*/



// 상태 값을 한글로 변환하는 함수
function getStatusText(status) {
    switch (status) {
        case 0:
            return '정상';
        case 2:
            return '중지';
        case 3:
            return '휴면';
        case 4:
            return '탈퇴'; // 비활성
        default:
            return '알 수 없음';
    }
}

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

            // status 값을 한글로 변환하여 설정
            const statusText = getStatusText(data.status);
            setInputValue('input[name="status"]', statusText, '상태');

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
    // 페이지 로드 시 최신 데이터 동기화
    fetchLatestMemberData();

    // 선택 수정 버튼 이벤트 설정
    const insertBtn = document.querySelector(".insert_btn");

    if (!insertBtn) {
        console.error("insert_btn 요소를 찾을 수 없습니다.");
        return;
    }

    insertBtn.addEventListener("click", function () {
        const selectedMembers = [];
        const checkboxes = document.querySelectorAll("input[name='RowCheck']:checked");
        const validGrades = ["vvip", "vip", "gold", "silver", "family", "admin"];

        checkboxes.forEach(checkbox => {
            const memberRow = checkbox.closest("tr");
            const memberId = checkbox.value;
            const gradeSelect = memberRow.querySelector("select[name='grade']");

            if (gradeSelect) {
                const grade = gradeSelect.value;
                if (validGrades.includes(grade)) {
                    selectedMembers.push({uid: memberId, grade: grade});
                } else {
                    alert("유효하지 않은 등급이 선택되었습니다.");
                }
            }
        });

        if (selectedMembers.length === 0) {
            alert("선택된 회원이 없습니다.");
            return;
        }

        // Ajax 요청으로 선택된 회원 등급 업데이트
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
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    alert("회원 등급이 성공적으로 수정되었습니다.");
                    updateMemberGradesInDOM(selectedMembers);
                    location.reload();
                } else {
                    alert(data.message || "등급 수정에 실패했습니다.");
                }
            })
            .catch(error => {
                console.error("오류:", error);
                alert("오류가 발생했습니다: " + error.message);
            });
    });
});

function fetchLatestMemberData() {
    fetch("/api/admin/member/list")
        .then(response => {
            if (!response.ok) {
                throw new Error("서버 응답 오류: " + response.status);
            }
            return response.json();
        })
        .then(data => {
            console.log("받아온 데이터:", data); // 데이터를 콘솔에 출력하여 확인

            if (!data || !Array.isArray(data) || data.length === 0) {
                console.warn("memberList가 비어있거나 없습니다.");
                return;
            }

            data.forEach(member => {
                const memberRow = document.querySelector(`tr[data-member-id="${member.uid}"]`);
                const gradeSelect = memberRow ? memberRow.querySelector("select[name='grade']") : null;

                if (gradeSelect) {
                    // grade 값을 소문자로 변환하여 설정
                    const gradeValue = member.grade ? member.grade.toLowerCase() : "family"; // 기본값을 family로 설정
                    console.log(`Setting grade for ${member.uid}: ${gradeValue}`);
                    gradeSelect.value = gradeValue;
                } else {
                    console.warn(`gradeSelect 요소가 없습니다. uid: ${member.uid}`);
                }
            });
        })
        .catch(error => console.error("최신 데이터 동기화 오류:", error));
}

// 선택된 등급을 DOM에 직접 반영
function updateMemberGradesInDOM(selectedMembers) {
    selectedMembers.forEach(member => {
        const memberRow = document.querySelector(`tr[data-member-id="${member.uid}"]`);
        if (memberRow) {
            const gradeSelect = memberRow.querySelector("select[name='grade']");
            if (gradeSelect) {
                gradeSelect.value = member.grade; // DOM에 변경된 등급 반영
            } else {
                console.warn(`gradeSelect 요소가 없습니다. uid: ${member.uid}`);
            }
        } else {
            console.warn(`memberRow가 존재하지 않습니다. uid: ${member.uid}`);
        }
    });
}

// 전체 선택 함수
function toggleAllChecks(checkbox) {
    // 모든 개별 체크박스를 선택
    const checkboxes = document.querySelectorAll("input[name='RowCheck']");

    // 전체 선택 체크박스 상태에 따라 모든 체크박스를 선택 또는 해제
    checkboxes.forEach((rowCheck) => {
        rowCheck.checked = checkbox.checked;
    });
}

// 상태 변경 버튼 클릭 시 호출되는 함수
function handleStatusClick(element) {
    const uid = element.getAttribute('data-uid');
    const currentStatus = element.getAttribute('data-status');

    console.log(`handleStatusClick: uid=${uid}, currentStatus=${currentStatus}`);
    confirmStatusChange(uid, getStatusAction(currentStatus));  // 상태 변경 확인
}

// 상태 변경을 확인하는 함수
function confirmStatusChange(uid, action) {
    if (confirm(getConfirmationMessage(uid, action))) {
        applyStatusChange(uid, action);  // 상태 변경 함수 호출
    }
}

// 상태 변경에 대한 메시지를 반환하는 함수
function getConfirmationMessage(uid, action) {
    const messages = {
        'suspend': `${uid} 회원을 중지하시겠습니까?`,
        'reactivate': `${uid} 회원을 재개하시겠습니까?`,
        'deactivate': `${uid} 회원을 비활성하시겠습니까?`
    };
    return messages[action] || "상태를 변경하시겠습니까?";
}

// 실제 상태 변경을 적용하는 함수
function applyStatusChange(uid, action) {
    const endpointMap = {
        'suspend': `/api/admin/member/suspend/${uid}`,     // 중지 API
        'reactivate': `/api/admin/member/reactivate/${uid}`, // 재개 API
        'deactivate': `/api/admin/member/deactivate/${uid}` // 비활성 API
    };

    console.log("현재 UID:", uid, "액션:", action); // UID 및 액션 확인
    fetch(endpointMap[action], {method: action === 'deactivate' ? 'DELETE' : 'PUT'})
        .then(response => {
            if (!response.ok) {
                return response.json().then(errData => {
                    throw new Error(errData.message || "상태 변경 실패");
                });
            }
            return response.json(); // 응답을 JSON으로 파싱
        })
        .then(data => {
            console.log("API 응답:", data);
            const newStatus = data.newStatus; // 실제 변경된 상태로 설정
            updateStatusUI(uid, newStatus); // UI 업데이트 함수 호출

            // 사용자에게 성공 메시지 표시
            showAlert(data.message);
            location.reload();  // 상태 변경 후 페이지 새로고침
        })
        .catch(error => {
            console.error("상태 변경 오류:", error);
            showAlert(error.message);
        });
}

// 알림 메시지 표시 함수
function showAlert(message) {
    alert(message);
}

// 상태 변경에 따라 UI 업데이트
function updateStatusUI(uid, newStatus) {
    const statusCell = document.querySelector(`td[data-uid="${uid}"]`);
    if (statusCell) {
        statusCell.textContent = getStatusLabel(newStatus); // 상태 라벨 설정

        // 기존 클래스 제거 및 새로운 클래스 추가
        statusCell.className = '';  // 기존 클래스 초기화
        const newClass = getStatusClass(getStatusAction(newStatus)); // 액션에 따라 클래스 설정
        statusCell.classList.add(newClass);
    }
}

// 상태별 클래스를 반환하는 함수
function getStatusClass(action) {
    const classes = {
        'suspend': 'status-stopped',
        'reactivate': 'status-normal',
        'deactivate': 'status-exit'
    };
    return classes[action] || 'status-unknown';
}

// 현재 상태에 따라 필요한 액션 반환 (중지 기능 포함)
function getStatusAction(currentStatus) {
    switch (parseInt(currentStatus)) {
        case 0:
            return 'suspend';      // 정상인 경우 중지
        case 2:
        case 3:
            return 'reactivate';    // 중지 또는 휴면일 경우 재개
        case 4:
            return 'deactivate';    // 탈퇴
        default:
            return '';              // 알 수 없음
    }
}

// 상태에 따른 라벨을 반환하는 함수
function getStatusLabel(status) {
    console.log(`Received status: ${status}`); // 함수가 호출될 때 상태 출력
    switch (parseInt(status)) {
        case 0:
            return '정상';
        case 2:
            return '중지';
        case 3:
            return '휴면';
        case 4:
            return '탈퇴';
        default:
            return '알 수 없음';
    }
}

// 상태 값을 액션에 따라 결정하는 함수
function getStatusValue(action) {
    switch (action) {
        case 'suspend':
            return 2;      // 중지
        case 'reactivate':
            return 0;    // 정상으로 재개
        case 'deactivate':
            return 4;    // 탈퇴
        default:
            return 1;              // 알 수 없음
    }
}

// 예시 상태 (여기서도 실제 상태 값으로 테스트해야 함)
const currentStatus = 2;

// 상태 액션 및 라벨 출력
const action = getStatusAction(currentStatus);
const label = getStatusLabel(currentStatus);

console.log(`Action: ${action}, Label: ${label}`); // Action: suspend, Label: 중지
