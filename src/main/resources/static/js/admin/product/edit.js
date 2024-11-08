// 옵션 이벤트 리스너 초기화 함수
function initializeOptionEventListeners() {
    document.querySelectorAll(".option-name, .option-type, .option-tag span").forEach(element => {
        element.removeEventListener("change", updateOptionAndRefreshCombinations);
        element.removeEventListener("input", updateOptionAndRefreshCombinations);

        element.addEventListener("change", updateOptionAndRefreshCombinations);
        element.addEventListener("input", updateOptionAndRefreshCombinations);
    });
}
const categoryCache = {};

function loadCategories() {
    fetch('/api/cate/root')
        .then(response => response.json())
        .then(data => {
            const category1Select = document.getElementById("category1");
            data.forEach(category => {
                const option = document.createElement("option");
                option.value = category.id;
                option.textContent = category.name;
                category1Select.appendChild(option);
            });
        })
        .catch(error => console.error("Error loading categories:", error));
}

function loadSubCategories(parentId, targetSelectId) {
    const category2SelectId = 'category2';
    const category3SelectId = 'category3';

    if (targetSelectId === category1) {
        clearSelectOptions(category2SelectId);
        clearSelectOptions(category3SelectId);
    } else if (targetSelectId === category2SelectId) {
        clearSelectOptions(category3SelectId);
    }

    if (categoryCache[parentId]) {
        populateSelectOptions(targetSelectId, categoryCache[parentId]);
    } else {
        fetch(`/api/cate/${parentId}/subcategories`)
            .then(response => response.json())
            .then(data => {
                categoryCache[parentId] = data;
                populateSelectOptions(targetSelectId, data);
            })
            .catch(error => console.error("Error loading subcategories:", error));
    }
}
// 페이지 로드 시 초기화
document.addEventListener("DOMContentLoaded", () => {
    loadCategories();
    initializeOptionEventListeners();
});

// 탭 전환 함수
function showTab(tabId) {
    document.querySelectorAll('.tab-content').forEach(content => content.style.display = 'none');
    document.getElementById(tabId).style.display = 'block';
}

// 할인 가격 계산 함수
function calculateDiscount() {
    const price = parseFloat(document.getElementById("price").value) || 0;
    const discountRate = parseFloat(document.getElementById("discountRate").value) || 0;
    const discountedPrice = price * (1 - discountRate / 100);
    document.getElementById("discountedPrice").textContent = discountedPrice.toFixed(2);
}

// 옵션값 추가 함수 (Enter 키 입력 시 추가)
function addOptionValueOnEnter(event, input) {
    if (event.key === "Enter") {
        event.preventDefault(); // 기본 엔터 동작 방지
        const value = input.value.trim();

        if (value) {
            const valuesContainer = input.closest('.option-values');

            if (valuesContainer) {
                const newTag = document.createElement('span');
                newTag.className = 'option-tag';
                newTag.innerHTML = `<span>${value}</span> <button type="button" onclick="removeOptionValue(this)" class="remove-value-btn">x</button>`;

                // 새로운 옵션값 태그를 input 필드 앞에 추가
                valuesContainer.insertBefore(newTag, input);
                input.value = '';  // 입력 필드 초기화
                updateCombinationTable();  // 옵션 값이 추가되었으므로 조합 테이블 업데이트
            }
        }
    }
}

// 옵션 삭제 시 조합 테이블 갱신
function removeOptionRow(button) {
    const row = button.closest("tr");
    if (row) row.remove();
    handleOptionChange();  // 옵션 행이 제거되었으므로 조합 업데이트
}

// 옵션 값 삭제 시 조합 테이블 갱신
function removeOptionValue(button) {
    const tag = button.parentElement; // button의 부모 요소(span)
    if (tag) {
        tag.remove();
        updateCombinationTable(); // 옵션 값 삭제 시 조합 테이블 업데이트
    }
}

