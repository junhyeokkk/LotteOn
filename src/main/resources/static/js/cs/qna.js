/*
    날짜 : 2024/10/29
    이름 : 김소희
    내용 : 사이드 메뉴 js 파일 생성
*/

// 특정 카테고리에 따라 QNA 목록을 불러오는 함수
function loadQnasByType(type1, page = 0) {
    document.querySelector('.qna_article h1').textContent = type1;
    document.querySelector('.qna_article h2').textContent = `${type1} 관련 문의내용입니다.`;

    const paginationContainer = document.querySelector('.pagination');

    // Fetch QNA data from the server
    fetch(`/api/cs/qna?type1=${type1}&page=${page}`)
        .then(response => response.json())
        .then(data => {
            // 페이지네이션 버튼 초기화
            paginationContainer.innerHTML = '';

            const pageable = data.pageable || {};
            const currentPage = pageable.pageNumber || 0;
            const totalPages = data.totalPages || 1;

            // 이전 페이지 버튼
            if (currentPage > 0) {
                paginationContainer.insertAdjacentHTML('beforeend', `<a onclick="loadQnasByType('${type1}', ${currentPage - 1})" class="prev">‹ 이전</a>`);
            }

            // 페이지 번호 버튼
            for (let pageNum = 0; pageNum < totalPages; pageNum++) {
                const activeClass = pageNum === currentPage ? 'active' : '';
                paginationContainer.insertAdjacentHTML('beforeend', `<a onclick="loadQnasByType('${type1}', ${pageNum})" class="${activeClass}">${pageNum + 1}</a>`);
            }

            // 다음 페이지 버튼
            if (currentPage < totalPages - 1) {
                paginationContainer.insertAdjacentHTML('beforeend', `<a onclick="loadQnasByType('${type1}', ${currentPage + 1})" class="next">다음 ›</a>`);
            }
        })
        .catch(error => console.error('Error fetching QNA data:', error));
}

// 페이지 로드 시 기본 QNA 목록 로드
document.addEventListener("DOMContentLoaded", function() {
    const urlParams = new URLSearchParams(window.location.search);
    const type1 = urlParams.get('type1') || '회원';
    loadQnasByType(type1); // 기본 카테고리 설정
});
