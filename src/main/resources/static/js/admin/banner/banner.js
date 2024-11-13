/*
    날짜 : 2024/10/22
    이름 : 최준혁
    내용 : 배너 js 파일 생성
*/
window.onload =function (){

    const sizeInput = document.querySelector('.bannersize'); // 사이즈 입력란 선택
    const linkInput = document.querySelector('.bannerlink'); // 링크 입력란 선택
    const titleElement = document.querySelector('.banner_title'); // h3 요소 선택
    const bannerPlaceSelect = document.querySelector('.bannerplace'); // select 요소 선택
    const buttons = document.querySelectorAll('.bannerinsertbtn button'); // 모든 버튼 요소 선택

    let filteredBanners = []; // 현재 포지션에 맞는 배너들을 저장할 변수
    let currentPage = 1; // 현재 페이지
    const itemsPerPage = 5; // 페이지당 배너 수

    // 초기화
    sizeInput.value = '1200 x 80';
    linkInput.value = 'http://localhost:8080/';
    titleElement.textContent = '메인 상단 배너';

    function closeModal() {
        document.getElementById('bannerModal').style.display = 'none'; // Hide the modal
    }

    // 각 버튼에 클릭 이벤트 추가
    buttons.forEach(button => {
        // 기존 리스너가 있을 경우 제거
        button.removeEventListener('click', handleButtonClick);
        button.addEventListener('click', handleButtonClick);

    });

    // 페이지 로드 시 모든 배너를 불러오고 로컬 스토리지에 저장
    fetchBanners();


    function handleButtonClick() {
        const position = this.className;
        const banners = JSON.parse(localStorage.getItem('banners')) || [];

        // 해당 포지션의 배너만 필터링
        filteredBanners = banners.filter(banner => banner.position === position);

        // 타이틀 업데이트
        titleElement.textContent = this.textContent;

        // 페이지네이션 적용
        currentPage = 1; // 페이지 리셋
        paginateBanners(filteredBanners);
    }


    // select 요소에 change 이벤트 추가
    bannerPlaceSelect.addEventListener('change', function () {
        const selectedValue = this.value;
        switch (selectedValue) {
            case 'Main':
                sizeInput.value = '1200 x 80';
                linkInput.value = 'http://localhost:8080/';
                titleElement.textContent = '메인 상단 배너';
                break;
            case 'Silder':
                sizeInput.value = '1920 x 500';
                linkInput.value = 'http://localhost:8080/';
                titleElement.textContent = '메인 슬라이더 배너';
                break;
            case 'Product':
                sizeInput.value = '300 x 300';
                linkInput.value = 'http://localhost:8080/product/view';
                titleElement.textContent = '상품 상세보기 배너';
                break;
            case 'Login':
                sizeInput.value = '400 x 200';
                linkInput.value = 'http://localhost:8080/member/login';
                titleElement.textContent = '회원 로그인 배너';
                break;
            case 'Mypage':
                sizeInput.value = '500 x 250';
                linkInput.value = 'http://localhost:8080/mypage/home';
                titleElement.textContent = '마이 페이지 배너';
                break;
            default:
                sizeInput.value = '';
                linkInput.value = '';
                titleElement.textContent = '';
        }
    });

    // 배너 데이터를 가져오는 함수
    function fetchBanners() {
        fetch('/admin/config/api/banner')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok: ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                console.log('Fetched data:', data);
                if (!Array.isArray(data.banners)) {
                    throw new Error('Banners is not an array');
                }
                localStorage.setItem('banners', JSON.stringify(data.banners));

                // 페이지 로드 시 Main 포지션 배너만 필터링하여 표시
                filteredBanners = data.banners.filter(banner => banner.position === 'Main');
                paginateBanners(filteredBanners); // Main 배너만 페이지네이션

            })
            .catch(error => {
                console.error('Error fetching banners:', error);
                alert('배너를 가져오는 중 오류가 발생했습니다.');
            });
    }


    // 배너를 화면에 표시하는 함수
    function displayBanners(banners) {
        const bannerList = document.querySelector('.admin_list');
        bannerList.innerHTML = ''; // 초기화

        if (banners.length === 0) {
            bannerList.innerHTML = '<tr><td colspan="10">배너가 없습니다.</td></tr>';
            return;
        }

        banners.forEach(banner => {
            const row = `
            <tr>
                <td><input type="checkbox" class="banner-checkbox" data-id="${banner.id}" /></td>
                <td>${banner.id}</td>
                <td><img src="${banner.img}" class="thumb" style="width: 70px; height: 70px;"></td>
                <td style="text-align: left;">
                    <p>${banner.name}</p>
                    <p>크기 : ${banner.size}</p>
                    <p>배경색 : ${banner.backgroundColor}</p>
                    <p>${banner.backgroundLink}</p>
                </td>
                <td>${banner.position}</td>
                <td>${formatDate(banner.displayStartDate)}</td>
                <td>${formatDate(banner.displayEndDate)}</td>
                <td>${banner.displayStartTime}</td>
                <td>${banner.displayEndTime}</td>
                <td>
                    <a href="#" class="toggle-active" data-id="${banner.id}" data-active="${banner.isActive}">
                        ${banner.isActive === 1 ? '활성' : '비활성'}
                    </a>
                </td>
            </tr>
        `;
            bannerList.insertAdjacentHTML('beforeend', row);
        });

        // 활성화/비활성 토글 이벤트 추가
        document.querySelectorAll('.toggle-active').forEach(button => {
            button.addEventListener('click', function(event) {
                event.preventDefault();
                const bannerId = this.getAttribute('data-id');
                const currentState = parseInt(this.getAttribute('data-active'), 10);

                // 서버에 활성화 상태를 업데이트 요청
                toggleBannerActive(bannerId, currentState === 1 ? 0 : 1).then(success => {
                    if (success) {
                        this.textContent = currentState === 1 ? '비활성' : '활성';
                        this.setAttribute('data-active', currentState === 1 ? '0' : '1');
                    } else {
                        alert("상태 변경에 실패했습니다.");
                    }
                });
            });
        });
    }
    // 서버에 활성화 상태 업데이트 요청
    function toggleBannerActive(bannerId, newState) {
        return fetch(`/admin/config/api/banners/${bannerId}/toggleActive`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ isActive: newState })
        })
            .then(response => response.ok)
            .catch(error => {
                console.error("Error updating banner active state:", error);
                return false;
            });
    }

    // 페이지네이션 함수
    function paginateBanners(banners) {
        const totalPages = Math.ceil(banners.length / itemsPerPage);
        const startIndex = (currentPage - 1) * itemsPerPage;
        const endIndex = startIndex + itemsPerPage;

        // 현재 페이지에 해당하는 배너들만 표시
        const currentBanners = banners.slice(startIndex, endIndex);
        displayBanners(currentBanners);

        // 페이지 번호 표시
        displayPageNumbers(totalPages);
        updatePaginationButtons(currentPage, totalPages);
    }


    // 페이지네이션을 위한 초기 호출
    const banners = JSON.parse(localStorage.getItem('banners'));
    if (banners) {
        paginateBanners(banners);
    }


    // 페이지 번호 표시 및 클릭 이벤트
    function displayPageNumbers(totalPages) {
        const pagingList = document.querySelector('.pagingList');
        pagingList.innerHTML = ''; // 기존 페이지 번호 제거

        // 이전 버튼 추가
        const prevButton = `<a href="#" class="prev" id="prevBtn"><</a>`;
        pagingList.insertAdjacentHTML('beforeend', prevButton);

        // 페이지 번호 추가 (페이지 클릭 이벤트 포함)
        for (let i = 1; i <= totalPages; i++) {
            const pageLink = `<a href="#" class="${i === currentPage ? 'active' : ''}" data-page="${i}">${i}</a>`;
            pagingList.insertAdjacentHTML('beforeend', pageLink);
        }

        // 다음 버튼 추가
        const nextButton = `<a href="#" class="next" id="nextBtn">></a>`;
        pagingList.insertAdjacentHTML('beforeend', nextButton);

        // 페이지 번호 클릭 이벤트 추가
        pagingList.querySelectorAll('a[data-page]').forEach(link => {
            link.addEventListener('click', (event) => {
                event.preventDefault(); // 링크 기본 동작 방지
                currentPage = parseInt(event.target.getAttribute('data-page'));
                paginateBanners(filteredBanners); // 페이지 이동 후 다시 페이지네이션 호출
            });
        });

        // 이전 버튼 클릭 이벤트 추가
        document.getElementById('prevBtn').addEventListener('click', (event) => {
            event.preventDefault(); // 기본 동작 방지
            if (currentPage > 1) {
                currentPage--;
                paginateBanners(filteredBanners); // 이전 페이지로 이동
            }
        });

        // 다음 버튼 클릭 이벤트 추가
        document.getElementById('nextBtn').addEventListener('click', (event) => {
            event.preventDefault(); // 기본 동작 방지
            const totalPages = Math.ceil(filteredBanners.length / itemsPerPage);
            if (currentPage < totalPages) {
                currentPage++;
                paginateBanners(filteredBanners); // 다음 페이지로 이동
            }
        });
    }


    // 페이징 이전, 다음 버튼
    function updatePaginationButtons(currentPage, totalPages) {
        const prevButton = document.querySelector('#prevBtn'); // 이전 버튼
        const nextButton = document.querySelector('#nextBtn'); // 다음 버튼

        // 첫 페이지일 경우 '<' 버튼 숨김
        if (currentPage === 1) {
            prevButton.style.display = 'none';
        } else {
            prevButton.style.display = 'inline-block'; // 첫 페이지가 아니면 다시 표시
        }

        // 마지막 페이지일 경우 '>' 버튼 숨김
        if (currentPage === totalPages) {
            nextButton.style.display = 'none';
        } else {
            nextButton.style.display = 'inline-block'; // 마지막 페이지가 아니면 다시 표시
        }
    }

    // 날짜 포맷 함수
    function formatDate(date) {
        const d = new Date(date);
        return `${d.getFullYear()}/${String(d.getMonth() + 1).padStart(2, '0')}/${String(d.getDate()).padStart(2, '0')}`;
    }

    // 시간 포맷 함수
    function formatTime(time) {
        const t = new Date(time);
        return `${String(t.getHours()).padStart(2, '0')}:${String(t.getMinutes()).padStart(2, '0')}`;
    }

    // 모달 취소 버튼
    document.getElementById('bannermodalclose').addEventListener('click', (event) => {
        event.preventDefault(); // 링크 기본 동작 방지
        closeModal();
    });

    // 배너등록
    const form = document.getElementById('bannerForm');
    const submitButton = form.querySelector('.adminmodalbutton');

    // 중복 제출 방지 플래그
    let isSubmitting = false;

    submitButton.addEventListener('click', (event) => {
        event.preventDefault();  // 기본 폼 제출 동작 방지
        event.stopPropagation();  // 이벤트 전파 방지

        console.log('ddddddddddddddddddddddddddddddddddddd');
        // 중복 제출 방지
        if (isSubmitting) {
            return;  // 이미 제출 중이면 아무 작업도 하지 않음
        }
        isSubmitting = true;  // 제출 플래그 설정

        // 시간 값 가져오기
        const startHour = form.querySelector('select[name="hour"]').value;
        const startMinute = form.querySelector('select[name="minute"]').value;
        const endHour = form.querySelector('select[name="endHour"]').value;
        const endMinute = form.querySelector('select[name="endminute"]').value;

        // 시간 형식 설정
        const displayStartTime = `${startHour}:${startMinute}`;
        const displayEndTime = `${endHour}:${endMinute}`;

        // 폼에 숨겨진 시간 필드 설정
        form.querySelector('#displayStartTime').value = displayStartTime;
        form.querySelector('#displayEndTime').value = displayEndTime;

        // FormData 객체 생성
        const formData = new FormData(form);

        // 서버에 데이터 전송
        fetch('/admin/config/banner', {
            method: 'POST',
            body: formData,
        })
            .then(response => {
                if (response.ok) {
                    alert('배너 등록 완료');
                    closeModal();  // 모달 닫기 함수 호출
                    location.reload();
                } else {
                    alert('배너 등록 실패');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('배너 등록 에러');
            })
            .finally(() => {
                isSubmitting = false;  // 제출 플래그 리셋
            });
    });

    // 모달 닫기 함수
    function closeModal() {
        document.getElementById('bannerModal').style.display = 'none'; // Hide the modal
    }
};
// 전체 선택
function toggleSelectAll(source) {
    const checkboxes = document.querySelectorAll('.admin_list input[type="checkbox"]');
    checkboxes.forEach(checkbox => checkbox.checked = source.checked);
}

// 선택된 배너 삭제 기능
function deleteSelectedBanners() {
    const selectedCheckboxes = document.querySelectorAll('.admin_list .banner-checkbox:checked');
    const selectedIds = Array.from(selectedCheckboxes).map(checkbox => checkbox.getAttribute('data-id'));

    if (selectedIds.length > 0) {
        fetch('/admin/config/api/banners/delete', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ ids: selectedIds }) // 선택된 ID를 서버로 전송
        })
            .then(response => {
                if (response.ok) {
                    alert(`${selectedIds.length}개의 배너가 삭제되었습니다.`);
                    // 삭제된 행을 화면에서 제거
                    selectedCheckboxes.forEach(checkbox => checkbox.closest('tr').remove());
                    location.reload();
                } else {
                    alert("배너 삭제에 실패했습니다.");
                }
            })
            .catch(error => {
                console.error("Error deleting banners:", error);
                alert("삭제 요청 중 오류가 발생했습니다.");
            });
    } else {
        alert("삭제할 배너를 선택하세요.");
    }
}
