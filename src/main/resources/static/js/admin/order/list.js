document.addEventListener("DOMContentLoaded", () => {
    const modal = document.getElementById("orderinfomodal");
    const closeModalBtn = document.getElementById("closeModal");

    // 모달 열기 함수
    function openModal() {
        modal.style.display = "block";
    }

    // 모달 닫기 함수
    function closeModal() {
        modal.style.display = "none";
    }

    function renderOrderDetail(order) {
        const orderTable = document.querySelector(".widthtable tbody");
        orderTable.innerHTML = "";  // 기존 데이터 제거

        if (order && Array.isArray(order.orderItems) && order.orderItems.length > 0) {
            order.orderItems.forEach(item => {
                const tr = document.createElement("tr");
                tr.innerHTML = `
                <td><img class="modalimgsize" src="/${item.productImage}" alt="상품 이미지" /></td>
                <td>${item.productId}</td>
                <td>${item.productName}</td>
                <td>${item.shopName}</td>
                <td>${item.originalPrice}원</td>
                <td>${item.discountAmount}원</td>
                <td>${item.quantity}개</td>
                <td>${item.deliveryPrice ? item.deliveryPrice.toLocaleString() : "0"}원</td>
                <td>${item.orderPrice ? item.orderPrice.toLocaleString() : "0"}원</td>
            `;
                orderTable.appendChild(tr);
            });
        } else {
            orderTable.innerHTML = "<tr><td colspan='9'>주문 내역이 없습니다.</td></tr>";
        }

        // 주문 요약 정보 업데이트
        document.querySelector(".modalprice-value div:nth-child(1) span:last-child").innerText = `${order.totalOriginalPrice ? order.totalOriginalPrice.toLocaleString() : "0"}원`;
        document.querySelector(".modalprice-value div:nth-child(2) span:last-child").innerText = `${order.totalDiscountAmount ? order.totalDiscountAmount.toLocaleString() : "0"}원`;
        document.querySelector(".modalprice-value div:nth-child(3) span:last-child").innerText = `${order.totalDeliveryFee ? order.totalDeliveryFee.toLocaleString() : "0"}원`;
        document.querySelector(".modalprice-value div:nth-child(4) span:last-child").innerText = `${order.totalPaymentAmount ? order.totalPaymentAmount.toLocaleString() : "0"}원`;

        // 주문 정보 업데이트
        document.querySelector(".heighttable tr:nth-of-type(1) td").innerText = `${order.orderId}`;
        document.querySelector(".heighttable tr:nth-of-type(2) td").innerText = `${order.paymentMethod}`;
        document.querySelector(".heighttable tr:nth-of-type(3) td").innerText = `${order.ordererName} / ${order.ordererPhone}`;
        document.querySelector(".heighttable tr:nth-of-type(4) td").innerText = `${order.status}`;
        document.querySelector(".heighttable tr:nth-of-type(5) td").innerText = `${order.totalOrderAmount}`;

        // 배송정보 업데이트
        document.querySelector(".deliverytable tr:nth-of-type(1) td").innerText = `${order.recipientName}`;
        document.querySelector(".deliverytable tr:nth-of-type(2) td").innerText = `${order.recipientPhone}`;
        document.querySelector(".deliverytable tr:nth-of-type(3) td").innerText = `${order.recipientAddress}`;

        modal.style.display = "block";  // 모달 열기
    }

    // 각 주문번호에 해당되는 주문 들고오기 (주문상세)
    document.querySelectorAll(".cs_modify-btn").forEach(button => {
        button.addEventListener("click", event => {
            event.preventDefault();
            const orderId = button.getAttribute("data-order-id");

            fetch(`/api/admin/order/detail?id=${orderId}`)
                .then(response => response.text())  // JSON 대신 text로 응답을 확인
                .then(data => {
                    console.log("Raw response:", data);  // JSON 파싱 전의 원본 응답 출력
                    const order = JSON.parse(data);     // 파싱 시도
                    renderOrderDetail(order);
                })
                .catch(error => console.error("Error fetching order details:", error));

        });
    });

    // 주문 리스트 모달에서 주문 아이템별로 배송 등록을 위해 주문 아이템 리스트 띄우기 (배송)
    document.querySelectorAll(".deliveryBtn").forEach(button => {
        button.addEventListener("click", event => {
            event.preventDefault();
            const orderId = button.getAttribute("data-order-id");

            fetch(`/api/admin/order/detail?id=${orderId}`)
                .then(response => response.json())
                .then(order => {
                    console.log('sadfffffffff' + order)
                    renderOrderItemsInDeliveryModal(order);
                    document.getElementById("deliveryInsertModal").style.display = "block"; // 주문 리스트 모달 표시
                })
                .catch(error => console.error("Error fetching order details:", error));
        });
    });

    // 주문 리스트 모달에 주문 아이템 추가 함수
    function renderOrderItemsInDeliveryModal(order) {
        const modalSection = document.querySelector("#deliveryInsertModal .modal-section");

        console.log('ddddd'+ order)
        modalSection.innerHTML = `<h3>주문 리스트</h3>`; // 기존 내용을 지우고 주문 리스트 제목 추가

        // 주문 아이템별로 카드 생성
        order.orderItems.forEach(item => {
            const itemHTML = `
        <div class="delivery-card">
            <div class="delivery-card-header">
                <div class="delivery-title">
                    <strong>${item.productName}</strong> (수량: ${item.quantity})
                </div>
                <div class="option-info">
                    ${item.optionCombination || "옵션 없음"}
                </div>
                ${item.deliveryStatus === 'READY' ? `
                    <div class="action-buttons">
                        <button class="delivery-register-btn" data-order-item-id="${item.id}">배송 등록</button>
                    </div>
                ` : `<div class="action-buttons">
                       <span>배송등록완료</span>
                    </div>`}
            </div>
        </div>
    `;
            modalSection.insertAdjacentHTML("beforeend", itemHTML);
        });

        // 배송 등록 버튼 클릭 이벤트 연결
        document.querySelectorAll(".delivery-register-btn").forEach(button => {
            button.addEventListener("click", event => {
                const orderItemId = event.currentTarget.getAttribute("data-order-item-id");
                openDeliveryInfoModal(orderItemId);
            });
        });
    }

    // 주문 아이템별로 배송 등록 모달 열기
    function openDeliveryInfoModal(orderItemId) {
        fetch(`/api/admin/order/item/detail?id=${orderItemId}`)
            .then(response => response.json())
            .then(itemDetails => {
                renderDeliveryInfoModal(itemDetails); // 상세 정보를 모달에 렌더링
                document.getElementById("deliveryInsertModal").style.display= "none"
                document.getElementById("deliveryInfoModal").style.display = "block"; // 배송 상세 모달 표시
            })
            .catch(error => console.error("오더 아이템 select 실패:", error));
    }

    // 배송 등록 모달에 orderitem 필요한 값 띄우기
    function renderDeliveryInfoModal(itemDetails) {
        const deliveryModal = document.querySelector("#deliveryInfoModal .modal-section");

        deliveryModal.innerHTML = `
        <article>
            <h3>상품 정보</h3>
            <table border="0" class="widthtable">
                <tr>
                    <th></th>
                    <th>상품번호</th>
                    <th>상품명</th>
                    <th>판매자</th>
                    <th>가격</th>
                    <th>수량</th>
                    <th>배송비</th>
                    <th>결제금액</th>
                </tr>
                <tr>
                    <td><img class="modalimgsize" src="http://localhost:8080/uploads/product/${itemDetails.productImg}" alt="상품 이미지" /></td>
                    <td>${itemDetails.productId}</td>
                    <td>${itemDetails.productName}</td>
                    <td>${itemDetails.shopName}</td>
                    <td>${itemDetails.price}</td>
                    <td>${itemDetails.quantity}</td>
                    <td>${itemDetails.deliveryFee}</td>
                    <td>${itemDetails.totalPrice}</td>
                </tr>
            </table>
        </article>
        <article>
            <h3>배송 정보</h3>
            <table border="0" class="heighttable">
                <input type="hidden" name="OrderItemId" value="${itemDetails.orderItemId}"/>
                <tr><th>주문번호</th><td>${itemDetails.orderId}</td></tr>
                <tr><th>수령인</th><td>${itemDetails.recipientName}</td></tr>
                <tr><th>연락처</th><td>${itemDetails.recipientPhone}</td></tr>
                <tr><th>주소</th><td>${itemDetails.recipientAddr1}</td></tr>
                <tr><th>택배사</th><td><input name="delCompany" type="text" required/></td></tr>
                <tr><th>송장번호</th><td><input name="invoiceNum" type="text" required/></td></tr>
                <tr><th>메모</th><td><input name="memo" type="text" required/></td></tr>
            </table>
            <div class="modalbuttondiv">
                 <input class="adminmodalbutton" type="button" value="등록하기" />
            </div>
        </article>
    `;

        // 등록하기 버튼 등록이벤트
        deliveryModal.querySelector(".adminmodalbutton").addEventListener("click", function () {
            const deliveryData = {
                orderItemId: document.querySelector("input[name='OrderItemId']").value,
                delCompany: document.querySelector("input[name='delCompany']").value,
                invoiceNum: document.querySelector("input[name='invoiceNum']").value, // 하이픈 인코딩
                memo: document.querySelector("input[name='memo']").value
            };

            fetch("/api/admin/order/register-delivery", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(deliveryData),
            })
                .then(response => response.text())
                .then(message => {
                    alert(message); // 성공 메시지 출력
                    document.getElementById("deliveryInfoModal").style.display = "none"; // 모달 닫기
                    location.reload(); // 페이지 새로고침하여 데이터 반영
                })
                .catch(error => console.error("배송 등록 실패:", error));
        });
    }



    // 모달 닫기 버튼 클릭 이벤트
    closeModalBtn.addEventListener("click", closeModal);

    // 모달 외부를 클릭했을 때 모달 닫기
    window.addEventListener("click", event => {
        if (event.target === modal) {
            closeModal();
        }
    });
});
