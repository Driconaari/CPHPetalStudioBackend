// src/main/resources/static/js/apiUtils.js

function authenticatedFetch(url, options = {}) {
    const jwtToken = localStorage.getItem('jwtToken'); // Retrieve the JWT token from local storage or another secure place

    if (!jwtToken) {
        throw new Error('JWT token is missing');
    }

    const headers = {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + jwtToken,
        ...options.headers
    };

    return fetch(url, {
        ...options,
        headers
    });
}

export { authenticatedFetch };