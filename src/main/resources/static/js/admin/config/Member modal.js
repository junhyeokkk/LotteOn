function openUserModal(element) {
    const userId = element.getAttribute("data-user-id"); // 클릭한 버튼의 data-user-id 속성에서 ID 가져오기

    // 서버에서 회원 정보 가져오기
    fetch(`/api/members/${userId}`)
        .then(response => response.json())
        .then(data => {
            // 회원 정보를 모달 창의 각 필드에 채우기
            document.getElementById("uid").value = data.uid;
            document.getElementById("name").value = data.name;
            document.getElementById("email").value = data.email;
            document.getElementById("phone").value = data.phone;

            // 성별 체크박스 선택
            if (data.gender === 'MALE') {
                document.getElementById("genderMale").checked = true;
            } else if (data.gender === 'FEMALE') {
                document.getElementById("genderFemale").checked = true;
            }

            // 모달 창 표시
            document.getElementById("userchangemodal").style.display = "block";
        })
        .catch(error => console.error('회원 정보를 가져오는 데 실패했습니다:', error));
}

function closeModal() {
    document.getElementById("userchangemodal").style.display = "none";
}
