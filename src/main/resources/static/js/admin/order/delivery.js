document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll(".delivery-detail-btn").forEach(button => {
        button.addEventListener("click", event => {
            event.preventDefault();
            console.log('찍히니?')
            const deliveryId = button.getAttribute("data-delivery-id");

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

});