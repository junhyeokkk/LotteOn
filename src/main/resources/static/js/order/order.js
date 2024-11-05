/*
    날짜 : 2024/10/31
    이름 : 최준혁
    내용 : 오더 js 파일 생성
*/

document.addEventListener("DOMContentLoaded", function () {
    const userPoints = parseInt(document.getElementById("userPoint").textContent.trim(), 10);
    const inputPoint = document.getElementById("inputPoint");
    const couponApplyButton = document.querySelector(".coupon-apply");
    const prodDisElement = document.getElementById("prodDis");
    const totalElement = document.getElementById("total");
    const deliveryFeeElement = document.getElementById("prodFee");

    // 포인트 5000 이상일 때만 readonly 해제
    if (userPoints >= 5000) {
        inputPoint.readOnly = false;
    } else {
        inputPoint.placeholder = "사용할 수 있는 포인트가 부족합니다";
    }

    couponApplyButton.addEventListener("click", toggleCouponUsage);

    function toggleCouponUsage() {
        if (couponApplyButton.textContent === "사용하기") {
            applyCoupon();
            alert('쿠폰이 적용되었습니다.');
            couponApplyButton.textContent = "사용취소";
        } else {
            cancelCoupon();
            alert('쿠폰이 적용취소되었습니다.');
            couponApplyButton.textContent = "사용하기";
        }
    }
});

// 초기 상태 저장
let originalValues = {
    totalDiscount: 0,
    deliveryFee: 0,
    totalOrderPrice: 0
};

// 쿠폰 적용 함수
function applyCoupon() {
    const couponSelect = document.getElementById("takecoupon");
    const prodDisElement = document.getElementById("prodDis");
    const totalElement = document.getElementById("total");
    const deliveryFeeElement = document.getElementById("prodFee");

    const selectedOption = couponSelect.options[couponSelect.selectedIndex];
    const discountValue = parseFloat(selectedOption.getAttribute("data-discount")) || 0;

    let discountAmount = 0;
    let totalOrderPrice = parseFloat(totalElement.textContent.replace(/[^\d]/g, "")) || 0;

    // Save original values for cancellation (only if not already set)
    if (originalValues.totalOrderPrice === 0) {
        originalValues.totalDiscount = parseFloat(prodDisElement.textContent.replace(/[^\d]/g, "")) || 0;
        originalValues.deliveryFee = parseFloat(deliveryFeeElement.textContent.replace(/[^\d]/g, "")) || 0;
        originalValues.totalOrderPrice = totalOrderPrice;
    }

    // Discount based on value
    if (discountValue >= 1) {
        discountAmount = discountValue;
    } else if (discountValue > 0 && discountValue < 1) {
        discountAmount = totalOrderPrice * discountValue;
    } else {
        deliveryFeeElement.textContent = `0원`;
    }

    // Update display elements
    document.getElementById("couponDiscount").textContent = `-${discountAmount.toLocaleString()}원`;
    prodDisElement.textContent = `-${(originalValues.totalDiscount + discountAmount).toLocaleString()}원`;
    totalElement.textContent = `${(totalOrderPrice - discountAmount).toLocaleString()}원`;
}

// 쿠폰 취소 함수
function cancelCoupon() {
    const couponDiscount = document.getElementById("couponDiscount");
    const prodDisElement = document.getElementById("prodDis");
    const deliveryFeeElement = document.getElementById("prodFee");
    const totalElement = document.getElementById("total");

    // Reset discount, delivery fee, and total order price to original values
    couponDiscount.textContent = `-0원`;
    prodDisElement.textContent = `-${originalValues.totalDiscount.toLocaleString()}원`;
    deliveryFeeElement.textContent = `${originalValues.deliveryFee.toLocaleString()}원`;
    totalElement.textContent = `${originalValues.totalOrderPrice.toLocaleString()}원`;
}


// 포인트 유효성 검사
function isValidPoint(usedPoints, userPoints) {
    if (usedPoints < 5000) {
        alert("포인트는 5,000점 이상 사용해야 합니다.");
        return false;
    }
    if (usedPoints > userPoints) {
        alert("사용 가능한 포인트를 초과했습니다.");
        return false;
    }
    return true;
}

// 포인트 적용
function applyPoints() {
    const inputPoint = document.getElementById("inputPoint");
    const userPoints = parseInt(document.getElementById("userPoint").textContent.trim(), 10);
    const usedPoints = parseInt(inputPoint.value, 10);
    const prodPointElement = document.getElementById("prodPoint");
    const prodDisElement = document.getElementById("prodDis");
    const totalElement = document.getElementById("total");

    if (isNaN(usedPoints) || usedPoints <= 0) {
        alert("사용할 포인트를 올바르게 입력하세요.");
        return;
    }

    if (isValidPoint(usedPoints, userPoints)) {
        alert(`포인트 ${usedPoints}점이 적용되었습니다.`);

        // 포인트 할인 표시 업데이트
        prodPointElement.textContent = `-${usedPoints.toLocaleString()}원`;

        // 할인 금액에 포인트 추가 적용
        const currentDiscount = parseInt(prodDisElement.textContent.replace(/[^\d]/g, ""), 10);
        const totalDiscount = currentDiscount + usedPoints;
        prodDisElement.textContent = `-${totalDiscount.toLocaleString()}원`;

        // 전체 주문 금액에서 포인트 할인 적용
        const currentTotal = parseInt(totalElement.textContent.replace(/[^\d]/g, ""), 10);
        const newTotal = currentTotal - usedPoints;
        totalElement.textContent = `${newTotal.toLocaleString()}원`;
    } else {
        inputPoint.value = "";
    }
}


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
        pointDiscount: parseInt(document.getElementById("prodPoint").textContent.trim().replace(/[^\d]/g, ""), 10),
        couponDiscount: parseInt(document.getElementById("couponDiscount").textContent.trim().replace(/[^\d]/g, ""), 10),
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
