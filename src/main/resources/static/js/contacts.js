const sliderContainer = document.getElementById('feedback-slider');
const feedbackForm = document.getElementById('feedbackForm');
const phoneInput = document.getElementById('phone');

let currentSlideIndex = 0;
let slides = [];
let sliderInterval;

document.addEventListener('DOMContentLoaded', async () => {
    await fetchFeedbacks();
    startSlider();
});

async function fetchFeedbacks() {
    try {
        const response = await fetch('/api/feedbacks');
        const result = await response.json();

        if (!response.ok || !result.success) {
            throw new Error(result.message || 'Không thể tải phản hồi khách hàng');
        }

        renderSlides(result.data || []);
    } catch (error) {
        console.error('Error fetching feedbacks:', error);
        sliderContainer.innerHTML = '<p>Chưa có phản hồi nào.</p>';
    }
}

function renderSlides(feedbacks) {
    sliderContainer.innerHTML = '';
    slides = feedbacks.map((feedback) => {
        const slide = document.createElement('div');
        slide.className = 'slide';
        slide.innerHTML = `
            <h4>${feedback.fullName}</h4>
            <p>"${feedback.message}"</p>
            <small>Trạng thái: ${feedback.flag ?? 'NEW'}${feedback.assignedTo ? ` • NV: ${feedback.assignedTo}` : ''}</small>
        `;
        sliderContainer.appendChild(slide);
        return slide;
    });

    if (slides.length > 0) {
        slides[0].classList.add('active');
    }
}

function startSlider() {
    if (sliderInterval) {
        clearInterval(sliderInterval);
    }

    sliderInterval = setInterval(() => {
        if (slides.length === 0) {
            return;
        }

        slides[currentSlideIndex].classList.remove('active');
        currentSlideIndex = (currentSlideIndex + 1) % slides.length;
        slides[currentSlideIndex].classList.add('active');
    }, 5000);
}

feedbackForm.addEventListener('submit', async (event) => {
    event.preventDefault();

    const payload = {
        fullName: document.getElementById('fullName').value.trim(),
        email: document.getElementById('email').value.trim(),
        phone: phoneInput.value.trim(),
        subject: document.getElementById('subject').value.trim(),
        message: document.getElementById('message').value.trim()
    };

    try {
        const response = await fetch('/api/feedbacks', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        const result = await response.json();

        if (!response.ok || !result.success) {
            throw new Error(result.message || 'Không thể gửi phản hồi');
        }

        alert(result.message);
        feedbackForm.reset();
        await fetchFeedbacks();
    } catch (error) {
        console.error('Error submitting feedback:', error);
        alert(error.message);
    }
});

phoneInput.addEventListener('input', function () {
    this.value = this.value.replace(/[^0-9]/g, '');
});
