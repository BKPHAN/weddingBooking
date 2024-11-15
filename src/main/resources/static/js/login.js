const navbarMenu = document.querySelector(".navbar .links");
const hamburgerBtn = document.querySelector(".hamburger-btn");
const hideMenuBtn = navbarMenu.querySelector(".close-btn");
const showPopupBtn = document.querySelector(".login-btn");
const formPopup = document.querySelector(".form-popup");
const hidePopupBtn = formPopup.querySelector(".close-btn");
const signupLoginLink = formPopup.querySelectorAll(".bottom-link a");

// Show mobile menu
hamburgerBtn.addEventListener("click", () => {
    navbarMenu.classList.toggle("show-menu");
});

// Hide mobile menu
hideMenuBtn.addEventListener("click", () =>  hamburgerBtn.click());

// Show login popup
showPopupBtn.addEventListener("click", () => {
    document.body.classList.toggle("show-popup");
});

// Hide login popup
hidePopupBtn.addEventListener("click", () => showPopupBtn.click());

// Show or hide signup form
signupLoginLink.forEach(link => {
    link.addEventListener("click", (e) => {
        e.preventDefault();
        formPopup.classList[link.id === 'signup-link' ? 'add' : 'remove']("show-signup");
    });
});

document.getElementById('login-form').addEventListener('submit', async function (event) {
    event.preventDefault(); // Ngăn không cho form submit mặc định

    // Lấy giá trị từ form
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    try {
        // Gửi yêu cầu POST tới API đăng nhập
        const response = await fetch('/api/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email, password }),
        });

        // Xử lý kết quả
        if (response.ok) {
                // Nếu đăng nhập thành công, chuyển hướng sang '/wedding'
                window.location.href = '/wedding'; // Chuyển hướng trang
        } else {
            // Nếu thất bại, hiển thị thông báo lỗi
            const errorMessage = await response.text();
            alert(errorMessage || 'Login failed');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred. Please try again.');
    }
});

document.getElementById('signup-form').addEventListener('submit', async function (event) {
    event.preventDefault(); // Ngăn không cho form submit mặc định

    // Lấy giá trị từ form
    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const isPolicyChecked = document.getElementById('policy').checked;

    if (!isPolicyChecked) {
        alert("You must agree to the Terms & Conditions.");
        return;
    }

    try {
        // Gửi yêu cầu POST tới API đăng ký
        const response = await fetch('/api/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ name, email, password }),
        });

        // Xử lý kết quả
        if (response.ok) {
            alert('Registration successful');
            window.location.href = '/login'; // Chuyển hướng đến trang đăng nhập
        } else {
            const errorMessage = await response.text();
            alert(errorMessage || 'Registration failed');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred. Please try again.');
    }
});
