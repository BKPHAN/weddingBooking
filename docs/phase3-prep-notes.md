# Phase 3 Preparatory Notes (Admin & Nội dung)

## 1. Dữ liệu & seed cần thiết
- **Halls**: tối thiểu 4 mẫu (capacity, base_price, amenities JSON).
- **Menus**: chia nhóm (Oriental/Western/Vegan) ≥ 2 item mỗi nhóm.
- **Promotions**: ít nhất 2 chiến dịch đang diễn ra + 1 hết hạn dùng để kiểm thử filter.
- **Media albums**: tạo album cưới, sự kiện doanh nghiệp, hậu trường (kèm 3 ảnh mẫu/album).

## 2. Endpoint dự kiến (REST `/api/v1/admin/**`)
- `GET /halls`, `POST /halls`, `PUT /halls/{id}`, `DELETE /halls/{id}`.
- `GET /menus`, `POST /menus`, `PUT /menus/{id}`, `DELETE /menus/{id}`.
- `GET /promotions`, `POST /promotions`, `PUT /promotions/{id}`, `DELETE /promotions/{id}`.
- `GET /media/albums`, `POST /media/albums`, `GET /media/items?albumId=`, `POST /media/items`.
- Dashboard: `GET /dashboard/overview` trả booking theo trạng thái, feedback mới, doanh thu dự kiến.

## 3. Phân quyền dự kiến
- `ADMIN`: toàn quyền CRUD + dashboard.
- `STAFF`: đọc dashboard, cập nhật contact, promotion.
- `USER`: không truy cập `/api/v1/admin/**`.

## 4. Công việc chuẩn bị khi bắt đầu Phase 3
- Tạo DTO + mapper riêng cho admin (tránh lộ entity toàn bộ).
- Xây dựng template Thymeleaf hoặc API JSON thuần (quyết định frontend).
- Viết migration seed dữ liệu mẫu tương ứng danh mục trên.
- Bổ sung test coverage cho repository/service admin.

## 5. Đã triển khai (07/11/2025)
- API CRUD `/api/v1/admin/halls|menus|promotions|media/**`.
- Dashboard overview `/api/v1/admin/dashboard/overview` + trang UI dashboard/content (Thymeleaf + JS fetch).
- Migration `V3__admin_seed.sql`, test tích hợp (hall/media admin), cập nhật kế hoạch Phase 3.

## 6. Hướng dẫn nhanh sử dụng admin console
- **Dashboard**: truy cập `/admin/dashboard`, chọn bộ lọc 7/30/90 ngày để xem booking sắp tới; status card hiển thị tỷ lệ theo trạng thái.
- **Content**: truy cập `/admin/content`.
  - Form “Add Hall/Menu” tạo dữ liệu mới (dùng JSON array cho amenities).
  - Bảng có nút **Edit/Delete**; khi Edit sẽ hiển thị form chỉnh sửa phía dưới.
  - Quản lý media: chọn album → xem danh sách; dùng nút *Upload & Fill URL* để tải file (stub) và tự động điền URL trước khi tạo item.
- Mọi thao tác sử dụng API `Bearer` JWT; trang giả định người dùng đã đăng nhập (STAFF/ADMIN).
- Tài khoản mẫu: `admin@wedding.local / 123345678` (seed qua Flyway V4, quyền ADMIN).
