/*
    날짜 : 2024/10/22
    이름 : 최준혁
    내용 : 카테고리 js 파일 생성

    수정이력
        - 2024/10/28 이상훈 - 카테고리 crud

*/


// 드래그 앤 드롭 기능
const categoryList = document.getElementById('categoryList');
let draggedElement = null; // 드래그한 요소를 저장할 변수

// 드래그 시작
categoryList.addEventListener('dragstart', function (event) {
    console.log("dragstart")
    const target = event.target.closest('[data-category-id]'); // 가장 가까운 드래그 가능한 요소 찾기
    if (target) {
        draggedElement = target; // 드래그할 요소를 저장
        event.dataTransfer.effectAllowed = 'move';
        draggedElement.classList.add('dragging'); // 드래그 클래스 추가
    }
});

// 드래그 오버
categoryList.addEventListener('dragover', function (event) {
    event.preventDefault();
    event.dataTransfer.dropEffect = 'move';
});

// 드래그 종료
categoryList.addEventListener('dragleave', function (event) {
    const target = event.target.closest('.category-item, .subcategory-item'); // 가장 가까운 드래그 가능한 요소 찾기
    if (target) {
        target.classList.remove('dragging');
    }
});

// 드롭
categoryList.addEventListener('drop', async function (event) {
    event.preventDefault();
    const targetElement = event.target.closest('[data-category-id]');
    if (!targetElement || !draggedElement || targetElement === draggedElement) return;

    const cateId = draggedElement.dataset.categoryId;
    const targetId = targetElement.dataset.categoryId;

    try {
        const response = await fetch(`/api/cate/${cateId}/displayOrder/${targetId}`, {
            method: 'PATCH'
        });

        if (response.ok) {
            swapElements(draggedElement, targetElement)
        } else {
            alert("카테고리 순서 변경에 실패했습니다.");
        }
    } catch (e) {
        alert("카테고리 순서 변경에 실패했습니다.");
        console.error("Change DisplayOrder Error: ", e);
    } finally {
        draggedElement.classList.remove('dragging');
        draggedElement = null;
    }
});


// 카테고리 열기/닫기 기능 (1차 및 2차 카테고리)
document.getElementById('categoryList').addEventListener('click', function (event) {
    // 1차 카테고리 토글
    const categoryHeader = event.target.closest('.category-header');
    if (categoryHeader) {
        const subcategoryList = categoryHeader.parentNode.querySelector(".subcategory-list")
        const addBtn = categoryHeader.parentNode.querySelector(".add-btn.cate2");
        const toggleIcon = categoryHeader.querySelector('.toggle-icon');

        let totalHidden;

        if (subcategoryList) {
            const isHidden = subcategoryList.style.display === 'none' || subcategoryList.style.display === '';
            subcategoryList.style.display = isHidden ? 'block' : 'none';
            totalHidden = isHidden;
        }

        if (addBtn) {
            const isHidden = addBtn.style.display === 'none' || addBtn.style.display === '';
            addBtn.style.display = isHidden ? 'block' : 'none';
            totalHidden = isHidden
        }


        // 서브 카테고리 보이기/숨기기
        // 토글 아이콘 변경
        toggleIcon.textContent = totalHidden ? '▼' : '▶';
    }

    // 2차 카테고리 토글
    const subcategoryHeader = event.target.closest('.subcategory-header');
    if (subcategoryHeader) {
        const tertiaryCategoryList = subcategoryHeader.parentNode.querySelector(".tertiary-category-list")
        const addBtn = subcategoryHeader.parentNode.querySelector(".add-btn.add-tertiary-btn");


        const toggleIcon = subcategoryHeader.querySelector('.toggle-icon');
        let totalHidden;

        if (tertiaryCategoryList) {
            const isHidden = tertiaryCategoryList.style.display === 'none' || tertiaryCategoryList.style.display === '';
            tertiaryCategoryList.style.display = isHidden ? 'block' : 'none';
            totalHidden = isHidden
        }

        if (addBtn) {
            const isHidden = addBtn.style.display === 'none' || addBtn.style.display === '';
            addBtn.style.display = isHidden ? 'block' : 'none';
            totalHidden = isHidden
        }


        // 3차 카테고리 보이기/숨기기
        tertiaryCategoryList.style.display = totalHidden ? 'block' : 'none';

        // 토글 아이콘 변경
        toggleIcon.textContent = totalHidden ? '▼' : '▶';
    }
});


