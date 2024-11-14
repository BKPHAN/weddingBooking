document.getElementById('bookingForm').addEventListener('submit', async function (event) {
    event.preventDefault();

    // Lấy thông tin từ form
    const customerName = document.getElementById('name').value;
    const phone = document.getElementById('phone').value;
    const email = document.getElementById('email').value;
    const weddingDate = document.getElementById('date').value;
    const guestCount = document.getElementById('guests').value;
    const offer = document.getElementById('offer').value;

    // Tạo dữ liệu gửi đến API
    const bookingData = {customerName, phone, email, weddingDate, guestCount, offer};

    try {
        // Gửi dữ liệu tới API
        const response = await fetch('/api/booking', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(bookingData),
        });

        // Kiểm tra phản hồi từ API
        if (!response.ok) {
            alert('Gửi thông tin đặt tiệc thất bại! Vui lòng kiểm tra lại thông tin');
        }

        const result = await response.json();

        // Kiểm tra phản hồi từ API (true/false)
        if (result === true) {
            if(confirm("Thông tin đặt tiệc đã được gửi thành công! Chúng tôi xin cảm ơn đã tin tưởng sử dụng dịch vụ của chúng tôi!"))
                // Sau khi gửi thành công, gọi lại GET để lấy các phản hồi mới nhất
                location.reload();
        } else {
            alert('Failed to submit feedback');
        }
    } catch (error) {
        console.error('Lỗi khi gửi thông tin đặt tiệc:', error);
    }
    // Xóa dữ liệu trong form
    document.getElementById('bookingForm').reset();
});


document.getElementById('phone').addEventListener('input', function (e) {
    this.value = this.value.replace(/[^0-9]/g, ''); // Loại bỏ tất cả các ký tự không phải số
});