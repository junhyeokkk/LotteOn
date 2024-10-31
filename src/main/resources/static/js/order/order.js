/*
    날짜 : 2024/10/31
    이름 : 최준혁
    내용 : 오더 js 파일 생성
*/
function submitOrder() {
    const formData = new FormData(document.getElementById("buyForm"));

    // 주문 항목 데이터 수집
    const orderItems = [];
    document.querySelectorAll(".order__item").forEach((itemRow, index) => {
        orderItems.push({
            productId: formData.getAll("productId")[index],
            quantity: itemRow.querySelector("td:nth-child(2)").textContent.trim(),
            discountRate: itemRow.querySelector("td:nth-child(4)").textContent.trim().replace("%", ""),
            point: itemRow.querySelector("td:nth-child(5)").textContent.trim().replace(",", ""),
            deliveryFee: itemRow.querySelector("td:nth-child(6)").textContent.trim().replace("원", "").replace("무료", "0"),
            orderPrice: itemRow.querySelector("td:nth-child(7)").textContent.trim().replace(",", ""),
            productOptionCombinationFormatting : itemRow.querySelector("p[name='formattedOptions']").textContent.trim(),
            productOptionCombinationId : formData.getAll("combinationId")[index],
            productName : itemRow.querySelector(".product-name").textContent.trim()
        });

        console.log('오더아이템 ' + orderItems);
    });



    // Form 데이터에서 필요한 정보를 JSON 형태로 구성
    const orderData = {
        recipientName: formData.get("recipientName"),
        recipientPhone: formData.get("recipientPhone"),
        recipientZip: formData.get("recipientZip"),
        recipientAddr1: formData.get("recipientAddr1"),
        recipientAddr2: formData.get("recipientAddr2"),
        usedPoint: parseInt(formData.get("usedPoint") || "0", 10),
        couponId: formData.get("couponId") !== "쿠폰선택" ? parseInt(formData.get("couponId"), 10) : null,
        paymentMethod: formData.get("paymentMethod"),
        orderItems: orderItems,
        totalPrice: parseInt(document.getElementById("total").textContent.trim().replace(",", ""))
    };
    console.log('오더 데이타 ' + orderData);

    // API 요청 보내기
    fetch('/api/order/create', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(orderData)
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error("주문에 실패했습니다.");
        })
        .then(data => {
            console.log("주문 성공:", data);
            alert("주문이 성공적으로 완료되었습니다.");
            // 예: 주문 완료 후 주문 요약 페이지로 이동하기
            window.location.href = '/product/complete'
            // window.location.href = `/order/summary/${data.orderId}`;
        })
        .catch(error => {
            console.error("Error:", error);
            alert(error.message);
        });
}
