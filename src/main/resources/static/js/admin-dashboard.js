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

        renderStatusBars(bookingStatusBars, data.bookingByStatus || {});

        state.upcomingBookings = data.upcomingBookings || [];
        renderUpcoming(upcomingBody, state.upcomingBookings, Number(bookingFilter.value));

        contactBody.innerHTML = '';
        (data.recentContacts || []).forEach(contact => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${contact.fullName}</td>
                <td>${contact.email}</td>
                <td>${contact.subject || '-'}</td>
                <td>${contact.flag || '-'}</td>
                <td>${formatDateTime(contact.createdAt)}</td>
            `;
            contactBody.appendChild(row);
        });
    } catch (error) {
        console.error(error);
        showError(errorBanner, error.message);
    }

    bookingFilter.addEventListener('change', () => {
        renderUpcoming(upcomingBody, state.upcomingBookings, Number(bookingFilter.value));
    });
});

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

function renderStatusBars(container, statusMap) {
    container.innerHTML = '';
    const entries = Object.entries(statusMap);
    const total = entries.reduce((acc, [, count]) => acc + count, 0) || 1;

    entries.forEach(([status, count]) => {
        const percentage = Math.round((count / total) * 100);
        const card = document.createElement('div');
        card.className = 'card';
        card.innerHTML = `
            <h3>${status}</h3>
            <p>${count} booking(s)</p>
            <div style="background:#e6ecf2;border-radius:12px;height:12px;overflow:hidden;">
                <div style="background:#f2d936;width:${percentage}%;height:100%;"></div>
            </div>
            <small>${percentage}% tổng số booking</small>
        `;
        container.appendChild(card);
    });
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
