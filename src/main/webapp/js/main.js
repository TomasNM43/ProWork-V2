// Funciones JavaScript para ProWork
console.log('ProWork cargado correctamente');

// Función de ejemplo
document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM cargado');
    
    // Agregar efecto a las tarjetas
    const cards = document.querySelectorAll('.card');
    cards.forEach(card => {
        card.addEventListener('click', function(e) {
            if (!e.target.classList.contains('btn')) {
                console.log('Tarjeta clickeada:', this.querySelector('h3').textContent);
            }
        });
    });
});
