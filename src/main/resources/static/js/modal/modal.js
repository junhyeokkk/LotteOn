/*
    날짜 : 2024/10/23
    이름 : 이도영(최초 작성자)
    내용 : 관리자 모달 처리 js
    수정이력
    - 2024/10/30 이도영 - 관리자 쿠폰 개별 모달 수정
    - 2024/11/01 이도영 - 다운받은 개별 쿠폰 모달 수정 임시
    - 2024/11/03 이도영 - 사용자 쿠폰 다운로드 시 처리 방식 수정
    - 2024/11/05 이도영 - 전반적인 모든 모달 기능 수정 , 삭제기능 수정기능 추가
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
            if(modalId === "couponmodal"){
                openCouponTakeModal(this);
                return;
            }
            if(modalId === "couponchangemodal"){
                openCouponChangeModal(this);
                return;
            }

            if(modalId === "reviewModal")
            {
                openReviewModal(this);
                return;
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

        return modal;
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
    // 삭제 확인 및 요청 함수
    function confirmDelete() {
        if (confirm("삭제하시겠습니까?")) {
            const couponId = document.querySelector('#couponchangemodal input[name="couponId"]').value;
            console.log("couponId : "+couponId);
            fetch(`/admin/coupon/delete/${couponId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('삭제 요청이 실패했습니다.');
                    }
                    alert("쿠폰이 성공적으로 삭제되었습니다.");
                    closeModal('couponchangemodal');
                    location.reload();
                })
                .catch(error => {
                    console.error('삭제 중 오류 발생:', error);
                    alert('삭제 중 오류가 발생했습니다.');
                });
        }
    }

    // 삭제 버튼에 confirmDelete 함수 바인딩
    const deleteButton = document.querySelector('.modalbutton[value="삭제하기"]');
    if (deleteButton) {
        deleteButton.addEventListener("click", confirmDelete);
    }
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

    // 다운받은 쿠폰 정보를 서버에서 가져와 모달을 여는 함수
    function openCouponTakeModal(element) {
        const couponId = element.getAttribute('data-coupontake-id');
        console.log("TTTTT"+couponId);
        // 서버에 쿠폰 정보를 요청하여 모달 업데이트
        fetch(`/admin/coupontake/select/${couponId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to load coupon data');
                }
                return response.json();
            })
            .then(data => {
                // 받은 데이터를 모달에 업데이트합니다.
                updateCouponTakeInfoInModal(data);
                // 업데이트된 후 모달을 엽니다.
                openModal('couponmodal');
            })
            .catch(error => {
                console.error('Error loading coupon data:', error);
                alert('쿠폰 정보를 불러오는 중 오류가 발생했습니다.');
            });
    }
    //수정할 쿠폰에 대한 모달 출력
    function openCouponChangeModal(element) {
        const couponId = element.getAttribute('data-coupon-id');
        console.log(couponId);
        document.querySelector('#couponchangemodal input[name="couponId"]').value = couponId;
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
                updateCouponChangeModal(data);
                // 데이터 처리 후 모달을 엽니다.
                openModal('couponchangemodal');
            })
            .catch(error => {
                console.error('Error loading coupon data:', error);
                alert('쿠폰 정보를 불러오는 중 오류가 발생했습니다.');
            });
    }

    function openReviewModal(element)
    {
        // TODO: Review Modal
        const itemElement = element.closest("[data-product-id]");
        const productId = itemElement.dataset.productId;
        const productName = itemElement.dataset.productName;

        const modal = openModal("reviewModal");
        updateReviewModal(modal, productId, productName);
    }

    function updateReviewModal(modal ,productId, productName) {
        const productNameEl = modal.querySelector(".productName");
        const productIdEl = modal.querySelector(".productNo");
        const contentEl = modal.querySelector("#reviewContent");
        const ratingEl = modal.querySelector(".rating");

        productNameEl.textContent = productName;
        productIdEl.value=productId;
        contentEl.value="";
        ratingEl.style.setProperty("--value", 5);
    }

    const reviewForm = document.querySelector("#reviewForm");
    if (reviewForm) {
        reviewForm.addEventListener("submit", async function(e) {
            e.preventDefault();

            try {
                const response = await fetch("/api/review", {
                    method: 'post',
                    body: JSON.stringify({
                        content: e.target.content.value,
                        score: e.target.score.value,
                        memberId: e.target.memberId.value,
                        productId: e.target.productId.value
                    }),
                    headers: {
                        'Content-Type': 'application/json'
                    },
                });

                if (!response.ok) {
                    alert("리뷰 등록에 실패했습니다.");
                } else {
                    alert("리뷰 등록 성공!!");
                }
            } catch (error) {
                alert("리뷰 등록에 실패했습니다.");
                console.error("Review Create Error:", error);
            } finally {
                closeModal("reviewModal");
            }
        });
    }

    // 쿠폰 정보를 모달에 업데이트하는 함수 (동적 업데이트)
    function updateCouponInfoInModal(coupon) {
        document.querySelector('.couponid').textContent = coupon.couponId || '정보 없음';
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

    //발급받은 쿠폰에 대한 모달 작성
    function updateCouponTakeInfoInModal(data) {
        // 쿠폰 번호
        document.querySelector('.couponid').textContent = data.couponId || 'N/A';
        // 발급처
        document.querySelector('.shopname').textContent = data.shopName || 'N/A';
        // 발급 번호
        document.querySelector('.coupontakeid').textContent = data.couponTakenId || 'N/A';

        // 사용 여부 상태 표시
        const useStatusElement = document.querySelector('.couponusecheck');
        const useStatus = {
            0: '미사용',
            2: '사용',
            3: '기간 만료',
            4: '정지'
        };
        useStatusElement.textContent = useStatus[data.couponUseCheck] || 'N/A';

        // 상태에 따른 색상 클래스 추가
        useStatusElement.classList.remove('status-unused', 'status-used', 'status-expired', 'status-stopped');
        if (data.couponUseCheck === 0) {
            useStatusElement.classList.add('status-unused');
        } else if (data.couponUseCheck === 2) {
            useStatusElement.classList.add('status-used');
        } else if (data.couponUseCheck === 3) {
            useStatusElement.classList.add('status-expired');
        } else if (data.couponUseCheck === 4) {
            useStatusElement.classList.add('status-stopped');
        }
        // 쿠폰 종류
        const usecouponType = {
            "single": '개별상품할인',
            "ordersale": '주문상품할인',
            "freedelivery": '배송비 무료',
        };
        document.querySelector('.coupontype').textContent = usecouponType[data.couponType] || 'N/A';
        // 쿠폰명
        document.querySelector('.couponname').textContent = data.couponName || 'N/A';
        
        // 혜택
        const usecouponDiscount = {
            1000:'1,000원 할인',
            2000:'2,000원 할인',
            3000:'3,000원 할인',
            4000:'4,000원 할인',
            5000:'5,000원 할인',
            0.1:'10% 할인',
            0.2:'20% 할인',
            0.3:'30% 할인',
            0.4:'40% 할인',
            0.5:'50% 할인',
            0:'배송비 무료'
        };
        document.querySelector('.coupondiscount').textContent = usecouponDiscount[data.couponDiscount] || 'N/A';
        
        // 사용 기간
        document.querySelector('.period').textContent = `${data.couponGetDateFormatted} ~ ${data.couponExpireDateFormatted}` || 'N/A';
        // 유의사항
        document.querySelector('.couponetc').value = data.couponetc || '';
    }
    //수정하는 쿠폰 정보를 보여주는 곳
    function updateCouponChangeModal(data) {
        // 예시: 모달에 표시할 데이터 업데이트
        // 발급처 정보
        console.log(data.memberId);
        document.querySelector('#couponchangemodal input[readonly]').value = data.issuerInfo || '';
        document.querySelector('#couponchangemodal input[name="memberId"]').value = data.memberId || '';
        // 쿠폰 종류 선택
        const coupontypeSelect = document.querySelector('#couponchangemodal select[name="coupontype"]');
        if (coupontypeSelect) coupontypeSelect.value = data.coupontype || 'single';
        // 쿠폰명
        document.querySelector('#couponchangemodal input[name="couponname"]').value = data.couponname || '';
        // 혜택 선택
        const coupondiscountSelect = document.querySelector('#couponchangemodal select[name="coupondiscount"]');
        if (coupondiscountSelect) coupondiscountSelect.value = data.coupondiscount || '';
        // 사용 기간
        document.querySelector('#couponchangemodal input[name="couponstart"]').value = data.couponstart ? data.couponstart.split('T')[0] : '';
        document.querySelector('#couponchangemodal input[name="couponend"]').value = data.couponend ? data.couponend.split('T')[0] : '';
        document.querySelector('#couponchangemodal input[name="couponperiod"]').value = data.couponperiod || '';
        // 유의사항
        document.querySelector('#couponchangemodal textarea[name="couponetc"]').value = data.couponetc || '';
        // 필요한 다른 필드들에 대해 같은 방식으로 설정합니다.
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
    document.querySelectorAll('.notusedBtn').forEach(button => {
        button.addEventListener('click', function (event) {
            event.preventDefault();
            event.preventDefault();
            const couponId = this.getAttribute('data-coupon-id');
            const useCheck = parseInt(this.getAttribute('data-use-check'), 10);

            if (confirm("변경하시겠습니까?")) {
                // 변경할 상태 설정
                const newStatus = useCheck === 0 ? 4 : 0;

                // 서버로 변경 요청 보내기
                fetch(`/admin/coupontake/cancelorrestart/${couponId}/${newStatus}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ couponUseCheck: newStatus })
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('상태 변경에 실패했습니다.');
                        }
                        alert("변경이 완료되었습니다.");
                        location.reload(); // 페이지 새로고침
                    })
                    .catch(error => {
                        console.error('변경 중 오류 발생:', error);
                        alert('변경 중 오류가 발생했습니다.');
                    });
            }
        });
    });
    document.querySelectorAll('.download-coupon-btn').forEach(button => {
        button.addEventListener('click', function() {
            const memberId = this.getAttribute('data-member-id');
            // memberId가 null 또는 없을 경우 로그인 페이지로 이동 안내
            if (!memberId) {
                const goToLogin = confirm("로그인 이후 쿠폰을 다운받을 수 있습니다. 로그인 페이지로 이동하시겠습니까?");
                if (goToLogin) {
                    window.location.href = "/user/login";
                }
                return;
            }

            const couponId = this.getAttribute('data-coupon-id');
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

    const saveButton = document.querySelector('.save-button');
    if (saveButton) {
        saveButton.addEventListener('click', function() {
            //2024/11/08 이도영 비로그인 쿠폰 사용 처리
            const memberId = document.querySelector('.download-coupon-btn').getAttribute('data-member-id');
            if (!memberId) {
                const goToLogin = confirm("로그인 이후 쿠폰을 다운받을 수 있습니다. 로그인 페이지로 이동하시겠습니까?");
                if (goToLogin) {
                    window.location.href = "/user/login";
                }
                return;
            }
            // 모든 쿠폰 ID 수집
            const couponIds = Array.from(document.querySelectorAll('.download-coupon-btn')).map(button =>
                button.getAttribute('data-coupon-id')
            );

            const shopId = document.querySelector('.download-coupon-btn').getAttribute('data-shop-id');
            // memberId가 null 또는 없을 경우 로그인 페이지로 이동 안내

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
                })
                .catch(error => {
                    console.error(error);
                    alert('쿠폰 저장 중 오류가 발생했습니다.');
                });
        });
    }
});
