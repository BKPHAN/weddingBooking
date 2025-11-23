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
        const accessToken = window.localStorage.getItem('accessToken');
        if (!accessToken) {
            showError('Vui lòng đăng nhập lại để xem thông tin.');
            return;
        }

        try {
            const response = await fetch('/api/v1/admin/profile/me', {
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            });

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
