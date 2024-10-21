// fetch GET용
async function fetchGet(url){

    console.log("fetchData1...1");

    try{
        console.log("fetchData1...2");
        const response = await fetch(url);
        console.log("here1");

        if(!response.ok){
            console.log("here2");
            throw new Error('response not ok');
        }

        const data = await response.json();
        console.log("data1 : " + data);

        return data;

    }catch (err) {
        console.log(err)
    }
}

// fetch POST용
async function fetchPost(url, jsonData){

    console.log("fetchData2...1");

    try{
        console.log("fetchData2...2");
        const response = await fetch(url, {
            method: 'POST',
            headers: {"Content-type":"application/json"},
            body: JSON.stringify(jsonData)
        });
        console.log("fetchData2...3");

        if(!response.ok){
            console.log("fetchData2...4");
            throw new Error('response not ok');
        }

        const data = await response.json();
        //console.log("fetchData2...5 : " + JSON.stringify(data));
        //console.log('data.writer : ' + data.writer);

        return data;

    }catch (err) {
        console.log(err)
        return null;
    }
}

// fetch DELETE용
async function fetchDelete(url){

    try{
        const response = await fetch(url, {
            method: 'DELETE'
        });

        if(!response.ok){
            throw new Error('response not ok');
        }

        const data = await response.json();
        console.log("data1 : " + data);

        return data;

    }catch (err) {
        console.log(err)
    }
}


// fetch PUT용
async function fetchPut(url, jsonData){

    try{
        const response = await fetch(url, {
            method: 'PUT',
            headers: {"Content-type":"application/json"},
            body: JSON.stringify(jsonData)
        });

        if(!response.ok){
            throw new Error('response not ok');
        }

        const data = await response.json();
        console.log("data1 : " + data);

        return data;

    }catch (err) {
        console.log(err)
    }
}


function alertModal(message){
    const modal = document.getElementById('alertModal');
    modal.getElementsByClassName('modal-body')[0].innerText = message;
    const resultModal = new bootstrap.Modal(modal);
    resultModal.show();
}

function confirmModal(message){
    return new Promise(function (resolve, reject){
        const confirmModal = document.getElementById('confirmModal');
        const btnOk = document.getElementById('btnOk');
        const btnCancel = document.getElementById('btnCancel');
        confirmModal.getElementsByClassName('modal-body')[0].innerText = message;

        const modal = new bootstrap.Modal(confirmModal);
        modal.show(); // 모달 열기

        // 확인 버튼 클릭 시
        btnOk.addEventListener('click', function (){
            resolve(true);
        });

        // 취소 버튼 클릭 시
        btnCancel.addEventListener('click', function (){
            resolve(false);
        });
    });

}

function showResultValid(result, message){
    result.style.color = 'green';
    result.innerText = message;
}

function showResultInvalid(result, message){
    result.style.color = 'red';
    result.innerText = message;
}
