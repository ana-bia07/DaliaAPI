const form = document.getElementById("login");
const email = document.getElementById("email");
const password = document.getElementById("password");
const resultado = document.getElementById("resultado")

form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const login = {
        email: email.value.trim(),
        password: password.value.trim()
    };

    await realizarLogin(login);
})

async function realizarLogin(dados){
    try{
        const response = await fetch("http://localhost:8080/api/user/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(dados)
        });
        if(!response.ok){
            throw new Error("Email ou senha incorretos.");
        }

        const data = await response.json();
        localStorage.setItem("tokenJWT", data.token);

        window.location.href = "/html/Dashboard.html";
    }catch (error){
        resultado = error
        alert(error.message);
        console.error("erro de login: ", error)
    }
}
