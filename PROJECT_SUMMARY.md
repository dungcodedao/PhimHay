# Dự án MovieApp - PhimHay: Nhật ký Phát triển & Tổng kết Công việc

Tài liệu này tổng hợp toàn bộ các công việc, công nghệ và tính năng đã được triển khai trong dự án **MovieApp (PhimHay)** tính đến thời điểm hiện tại.

---

## 1. Kiến trúc Hệ thống (Architecture)
Dự án được xây dựng dựa trên mô hình **Clean Architecture** kết hợp với **MVVM**, chia dự án thành các Module tách biệt để đảm bảo khả năng mở rộng và kiểm thử:
- **Presentation Layer**: Sử dụng Jetpack Compose cho UI, ViewModel để quản lý State.
- **Domain Layer**: Chứa các Business Logic (UseCases) và Repository Interface (Thuần Kotlin).
- **Data Layer**: Triển khai Repository, gọi API từ Cloud (TMDb) và quản lý DB cục bộ (Room).

---

## 2. Các công nghệ cốt lõi đã tích hợp (Tech Stack)
- **UI & UX**:
    - **Jetpack Compose**: Xây dựng UI bằng code khai báo (Declarative UI).
    - **Material 3**: Hệ thống Design System mới nhất từ Google.
    - **Palette API**: Trích xuất màu từ ảnh để thay đổi Theme động theo từng bộ phim.
    - **Lottie**: Tích hợp các hiệu ứng animations mượt mà.
    - **Coil**: Thư viện tải hình ảnh tối ưu.
- **Dependency Injection**: 
    - **Hilt (Dagger)**: Quản lý khởi tạo và tiêm phụ thuộc toàn dự án.
- **Network & API**:
    - **Retrofit & OkHttp**: Giao tiếp với TMDb API.
    - **Kotlinx Serialization**: Chuyển đổi dữ liệu JSON sang Kotlin Object.
- **Database & Backend**:
    - **Supabase**: Quản lý Xác thực (Authentication) và đồng bộ dữ liệu Real-time.
    - **Room Database**: Lưu trữ dữ liệu Offline cho Phim yêu thích và Lịch sử xem.
- **Media**:
    - **Media3/ExoPlayer**: Trình phát Video hiện đại để xem Trailer.

---

## 3. Các tính năng đã hoàn thiện (Done)

### ✅ Khám phá & Tìm kiếm
- Hiển thị danh sách phim theo danh mục: Trending, Top Rated, Now Playing...
- Tìm kiếm phim thời gian thực với cơ chế **Debounce** (giảm thiểu tải cho server).
- Phân trang tự động bằng **Paging 3** cho danh sách kết quả tìm kiếm.

### ✅ Chi tiết Phim (Detail Screen)
- Hiển thị thông tin tổng quan, điểm đánh giá, ngày phát hành.
- Danh sách dàn diễn viên (Cast) thực tế của phim.
- Tích hợp trình phát Trailer video.
- Cơ chế **Dynamic Background**: Nền màn hình thay đổi màu sắc dựa trên Poster phim.

### ✅ Quản lý Người dùng & Cá nhân hóa
- Luồng **Authentication**: Đăng nhập/Đăng ký thông qua Supabase.
- Chức năng **Yêu thích (Watchlist)**: Lưu phim vào danh sách riêng.
- Chức năng **Lịch sử xem**: Theo dõi các phim đã từng xem.
- Hệ thống đánh giá/nhận xét phim (Real-time).

### ✅ Tối ưu hệ thống (Performance)
- Hỗ trợ **Offline First**: Xem lại dữ liệu cũ khi không có kết nối Internet nhờ Room.
- Hiệu ứng **Shimmer Loading**: Nâng cao trải nghiệm khi chờ dữ liệu tải về.
- Chuyển cảnh mượt mà giữa các màn hình (Compose Animations).

---

## 4. Quản lý Dự án & Repo
- Thiết lập file `.gitignore` chuẩn quy trình Production: Loại bỏ logs, file build, cấu hình cá nhân và metadata thừa.
- Xây dựng file `README.md` chuyên nghiệp để nộp CV và giới thiệu với nhà tuyển dụng.
- Đồng bộ mã nguồn lên GitHub Repo: **PhimHay**.

---

## 5. Kế hoạch Giai đoạn Tiếp theo (Next Steps)
- [ ] Thiết kế lại màn hình **Splash** với hiệu ứng Cinematic cực hiệu ứng.
- [ ] Tối ưu giao diện **Login/Register** với phong cách mờ ảo (Glassmorphism).
- [ ] Triển khai **Deep Linking** để chia sẻ phim qua mạng xã hội.
- [ ] Cải thiện tốc độ tải dữ liệu thông qua cơ chế Caching nâng cao.

---
**Ngày tổng kết:** 20/02/2026
**Tác giả:** Ngô Văn Dũng
