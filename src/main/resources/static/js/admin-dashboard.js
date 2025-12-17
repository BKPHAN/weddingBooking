document.addEventListener('DOMContentLoaded', async () => {
    const errorBanner = document.getElementById('dashboard-error');
    const bookingStatusBars = document.getElementById('booking-status-bars');
    const upcomingBody = document.getElementById('upcoming-bookings-body');
    const bookingFilter = document.getElementById('booking-filter');
    const contactBody = document.getElementById('recent-contacts-body');
    const state = {
        upcomingBookings: []
    };

    try {
        const response = await fetch('/api/v1/admin/dashboard/overview');
        if (!response.ok) {
            if (response.status === 401 || response.status === 403) {
                throw new Error('Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.');
            }
            throw new Error('Không thể tải dữ liệu bảng điều khiển');
        }

        const payload = await response.json();
        if (!payload.success) {
            throw new Error(payload.message || 'Không thể tải dữ liệu bảng điều khiển');
        }

        const data = payload.data;

        document.getElementById('total-bookings').textContent = data.totalBookings ?? 0;
        document.getElementById('expected-revenue').textContent = formatCurrency(data.expectedRevenue);
        document.getElementById('pending-contacts').textContent = data.pendingContacts ?? 0;

        // Initialize Charts
        if (typeof Chart !== 'undefined') {
            initCharts(data);
        }

        state.upcomingBookings = data.upcomingBookings || [];
        renderUpcoming(upcomingBody, state.upcomingBookings, Number(bookingFilter.value));

        // ... existing contact code ...
    } catch (error) {
        console.error(error);
        showError(errorBanner, error.message);
    }

    bookingFilter.addEventListener('change', () => {
        renderUpcoming(upcomingBody, state.upcomingBookings, Number(bookingFilter.value));
    });
});

function initCharts(data) {
    // Pie Chart - Status
    const statusCtx = document.getElementById('statusChart').getContext('2d');
    const statusLabels = Object.keys(data.bookingByStatus || {});
    const statusValues = Object.values(data.bookingByStatus || {});

    // Translation map for statuses
    const translations = {
        'PENDING': 'Chờ duyệt',
        'CONFIRMED': 'Đã xác nhận',
        'COMPLETED': 'Hoàn thành',
        'CANCELLED': 'Đã hủy'
    };

    new Chart(statusCtx, {
        type: 'pie',
        data: {
            labels: statusLabels.map(l => translations[l] || l),
            datasets: [{
                data: statusValues,
                backgroundColor: ['#f2d936', '#38b2ac', '#4299e1', '#e53e3e'],
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false
        }
    });

    // Line Chart - Monthly Trends
    const monthlyCtx = document.getElementById('monthlyChart').getContext('2d');
    const months = [];
    const counts = [];
    const monthlyData = data.monthlyBookings || {};

    for (let i = 1; i <= 12; i++) {
        months.push('T' + i);
        counts.push(monthlyData[i] || 0);
    }

    new Chart(monthlyCtx, {
        type: 'line',
        data: {
            labels: months,
            datasets: [{
                label: 'Số lượng đặt tiệc',
                data: counts,
                borderColor: '#103238',
                backgroundColor: 'rgba(16, 50, 56, 0.1)',
                tension: 0.4,
                fill: true
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        stepSize: 1
                    }
                }
            }
        }
    });
}

function formatCurrency(value) {
    if (!value && value !== 0) {
        return '--';
    }
    try {
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND',
            maximumFractionDigits: 0
        }).format(value);
    } catch (e) {
        return value;
    }
}

function formatDateTime(value) {
    if (!value) {
        return '';
    }
    try {
        return new Intl.DateTimeFormat('vi-VN', {
            dateStyle: 'medium',
            timeStyle: 'short'
        }).format(new Date(value));
    } catch (e) {
        return value;
    }
}

function showError(element, message) {
    element.textContent = message;
    element.style.display = 'block';
}

function renderUpcoming(tbody, bookings, rangeDays) {
    tbody.innerHTML = '';
    const cutoff = addDays(new Date(), rangeDays);
    bookings
        .filter(booking => booking.eventDate && new Date(booking.eventDate) <= cutoff)
        .forEach(booking => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${booking.brideName} &amp; ${booking.groomName}</td>
                <td>${booking.eventDate || ''}</td>
                <td>${booking.timeSlot || ''}</td>
                <td>${booking.hallName || '-'}</td>
                <td>${booking.guestCount || '-'}</td>
            `;
            tbody.appendChild(row);
        });

    if (!tbody.children.length) {
        const row = document.createElement('tr');
        row.innerHTML = `<td colspan="5">Không có booking trong khoảng ${rangeDays} ngày.</td>`;
        tbody.appendChild(row);
    }
}

function addDays(date, days) {
    const cloned = new Date(date.getTime());
    cloned.setDate(cloned.getDate() + days);
    cloned.setHours(23, 59, 59, 999);
    return cloned;
}