// 페이지 로드 시 서브 카테고리 숨기기
document.querySelectorAll('.subcategory-list').forEach(list => list.style.display = 'none');

// 1차 카테고리 추가 기능
document.getElementById('addCategoryBtn').addEventListener('click', async function () {
    const newCategoryName = document.getElementById('newCategoryInput').value.trim();
    if (newCategoryName === '') {
        alert('카테고리 이름을 입력하세요.');
        return;
    }
    try {
        const response = await fetch('/api/cate',
            {
                method: 'post',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    name: newCategoryName,
                    level: 1,
                })
            })

        if(!response.ok)
        {
            alert("카테고리 생성에 실패했습니다.")
            return;
        }


        const data = await response.json();
        console.log(data)
        // 새로운 1차 카테고리 HTML 요소
        const newCategoryItem = document.createElement('li');
        newCategoryItem.classList.add('category-item');
        newCategoryItem.dataset.categoryId = data.id;
        newCategoryItem.draggable=true;
        newCategoryItem.innerHTML = `
                <div class="category-header">
                    <span class="toggle-icon">▶</span>
                    <span>${newCategoryName}</span>
                    <button class="delete-btn cate1-delete">삭제</button>
                </div>
                 <ul class="subcategory-list"></ul>
                <button class="add-btn cate2" style="display: none">+ 2차 카테고리 추가</button>
                <div class="add-subcategory-section" style="display: none;">
                    <input type="text" class="newSubcategoryInput" placeholder="2차 카테고리 이름을 입력하세요" />
                    <button class="add-subcategory-btn">+ 추가</button>
                </div>
            `;

        // 1차 카테고리 리스트에 추가
        document.getElementById('categoryList').appendChild(newCategoryItem);


        newCategoryItem.querySelector('.add-btn.cate2').addEventListener('click', function (e) {
            const addSubcategorySection = e.target.closest('.category-item').querySelector('.add-subcategory-section');
            addSubcategorySection.style.display = addSubcategorySection.style.display === 'none' ? 'block' : 'none';
        });

    } catch (e) {
        alert("카테고리 생성에 실패했습니다.");
        console.error("Category Create Error:", e);
    } finally {
        // 입력 필드 비우기
        document.getElementById('newCategoryInput').value = '';
    }
});


// 페이지 로드 시 기존 .add-btn 버튼에 클릭 이벤트 리스너 추가
document.querySelectorAll('.add-btn.cate2').forEach(addBtn => {
    addBtn.addEventListener('click', function () {
        // .subcategory-list의 다음 형제 요소가 아니라, 현재 addBtn이 속한 category-item 내의 .add-subcategory-section을 찾습니다.
        const addSubcategorySection = addBtn.closest('.category-item').querySelector('.add-subcategory-section');
        addSubcategorySection.style.display = addSubcategorySection.style.display === 'none' ? 'block' : 'none';
    });
});

// 3차
// 페이지 로드 시 기존 .add-btn 버튼에 클릭 이벤트 리스너 추가
document.querySelectorAll('.add-btn.add-tertiary-btn').forEach(addBtn => {
    addBtn.addEventListener('click', function () {
        // .subcategory-list의 다음 형제 요소가 아니라, 현재 addBtn이 속한 category-item 내의 .add-subcategory-section을 찾습니다.
        const addTertiarySection = addBtn.closest('.subcategory-item').querySelector('.add-tertiary-section');
        addTertiarySection.style.display = addTertiarySection.style.display === 'none' ? 'block' : 'none';
    });
});


