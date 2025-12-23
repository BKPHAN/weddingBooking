# BÁO CÁO ĐỒ ÁN MÔN HỌC: WEBSITE QUẢN LÝ TRUNG TÂM TIỆC CƯỚI

## 1. GIỚI THIỆU ĐỀ TÀI

### 1.1. Tổng quan
Dự án xây dựng một hệ thống website toàn diện cho Trung tâm Tiệc cưới, phục vụ hai đối tượng chính: khách hàng (người dùng cuối) và ban quản lý (Admin/Staff). Hệ thống nhằm mục đích số hóa quy trình quảng bá dịch vụ, đặt lịch và quản lý vận hành trung tâm tiệc cưới.

### 1.2. Mục tiêu dự án
*   **Đối với khách hàng:** Cung cấp thông tin trực quan về không gian sảnh, thực đơn, các gói dịch vụ. Cho phép khách hàng tìm hiểu, ước tính chi phí và gửi yêu cầu đặt lịch trực tuyến một cách thuận tiện.
*   **Đối với quản trị viên:** Cung cấp công cụ quản lý chuyên nghiệp để theo dõi lịch đặt, xử lý thông tin liên hệ, cập nhật nội dung website (thực đơn, sảnh cưới, tin tức) và báo cáo thống kê doanh thu/hoạt động.

---

## 2. PHÂN TÍCH VÀ THIẾT KẾ HỆ THỐNG

### 2.1. Yêu cầu chức năng
#### Phân hệ Khách hàng (Public Site)
*   **Trang chủ:** Giới thiệu tổng quan, banner sự kiện, các dịch vụ nổi bật.
*   **Giới thiệu & Dịch vụ:** Thông tin chi tiết về trung tâm, các gói dịch vụ cưới hỏi, sự kiện.
*   **Sảnh tiệc & Thực đơn:** Xem danh sách, chi tiết sảnh cưới (sức chứa, hình ảnh) và thực đơn (món ăn, giá cả).
*   **Đặt lịch (Booking):** Form đăng ký tiệc trực tuyến (chọn ngày, sảnh, số lượng khách, ngân sách, ghi chú).
*   **Liên hệ & Tư vấn:** Form gửi câu hỏi, yêu cầu tư vấn nhanh.
*   **Thư viện Media:** Xem album ảnh, video hoạt động thực tế.

#### Phân hệ Quản trị (Admin Dashboard)
*   **Xác thực:** Đăng nhập bảo mật (JWT Authentication).
*   **Dashboard:** Thống kê tổng quan số lượng booking, trạng thái xử lý, doanh thu dự kiến.
*   **Quản lý Booking:** Danh sách đơn đặt, bộ lọc trạng thái (Pending/Confirmed/Cancelled), chi tiết yêu cầu, duyệt/hủy đơn.
*   **Quản lý Liên hệ (Contacts):** Theo dõi phản hồi khách hàng, đánh dấu trạng thái xử lý.
*   **Quản lý Nội dung (CMS):** Thêm/Sửa/Xóa thông tin Sảnh cưới, Thực đơn, Khuyến mãi, Album ảnh.

### 2.2. Chi tiết Công nghệ & Lý do lựa chọn
Hệ thống được xây dựng trên nền tảng các công nghệ mạnh mẽ và phổ biến nhất hiện nay, đảm bảo tính ổn định và khả năng bảo trì lâu dài:

1.  **Spring Boot (Backend Framework):**
    *   **Lý do chọn:** Cung cấp khả năng phát triển nhanh (RAD), cấu hình tự động (Auto Configuration) và hệ sinh thái thư viện khổng lồ.
    *   **Ứng dụng:** Xử lý logic nghiệp vụ, RESTful API, quản lý bảo mật với Spring Security.
