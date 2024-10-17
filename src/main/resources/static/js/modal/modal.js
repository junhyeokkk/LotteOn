document.addEventListener('DOMContentLoaded', function() {

    // 모달 열기 함수
    function openModal(modalId) {
        const modal = document.getElementById(modalId);
        if (modal) {
            modal.style.display = 'block';
        }
    }

    // 모달 닫기 함수
    function closeModal(modalId) {
        const modal = document.getElementById(modalId);
        if (modal) {
            modal.style.display = 'none';
        }
    }

    // 모달 열기 버튼들 (data-modal 속성 기반으로 처리)
    document.querySelectorAll('[data-modal]').forEach(function (button) {
        button.addEventListener('click', function () {
            const modalId = this.getAttribute('data-modal');
            console.log(modalId);
            openModal(modalId);
        });
    });
    // <a href="#" data-modal="versioncheckModal">[확인]</a>
    // 모달 닫기 버튼들 (.closeModalBtn 클래스를 가진 모든 버튼에 이벤트 등록)
    document.querySelectorAll('#closeModal').forEach(function (button) {
        button.addEventListener('click', function () {
            const modal = button.closest('.modal');  // 가장 가까운 모달 요소를 찾음
            closeModal(modal.id);
        });
    });
});