// 옵션 행 추가 함수
function addOptionRow() {
    const tableBody = document.querySelector("#optionTable tbody");
    const newRow = document.createElement("tr");
    newRow.innerHTML = `
        <td>
            <select class="option-type" onchange="handleOptionChange()">
                <option value="기본">기본</option>
                <option value="입력형">입력형</option>
                <option value="색상">색상</option>
            </select>
        </td>
        <td><input type="text" class="option-name" onchange="handleOptionChange()" /></td>
        <td>
            <div class="option-values">
                <input type="text" placeholder="옵션값 추가 (예: S, M, L)" onkeypress="addOptionValueOnEnter(event, this)" class="add-option-value-input" />
            </div>
        </td>
        <td><button type="button" onclick="removeOptionRow(this)">X</button></td>
    `;
    tableBody.appendChild(newRow);
    handleOptionChange(); // 옵션 행 추가 시 조합 테이블 업데이트
}

// 이미지 미리보기 함수
function previewImage(event, imgId) {
    const reader = new FileReader();
    reader.onload = () => document.getElementById(imgId).src = reader.result;
    reader.readAsDataURL(event.target.files[0]);
}

/* 상품 업데이트 관련 메서드 */
// 상품 수정 FormData 생성
function createProductUpdateFormData(productId) {
    const formData = new FormData();

    const productDTO = {
        id: productId,
        productName: document.getElementById('productName').value,
        description: document.getElementById('description').value,
        manufacturer: document.getElementById('manufacturer').value,
        price: document.getElementById('price').value,
        discountRate: document.getElementById('discountRate').value,
        point: document.getElementById('point').value,
        stock: document.getElementById('stockCount').value,
        deliveryFee: document.getElementById('deliveryFee').value,
        status: document.getElementById('status').value,
        warranty: document.getElementById('warranty').value,
        receiptIssued: document.getElementById('receiptIssued').value,
        businessType: document.getElementById('businessType').value,
        origin: document.getElementById('origin').value,
        categoryId: document.getElementById('category3').value,
    };


    formData.append("productDTO", new Blob([JSON.stringify(productDTO)], { type: "application/json" }));
    formData.append("options", JSON.stringify(prepareOptionData()));
    formData.append("combinations", JSON.stringify(prepareCombinationData()));

    // 이미지 파일 추가
    addImageToFormData(formData, 'productImg1');
    addImageToFormData(formData, 'productImg2');
    addImageToFormData(formData, 'productImg3');

    return formData;
}

// 이미지 파일을 FormData에 추가
function addImageToFormData(formData, imageId) {
    const imageFile = document.getElementById(imageId).files[0];
    if (imageFile) {
        formData.append(imageId, imageFile);
    }
}

// 상품 수정 요청 전송 메서드
function updateProduct() {
    const id = document.querySelector("input[name='id']").value;
    const formData = createProductUpdateFormData(id);

    fetch(`/api/admin/product/update/${id}`, {
        method: "PUT",
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.text();
        })
        .then(data => {
            alert("상품이 성공적으로 수정되었습니다.");
            window.location.href = "/admin/product/list";
        })
        .catch(error => console.error("Error:", error));
}

// 옵션 데이터를 가져오는 함수
function prepareOptionData() {
    const options = [];
    document.querySelectorAll("#optionTable tbody tr").forEach(optionRow => {
        const optionType = optionRow.querySelector(".option-type").value;
        const optionName = optionRow.querySelector(".option-name").value;
        const optionValues = Array.from(optionRow.querySelectorAll(".option-tag span"))
            .map(valueSpan => valueSpan.textContent);

        if (optionName && optionValues.length > 0) {
            options.push({
                type: optionType,
                name: optionName,
                values: optionValues
            });
        }
    });
    return options;
}

