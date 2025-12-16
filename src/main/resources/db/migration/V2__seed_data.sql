-- Seed Users (Admin)
INSERT INTO users (username, email, password_hash, status, primary_role)
VALUES ('admin', 'admin@wedding.local', '$2a$10$FXqALa7TwCxP5DI/9J2pje927Qg1ak6gK22nOi8E..ZIdXKq2fngi', 'ACTIVE', 'ADMIN'),
       ('user', 'user@wedding.local', '$2a$10$FXqALa7TwCxP5DI/9J2pje927Qg1ak6gK22nOi8E..ZIdXKq2fngi', 'ACTIVE', 'USER')
ON DUPLICATE KEY UPDATE status = VALUES(status);

-- Seed Employees (Admin Profile & User Profile)
INSERT INTO employees (user_id, full_name, email)
SELECT id, 'System Administrator', 'admin@wedding.local'
FROM users WHERE username = 'admin';

INSERT INTO employees (user_id, full_name, email)
SELECT id, 'Demo User', 'user@wedding.local'
FROM users WHERE username = 'user';

-- Seed Halls
INSERT INTO halls (code, name, capacity, base_price, description, amenities, image_url, display_order)
VALUES ('GRAND', 'Grand Ballroom', 500, 150000000, 'Sảnh lớn sang trọng phù hợp tiệc cưới cao cấp',
        JSON_ARRAY('LED Wall', 'Premium Sound', 'Private Lounge'),
        'https://example.com/images/halls/grand.jpg', 1),
       ('GARDEN', 'Garden View', 300, 95000000, 'Sảnh ngoài trời phong cách nhiệt đới',
        JSON_ARRAY('Outdoor Stage', 'Live Music', 'Decor Lighting'),
        'https://example.com/images/halls/garden.jpg', 2)
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Seed Menus (Catalog)
INSERT INTO menus (name, price, description, category, image_url, is_featured, type)
VALUES 
-- Menu Items
('Luxury Oriental', 3500000, 'Thực đơn Á cao cấp 10 món', 'ORIENTAL', 'https://example.com/images/menus/oriental.jpg', 1, 'MENU'),
('Western Delight', 3200000, 'Thực đơn Âu hiện đại', 'WESTERN', 'https://example.com/images/menus/western.jpg', 0, 'MENU'),
('Gà Quay Mật Ong', 350000, 'Gà thả vườn quay mật ong rừng, da giòn thịt ngọt.', 'Món chính', 'https://tranganpalace.vn/wp-content/uploads/2021/04/thuc-don-tiec-cuoi-buffet-1.jpg', 0, 'MENU'),
('Súp Vi Cá', 150000, 'Súp vi cá thượng hạng, bổ dưỡng và sang trọng.', 'Khai vị', 'https://tranganpalace.vn/wp-content/uploads/2021/04/thuc-don-tiec-cuoi-buffet-2.jpg', 0, 'MENU'),
('Bò Sốt Tiêu Đen', 450000, 'Thăn bò nhập khẩu sốt tiêu đen đậm đà.', 'Món chính', 'https://tranganpalace.vn/wp-content/uploads/2021/04/thuc-don-tiec-cuoi-buffet-3.jpg', 0, 'MENU'),
('Salad Nga', 80000, 'Salad rau củ tươi mát, sốt mayonnaise béo ngậy.', 'Khai vị', 'https://tranganpalace.vn/wp-content/uploads/2021/04/thuc-don-tiec-cuoi-buffet-4.jpg', 0, 'MENU'),
('Chè Hạt Sen', 50000, 'Chè hạt sen long nhãn thanh mát.', 'Tráng miệng', 'https://tranganpalace.vn/wp-content/uploads/2021/04/thuc-don-tiec-cuoi-buffet-5.jpg', 0, 'MENU'),
('Tôm Hùm Bỏ Lò', 1200000, 'Tôm hùm phô mai bỏ lò thơm lừng.', 'Món chính', 'https://tranganpalace.vn/wp-content/uploads/2021/04/thuc-don-tiec-cuoi-buffet-6.jpg', 0, 'MENU'),

-- Decor Items
('Bàn Ghế Tiffany', 0, 'Bộ bàn ghế Tiffany trắng sang trọng, nơ vàng gold.', 'Bàn ghế', 'https://tranganpalace.vn/wp-content/uploads/2019/12/trang-tri-tiec-cuoi-tai-nha-2.jpg.webp', 0, 'DECOR'),
('Cổng Hoa Lụa', 2000000, 'Cổng hoa lụa cao cấp 3m, tone màu Pastel.', 'Cổng hoa', 'https://tranganpalace.vn/wp-content/uploads/2019/12/trang-tri-tiec-cuoi-tai-nha.jpg.webp', 0, 'DECOR'),
('Sân Khấu 3D', 5000000, 'Backdrop sân khấu 3D với hệ thống ánh sáng hiện đại.', 'Sân khấu', 'https://tranganpalace.vn/wp-content/uploads/2019/12/trang-tri-tiec-cuoi-tai-nha-1.jpg.webp', 0, 'DECOR'),
('Bàn Gallery', 1500000, 'Bàn Gallery đón khách phong cách Vintage.', 'Trang trí', 'https://tranganpalace.vn/wp-content/uploads/2019/12/trang-tri-tiec-cuoi-tai-nha-4.jpg.webp', 0, 'DECOR')

ON DUPLICATE KEY UPDATE price = VALUES(price);

-- Seed Promotions
INSERT INTO promotions (title, slug, description, start_date, end_date, terms)
VALUES ('Early Bird 2025', 'early-bird-2025', 'Ưu đãi giảm 15% cho hợp đồng ký trước 31/03/2025',
        '2025-01-01', '2025-03-31', 'Áp dụng cho hợp đồng có giá trị từ 200 triệu'),
       ('Summer Love', 'summer-love', 'Tặng gói trang trí cao cấp cho tiệc cưới tháng 6-8',
        '2025-06-01', '2025-08-31', 'Không áp dụng đồng thời chương trình khác')
ON DUPLICATE KEY UPDATE description = VALUES(description);
