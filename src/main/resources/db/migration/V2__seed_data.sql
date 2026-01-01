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
        '/images/gallery/1.jpg', 1),
       ('GARDEN', 'Garden View', 300, 95000000, 'Sảnh ngoài trời phong cách nhiệt đới',
        JSON_ARRAY('Outdoor Stage', 'Live Music', 'Decor Lighting'),
        '/images/gallery/2.jpg', 2)
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Seed Menus (Catalog)
INSERT INTO menus (name, price, description, category, image_url, is_featured, type)
VALUES
-- Menu Items
('Luxury Oriental', 3500000, 'Thực đơn Á cao cấp 10 món', 'ORIENTAL', '/images/menu/1.jpg', 1, 'MENU'),
('Western Delight', 3200000, 'Thực đơn Âu hiện đại', 'WESTERN', '/images/menu/2.jpg', 0, 'MENU'),
('Gà Quay Mật Ong', 350000, 'Gà thả vườn quay mật ong rừng, da giòn thịt ngọt.', 'Món chính', '/images/menu/3.jpg', 0, 'MENU'),
('Súp Vi Cá', 150000, 'Súp vi cá thượng hạng, bổ dưỡng và sang trọng.', 'Khai vị', '/images/menu/4.jpg', 0, 'MENU'),
('Bò Sốt Tiêu Đen', 450000, 'Thăn bò nhập khẩu sốt tiêu đen đậm đà.', 'Món chính', '/images/menu/5.jpg', 0, 'MENU'),
('Salad Nga', 80000, 'Salad rau củ tươi mát, sốt mayonnaise béo ngậy.', 'Khai vị', '/images/menu/6.jpg', 0, 'MENU'),
('Chè Hạt Sen', 50000, 'Chè hạt sen long nhãn thanh mát.', 'Tráng miệng', '/images/menu/7.jpg', 0, 'MENU'),
('Tôm Hùm Bỏ Lò', 1200000, 'Tôm hùm phô mai bỏ lò thơm lừng.', 'Món chính', '/images/menu/8.jpg', 0, 'MENU'),

-- Decor Items
('Bàn Ghế Tiffany', 0, 'Bộ bàn ghế Tiffany trắng sang trọng, nơ vàng gold.', 'Bàn ghế', '/images/banner/slider2.jpg', 0, 'DECOR'),
('Cổng Hoa Lụa', 2000000, 'Cổng hoa lụa cao cấp 3m, tone màu Pastel.', 'Cổng hoa', '/images/banner/slider3.jpg', 0, 'DECOR'),
('Sân Khấu 3D', 5000000, 'Backdrop sân khấu 3D với hệ thống ánh sáng hiện đại.', 'Sân khấu', '/images/banner/san-khau-dam-cuoi-2.jpg', 0, 'DECOR'),
('Bàn Gallery', 1500000, 'Bàn Gallery đón khách phong cách Vintage.', 'Trang trí', '/images/banner/san-khau-dep-cho-tiec-cuoi-1.jpg', 0, 'DECOR'),

-- Gallery Items
('Gallery Image 1', 0, 'Không gian tiệc cưới sang trọng', 'Luxury', '/images/gallery/1.jpg', 1, 'GALLERY'),
('Gallery Image 2', 0, 'Sảnh tiệc cưới lộng lẫy', 'Luxury', '/images/gallery/2.jpg', 0, 'GALLERY'),
('Gallery Image 3', 0, 'Trang trí tiệc cưới hiện đại', 'Modern', '/images/gallery/3.jpg', 0, 'GALLERY'),
('Gallery Image 4', 0, 'Phong cách cổ điển', 'Classic', '/images/gallery/4.jpg', 0, 'GALLERY'),
('Gallery Image 5', 0, 'Không gian tiệc ngoài trời', 'Rustic', '/images/gallery/5.jpg', 0, 'GALLERY'),
('Gallery Image 6', 0, 'Sân khấu tiệc cưới đẹp', 'Luxury', '/images/gallery/6.jpg', 0, 'GALLERY'),
('Gallery Image 7', 0, 'Bàn tiệc sang trọng', 'Classic', '/images/gallery/7.jpg', 0, 'GALLERY'),
('Gallery Image 8', 0, 'Ánh sáng lãng mạn', 'Modern', '/images/gallery/8.jpg', 0, 'GALLERY'),
('Gallery Image 9', 0, 'Hoa trang trí tinh tế', 'Rustic', '/images/gallery/9.jpg', 0, 'GALLERY'),
('Gallery Image 10', 0, 'Tổng thể không gian', 'Luxury', '/images/gallery/10.jpg', 0, 'GALLERY'),

