# Quy trình tiếp nhận & chăm sóc phản hồi khách hàng

## 1. Tiếp nhận phản hồi
- **Nguồn**: form `/contact` hoặc API `/api/feedbacks`.
- **Hệ thống ghi nhận**: `ContactService.saveContact`.
- **Thông báo**: mail stub gửi tới `support@wedding.local` (hoặc log nếu `notification.mail.enabled=false`).
- **Cờ trạng thái tự động**:
  - `URGENT`: nội dung chứa từ khóa gấp/urgent/asap.
  - `FOLLOW_UP`: cùng email gửi lại trong vòng 7 ngày.
  - `NEW`: trạng thái mặc định.

## 2. Phân công xử lý
- Hệ thống tự gán nhân sự đầu tiên có `primaryRole = STAFF`.
- Nếu không có STAFF sẽ giao cho ADMIN.
- Có thể cập nhật thủ công qua `ContactService.assignContact(contactId, userId)`.

## 3. Theo dõi & phản hồi
- Nhân sự phụ trách liên hệ khách, cập nhật flag:
  - `ASSIGNED`: khi bắt đầu xử lý.
  - `RESOLVED`: khi đã phản hồi xong (qua `ContactService.markResolved`).

## 4. Ghi chú & lưu trữ
- Ghi nhận nội dung liên hệ trong CRM nội bộ (chưa triển khai).
- Lưu contact để phục vụ thống kê và chăm sóc định kỳ (truy vấn API tương lai).

## 5. Checklist dành cho nhân sự
1. Kiểm tra email thông báo hoặc dashboard contact mới.
2. Liên hệ khách trong vòng 2 giờ với flag `URGENT`, 8 giờ với `NEW`.
3. Cập nhật flag `ASSIGNED` ngay khi bắt đầu xử lý.
4. Sau khi hoàn thành, chuyển trạng thái `RESOLVED`, ghi chú trong CRM.
5. Nếu khách yêu cầu báo giá/đặt tiệc -> chuyển thông tin cho bộ phận booking và follow-up.
