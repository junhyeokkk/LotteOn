
// 유효성 검사에 사용할 정규표현식
const reUid = /^[a-z]+[a-z0-9]{4,19}$/g;
const rePass = /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{5,16}$/;
const reName = /^[가-힣]{2,10}$/
const reEmail = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
const reHp = /^01(?:0|1|[6-9])-(?:\d{4})-\d{4}$/;

// 유효성 검사에 사용할 상태변수
let isUidOk = false;
let isPassOk = false;
let isNameOk = false;
let isEmailOk = false;
let isHpOk = false;
let isGenderOk = false;
let isAddressOk = false;

window.onload=function () {

	const btnCheckUid = document.getElementById('btnCheckUid');
	const registerForm = document.getElementsByClassName('sendForm')[0];


	const userUidInput = document.querySelector('input[name="uid"]');
	const genderInputs = document.querySelectorAll('input[name="gender"]');
	const resultId = document.querySelector('.resultId');

	const passInput = document.getElementsByName('pass')[0];
	const pass2Input = document.getElementsByName('pass2')[0];
	const Name = document.getElementsByName('name')[0];
	const resultPass = document.getElementsByClassName('resultPass')[0];
	const resultName = document.getElementsByClassName('resultName')[0];
	const resultHp = document.getElementsByClassName('resultHp')[0];


	// 아이디 유효성 검사
	btnCheckUid.onclick = async function (e) {

		const type = btnCheckUid.dataset.type; // data-type 속성 값
		const value = userUidInput.value; // userUid 입력 값

		console.log(type + value);

		// 아이디 유효성 검사
		if (!value.match(reUid)) {
			resultId.innerText = '아이디가 유효하지 않습니다.';
			resultId.style.color = 'red';
			return;
		}

		const data = await fetchGet(`/user/Register/${type}/${value}`)
		console.log(data.result)

		if (data.result > 0) {
			resultId.innerText ='이미 사용중인 아이디입니다.';
			resultId.style.color = 'red';
			isUidOk = false;
		} else {
			resultId.innerText ='사용 가능한 아이디입니다.';
			resultId.style.color = 'green';
			isUidOk = true;
		}
	}


	// 비밀번호 유효성 검사
	pass2Input.addEventListener('focusout', function(){

		const pass = passInput.value;
		const pass2 = pass2Input.value;

		if(!pass.match(rePass)){
			resultPass.innerText = "비밀번호가 유효하지 않습니다.";
			resultPass.style.color = 'red';
			return;
		}

		if(pass == pass2){
			resultPass.innerText = "비밀번호가 일치합니다.";
			resultPass.style.color = 'green';
			isPassOk = true;
		}else{
			resultPass.innerText = "비밀번호가 일치하지 않습니다.";
			resultPass.style.color = 'red';
			isPassOk = false;
		}
	});

	// 이름 유효성 검사
	Name.addEventListener('focusout', function(){

		const name = Name.value;

		if(!name.match(reName)){
			resultName.innerText = "이름이 유효하지 않습니다.";
			resultName.style.color = 'red';
			isNameOk = false;
		}else{
			resultName.innerText = "";
			isNameOk = true;
		}
	});

	// 이메일 유효성 검사

	const btnSendEmail = document.querySelector('#btnSendEmail');
	const EmailInput = document.querySelector('input[name="email"]');
	const btnAuthEmail = document.getElementById('btnAuthEmail');

	const resultEmail = document.getElementById('resultEmail');
	const auth = document.getElementsByClassName('auth')[0];

	btnSendEmail.addEventListener('click', async function (){

		const type = btnSendEmail.dataset.type;
		const value = EmailInput.value;

		if (!value.match(reEmail)) {
			showResultInvalid(resultEmail, '이메일 형식이 맞지 않습니다.');
			isEmailOk = false;
			return;
		}

		const data = await fetchGet(`/user/Register/${type}/${value}`);

		if(data.result > 0){
			showResultInvalid(resultEmail, '이미 사용중인 이메일 입니다.');
			isEmailOk = false;
		}else{
			showResultValid(resultEmail, '인증코드가 발송 되었습니다.');
			// 인증코드 입력 필드 활성화
				auth.style.display = 'block';

			isEmailOk = false;
		}

	})

	genderInputs.forEach(function(input) {
		input.addEventListener('change', function() {
			isGenderOk = true; // 성별이 선택되었을 때 true로 설정
		});
	});

	// 이메일 인증코드 확인
	btnAuthEmail.onclick = async function (){
		const userauth = document.querySelector('.userauth');

		const jsonData = {"code": userauth.value};

		const data = await fetchPost(`/user/Register/email`, jsonData);

		console.log(data.result);

		if(!data.result){
			showResultInvalid(resultEmail, '인증코드가 일치하지 않습니다.');
			isEmailOk = false;
		}else{
			showResultValid(resultEmail, '이메일이 인증되었습니다.');
			isEmailOk = true;
		}
	}

	// 휴대폰 유효성 검사
	const phnumber = document.getElementById('ph');
	phnumber.addEventListener('focusout', async function(){

		const value = phnumber.value;
		const type = 'ph'

		if(!value.match(reHp)){
			resultHp.innerText = '전화번호가 유효하지 않습니다.';
			resultHp.style.color = 'red';
			return;
		}

		const data = await fetchGet(`/user/Register/${type}/${value}`);
		console.log(data.result)

		if(data.result > 0){
			resultHp.innerText = '이미 사용중인 휴대폰번호 입니다.';
			resultHp.style.color = 'red';
			isHpOk = false;
		}else{
			resultHp.innerText = '사용할 수 있는 번호입니다.';
			resultHp.style.color = 'green';
			isHpOk = true;
		}
	});
	// 주소 유효성 검사 추가
	const address1Input = document.getElementById('address1');
	const address2Input = document.getElementById('address2');
	const resultaddr = document.querySelector('.resultaddr');
	address2Input.addEventListener('focusout', function() {
		const address1 = address1Input.value;
		const address2 = address2Input.value;

		if (address1 !== '' && address2 !== '') {
			resultaddr.innerText = "";
			isAddressOk = true;
		} else {
			resultaddr.innerText = '주소를 입력해주세요';
			resultaddr.style.color = 'red';
			isAddressOk = false;
		}
	});

	//최종 폼 전송 유효성 검사
	registerForm.onsubmit = function () {

		// 아이디 유효성 검사 완료 여부
		if (!isUidOk) {
			alert('아이디가 유효하지 않습니다.');
			return false; // 폼 전송 취소
		}

		// 비밀번호 유효성 검사 완료 여부
		if (!isPassOk) {
			alert('비밀번호가 유효하지 않습니다.');
			return false; // 폼 전송 취소
		}

		// 이름 유효성 검사 완료 여부
		if (!isNameOk) {
			alert('이름이 유효하지 않습니다.');
			return false; // 폼 전송 취소
		}
		// 성별 유효성 검사 완료 여부 (필수 추가)
		if (!isGenderOk) {
			alert('성별을 선택해주세요.');
			return false; // 폼 전송 취소
		}
		// 이메일 유효성 검사 완료 여부
		if (!isEmailOk) {
			alert('이메일이 유효하지 않습니다.');
			return false; // 폼 전송 취소
		}

		// 휴대폰 유효성 검사 완료 여부
		if (!isHpOk) {
			alert('휴대폰 번호가 유효하지 않습니다.');
			return false; // 폼 전송 취소
		}

		// 주소 유효성 검사 완료 여부
		if (!isAddressOk) {
			alert('주소가 유효하지 않습니다.');
			return false; // 폼 전송 취소
		}
		return true;
	};


}
