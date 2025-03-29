// API endpoints
const API_BASE_URL = '/api';
const ENDPOINTS = {
    USERS: `${API_BASE_URL}/users`
};

// Initialize the application
document.addEventListener('DOMContentLoaded', () => {
    fetchAndRenderUsers();
});

// Fetch users from the API
async function fetchAndRenderUsers() {
    try {
        const response = await fetch(ENDPOINTS.USERS);
        if (!response.ok) throw new Error('Failed to fetch users');
        const result = await response.json();
        const users = result.userDTOList;
        renderUserList(users);
    } catch (error) {
        console.error('Error fetching users:', error);
    }
}

// Render user list
function renderUserList(users) {
    const userListElement = document.getElementById('userList');
    userListElement.innerHTML = '';

    users.forEach(user => {
        const userElement = document.createElement('div');
        userElement.className = 'user-item';

        const profileUrl = user.binaryContentDTO
            ? `data:${user.binaryContentDTO.contentType};base64,${user.binaryContentDTO.bytes}`
            : '/default-avatar.png';

        userElement.innerHTML = `
            <img src="${profileUrl}" alt="${user.username}" class="user-avatar">
            <div class="user-info">
                <div class="user-name">${user.username}</div>
                <div class="user-email">${user.email}</div>
            </div>
            <div class="status-badge ${user.isLogin ? 'online' : 'offline'}">
                ${user.isLogin ? '온라인' : '오프라인'}
            </div>
        `;

        userListElement.appendChild(userElement);
    });
}