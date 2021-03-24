function checkFileSize(fileInput) {
    let element = document.getElementById(fileInput);
    const MAX_SIZE = 10;
    const BYTE_TO_MB = 1024 * 1000;
    if (element.files != null) {
        let size = element.files[0].size / BYTE_TO_MB;
        console.log(fileInput + ' size: ' + size);
        let messageElement = document.getElementById(fileInput + '_error_message');
        let errorDiv = document.getElementById(fileInput + '_error');
        let saveButton = document.getElementById('save');
        if (size > MAX_SIZE) {
            messageElement.innerHTML = 'Uploaded file is too big!';
            errorDiv.style.display = 'block';
            saveButton.disabled = true;
        } else {
            messageElement.innerHTML = '';
            errorDiv.style.display = 'none';
            saveButton.disabled = false;
        }
    }
}