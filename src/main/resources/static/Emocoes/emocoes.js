const recommendations = {
    happy: ['Continue assim!', 'Pratique atividades que você ama!'],
    neutro: ['Tente fazer algo que te deixe feliz.', 'Uma caminhada pode ajudar!'],
    triste: ['Lembre-se de se cuidar.', 'Converse com um amigo!'],
    bravo: ['Tente relaxar e respirar fundo.', 'Uma pausa pode ajudar.'],
    colicas: ['Beba chá quente.', 'Repouso é importante.'],
    dorCabeca: ['Beba água.', 'Descanse em um lugar tranquilo.'],
    cansaco: ['Tente descansar um pouco.', 'Um lanche leve pode ajudar.'],
    inchaço: ['Beba bastante água.', 'Evite alimentos muito salgados.']
};

function recommendHabits() {
    const selectedMood = document.querySelector('.mood-options input[type="radio"]:checked');
    const selectedSymptoms = document.querySelectorAll('.sintomas .sintoma.selected');
    const flowLevel = document.getElementById('flow-level').value;

    let allRecommendations = [];

    if (selectedMood) {
        const moodValue = selectedMood.value;
        allRecommendations = allRecommendations.concat(recommendations[moodValue] || []);
    }

    selectedSymptoms.forEach(symptom => {
        const symptomValue = symptom.value;
        allRecommendations = allRecommendations.concat(recommendations[symptomValue] || []);
    });

    if (flowLevel === 'high') {
        allRecommendations = allRecommendations.concat(['Descansar mais', 'Evitar atividades físicas intensas']);
    } else if (flowLevel === 'medium') {
        allRecommendations = allRecommendations.concat(['Manter a rotina de alimentação saudável', 'Descansar o suficiente']);
    } else if (flowLevel === 'low') {
        allRecommendations = allRecommendations.concat(['Beber mais água', 'Descansar']);
    }

    const uniqueRecommendations = [...new Set(allRecommendations)];
    const recommendationsList = uniqueRecommendations.map(recommendation => `<li>${recommendation}</li>`).join('');

    document.getElementById('recommended-habits').innerHTML = `<ul>${recommendationsList}</ul>`;
}

document.querySelectorAll('.sintoma').forEach(button => {
    button.addEventListener('click', function() {
        this.classList.toggle('selected');
        recommendHabits();
    });
});

document.getElementById('flow-level').addEventListener('change', recommendHabits);

document.querySelectorAll('.mood-options input[type="radio"]').forEach(radio => {
    radio.addEventListener('change', recommendHabits);
});

document.addEventListener('DOMContentLoaded', recommendHabits);
