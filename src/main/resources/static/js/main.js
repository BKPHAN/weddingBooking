/* Main JS - Handles Global Logic (Auth, UI) */

// =======================
// AUTHENTICATION LOGIC
// =======================

// =======================
// TOAST NOTIFICATIONS
// =======================
function showToast(type, message) {
    let container = document.querySelector('.toast-container');
    if (!container) {
        container = document.createElement('div');
        container.className = 'toast-container';
        document.body.appendChild(container);
    }

    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.innerHTML = `
        <div class="toast-content">
            ${type === 'success' ?
            '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>' :
            '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="15" y1="9" x2="9" y2="15"></line><line x1="9" y1="9" x2="15" y2="15"></line></svg>'}
            <span>${message}</span>
        </div>
        <button class="toast-close" onclick="this.parentElement.remove()">&times;</button>
    `;

    container.appendChild(toast);

    // Trigger animation
    requestAnimationFrame(() => toast.classList.add('show'));

    // Auto remove
    setTimeout(() => {
        toast.classList.remove('show');
        toast.classList.add('hide');
        toast.addEventListener('animationend', () => toast.remove());
    }, 3000);
}

// =======================
// CAROUSEL LOGIC
// =======================
function initCarousels() {
    const carousels = document.querySelectorAll('.carousel-container');

    carousels.forEach(carousel => {
        const track = carousel.querySelector('.carousel-track');
        const slides = Array.from(track.children);
        const nextBtn = carousel.querySelector('.carousel-btn.next');
        const prevBtn = carousel.querySelector('.carousel-btn.prev');
        const intervalTime = parseInt(carousel.dataset.interval) || 5000;

        let currentIndex = 0;
        let slideWidth = slides[0].getBoundingClientRect().width;
        let slidesPerView = Math.round(carousel.offsetWidth / slideWidth);
        let maxIndex = Math.max(0, slides.length - slidesPerView);
        let autoSlideTimer;

        // Update dimensions on resize
        const updateDimensions = () => {
            slideWidth = slides[0].getBoundingClientRect().width;
            slidesPerView = Math.round(carousel.offsetWidth / slideWidth);
            maxIndex = Math.max(0, slides.length - slidesPerView);
            // Re-align track
            moveToSlide(currentIndex);
        };

        window.addEventListener('resize', updateDimensions);

        const moveToSlide = (index) => {
            // BOUNDARY CHECKS
            if (index < 0) index = maxIndex;
            if (index > maxIndex) index = 0;

            currentIndex = index;
            const amountToMove = -(slideWidth * currentIndex);
            track.style.transform = `translateX(${amountToMove}px)`;
        };

        // Navigation
        if (nextBtn) {
            nextBtn.addEventListener('click', () => {
                moveToSlide(currentIndex + 1);
                resetTimer();
            });
        }

        if (prevBtn) {
            prevBtn.addEventListener('click', () => {
                moveToSlide(currentIndex - 1);
                resetTimer();
            });
        }

        // Auto Slide
        const startTimer = () => {
            autoSlideTimer = setInterval(() => {
                moveToSlide(currentIndex + 1);
            }, intervalTime);
        };

        const resetTimer = () => {
            clearInterval(autoSlideTimer);
            startTimer();
        };

        // Pause on hover
        carousel.addEventListener('mouseenter', () => clearInterval(autoSlideTimer));
        carousel.addEventListener('mouseleave', startTimer);

        // Init
        // Wait a bit for layout to settle
        requestAnimationFrame(() => {
            updateDimensions();
            startTimer();
        });
    });
}

// Initialize on Load
document.addEventListener('DOMContentLoaded', () => {
    // ... existing init code if any

    // Check for Session Messages
    if (sessionStorage.getItem('loginSuccess')) {
        showToast('success', 'Đăng nhập thành công!');
        sessionStorage.removeItem('loginSuccess');
    }

    initCarousels();
});

async function handleLogin(event) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    const data = Object.fromEntries(formData.entries());

    try {
        const response = await fetch('/api/v1/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            const authResponse = await response.json();

            // Set session flag specific for User or Admin
            sessionStorage.setItem('loginSuccess', 'true');

            // Redirect logic
            if (authResponse.primaryRole === 'ADMIN') {
                window.location.href = '/admin/dashboard';
            } else {
                window.location.reload();
            }
        } else {
            const error = await response.json();
            showToast('error', error.message || 'Tên đăng nhập hoặc mật khẩu không đúng');
        }
    } catch (err) {
        console.error('Login error:', err);
        showToast('error', 'Có lỗi xảy ra khi đăng nhập. Vui lòng thử lại.');
    }
}

