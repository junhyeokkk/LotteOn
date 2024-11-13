/*
    날짜 : 2024/10/29
    이름 : 김소희
    내용 : 사이드 메뉴 js 파일 생성
*/

/*
    날짜 : 2024/10/29
    이름 : 김소희
    내용 : 사이드 메뉴 js 파일 생성
*/

// "간단히 보기" 및 "더 보기" 기능
function toggleFaqView(type2, viewType) {

    const faqCategory = document.querySelector(`.faq-category[data-type2="${type2}"]`);
    const faqItems = faqCategory.querySelectorAll('.faq-item');
    const countSpan = faqCategory.querySelector('.more-view .count');

    if (viewType === 'simple') {
        faqItems.forEach((item, index) => {
            item.style.display = index < 3 ? 'block' : 'none';
        });
        // "간단히 보기" 상태에서는 count를 다시 보이도록 설정
        if (countSpan) {
            countSpan.style.visibility = 'visible';
        }
    } else if (viewType === 'more') {
        faqItems.forEach((item, index) => {
            item.style.display = index < 10 ? 'block' : 'none';
        });
        // "더 보기" 상태에서는 count를 숨김
        if (countSpan) {
            countSpan.style.visibility = 'hidden';
        }
    }
    document.addEventListener("DOMContentLoaded", () => {
        const sidebarLinks = document.querySelectorAll(".cs_sidebar > ul > li > a");
        const currentUrl = window.location.pathname + window.location.search;

        sidebarLinks.forEach(link => {
            const linkUrl = new URL(link.href);
            const linkPath = linkUrl.pathname + linkUrl.search;

            // 현재 URL과 링크 URL이 일치할 경우 `active` 클래스 추가
            if (currentUrl === linkPath) {
                link.classList.add("active");
            }
        });
    });


}
