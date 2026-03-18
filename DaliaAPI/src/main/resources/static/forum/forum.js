function submitComment(button) {
    const postId = button.getAttribute("data-post-id");
    const postDiv = button.closest(".Post");
    const comentarioTextarea = postDiv.querySelector(".comment-text");

    const comentario = comentarioTextarea.value;

    if (!comentario.trim()) {
        alert("Comentário não pode estar vazio.");
        return;
    }

    fetch(`/posts/${postId}/comments`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ comment: comentario })
    })
        .then(res => {
            if (!res.ok) throw new Error("Erro ao comentar.");
            return res.json();
        })
        .then(() => {
            location.reload(); // após o reload, quebra é aplicada via função abaixo
        })
        .catch(err => {
            console.error(err);
            alert("Erro ao enviar comentário.");
        });
}

function toggleLike(element) {
    const postId = element.getAttribute("data-post-id");
    const isLiked = element.classList.contains("liked");
    const likeCountSpan = element.querySelector(".like__count");
    const heartImg = element.querySelector("img");
    let likeCount = parseInt(likeCountSpan.textContent);

    const url = isLiked ? `/posts/${postId}/unlike` : `/posts/${postId}/like`;

    fetch(url, { method: "PUT" })
        .then(res => {
            if (!res.ok) throw new Error("Erro ao curtir/descurtir.");

            if (isLiked) {
                likeCountSpan.textContent = likeCount - 1;
                element.classList.remove("liked");
                heartImg.src = "/forum/img/forum_img/Heart%20-%20outlined.svg";
            } else {
                likeCountSpan.textContent = likeCount + 1;
                element.classList.add("liked");
                heartImg.src = "/forum/img/forum_img/Heart%20-%20full.svg";
            }
        })
        .catch(err => {
            console.error(err);
            alert("Erro ao curtir/descurtir.");
        });
}

// Aplica quebra de linha visual em todos os comentários visíveis após o carregamento da página
function aplicarQuebraComentarios() {
    const maxLineLength = 57;
    document.querySelectorAll(".comment-content").forEach(element => {
        const originalText = element.textContent.trim();
        const words = originalText.split(/\s+/);
        let lines = [];
        let currentLine = "";

        for (let word of words) {
            if ((currentLine + " " + word).trim().length <= maxLineLength) {
                currentLine += (currentLine ? " " : "") + word;
            } else {
                lines.push(currentLine);
                currentLine = word;
            }
        }
        if (currentLine) lines.push(currentLine);
        element.textContent = lines.join("\n");
    });
}

document.addEventListener("DOMContentLoaded", aplicarQuebraComentarios);