/* Main JS - Handles Global Logic (Auth, UI) */

// =======================
// AUTHENTICATION LOGIC
// =======================

// =======================
// HERO BANNER SLIDESHOW
// =======================
let heroSlideIndex = 0;
let heroSlideTimer = null;

function moveHeroSlide(direction) {
    const container = document.querySelector('.hero-slides-container');
    const slides = document.querySelectorAll('.hero-slide');
    if (!container || slides.length === 0) return;

    heroSlideIndex += direction;

    // Loop around
    if (heroSlideIndex >= slides.length) heroSlideIndex = 0;
    if (heroSlideIndex < 0) heroSlideIndex = slides.length - 1;

    updateHeroSlide();
    resetHeroTimer();
}

function goToHeroSlide(index) {
    heroSlideIndex = index;
    updateHeroSlide();
    resetHeroTimer();
}

function updateHeroSlide() {
    const container = document.querySelector('.hero-slides-container');
    if (!container) return;
    const offset = heroSlideIndex * -33.333;
    container.style.transform = `translateX(${offset}%)`;
}

function resetHeroTimer() {
    clearInterval(heroSlideTimer);
    startHeroTimer();
}

function startHeroTimer() {
    heroSlideTimer = setInterval(() => {
        moveHeroSlide(1);
    }, 5000); // Change every 5 seconds
}

function initHeroSlider() {
    const slider = document.getElementById('heroSlider');
    if (!slider) return;

    // Pause on hover
    slider.addEventListener('mouseenter', () => clearInterval(heroSlideTimer));
    slider.addEventListener('mouseleave', startHeroTimer);

    // Start auto-slide
    startHeroTimer();
}

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
        const is3D = carousel.classList.contains('carousel-3d');

        let currentIndex = 0;
        let slideCount = slides.length;

        // STANDARD VARS
        let slideWidth = slides[0].getBoundingClientRect().width;
        let slidesPerView = Math.round(carousel.offsetWidth / slideWidth);
        let maxIndex = Math.max(0, slides.length - slidesPerView);

        // 3D CYLINDER VARS
        let theta = 0;
        let radius = 0;

        if (is3D) {
            theta = 360 / slideCount;
            // Radius = (width / 2) / tan(PI / count)
            // Use 120 for balanced spacing between slides
            radius = Math.round((150 / 2) / Math.tan(Math.PI / slideCount));

            // Initial Positioning
            slides.forEach((slide, index) => {
                slide.style.transform = `rotateY(${index * theta}deg) translateZ(${radius}px)`;
                if (index === 0) slide.classList.add('active');
            });
        }

        // Update dimensions on resize
        const updateDimensions = () => {
            if (!is3D) {
                slideWidth = slides[0].getBoundingClientRect().width;
                slidesPerView = Math.round(carousel.offsetWidth / slideWidth);
                maxIndex = Math.max(0, slides.length - slidesPerView);
                moveToSlide(currentIndex);
            }
        };

        window.addEventListener('resize', updateDimensions);

        const moveToSlide = (index) => {
            if (is3D) {
                // CYLINDER LOGIC
                currentIndex = index;
                const angle = theta * currentIndex * -1;
                track.style.transform = `translateZ(${-radius}px) rotateY(${angle}deg)`;

                // Calculate Active Index (circular)
                let activeIndex = currentIndex % slideCount;
                if (activeIndex < 0) activeIndex += slideCount;

                slides.forEach((slide, i) => {
                    slide.classList.remove('active');
                    if (i === activeIndex) slide.classList.add('active');
                });
            } else {
                // STANDARD LOGIC
                if (index < 0) index = maxIndex;
                if (index > maxIndex) index = 0;

                currentIndex = index;
                const amountToMove = -(slideWidth * currentIndex);
                track.style.transform = `translateX(${amountToMove}px)`;
            }
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
        setTimeout(() => {
            updateDimensions();
            if (is3D) moveToSlide(0);
            startTimer();
        }, 100);
    });
}

// Initialize on Load
document.addEventListener('DOMContentLoaded', () => {
    // Check for Session Messages
    if (sessionStorage.getItem('loginSuccess')) {
        showToast('success', 'Đăng nhập thành công!');
        sessionStorage.removeItem('loginSuccess');
    }

    initCarousels();
    initHeroSlider(); // Hero Banner Slideshow

    // Initialize Snowfall
    createSnowflakes();

    // Initialize Theme
    const savedTheme = localStorage.getItem('theme');
    const themeBtnIcon = document.querySelector('.theme-toggle-btn i');
    if (savedTheme === 'dark') {
        document.documentElement.setAttribute('data-theme', 'dark');
        if (themeBtnIcon) {
            themeBtnIcon.classList.remove('fa-moon');
            themeBtnIcon.classList.add('fa-sun');
        }
    }
});

function toggleTheme() {
    const currentTheme = document.documentElement.getAttribute('data-theme');
    const themeBtnIcon = document.querySelector('.theme-toggle-btn i');
    let newTheme = 'light';

    if (currentTheme === 'dark') {
        document.documentElement.removeAttribute('data-theme');
        localStorage.setItem('theme', 'light');
        if (themeBtnIcon) {
            themeBtnIcon.classList.remove('fa-sun');
            themeBtnIcon.classList.add('fa-moon');
        }
    } else {
        document.documentElement.setAttribute('data-theme', 'dark');
        localStorage.setItem('theme', 'dark');
        newTheme = 'dark';
        if (themeBtnIcon) {
            themeBtnIcon.classList.remove('fa-moon');
            themeBtnIcon.classList.add('fa-sun');
        }
    }
}

function createSnowflakes() {
    const container = document.createElement('div');
    container.id = 'snow-container';
    document.body.prepend(container);

    const characters = ['❄', '❅', '❆', '•'];

    // Create snowflakes periodically
    setInterval(() => {
        // Limit max flakes to prevent performance issues
        if (container.childElementCount > 50) return;

        const snowflake = document.createElement('div');
        snowflake.classList.add('snowflake');
        snowflake.innerText = characters[Math.floor(Math.random() * characters.length)];

        snowflake.style.left = Math.random() * 100 + 'vw';
        snowflake.style.opacity = Math.random() * 0.7 + 0.3;
        snowflake.style.fontSize = (Math.random() * 15 + 10) + 'px';

        const duration = Math.random() * 5 + 5; // 5-10 seconds
        snowflake.style.animationDuration = duration + 's';

        container.appendChild(snowflake);

        // Remove after animation
        setTimeout(() => {
            snowflake.remove();
        }, duration * 1000);
    }, 400); // Slower creation rate
}

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


/* =======================
   ACTIVE MENU HIGHLIGHT
======================= */
document.addEventListener('DOMContentLoaded', () => {
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.nav-link');

    navLinks.forEach(link => {
        const href = link.getAttribute('href');
        if (href === currentPath) {
            link.classList.add('active');
        } else if (href !== '/' && currentPath.startsWith(href)) {
            link.classList.add('active');
        }
    });
});