-- Service Items
('Tiệc Cưới Trọn Gói', 0, 'Dịch vụ tổ chức tiệc cưới chuyên nghiệp từ A-Z: trang trí sảnh tiệc, kịch bản chương trình, MC, ban nhạc, khánh tiết.', 'Wedding', '/images/service/slider1.jpg', 1, 'SERVICE'),
('Sự Kiện & Hội Nghị', 0, 'Không gian hội trường đa năng với hệ thống âm thanh, ánh sáng, màn hình LED hiện đại, phù hợp mọi loại hình sự kiện.', 'Event', '/images/service/slider2.jpg', 0, 'SERVICE'),
('Outside Catering', 0, 'Mang hương vị ẩm thực 5 sao đến bất kỳ đâu bạn muốn. Phục vụ chuyên nghiệp, tận tâm tại tư gia hoặc địa điểm ngoài trời.', 'Catering', '/images/service/slider3.jpg', 0, 'SERVICE'),
('Trang Trí Sân Khấu', 0, 'Thiết kế và trang trí sân khấu độc đáo, ấn tượng cho ngày trọng đại của bạn.', 'Decoration', '/images/service/san-khau-dam-cuoi-1.jpg', 0, 'SERVICE'),
('Backdrop 3D Hiện Đại', 0, 'Backdrop sân khấu 3D với hệ thống ánh sáng lung linh, tạo điểm nhấn cho tiệc cưới.', 'Decoration', '/images/service/san-khau-dam-cuoi-2.jpg', 0, 'SERVICE'),
('Concept Tiệc Cưới', 0, 'Tư vấn và thiết kế concept tiệc cưới độc đáo, phù hợp với phong cách riêng của bạn.', 'Planning', '/images/service/san-khau-dep-cho-tiec-cuoi-1.jpg', 0, 'SERVICE'),
('Trang Trí Hoa Tươi', 0, 'Trang trí hoa tươi cao cấp, tinh tế, tạo không gian lãng mạn cho ngày trọng đại.', 'Decoration', '/images/service/trang-tri-san-khau-dap-cuoi-dep-tphcm-2.jpg', 0, 'SERVICE')

ON DUPLICATE KEY UPDATE price = VALUES(price);

-- Seed Promotions
INSERT INTO promotions (title, slug, description, start_date, end_date, terms)
VALUES ('Early Bird 2025', 'early-bird-2025', 'Ưu đãi giảm 15% cho hợp đồng ký trước 31/03/2025',
        '2025-01-01', '2025-03-31', 'Áp dụng cho hợp đồng có giá trị từ 200 triệu'),
       ('Summer Love', 'summer-love', 'Tặng gói trang trí cao cấp cho tiệc cưới tháng 6-8',
        '2025-06-01', '2025-08-31', 'Không áp dụng đồng thời chương trình khác')
ON DUPLICATE KEY UPDATE description = VALUES(description);

-- Seed Media Albums
INSERT INTO media_albums (title, description)
VALUES ('Wedding Gallery', 'Bộ sưu tập ảnh tiệc cưới đẹp'),
       ('Wedding Collection 1', 'Bộ sưu tập concept 1'),
       ('Wedding Collection 2', 'Bộ sưu tập concept 2'),
       ('Wedding Collection 3', 'Bộ sưu tập concept 3')
ON DUPLICATE KEY UPDATE title = VALUES(title);

-- Seed Media Items
INSERT INTO media_items (title, type, url, thumbnail_url, album_id, display_order)
VALUES
-- Album 1: Wedding Gallery (10 images)
('Gallery Image 1', 'IMAGE', '/images/gallery/1.jpg', '/images/gallery/1.jpg', 1, 1),
('Gallery Image 2', 'IMAGE', '/images/gallery/2.jpg', '/images/gallery/2.jpg', 1, 2),
('Gallery Image 3', 'IMAGE', '/images/gallery/3.jpg', '/images/gallery/3.jpg', 1, 3),
('Gallery Image 4', 'IMAGE', '/images/gallery/4.jpg', '/images/gallery/4.jpg', 1, 4),
('Gallery Image 5', 'IMAGE', '/images/gallery/5.jpg', '/images/gallery/5.jpg', 1, 5),
('Gallery Image 6', 'IMAGE', '/images/gallery/6.jpg', '/images/gallery/6.jpg', 1, 6),
('Gallery Image 7', 'IMAGE', '/images/gallery/7.jpg', '/images/gallery/7.jpg', 1, 7),
('Gallery Image 8', 'IMAGE', '/images/gallery/8.jpg', '/images/gallery/8.jpg', 1, 8),
('Gallery Image 9', 'IMAGE', '/images/gallery/9.jpg', '/images/gallery/9.jpg', 1, 9),
('Gallery Image 10', 'IMAGE', '/images/gallery/10.jpg', '/images/gallery/10.jpg', 1, 10),