2.  **MySQL (Database):**
    *   **Lý do chọn:** Hệ quản trị cơ sở dữ liệu quan hệ phổ biến, miễn phí, hỗ trợ tốt các giao dịch phức tạp (ACID) cần thiết cho booking.
    *   **Ứng dụng:** Lưu trữ bền vững dữ liệu người dùng, đơn hàng, danh mục.
3.  **Hibernate / Spring Data JPA (ORM):**
    *   **Lý do chọn:** Tăng tốc độ phát triển bằng cách ánh xạ object-table, giảm thiểu việc viết query SQL thủ công dễ gây lỗi.
4.  **Thymeleaf (Template Engine):**
    *   **Lý do chọn:** Tích hợp sâu với Spring, cho phép render giao diện ngay phía server (SSR), tốt cho SEO ban đầu so với SPA thuần túy.
5.  **JWT (JSON Web Token):**
    *   **Lý do chọn:** Chuẩn xác thực hiện đại, stateless (không lưu session trên server), giúp hệ thống dễ dàng mở rộng (scale) sau này.

### 2.3. Thiết kế Hệ thống Chi tiết
#### 2.3.1. Biểu đồ Use Case (Use Case Diagram)
Hệ thống được chia thành 2 nhóm tác nhân chính với các chức năng cụ thể:

*   **Khách hàng (Guest/User):**
    *   **Tìm kiếm & Tra cứu:** Xem danh sách sảnh cưới, thực đơn, album ảnh.
    *   **Đặt lịch (Booking):** Đăng ký thông tin đặt tiệc, nhận báo giá sơ bộ.
    *   **Liên hệ:** Gửi phản hồi hoặc yêu cầu tư vấn.
*   **Quản trị viên (Admin/Staff):**
    *   **Quản lý hệ thống:** Đăng nhập, quản lý tài khoản nhân viên.
    *   **Quản lý kinh doanh:** Duyệt đơn đặt tiệc, quản lý danh sách sảnh, thực đơn.
    *   **Thống kê:** Xem báo cáo doanh thu, hiệu suất hoạt động.

![Biểu đồ Use Case](docs/diagrams/usecase_diagram.png)

#### 2.3.2. Thiết kế Cơ sở dữ liệu (ERD Model)
Mô hình Quan hệ Thực thể (Entity Relationship Diagram) được thiết kế chuẩn hóa để đảm bảo tính toàn vẹn dữ liệu:

```mermaid
erDiagram
    USERS ||--o{ BOOKINGS : "manage"
    BOOKINGS }|--|| HALLS : "organized_at"
    BOOKINGS ||--|{ BOOKING_SERVICES : "includes"
    MENUS ||--|{ BOOKING_MENUS : "selected_in"
    BOOKINGS ||--|{ BOOKING_MENUS : "has"
    
    USERS {
        int id PK
        string username
        string password_hash
        string role "ADMIN/STAFF"
    }

    HALLS {
        int id PK
        string name
        int capacity
        decimal base_price
    }

    BOOKINGS {
        int id PK
        string bride_name
        string groom_name
        datetime event_date
        string status "PENDING/CONFIRMED"
        int hall_id FK
    }

    MENUS {
        int id PK
        string name
        decimal price
    }
    
    CONTACTS {
        int id PK
        string email
        string message
        boolean is_resolved
    }
```

![Biểu đồ ERD](docs/diagrams/erd_diagram.png)

#### 2.3.3. Mô tả các thực thể chính
*   **USERS:** Quản lý thông tin đăng nhập và phân quyền (RBAC).
*   **HALLS:** Lưu trữ thông tin cơ sở vật chất (Sảnh cưới), giá sàn, sức chứa tối đa.
*   **BOOKINGS:** Bảng trung tâm lưu trữ toàn bộ thông tin giao dịch đặt tiệc, liên kết chặt chẽ với Sảnh (Halls) và Dịch vụ đi kèm.
*   **MENUS & SERVICES:** Danh mục sản phẩm kinh doanh, có thể mở rộng linh hoạt.

