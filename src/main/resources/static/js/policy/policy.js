    function toggleContent(element) {
    const content = element.nextElementSibling;
    const button = element.querySelector('.toggle-icon');  // 정확한 이미지 선택

    if (content.style.display === "none" || content.style.display === "") {
    content.style.display = "block";
    button.src = "/images/policy/minus.png";  // 접기 이미지로 변경
    button.alt = "접기";  // 대체 텍스트 변경
} else {
    content.style.display = "none";
    button.src = "/images/policy/plus.png";  // 펼치기 이미지로 변경
    button.alt = "펼치기";  // 대체 텍스트 변경
}
}


    function printPage() {
    window.print();  // 브라우저의 인쇄 기능을 호출
}

    // 페이지 상단으로 즉시 스크롤하는 함수
    function scrollToTop() {
    window.scrollTo(0, 0);  // 최상단으로 즉시 이동
}

    // 스크롤 이벤트 리스너로 버튼 표시 제어
    window.onscroll = function() {
    var scrollTopBtn = document.getElementById("scrollTopBtn");
    var footer = document.querySelector("footer");  // 푸터 요소 선택
    var footerRect = footer.getBoundingClientRect();  // 푸터의 위치 정보

    // 현재 스크롤 위치와 푸터의 위치를 기반으로 버튼 표시 제어
    if ((document.body.scrollTop > 300 || document.documentElement.scrollTop > 300) && footerRect.top > window.innerHeight) {
    scrollTopBtn.style.display = "block";  // 버튼 표시
} else {
    scrollTopBtn.style.display = "none";   // 버튼 숨김
}
};
