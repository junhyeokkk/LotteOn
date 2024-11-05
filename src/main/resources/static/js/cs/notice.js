/*
    날짜 : 2024/10/29
    이름 : 김소희
    내용 : 카테고리 js 파일 생성
*/

// 특정 카테고리에 따라 Notice 목록을 불러오는 함수
function loadNoticesByType(type1, page = 0) {
    document.querySelector('.no_article h1').textContent = type1;
    document.querySelector('.no_article h2').textContent = `${type1} 관련 공지사항입니다.`;

    fetch(`/api/cs/notice/list?type1=${type1}&page=${page}`)
        .then(response => response.json())
        .then(data => {
            const noticeContainer = document.querySelector('.notice-section');
            const paginationContainer = document.querySelector('.pagination');

            // 공지사항 목록 초기화
            noticeContainer.innerHTML = '';

            // 공지사항 항목 추가
            if (data.content && Array.isArray(data.content)) {
                data.content.forEach(notice => {
                    noticeContainer.insertAdjacentHTML('beforeend', `
                        <tr>
                            <td><a href="/cs/layout/notice/view/${notice.id}">${notice.title}</a></td>
                            <td style="text-align: right;">${new Date(notice.createdAt).toLocaleDateString('ko-KR')}</td>
                        </tr>
                    `);
                });
            } else {
                noticeContainer.innerHTML = '<tr><td colspan="2">공지사항이 없습니다.</td></tr>';
            }

            // 페이지네이션 버튼 초기화
            paginationContainer.innerHTML = '';

            const pageable = data.pageable || {};
            const currentPage = pageable.pageNumber || 0;
            const totalPages = data.totalPages || 1;

            // 이전 페이지 버튼
            if (currentPage > 0) {
                paginationContainer.insertAdjacentHTML('beforeend', `<a onclick="loadNoticesByType('${type1}', ${currentPage - 1})" class="prev">‹ 이전</a>`);
            }

            // 페이지 번호 버튼
            for (let pageNum = 0; pageNum < totalPages; pageNum++) {
                const activeClass = pageNum === currentPage ? 'active' : '';
                paginationContainer.insertAdjacentHTML('beforeend', `<a onclick="loadNoticesByType('${type1}', ${pageNum})" class="${activeClass}">${pageNum + 1}</a>`);
            }

            // 다음 페이지 버튼
            if (currentPage < totalPages - 1) {
                paginationContainer.insertAdjacentHTML('beforeend', `<a onclick="loadNoticesByType('${type1}', ${currentPage + 1})" class="next">다음 ›</a>`);
            }
        })
        .catch(error => console.error('Error fetching Notice data:', error));
}

// 페이지 로드 시 기본 Notice 목록 로드
document.addEventListener("DOMContentLoaded", function() {
    urlSearch = new URLSearchParams(location.search);
    userName = urlSearch.get('type1')
    console.log(userName)
    userName = userName??"전체"
    loadNoticesByType(userName); // 기본 카테고리 설정
});
