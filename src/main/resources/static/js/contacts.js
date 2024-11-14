const sliderContainer = document.getElementById('feedback-slider');

// Hàm khởi động slider khi trang được tải
document.addEventListener('DOMContentLoaded', () => {
    // Gọi GET để lấy danh sách feedbacks khi trang được tải
    fetchFeedbacks();
    startSlider(); // Khởi động slider ngay khi trang tải
});

// Hàm GET để lấy các feedbacks mới nhất và hiển thị trong slider
async function fetchFeedbacks() {
    try {
        // Gọi API GET để lấy danh sách feedbacks
        const response = await fetch('/api/feedbacks');

        if (!response.ok) {
            throw new Error('Failed to fetch feedbacks');
        }

        const feedbacks = await response.json();

        // Xóa các slide cũ
        sliderContainer.innerHTML = '';

        // Tạo các slide mới từ danh sách feedbacks nhận được
        feedbacks.forEach(feedback => {
            const slide = document.createElement('div');
            slide.classList.add('slide');
            slide.innerHTML = `
                <h4>${feedback.username}</h4>
                <p style="color:#000;">"${feedback.message}"</p>
            `;
            sliderContainer.appendChild(slide);
        });

        // Khởi động slider nếu có feedbacks
        if (feedbacks.length > 0) {
            startSlider();
        }
    } catch (error) {
        console.error('Error fetching feedbacks:', error);
    }
}

// Hàm gửi dữ liệu khi nhấn submit
async function submitFeedback(event) {
    event.preventDefault(); // Ngăn hành động submit mặc định của form

    const username = document.getElementById("username").value;
    const email = document.getElementById("email").value;
    const message = document.getElementById("message").value;

    const data = {username, email, message};

    try {
        // Gửi dữ liệu tới API
        const response = await fetch('/api/feedbacks', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        });

        if (!response.ok) {
            throw new Error('Failed to submit feedback');
        }

        const result = await response.json();

        // Kiểm tra phản hồi từ API (true/false)
        if (result === true) {
            if (confirm("bạn đã gửi phản hồi thành công! Chúng tôi xin cảm ơn!"))
                // Sau khi gửi thành công, gọi lại GET để lấy các phản hồi mới nhất
                location.reload();
        } else {
            alert('Failed to submit feedback');
        }
    } catch (error) {
        console.error('Error submitting feedback:', error);
    }
}

// Hàm khởi động slider
function startSlider() {
    let index = 0;
    const slides = document.querySelectorAll('.slider .slide');
    const sliderWidth = sliderContainer.clientWidth;

    function showSlides() {
        // Ẩn slide trước đó
        slides.forEach((slide, i) => {
            if (i !== index) {
                slide.style.opacity = '0';
                slide.style.transform = 'translateX(-50px)';
            }
        });

        // Hiển thị slide hiện tại
        slides[index].style.opacity = '1';
        slides[index].style.transform = 'translateX(0)';

        // Kiểm tra nếu tất cả các slide đã vượt qua chiều rộng của sliderContainer
        index += 1;

        // Nếu đã chạy hết, đặt index về 0 để lặp lại
        if ((index * 250) >= sliderWidth) {
            index = 0;
        }

        // Nếu đã chạy hết, đặt index về 0 để lặp lại
        if (index >= slides.length) {
            index = 0;
        }
    }

    setInterval(showSlides, 5000); // Chuyển slide mỗi 5 giây
    showSlides(); // Hiển thị slide đầu tiên
}

// Gắn sự kiện submit cho form
document.getElementById("feedbackForm").addEventListener("submit", submitFeedback);


function toggleBranch(branchId) {
    // Lấy tất cả các chi nhánh
    var allBranches = document.querySelectorAll('.contacts-info');

    // Ẩn tất cả các chi nhánh trước khi hiển thị chi nhánh mới
    allBranches.forEach(function (branch) {
        branch.style.display = 'none';
    });

    // Lấy chi nhánh được nhấn và hiển thị nó
    var branchToToggle = document.getElementById(branchId);
    if (branchToToggle.style.display === "none" || branchToToggle.style.display === "") {
        branchToToggle.style.display = "block";
    } else {
        branchToToggle.style.display = "none";
    }
}
