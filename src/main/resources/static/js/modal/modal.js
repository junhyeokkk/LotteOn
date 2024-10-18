document.addEventListener('DOMContentLoaded', function() {

    // 모달 열기 함수
    function openModal(modalId) {
        const modal = document.getElementById(modalId);
        if (modal) {
            modal.style.display = 'block';
            document.body.style.overflow = 'hidden';  // 외부 스크롤 막기
        }
    }

    // 모달 닫기 함수
    function closeModal(modalId) {
        const modal = document.getElementById(modalId);
        if (modal) {
            modal.style.display = 'none';
            document.body.style.overflow = '';  // 외부 스크롤 다시 활성화
        }
    }

    // 모달 열기 버튼들 (data-modal 속성 기반으로 처리)
    document.querySelectorAll('[data-modal]').forEach(function (button) {
        button.addEventListener('click', function () {
            const modalId = this.getAttribute('data-modal');
            if(modalId === "inquiryModal"){
                closeModal('sellerInfoModal');
            }
            console.log(modalId);
            openModal(modalId);
        });
    });
    // <a href="#" data-modal="versioncheckModal">[확인]</a>
    // 모달 닫기 버튼들 (.closeModalBtn 클래스를 가진 모든 버튼에 이벤트 등록)
    document.querySelectorAll('#closeModal, .modalcanclebutton').forEach(function (button) {
        button.addEventListener('click', function () {
            const modal = button.closest('.modal');  // 가장 가까운 모달 요소를 찾음
            closeModal(modal.id);
        });
    });
    // 별 클릭 이벤트
    const modalstars = document.querySelectorAll('.modalstar');
    modalstars.forEach((modalstar, index) => {
        modalstar.addEventListener('click', () => {
            modalstars.forEach(s => s.classList.remove('filled'));
            for (let i = 0; i <= index; i++) {
                modalstars[i].classList.add('filled');
            }
        });
    });
});
