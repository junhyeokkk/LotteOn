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
            productName : itemRow.querySelector(".product-name").textContent.trim(),
            cartId: formData.getAll("cartId")[index]
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
            // window.location.href = '/product/complete'
            window.location.href = `/product/complete/${data.orderId}`;
        })
        .catch(error => {
            console.error("Error:", error);
            alert(error.message);
        });
}



document.addEventListener('DOMContentLoaded', function() {
    const applyButton = document.querySelector('.point-usage .btn-apply');

    if (applyButton) {
        applyButton.addEventListener('click', function(event) {
            // 기본 동작 방지
            event.preventDefault();

            // 입력된 포인트 값 가져오기
            let inputPoint = parseInt(document.getElementById('inputPoint').value);
            let currentPoints = parseInt(document.getElementById('userPoint').textContent.replace(/[^0-9]/g, ''));
            let totalAmountElement = document.getElementById('total');
            let totalAmount = parseInt(totalAmountElement.textContent.replace(/,/g, ''));
            let pointDiscountElement = document.getElementById('prodPoint');

            // 입력된 포인트 유효성 검사
            if (isNaN(inputPoint) || inputPoint <= 0) {
                alert('올바른 포인트 값을 입력하세요.');
                return;
            }

            if (inputPoint > currentPoints) {
                alert('보유 포인트보다 많은 포인트를 사용할 수 없습니다.');
                return;
            }

            if (inputPoint < 5000) {
                alert('최소 5,000점 이상만 사용할 수 있습니다.');
                return;
            }

            // 포인트 할인 적용 후 서버로 전송
            fetch('/api/order/use', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    givePoints: inputPoint
                })
            })
                .then(response => {
                    // 응답의 Content-Type이 JSON인지 확인하고 파싱
                    const contentType = response.headers.get("content-type");
                    if (contentType && contentType.indexOf("application/json") !== -1) {
                        return response.json();
                    } else {
                        return response.text(); // JSON이 아닌 경우 text로 처리
                    }
                })
                .then(data => {
                    if (typeof data === 'string') {
                        alert(data); // 서버에서 받은 문자열을 표시
                    } else if (data.status === 'OK') {
                        alert('포인트가 성공적으로 사용되었습니다.');

                        // 포인트 할인 및 총 결제 금액 화면에 반영
                        let newTotalAmount = totalAmount - inputPoint;
                        pointDiscountElement.textContent = `-${inputPoint.toLocaleString()}원`;
                        totalAmountElement.textContent = newTotalAmount.toLocaleString() + '원';
                    } else {
                        alert('포인트 사용에 실패했습니다: ' + data.message);
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('서버 오류가 발생했습니다.');
                });
        });
    } else {
        console.error('포인트 사용 버튼을 찾을 수 없습니다.');
    }
});
