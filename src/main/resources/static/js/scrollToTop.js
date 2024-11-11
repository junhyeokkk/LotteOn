// 페이지 상단으로 부드럽게 스크롤하는 함수
function scrollToTop() {
    window.scrollTo({
        top: 0,
        behavior: 'smooth'  // 부드러운 스크롤
    });
}

// 스크롤 이벤트 리스너로 버튼 표시 제어
window.onscroll = function() {
    var scrollTopBtn = document.getElementById("scrollTopBtn");
    var footer = document.querySelector("footer");  // 푸터 요소 선택
    var footerRect = footer.getBoundingClientRect();  // 푸터의 위치 정보

    // 현재 스크롤 위치와 푸터의 위치를 기반으로 버튼 표시 제어
    if ((document.body.scrollTop > 300 || document.documentElement.scrollTop > 300) && footerRect.top > window.innerHeight) {
        scrollTopBtn.classList.add("show");  // 버튼 표시
    } else {
        scrollTopBtn.classList.remove("show");   // 버튼 숨김
    }
};

// DOMContentLoaded 이벤트 리스너 추가
document.addEventListener("DOMContentLoaded", function() {
    // 버튼 클릭 시 스크롤 함수 호출
    document.getElementById("scrollTopBtn").onclick = scrollToTop;
});
