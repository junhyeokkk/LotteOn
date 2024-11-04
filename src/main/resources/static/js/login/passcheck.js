/*
    날짜 : 2024/11/04
    이름 : 이도영(최초 작성자)
    내용 : 비밀번호 검사 js
*/
window.onload = function () {
    // 유효성 검사에 사용할 상태변수
    let isPassOk = false;
    const registerForm = document.querySelector('.sendForm');
    const auth = document.querySelector('.auth');
    const resultPass = document.querySelector('.resultPass');
    // 유효성 검사에 사용할 정규표현식
    const patterns = {
        pass: /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{5,16}$/
    };
    // 함수: 유효성 검사 결과 표시
    const showResult = (element, message, isValid) => {
        element.innerText = message;
        element.style.color = isValid ? 'green' : 'red';
    };

    // 비밀번호 유효성 검사
    const passInput = document.getElementsByName('pass')[0];
    const pass2Input = document.getElementsByName('pass2')[0];
    pass2Input.addEventListener('focusout', function () {
        if (!passInput.value.match(patterns.pass)) {
            showResult(resultPass, "비밀번호가 유효하지 않습니다.", false);
            isPassOk = false;
        } else if (passInput.value === pass2Input.value) {
            showResult(resultPass, "비밀번호가 일치합니다.", true);
            isPassOk = true;
        } else {
            showResult(resultPass, "비밀번호가 일치하지 않습니다.", false);
            isPassOk = false;
        }
    });
    // 최종 폼 전송 유효성 검사
    registerForm.onsubmit = function (e) {
        // 비밀번호 유효성 검사 완료 여부
        if (!isPassOk) {
            alert('비밀번호가 유효하지 않습니다.');
            e.preventDefault(); // 폼 전송 취소
            return false;
        }
        return true;
    }
}