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
    let totalImages = 0;
    let banners = [];

    // 서버에서 메인 배너 이미지 가져오기
    fetch('/api/banner/product')
        .then(response => response.json())
        .then(fetchedBanners => {
            banners = fetchedBanners;
            totalImages = banners.length;

            // 첫 번째 이미지를 표시
            displayImage(currentIndex);
        })
        .catch(error => console.error('Error fetching main banners:', error));

    // 이미지 표시 함수
    function displayImage(index) {
        imgContainer.innerHTML = ''; // 기존 이미지를 제거
        const banner = banners[index];
        const bannerLink = document.createElement('a');
        bannerLink.href = banner.backgroundLink || '#';
        bannerLink.innerHTML = `<img class="totalImageBox__img" src="${banner.img}" alt="${banner.name}" />`;
        imgContainer.appendChild(bannerLink);
    }

    // 다음 이미지로 이동
    nextButton.addEventListener('click', () => {
        currentIndex = (currentIndex + 1) % totalImages;
        displayImage(currentIndex);
    });

    // 이전 이미지로 이동
    prevButton.addEventListener('click', () => {
        currentIndex = (currentIndex - 1 + totalImages) % totalImages;
        displayImage(currentIndex);
    });

    // 자동 슬라이드 기능 (4초마다)
    setInterval(() => {
        nextButton.click();
    }, 4000);
});