#### 2.3.4. Kiến trúc Hệ thống Tổng quan (High-Level Architecture)
Mô hình kiến trúc phân lớp (Layered Architecture) được áp dụng để đảm bảo sự tách biệt rõ ràng giữa các thành phần:

```mermaid
graph TD
    Client[Client Browser / Mobile] -->|HTTP Request| Controller[Controller Layer (Web/API)]
    
    subgraph "Backend (Spring Boot)"
        Controller -->|DTO| Service[Service Layer (Business Logic)]
        Service -->|Entity| Repo[Repository Layer (Data Access)]
        Repo -->|JPA/Hibernate| DB[(MySQL Database)]
        
        Service -->|SMTP| Email[Email Service]
        Service -->|Upload| Cloud[Cloud Storage]
    end
    
    subgraph "Frontend"
        View[Thymeleaf View] -.->|Render| Client
        JS[JavaScript / AJAX] -.->|API Call| Controller
    end
```

![Kiến trúc hệ thống](docs/diagrams/architecture_diagram.png)

#### 2.3.5. Chi tiết các Quy trình nghiệp vụ (Sequence Diagrams)
Dưới đây là biểu đồ tuần tự cho các chức năng cốt lõi của hệ thống:

**a. Quy trình Đặt tiệc (Booking Process)**
*(Luồng quan trọng nhất dành cho khách hàng)*

```mermaid
sequenceDiagram
    actor User as Khách hàng
    participant FE as Frontend
    participant Ctrl as BookingController
    participant Svc as BookingService
    participant Repo as BookingRepository
    participant DB as MySQL
    participant Mail as EmailService

    User->>FE: Gửi form đặt tiệc
    FE->>Ctrl: POST /api/booking
    activate Ctrl
    Ctrl->>Svc: createBooking()
    activate Svc
    
    Svc->>Svc: Validate(ngày, sảnh)
    alt Validate Failed
        Svc-->>Ctrl: Error
        Ctrl-->>FE: Lỗi validation
    else Success
        Svc->>Repo: save()
        Repo->>DB: INSERT bookings
        Svc->>Mail: Gửi email xác nhận
        Svc-->>Ctrl: Success
    end
    deactivate Svc
    Ctrl-->>FE: 200 OK
    deactivate Ctrl
```

![Quy trình Đặt tiệc](docs/diagrams/sequence_booking.png)

**b. Quy trình Xác thực & Tài khoản (Authentication & Account)**

*   **Đăng ký tài khoản (Registration Flow):**

```mermaid
sequenceDiagram
    actor User as Người dùng mới
    participant FE as Frontend
    participant Auth as AuthController
    participant Svc as UserService
    participant DB as Database

    User->>FE: Nhập thông tin đăng ký
    FE->>Auth: POST /api/auth/register
    activate Auth
    Auth->>Svc: registerUser(DTO)
    
    Svc->>Svc: Check trùng Email/Username
    alt Email đã tồn tại
        Svc-->>Auth: Error: Exists
        Auth-->>FE: 400 Bad Request
        FE-->>User: "Email đã được sử dụng"
    else Hợp lệ
        Svc->>Svc: Hash Password (BCrypt)
        Svc->>DB: INSERT User (Role: USER)
        DB-->>Svc: Success
        Svc-->>Auth: Success
        Auth-->>FE: 201 Created
        FE-->>User: "Đăng ký thành công"
    end
    deactivate Auth
```

![Quy trình Đăng ký](docs/diagrams/sequence_register.png)

*   **Đăng nhập hệ thống (Login Flow):**

```mermaid
sequenceDiagram
    actor User as Người dùng
    participant FE as Frontend
    participant Auth as AuthController
    participant Sec as SecurityConfig
    participant DB as Database

    User->>FE: Nhập User/Pass
    FE->>Auth: POST /api/auth/login
    activate Auth
    Auth->>Sec: authenticate()
    Sec->>DB: Find User & Match Hash
    
    alt Sai mật khẩu / Không tồn tại
        DB-->>Sec: Fail
        Sec-->>Auth: Exception
        Auth-->>FE: 401 Unauthorized
    else Hợp lệ
        DB-->>Sec: UserDetails
        Sec->>Sec: Generate JWT
        Sec-->>Auth: Token Response
        Auth-->>FE: 200 OK + Token
        FE->>FE: Lưu Token (LocalStorage/Cookie)
    end
    deactivate Auth
```

