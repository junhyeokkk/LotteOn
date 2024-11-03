/*
    날짜 : 2024/10/23
    이름 : 이도영(최초 작성자)
    내용 : 관리자 모달 처리 js
    수정이력
    - 2024/10/30 이도영 - 관리자 쿠폰 개별 모달 수정
    - 2024/11/01 이도영 - 다운받은 개별 쿠폰 모달 수정 임시
    - 2024/11/03 이도영 - 사용자 쿠폰 다운로드 시 처리 방식 수정
 */
document.addEventListener('DOMContentLoaded', function () {

    // 모달 열기 버튼들 (data-modal 속성 기반으로 처리)
    document.querySelectorAll('[data-modal]').forEach(function (button) {
        button.addEventListener('click', function () {
            const modalId = this.getAttribute('data-modal');

            // couponinfomodal에 대한 처리를 추가
            if (modalId === "couponinfomodal") {
                openCouponModal(this);
                return; // couponinfomodal은 데이터 처리 후 열기 때문에 여기서 일반 모달 열기 방식을 사용하지 않음
            }

            // 다른 모달에 대한 처리
            if (modalId === "inquiryModal") {
                closeModal('sellerInfoModal');
            }
            if (modalId === "deliberyinsertModal") {
                closeModal('deliberyModal');
            }
            console.log(modalId);
            openModal(modalId);
        });
    });

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

    // 모달 닫기 버튼들 (.closeModalBtn 클래스를 가진 모든 버튼에 이벤트 등록)
    document.querySelectorAll('#closeModal, .modalcanclebutton').forEach(function (button) {
        button.addEventListener('click', function () {
            const modal = button.closest('.modal');  // 가장 가까운 모달 요소를 찾음
            closeModal(modal.id);
        });
    });

    // 쿠폰 정보를 서버에서 가져와 모달을 여는 함수
    function openCouponModal(element) {
        const couponId = element.getAttribute('data-coupon-id');

        // 서버에 쿠폰 정보를 요청하여 모달 업데이트
        fetch(`/admin/coupon/select/${couponId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to load coupon data');
                }
                return response.json();
            })
            .then(data => {
                // 받은 데이터를 모달에 업데이트합니다.
                updateCouponInfoInModal(data);
                // 업데이트된 후 모달을 엽니다.
                openModal('couponinfomodal');
            })
            .catch(error => {
                console.error('Error loading coupon data:', error);
                alert('쿠폰 정보를 불러오는 중 오류가 발생했습니다.');
            });
    }

    // 쿠폰 정보를 서버에서 가져와 모달을 여는 함수
    function openCouponTakeModal(element) {
        const couponId = element.getAttribute('data-coupontake-id');

        // 서버에 쿠폰 정보를 요청하여 모달 업데이트
        fetch(`/admin/coupon/select/${couponId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to load coupon data');
                }
                return response.json();
            })
            .then(data => {
                // 받은 데이터를 모달에 업데이트합니다.
                updateCouponInfoInModal(data);
                // 업데이트된 후 모달을 엽니다.
                openModal('couponinfomodal');
            })
            .catch(error => {
                console.error('Error loading coupon data:', error);
                alert('쿠폰 정보를 불러오는 중 오류가 발생했습니다.');
            });
    }

    // 쿠폰 정보를 모달에 업데이트하는 함수 (동적 업데이트)
    function updateCouponInfoInModal(coupon) {
        document.querySelector('.couponid').textContent = coupon.couponid || '정보 없음';
        document.querySelector('.issuerInfo').textContent = coupon.issuerInfo || '정보 없음';
        document.querySelector('.couponname').textContent = coupon.couponname || '정보 없음';

        // 쿠폰 종류
        let coupontypeText = '알 수 없음';
        switch (coupon.coupontype) {
            case 'single':
                coupontypeText = '개별상품할인';
                break;
            case 'ordersale':
                coupontypeText = '주문상품할인';
                break;
            case 'freedelivery':
                coupontypeText = '배송비무료';
                break;
        }
        document.querySelector('.coupontype').textContent = coupontypeText;

        // 쿠폰 할인
        let coupondiscountText = '할인 없음';
        switch (coupon.coupondiscount) {
            case 1:
                coupondiscountText = '1000원 할인';
                break;
            case 2:
                coupondiscountText = '2000원 할인';
                break;
            case 3:
                coupondiscountText = '3000원 할인';
                break;
            case 4:
                coupondiscountText = '4000원 할인';
                break;
            case 5:
                coupondiscountText = '5000원 할인';
                break;
            case 6:
                coupondiscountText = '10% 할인';
                break;
            case 7:
                coupondiscountText = '20% 할인';
                break;
            case 8:
                coupondiscountText = '30% 할인';
                break;
            case 9:
                coupondiscountText = '40% 할인';
                break;
            case 10:
                coupondiscountText = '50% 할인';
                break;
            case 11:
                coupondiscountText = '배송비 무료';
                break;
        }
        document.querySelector('.coupondiscount').textContent = coupondiscountText;

        // 쿠폰 기간 - T 뒤의 내용 제거
        const startDate = coupon.couponstart ? coupon.couponstart.split('T')[0] : '정보 없음';
        const endDate = coupon.couponend ? coupon.couponend.split('T')[0] : '정보 없음';
        document.querySelector('.coupondate').textContent = `${startDate} ~ ${endDate}`;
        document.querySelector('.couponperiod').innerHTML = '발급일로부터 <span style="color: red;">' + coupon.couponperiod + '</span>일 이내';
        document.querySelector('.couponetc').textContent = coupon.couponetc || '정보 없음';
    }

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
    document.querySelectorAll('.download-coupon-btn').forEach(button => {
        button.addEventListener('click', function() {
            const couponId = this.getAttribute('data-coupon-id');
            const memberId = this.getAttribute('data-member-id');
            const shopId = this.getAttribute('data-shop-id');
            console.log("couponId: " + couponId);
            console.log("memberId: " + memberId);
            console.log("shopId: " + shopId);

            fetch(`/coupontake/set/${memberId}/${shopId}/${couponId}`, {
                method: 'GET'
            })
                .then(response => {
                    if (response.status === 409) {
                        throw new Error('이미 저장한 쿠폰입니다');
                    } else if (!response.ok) {
                        throw new Error('쿠폰 저장에 실패했습니다');
                    }
                    return response.json();
                })
                .then(data => {
                    alert('쿠폰이 성공적으로 저장되었습니다!');
                })
                .catch(error => {
                    if (error.message === '이미 저장한 쿠폰입니다') {
                        alert(error.message);
                    } else {
                        console.error(error);
                        alert('쿠폰 저장 중 오류가 발생했습니다.');
                    }
                });
        });
    });
    document.querySelector('.save-button').addEventListener('click', function() {
        // 모든 쿠폰 ID 수집
        const couponIds = Array.from(document.querySelectorAll('.download-coupon-btn')).map(button =>
            button.getAttribute('data-coupon-id')
        );

        const memberId = document.querySelector('.download-coupon-btn').getAttribute('data-member-id');
        const shopId = document.querySelector('.download-coupon-btn').getAttribute('data-shop-id');

        // 서버로 요청 보내기
        fetch(`/coupontake/all/${memberId}/${shopId}?couponIds=` + couponIds.join(','), {
            method: 'GET'
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('쿠폰 저장에 실패했습니다');
                }
                return response.json();
            })
            .then(data => {
                alert('모든 쿠폰이 성공적으로 저장되었습니다!');
                // 필요한 경우, 화면을 업데이트하거나 추가 알림을 표시할 수 있습니다.
            })
            .catch(error => {
                console.error(error);
                alert('쿠폰 저장 중 오류가 발생했습니다.');
            });
    });
});
