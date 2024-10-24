window.onload = function () {
	// 유효성 검사에 사용할 상태변수
	let isUidOk = false;
	let isPassOk = false;

	let isShop_nameOk = false;
	let isRepresentativeOk = false;
	let isBusiness_registrationOk = false;
	let isE_commerce_registrationOk = false;
	let isPhOk = false;
	let isFaxOk = false;
	let isAddressOk = false;

	const registerForm = document.querySelector('.sendForm');
	const resultId = document.querySelector('.resultId');
	const resultPass = document.querySelector('.resultPass');

	const resultShop_name = document.querySelector('.resultShop_name');
	const resultRepresentative = document.querySelector('.resultRepresentative');
	const resultBusiness_registration = document.querySelector('.resultBusiness_registration');
	const resultE_commerce_registration = document.querySelector('.resultE_commerce_registration');
	const resultPh = document.querySelector('.resultPh');
	const resultFax = document.querySelector('.resultFax');

	const auth = document.querySelector('.auth');
	const resultaddr = document.querySelector('.resultaddr');

	// 유효성 검사에 사용할 정규표현식
	const patterns = {
		uid: /^[a-z]+[a-z0-9]{4,19}$/g,
		pass: /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{5,16}$/,
		shop_name: /^[가-힣]{2,10}$/,
		ph: /^01(?:0|1|[6-9])-(?:\d{4})-\d{4}$/
	};

	// 함수: 유효성 검사 결과 표시
	const showResult = (element, message, isValid) => {
		element.innerText = message;
		element.style.color = isValid ? 'green' : 'red';
	};

	// 아이디 유효성 검사
	document.getElementById('btnCheckUid').onclick = async function (e) {
		e.preventDefault(); // 기본 제출 동작 막기
		const userUidInput = document.querySelector('input[name="uid"]');
		const value = userUidInput.value;
		if (!value.match(patterns.uid)) {
			showResult(resultId, '아이디가 유효하지 않습니다.', false);
			return;
		}
		const data = await fetchGet(`/user/Register/uid/${value}`);
		if (data.result) {
			showResult(resultId, '이미 사용중인 아이디입니다.', false);
			isUidOk = false;
		} else {
			showResult(resultId, '사용 가능한 아이디입니다.', true);
			isUidOk = true;
		}
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

	// 회사 이름 유효성 검사
	const Shop_nameInput = document.getElementsByName('shop_name')[0];
	Shop_nameInput.addEventListener('focusout', function () {
		const shop_name = Shop_nameInput.value;
		if (!shop_name.match(patterns.shop_name)) {
			showResult(resultShop_name, "이름이 유효하지 않습니다.", false);
			isShop_nameOk = false;
		} else {
			resultShop_name.innerText = "";  // 메시지 지우기
			isShop_nameOk = true;
		}
	});

	// 이메일 유효성 검사 및 인증코드 발송
	const btnSendEmail = document.getElementById('btnSendEmail');
	const EmailInput = document.querySelector('input[name="email"]');
	const btnAuthEmail = document.getElementById('btnAuthEmail');
	const authInput = document.querySelector('.userauth'); // 인증 코드 입력 필드

	btnSendEmail.addEventListener('click', async function (e) {
		e.preventDefault(); // 기본 제출 동작 막기
		const value = EmailInput.value;
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

	// 휴대폰 번호 유효성 검사
	const phInput = document.getElementById('ph');
	phInput.addEventListener('focusout', async function () {
		const value = phInput.value;
		if (!value.match(patterns.hp)) {
			showResult(resultHp, '전화번호가 유효하지 않습니다.', false);
			isHpOk = false;
			return;
		}
		const data = await fetchGet(`/user/Register/ph/${value}`);
		if (data.result) {
			showResult(resultHp, '이미 사용중인 휴대폰번호입니다.', false);
			isHpOk = false;
		} else {
			showResult(resultHp, '사용할 수 있는 번호입니다.', true);
			isHpOk = true;
		}
	});

	// 주소 유효성 검사
	const address1Input = document.getElementById('address1');
	const address2Input = document.getElementById('address2');
	address2Input.addEventListener('focusout', function () {
		if (address1Input.value !== '' && address2Input.value !== '') {
			resultaddr.innerText = "";  // 메시지 지우기
			isAddressOk = true;
		} else {
			showResult(resultaddr, '주소를 입력해주세요.', false);
			isAddressOk = false;
		}
	});

	// 성별 유효성 검사
	const genderInputs = document.querySelectorAll('input[name="gender"]');
	genderInputs.forEach(input => {
		input.addEventListener('change', function () {
			isGenderOk = true;  // 성별이 선택되었을 때 true로 설정
		});
	});

	// 최종 폼 전송 유효성 검사
	registerForm.onsubmit = function (e) {
		// 아이디 유효성 검사 완료 여부
		if (!isUidOk) {
			alert('아이디가 유효하지 않습니다.');
			e.preventDefault(); // 폼 전송 취소
			return false;
		}

		// 비밀번호 유효성 검사 완료 여부
		if (!isPassOk) {
			alert('비밀번호가 유효하지 않습니다.');
			e.preventDefault(); // 폼 전송 취소
			return false;
		}

		// 이름 유효성 검사 완료 여부
		if (!isNameOk) {
			alert('이름이 유효하지 않습니다.');
			e.preventDefault(); // 폼 전송 취소
			return false;
		}

		// 성별 유효성 검사 완료 여부
		if (!isGenderOk) {
			alert('성별을 선택해주세요.');
			e.preventDefault(); // 폼 전송 취소
			return false;
		}

		// 이메일 유효성 검사 완료 여부
		if (!isEmailOk || !isEmailVerified) {
			alert('이메일이 인증되지 않았습니다.');
			e.preventDefault(); // 폼 전송 취소
			return false;
		}

		// 휴대폰 유효성 검사 완료 여부
		if (!isHpOk) {
			alert('휴대폰 번호가 유효하지 않습니다.');
			e.preventDefault(); // 폼 전송 취소
			return false;
		}

		// 주소 유효성 검사 완료 여부
		if (!isAddressOk) {
			alert('주소가 유효하지 않습니다.');
			e.preventDefault(); // 폼 전송 취소
			return false;
		}

		return true;
	}
}