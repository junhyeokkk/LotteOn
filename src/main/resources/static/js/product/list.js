/*
    날짜 : 2024/10/28
    이름 : 최준혁
    내용 : 메인 상품 list js 파일 생성
*/
window.onload = function () {
    setCateCodeInputValue();
    let currentURL = window.location.href;
    let splitURL = currentURL.split('?')[1];
    let sortCode = ""
    let sortText = '';
    if (splitURL !== undefined) {
        let params = new URLSearchParams(window.location.search);
        sortCode = params.get('sort');
    }
    const sortNav = document.getElementsByClassName('sort')[0];
    const mainSortNav = document.getElementsByClassName('mainSort')[0];
    const aTag = sortNav.querySelectorAll('a');
    const mTag = sortNav.querySelectorAll('a');

    switch (sortCode) {
        case 'prodSold':
            aTag[0].classList.add('navOn');
            sortText = ' > 판매많은순';
            break;
        case 'prodLowPrice':
            aTag[1].classList.add('navOn');
            sortText = ' > 낮은가격순';
            break;
        case 'prodHighPrice':
            aTag[2].classList.add('navOn');
            sortText = ' > 높은가격순';
            break;
        case 'prodScore':
            aTag[3].classList.add('navOn');
            sortText = ' > 평점높은순';
            break;
        case 'prodReview':
            aTag[4].classList.add('navOn');
            sortText = ' > 후기많은순';
            break;
        case 'prodRdate':
            aTag[5].classList.add('navOn');
            sortText = ' > 최근등록순';
            break;
        case 'prodHit-main':
            sortText = ' > 히트상품';
            break;
        case 'prodScore-main':
            sortText = ' > 추천상품';
            break;
        case 'prodRdate-main':
            sortText = ' > 최신상품';
            break;
        case 'prodSold-main':
            sortText = ' > 인기상품';
            break;
        case 'prodDiscount-main':
            sortText = ' > 할인상품';
            break;
        default:
            sortText = '';
    }

    document.getElementById('sortText').innerText = sortText;

}

function sort(sortType) {
    const currentUrl = new URL(window.location.href);
    currentUrl.searchParams.set('sortBy', sortType);
    window.location.href = currentUrl.toString();
}

function page(num) {
    const currentUrl = new URL(window.location.href);
    currentUrl.searchParams.set('page', num);
    currentUrl.searchParams.set('size', 10);
    window.location.href = currentUrl.toString();
}

// 상품 상태 확인
function checkProdStatus(event) {
    const button = event.target;
    const prod = button.closest('a');
    if (prod.className === "판매중단") {
        event.preventDefault();
        alert("판매 중단된 상품입니다.");
    }
}