let currentSlide = 0;
const slides = document.querySelectorAll(".slides img");
const titles = document.querySelectorAll(".post-title");

function showSlide(index) {
    // Ẩn tất cả các ảnh và tiêu đề
    slides.forEach(slide => slide.classList.remove("active"));
    titles.forEach(title => title.classList.remove("active"));
    // Hiển thị ảnh và tiêu đề tương ứng với index
    slides[index].classList.add("active");
    titles[index].classList.add("active");
}

function moveSlide(step) {
    currentSlide = (currentSlide + step + slides.length) % slides.length;
    showSlide(currentSlide);
}

// Hiển thị slide đầu tiên
showSlide(currentSlide);

// Nếu bạn muốn tự động chuyển slide mỗi 3 giây
// setInterval(() => {
//     moveSlide(1);
// }, 3000);