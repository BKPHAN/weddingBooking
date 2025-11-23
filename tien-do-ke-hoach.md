# Tiến độ thực hiện kế hoạch Spring Boot

| Phase | Mục tiêu chính | Ngày cập nhật | Trạng thái | Ghi chú |
|-------|----------------|---------------|------------|---------|
| Phase 1 - Nền tảng hệ thống | Cấu hình dự án, thiết kế dữ liệu, bảo mật cơ bản | 2025-11-06 | ✅ Hoàn thành | Đã bổ sung Flyway, mở rộng mô hình dữ liệu, triển khai JWT skeleton và làm mới UI booking/contact theo schema mới. |
| Phase 2 - Booking & Contact nâng cao | Hoàn thiện nghiệp vụ booking/contact, validation, unit test | 2025-11-07 | ✅ Hoàn thành | Validation nâng cao, contact workflow, thông báo, unit/integration test và tài liệu follow-up đã hoàn tất. |
| Phase 3 - Admin & nội dung | Dashboard, CRUD halls/menus/promotions, API JSON | 2025-11-07 | ✅ Hoàn thành | Xây dựng API CRUD, dashboard overview, UI quản trị (dashboard/content), quản lý media, upload stub và seed dữ liệu demo. |
| Phase 4 - Tích hợp & hoàn thiện | Email/SMS, Docker deploy, kiểm thử tích hợp | -- | ⏳ Chưa thực hiện | Dự kiến triển khai sau khi các module chính ổn định, tập trung bàn giao và triển khai. |

## Chi tiết công việc từng phase

### Phase 1 – Nền tảng hệ thống (✅ Hoàn thành)
- Chuẩn hoá cấu hình Spring Boot: UTF-8, Flyway, Actuator, Validation, bổ sung dependency cần thiết.
- Thiết kế lại mô hình dữ liệu (Booking, Contact, Halls, Menu, Promotion, Media, User/Role, RefreshToken) và tạo migration `V1__init_schema`, `V2__seed_data`.
- Tạo DTO/Mapper (MapStruct), cập nhật controller/service/repository theo kiến trúc nhiều lớp.
- Thiết lập bảo mật cơ bản với JWT stateless, custom filter, user detail service và endpoint xác thực.
- Làm mới giao diện và JS cho booking/contact để thu thập dữ liệu đầy đủ, sử dụng `ApiResponse`.

### Phase 2 – Booking & Contact nâng cao (🟡 Đang thực hiện)
- ✅ Áp dụng validation nghiệp vụ cho booking (kiểm tra ngày, ngân sách, tránh trùng sảnh) và contact (cờ URGENT/FOLLOW_UP).
- ✅ Tự động phân công nhân sự mặc định, gửi thông báo (log + mail giả lập) cho booking/contact, phản hồi JSON chi tiết hơn.
- ✅ Bổ sung unit test cho `BookingService`, `ContactService` cùng REST integration test cho `/api/booking` và `/api/feedbacks`.
- ✅ Viết tài liệu quy trình tiếp nhận & phân loại contact, hướng dẫn xử lý follow-up (`docs/contact-followup-process.md`).
- 🔜 Rà soát REST edge-case/validation và chuẩn bị tài liệu chuyển tiếp Phase 3.

### Phase 3 – Admin & nội dung (✅ Hoàn thành)
- Đã hoàn thiện API CRUD quản trị cho halls, menus, promotions, media albums/items và dashboard overview.
- Xây dựng trang dashboard & content (Thymeleaf + JS) với chart đơn giản, bộ lọc booking, form tạo/sửa/xoá và upload media stub.
- Seed dữ liệu demo qua `V3__admin_seed.sql`, bổ sung tài liệu `docs/phase3-prep-notes.md`, `docs/contact-followup-process.md`.
- Kiểm thử REST/admin (JUnit + MockMvc) và cập nhật kế hoạch `tien-do-ke-hoach.md`.

### Phase 4 – Tích hợp & hoàn thiện (⏳ Chưa thực hiện)
- Kết nối email/SMS thực tế, đồng bộ lịch (Google/Outlook) nếu khả thi.
- Thử nghiệm thanh toán đặt cọc (VNPay/MoMo mock) và kiểm thử end-to-end.
- Docker hóa (ứng dụng + MySQL/Flyway) và viết hướng dẫn triển khai staging/production.
- Bổ sung kiểm thử tích hợp (Spring Boot Test với H2), đảm bảo coverage JaCoCo ≥ 70%.
- Chuẩn bị tài liệu bàn giao: hướng dẫn admin, README cập nhật, quy trình vận hành Flyway/JWT.

> Ghi chú: Nội dung được cập nhật theo tiến độ mới nhất của từng phase, bám sát `ke-hoach-spring-boot.txt`.
