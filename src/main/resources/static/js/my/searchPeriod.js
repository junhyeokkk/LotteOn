document.addEventListener('DOMContentLoaded', function() {
    const today = new Date().toISOString().split('T')[0]; // 오늘 날짜
    const startDateInput = document.getElementById('startDate');
    const endDateInput = document.getElementById('endDate');

    // 처음에는 종료 날짜 입력 필드를 비활성화
    endDateInput.setAttribute('disabled', 'true');

    // 시작 날짜는 오늘 날짜 이전만 선택 가능하게 설정
    startDateInput.setAttribute('max', today);

    startDateInput.addEventListener('change', function() {
        const selectedStartDate = new Date(this.value);

        // 시작 날짜가 선택되면 종료 날짜 입력 필드를 활성화
        endDateInput.removeAttribute('disabled');

        // 종료 날짜는 시작 날짜 이후부터 3년 내로 설정, 그리고 오늘 날짜를 넘을 수 없음
        const maxEndDate = new Date(selectedStartDate);
        maxEndDate.setFullYear(maxEndDate.getFullYear() + 3);

        const formattedMaxEndDate = maxEndDate.toISOString().split('T')[0];
        const minEndDate = this.value;

        // 종료 날짜는 시작 날짜 이후부터 선택 가능하고, 오늘 날짜와 3년 후 날짜 중 더 이른 날짜까지만 선택 가능
        endDateInput.setAttribute('min', minEndDate);
        endDateInput.setAttribute('max', formattedMaxEndDate < today ? formattedMaxEndDate : today);
    });
});