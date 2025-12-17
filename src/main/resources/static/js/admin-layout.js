document.addEventListener('DOMContentLoaded', () => {
    const toggleButton = document.getElementById('sidebar-toggle');
    if (!toggleButton) {
        attachUserMenuHandlers();
        attachProfileModalHandlers();
        return;
    }

    const body = document.body;
    const storageKey = 'wedding-admin-sidebar';
    const savedState = window.localStorage.getItem(storageKey);

    if (savedState === 'collapsed') {
        body.classList.add('sidebar-collapsed');
        toggleButton.setAttribute('aria-expanded', 'false');
    }

    toggleButton.addEventListener('click', () => {
        const collapsed = body.classList.toggle('sidebar-collapsed');
        toggleButton.setAttribute('aria-expanded', collapsed ? 'false' : 'true');
        window.localStorage.setItem(storageKey, collapsed ? 'collapsed' : 'expanded');
    });

    attachUserMenuHandlers();
    attachProfileModalHandlers();
});

function attachUserMenuHandlers() {
    const userToggle = document.getElementById('user-menu-toggle');
    if (!userToggle) {
        return;
    }

    const userMenu = userToggle.closest('.user-menu');
    if (!userMenu) {
        return;
    }

    const dropdown = userMenu.querySelector('.user-menu-dropdown');

    const closeMenu = () => {
        userMenu.classList.remove('open');
        userToggle.setAttribute('aria-expanded', 'false');
    };

    userToggle.addEventListener('click', (event) => {
        event.stopPropagation();
        const open = userMenu.classList.toggle('open');
        userToggle.setAttribute('aria-expanded', open ? 'true' : 'false');
    });

    document.addEventListener('click', (event) => {
        if (!userMenu.contains(event.target)) {
            closeMenu();
        }
    });

    // close if focus leaves dropdown via keyboard
    if (dropdown) {
        dropdown.addEventListener('keydown', (event) => {
            if (event.key === 'Escape') {
                closeMenu();
                userToggle.focus();
            }
        });
    }
}

function attachProfileModalHandlers() {
    const modal = document.getElementById('profile-modal');
    const triggers = document.querySelectorAll('[data-profile-trigger]');
    if (!modal || !triggers.length) {
        return;
    }

    const loadingState = modal.querySelector('[data-profile-loading]');
    const errorState = modal.querySelector('[data-profile-error]');
    const contentState = modal.querySelector('[data-profile-content]');
    const fieldNodes = {};
    modal.querySelectorAll('[data-profile-field]').forEach((node) => {
        if (node.dataset.profileField) {
            fieldNodes[node.dataset.profileField] = node;
        }
    });
    const rolesContainer = modal.querySelector('[data-profile-list="roles"]');
    const permissionsContainer = modal.querySelector('[data-profile-list="permissions"]');
    const closeButtons = modal.querySelectorAll('[data-profile-close]');
    let lastFocusedElement = null;

    const openModal = () => {
        modal.classList.add('is-open');
        modal.setAttribute('aria-hidden', 'false');
        document.body.style.overflow = 'hidden';
        window.requestAnimationFrame(() => {
            const focusTarget = modal.querySelector('.profile-modal__close');
            if (focusTarget) {
                focusTarget.focus();
            }
        });
    };

    const closeModal = () => {
        modal.classList.remove('is-open');
        modal.setAttribute('aria-hidden', 'true');
        document.body.style.overflow = '';
        if (lastFocusedElement && typeof lastFocusedElement.focus === 'function') {
            lastFocusedElement.focus();
        }
    };

    const resetStates = () => {
        setProfileHidden(loadingState, false);
        if (errorState) {
            errorState.textContent = '';
        }
        setProfileHidden(errorState, true);
        setProfileHidden(contentState, true);
    };

    const showError = (message) => {
        setProfileHidden(loadingState, true);
        if (errorState) {
            errorState.textContent = message || 'Không thể tải thông tin người dùng.';
        }
        setProfileHidden(errorState, false);
        setProfileHidden(contentState, true);
    };

    const renderChipCollection = (container, values, extraClass) => {
        if (!container) {
            return;
        }
        container.innerHTML = '';
        if (!values || !values.length) {
            const placeholder = document.createElement('span');
            placeholder.className = 'profile-modal__chip profile-modal__chip--muted';
            if (extraClass) {
                placeholder.classList.add(extraClass);
            }
            placeholder.textContent = 'Chưa thiết lập';
            container.appendChild(placeholder);
            return;
        }
        values.forEach((value) => {
            const chip = document.createElement('span');
            chip.className = 'profile-modal__chip';
            if (extraClass) {
                chip.classList.add(extraClass);
            }
            chip.textContent = value;
            container.appendChild(chip);
        });
    };

    const renderProfile = (profile) => {
        setProfileHidden(loadingState, true);
        setProfileHidden(errorState, true);
        setProfileHidden(contentState, false);

        const mappedValues = {
            fullName: profile.fullName,
            username: profile.username,
            email: profile.email,
            primaryRole: profile.primaryRole,
            status: profile.status,
            createdAt: formatProfileDateTime(profile.createdAt),
            updatedAt: formatProfileDateTime(profile.updatedAt)
        };

        Object.entries(mappedValues).forEach(([field, value]) => {
            if (fieldNodes[field]) {
                const node = fieldNodes[field];
                node.textContent = value || '--';
                if (field === 'status') {
                    if (value) {
                        node.dataset.state = value.toString().toLowerCase();
                    } else {
                        delete node.dataset.state;
                    }
                }
            }
        });

        renderChipCollection(rolesContainer, profile.roles, 'profile-modal__chip--role');
        const normalizedPermissions = Array.isArray(profile.permissions)
            ? profile.permissions
            : profile.permissions
                ? Array.from(profile.permissions)
                : [];
        renderChipCollection(permissionsContainer, normalizedPermissions, 'profile-modal__chip--permission');
    };

    const fetchProfile = async () => {
        resetStates();
        openModal();
        try {
            const response = await fetch('/api/v1/admin/profile/me');

            if (!response.ok) {
                if (response.status === 401 || response.status === 403) {
                    throw new Error('Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.');
                }
                throw new Error('Không thể tải thông tin người dùng.');
            }

            const payload = await response.json();
            if (!payload.success) {
                throw new Error(payload.message || 'Không thể tải thông tin người dùng.');
            }

            renderProfile(payload.data || {});
        } catch (error) {
            console.error(error);
            showError(error.message);
        }
    };

    triggers.forEach((trigger) => {
        trigger.addEventListener('click', (event) => {
            event.preventDefault();
            const userMenu = trigger.closest('.user-menu');
            if (userMenu) {
                userMenu.classList.remove('open');
                const toggle = userMenu.querySelector('#user-menu-toggle');
                if (toggle) {
                    toggle.setAttribute('aria-expanded', 'false');
                }
            }
            lastFocusedElement = document.activeElement;
            fetchProfile();
        });
    });

    closeButtons.forEach((button) => {
        button.addEventListener('click', () => {
            closeModal();
        });
    });

    modal.addEventListener('click', (event) => {
        if (event.target === modal) {
            closeModal();
        }
    });

    document.addEventListener('keydown', (event) => {
        if (event.key === 'Escape' && modal.classList.contains('is-open')) {
            closeModal();
        }
    });
}

