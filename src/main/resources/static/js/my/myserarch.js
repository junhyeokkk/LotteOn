// 전체 선택 함수
function toggleAllChecks(checkbox) {
    // 모든 개별 체크박스를 선택
    const checkboxes = document.querySelectorAll("input[name='RowCheck']");

    // 전체 선택 체크박스 상태에 따라 모든 체크박스를 선택 또는 해제
    checkboxes.forEach((RowCheck) => {
        RowCheck.checked = checkbox.checked;
    })
}

document.addEventListener('DOMContentLoaded', function () {
    const today = new Date();
    const todayStr = today.toISOString().split('T')[0]; // 오늘 날짜 문자열
    const startDateInput = document.getElementById('startDate');
    const endDateInput = document.getElementById('endDate');

    // 시작 날짜의 최대 값을 최신 5개월 전으로 설정
    const maxStartDate = new Date(today);
    maxStartDate.setMonth(today.getMonth() - 5);
    startDateInput.setAttribute('min', maxStartDate.toISOString().split('T')[0]);
    startDateInput.setAttribute('max', todayStr);

    // 종료 날짜의 최대 값을 오늘로 설정
    endDateInput.setAttribute('max', todayStr);

    // 시작 날짜 변경 시 종료 날짜 설정
    startDateInput.addEventListener('change', function () {
        const selectedStartDate = new Date(this.value);
        const maxEndDate = new Date(selectedStartDate);
        maxEndDate.setFullYear(maxEndDate.getFullYear() + 1); // 최대 1년 후까지만 조회 가능

        const formattedMaxEndDate = maxEndDate.toISOString().split('T')[0];
        endDateInput.setAttribute('min', this.value);
        endDateInput.setAttribute('max', formattedMaxEndDate < todayStr ? formattedMaxEndDate : todayStr);
    });

    // 기간 버튼 클릭 시 시작 날짜 및 종료 날짜 설정
    document.querySelectorAll('.period-btn').forEach(button => {
        button.addEventListener('click', function () {
            const period = this.getAttribute('data-period');
            let startDate;

            switch (period) {
                case 'week': // 1주일
                    startDate = new Date(today);
                    startDate.setDate(today.getDate() - 7);
                    break;
                case 'halfmonth': // 15일
                    startDate = new Date(today);
                    startDate.setDate(today.getDate() - 15);
                    break;
                case 'month': // 1개월
                    startDate = new Date(today);
                    startDate.setMonth(today.getMonth() - 1);
                    break;
                case 'all': // 전체 조회 (날짜 비우기)
                    startDateInput.value = '';
                    endDateInput.value = '';
                    return;
            }

            // 시작 날짜와 종료 날짜를 설정
            startDateInput.valueAsDate = startDate;
            endDateInput.valueAsDate = today; // 종료 날짜는 오늘로 설정
        });
    });
});

function searchRecords(baseUrl) {
    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;

    if (!startDate && !endDate) {
        // 전체 조회 URL (기본 경로로 이동)
        const url = baseUrl;
        window.location.href = url;
    } else if (startDate && endDate) {
        // 특정 날짜 범위 조회 URL
        const url = `${baseUrl}?startDate=${startDate}&endDate=${endDate}`;
        window.location.href = url;
    } else {
        // 시작 날짜 또는 종료 날짜 중 하나만 선택된 경우 경고 메시지
        alert("시작 날짜와 종료 날짜를 모두 선택하거나, 날짜를 비워 전체 조회를 선택하세요.");
    }
}

