/*
    날짜 : 2024/10/24
    이름 : 최준혁
    내용 : 상품 등록 js 파일 생성
*/

document.addEventListener("DOMContentLoaded", () => {
    loadCategories();
});

window.onload = function() {


};
function prepareFormData() {
    // 옵션 정보 생성
    const options = [];
    document.querySelectorAll("#optionTableBody tr").forEach((row) => {
        const optionName = row.querySelector("[name='optionName[]']").value;
        const optionValues = row.querySelector("[name='optionValues[]']").value.split(",");
        options.push({
            name: optionName.trim(),
            active: true,
            optionItems: optionValues.map(value => ({ value: value.trim() }))
        });
    });

    // 옵션 조합 정보 생성
    const combinations = [];
    document.querySelectorAll("#combinationTableBody tr").forEach((row) => {
        const optionCombination = row.dataset.optionCombination;
        const stock = row.querySelector("[name='stock']").value;
        combinations.push({
            optionCombination,
            stock: parseInt(stock)
        });
    });

    // 상세 정보 생성
    const productDetails = [];
    document.querySelectorAll("#productDetailsTableBody tr").forEach((row) => {
        const detailName = row.querySelector("[name='detailName[]']").value;
        const detailValue = row.querySelector("[name='detailValue[]']").value;
        productDetails.push({
            name: detailName.trim(),
            value: detailValue.trim()
        });
    });

    console.log('asdfsafda' + productDetails)
    const form = document.querySelector('#registerform');
    // FormData 객체 생성
    const formData = new FormData(form);

    /*옵션과 조합 데이터를 JSON 형식으로 추가*/
    formData.append("optionsJson", JSON.stringify(options));
    formData.append("combinationsJson", JSON.stringify(combinations));
    formData.append("productDetailsJson", JSON.stringify(productDetails));
    //
    // document.getElementById("optionsJson").value = JSON.stringify(options);
    // document.getElementById("combinationsJson").value = JSON.stringify(combinations);

    console.log("aaaaaaaaaaaaaaaaaOptions:", options);
    console.log("sssssssssssssssssCombinations:", combinations);
    console.log("productDetailsJson:", productDetails);

    // 서버로 전송
    fetch('/api/product/register', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error("등록 실패");
            }
        })
        .then(data => {
            alert("상품이 잘 등록되었습니다.");
            window.location.href = "/admin/product/list"; // 목록 페이지로 이동
        })
        .catch(error => console.error("등록 중 오류:", error));
}