![Quy trình Đăng nhập](docs/diagrams/sequence_login.png)

**c. Quy trình Quản lý Nội dung (Admin CRUD Flow)**
*(Mô hình chung cho quản lý Sảnh, Menu, Tin tức)*

```mermaid
sequenceDiagram
    actor Admin
    participant FE as Dashboard
    participant API as ResourceAPI
    participant DB as Database

    Admin->>FE: "Thêm mới Sảnh/Món ăn"
    FE->>API: POST /api/resources
    API->>API: Validate Input
    API->>DB: INSERT record
    DB-->>API: Success ID
    API-->>FE: 201 Created
    FE-->>Admin: Cập nhật danh sách hiển thị
```

![Quy trình CRUD Admin](docs/diagrams/sequence_crud.png)

**d. Quy trình Gửi Liên hệ (Contact Flow)**

```mermaid
sequenceDiagram
    actor Guest as Khách vãng lai
    participant Web as Website
    participant API as ContactAPI
    participant DB as Database

    Guest->>Web: Điền form liên hệ
    Web->>API: POST /api/contacts
    API->>DB: Save Message (Status: PENDING)
    API-->>Web: Success Message
    Web-->>Guest: "Cảm ơn bạn đã liên hệ"
```

![Quy trình Liên hệ](docs/diagrams/sequence_contact.png)

#### 2.3.6. Biểu đồ Hoạt động (Activity Diagrams) - Nghiệp vụ Chi tiết
Mô tả luồng đi của dữ liệu và các điểm ra quyết định trong quy trình:

**a. Quy trình Đăng ký (User Registration Flow)**

```mermaid
flowchart TD
    Start([Bắt đầu]) --> Input[Khách nhập thông tin Đăng ký]
    Input --> Valid{Kiểm tra dữ liệu?}
    Valid -- Không hợp lệ --> Error1[Hiển thị lỗi Validation]
    Error1 --> Input
    
    Valid -- Hợp lệ --> CheckExists{Check Trùng Email?}
    CheckExists -- Đã tồn tại --> Error2[Báo lỗi: Email đã dùng]
    Error2 --> Input
    
    CheckExists -- Chưa tồn tại --> Hash[Mã hóa mật khẩu]
    Hash --> SaveDB[(Lưu xuống CSDL)]
    SaveDB --> Success[Thông báo Đăng ký thành công]
    Success --> End([Kết thúc])
```

![Quy trình Đăng ký](docs/diagrams/activity_register.png)

**b. Quy trình Đăng nhập (User Login Flow)**

```mermaid
flowchart TD
    Start([Bắt đầu]) --> Input[Nhập Username & Password]
    Input --> CheckDB{Kiểm tra Database}
    
    CheckDB -- Sai TK/MK --> Fail[Báo lỗi đăng nhập]
    Fail --> Input
    
    CheckDB -- Đúng thông tin --> GenToken[Tạo JWT Token]
    GenToken --> SaveClient[Lưu Token phía Client]
    SaveClient --> Redirect[Chuyển hướng về Trang chủ]
    Redirect --> End([Kết thúc])
```

![Quy trình Đăng nhập](docs/diagrams/activity_login.png)

#### 2.3.7. Biểu đồ Trạng thái Booking (State Diagram)

![Trạng thái Booking](docs/diagrams/state_booking.png)

### 2.4. Tính thực tiễn & Ứng dụng thực tế
Dự án không chỉ dừng lại ở lý thuyết mà giải quyết các bài toán "đau đầu" thực tế của các trung tâm tiệc cưới hiện nay:

