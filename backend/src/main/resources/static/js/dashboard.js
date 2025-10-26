document.addEventListener("DOMContentLoaded", () => {
    // --- Gráfico de Barras ---
    const ctxBar = document.getElementById('tareasChart').getContext('2d');
    
    new Chart(ctxBar, {
        type: 'bar',
        data: {
            labels: ['Alta', 'Media', 'Baja'],
            datasets: [{
                label: 'Total de tareas',
                data: [2400, 2000, 2290],
                backgroundColor: ['#c62828', '#ef6c00', '#2e7d32'],
                borderColor: ['#a71d1d', '#d65a00', '#1b5e20'],
                borderWidth: 1,
                borderRadius: 6
            }]
        },
        options: {
            scales: { y: { beginAtZero: true } },
            plugins: { legend: { display: false } }
        }
    });

    // --- Gráfico Circular ---
    const ctxPie = document.getElementById('estadoChart').getContext('2d');

    new Chart(ctxPie, {
        type: 'doughnut',
        data: {
            labels: ['Completadas', 'En progreso', 'Pendientes'],
            datasets: [{
                label: 'Estado de tareas',
                data: [4, 3, 2],
                backgroundColor: ['#1565c0', '#ef6c00', '#c62828'],
                borderColor: ['#0d47a1', '#d65a00', '#a71d1d'],
                borderWidth: 2
            }]
        },
        options: {
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: { color: '#333' }
                }
            },
            cutout: '65%'
        }
    });
});