// 각 2차 카테고리 추가 버튼에 클릭 이벤트 추가
document.addEventListener('click', async function (event) {
    if (event.target.classList.contains('add-subcategory-btn')) {
        const subcategoryInput = event.target.previousElementSibling;
        const subcategoryName = subcategoryInput.value.trim();
        const item = event.target.closest(".category-item");
        const categoryId = item.dataset.categoryId;

        if (subcategoryName === '') {
            alert('2차 카테고리 이름을 입력하세요.');
            return;
        }

        try {


            const response = await fetch("/api/cate", {
                method: 'post',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    name: subcategoryName,
                    level: 2,
                    parentId: categoryId
                }),
            })

            if(!response.ok)
            {
                alert("카테고리 생성에 실패했습니다.")
                return;
            }

            const data = await response.json();

            // 새로운 2차 카테고리 HTML 요소
            const newSubcategoryItem = document.createElement('li');
            newSubcategoryItem.classList.add('subcategory-item');
            newSubcategoryItem.dataset.categoryId = data.id
            newSubcategoryItem.draggable=true;
            newSubcategoryItem.innerHTML = `
            <div class="subcategory-header">
                <span class="toggle-icon">▶</span>
                <span>${subcategoryName}</span>
                <button class="delete-btn cate2-delete">삭제</button>
            </div>
            <ul class="tertiary-category-list" style="display:none;"></ul>
            <button class="add-btn add-tertiary-btn" style="display: none">+ 3차 카테고리 추가</button>
            <div class="add-tertiary-section" style="display: none;">
                <input type="text" class="newTertiaryInput" placeholder="3차 카테고리 이름을 입력하세요"/>
                <button class="add-tertiary-btn cate3">+ 추가</button>
            </div>
        `;

            // 해당 서브 카테고리 리스트에 추가
            const subcategoryList = event.target.closest('.category-item').querySelector('.subcategory-list');
            subcategoryList.appendChild(newSubcategoryItem)

            const addBtn = newSubcategoryItem.querySelector('.add-btn.add-tertiary-btn');
            addBtn.addEventListener('click', function () {
                // .subcategory-list의 다음 형제 요소가 아니라, 현재 addBtn이 속한 category-item 내의 .add-subcategory-section을 찾습니다.
                const addTertiarySection = addBtn.closest('.subcategory-item').querySelector('.add-tertiary-section');
                addTertiarySection.style.display = addTertiarySection.style.display === 'none' ? 'block' : 'none';
            });


            // // 3차 카테고리 토글 기능 추가
            // const subcategoryHeader = newSubcategoryItem.querySelector('.subcategory-header');
            // subcategoryHeader.addEventListener('click', function () {
            //
            //     const tertiaryCategoryList = subcategoryHeader.querySelector(".tertiary-category-list");
            //     const addBtn = subcategoryHeader.querySelector(".add-btn.add-tertiary-btn");
            //
            //     const toggleIcon = this.querySelector('.toggle-icon');
            //
            //     let totalHidden;
            //
            //     console.log(addBtn)
            //
            //     if(tertiaryCategoryList != null)
            //     {
            //     const isHidden = tertiaryCategoryList.style.display === 'none' || tertiaryCategoryList.style.display === '';
            //     tertiaryCategoryList.style.display = isHidden ? 'block' : 'none';
            //     totalHidden = isHidden;
            //     }
            //
            //     if(addBtn != null)
            //     {
            //         const isHidden = addBtn.style.display === 'none' || tertiaryCategoryList.style.display === '';
            //         addBtn.style.display = isHidden ? 'block' : 'none';
            //         totalHidden = isHidden;
            //     }
            //
            //     // 3차 카테고리 보이기/숨기기
            //
            //     // 토글 아이콘 변경
            //     toggleIcon.textContent = totalHidden ? '▼' : '▶';
            // });
        } catch (e) {
            alert("2차 카테고리 생성에 실패했습니다.");
            console.error("Create 2Category Error: ", e);
        } finally {
            // 입력 필드와 버튼 숨기기
            const addSubcategorySection = event.target.closest('.add-subcategory-section');
            addSubcategorySection.style.display = 'none';

            // 입력 필드 비우기
            subcategoryInput.value = '';
        }
    }

    if (event.target.classList.contains('add-tertiary-btn') && event.target.classList.contains('cate3')) {

        const subcategoryInput = event.target.previousElementSibling;
        const subcategoryName = subcategoryInput.value.trim();
        const item = event.target.closest(".subcategory-item");
        const categoryId = item.dataset.categoryId;

        if (subcategoryName === '') {
            alert('3차 카테고리 이름을 입력하세요.');
            return;
        }

        try{
            const response = await fetch("/api/cate", {
                method: 'post',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    name: subcategoryName,
                    level: 3,
                    parentId: categoryId
                }),
            });

            if(!response.ok)
            {
                alert("카테고리 생성에 실패했습니다.")
                return;
            }
            
            const data = await response.json();


            // 새로운 3차 카테고리 HTML 요소
            const newSubcategoryItem = document.createElement('li');
            newSubcategoryItem.dataset.categoryId=data.id;
            newSubcategoryItem.classList.add('tertiary-category-item');
            newSubcategoryItem.draggable=true;
            newSubcategoryItem.innerHTML = `
           <li class="tertiary-category-item" draggable="true">
                <span>${subcategoryName}</span>
                <button class="delete-btn cate3-delete">삭제</button>
           </li>
        `;

            // 해당 서브 카테고리 리스트에 추가
            const subcategoryList = event.target.closest('.subcategory-item').querySelector('.tertiary-category-list');
            subcategoryList.appendChild(newSubcategoryItem);
        }catch (e) {
            alert("3차 카테고리 생성에 실패했습니다.");
            console.error("Create 2Category Error: ", e);
        }finally {
            // 입력 필드와 버튼 숨기기
            const addSubcategorySection = event.target.closest('.add-tertiary-section');
            addSubcategorySection.style.display = 'none';

            // 입력 필드 비우기
            subcategoryInput.value = '';
        }


    }
});


