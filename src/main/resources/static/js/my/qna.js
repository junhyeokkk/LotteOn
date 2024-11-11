document.addEventListener("DOMContentLoaded", function() {
    // 페이지네이션 버튼 생성 함수
    function createPagination(currentPage, totalPages) {
        const paginationContainer = document.querySelector('.pagingList');
        paginationContainer.innerHTML = ''; // 기존 페이지네이션 초기화

        // 이전 버튼
        if (currentPage > 0) {
            paginationContainer.insertAdjacentHTML(
                'beforeend',
                `<a href="#" class="prev" onclick="navigateToPage(${currentPage - 1})"><</a>`
            );
        }

        // 페이지 번호 버튼
        for (let i = 0; i < totalPages; i++) {
            const activeClass = i === currentPage ? 'active' : '';
            paginationContainer.insertAdjacentHTML(
                'beforeend',
                `<a href="#" class="${activeClass}" onclick="navigateToPage(${i})">${i + 1}</a>`
            );
        }

        // 다음 버튼
        if (currentPage < totalPages - 1) {
            paginationContainer.insertAdjacentHTML(
                'beforeend',
                `<a href="#" class="next" onclick="navigateToPage(${currentPage + 1})">></a>`
            );
        }
    }

    // 페이지 이동 함수
    function navigateToPage(pageNumber) {
        const urlParams = new URLSearchParams(window.location.search);
        urlParams.set('pg', pageNumber); // 'pg' 파라미터를 URL에 설정
        window.location.search = urlParams.toString(); // URL 갱신을 통해 페이지 이동
    }

    // 서버에서 초기 데이터 받아오기
    function loadData() {
        const urlParams = new URLSearchParams(window.location.search);
        const currentPage = parseInt(urlParams.get('pg')) || 0;

        fetch(`/myPage/qna?page=${currentPage}`)
            .then(response => response.json())
            .then(data => {
                const inquiries = data.content || [];
                const totalPages = data.totalPages;

                // 문의 항목을 테이블에 렌더링
                const tableBody = document.querySelector('.widthtable tbody');
                tableBody.innerHTML = '';
                inquiries.forEach((inquiry, index) => {
                    tableBody.insertAdjacentHTML(
                        'beforeend',
                        `<tr>
                            <td>${index + 1}</td>
                            <td>${inquiry.type1}</td>
                            <td>${inquiry.type2}</td>
                            <td><a href="/myPage/qna/view/${inquiry.id}">${inquiry.title}</a></td>
                            <td>${new Date(inquiry.createdAt).toLocaleDateString()}</td>
                            <td>${inquiry.answer ? '답변완료' : '검토중'}</td>
                        </tr>`
                    );
                });

                // 페이지네이션 업데이트
                createPagination(currentPage, totalPages);
            })
            .catch(error => console.error('Error loading QNA data:', error));
    }

    // 초기 데이터 로드
    loadData();
});