// 옵션 조합 데이터를 수집하여 서버 전송 형식으로 준비
function prepareCombinationData() {
    const combinations = [];
    document.querySelectorAll("#combinationTableBody tr").forEach(row => {
        const combinationText = row.querySelector("td").textContent;
        const stock = row.querySelector(".combination-stock").value;
        const status = row.querySelector(".status-select").value;

        combinations.push({
            combinationText,
            stock: parseInt(stock, 10),
            status
        });
    });
    return combinations;
}

// 모든 조합 생성
function getCombinations(options) {
    if (options.length === 0) return [];

    const [firstOption, ...remainingOptions] = options;
    const restCombinations = getCombinations(remainingOptions);

    const combinations = [];
    firstOption.values.forEach(value => {
        if (restCombinations.length > 0) {
            restCombinations.forEach(combination => {
                combinations.push([{ name: firstOption.name, value }, ...combination]);
            });
        } else {
            combinations.push([{ name: firstOption.name, value }]);
        }
    });
    return combinations;
}

// 기존 행 업데이트 함수
function createStatusSelect(existingStatusValue = 'SALE') {
    const statusOptions = ["SALE", "SOLDOUT", "STOP"];
    const select = document.createElement('select');
    select.className = 'status-select';

    statusOptions.forEach(status => {
        const option = document.createElement('option');
        option.value = status;
        option.textContent = status === "SALE" ? "판매중" : status === "SOLDOUT" ? "품절" : "판매중지";
        if (status === existingStatusValue) {
            option.selected = true;
        }
        select.appendChild(option);
    });

    return select;
}

// 옵션 조합 테이블 업데이트 함수
function updateCombinationTable() {
    const options = prepareOptionData();
    const combinations = getCombinations(options);

    const tableBody = document.getElementById("combinationTableBody");
    const existingRows = Array.from(tableBody.querySelectorAll("tr"));

    combinations.forEach((combination, index) => {
        let row;

        // 기존 row가 있는 경우 값을 유지하면서 업데이트
        if (existingRows[index]) {
            row = existingRows[index];
        } else {
            // 새로 추가된 조합에 대해 새 행 생성
            row = document.createElement("tr");
            tableBody.appendChild(row);
        }

        const combinationText = combination.map(opt => `${opt.name}: ${opt.value}`).join(", ");

        // 기존 행에서 현재 상태 값을 가져오거나 기본 "SALE"로 설정
        const existingStatusValue = row.querySelector('.status-select') ? row.querySelector('.status-select').value : 'SALE';

        row.innerHTML = `
            <td>${combinationText}</td>
            <td><input type="number" class="combination-stock" placeholder="재고 수량 입력" value="${row.querySelector('.combination-stock')?.value || ''}" /></td>
        `;

        // 상태 select 요소를 새로 생성하여 기존 상태를 유지
        const statusSelect = createStatusSelect(existingStatusValue);
        const statusCell = document.createElement('td');
        statusCell.appendChild(statusSelect);
        row.appendChild(statusCell);
    });

    // 기존 행이 조합보다 많을 경우, 불필요한 행 삭제
    if (existingRows.length > combinations.length) {
        for (let i = combinations.length; i < existingRows.length; i++) {
            tableBody.removeChild(existingRows[i]);
        }
    }
}



// 옵션 수정 시 조합 테이블 업데이트
function handleOptionChange() {
    updateCombinationTable();
}

// 수정 완료 아이콘 표시
function showConfirmationIcon() {
    document.querySelectorAll(".update-confirm-icon").forEach(icon => {
        icon.style.display = "inline";
        setTimeout(() => { icon.style.display = "none"; }, 2000);
    });
}

function updateOptionAndRefreshCombinations() {
    updateCombinationTable();
    showConfirmationIcon();
}

// 옵션 값이 추가되거나 수정될 때 자동 호출
document.querySelectorAll(".option-name, .option-type, .option-tag span").forEach(element => {
    element.addEventListener("change", updateOptionAndRefreshCombinations);
    element.addEventListener("input", updateOptionAndRefreshCombinations);
});