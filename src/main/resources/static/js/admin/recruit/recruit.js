/*
    날짜 : 2024/11/04
    이름 : 강유정
    내용 : 채용 js 파일 생성
*/

$(document).ready(function () {

    // 폼 제출 시 데이터 전송
    document.getElementById('recruitForm').onsubmit = function(event) {
        event.preventDefault();

        // 폼 데이터를 객체로 변환
        const recruitData = {
            title: document.getElementById('title').value,
            position: document.getElementById('position').value,
            history: document.getElementById('history').value,
            type: document.getElementById('type').value,
            displayStartDate: document.getElementById('startDate').value,
            displayEndDate: document.getElementById('endDate').value,
            etc: document.getElementById('etc').value
        };

        // 서버로 POST 요청
        fetch("/admin/cs/recruit/list", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(recruitData)
        })
            .then(response => {
                if (response.ok) {
                    alert("채용 정보가 등록되었습니다.");
                    location.href = "/admin/cs/recruit/list"; // 등록 후 목록으로 이동
                } else {
                    throw new Error("등록에 실패했습니다.");
                }
            })
            .catch(error => {
                alert(error.message);
            });
    };


    $(document).ready(function () {
        // 전체 선택 체크박스 클릭 시
        $('#selectAll').click(function () {
            $('.delete-checkbox').prop('checked', this.checked);
        });

        // 삭제 버튼 클릭 시
        $('.delete_btn').click(function () {
            var selectedIds = [];

            // 선택된 체크박스의 ID 수집
            $('.delete-checkbox:checked').each(function () {
                selectedIds.push($(this).val());
            });

            if (selectedIds.length > 0) {
                // 서버로 선택된 ID들을 삭제 요청
                $.ajax({
                    type: 'POST',
                    url: '/admin/recruit/delete',
                    contentType: 'application/json',
                    data: JSON.stringify({ recruitIds: selectedIds }),
                    success: function (response) {
                        if (response.success) {
                            alert('선택한 항목이 삭제되었습니다.');
                            location.reload(); // 삭제 후 페이지 새로고침
                        } else {
                            alert('삭제에 실패했습니다.');
                        }
                    },
                    error: function () {
                        alert('오류가 발생했습니다.');
                    }
                });
            } else {
                alert('삭제할 항목을 선택해주세요.');
            }
        });
    });

});
