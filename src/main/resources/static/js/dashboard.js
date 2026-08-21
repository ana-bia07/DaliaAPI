document.addEventListener("DOMContentLoaded", async () => {

    const token = localStorage.getItem("tokenJWT");

    if(!token){
        window.location.href = "/html/login.html";
        return;
    }

    try{
        const response = await fetch("http://localhost:8080/api/admin/", {
            method: "GET",
            headers: {
                "Authorization":`Bearer ${token}`,
                "Accept":  "application/json"
            }
        });
        if (response.status === 401 || response.status === 403){
            alert("Acesso restrito para administradores.");
            console.log("Acesso restrito para admins, man");
            localStorage.removeItem("tokenJWT");
            window.location.href = "/html/login.html";
            return;
        }
        const data = await response.json();
        const percentMenstruacao = document.getElementById("percentMenstruacao");
        const percentGravidez = document.getElementById("percentGravidez");
        const rankingTopics = document.getElementById("rankingTopics");
        const denuncias = document.getElementById("denuncias");

        if(percentMenstruacao && data.modoMenstruacao != undefined){
            percentMenstruacao.innerHTML= `${data.modoMenstruacao}%`;
            const barra = document.getElementById("barMenstruacao");
            barra.style.width = data.modoMenstruacao + "%"
        }

        if(percentGravidez && data.modoGravidez != undefined){
            percentGravidez.innerHTML= `${data.modoGravidez}%`;
            const barra = document.getElementById("barGravidez");
            barra.style.width = data.modoGravidez + "%"
        }
        rankingTopics.innerHTML = "";

        if (data.categoriaPost) {
            Object.entries(data.categoriaPost).forEach(([categoria, total]) => {
            const li = document.createElement("li");
            li.innerText = `${categoria}: ${total}`;
            rankingTopics.appendChild(li);
            console.log("Foi o 1")
        });
        } else if (data.categoriaPost) {
            data.categoriaPost.forEach(item => {
                const li = document.createElement("li");
                li.innerText = item;
                rankingTopics.appendChild(li);
                console.log("Foi o 2")
            });
        } 
        if (data.denuncias) {
            Object.entries(data.denuncias).forEach(([denuncia]) => {
            const li = document.createElement("li");
            li.innerText = `${denuncia}`;
            denuncias.appendChild(li);
            console.log("Foi o denunicia")
        });}
    }catch (erro){
        console.error("Erro ao carregar dados de dashboard: ", error);
    }
});