/*
    날짜 : 2024/10/29
    이름 : 김소희
    내용 : 사이드 메뉴 js 파일 생성
*/

// 특정 카테고리에 따라 FAQ 목록을 불러오는 함수
function loadNoticesByType(type, page = 0) {
    try{
        // h1 및 h2 요소 업데이트
        document.querySelector('.no_article h1').textContent = type;
        document.querySelector('.no_article h2').textContent = `${type} 관련 자주 묻는 질문입니다.`;
    }catch (e){}


    // Notice 데이터를 서버에서 가져옴
    fetch(`/api/cs/notice/list/${type}?page=${page}`)
        .then(response => response.json())
        .then(data => {
            let noticeContainer = document.querySelector('.notice-section');
            let paginationContainer = document.querySelector('.pagination');

            // 기존 FAQ 항목 초기화
            noticeContainer.innerHTML = '';

            // FAQ 항목 추가
            data.content.forEach(faq => {
                noticeContainer.innerHTML += `
                    <div class="notice-item">
                        <a href="/cs/layout/notice/view/${faq.id}" class="question">Q. ${notice.title}</a>
                    </div>
                `;
            });

            // 페이지네이션 버튼 추가
            paginationContainer.innerHTML = ''; // 기존 페이지네이션 버튼 초기화
            if (!data.first) {
                paginationContainer.innerHTML += `<button onclick="loadNoticesByType('${type}', ${data.pageable.pageNumber - 1})">Previous</button>`;
            }
            for (let pageNum = 0; pageNum < data.totalPages; pageNum++) {
                paginationContainer.innerHTML += `<button onclick="loadNoticesByType('${type}', ${pageNum})" class="${pageNum === data.pageable.pageNumber ? 'active' : ''}">${pageNum + 1}</button>`;
            }
            if (!data.last) {
                paginationContainer.innerHTML += `<button onclick="loadNoticesByType('${type}', ${data.pageable.pageNumber + 1})">Next</button>`;
            }
        })
        .catch(error => console.error('Error fetching Notice data:', error));
}

// 페이지 로드 시 기본 FAQ 목록 로드 (첫 카테고리로 예시)
document.addEventListener("DOMContentLoaded", function() {
    loadFaqsByType('전체'); // '전체' 카테고리 전체 로드
});
