
    // 리뷰 팝업
    function reviewPop(prodNo, no, price) {
    const popReview = $('#popReview');
    popReview.empty();

    console.log(popReview);

    let receiveFlag = false;
    // 구매를 확정한 상태인지 (product_order) ordstatus가 1인지 2인지
    $.ajax({
    url: '/LotteON/my/checkRecive',
    type: 'GET',
    data: {"no" :no},
    async: false,
    success: function(data) {
    console.log("data : "+data);
        if(data == 1){
        receiveFlag = false;
    } else if(data == 2) {
        receiveFlag = true;
    }
    console.log("receiveFlag : "+receiveFlag);
}
})
    if (!receiveFlag) {
    alert('구매확정을 먼저 해주세요.');
    location.reload();
    return false;
}

    let flag = false;
    // 리뷰 등록 여부 확인
    $.ajax({
        url: '/LotteON/my/checkReview',
        type: 'GET',
        data: {
            "prodNo" :prodNo,
            "ordno" :no
        },
        async: false,
        success: function(data) {
            console.log("data : "+data);
            if(data == 0){
                flag = true;
            } else if(data >= 1) {
                flag = false;
            }
            console.log("flag : "+flag);

        }

    })

    if (!flag) {
    alert('이미 리뷰가 등록되어 있습니다! 다른상품을 선택해주세요');
    return false;
}

    $.ajax({
    url: '/LotteON/my/checkProduct',
    type: 'GET',
    data: {"prodNo" :prodNo},
    success: function(data) {
    console.log("data : "+data);
    popReview.append(`
            <div>
                <nav>
                    <h1>상품평 작성하기</h1>
                    <button class="btnClose">X</button>
                </nav>
                <section>
                        <table>
                            <tr>
                                <th>상품명</th>
                                <td class="productName">${data.prodName}</td>
                            </tr>
                            <tr>
                                <th>만족도</th>
                                <td class="rating">
                                    <div class="my-rating"></div>
                                </td>
                            </tr>
                            <tr>
                                <th>내용</th>
                                <td class="review">
                                    <textarea name="review" placeholder="내용입력"></textarea>
                                </td>
                            </tr>
                        </table>
                        <p>
                            ※ 상품평을 작성하시면 구매확정 포인트와 더불어 추가 포인트를 지급합니다.
                        </p>

                        <div>
                            <button class="btnPositive" onclick="review(${prodNo},${price},${no})">작성완료</button>
                            <button class="btnNegative btnCancel">취소</button>
                        </div>
                </section>
            </div>
        `);

    // 팝업 닫기
    $('.btnClose').click(function(){
    $(this).closest('.popup').removeClass('on');
});
    // 팝업 닫기
    $('.btnCancel').click(function(){
    $(this).closest('.popup').removeClass('on');
});

    // 상품평 작성 레이팅바 기능
    $(".my-rating").starRating({
    starSize: 20,
    useFullStars: true,
    strokeWidth: 0,
    useGradient: false,
    minRating: 1,
    ratedColors: ['#ffa400', '#ffa400', '#ffa400', '#ffa400', '#ffa400'],
    callback: function(currentRating, $el){
    rating = currentRating;
    console.log('DOM element ', $el);
        }
    });
    }
})
    popReview.addClass('on');
}





    function receivePop(no) {
    const popReceive = $('#popReceive');
    popReceive.empty();
    let flag = false;
    // 리뷰 등록 여부 확인
    $.ajax({
    url: '/LotteON/my/checkRecive',
    type: 'GET',
    data: {"no" :no},
    async: false,
    success: function(data) {
        console.log("data : "+data);
        if(data == 1){
            flag = true;
        } else if(data == 2) {
            flag = false;
        }
}
})
    if (!flag) {
    alert('이미 구매 확정한 상품입니다.');
    return location.reload();
}
    popReceive.append(`
        <div>
            <nav>
                <h1>수취확인</h1>
                <button class="btnClose">X</button>
            </nav>
            <section>
                <p>
                    상품을 잘 받으셨나요?<br>
                    상품을 받으셨으면 수취확인을 눌러 구매확정을 진행하세요.<br>
                    구매확정 후 포인트를 지급해 드립니다.
                </p>

                <div>
                    <button class="btnPositive btnConfirm" onclick="receive(${no})">확인</button>
                    <button class="btnNegative btnCancel">취소</button>
                </div>
            </section>
        </div>
    `);
    // 팝업 닫기
    $('.btnClose').click(function(){
    $(this).closest('.popup').removeClass('on');
});
    // 팝업 닫기
    $('.btnCancel').click(function(){
    $(this).closest('.popup').removeClass('on');
});
    popReceive.addClass('on');
}

    // 리뷰
    function review(prodNo,price,no) {
    let reviewText = $('textArea[name=review]').val();
    const jsonData = {
    "prodNo" : prodNo,
    "content" : reviewText,
    "rating" : rating,
    "price" : price,
        "no" : no
}
    $.ajax({
    url:  '/LotteON/my/reviewRegister',
    type: 'POST',
    data: jsonData,
    success: function(data) {
    alert("리뷰가 작성 되었습니다.")
    $('.btnClose').closest('.popup').removeClass('on');
}
})
}

    // 수취확인
    function receive(no) {
    $.ajax({
        url:  '/LotteON/my/updateOrdstatus',
        type: 'POST',
        data: {"no" :no},
        success: function(data) {
            console.log(data);
            alert('구매확정 되었습니다.');
            $('.btnClose').closest('.popup').removeClass('on');
        },
        error:function (data){
            alert('오류가 발생 하였습니다. 잠시 후 다시 시도해주세요.');
            $('.btnClose').closest('.popup').removeClass('on');
        }

        })
    }

    function seller(seller,ordUid){
    console.log(ordUid);
    const popSeller=$('#popSeller');
    popSeller.empty();
    let popflag = false;

    console.log(seller);
    $.ajax({
        url: '/LotteON/my/seller',
        type: 'POST',
        data: {"seller": seller},
        success: function (data) {

            console.log(data)
            popSeller.append(`
            <div>
                <nav>
                    <h1>판매자 정보</h1>
                    <button class="btnClose">X</button>
                </nav>
                <section>
                    <table border="0">
                        <tr>
                            <th>판매자 등급</th>
                            <td class="level">파워딜러</td>
                        </tr>
                        <tr>
                            <th>상호</th>
                            <td class="company">${data.company}</td>
                        </tr>
                        <tr>
                            <th>대표자</th>
                            <td class="ceo">${data.ceo}</td>
                        </tr>
                        <tr>
                            <th>전화번호</th>
                            <td class="tel">${data.tel}</td>
                        </tr>
                        <tr>
                            <th>FAX</th>
                            <td class="fax">${data.fax}</td>
                        </tr>
                        <tr>
                            <th>Email</th>
                            <td class="email">${data.email}</td>
                        </tr>
                        <tr>
                            <th>사업자번호</th>
                            <td class="bizNum">${data.bizRegNum}</td>
                        </tr>
                        <tr>
                            <th>영업소재지</th>
                            <td class="address">
                                [${data.zip}] ${data.addr1} ${data.addr2}
                            </td>
                        </tr>
                    </table>
                    <p>
                        ※ 위 정보는 전자상거래법 제 20조 제 2항 및 동법 시행령 제 25조 제 2항에 의거하여<br>
                        소비자보호에 의거 공개합니다.
                    </p>

                    <div>
                        <button class="btnPositive btnQuestion" onclick="writeQna('${ordUid}')">문의하기</button>
                    </div>

                </section>
            </div>
        `);

        }

        })
        $('.btnClose').closest('.popup').removeClass('on');
        popSeller.addClass('on');
 }

 function writeQna(ordUid){
    const popQuestion = $('#popQuestion');
    popQuestion.empty();

    $.ajax({
        url: '/LotteON/my/writeQna',
        type: 'POST',
        data: {"ordUid": ordUid},
        success: function (data) {
            console.log(data)
            popQuestion.append(`
        
            <div>
                <nav>
                    <h1>문의하기</h1>
                    <button class="btnClose">X</button>
                </nav>
                <section>
                    <form action="/LotteON/my/insertQna" method="post">
                        <table border="0">
                            <tr>
                                <th>문의종류</th>
                                <td class="kind">
                                    <label><input type="radio" name="kind" value="oproduct"/>상품</label>
                                    <label><input type="radio" name="kind" value="dinfo"/>배송</label>
                                    <label><input type="radio" name="kind" value="return"/>반품/취소</label>
                                    <label><input type="radio" name="kind" value="minfo"/>교환</label>
                                    <label><input type="radio" name="kind" value="change"/>기타</label>
                                </td>
                            </tr>
                            <tr>
                                <th>Email</th>
                                <td class="email">
                                    <input type="text" name="email" value="${data}"/>
                                </td>
                            </tr>
                            <tr>
                                <th>제목</th>
                                <td class="title">
                                    <input type="text" name="title" placeholder="제목입력"/>
                                </td>
                            </tr>
                            <tr>
                                <th>내용</th>
                                <td class="content">
                                    <textarea name="content" placeholder="내용입력"></textarea>
                                </td>
                            </tr>
                        </table>
                        <p>
                            ※ 정확한 정보를 입력하셔야 빠른 답변을 받으실수 있습니다.
                        </p>

                        <div>
                            <input type="hidden" name="uid" value="${ordUid}">
                            <input type="submit" class="btnPositive" value="등록하기"/>
                            <button class="btnNegative btnCancel">취소</button>
                        </div>
                    </form>
                </section>
            </div> `)

        }

    })

     $('.btnClose').closest('.popup').removeClass('on');
     popQuestion.addClass('on');


}
// 상품정보 ajax 로 받아서 팝업띄울거임
function product(itemNo){
    console.log(itemNo);// 상품번호
    const popOrder = $('#popOrder');
    popOrder.empty();

    $.ajax({
        url: '/LotteON/my/orderSelect',
        type: 'POST',
        data: {"itemNo": itemNo},
        success: function (data) {
            console.log(data)
            popOrder.append(`
            <div>
                <nav>
                    <h1>주문상세</h1>
                    <button class="btnClose">X</button>
                </nav>
                <section>
                    <article class="order">
                        <h3>주문정보</h3>
                        <table border="0">
                            <tr>
                                <th>날짜</th>
                                <th>상품정보</th>
                                <th>결제금액</th>
                                <th>상태</th>
                            </tr>
                            <tr>
                                <td>
                                    <p class="date">${data.ordDate}</p>
                                    주문번호 : <span class="ordNo" >${data.ordNo}</span>
                                </td>
                                <td>
                                    <img src="/LotteON/thumbs/${data.prodCate1}/${data.prodCate2}/${data.thumb1}" alt=""/>
                                    <ul>
                                        <li class="company" >${data.company}</li>
                                        <li class="prodName" >${data.prodName}</li>
                                        <li>수량 : <span class="prodCount" >${data.count}</span>개</li>
                                        <li class="prodPrice" >${data.price}원</li>
                                    </ul>
                                </td>
                                <td class="payment">
                                    <ol>
                                        <li class="price">
                                            <span>판매가</span>
                                            <span >${data.price}원</span>
                                        </li>
                                        <li class="discount">
                                            <span>할인</span>
                                            <span > - ${data.price*(data.discount*0.01)}원</span>
                                        </li>
                                        <li class="total">
                                            <span>결제금액</span>
                                            <span >${data.total}원</span>
                                        </li>
                                    </ol>
                                </td>
                                <td class="status">배송완료</td>
                            </tr>
                        </table>
                    </article>
                    <article class="delivery">
                        <h3>배송정보</h3>
                        <table border="0">
                            <tr>
                                <th>주문자</th>
                                <td class="name" >${data.recipName}</td>
                            </tr>
                            <tr>
                                <th>연락처</th>
                                <td class="hp" >${data.recipHp}</td>
                            </tr>
                            <tr>
                                <th>배송지</th>
                                <td class="address">[${data.recipZip}] ${data.recipAddr1} ${data.recipAddr2}</td>
                            </tr>
                            <tr>
                                <th>배송요청사항</th>
                                <td class="request">없음</td>
                            </tr>
                        </table>
                    </article>
                </section>
            </div> `)

        }
    })
    $('.btnClose').closest('.popup').removeClass('on');
    popOrder.addClass('on');
}
