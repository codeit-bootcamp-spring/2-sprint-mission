// API endpoints
const API_BASE_URL = '/api';
const ENDPOINTS = {
  USERS: `${API_BASE_URL}/user/findAll`,
  BINARY_CONTENT: `${API_BASE_URL}/binaryContent/find`
};

// Initialize the application
document.addEventListener('DOMContentLoaded', () => {
  fetchAndRenderUsers();
});

// Fetch users from the API
async function fetchAndRenderUsers() {
  try {
    const response = await fetch(ENDPOINTS.USERS);
    if (!response.ok) {
      throw new Error('Failed to fetch users');
    }
    const users = await response.json();
    renderUserList(users);
  } catch (error) {
    console.error('Error fetching users:', error);
  }
}

// Fetch user profile image
async function fetchUserProfile(profileId) {
  try {
    const response = await fetch(
        `${ENDPOINTS.BINARY_CONTENT}?binaryContentId=${profileId}`);
    if (!response.ok) {
      throw new Error('Failed to fetch profile');
    }
    const profile = await response.json();

    console.log('filePath:', profile.filePath);

    return profile.filePath;
  } catch (error) {
    console.error('Error fetching profile:', error);
    return '/default-avatar.png'; // fallback
  }
}

// Render user list
async function renderUserList(users) {
  const userListElement = document.getElementById('userList');
  userListElement.innerHTML = ''; // Clear existing content

  for (const user of users) {
    console.log("유저:", user.username, "| 온라인 상태:", user.online);

    const userElement = document.createElement('div');
    userElement.className = 'user-item';

    // Get profile image URL
    const profileUrl = user.profileId ?
        await fetchUserProfile(user.profileId) :
        '/default-avatar.png';

    userElement.innerHTML = `
            <img src="${profileUrl}" alt="${user.username}" class="user-avatar">
            <div class="user-info">
                <div class="user-name">${user.username}</div>
                <div class="user-email">${user.email}</div>
            </div>
            <div class="status-badge ${user.online ? 'online' : 'offline'}">
                ${user.online ? '   온라인' : '오프라인'}
            </div>
        `;

    userListElement.appendChild(userElement);
  }
}
