/*
    날짜 : 2024/10/21
    이름 : 최준혁
    내용 : 사이드바 js 파일 생성

    - 서브메뉴 활성화 기능 추가
    - 2024/11/04 이도영 기존 사이드바 기능 관리자와 판매자 별도 기능 수정
*/
document.addEventListener("DOMContentLoaded", function() {

    // 페이지 로드될 때 내가 클릭한 li active 처리
    let pathArray = window.location.pathname.split('/');

    const path1 = pathArray[pathArray.length - 2];
    const path2 = pathArray[pathArray.length - 1];

    // cs 3차분류이기 때문에 따로 처리
    const path3 = pathArray[pathArray.length - 3];

    if(path3 === 'cs'){
        console.log(path1);
        console.log(path2);
        console.log(path3);

        // path1에 해당하는 ol 찾기
        let ol = document.getElementsByClassName(path3)[0]; // .path1 클래스를 가진 ol 찾기
        console.log("ol:", ol);


        if (ol) {
            ol.style.display = 'block';
            ol.classList.add('active');

            let li = ol.getElementsByClassName(path1)[0];
            if (li) {
                li.classList.add('active');
                console.log("li:", li);
            } else {
                console.log("li not found");
            }
        } else {
            console.log("li not found");
        }
    }
    else {
        console.log(path1);
        console.log(path2);

        // path1에 해당하는 ol 찾기
        let ol = document.getElementsByClassName(path1)[0]; // .path1 클래스를 가진 ol 찾기
        console.log("ol:", ol);

        // ol 자식들 중에 path2에 해당하는 li 찾기
        if (ol) {
            ol.style.display = 'block'; // ol 표시
            ol.classList.add('active'); // ol에 active 클래스 추가

            let li = ol.getElementsByClassName(path2)[0]; // ol 자식 중에서 path2 클래스를 가진 li 찾기
            if (li) {
                li.classList.add('active'); // li에 active 클래스 추가
                console.log("li:", li);
            } else {
                console.log("li not found");
            }
        } else {
            console.log("ol not found");
        }
    }


    const sidebarLinks = document.querySelectorAll('#admin_sidebar > li > a');

    sidebarLinks.forEach(link => {
        const subMenu = link.nextElementSibling;

        // 마우스가 들어왔을 때 (서브 메뉴 열기)
        link.addEventListener('mouseenter', function() {
            if (subMenu) {
                subMenu.style.display = 'block';
            }
        });

        // 서브 메뉴에 마우스가 들어갔을 때 유지
        if (subMenu) {
            subMenu.addEventListener('mouseenter', function() {
                subMenu.style.display = 'block';
            });
        }

        // 서브 메뉴 항목 클릭 이벤트 추가
        //2024/11/04 기존 사이드바 기능 관리자와 판매자 별로 기능 수정
        if (subMenu) {
            const subLinks = subMenu.querySelectorAll('li > a');
            subLinks.forEach(subLink => {
                subLink.addEventListener('click', function(event) {
                    event.stopPropagation(); // 클릭 이벤트 전파 방지

                    // 모든 서브 메뉴 항목에서 active 클래스 제거
                    document.querySelectorAll('#admin_sidebar li a.active').forEach(activeLink => {
                        activeLink.classList.remove('active');
                    });

                    // 클릭한 서브 메뉴 항목에 active 클래스 추가
                    subLink.classList.add('active');

                    // 클릭된 메뉴의 부모 메뉴도 active 상태로 유지
                    const parentLink = subLink.closest('li').querySelector('a');
                    parentLink.classList.add('active'); // 부모 메뉴도 active 상태로 설정

                    // 모든 서브 메뉴를 닫기
                    const allSubMenus = document.querySelectorAll('#admin_sidebar ol');
                    allSubMenus.forEach(menu => {
                        menu.style.display = 'none'; // 모든 서브 메뉴 닫기
                        menu.classList.remove('active'); // 모든 서브 메뉴 active 클래스 제거
                    });

                    // 클릭된 서브 메뉴의 부모 메뉴만 열기
                    subMenu.classList.add('active'); // 서브 메뉴에 active 클래스 추가
                    subMenu.style.display = 'block'; // 클릭된 서브 메뉴를 항상 열어 두기
                });
            });
        }

        // a 태그 클릭 이벤트 추가
        link.addEventListener('click', function(event) {
            event.preventDefault();

            // 모든 a 태그에서 active 클래스 제거
            document.querySelectorAll('#admin_sidebar li a.active').forEach(activeLink => {
                activeLink.classList.remove('active');
            });

            // 모든 서브 메뉴를 닫기
            const allSubMenus = document.querySelectorAll('#admin_sidebar ol');
            allSubMenus.forEach(menu => {
                menu.style.display = 'none'; // 모든 서브 메뉴 닫기
                menu.classList.remove('active'); // 모든 서브 메뉴의 active 클래스 제거
            });

            // 현재 클릭된 a 태그에 active 클래스 추가
            link.classList.add('active');

            // 클릭된 a 태그의 서브 메뉴만 열기
            if (subMenu) {
                subMenu.style.display = 'block';
                subMenu.classList.add('active');

                // ol 안의 첫 번째 li > a 클릭한 것처럼 동작
                const firstSubLink = subMenu.querySelector('li > a');
                if (firstSubLink) {
                    firstSubLink.click();  // 첫 번째 a 태그를 자동으로 클릭
                }
            } else {
                // 서브 메뉴가 없는 경우, href로 이동
                window.location.href = link.getAttribute('href'); // 해당 링크로 이동
            }
        });

        // 서브 메뉴가 클릭되지 않은 경우 마우스가 나갔을 때 닫기
        link.addEventListener('mouseleave', function() {
            if (subMenu && !subMenu.classList.contains('active')) {
                subMenu.style.display = 'none';
            }
        });

        // 서브 메뉴에서 마우스가 나갔을 때 닫기
        if (subMenu) {
            subMenu.addEventListener('mouseleave', function() {
                if (!subMenu.classList.contains('active')) {
                    subMenu.style.display = 'none';
                }
            });
        }
    });
});