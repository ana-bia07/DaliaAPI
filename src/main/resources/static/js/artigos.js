document.addEventListener("DOMContentLoaded", async () => {
    const token = localStorage.getItem("tokenJWT");
    if(!token){
        window.location.href = "/html/login.html";
        return;
    }
    
    await carregarArtigos();

    const form = document.getElementById('artigoCreate');

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const idInput = document.getElementById('idArtigo');
            const id = idInput ? idInput.value : "";

        const title = document.getElementById('title').value;
        const legend = document.getElementById('legend').value;
        const link = document.getElementById('link').value;
        const category = document.getElementById('category').value;

        const dado = {title:title, legend:legend, link:link, category:category}

        if(id){
            await atualizarArtigo(id, dado);
        }else{
            await createArtigo(dado);
        }
        form.reset();
    })
});

async function carregarArtigos() {
    const token = localStorage.getItem("tokenJWT");
    const artigosCard = document.getElementById("cardsArtigos");

    try{
        const response = await fetch("http://localhost:8080/api/articles/getTodos", {
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

        const artigos = await response.json();
        artigosCard.innerHTML = "";

        console.log("vai criar o comentario")
        
        artigos.forEach(artigos => {
            const artigoJSON = JSON.stringify(artigos).replace(/"/g, '&quot;');

            const cardHTML = `
            <div class="card-admin-post" data-post-id="${artigos.id}">
                <div class="card-left">
                    <p class="category">${artigos.category}</p>
                    <strong class="post-titulo">${artigos.title}</strong>
                    <p class="post-corpo">${artigos.legend}</p>
                    <p class="artigo-link artigo-corpo">${artigos.link}</p>
                </div>
                <div class="card-right">
                    <button class="btn-deletar" onclick="deletarArtigo('${artigos.id}')">Deletar</button>
                </div>
            </div>`;

             artigosCard.innerHTML += cardHTML;
        });
        
        console.log("chegou no final")
       
    }catch (error){
        console.error("falha a lsitar os post", error);
    }
}


async function createArtigo(dados){

    const token = localStorage.getItem("tokenJWT");

    try{
         const response = await fetch("http://localhost:8080/api/articles/create", {
            method: "POST",
            headers: {
                "Authorization":`Bearer ${token}`,
                "Content-Type":"application/json"
            },
            body: JSON.stringify(dados)
        });
        if(response.ok){
            alert("Artigo criado com sucesso!");
            await carregarArtigos();
        }

    }catch(erro){
        console.error("erro ao criar artigo: ", erro)
    }
}

function prepararEdicao(artigo) {
    document.getElementById('idArtigo').value = artigo.id;
    document.getElementById('title').value = artigo.title;
    document.getElementById('legend').value = artigo.legend;
    document.getElementById('link').value = artigo.link;
    document.getElementById('category').value = artigo.category || "";

    // Muda o texto do botão principal e exibe o botão de cancelar
    const btnSalvar = document.getElementById('btnSalvar');
    if (btnSalvar) btnSalvar.innerText = "Atualizar Artigo";

    window.scrollTo({ top: 0, behavior: 'smooth' }); // Rola até o formulário
}

async function atualizarArtigo(id, dados) {
    const token = localStorage.getItem("tokenJWT");

    try {
        const response = await fetch(`http://localhost:8080/api/articles/${id}`, {
            method: "PUT",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(dados)
        });

        if (response.ok) {
            alert("Artigo atualizado com sucesso!");
            await carregarArtigos();
        } else {
            alert("Erro ao atualizar o artigo.");
        }
    } catch (erro) {
        console.error("Erro ao atualizar artigo:", erro);
    }
}

async function deletarArtigo(id){
    if(!confirm("Deseja deletar este artigo?"))return;

    const token = localStorage.getItem("tokenJWT");

    try{
         const response = await fetch(`http://localhost:8080/api/articles/${id}`, {
            method: "DELETE",
            headers: {
                "Authorization":`Bearer ${token}`
            }
        });
        if(response.ok){
            alert("Artigo excluido com sucesso!");
            await carregarArtigos();
        }

    }catch(erro){
        console.error("erro ao deletar artigo: ", erro)
    }
}

artigos.forEach(artigo => {
    const artigoJSON = JSON.stringify(artigo).replace(/"/g, '&quot;');

    const cardHTML = `
        <div class="card-artigo" data-post-id="${artigo.id}">
            <div class="card-info">
                <span class="artigo-categoria">${artigo.category || 'Geral'}</span>
                <h3 class="artigo-titulo">${artigo.title}</h3>
                <p class="artigo-descricao">${artigo.legend}</p>
            </div>
            <div class="card-acoes">
                <button type="button" class="btn-acao btn-editar" title="Editar Artigo" onclick='prepararEdicao(${artigoJSON})'>
                    <i data-lucide="pencil"></i>
                </button>
                <button type="button" class="btn-acao btn-deletar" title="Deletar Artigo" onclick="deletarArtigo('${artigo.id}')">
                    <i data-lucide="trash-2"></i>
                </button>
            </div>
        </div>
    `;

    artigosCard.innerHTML += cardHTML;
});

// Renderiza os ícones SVG dinâmicos após atualizar o HTML
lucide.createIcons();