function formatProfileDateTime(value) {
    if (!value) {
        return '--';
    }
    try {
        const date = new Date(value);
        if (Number.isNaN(date.getTime())) {
            return value;
        }
        return new Intl.DateTimeFormat('vi-VN', {
            dateStyle: 'medium',
            timeStyle: 'short'
        }).format(date);
    } catch (error) {
        return value;
    }
}

function setProfileHidden(element, shouldHide) {
    if (!element) {
        return;
    }
    if (shouldHide) {
        element.setAttribute('hidden', 'true');
        element.style.display = 'none';
    } else {
        element.removeAttribute('hidden');
        element.style.display = '';
    }
}

// =======================
// TOAST NOTIFICATIONS
// =======================
function showToast(type, message) {
    let container = document.querySelector('.toast-container');
    if (!container) {
        container = document.createElement('div');
        container.className = 'toast-container';
        document.body.appendChild(container);
    }

    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.innerHTML = `
        <div class="toast-content">
            ${type === 'success' ?
            '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>' :
            '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="15" y1="9" x2="9" y2="15"></line><line x1="9" y1="9" x2="15" y2="15"></line></svg>'}
            <span>${message}</span>
        </div>
        <button class="toast-close" onclick="this.parentElement.remove()">&times;</button>
    `;


    container.appendChild(toast);
    requestAnimationFrame(() => toast.classList.add('show'));

    setTimeout(() => {
        toast.classList.remove('show');
        toast.classList.add('hide');
        toast.addEventListener('animationend', () => toast.remove());
    }, 3000);
}

// Image Upload Helper
function uploadImage(fileInput, targetInputId, type) {
    const file = fileInput.files[0];
    if (!file) return;

    // Show loading state if needed
    const targetInput = document.getElementById(targetInputId);
    const originalPlaceholder = targetInput.placeholder;
    targetInput.value = "Đang tải lên...";
    targetInput.disabled = true;

    const formData = new FormData();
    formData.append("file", file);
    formData.append("type", type);

    // Get CSRF token if enabled (Spring Security usually requires it for POST)
    // Assuming cookie or meta tag. For simplicity/freelance, trying standard fetch.
    // Ideally we should grab X-CSRF-TOKEN from meta.

    fetch('/admin/upload/image', {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.url) {
                targetInput.value = data.url;
                // Trigger change for preview
                if (typeof previewImage === "function") previewImage();
                showToast('success', 'Tải ảnh thành công!');
            } else {
                targetInput.value = '';
                showToast('error', 'Lỗi tải ảnh: ' + (data.error || 'Unknown'));
            }
        })
        .catch(error => {
            console.error('Error:', error);
            targetInput.value = '';
            showToast('error', 'Lỗi kết nối server');
        })
        .finally(() => {
            targetInput.disabled = false;
            targetInput.placeholder = originalPlaceholder;
        });
}
// Check Session Message on Load
document.addEventListener('DOMContentLoaded', () => {
    if (sessionStorage.getItem('loginSuccess')) {
        showToast('success', 'Đăng nhập trang quản trị thành công!');
        sessionStorage.removeItem('loginSuccess');
    }
});
