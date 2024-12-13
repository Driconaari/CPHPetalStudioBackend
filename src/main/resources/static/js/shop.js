// src/main/resources/static/js/shop.js

document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.add-to-cart-button').forEach(button => {
        button.addEventListener('click', event => {
            const bouquetId = event.target.dataset.bouquetId;
            const quantity = 1; // or get the quantity from an input field
            const jwtToken = localStorage.getItem('jwtToken'); // Retrieve JWT token from local storage

            fetch('/api/cart/add', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + jwtToken // Include JWT token in the Authorization header
                },
                body: JSON.stringify({
                    bouquetId: bouquetId,
                    quantity: quantity
                })
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                console.log('Item added to cart:', data);
                // Optionally update the UI to reflect the added item
            })
            .catch(error => {
                console.error('Error adding item to cart:', error);
            });
        });
    });
});