// 상세정보 열기/닫기 함수
function toggleDetails() {
    const detailTable = document.querySelector('.detail-table');
    const icon = document.querySelector('.toggle-icon');

    // 테이블 표시 상태를 토글
    if (detailTable.style.display === "none" || detailTable.style.display === "") {
        detailTable.style.display = "table";
        icon.textContent = "▲";
    } else {
        detailTable.style.display = "none";
        icon.textContent = "▼";
    }
}
document.addEventListener('DOMContentLoaded', () => {
    let currentIndex = 0;
    const imgContainer = document.querySelector('.totalImageBox');
    const prevButton = document.querySelector('.sliderPrev');
    const nextButton = document.querySelector('.sliderNext');

    // 서버에서 메인 배너 이미지 가져오기
    fetch('/api/banner/product')
        .then(response => response.json())
        .then(banners => {
            imgContainer.innerHTML = ''; // 기존 이미지 제거
            banners.forEach(banner => {
                const bannerLink = document.createElement('a');
                bannerLink.href = banner.backgroundLink || '#';
                bannerLink.innerHTML = `<img class="totalImageBox__img" src="${banner.img}" alt="${banner.name}" />`;
                imgContainer.appendChild(bannerLink);
            });
            updateImage(); // 초기 이미지를 슬라이더로 설정
        })
        .catch(error => console.error('Error fetching main banners:', error));

    // 이미지 업데이트 함수
    function updateImage() {
        imgContainer.style.transform = `translateX(-${currentIndex * 100}%)`;
    }

    // 다음 버튼 클릭 이벤트
    nextButton.addEventListener('click', () => {
        currentIndex = (currentIndex + 1) % imgContainer.childElementCount;
        updateImage();
    });

    // 이전 버튼 클릭 이벤트
    prevButton.addEventListener('click', () => {
        currentIndex = (currentIndex - 1 + imgContainer.childElementCount) % imgContainer.childElementCount;
        updateImage();
    });

    // 자동 슬라이드 기능 (4초마다)
    setInterval(() => {
        nextButton.click();
    }, 4000);
});