async function handleRegister(event) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    const data = Object.fromEntries(formData.entries());

    try {
        const response = await fetch('/api/v1/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            showToast('success', 'Đăng ký thành công! Vui lòng đăng nhập.');
            switchTab('login');
            form.reset();
        } else {
            const error = await response.json();
            showToast('error', error.message || 'Đăng ký thất bại');
        }
    } catch (err) {
        console.error('Register error:', err);
        showToast('error', 'Có lỗi xảy ra khi đăng ký.');
    }
}


// =======================
// UI HELPERS (MODAL & TABS)
// =======================

function openAuthModal() {
    const modal = document.getElementById('authModal');
    if (modal) {
        modal.classList.add('open');
    }
}

function closeAuthModal() {
    const modal = document.getElementById('authModal');
    if (modal) {
        modal.classList.remove('open');
    }
}

function switchTab(tab) {
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');
    const tabLogin = document.getElementById('tab-login');
    const tabRegister = document.getElementById('tab-register');

    if (!loginForm || !registerForm) return;

    if (tab === 'login') {
        loginForm.style.display = 'block';
        registerForm.style.display = 'none';

        tabLogin.style.background = '#fff';
        tabLogin.style.color = 'var(--color-teal)';
        tabRegister.style.background = 'transparent';
        tabRegister.style.color = '#fff';
    } else {
        loginForm.style.display = 'none';
        registerForm.style.display = 'block';

        tabRegister.style.background = '#fff';
        tabRegister.style.color = 'var(--color-teal)';
        tabLogin.style.background = 'transparent';
        tabLogin.style.color = '#fff';
    }
}

// Toggle Password Visibility
function togglePassword(inputId, btn) {
    const input = document.getElementById(inputId);
    if (!input) return;

    const type = input.getAttribute('type') === 'password' ? 'text' : 'password';
    input.setAttribute('type', type);

    // Toggle Icon
    if (type === 'text') {
        // Show Eye Slash (Hidden state icon meaning "Click to Hide")
        btn.innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path><line x1="1" y1="1" x2="23" y2="23"></line></svg>`;
        btn.style.color = 'var(--color-teal)';
    } else {
        // Show Eye (Visible state icon meaning "Click to Show")
        btn.innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path><circle cx="12" cy="12" r="3"></circle></svg>`;
        btn.style.color = 'var(--color-text-muted)';
    }
}

// Dropdown Logic
function toggleDropdown() {
    const dropdown = document.getElementById('userDropdownMenu');
    if (dropdown) {
        dropdown.classList.toggle('show');
    }
}

// Close dropdown when clicking outside
document.addEventListener('click', function (event) {
    const dropdown = document.getElementById('userDropdownMenu');
    const toggle = document.querySelector('.dropdown-toggle');

    if (dropdown && toggle && !dropdown.contains(event.target) && !toggle.contains(event.target)) {
        dropdown.classList.remove('show');
    }
});

// Auto open modal if URL has ?login
document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('login')) {
        openAuthModal();
    }
});

// Navbar Sticky Effect (Optional)
window.addEventListener('scroll', function () {
    const header = document.querySelector('.header');
    if (window.scrollY > 50) {
        // header.classList.add('scrolled'); // Define .scrolled in CSS if needed
    } else {
        // header.classList.remove('scrolled');
    }
});

// Catalog Tab Switching
// Catalog Tab Switching Logic
function switchCatalogTab(tabName) {
    // Remove active class from all tabs
    document.querySelectorAll('.catalog-tab').forEach(t => t.classList.remove('active'));

    // Remove active class from all grids
    document.querySelectorAll('.item-grid').forEach(g => g.classList.remove('active'));

    // Activate the clicked tab (using data-tab attribute)
    const activeBtn = document.querySelector(`.catalog-tab[data-tab="${tabName}"]`);
    if (activeBtn) {
        activeBtn.classList.add('active');
    }

    // Activate the corresponding grid
    const targetGrid = document.getElementById(`${tabName}-grid`);
    if (targetGrid) {
        targetGrid.classList.add('active');
    }
}

// Logout Handler
// Logout Handler (Handled by Server & Cookies)
function handleClientLogout() {
    // No need to clear localStorage as we don't store tokens there anymore.
    // Browser will follow the href="/logout" link automatically.
}

// Initialize Catalog Tabs (Event Listeners)
document.addEventListener('DOMContentLoaded', () => {
    const tabs = document.querySelectorAll('.catalog-tab');
    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            const tabName = tab.getAttribute('data-tab');
            if (tabName) {
                switchCatalogTab(tabName);
            }
        });
    });
});
