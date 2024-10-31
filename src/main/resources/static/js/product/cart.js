/*
    날짜 : 2024/10/29
    이름 : 최준혁
    내용 : 카트 js 파일 생성
*/
window.onload = function () {
    document.getElementById("orderForm").addEventListener("submit", function (event) {
        event.preventDefault();

        // 선택된 장바구니 항목 수집
        const selectedItems = Array.from(document.querySelectorAll('input[name="selectedCart"]:checked'))
            .map(item => item.value);  // 체크박스의 value에 cartId가 들어간다고 가정

        // 서버로 주문 요청
        fetch("/api/product/prepareOrder", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({selectedCartIds: selectedItems})
        })
            .then(response => {
                if (response.ok) {
                    // 성공 시 주문 페이지로 이동
                    window.location.href = "/product/order";
                } else {
                    alert("주문 준비에 실패했습니다.");
                }
            })
            .catch(error => console.error("Error:", error));
    });
}

function calculateTotalPrice() {
    // 초기화
    let totalQuantity = 0;
    let totalPrice = 0;
    let totalDiscount = 0;
    let totalDelivery = 0;
    let totalPoint = 0;
    let totalFinalPrice = 0;

    // 모든 체크된 상품 요소 가져오기
    const selectedItems = document.querySelectorAll('input[name="selectedCart"]:checked');

    console.log('ddd' + selectedItems.length);
    // 선택된 각 상품 정보 합산
    selectedItems.forEach(item => {
        const quantity = parseInt(item.getAttribute("data-quantity"));
        console.log('quantity' + quantity);
        const price = parseInt(item.getAttribute("data-price"));
        const discountRate = parseInt(item.getAttribute("data-discount"));
        const deliveryFee = parseInt(item.getAttribute("data-delivery")) || 0; // 무료배송은 0
        const point = parseInt(item.getAttribute("data-point"));
        const totalItemPrice = parseInt(item.getAttribute("data-total"));

        // 상품 수량, 가격, 배송비, 포인트, 총합 계산
        totalQuantity += quantity;
        totalPrice += price * quantity;
        totalDiscount += (price * quantity - totalItemPrice); // 원가 - 할인된 금액
        totalDelivery += deliveryFee;
        totalPoint += point;
        totalFinalPrice += totalItemPrice;
    });

    // HTML에 각 값 업데이트
    document.getElementById("prodCount").textContent = totalQuantity;
    document.getElementById("prodOrgPrice").textContent = totalPrice.toLocaleString() + "원";
    document.getElementById("prodDisPrice").textContent = "-" + totalDiscount.toLocaleString() + "원";
    document.getElementById("prodDelivery").textContent = totalDelivery.toLocaleString() + "원";
    document.getElementById("prodPoint").textContent = totalPoint;
    document.getElementById("prodTotalPrice").textContent = totalFinalPrice.toLocaleString() + "원";
}
