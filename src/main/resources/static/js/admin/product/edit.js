
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