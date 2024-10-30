/*
     날짜 : 2024/10/30
     이름 : 이도영
     내용 : 달력 기간 설정
*/
document.addEventListener('DOMContentLoaded', function() {
    const today = new Date().toISOString().split('T')[0]; // 오늘 날짜
    const startDateInput = document.getElementById('startDate');
    const endDateInput = document.getElementById('endDate');

    // 처음에는 종료 날짜 입력 필드를 비활성화
    endDateInput.setAttribute('disabled', 'true');

    // 시작 날짜는 오늘 날짜와 이후만 선택 가능하게 설정
    startDateInput.setAttribute('min', today);

    startDateInput.addEventListener('change', function() {
        const selectedStartDate = new Date(this.value);

        // 시작 날짜가 선택되면 종료 날짜 입력 필드를 활성화
        endDateInput.removeAttribute('disabled');

        // 종료 날짜는 시작 날짜 이후부터 3년 내로 설정
        const maxEndDate = new Date(selectedStartDate);
        maxEndDate.setFullYear(maxEndDate.getFullYear() + 3);

        const formattedMaxEndDate = maxEndDate.toISOString().split('T')[0];
        const minEndDate = selectedStartDate.toISOString().split('T')[0];

        // 종료 날짜는 시작 날짜 이후부터 선택 가능하고, 최대 3년 뒤까지 설정 가능
        endDateInput.setAttribute('min', minEndDate);
        endDateInput.setAttribute('max', formattedMaxEndDate);
    });
});
