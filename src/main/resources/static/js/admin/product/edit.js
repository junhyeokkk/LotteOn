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

// 이미지 미리보기 함수
function previewImage(event, imgId) {
    const reader = new FileReader();
    reader.onload = () => document.getElementById(imgId).src = reader.result;
    reader.readAsDataURL(event.target.files[0]);
}

function addOptions1() {
    const optionTableBody = document.getElementById('optionTableBody');
    const newRow = document.createElement('tr');

    // 옵션 타입 셀
    const typeCell = document.createElement('td');
    const typeSelect = document.createElement('select');
    typeSelect.innerHTML = `
        <option value="기본">기본</option>
        <option value="입력형">입력형</option>
        <option value="색상">색상</option>
    `;
    typeCell.appendChild(typeSelect);

    // 옵션명 셀
    const nameCell = document.createElement('td');
    const nameInput = document.createElement('input');
    nameInput.type = 'text';
    nameInput.placeholder = '옵션명 입력';
    nameCell.appendChild(nameInput);

    // 옵션값 셀
    const valueCell = document.createElement('td');
    const valueContainer = document.createElement('div');
    valueContainer.className = 'value-container';

    // 옵션값 편집 기능
    const valueInput = document.createElement('input');
    valueInput.type = 'text';
    valueInput.placeholder = '옵션값 입력 (예: S, M, L)';
    valueInput.classList.add('value-input');
    valueInput.addEventListener('keypress', function (e) {
        if (e.key === 'Enter') {
            e.preventDefault(); // 폼 제출 방지
            addOptionValue(valueContainer, valueInput.value);
            valueInput.value = ''; // 입력 후 초기화
        }
    });
    valueContainer.appendChild(valueInput);
    valueCell.appendChild(valueContainer);

    // 필수 여부 체크박스
    const requiredCell = document.createElement('td');
    const requiredCheckbox = document.createElement('input');
    requiredCheckbox.type = 'checkbox';
    requiredCheckbox.className = 'required-checkbox';
    requiredCell.appendChild(requiredCheckbox);

    // 삭제 버튼 셀
    const deleteCell = document.createElement('td');
    const deleteButton = document.createElement('button');
    deleteButton.type = 'button';
    deleteButton.textContent = 'X';
    deleteButton.className = 'delete-btn';
    deleteButton.onclick = function () {
        optionTableBody.removeChild(newRow);
    };
    deleteCell.appendChild(deleteButton);

    // 행에 추가
    newRow.appendChild(typeCell);
    newRow.appendChild(nameCell);
    newRow.appendChild(valueCell);
    newRow.appendChild(requiredCell);
    newRow.appendChild(deleteCell);

    optionTableBody.appendChild(newRow);
}

// 옵션값 추가 함수
function addOptionValue(container, value) {
    const valueSpan = document.createElement('span');
    valueSpan.className = 'option-value';
    valueSpan.textContent = value;
    valueSpan.contentEditable = true; // 옵션값을 클릭하여 수정 가능
    valueSpan.addEventListener('blur', function () {
        // 엔터를 누르거나 다른 곳을 클릭하면 값이 수정된 상태로 유지
        valueSpan.textContent = valueSpan.textContent.trim();
    });

    // 삭제 버튼 추가
    const deleteValueBtn = document.createElement('button');
    deleteValueBtn.type = 'button';
    deleteValueBtn.textContent = 'X';
    deleteValueBtn.className = 'delete-value-btn';
    deleteValueBtn.onclick = function () {
        container.removeChild(valueSpan);
    };

    valueSpan.appendChild(deleteValueBtn);
    container.appendChild(valueSpan);
}