-- Album 2: Wedding Collection 1 (12 images)
('Collection 1 - Image 1', 'IMAGE', '/images/collection/1/1.jpg', '/images/collection/1/1.jpg', 2, 1),
('Collection 1 - Image 2', 'IMAGE', '/images/collection/1/2.jpg', '/images/collection/1/2.jpg', 2, 2),
('Collection 1 - Image 3', 'IMAGE', '/images/collection/1/3.jpg', '/images/collection/1/3.jpg', 2, 3),
('Collection 1 - Image 4', 'IMAGE', '/images/collection/1/4.jpg', '/images/collection/1/4.jpg', 2, 4),
('Collection 1 - Image 5', 'IMAGE', '/images/collection/1/5.jpg', '/images/collection/1/5.jpg', 2, 5),
('Collection 1 - Image 6', 'IMAGE', '/images/collection/1/6.jpg', '/images/collection/1/6.jpg', 2, 6),
('Collection 1 - Image 7', 'IMAGE', '/images/collection/1/7.jpg', '/images/collection/1/7.jpg', 2, 7),
('Collection 1 - Image 8', 'IMAGE', '/images/collection/1/8.jpg', '/images/collection/1/8.jpg', 2, 8),
('Collection 1 - Image 9', 'IMAGE', '/images/collection/1/9.jpg', '/images/collection/1/9.jpg', 2, 9),
('Collection 1 - Image 10', 'IMAGE', '/images/collection/1/10.jpg', '/images/collection/1/10.jpg', 2, 10),
('Collection 1 - Image 11', 'IMAGE', '/images/collection/1/11.jpg', '/images/collection/1/11.jpg', 2, 11),
('Collection 1 - Image 12', 'IMAGE', '/images/collection/1/12.jpg', '/images/collection/1/12.jpg', 2, 12),

-- Album 3: Wedding Collection 2 (13 images)
('Collection 2 - Image 1', 'IMAGE', '/images/collection/2/1.jpg', '/images/collection/2/1.jpg', 3, 1),
('Collection 2 - Image 2', 'IMAGE', '/images/collection/2/2.jpg', '/images/collection/2/2.jpg', 3, 2),
('Collection 2 - Image 3', 'IMAGE', '/images/collection/2/3.jpg', '/images/collection/2/3.jpg', 3, 3),
('Collection 2 - Image 4', 'IMAGE', '/images/collection/2/4.jpg', '/images/collection/2/4.jpg', 3, 4),
('Collection 2 - Image 5', 'IMAGE', '/images/collection/2/5.jpg', '/images/collection/2/5.jpg', 3, 5),
('Collection 2 - Image 6', 'IMAGE', '/images/collection/2/6.jpg', '/images/collection/2/6.jpg', 3, 6),
('Collection 2 - Image 7', 'IMAGE', '/images/collection/2/7.jpg', '/images/collection/2/7.jpg', 3, 7),
('Collection 2 - Image 8', 'IMAGE', '/images/collection/2/8.jpg', '/images/collection/2/8.jpg', 3, 8),
('Collection 2 - Image 9', 'IMAGE', '/images/collection/2/9.jpg', '/images/collection/2/9.jpg', 3, 9),
('Collection 2 - Image 10', 'IMAGE', '/images/collection/2/10.jpg', '/images/collection/2/10.jpg', 3, 10),
('Collection 2 - Image 11', 'IMAGE', '/images/collection/2/11.jpg', '/images/collection/2/11.jpg', 3, 11),
('Collection 2 - Image 12', 'IMAGE', '/images/collection/2/12.jpg', '/images/collection/2/12.jpg', 3, 12),
('Collection 2 - Image 13', 'IMAGE', '/images/collection/2/13.jpg', '/images/collection/2/13.jpg', 3, 13),

-- Album 4: Wedding Collection 3 (9 images)
('Collection 3 - Image 1', 'IMAGE', '/images/collection/3/1.jpg', '/images/collection/3/1.jpg', 4, 1),
('Collection 3 - Image 2', 'IMAGE', '/images/collection/3/2.jpg', '/images/collection/3/2.jpg', 4, 2),
('Collection 3 - Image 3', 'IMAGE', '/images/collection/3/3.jpg', '/images/collection/3/3.jpg', 4, 3),
('Collection 3 - Image 4', 'IMAGE', '/images/collection/3/4.jpg', '/images/collection/3/4.jpg', 4, 4),
('Collection 3 - Image 5', 'IMAGE', '/images/collection/3/5.jpg', '/images/collection/3/5.jpg', 4, 5),
('Collection 3 - Image 6', 'IMAGE', '/images/collection/3/6.jpg', '/images/collection/3/6.jpg', 4, 6),
('Collection 3 - Image 7', 'IMAGE', '/images/collection/3/7.jpg', '/images/collection/3/7.jpg', 4, 7),
('Collection 3 - Image 8', 'IMAGE', '/images/collection/3/8.jpg', '/images/collection/3/8.jpg', 4, 8),
('Collection 3 - Image 9', 'IMAGE', '/images/collection/3/9.jpg', '/images/collection/3/9.jpg', 4, 9)

ON DUPLICATE KEY UPDATE url = VALUES(url);