function toggleOptionSections() {
    const hasOptionsValue = document.getElementById("hasOption").value;
    document.getElementById("hiddenHasOptionsInput").value = (hasOptionsValue === "true");

    const hasOptions = document.getElementById("hasOption").value === "true";
    const optionSection = document.getElementById("optionSection");
    const combinationSection = document.getElementById("combinationSection");
    const stock = document.getElementById("stockCount");


    // 옵션 여부에 맞춰서 재고량 자동계산 여부 설정 옵션이 없는상품이면 직접입력하도록 readonly 삭제
    if(!hasOptions) {
        stock.value = '';
        stock.removeAttribute("readonly");
    } else{
        stock.value = '';
        stock.readOnly = true;
    }

    // 옵션 여부에 따라 섹션을 표시하거나 숨김
    optionSection.style.display = hasOptions ? "block" : "none";
    combinationSection.style.display = hasOptions ? "block" : "none";

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

// 포인트 가격에 따라 자동설정
function calculatePoint() {
    const priceInput = document.getElementById('price');
    const pointInput = document.getElementById('point');

    // 가격의 1%를 포인트로 설정
    const priceValue = parseFloat(priceInput.value) || 0;
    const pointValue = Math.floor(priceValue * 0.01); // 소수점 버림

    pointInput.value = pointValue;
}

function updateFileName(input, spanId) {
    const span = document.getElementById(spanId);
    span.textContent = input.files.length > 0 ? input.files[0].name : "선택된 파일 없음";
}

function populateSelectOptions(selectId, data) {
    const selectElement = document.getElementById(selectId);
    selectElement.innerHTML = `<option value="">선택</option>`;
    data.forEach(category => {
        const option = document.createElement("option");
        option.value = category.id;
        option.textContent = category.name;
        selectElement.appendChild(option);
    });
}

function clearSelectOptions(selectId) {
    const select = document.getElementById(selectId);
    select.innerHTML = '<option value="">선택</option>';
}

function addOptions(button) {
    const input = button.previousElementSibling;
    const values = input.value.split(',').map(value => value.trim()).filter(value => value);

    // 기존 옵션 목록을 가져옴
    const optionList = button.parentElement.nextElementSibling;

    values.forEach(value => {
        const li = document.createElement("li");
        li.textContent = value;
        const removeBtn = document.createElement("button");
        removeBtn.textContent = "삭제";
        removeBtn.type = "button";
        removeBtn.onclick = () => li.remove();
        li.appendChild(removeBtn);
        optionList.appendChild(li);
    });

    // 입력 필드 초기화
    input.value = "";
}

function addOptionValue(button) {
    const container = button.closest('.option-values-container');
    const valueInput = container.querySelector('input[type="text"]');
    const valueList = container.nextElementSibling;
    const optionNameInput = container.closest('tr').querySelector('input[name="optionName[]"]');

    if (!optionNameInput.value) {
        alert('옵션명을 먼저 입력해주세요.');
        return;
    }

    if (valueInput.value) {
        const li = document.createElement('li');
        li.textContent = valueInput.value;
        li.className = 'option-item';

        const removeButton = document.createElement('button');
        removeButton.textContent = '삭제';
        removeButton.onclick = () => {
            li.remove();
            generateCombinations(); // 값 삭제 시 조합 재생성
        };

        // 삭제 버튼에 스타일 추가
        removeButton.style.marginLeft = '10px';
        removeButton.style.backgroundColor = '#e74c3c';
        removeButton.style.color = '#fff';
        removeButton.style.border = 'none';
        removeButton.style.padding = '5px 10px';
        removeButton.style.borderRadius = '4px';
        removeButton.style.cursor = 'pointer';

        li.appendChild(removeButton);
        valueList.appendChild(li);
        valueInput.value = '';
        generateCombinations(); // 값 추가 시 조합 재생성
    }
}

function checkCategorySelection() {
    const category1 = document.getElementById('category1');
    const category2 = document.getElementById('category2');
    const category3 = document.getElementById('category3');

    if (!category1.value) {
        alert('1차 분류를 선택해주세요.');
        return false;
    }

    if (!category2.value) {
        alert('2차 분류를 선택해주세요.');
        return false;
    }

    if (!category3.value) {
        alert('3차 분류를 선택해주세요.');
        return false;
    }

    return true;
}

function gatherOptionValues() {
    const optionContainers = document.querySelectorAll('.option-values-container');
    const optionData = [];

    optionContainers.forEach(container => {
        const optionNameInput = container.closest('tr').querySelector('input[name="optionName[]"]');
        const optionName = optionNameInput ? optionNameInput.value.trim() : '';

        if (optionName) {
            const values = [...container.nextElementSibling.querySelectorAll('.option-item')]
                .map(item => item.textContent.replace('삭제', '').trim());

            optionData.push({
                name: optionName,
                values: values
            });
        }
    });

    return optionData;
}

function generateCombinations() {
    const optionData = gatherOptionValues();
    const combinationTableBody = document.getElementById('combinationTableBody');
    combinationTableBody.innerHTML = '';

    if (optionData.length === 0) {
        alert('옵션이 없습니다.');
        return;
    }

    const combinations = getCombinations(optionData.map(option => option.values));

    combinations.forEach(combination => {
        const row = document.createElement('tr');
        const combinationText = combination.join(', ');

        const stockInput = document.createElement('input');
        stockInput.type = 'number';
        stockInput.placeholder = '재고 수량';
        stockInput.style.width = '100px';

        row.innerHTML = `<td>${combinationText}</td>`;
        const stockCell = document.createElement('td');
        stockCell.appendChild(stockInput);
        row.appendChild(stockCell);

        combinationTableBody.appendChild(row);
    });
}


// 폼 제출 시 재고가 필수인지 체크하는 함수
function validateStockInputs() {
    const stockInputs = document.querySelectorAll('#combinationTableBody input[type="number"]');
    let allValid = true;

    stockInputs.forEach(input => {
        if (!input.value) {
            alert('모든 옵션 조합에 대해 재고 수량을 입력해야 합니다.');
            allValid = false;
        }
    });

    return allValid; // 모든 입력이 유효한 경우 true 반환
}

/////////////////////////////////////////////////////////////////

// 옵션 추가
function addOption() {
    const row = document.createElement("tr");
    row.innerHTML = `
    <td><input type="text" name="optionName[]" placeholder="옵션명 입력" /></td>
    <td><input type="text" name="optionValues[]" placeholder="옵션 항목 입력 (예: S, M, L)" /></td>
    <td><button type="button" onclick="removeOption(this)">삭제</button></td>
  `;
    document.getElementById("optionTableBody").appendChild(row);
}

// 옵션 삭제
function removeOption(button) {
    const row = button.closest("tr");
    row.remove();
}

// 선택 옵션 등록 완료 클릭 시 조합 생성
function completeOptionSelection() {
    const optionNames = [];
    const optionValues = [];

    document.querySelectorAll('#optionTableBody tr').forEach(row => {
        const optionName = row.querySelector('input[name="optionName[]"]').value.trim();
        const values = row.querySelector('input[name="optionValues[]"]').value.split(",").map(v => v.trim());

        if (optionName && values.length > 0) {
            optionNames.push(optionName);
            optionValues.push(values);
        }
    });

    if (optionNames.length > 0) {
        createCombinations(optionNames, optionValues);
    } else {
        alert("옵션을 최소 1개 이상 입력해주세요.");
    }

}
// 옵션이 존재할때 총 재고량을 옵션 조합의 재고의 합으로 계산
function updateTotalStock() {
    const stockInputs = document.querySelectorAll("#combinationTableBody input[name='stock']");
    let totalStock = 0;

    stockInputs.forEach(input => {
        const stockValue = parseInt(input.value, 10) || 0; // 값이 비어있으면 0으로 처리
        totalStock += stockValue;
    });

    // 총 재고 수량을 readonly 필드에 반영
    document.getElementById("stockCount").value = totalStock;
}

// 조합 생성 및 테이블에 추가
function createCombinations(optionNames, optionValues) {
    const combinationTableBody = document.getElementById("combinationTableBody");
    combinationTableBody.innerHTML = ""; // 기존 조합 초기화

    const combinations = getCombinations(optionValues); // 가능한 모든 조합 생성

    combinations.forEach(combination => {
        const row = document.createElement("tr");

        // 옵션 조합을 표시하는 열
        const optionCell = document.createElement("td");
        const combinationString = combination.join(", "); // 조합을 문자열로 저장
        optionCell.textContent = combinationString;

        // 조합 데이터를 행의 데이터 속성에 추가 (서버에 전송하기 위해)
        row.dataset.optionCombination = combinationString;

        // 재고 수량 입력 필드
        const stockCell = document.createElement("td");
        const stockInput = document.createElement("input");
        stockInput.type = "number";
        stockInput.name = "stock";
        stockInput.placeholder = "재고 수량 입력";

        // stock 입력 필드에 이벤트 리스너 추가
        stockInput.addEventListener("input", updateTotalStock);

        stockCell.appendChild(stockInput);
        row.appendChild(optionCell);
        row.appendChild(stockCell);
        combinationTableBody.appendChild(row);
    });

    // 조합 결과를 표시
    document.getElementById("combinationSection").style.display = "block";
}


// 가능한 모든 조합을 생성하는 함수
function getCombinations(values) {
    if (values.length === 0) return [[]];
    const [first, ...rest] = values;
    const combinationsWithoutFirst = getCombinations(rest);
    return first.flatMap(value => combinationsWithoutFirst.map(combination => [value, ...combination]));
}

// 상세정보
function addDetail() {
    const tableBody = document.getElementById("productDetailsTableBody");
    const newRow = document.createElement("tr");

    newRow.innerHTML = `
    <td><input type="text" name="detailName[]" placeholder="옵션 이름 (예: 효과)" style="width: 100%;"></td>
    <td><input type="text" name="detailValue[]" placeholder="옵션 값 (예: 수분/보습)" style="width: 100%;"></td>
    <td><button type="button" onclick="removeDetail(this)">삭제</button></td>
  `;

    tableBody.appendChild(newRow);
}

function removeDetail(button) {
    const row = button.closest("tr");
    row.parentNode.removeChild(row);
}
