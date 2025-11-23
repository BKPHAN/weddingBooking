document.addEventListener('DOMContentLoaded', () => {
    const errorBanner = document.getElementById('content-error');
    const hallForm = document.getElementById('hall-form');
    const menuForm = document.getElementById('menu-form');

    const hallTableBody = document.getElementById('hall-table-body');
    const menuTableBody = document.getElementById('menu-table-body');

    const hallEditWrapper = document.getElementById('hall-edit-wrapper');
    const hallEditForm = document.getElementById('hall-edit-form');
    const hallEditCancel = document.getElementById('hall-edit-cancel');

    const menuEditWrapper = document.getElementById('menu-edit-wrapper');
    const menuEditForm = document.getElementById('menu-edit-form');
    const menuEditCancel = document.getElementById('menu-edit-cancel');

    const albumSelect = document.getElementById('album-select');
    const mediaItemForm = document.getElementById('media-item-form');
    const mediaTableBody = document.getElementById('media-table-body');
    const mediaUploadBtn = document.getElementById('media-upload-btn');
    const mediaFileInput = document.getElementById('media-file');

    const accessToken = window.localStorage.getItem('accessToken');

    if (!accessToken) {
        showError(errorBanner, 'Vui lòng đăng nhập lại để truy cập trang quản trị.');
        disableInteractions();
        return;
    }

    const authHeaders = (extra = {}) => ({
        'Authorization': `Bearer ${accessToken}`,
        ...extra
    });

    loadHalls();
    loadMenus();
    loadAlbums();

    hallEditCancel.addEventListener('click', () => {
        hallEditWrapper.style.display = 'none';
        hallEditForm.reset();
    });

    hallEditForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        hideError();
        const id = document.getElementById('hall-edit-id').value;
        const payload = collectHallPayload(hallEditForm);
        await submitWithFeedback(`/api/v1/admin/halls/${id}`, payload, 'PUT');
        hallEditWrapper.style.display = 'none';
        hallEditForm.reset();
        loadHalls();
    });

    menuEditCancel.addEventListener('click', () => {
        menuEditWrapper.style.display = 'none';
        menuEditForm.reset();
    });

    menuEditForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        hideError();
        const id = document.getElementById('menu-edit-id').value;
        const payload = collectMenuPayload(menuEditForm);
        await submitWithFeedback(`/api/v1/admin/menus/${id}`, payload, 'PUT');
        menuEditWrapper.style.display = 'none';
        menuEditForm.reset();
        loadMenus();
    });

    hallForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        hideError();

        const payload = collectHallPayload(hallForm, true);

        await submitWithFeedback('/api/v1/admin/halls', payload, 'POST');
        hallForm.reset();
        loadHalls();
    });

    menuForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        hideError();

        const payload = collectMenuPayload(menuForm);
        await submitWithFeedback('/api/v1/admin/menus', payload, 'POST');
        menuForm.reset();
        document.getElementById('menu-featured').checked = false;
        loadMenus();
    });

    async function loadHalls() {
        try {
            const response = await fetch('/api/v1/admin/halls', {
                headers: authHeaders()
            });
            const result = await response.json();
            if (!response.ok || !result.success) {
                handleAuthError(response.status, result.message || 'Không thể tải danh sách sảnh');
            }

            hallTableBody.innerHTML = '';
            (result.data || []).forEach(hall => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${hall.code}</td>
                <td>${hall.name}</td>
                <td>${hall.capacity}</td>
                <td data-raw="${hall.basePrice ?? ''}">${formatCurrency(hall.basePrice)}</td>
                <td>${hall.active ? 'Active' : 'Disabled'}</td>
                <td>
                    <button data-action="edit-hall" data-id="${hall.id}">Edit</button>
                    <button data-action="delete-hall" data-id="${hall.id}" style="margin-left:8px;">Delete</button>
                </td>
            `;
            row.dataset.description = hall.description || '';
            row.dataset.amenities = hall.amenities || '';
            row.dataset.imageUrl = hall.imageUrl || '';
            row.dataset.displayOrder = hall.displayOrder ?? '';
            hallTableBody.appendChild(row);
        });
        } catch (error) {
            showError(errorBanner, error.message);
        }
    }

    async function loadMenus() {
        try {
            const response = await fetch('/api/v1/admin/menus', {
                headers: authHeaders()
            });
            const result = await response.json();
            if (!response.ok || !result.success) {
                handleAuthError(response.status, result.message || 'Không thể tải thực đơn');
            }

            menuTableBody.innerHTML = '';
            (result.data || []).forEach(menu => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${menu.name}</td>
                <td>${menu.category}</td>
                <td data-raw="${menu.price ?? ''}">${formatCurrency(menu.price)}</td>
                <td>${menu.featured ? 'Yes' : 'No'}</td>
                <td>
                    <button data-action="edit-menu" data-id="${menu.id}">Edit</button>
                    <button data-action="delete-menu" data-id="${menu.id}" style="margin-left:8px;">Delete</button>
                </td>
            `;
            row.dataset.description = menu.description || '';
            row.dataset.imageUrl = menu.imageUrl || '';
            row.dataset.featured = menu.featured ? 'true' : 'false';
            menuTableBody.appendChild(row);
        });
        } catch (error) {
            showError(errorBanner, error.message);
        }
    }

    function formatCurrency(value) {
        if (!value && value !== 0) {
            return '--';
        }
        try {
            const formatted = new Intl.NumberFormat('vi-VN', {
                style: 'currency',
                currency: 'VND',
                maximumFractionDigits: 0
            }).format(value);
            const span = document.createElement('span');
            span.textContent = formatted;
            span.dataset.raw = value;
            return span.outerHTML;
        } catch (e) {
            return value;
        }
    }

    function showError(element, message) {
        element.style.background = '#ffecec';
        element.style.color = '#a94442';
        element.style.borderColor = '#f5c6cb';
        element.textContent = message;
        element.style.display = 'block';
    }

    function showSuccess(element, message) {
        element.style.background = '#e6ffea';
        element.style.color = '#1c7c3f';
        element.style.borderColor = '#a6f5b8';
        element.textContent = message;
        element.style.display = 'block';
    }

    function hideError() {
        errorBanner.style.display = 'none';
        errorBanner.textContent = '';
    }

    hallTableBody.addEventListener('click', async (event) => {
        const target = event.target;
        const action = target.dataset.action;
        const id = target.dataset.id;
        if (!action || !id) {
            return;
        }

        if (action === 'delete-hall') {
            await deleteWithFeedback(`/api/v1/admin/halls/${id}`);
            loadHalls();
            return;
        }

        if (action === 'edit-hall') {
            const row = target.closest('tr');
            document.getElementById('hall-edit-id').value = id;
            document.getElementById('hall-edit-code').value = row.children[0].textContent.trim();
            document.getElementById('hall-edit-name').value = row.children[1].textContent.trim();
            document.getElementById('hall-edit-capacity').value = row.children[2].textContent.trim();
            document.getElementById('hall-edit-base-price').value = row.children[3].dataset.raw || '';
            document.getElementById('hall-edit-amenities').value = row.dataset.amenities || '';
            document.getElementById('hall-edit-image').value = row.dataset.imageUrl || '';
            document.getElementById('hall-edit-order').value = row.dataset.displayOrder || '';
            document.getElementById('hall-edit-active').value = row.children[4].textContent.includes('Active') ? 'true' : 'false';
            hallEditWrapper.style.display = 'block';
        }
    });

    menuTableBody.addEventListener('click', async (event) => {
        const target = event.target;
        const action = target.dataset.action;
        const id = target.dataset.id;
        if (!action || !id) {
            return;
        }

        if (action === 'delete-menu') {
            await deleteWithFeedback(`/api/v1/admin/menus/${id}`);
            loadMenus();
            return;
        }

        if (action === 'edit-menu') {
            const row = target.closest('tr');
            document.getElementById('menu-edit-id').value = id;
            document.getElementById('menu-edit-name').value = row.children[0].textContent.trim();
            document.getElementById('menu-edit-category').value = row.children[1].textContent.trim();
            document.getElementById('menu-edit-price').value = row.children[2].dataset.raw || '';
            document.getElementById('menu-edit-description').value = row.dataset.description || '';
            document.getElementById('menu-edit-image').value = row.dataset.imageUrl || '';
            document.getElementById('menu-edit-featured').value = row.dataset.featured === 'true' ? 'true' : 'false';
            menuEditWrapper.style.display = 'block';
        }
    });

    mediaUploadBtn.addEventListener('click', async () => {
        hideError();
        const file = mediaFileInput.files?.[0];
        if (!file) {
            showError(errorBanner, 'Please choose a file to upload');
            return;
        }
        try {
            const formData = new FormData();
            formData.append('file', file);
        const response = await fetch('/api/v1/admin/media/upload', {
                method: 'POST',
            headers: authHeaders(),
            body: formData
            });
            const result = await response.json();
            if (!response.ok || !result.success) {
            handleAuthError(response.status, result.message || 'Tải tệp thất bại');
            }
            document.getElementById('media-url').value = result.data.url;
            showSuccess(errorBanner, 'Upload successful. URL has been populated.');
        } catch (error) {
            showError(errorBanner, error.message);
        }
    });

    mediaItemForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        hideError();

        if (!albumSelect.value) {
            showError(errorBanner, 'Please choose an album first');
            return;
        }

        const payload = {
            title: mediaItemForm.title.value.trim(),
            type: mediaItemForm.type.value,
            url: mediaItemForm.url.value.trim(),
            thumbnailUrl: mediaItemForm.thumbnailUrl.value.trim(),
            displayOrder: mediaItemForm.displayOrder.value ? Number(mediaItemForm.displayOrder.value) : null
        };

        await submitWithFeedback(`/api/v1/admin/media/albums/${albumSelect.value}/items`, payload, 'POST');
        mediaItemForm.reset();
        loadMediaItems(albumSelect.value);
    });

    albumSelect.addEventListener('change', () => {
        hideError();
        if (albumSelect.value) {
            loadMediaItems(albumSelect.value);
        } else {
            mediaTableBody.innerHTML = '';
        }
    });

    mediaTableBody.addEventListener('click', async (event) => {
        const target = event.target;
        const action = target.dataset.action;
        const id = target.dataset.id;
        const albumId = albumSelect.value;

        if (!action || !id || !albumId) {
            return;
        }

        if (action === 'delete-media') {
            await deleteWithFeedback(`/api/v1/admin/media/albums/${albumId}/items/${id}`);
            loadMediaItems(albumId);
        }
    });

    async function loadAlbums() {
        try {
            const response = await fetch('/api/v1/admin/media/albums', {
                headers: authHeaders()
            });
            const result = await response.json();
            if (!response.ok || !result.success) {
                handleAuthError(response.status, result.message || 'Không thể tải danh sách album');
            }

            albumSelect.innerHTML = '<option value="">-- Chọn album --</option>';
            (result.data || []).forEach(album => {
                const option = document.createElement('option');
                option.value = album.id;
                option.textContent = album.title;
                albumSelect.appendChild(option);
            });
        } catch (error) {
            showError(errorBanner, error.message);
        }
    }

    async function loadMediaItems(albumId) {
        try {
            const response = await fetch(`/api/v1/admin/media/albums/${albumId}/items`, {
                headers: authHeaders()
            });
            const result = await response.json();
            if (!response.ok || !result.success) {
                handleAuthError(response.status, result.message || 'Không thể tải danh sách nội dung');
            }

            mediaTableBody.innerHTML = '';
            (result.data || []).forEach(item => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${item.title}</td>
                    <td>${item.type}</td>
                    <td><a href="${item.url}" target="_blank">Xem</a></td>
                    <td>${item.displayOrder ?? '-'}</td>
                    <td><button data-action="delete-media" data-id="${item.id}">Delete</button></td>
                `;
                mediaTableBody.appendChild(row);
            });
        } catch (error) {
            showError(errorBanner, error.message);
        }
    }

    async function submitWithFeedback(url, payload, method) {
        try {
            const response = await fetch(url, {
                method,
                headers: authHeaders({'Content-Type': 'application/json'}),
                body: JSON.stringify(payload)
            });
            const result = await response.json();
            if (!response.ok || !result.success) {
                handleAuthError(response.status, result.message || 'Thao tác thất bại');
            }
        } catch (error) {
            showError(errorBanner, error.message);
            throw error;
        }
    }

    async function deleteWithFeedback(url) {
        if (!confirm('Are you sure you want to delete?')) {
            return;
        }
        try {
            const response = await fetch(url, {
                method: 'DELETE',
                headers: authHeaders()
            });
            const result = await response.json();
            if (!response.ok || !result.success) {
                handleAuthError(response.status, result.message || 'Xóa thất bại');
            }
        } catch (error) {
            showError(errorBanner, error.message);
            throw error;
        }
    }

    function collectHallPayload(form, forceActive = false) {
        return {
            code: form.code.value.trim(),
            name: form.name.value.trim(),
            capacity: Number(form.capacity.value),
            basePrice: Number(form.basePrice.value),
            amenities: parseJsonSafe(form.amenities?.value),
            imageUrl: form.imageUrl?.value.trim() || '',
            displayOrder: form.displayOrder?.value ? Number(form.displayOrder.value) : null,
            active: forceActive ? true : (form.active ? form.active.value !== 'false' : true)
        };
    }

    function collectMenuPayload(form) {
        const featuredField = form.featured;
        const featuredValue = typeof featuredField?.checked === 'boolean'
            ? featuredField.checked
            : (featuredField?.value === 'true');
        return {
            name: form.name.value.trim(),
            price: Number(form.price.value),
            description: form.description?.value.trim() || '',
            category: form.category.value.trim(),
            imageUrl: form.imageUrl?.value.trim() || '',
            featured: featuredValue
        };
    }

    function parseJsonSafe(value) {
        if (!value) {
            return null;
        }
        try {
            const parsed = JSON.parse(value);
            return parsed;
        } catch (error) {
            return value;
        }
    }

    function handleAuthError(status, fallbackMessage) {
        if (status === 401 || status === 403) {
            throw new Error('Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.');
        }
        throw new Error(fallbackMessage);
    }

    function disableInteractions() {
        document.querySelectorAll('input, select, textarea, button').forEach(element => {
            element.disabled = true;
        });
    }
});





