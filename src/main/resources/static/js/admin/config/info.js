/*
    날짜 : 2024/10/22
    이름 : 최준혁
    내용 : info js 파일 생성
*/
document.addEventListener('DOMContentLoaded', function() {
    // PATCH 요청을 보내는 함수 (사이트 정보 수정)
    function patchSiteInfo() {
        const title = document.getElementById('title').value;
        const subTitle = document.getElementById('sub_title').value;

        // fetch를 이용하여 PATCH 요청 보내기
        fetch('/admin/config/updateSite', {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                title: title,
                sub_title: subTitle
            })
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('사이트 정보가 성공적으로 수정되었습니다.');
                    disableSiteEditing('site_button', ['title', 'sub_title'], 'apply_site_button')
                } else {
                    alert('사이트 정보 수정에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('사이트 정보 수정 중 오류가 발생했습니다.');
            });
    }

    // PATCH 요청을 보내는 함수 (회사 정보 수정)
    function patchComInfo() {
        const b_name = document.getElementById('b_name').value;
        const ceo = document.getElementById('ceo').value;
        const b_num = document.getElementById('b_num').value;
        const b_report = document.getElementById('b_report').value;
        const addr1 = document.getElementById('addr1').value;
        const addr2 = document.getElementById('addr2').value;


        // fetch를 이용하여 PATCH 요청 보내기
        fetch('/admin/config/updateCom', {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                b_name: b_name,
                ceo: ceo,
                b_num: b_num,
                b_report: b_report,
                addr1: addr1,
                addr2: addr2
            })
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('회사 정보가 성공적으로 수정되었습니다.');
                    disableSiteEditing('com_button', ['b_name', 'ceo','b_num', 'b_report', 'addr1' , 'addr2'], 'apply_com_button')
                } else {
                    alert('회사 정보 수정에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('회사 정보 수정 중 오류가 발생했습니다.');
            });
    }

    // PATCH 요청을 보내는 함수 (회사 정보 수정)
    function patchCsInfo() {
        const cs_num = document.getElementById('cs_num').value;
        const cs_time = document.getElementById('cs_time').value;
        const cs_email = document.getElementById('cs_email').value;
        const dispute = document.getElementById('dispute').value;

        // fetch를 이용하여 PATCH 요청 보내기
        fetch('/admin/config/updateCs', {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                cs_num: cs_num,
                cs_time: cs_time,
                cs_email: cs_email,
                dispute: dispute,

            })
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('고객센터 정보가 성공적으로 수정되었습니다.');
                    disableSiteEditing('cs_button', ['cs_num', 'cs_time','cs_email', 'dispute'], 'apply_cs_button')
                } else {
                    alert('고객센터 정보 수정에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('고객센터 정보 수정 중 오류가 발생했습니다.');
            });
    }

    // PATCH 요청을 보내는 함수 (카피라이트 정보 수정)
    function patchCopyInfo() {
        const copyright = document.getElementById('copyright').value;

        // fetch를 이용하여 PATCH 요청 보내기
        fetch('/admin/config/updateCopy', {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                copyright: copyright,
            })
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('카피라이트 정보가 성공적으로 수정되었습니다.');
                    disableSiteEditing('copy_button', ['copyright'], 'apply_copy_button')
                } else {
                    alert('카피라이트 정보 수정에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('카피라이트 정보 수정 중 오류가 발생했습니다.');
            });
    }

    // 파일 업로드 함수
    function uploadLogos() {
        const formData = new FormData(document.getElementById('logoForm'));

        fetch('/admin/config/uploadLogo', {
            method: 'POST',
            body: formData
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('로고가 성공적으로 업로드되었습니다.');
                    location.reload(); // 페이지 새로 고침
                } else {
                    alert('로고 업로드에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('로고 업로드 중 오류가 발생했습니다.');
            });
    }

    // 파일 이름을 업데이트하는 함수
    function updateFileName(input, spanId) {
        const fileName = input.files.length > 0 ? input.files[0].name : "선택된 파일 없음";
        document.getElementById(spanId).textContent = fileName;
    }

    // '적용하기' 버튼 클릭 이벤트 등록 (사이트 정보 수정)
    document.getElementById('apply_site_button').addEventListener('click', patchSiteInfo);

    // '적용하기' 버튼 클릭 이벤트 등록 (회사 정보 수정)
    document.getElementById('apply_com_button').addEventListener('click', patchComInfo);

    // '적용하기' 버튼 클릭 이벤트 등록 (고객센터 정보 수정)
    document.getElementById('apply_cs_button').addEventListener('click', patchCsInfo);

    // '적용하기' 버튼 클릭 이벤트 등록 (카피라이트 정보 수정)
    document.getElementById('apply_copy_button').addEventListener('click', patchCopyInfo);

    // '업로드' 버튼 클릭 이벤트 등록 (로고 업로드)
    document.getElementById('upload_button').addEventListener('click', uploadLogos);

    // 파일 입력 요소에 change 이벤트 등록
    const fileInput = document.getElementById('logoInput'); // 파일 입력 요소 ID를 사용하세요.
    fileInput.addEventListener('change', function() {
        updateFileName(fileInput, 'fileNameDisplay'); // 'fileNameDisplay'는 파일 이름을 표시할 span ID입니다.
    });
});

// 사이트 정보 수정 버튼 클릭 시 입력 필드를 활성화하는 함수
function enableSiteEditing(buttonId, inputIds, applyButtonId) {
    const editButton = document.getElementById(buttonId);
    const uploadButton = document.getElementById(applyButtonId);

    editButton.style.display = 'none'; // '수정하기' 버튼 숨기기
    uploadButton.style.display = 'inline-block'; // '적용하기' 버튼 표시

    // 각 입력 필드를 활성화 (readonly 해제)
    inputIds.forEach(function(inputId) {
        document.getElementById(inputId).removeAttribute('readonly');
    });
}

// 사이트 정보 수정 취소 시 입력 필드를 비활성화하는 함수
function disableSiteEditing(buttonId, inputIds, applyButtonId) {
    const editButton = document.getElementById(buttonId);
    const uploadButton = document.getElementById(applyButtonId);

    uploadButton.style.display = 'none'; // '적용하기' 버튼 숨기기
    editButton.style.display = 'inline-block'; // '수정하기' 버튼 표시

    // 각 입력 필드를 비활성화 (readonly 설정)
    inputIds.forEach(function(inputId) {
        document.getElementById(inputId).setAttribute('readonly', 'readonly');
    });
}


// 로고 수정 버튼 클릭 시 입력 필드를 활성화하는 함수
function enableLogoEditing(buttonId, inputIds) {
    const editButton = document.getElementById(buttonId);
    const uploadButton = document.getElementById('upload_button');

    editButton.style.display = 'none'; // '수정하기' 버튼 숨기기
    uploadButton.style.display = 'inline-block'; // '업로드' 버튼 표시

    // 각 입력 필드를 활성화 (readonly 해제)
    inputIds.forEach(function(inputId) {
        document.getElementById(inputId).removeAttribute('readonly');
    });
}
