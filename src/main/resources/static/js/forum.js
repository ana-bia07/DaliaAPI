document.addEventListener("DOMContentLoaded", async () => {
    const token = localStorage.getItem("tokenJWT");
    if(!token){
        window.location.href = "/html/login.html";
        return;
    }

    await carregarPostsAdmin();
});

async function carregarPostsAdmin() {
    const token = localStorage.getItem("tokenJWT");
    const container = document.getElementById("cardsContainer");

    try{
        const response = await fetch("http://localhost:8080/api/posts/getTodos", {
            method: "GET",
            headers: {
                "Authorization":`Bearer ${token}`,
                "Accept":  "application/json"
            }
        });
        console.log("fez a requisição")
        if (!response.ok){
            throw new Error("Erro ao buscar postagens.")
            console.log("entou no if, ixii")
        }

        const posts = await response.json();
        container.innerHTML = "";

        console.log("vai criar o comentario")
        posts.forEach(post => {
            const listaComentarios = (post.comments && post.comments.length > 0)
            ? post.comments.map((c, index) => `
                <li class="item-comentario">
                <span>${c.comment}</span>
                <button class="btn-deletar-comentario" 
                        onclick="deletarComentario('${post.id}', '${c.id || index}')">deletar comentario
                        <i data-lucide="trash-2"></i>
                </button>
            </li>`).join("")
            : "<li>Nenhum comentario.</li>";

        console.log("terminou comentario e começando post")

        const cardHTML = `
            <div class="card-admin-post" data-post-id="${post.id}">
                <div class="card-left">
                    <p class="category>${post.category}></p>
                    <strong class="post-titulo">${post.title}</strong>
                    <p class="post-corpo">${post.content}</p>
                        <div class="secao-comentarios">
                            <span class="subtitulo-comentarios">Lista de comentários:</span>
                            <ul class="lista-comentarios">
                                ${listaComentarios}
                            </ul>
                        </div>
                </div>

                <div class="card-right">
                    <button class="btn-deletar" onclick="deletarPost('${post.id}')">Deletar</button>
                </div>
            </div>`;
        console.log("chegou no final")
        container.innerHTML += cardHTML;
        });
        if (window.lucide) {
            lucide.createIcons();
        }

    }catch (error){
        console.error("falha a listar os posts", error);
    }
}

async function deletarPost(id){
    if(!confirm("Deseja deletar este post?"))return;

    const token = localStorage.getItem("tokenJWT");

    try{
         const response = await fetch("http://localhost:8080/api/posts/${id}", {
            method: "DELETE",
            headers: {
                "Authorization":`Bearer ${token}`
            }
        });
        if(response.ok){
            alert("Post excluido com sucesso!");
            await carregarPostsAdmin();
        }

    }catch(erro){
        console.error("erro ao deletar post: ", erro)
    }
}

async function deletarComentario(idPost, indexComent) {
    if(!confirm("Deseja deletar este o comentario" + indexComent))return;

    const token = localStorage.getItem("tokenJWT");

    try{
         const response = await fetch("http://localhost:8080/api/posts/${idPost}/${indexComent}", {
            method: "DELETE",
            headers: {
                "Authorization":`Bearer ${token}`
            }
        });
        if(response.ok){
            alert("Comentario excluido com sucesso!");
            await carregarPostsAdmin();
        }

    }catch(erro){
        console.error("erro ao deletar Comentario: ", erro)
    }
}