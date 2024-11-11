/*
    날짜 : 2024/11/10
    이름 : 박서홍
    내용 : myPage 사이드바 js 파일 생성
*/


document.addEventListener("DOMContentLoaded", function() {
    // 현재 URL 경로를 배열로 분리
    const pathArray = window.location.pathname.split('/');
    const currentPath = pathArray[pathArray.length - 1];

    // mypage_sidebar의 모든 링크 가져오기
    const sidebarLinks = document.querySelectorAll('#mypage_sidebar > li > a');

    // 현재 URL과 일치하는 링크에 active 클래스 추가
    sidebarLinks.forEach(link => {
        // 링크의 href 속성에서 마지막 경로를 가져옴
        const linkHref = link.getAttribute('href');
        const linkPathArray = linkHref.split('/');
        const linkPath = linkPathArray[linkPathArray.length - 1].split('?')[0]; // 쿼리 파라미터 제거

        // 현재 URL과 링크의 경로가 일치하는 경우
        if (currentPath === linkPath || (linkPath.includes('{uid}') && currentPath === 'coupon')) {
            link.classList.add('active');
        }

        // 클릭 이벤트로 active 클래스 업데이트
        link.addEventListener('click', function(event) {
            // 모든 링크에서 active 클래스 제거
            sidebarLinks.forEach(link => link.classList.remove('active'));

            // 클릭한 링크에 active 클래스 추가
            link.classList.add('active');
        });
    });
});
