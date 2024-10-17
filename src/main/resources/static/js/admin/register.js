function updateFileName(input, spanId) {
    const span = document.getElementById(spanId);
    if (input.files.length > 0) {
        span.textContent = input.files[0].name; // Update the span text with the selected file name
    } else {
        span.textContent = "선택된 파일 없음"; // Default text if no file is selected
    }
}
