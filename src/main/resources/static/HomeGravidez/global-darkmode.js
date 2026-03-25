(function() {
    const modoAtivo = localStorage.getItem('modoEscuro') === 'true';
    
    // Aplica as classes imediatamente
    if (modoAtivo) {
        document.documentElement.classList.add('modo-escuro-preload');
    }
    
    const style = document.createElement('style');
    style.textContent = `
        .modo-escuro-preload {
            --fundo: #43393D;
            --botoes: #D9D9D9;
            --botoes-hover: rgb(230, 230, 230);
            --botao-gravidez: #E3C3D8;
            --botao-gravidez-hover: rgb(250, 216, 238);
            --botao-confirmacao: #333;
            --botao-confirmacao-hover: #444;
            color: #fff !important;
            background-color: #43393D !important;
        }
        
        .modo-escuro-preload h1 {
            border-bottom-color: #fff !important;
        }
        
        .modo-escuro-preload footer {
            background-color: #333 !important;
            color: #fff !important;
        }
        
        .modo-noturno {
            height: 2rem;
            width: 60px;
            background-color: #fff;
            border-radius: 150px;
            position: relative;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        
        .modo-noturno .indicador {
            height: 100%;
            width: 30px;
            background-color: #000000;
            border-radius: 50%;
            transform: scale(0.8);
            position: absolute;
            left: 0;
            transition: all 0.3s ease;
        }
        
        .modo-noturno.ativo .indicador {
            transform: translateX(30px) scale(0.8);
            background-color: #222121;
        }
        
        .modo-noturno.ativo {
            background-color: #f7f3f3;
        }
        
        .modo-escuro {
            --fundo: #43393D;
            --botoes: #D9D9D9;
            --botoes-hover: rgb(230, 230, 230);
            --botao-gravidez: #E3C3D8;
            --botao-gravidez-hover: rgb(250, 216, 238);
            --botao-confirmacao: #333;
            --botao-confirmacao-hover: #444;
            color: #fff;
        }
        
        .modo-escuro .registro h1,
        .modo-escuro .registro h2 {
            color: #000000 !important;
        }
        
        .modo-escuro h1 {
            border-bottom-color: #fff;
        }
        
        .modo-escuro footer {
            background-color: #333;
            color: #fff;
        }
        
        .no-darkmode {
            filter: none !important;
        }
    `;
    document.head.appendChild(style);
})();

document.addEventListener('DOMContentLoaded', function() {
    const botaoModoNoturno = document.querySelector('.modo-noturno');
    const corpo = document.body;
    
    function finalizarCarregamentoModo() {
        const modoAtivo = localStorage.getItem('modoEscuro') === 'true';
        document.documentElement.classList.remove('modo-escuro-preload');
        
        if (modoAtivo) {
            corpo.classList.add('modo-escuro');
            aplicarFiltroImagens(true);
            if (botaoModoNoturno) botaoModoNoturno.classList.add('ativo');
        }
    }
    
    // Função para aplicar filtro apenas nas imagens do nav
    function aplicarFiltroImagens(isDark) {
        try {
            const imagensNav = document.querySelectorAll('nav img:not(.no-darkmode)');
            imagensNav.forEach(img => {
                img.style.filter = isDark ? 'brightness(0) invert(1)' : '';
                img.style.transition = 'filter 0.3s ease';
            });
        } catch (error) {
            console.error('Erro ao aplicar filtro de imagens:', error);
        }
    }
    
    // Configura o evento de clique (apenas se o botão existir na página)
    if (botaoModoNoturno) {
        botaoModoNoturno.addEventListener('click', () => {
            const escuroAtivo = !corpo.classList.contains('modo-escuro');
            corpo.classList.toggle('modo-escuro', escuroAtivo);
            botaoModoNoturno.classList.toggle('ativo', escuroAtivo);
            aplicarFiltroImagens(escuroAtivo);
            localStorage.setItem('modoEscuro', escuroAtivo);
        });
    }
    
    // Finaliza o carregamento do modo
    finalizarCarregamentoModo();
});