// 카테고리 및 서브 카테고리 삭제 기능 추가
document.addEventListener('click', async function (event) {
    // 1차 카테고리 삭제
    if (event.target.classList.contains('delete-btn') && event.target.classList.contains('cate1-delete')) {
        const categoryItem = event.target.closest('.category-item');
        const categoryId = categoryItem.dataset.categoryId;
        const conf = confirm(`해당 1차 카테고리를 정말 삭제하시겠습니까?`);
        if (!conf) {
            return;
        }

        try {
           const response = await fetch(`/api/cate/${categoryId}`,
                {
                    method: "delete"
                })

            if (!response.ok)
            {
                alert("카테고리 삭제에 실패했습니다.")
                return;
            }                
            

            if (categoryItem) {
                categoryItem.remove(); // 해당 .category-item 삭제
            }
        } catch (e) {
            alert("카테고리 삭제에 실패했습니다.")
            console.error("Category Delete Error:", e);
        }
    }
    // 2차 카테고리 삭제
    if (event.target.classList.contains('delete-btn') && event.target.classList.contains('cate2-delete')) {
        const subcategoryItem = event.target.closest('.subcategory-item');
        const categoryId = subcategoryItem.dataset.categoryId;
        const conf = confirm(`해당 2차 카테고리를 정말 삭제하시겠습니까?`);
        if (!conf) {
            return;
        }

        try {
            const response = await fetch(`/api/cate/${categoryId}`,
                {
                    method: "delete"
                })

            if(!response.ok)
            {
                alert("카테고리 삭제에 실패했습니다.")
                return;
            }

            if (subcategoryItem) {
                subcategoryItem.remove(); // 해당 .subcategory-item 삭제
            }
        } catch (e) {
            alert("카테고리 삭제에 실패했습니다.")
            console.error("Category Delete Error:", e);
        }
    }

    // 2차 카테고리 삭제
    if (event.target.classList.contains('delete-btn') && event.target.classList.contains('cate3-delete')) {
        const subcategoryItem = event.target.closest('.tertiary-category-item');
        const categoryId = subcategoryItem.dataset.categoryId;
        const conf = confirm(`해당 3차 카테고리를 정말 삭제하시겠습니까?`);
        if (!conf) {
            return;
        }

        try {
            const response = await fetch(`/api/cate/${categoryId}`,
                {
                    method: "delete"
                })
            
            if(!response.ok)
            {
                alert("카테고리 삭제에 실패했습니다.")
                return;
            }
            
            if (subcategoryItem) {
                subcategoryItem.remove(); // 해당 .subcategory-item 삭제
            }
        } catch (e) {
            alert("카테고리 삭제에 실패했습니다.")
            console.error("Category Delete Error:", e);
        }
    }
});

function swapElements(element1, element2) {
    // 두 요소가 부모를 갖고 있는지 확인 (필수)
    if (!element1 || !element2 || !element1.parentNode || !element2.parentNode) {
        console.error("두 요소 중 하나에 부모가 없습니다.");
        return;
    }

    // element1이 element2 앞에 위치하면 element2 뒤에 element1을 삽입
    // element2가 element1 앞에 위치하면 element1 뒤에 element2를 삽입하여 위치를 교환
    const sibling1 = element1.nextSibling;
    const sibling2 = element2.nextSibling;
    const parent1 = element1.parentNode;
    const parent2 = element2.parentNode;

    // 각 부모 요소에 상대적 위치에 삽입하여 위치 교환
    if (sibling1 === element2) {
        parent1.insertBefore(element2, element1);
    } else if (sibling2 === element1) {
        parent2.insertBefore(element1, element2);
    } else {
        parent1.insertBefore(element2, sibling1);
        parent2.insertBefore(element1, sibling2);
    }
}