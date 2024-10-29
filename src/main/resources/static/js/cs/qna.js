/*
    날짜 : 2024/10/29
    이름 : 김소희
    내용 : 사이드 메뉴 js 파일 생성
*/

// 특정 카테고리에 따라 QNA 목록을 불러오는 함수
function loadQnasByType(type, page = 0) {
    // h1 및 h2 요소 업데이트
    document.querySelector('.qna_article h1').textContent = type;
    document.querySelector('.qna_article h2').textContent = `${type} 관련 문의내용 입니다.`;

    // QNA 데이터 가져오기
    fetch(`/api/cs/qna/list/${type}?page=${page}`)
        .then(response => response.json())
        .then(data => {
            let qnaContainer = document.querySelector('.qna_article table tbody');
            let paginationContainer = document.querySelector('.pagingList');

            qnaContainer.innerHTML = ''; // 기존 QNA 항목 초기화

            // QNA 항목 추가
            data.content.forEach(qna => {
                qnaContainer.innerHTML += `
                    <tr>
                        <td><a href="/cs/layout/qna/view/${qna.id}">Q. ${qna.title}</a></td>
                        <td class="${qna.answer ? 'done' : 'ing'}">${qna.answer ? '답변 완료' : '검토 중'}</td>
                        <td>${qna.memberId ? qna.memberId : '비회원'}</td>
                        <td style="text-align: right;">${new Date(qna.createdAt).toLocaleDateString()}</td>
                    </tr>
                `;
            });

            // 페이지네이션 버튼 추가
            paginationContainer.innerHTML = ''; // 기존 페이지네이션 버튼 초기화
            if (!data.first) {
                paginationContainer.innerHTML += `<a href="javascript:void(0);" onclick="loadQnasByType('${type}', ${data.pageable.pageNumber - 1})" class="prev">‹</a>`;
            }

            for (let pageNum = 0; pageNum < data.totalPages; pageNum++) {
                paginationContainer.innerHTML += `<a href="javascript:void(0);" onclick="loadQnasByType('${type}', ${pageNum})" class="${pageNum === data.number ? 'active' : ''}">${pageNum + 1}</a>`;
            }

            if (!data.last) {
                paginationContainer.innerHTML += `<a href="javascript:void(0);" onclick="loadQnasByType('${type}', ${data.pageable.pageNumber + 1})" class="next">›</a>`;
            }
        })
        .catch(error => console.error('Error fetching QNA data:', error));
}

// 페이지 로드 시 기본 QNA 목록 로드 (첫 카테고리로 예시)
document.addEventListener("DOMContentLoaded", function() {
    loadQnasByType('회원'); // '회원' 카테고리 FAQ 로드
});
