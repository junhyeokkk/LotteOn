/*
     날짜 : 2024/10/25
     이름 : 강유정(최초 작성자)
     내용 : 셀러 유효성검사 생성

     수정사항
     - 2024/11/06 이도영 이메일 유효성 검사
     - 2024/11/08 이도영 통신판매업번호,전화번호,팩스번호 유효성 검사
*/

window.onload = function () {
	// 유효성 검사에 사용할 상태변수
	let isUidOk = false;
	let isPassOk = false;
	let isEmailVerified = false;
	let isShop_nameOk = false;
	let isRepresentativeOk = false;
	let isBusiness_registrationOk = false;
	let isE_commerce_registrationOk = false;
	let isPhOk = false;
	let isFaxOk = true;
	let isAddressOk = false;

	const registerForm = document.querySelector('.sendForm');
	const resultId = document.querySelector('.resultId');
	const resultPass = document.querySelector('.resultPass');
	const resultEmail = document.getElementById('resultEmail');
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
		email: /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i,
		ph: /^01(?:0|1|[6-9])-(?:\d{4})-\d{4}$/,
		business_registration: /^\d{3}-\d{2}-\d{5}$/
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
		const data = await fetchGet(`/user/Register/shopemail/${value}`);
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
	// 회사 이름 유효성 검사
	const Shop_nameInput = document.getElementsByName('shopName')[0];
	Shop_nameInput.addEventListener('focusout', async function () {
		const shop_name = Shop_nameInput.value;
		console.log(shop_name);
		if(!shop_name){
			showResult(resultShop_name, "회사명을 입력해주세요.", false);
			return;
		}
		const data = await fetchGet(`/user/Register/shop/${shop_name}`);
		if(data.result) {
			showResult(resultShop_name, "사용할수 없는 회사명 입니다", false);  // 메시지 지우기
			isShop_nameOk = false;
		}else{
			showResult(resultShop_name, "등록이 가능한 회사명 입니다",true);
			isShop_nameOk = true;
		}
	});

	// 대표 유효성 검사
	const NameInput = document.getElementsByName('representative')[0];
	NameInput.addEventListener('focusout', function () {
		const name = NameInput.value;
		if (!name) {
			showResult(resultRepresentative, "성함을 입력해주세요.", false);
			isRepresentativeOk = false;
		} else {
			resultRepresentative.innerText = "";  // 메시지 지우기
			isRepresentativeOk = true;
		}
	});
	// 사업자등록번호 유효성 검사
	const business_registrationPart1 = document.getElementById('business_registration_part1');
	const business_registrationPart2 = document.getElementById('business_registration_part2');
	const business_registrationPart3 = document.getElementById('business_registration_part3');
	const business_registrationHidden = document.getElementsByName('businessRegistration')[0];

	// 숫자만 입력 가능하고 최대치에 도달하면 다음 필드로 이동하도록 이벤트 추가
	[business_registrationPart1, business_registrationPart2, business_registrationPart3].forEach((input, index, array) => {
		input.addEventListener('input', function () {
			// 숫자가 아닌 문자는 제거
			input.value = input.value.replace(/[^0-9]/g, '');

			// 최대 길이에 도달하면 다음 필드로 포커스 이동
			if (input.value.length === input.maxLength) {
				const nextInput = array[index + 1];
				if (nextInput) {
					nextInput.focus();
				}
			}
		});
	});

	// 세 번째 필드에서 포커스 아웃 시 유효성 검사 실행
	business_registrationPart3.addEventListener('focusout', validateBusinessRegistration);

	// 유효성 검사 함수
	async function validateBusinessRegistration() {
		const part1 = business_registrationPart1.value;
		const part2 = business_registrationPart2.value;
		const part3 = business_registrationPart3.value;

		// 유효성 검사 (각각의 부분이 올바른 형식인지 확인)
		if (!part1.match(/^\d{3}$/) || !part2.match(/^\d{2}$/) || !part3.match(/^\d{5}$/)) {
			showResult(resultBusiness_registration, "사업자등록번호를 정확히 입력해주세요", false);
			isBusiness_registrationOk = false;
			return;
		}

		// 전체 사업자등록번호 조합
		const fullBusinessRegistration = `${part1}-${part2}-${part3}`;
		business_registrationHidden.value = fullBusinessRegistration; // 히든 필드에 전체 값 설정

		// 서버에 중복 확인 요청
		const data = await fetchGet(`/user/Register/businessregistration/${fullBusinessRegistration}`);

		if (data && data.result) {
			showResult(resultBusiness_registration, "사용할 수 없는 사업자등록번호입니다", false);
			isBusiness_registrationOk = false;
		} else {
			showResult(resultBusiness_registration, "등록이 가능한 사업자등록번호입니다", true);
			isBusiness_registrationOk = true;
		}
	}
	// 통신판매업번호 유효성 검사
	const e_commerce_registrationPart1 = document.getElementById('e_commerce_registration_part1');
	const e_commerce_registrationPart2 = document.getElementById('e_commerce_registration_part2');
	const e_commerce_registrationPart3 = document.getElementById('e_commerce_registration_part3');
	const e_commerce_registrationHidden = document.getElementsByName('eCommerceRegistration')[0];

	// 숫자만 입력 가능하고 최대치에 도달하면 다음 필드로 이동 (각 순서를 명확히 설정)
	e_commerce_registrationPart1.addEventListener('input', function () {
		// 숫자가 아닌 문자는 제거
		e_commerce_registrationPart1.value = e_commerce_registrationPart1.value.replace(/[^0-9]/g, '');

		// 최대 길이에 도달하면 두 번째 필드로 포커스 이동
		if (e_commerce_registrationPart1.value.length === e_commerce_registrationPart1.maxLength) {
			e_commerce_registrationPart2.focus();
		}
	});
	e_commerce_registrationPart2.addEventListener('input', function () {
		// 문자 이외의 문자는 제거
		e_commerce_registrationPart2.value = e_commerce_registrationPart2.value.replace(/[^a-zA-Z가-힣]/g, '');

		// 최대 길이에 도달하면 세 번째 필드로 포커스 이동
		if (e_commerce_registrationPart2.value.length === e_commerce_registrationPart2.maxLength) {
			e_commerce_registrationPart3.focus();
		}
	});

	e_commerce_registrationPart3.addEventListener('input', function () {
		// 숫자가 아닌 문자는 제거
		e_commerce_registrationPart3.value = e_commerce_registrationPart3.value.replace(/[^0-9]/g, '');
	});

	// 세 번째 필드에서 포커스 아웃 시 유효성 검사 실행
	e_commerce_registrationPart3.addEventListener('focusout', validateECommerceRegistration);

	// 유효성 검사 함수
	async function validateECommerceRegistration() {
		const part1 = e_commerce_registrationPart1.value;
		const part2 = e_commerce_registrationPart2.value;
		const part3 = e_commerce_registrationPart3.value;

		// 유효성 검사 (각각의 부분이 올바른 형식인지 확인)
		if (!part1.match(/^\d{4}$/) || !part2.match(/^[a-zA-Z가-힣]{1,5}$/) || !part3.match(/^\d{4}$/)) {
			showResult(resultE_commerce_registration, "정확히 입력해주세요", false);
			isE_commerce_registrationOk = false;
			return;
		}

		// 전체 통신판매업번호 조합
		const fullECommerceRegistration = `${part1}-${part2}-${part3}`;
		e_commerce_registrationHidden.value = fullECommerceRegistration; // 히든 필드에 전체 값 설정

		// 서버에 중복 확인 요청
		const data = await fetchGet(`/user/Register/e_commerce_registration/${fullECommerceRegistration}`);

		if (data && data.result) {
			showResult(resultE_commerce_registration, "사용할 수 없는 번호입니다", false);
			isE_commerce_registrationOk = false;
		} else {
			showResult(resultE_commerce_registration, "등록이 가능한 번호입니다", true);
			isE_commerce_registrationOk = true;
		}
	}

	// 휴대폰 번호 유효성 검사
	const phInput = document.getElementsByName('ph')[0];
	phInput.addEventListener('focusout', async function () {
		const value = phInput.value;
		if (!value.match(patterns.hp)) {
			showResult(resultPh, '전화번호가 유효하지 않습니다.', false);
			isPhOk = false;
			return;
		}
		const data = await fetchGet(`/user/Register/ph/${value}`);
		if (data.result) {
			showResult(resultPh, '이미 사용중인 휴대폰번호입니다.', false);
			isPhOk = false;
		} else {
			showResult(resultPh, '사용할 수 있는 번호입니다.', true);
			isPhOk = true;
		}
	});
	const faxInput = document.getElementsByName('fax')[0];
	faxInput.addEventListener('focusout', async function () {
		const value = faxInput.value;
		const data = await fetchGet(`/user/Register/fax/${value}`);
		if (data.result) {
			showResult(resultFax, '이미 사용중인 FAX 번호입니다.', false);
			isFaxOk = false;
		} else {
			showResult(resultFax, "", true);
			isFaxOk = true;
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

	//최종 폼 전송 유효성 검사
	registerForm.onsubmit = function (e) {
		alert('here!');

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
		// 이메일 유효성 검사 완료 여부
		if (!isEmailOk || !isEmailVerified) {
			alert('이메일이 인증되지 않았습니다.');
			e.preventDefault(); // 폼 전송 취소
			return false;
		}

		// 회사명 유효성 검사 완료 여부
		if (!isShop_nameOk) {
			alert('회사명이 유효하지 않습니다.');
			e.preventDefault(); // 폼 전송 취소
			return false;
		}
		// 대표 유효성 검사 완료 여부
		if(!isRepresentativeOk){
			alert('대표님의 성함이 유효하지 않습니다.');
			e.preventDefault();
			return false;
		}
		// 사업자 등록 번호 검사 완료 여부
		if(!isBusiness_registrationOk){
			alert('사업자 등록 번호가 유효하지 않습니다.')
			e.preventDefault();
			return false;
		}
		// 통신판매업번호 검사 완료 여부
		if(!isE_commerce_registrationOk){
			alert('통신판매업번호 번호가 유효하지 않습니다.')
			e.preventDefault();
			return false;
		}

		// 휴대폰 유효성 검사 완료 여부
		if (!isPhOk) {
			alert('휴대폰 번호가 유효하지 않습니다.');
			e.preventDefault(); // 폼 전송 취소
			return false;
		}
		// 휴대폰 유효성 검사 완료 여부
		if (!isFaxOk) {
			alert('FAX 번호가 유효하지 않습니다.');
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