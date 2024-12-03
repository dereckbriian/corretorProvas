document.getElementById("uploadForm").addEventListener("submit", function (e) {
    e.preventDefault();

    const fileInput = document.getElementById("fileInput");
    const file = fileInput.files[0];

    if (!file) {
        alert("Selecione um arquivo para enviar.");
        return;
    }

    const formData = new FormData();
    formData.append("file", file);

    fetch("/api/upload", {
        method: "POST",
        body: formData,
    })
        .then((response) => response.json())
        .then((data) => {
            const resultadoDiv = document.getElementById("resultado");
            resultadoDiv.innerHTML = `Acertos: ${data.acertos}, Erros: ${data.erros}`;
        })
        .catch(() => {
            alert("Erro ao processar o arquivo. Tente novamente.");
        });
});
