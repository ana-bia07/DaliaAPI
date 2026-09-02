// Constantes e Funções Compartilhadas
const API_URL = "https://daliaapi.onrender.com";

// 1. Busca o token salvo
function getToken() {
    return localStorage.getItem("tokenJWT");
}

// 2. Desloga e limpa o token
function logout() {
    localStorage.removeItem("tokenJWT");
    window.location.href = "/login.html";
}

// 3. Guarda de rota: exige login e chuta se não tiver token
function requireAuth() {
    const token = getToken();
    if (!token) {
    window.location.href = "/login.html";
    }
}

// 4. Função universal para chamar a API já com o Bearer Token injetado
async function apiFetch(endpoint, options = {}) {
const token = getToken();

const defaultHeaders = {
    "Content-Type": "application/json",
    "Accept": "application/json"
};

if (token) {
    defaultHeaders["Authorization"] = `Bearer ${token}`;
}

const config = {
    ...options,
    headers: {
    ...defaultHeaders,
    ...options.headers
    }
};

const response = await fetch(`${API_URL}${endpoint}`, config);

// Se o token expirou ou for inválido (401/403), desloga na hora
if (response.status === 401 || response.status === 403) {
    logout();
    throw new Error("Sessão expirada.");
}

return response;
}