document.addEventListener('DOMContentLoaded', () => {
    const navbarMenu = document.querySelector('.navbar .links');
    const hamburgerBtn = document.querySelector('.hamburger-btn');
    const hideMenuBtn = navbarMenu?.querySelector('.close-btn');
    const showPopupBtn = document.querySelector('.login-btn');
    const formPopup = document.querySelector('.form-popup');
    const hidePopupBtn = formPopup?.querySelector('.close-btn');
    const signupLoginLinks = formPopup ? formPopup.querySelectorAll('.bottom-link a') : [];

    const loginForm = document.getElementById('login-form');
    const signupForm = document.getElementById('signup-form');

    if (formPopup) {
        document.body.classList.add('show-popup');
    }

    if (hamburgerBtn && navbarMenu) {
        hamburgerBtn.addEventListener('click', () => {
            navbarMenu.classList.toggle('show-menu');
        });
    }

    if (hideMenuBtn && hamburgerBtn) {
        hideMenuBtn.addEventListener('click', () => hamburgerBtn.click());
    }

    if (showPopupBtn) {
        showPopupBtn.addEventListener('click', () => {
            document.body.classList.toggle('show-popup');
        });
    }

    if (hidePopupBtn && showPopupBtn) {
        hidePopupBtn.addEventListener('click', () => showPopupBtn.click());
    }

    signupLoginLinks.forEach((link) => {
        link.addEventListener('click', (event) => {
            event.preventDefault();
            if (!formPopup) {
                return;
            }
            formPopup.classList[link.id === 'signup-link' ? 'add' : 'remove']('show-signup');
        });
    });

    loginForm?.addEventListener('submit', async (event) => {
        event.preventDefault();

        const identifier = document.getElementById('login-identifier')?.value.trim();
        const password = document.getElementById('login-password')?.value ?? '';

        if (!identifier || !password) {
            alert('Please enter username/email and password.');
            return;
        }

        try {
            const response = await fetch('/api/v1/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    usernameOrEmail: identifier,
                    password
                })
            });

            if (!response.ok) {
                const errorPayload = await response.json().catch(() => null);
                throw new Error(errorPayload?.message || 'Login failed');
            }

            const auth = await response.json();
            if (auth.accessToken) {
                localStorage.setItem('accessToken', auth.accessToken);
            }
            if (auth.refreshToken) {
                localStorage.setItem('refreshToken', auth.refreshToken);
            }

            if (auth.primaryRole) {
                localStorage.setItem('primaryRole', auth.primaryRole);
            }

            const redirectUrl = auth.redirectUrl
                || (auth.primaryRole === 'ADMIN' ? '/admin/dashboard' : '/user/dashboard');

            window.location.href = redirectUrl;
        } catch (error) {
            console.error('Login error:', error);
            alert(error.message || 'Unable to login. Please try again.');
        }
    });

    signupForm?.addEventListener('submit', async (event) => {
        event.preventDefault();

        const fullName = document.getElementById('signup-fullname')?.value.trim();
        const username = document.getElementById('signup-username')?.value.trim();
        const email = document.getElementById('signup-email')?.value.trim();
        const password = document.getElementById('signup-password')?.value ?? '';
        const policyChecked = document.getElementById('signup-policy')?.checked ?? false;

        if (!policyChecked) {
            alert('You must agree to the Terms & Conditions.');
            return;
        }

        if (!fullName || !username || !email || !password) {
            alert('Please fill in all required fields.');
            return;
        }

        try {
            const response = await fetch('/api/v1/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    fullName,
                    username,
                    email,
                    password
                })
            });

            if (!response.ok) {
                const errorPayload = await response.json().catch(() => null);
                throw new Error(errorPayload?.message || 'Registration failed');
            }

            alert('Registration successful. Please log in.');
            signupForm.reset();
            if (formPopup?.classList.contains('show-signup')) {
                formPopup.classList.remove('show-signup');
            }
        } catch (error) {
            console.error('Registration error:', error);
            alert(error.message || 'Unable to register. Please try again.');
        }
    });
});
