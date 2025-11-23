INSERT INTO halls (code, name, capacity, base_price, description, amenities, image_url, display_order, is_active)
VALUES ('CRYSTAL', 'Crystal Hall', 450, 135000000,
        'Sảnh kính cao cấp, phù hợp gala & cưới tối.',
        JSON_ARRAY('LED Wall', 'Lighting Package', 'VIP Lounge'),
        'https://example.com/images/halls/crystal.jpg', 3, 1),
       ('RUBY', 'Ruby Terrace', 250, 89000000,
        'Không gian sân vườn mở, phong cách rustic.',
        JSON_ARRAY('Outdoor Stage', 'Canopy', 'Live Band Area'),
        'https://example.com/images/halls/ruby.jpg', 4, 1)
ON DUPLICATE KEY UPDATE name = VALUES(name);

INSERT INTO menus (name, price, description, category, image_url, is_featured)
VALUES ('Garden Vegan Delight', 2800000, 'Thực đơn chay cao cấp 8 món', 'VEGAN',
        'https://example.com/images/menus/vegan.jpg', 0),
       ('Premium Fusion', 3800000, 'Kết hợp món Âu - Á hiện đại', 'FUSION',
        'https://example.com/images/menus/fusion.jpg', 1)
ON DUPLICATE KEY UPDATE price = VALUES(price);

INSERT INTO promotions (title, slug, description, start_date, end_date, terms)
VALUES ('Luxury Combo 2025', 'luxury-combo-2025',
        'Ưu đãi combo sảnh Crystal + menu Fusion giảm 10%',
        '2025-04-01', '2025-06-30', 'Áp dụng hoá đơn từ 250 triệu trở lên.'),
       ('Weekday Special', 'weekday-special',
        'Giảm 5% cho tiệc cưới tổ chức từ Thứ 2 đến Thứ 5',
        '2025-01-15', '2025-12-20', 'Không áp dụng đồng thời chương trình khác.')
ON DUPLICATE KEY UPDATE description = VALUES(description);

INSERT INTO media_albums (title, description)
VALUES ('Elegant Weddings', 'Khoảnh khắc cưới sang trọng tại Crystal Hall'),
       ('Garden Moments', 'Ảnh ngoài trời tại Ruby Terrace')
ON DUPLICATE KEY UPDATE description = VALUES(description);

INSERT INTO media_items (title, type, url, thumbnail_url, album_id, display_order)
SELECT 'Crystal Hall Decor', 'IMAGE', 'https://example.com/images/albums/crystal-decor.jpg',
       'https://example.com/images/albums/crystal-thumb.jpg', ma.id, 1
FROM media_albums ma
WHERE ma.title = 'Elegant Weddings'
ON DUPLICATE KEY UPDATE url = VALUES(url);

INSERT INTO media_items (title, type, url, thumbnail_url, album_id, display_order)
SELECT 'Garden Sunset', 'IMAGE', 'https://example.com/images/albums/garden-sunset.jpg',
       'https://example.com/images/albums/garden-thumb.jpg', ma.id, 1
FROM media_albums ma
WHERE ma.title = 'Garden Moments'
ON DUPLICATE KEY UPDATE url = VALUES(url);
