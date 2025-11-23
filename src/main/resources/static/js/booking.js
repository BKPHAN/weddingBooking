const bookingForm = document.getElementById('bookingForm');
const confirmationBox = document.getElementById('confirmation');
const confirmationText = document.getElementById('confirmationText');

bookingForm.addEventListener('submit', async (event) => {
    event.preventDefault();

    const services = Array.from(document.querySelectorAll('input[name="services"]:checked')).map(
        (item) => item.value
    );

    const payload = {
        brideName: document.getElementById('brideName').value.trim(),
        groomName: document.getElementById('groomName').value.trim(),
        phone: document.getElementById('phone').value.trim(),
        email: document.getElementById('email').value.trim(),
        eventDate: document.getElementById('eventDate').value,
        timeSlot: document.getElementById('timeSlot').value,
        hallId: document.getElementById('hallId').value
            ? Number(document.getElementById('hallId').value)
            : null,
        guestCount: Number(document.getElementById('guestCount').value),
        budgetMin: document.getElementById('budgetMin').value
            ? Number(document.getElementById('budgetMin').value)
            : null,
        budgetMax: document.getElementById('budgetMax').value
            ? Number(document.getElementById('budgetMax').value)
            : null,
        notes: document.getElementById('notes').value.trim(),
        services
    };

    try {
        const response = await fetch('/api/booking', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        const result = await response.json();

        if (!response.ok || !result.success) {
            throw new Error(result.message || 'Không thể gửi yêu cầu đặt tiệc');
        }

        bookingForm.reset();
        document.querySelectorAll('input[name="services"]').forEach((item) => (item.checked = false));

        const summary = result.data || {};
        const hallInfo = summary.hallName ? `Sảnh: ${summary.hallName}` : 'Sảnh: Đang được tư vấn';
        const servicesInfo =
            summary.services && summary.services.length > 0
                ? `Dịch vụ bổ sung: ${summary.services.join(', ')}`
                : 'Dịch vụ bổ sung: chưa chọn';

        confirmationText.innerHTML = `
            <strong>${result.message}</strong><br>
            Người liên hệ: ${summary.brideName ?? ''} & ${summary.groomName ?? ''}<br>
            Ngày tổ chức: ${summary.eventDate ?? ''} (${summary.timeSlot ?? ''})<br>
            ${hallInfo}<br>
            ${servicesInfo}
        `;
        confirmationBox.hidden = false;
    } catch (error) {
        console.error('Lỗi khi gửi yêu cầu đặt tiệc:', error);
        alert(error.message);
    }
});

document.getElementById('phone').addEventListener('input', function () {
    this.value = this.value.replace(/[^0-9]/g, '');
});
