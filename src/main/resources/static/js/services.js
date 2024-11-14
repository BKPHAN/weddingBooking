// JavaScript cho các tính năng tương tác nếu cần (ví dụ như nhấp vào các ưu đãi để xem chi tiết)
document.addEventListener('DOMContentLoaded', function() {
    const policyItems = document.querySelectorAll('.policy-item');

    policyItems.forEach(item => {
        item.addEventListener('click', () => {
            alert("Chi tiết ưu đãi: " + item.querySelector('h2').textContent);
        });
    });
});


document.getElementById("bookingButton").addEventListener("click", function() {
    window.location.href = "/booking";
});