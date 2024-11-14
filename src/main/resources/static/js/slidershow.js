// script.js

let index = 0;
const slides = document.querySelectorAll('.slides img');

function showSlide() {
    slides.forEach((slide, i) => {
        slide.style.display = i === index ? 'block' : 'none';
    });
    index = (index + 1) % slides.length;
}

setInterval(showSlide, 5000); // 3 giây chuyển ảnh
showSlide();


// Hiệu ứng cuộn mượt
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
        document.querySelector(this.getAttribute('href')).scrollIntoView({
            behavior: 'smooth'
        });
    });
});
