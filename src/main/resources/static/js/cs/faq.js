/*
    날짜 : 2024/10/29
    이름 : 김소희
    내용 : 사이드 메뉴 js 파일 생성
*/

// 특정 카테고리에 따라 FAQ 목록을 불러오고 URL을 업데이트하는 함수
// function loadFaqsByType(type1, type2 = '', page = 0) {
//     // URL 변경
//     if (type2) {
//         history.pushState({}, '', `/cs/layout/faq/list?type1=${type1}&type2=${type2}`);
//     } else {
//         history.pushState({}, '', `/cs/layout/faq/list?type1=${type1}`);
//     }
//
//     // 카테고리 제목 및 설명 업데이트
//     document.querySelector('.faq_article h1').textContent = type1;
//     document.querySelector('.faq_article h2').textContent = type2 ? `${type1} - ${type2} 관련 자주 묻는 질문입니다.` : `${type1} 관련 자주 묻는 질문입니다.`;
//
//     // FAQ 데이터를 서버에서 가져옴
//     fetch(`/api/cs/faq/list?type1=${type1}&type2=${type2}&page=${page}`)
//         .then(response => response.json())
//         .then(data => {
//             const faqContainer = document.querySelector('.faq-section');
//             faqContainer.innerHTML = '';
//
//             // FAQ 항목 추가
//             Object.entries(data).forEach(([type2Key, faqs]) => {
//                 const categoryDiv = document.createElement('div');
//                 categoryDiv.classList.add('faq-category');
//                 categoryDiv.dataset.type2 = type2Key;
//
//                 // 첫 3개의 FAQ만 표시
//                 const initialFaqItems = faqs.slice(0, 3)
//                     .map(faq => `
//                         <div class="faq-item">
//                             <a href="/cs/layout/faq/view/${faq.id}" class="question">Q. ${faq.title}</a>
//                         </div>
//                     `).join('');
//
//                 categoryDiv.innerHTML = `
//                     <h2>${type2Key}</h2>
//                     ${initialFaqItems}
//                     <button class="simple-view" onclick="toggleFaqView('${type2Key}', 'simple')">간단히 보기</button>
//                     <button class="more-view" onclick="toggleFaqView('${type2Key}', 'more')">더 보기 <span class="count">${Math.min(faqs.length, 10)}</span></button>
//                 `;
//                 faqContainer.appendChild(categoryDiv);
//             });
//         })
//         .catch(error => console.error('Error fetching FAQ data:', error));
// }

// "간단히 보기" 및 "더 보기" 기능
function toggleFaqView(type2, viewType) {

    const faqCategory = document.querySelector(`.faq-category[data-type2="${type2}"]`);
    const faqItems = faqCategory.querySelectorAll('.faq-item');

    if (viewType === 'simple') {
        faqItems.forEach((item, index) => {
            item.style.display = index < 3 ? 'block' : 'none';
        });
    } else if (viewType === 'more') {
        faqItems.forEach((item, index) => {
            item.style.display = index < 10 ? 'block' : 'none';
        });
    }


}
