let calendar;  // declarar no escopo global para usar depois

document.addEventListener('DOMContentLoaded', async function () {
    const calendarEl = document.getElementById('calendar');
    const monthYearEl = document.getElementById('month-year');
    const prevBtn = document.getElementById('prev-month');
    const nextBtn = document.getElementById('next-month');

    try {
        const response = await fetch('/calendar-data');
        const eventos = await response.json();

        if (eventos.error) {
            console.error(eventos.error);
            calendarEl.innerHTML = '<p style="color: red;">' + eventos.error + '</p>';
            return;
        }

        let currentDate = new Date();
        const maxMonthsAhead = 3;

        calendar = new FullCalendar.Calendar(calendarEl, {
            initialDate: currentDate,
            initialView: 'dayGridMonth',
            locale: 'pt-br',
            height: 'auto',
            contentHeight: 'auto',
            aspectRatio: 1.35,
            headerToolbar: false,
            fixedWeekCount: false,
            displayEventTime: false,
            events: eventos,
            eventDidMount: function (info) {
                if (info.event.extendedProps.rendering === 'background') {
                    info.el.style.backgroundColor = info.event.backgroundColor || info.event.color;
                    info.el.style.opacity = '0.5';
                    info.el.style.border = 'none';
                }
            },
            dayMaxEventRows: true,
        });

        calendar.render();

        function updateMonthYearText(date) {
            const options = { year: 'numeric', month: 'long' };
            monthYearEl.textContent = date.toLocaleDateString('pt-BR', options);
        }

        updateMonthYearText(currentDate);

        function changeMonth(increment) {
            let newDate = new Date(currentDate);
            newDate.setMonth(newDate.getMonth() + increment);

            let maxDate = new Date();
            maxDate.setMonth(maxDate.getMonth() + maxMonthsAhead);

            if (newDate > maxDate) return;

            let minDate = new Date();
            minDate.setHours(0, 0, 0, 0);
            if (newDate < minDate) return;

            currentDate = newDate;
            calendar.gotoDate(currentDate);
            updateMonthYearText(currentDate);
        }

        prevBtn.addEventListener('click', () => changeMonth(-1));
        nextBtn.addEventListener('click', () => changeMonth(1));

        // Listener para o botão Registrar Menstruação
        document.getElementById('registrar-menstruacao').addEventListener('click', async () => {
            const hoje = new Date().toISOString().split('T')[0]; // yyyy-mm-dd

            try {
                const res = await fetch('/registrar-menstruacao', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ data: hoje }),
                });

                if (!res.ok) {
                    alert('Erro ao registrar menstruação');
                    return;
                }

                const eventosResponse = await fetch('/calendar-data');
                const eventosAtualizados = await eventosResponse.json();

                if (eventosAtualizados.error) {
                    alert('Erro ao carregar eventos atualizados');
                    return;
                }

                calendar.removeAllEvents();
                calendar.addEventSource(eventosAtualizados);
                calendar.refetchEvents();

                alert('Menstruação registrada e calendário atualizado!');

            } catch (error) {
                console.error('Erro:', error);
                alert('Erro ao registrar menstruação');
            }
        });

    } catch (error) {
        console.error('Erro ao carregar dados do calendário:', error);
        calendarEl.innerHTML = '<p style="color: red;">Erro ao carregar o calendário.</p>';
    }
});