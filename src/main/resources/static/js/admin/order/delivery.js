// 전역 변수로 deliveryId를 저장할 변수를 선언
let currentDeliveryId = null;
document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll(".delivery-detail-btn").forEach(button => {
        button.addEventListener("click", event => {
            event.preventDefault();

            console.log('찍히니?')
            const deliveryId = button.getAttribute("data-delivery-id");
            currentDeliveryId = button.getAttribute("data-delivery-id");

            fetch(`/api/admin/delivery/detail?id=${deliveryId}`)
                .then(response => response.json())
                .then(deliveryDetails => {
                    renderDeliveryInfoModal(deliveryDetails);
                    document.getElementById("deliverydetailmodal").style.display = "block";
                })
                .catch(error => console.error("Error fetching delivery details:", error));
        });
    });

    // 배송 상세 모달 채우기 함수
    function renderDeliveryInfoModal(deliveryDetails) {
        document.getElementById("order-number").textContent = deliveryDetails.orderId;
        document.getElementById("product-image").src = `/uploads/product/${deliveryDetails.productImage}`;
        document.getElementById("product-id").textContent = deliveryDetails.productId;
        document.getElementById("product-name").textContent = deliveryDetails.productName;
        document.getElementById("seller-name").textContent = deliveryDetails.sellerName;
        document.getElementById("price").textContent = `${deliveryDetails.price} 원`;
        document.getElementById("quantity").textContent = deliveryDetails.quantity;
        document.getElementById("delivery-fee").textContent = `${deliveryDetails.deliveryFee} 원`;
        document.getElementById("total-price").textContent = `${deliveryDetails.totalPrice} 원`;

        document.getElementById("order-id").textContent = deliveryDetails.orderId;
        document.getElementById("recipient-name").textContent = deliveryDetails.recipientName;
        document.getElementById("recipient-phone").textContent = deliveryDetails.recipientPhone;
        document.getElementById("recipient-address").textContent = deliveryDetails.recipientAddress;
        document.getElementById("delivery-company").textContent = deliveryDetails.deliveryCompany;
        document.getElementById("invoice-number").textContent = deliveryDetails.invoiceNumber;
        document.getElementById("additional-memo").textContent = deliveryDetails.memo;

        // 모달 열기
        document.getElementById("deliverydetailmodal").style.display = "block";
    }

    // 배송 완료 버튼 이벤트 리스너
    document.getElementById("complete-delivery-btn").addEventListener("click", function() {
        if (currentDeliveryId) { // 현재 deliveryId가 설정되어 있는지 확인
            fetch(`/api/admin/delivery/complete`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ deliveryId: currentDeliveryId }) // JSON으로 deliveryId 전송
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        alert("배송이 완료되었습니다.");
                        location.reload(); // 페이지 새로고침 또는 모달 닫기
                    } else {
                        alert("배송 완료 처리에 실패했습니다.");
                    }
                })
                .catch(error => console.error("Error:", error));
        } else {
            alert("배송 ID가 설정되지 않았습니다.");
        }
    });
});