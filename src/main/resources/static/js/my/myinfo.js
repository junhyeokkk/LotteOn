document.addEventListener("DOMContentLoaded", function () {
    // 함수: 유효성 검사 결과 표시
    const showResult = (element, message, isValid) => {
        element.innerText = message;
        element.style.color = isValid ? 'green' : 'red';
    };
    // 비밀번호 수정 관련 함수
    const togglePasswordEditButton = document.getElementById("togglePasswordEditButton");
    if (togglePasswordEditButton) {
        togglePasswordEditButton.addEventListener("click", togglePasswordEdit);
    }
    function togglePasswordEdit() {
        // 비밀번호 수정 버튼 클릭 시 passwordRow를 숨기고 passwordEditRow를 표시
        document.getElementById("passwordRow").style.display = "none";
        document.getElementById("passwordEditRow").style.display = "table-row";
    }

    // 취소 버튼에 대한 이벤트 리스너 추가
    const submitPasswordChangeButton = document.getElementById("submitPasswordChangeButton");
    if (submitPasswordChangeButton) {
        submitPasswordChangeButton.addEventListener("click", submitPasswordChange);
    }
    function cancelPasswordEdit() {
        // 취소 버튼 클릭 시 passwordEditRow를 숨기고 passwordRow를 다시 표시
        document.getElementById("passwordEditRow").style.display = "none";
        document.getElementById("passwordRow").style.display = "table-row";
        // 입력 필드 초기화
        document.getElementById("newPassword").value = "";
        document.getElementById("confirmPassword").value = "";
    }
    // 비밀번호 수정 확인 버튼 이벤트 리스너 추가
    const cancelPasswordEditButton = document.getElementById("cancelPasswordEditButton");
    if (cancelPasswordEditButton) {
        cancelPasswordEditButton.addEventListener("click", cancelPasswordEdit);
    }
    function submitPasswordChange() {
        event.preventDefault();
        const newPassword = document.getElementById("newPassword").value;
        const confirmPassword = document.getElementById("confirmPassword").value;
        const uid = document.getElementById("uid").value;
        console.log(uid);
        // 비밀번호가 입력되지 않았을 때 경고 표시
        if (!newPassword || !confirmPassword) {
            alert("비밀번호를 입력해 주세요.");
            return;
        }
        if (newPassword !== confirmPassword) {
            alert("비밀번호가 일치하지 않습니다.");
            return;
        }

        const data = {
            newPassword: newPassword
        };
        // AJAX request to update password
        fetch(`/api/admin/member/changepassword/${uid}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        })
            .then(response => response.ok ? response.text() : Promise.reject("비밀번호 변경 실패"))
            .then(message => {
                alert(message);
                cancelPasswordEdit();  // 비밀번호 수정 필드 숨기기
            })
            .catch(error => {
                console.error("에러:", error);
                alert("비밀번호 변경 중 오류가 발생했습니다.");
            });
    }
    // 이메일 수정 관련
    const toggleEmailEditButton = document.getElementById("toggleEmailEditButton");
    if (toggleEmailEditButton) {
        toggleEmailEditButton.addEventListener("click", function () { toggleEmailEdit(this); });
    }
    function toggleEmailEdit(button) {
        const emailLocalPart = document.getElementById("emailLocalPart");
        const emailDomain = document.getElementById("emailDomain");
        const domainSelect = document.getElementById("domainSelect");
        const emailHidden = document.querySelector("input[name='email']");
        const btnSendEmail = document.getElementById("btnSendEmail"); // 인증번호 받기 버튼
        if (emailLocalPart.hasAttribute("readonly")) {
            // 수정 가능 상태로 변경
            emailLocalPart.removeAttribute("readonly");
            emailDomain.removeAttribute("readonly");
            domainSelect.removeAttribute("disabled");
            btnSendEmail.style.display = "inline";
            // 버튼 색상 및 텍스트 변경
            button.classList.add("edit-mode");
            button.textContent = "수정 완료";
        } else {
            // 다시 읽기 전용 상태로 변경
            emailLocalPart.setAttribute("readonly", "readonly");
            emailDomain.setAttribute("readonly", "readonly");
            domainSelect.setAttribute("disabled", "disabled");
            const formattedEmail = `${emailLocalPart.value}@${emailDomain.value}`;
            emailHidden.value = formattedEmail;
            btnSendEmail.style.display = "none";
            // 버튼 색상 및 텍스트 원래대로 변경
            button.classList.remove("edit-mode");
            button.textContent = "수정하기";
        }
    }

    document.getElementById("domainSelect").addEventListener("change", function() {
        const domainSelect = document.getElementById("domainSelect");
        const emailDomain = document.getElementById("emailDomain");

        if (domainSelect.value === "직접 입력") {
            // 사용자가 직접 입력하도록 설정
            emailDomain.value = "";
            emailDomain.removeAttribute("readonly");
        } else {
            // 선택된 옵션 값으로 emailDomain 설정 및 읽기 전용 처리
            emailDomain.value = domainSelect.value;
            emailDomain.setAttribute("readonly", "readonly");
        }
    });
    // 전화번호 수정 버튼 이벤트 리스너
    const togglePhoneEditButton = document.getElementById("togglePhoneEditButton");
    if (togglePhoneEditButton) {
        togglePhoneEditButton.addEventListener("click", function () { togglePhoneEdit(this); });
    }
    function togglePhoneEdit(button) {
        const phone1 = document.getElementById("phone1");
        const phone2 = document.getElementById("phone2");
        const phone3 = document.getElementById("phone3");
        const phHidden = document.querySelector("input[name='ph']");
        if (phone1.hasAttribute("readonly")) {
            // 수정 가능 상태로 변경
            phone1.removeAttribute("readonly");
            phone2.removeAttribute("readonly");
            phone3.removeAttribute("readonly");

            // 버튼 색상 및 텍스트 변경
            button.classList.add("edit-mode");
            button.textContent = "수정 완료";
        } else {
            // 다시 읽기 전용 상태로 변경
            phone1.setAttribute("readonly", "readonly");
            phone2.setAttribute("readonly", "readonly");
            phone3.setAttribute("readonly", "readonly");
            const formattedPhone = `${phone1.value}-${phone2.value}-${phone3.value}`;
            phHidden.value = formattedPhone;
            // 버튼 색상 및 텍스트 원래대로 변경
            button.classList.remove("edit-mode");
            button.textContent = "수정하기";
        }
    }
    // 이메일 유효성 검사 및 인증코드 발송
    const btnSendEmail = document.getElementById('btnSendEmail');
    const btnAuthEmail = document.getElementById('btnAuthEmail');
    const authInput = document.querySelector('.userauth'); // 인증 코드 입력 필드
    const auth = document.querySelector('.auth');
    let isEmailOk = false;
    const resultEmail = document.getElementById('resultEmail');
    // 유효성 검사에 사용할 정규표현식
    const patterns = {
        email: /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i,
    };
    btnSendEmail.addEventListener('click', async function (e) {
        e.preventDefault(); // 기본 제출 동작 막기
        const emailLocalPart = document.getElementById("emailLocalPart").value;
        const emailDomain = document.getElementById("emailDomain").value;
        const value = emailLocalPart + "@"+ emailDomain;
        if (!value.match(patterns.email)) {
            showResult(resultEmail, '이메일 형식이 맞지 않습니다.', false);
            isEmailOk = false;
            return;
        }
        const data = await fetchGet(`/user/Register/email/${value}`);
        if (data.result) {
            showResult(resultEmail, '이미 사용중인 이메일 입니다.', false);
            isEmailOk = false;
        } else {
            showResult(resultEmail, '인증코드가 발송되었습니다.', true);
            auth.style.display = 'block';  // 인증 필드 활성화
            isEmailOk = true;
        }
    });

    // 이메일 인증코드 확인
    btnAuthEmail.addEventListener('click', async function (e) {
        e.preventDefault();
        const code = authInput.value;  // 사용자가 입력한 인증 코드
        const jsonData = {"code": code};
        const data = await fetchPost(`/user/Register/email`, jsonData);

        if (!data.result) {
            showResult(resultEmail, '인증코드가 일치하지 않습니다.', false);
            isEmailVerified = false;
        } else {
            showResult(resultEmail, '이메일이 인증되었습니다.', true);
            isEmailVerified = true;
        }
    });
    document.getElementById("contactInfoForm").addEventListener("submit", function(event) {
        event.preventDefault(); // 기본 폼 제출 방지
        const confirmation = confirm("정보를 수정 하시겠습니까?.");
        if (!confirmation) {
            return;
        }
        const uid = document.getElementById("uid").value; // uid 값 가져오기
        const emailLocalPart = document.getElementById("emailLocalPart").value;
        const emailDomain = document.getElementById("emailDomain").value;
        const phone1 = document.getElementById("phone1").value;
        const phone2 = document.getElementById("phone2").value;
        const phone3 = document.getElementById("phone3").value;
        const zip = document.getElementById("zip").value;
        const address1 = document.getElementById("address1").value;
        const address2 = document.getElementById("address2").value;
        let emailset = null;
        if (isEmailOk) {
            emailset = emailLocalPart + "@" + emailDomain;
        }
        // DTO 형식에 맞게 데이터 준비
        const memberDTO = {
            email: emailset,
            ph: `${phone1}-${phone2}-${phone3}`,
            zip: zip,
            addr1: address1,
            addr2: address2
        };

        fetch(`/api/admin/member/${uid}`, { // PUT 엔드포인트에 uid 포함
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(memberDTO)
        })
            .then(response => response.ok ? response.json() : Promise.reject("회원 정보 업데이트 실패"))
            .then(updatedMember => {
                alert("회원 정보가 성공적으로 업데이트되었습니다.");
                window.location.href = "/myPage/info";
            })
            .catch(error => {
                console.error("에러:", error);
                alert("회원 정보 업데이트 중 오류가 발생했습니다.");
            });
    });
    // 탈퇴하기 버튼
    const withdrawalLink = document.getElementById("withdrawalLink");
    if (withdrawalLink) {
        withdrawalLink.addEventListener("click", function (event) {
            event.preventDefault(); // 기본 링크 동작 방지
            handleWithdrawal();
        });
    }
    function handleWithdrawal() {
        const confirmation = confirm("정말로 탈퇴하시겠습니까? 탈퇴 시 모든 정보가 삭제됩니다.");

        if (confirmation) {
            const uid = document.getElementById("uid").value; // 사용자 ID

            fetch(`/myPage/info/delete/${uid}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                }
            })
                .then(response => {
                    if (response.ok) {
                        alert("회원 탈퇴가 성공적으로 처리되었습니다. 이용해 주셔서 감사합니다");
                        window.location.href = "/member/logout"; // 탈퇴 후 메인 페이지로 이동
                    } else {
                        alert("탈퇴 처리에 실패했습니다. 다시 시도해 주세요.");
                    }
                })
                .catch(error => {
                    console.error("에러 발생:", error);
                    alert("탈퇴 중 오류가 발생했습니다.");
                });
        }
    }
});