#### 1. Giải quyết bài toán "Quản lý thủ công"
*   **Thực trạng:** Nhiều trung tâm vẫn dùng sổ sách hoặc Excel rời rạc để lưu lịch, dễ dẫn đến tình trạng **"trùng lịch"** (double booking) hoặc sót đơn hàng.
*   **Giải pháp:** Hệ thống tự động kiểm tra sảnh trống, chặn đặt trùng lịch ngay từ khâu đăng ký, giúp ban quản lý hoàn toàn yên tâm.

#### 2. Nâng cao trải nghiệm khách hàng (Customer Experience)
*   **Thực trạng:** Khách phải đến tận nơi để xem giá, xem thực đơn.
*   **Giải pháp:** Khách hàng có thể ngồi tại nhà xem ảnh 360 độ, so sánh giá các set menu, tự ước tính chi phí cho đám cưới của mình chỉ với vài cú click.

#### 3. Tối ưu hóa vận hành & Doanh thu
*   Hệ thống báo cáo giúp người quản lý nhìn thấy ngay biểu đồ doanh thu, món ăn nào bán chạy, sảnh nào đắt khách để đưa ra chiến lược kinh doanh (khuyến mãi, giảm giá) kịp thời.

### 2.4. Thiết kế Cơ sở dữ liệu (Database Schema)
Mô hình dữ liệu quan hệ (ERD) bao gồm các thực thể chính:
*   `users`: Tài khoản quản trị viên và nhân viên.
*   `bookings`: Lưu trữ thông tin đơn đặt tiệc, thông tin khách hàng, trạng thái.
*   `halls`: Thông tin sảnh tiệc (tên, sức chứa, giá thuê, tiện ích).
*   `menus`: Thông tin món ăn, set menu.
*   `contacts`: Lưu trữ các yêu cầu liên hệ từ khách hàng.
*   `promotions` & `media_items`: Quản lý tin khuyến mãi và thư viện ảnh.

---

## 3. KẾT QUẢ TRIỂN KHAI

### 3.1. Các module đã hoàn thiện
1.  **Nền tảng hệ thống:** Đã thiết lập cấu trúc Spring Boot, kết nối MySQL, cấu hình UTF-8, tích hợp Flyway migration.
2.  **Module Booking & Contact:** Hoàn thiện luồng đặt lịch, validation dữ liệu chặt chẽ, workflow xử lý liên hệ từ khách hàng.
3.  **Module Admin Management:** Đã xây dựng hoàn chỉnh các tính năng CRUD cho Sảnh tiệc (Halls), Thực đơn (Menus), Khuyến mãi (Promotions). Dashboard hiển thị thống kê cơ bản.
4.  **API Services:** Cung cấp đầy đủ REST API cho các tác vụ quản lý và giao tiếp dữ liệu (`/api/v1/**`).

### 3.2. Trạng thái hiện tại
Các chức năng cốt lõi đã hoạt động ổn định và vượt qua các bài kiểm thử đơn vị (Unit Test) và tích hợp (Integration Test). Hệ thống sẵn sàng cho giai đoạn kiểm thử chấp nhận người dùng (UAT) và triển khai thử nghiệm.

---

## 4. HƯỚNG PHÁT TRIỂN & MỞ RỘNG
*   **Thanh toán điện tử:** Tích hợp cổng thanh toán VNPay/Momo để hỗ trợ đặt cọc online.
*   **Tương tác thời gian thực:** Tích hợp Chatbot AI để tư vấn tự động và hệ thống thông báo realtime (WebSocket).
*   **Mobile App:** Phát triển ứng dụng di động cho khách hàng theo dõi kế hoạch cưới.
*   **Công nghệ AR/VR:** Xem sảnh tiệc thực tế ảo 360 độ.

---
*Báo cáo được tổng hợp dựa trên hiện trạng project "Wedding Booking System".*
