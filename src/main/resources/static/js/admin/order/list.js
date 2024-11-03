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
                <td><img class="modalimgsize" th:src="'/uploads/product/'+${order.productImage}" alt="상품 이미지" /></td>
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

    // 각 주문 보기 버튼에 클릭 이벤트 추가
    document.querySelectorAll(".cs_modify-btn").forEach(button => {
        button.addEventListener("click", event => {
            event.preventDefault();
            const orderId = button.getAttribute("data-order-id");

            fetch(`/admin/order/detail?id=${orderId}`)
                .then(response => response.text())  // JSON 대신 text로 응답을 확인
                .then(data => {
                    console.log("Raw response:", data);  // JSON 파싱 전의 원본 응답 출력
                    const order = JSON.parse(data);     // 파싱 시도
                    renderOrderDetail(order);
                })
                .catch(error => console.error("Error fetching order details:", error));

        });
    });

    // 모달 닫기 버튼 클릭 이벤트
    closeModalBtn.addEventListener("click", closeModal);

    // 모달 외부를 클릭했을 때 모달 닫기
    window.addEventListener("click", event => {
        if (event.target === modal) {
            closeModal();
        }
    });
});
