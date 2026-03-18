const bolinhas = document.querySelectorAll('.bolinha');

const cores = {
    menstruacao: 'vermelho',
    periodo_fertil: 'verde',
    ovulacao: 'azul'
};

const userId = document.body.dataset.userid;

function formatarDataISO(data) {
    return data.toLocaleDateString('sv-SE'); // yyyy-MM-dd
}

async function pintarBolinhasComEventosDoBackend() {
    if (!userId || userId === 'visitante') {
        console.error('ID do usuário não encontrado ou visitante.');
        return;
    }

    try {
        const response = await fetch(`/api/ciclo5dias-home/${userId}`);
        if (!response.ok) {
            throw new Error(`Erro ao buscar eventos: ${response.status}`);
        }

        const eventos = await response.json();

        const hoje = new Date();
        const hojeStr = formatarDataISO(hoje);

        console.log('Eventos recebidos:', eventos);
        console.log('Hoje (JS):', hojeStr);
        console.log('Eventos recebidos:', eventos);



        eventos.forEach(ev => {
            console.log(`Evento recebido - Data: ${ev.data}, Status: ${ev.status}`);
        });


        bolinhas.forEach((bolinha, index) => {
            const offset = index - 2;
            const dataBolinha = new Date(hoje.getFullYear(), hoje.getMonth(), hoje.getDate() + offset);
            const dataStr = formatarDataISO(dataBolinha);

            const dateTextElem = bolinha.querySelector('.date-text');
            const dayNumberElem = bolinha.querySelector('.day-number');

            if (dateTextElem) {
                dateTextElem.textContent = `${String(dataBolinha.getDate()).padStart(2, '0')}/${String(dataBolinha.getMonth() + 1).padStart(2, '0')}`;
            }
            if (dayNumberElem) {
                dayNumberElem.textContent = dataBolinha.getDate();
            }

            bolinha.classList.remove(...Object.values(cores));

            const eventoDoDia = eventos.find(ev => ev.data === dataStr);
            if (eventoDoDia && cores[eventoDoDia.status]) {
                bolinha.classList.add(cores[eventoDoDia.status]);
            }
        });

        const infoCentral = document.getElementById('info-central');
        if (infoCentral) {
            infoCentral.classList.remove(...Object.values(cores));

            const eventoHoje = eventos.find(ev => ev.data === hojeStr);

            if (eventoHoje && cores[eventoHoje.status]) {
                const nomeEvento = eventoHoje.status;
                infoCentral.textContent = `Hoje é ${nomeEvento.replace('_', ' ')}`;
                infoCentral.classList.add(cores[nomeEvento]);
            } else {
                let mensagem = 'Sem evento nos próximos dias';
                for (let i = 1; i <= 2; i++) {
                    const futura = new Date(hoje.getFullYear(), hoje.getMonth(), hoje.getDate() + i);
                    const futuraStr = formatarDataISO(futura);
                    const eventoFuturo = eventos.find(ev => ev.data === futuraStr);
                    if (eventoFuturo && cores[eventoFuturo.status]) {
                        const nomeEvento = eventoFuturo.status.replace('_', ' ');
                        mensagem = `Faltam ${i} dia${i > 1 ? 's' : ''} para o ${nomeEvento}`;
                        infoCentral.classList.add(cores[eventoFuturo.status]);
                        break;
                    }
                }
                infoCentral.textContent = mensagem;
            }
        }

    } catch (error) {
        console.error('Erro ao pintar bolinhas:', error);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    pintarBolinhasComEventosDoBackend();
});
