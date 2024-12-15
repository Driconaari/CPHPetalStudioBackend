// src/main/resources/static/js/main.js

import { authenticatedFetch } from './apiUtils.js';

// Example usage of authenticatedFetch
authenticatedFetch('http://localhost:8080/api/cart/add', {
    method: 'POST',
    body: JSON.stringify({
        bouquetId: 1, // Replace with actual bouquet ID
        quantity: 1 // Replace with actual quantity
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
    })
    .catch(error => {
        console.error('Error adding item to cart:', error);
    });