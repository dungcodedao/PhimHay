# Kế hoạch Triển khai MovieApp - Giai đoạn Mới

## 1. Tổng quan dự án
Một ứng dụng khám phá phim hiện đại được xây dựng với **Clean Architecture**, **Jetpack Compose**, và **Supabase**. Ứng dụng cho phép người dùng khám phá phim, tìm kiếm, quản lý danh sách yêu thích và theo dõi lịch sử xem.

---

## 2. Tiến độ hiện tại (Những gì đã xong)

### **Kiến trúc & Hạ tầng**
- [x] **Clean Architecture**: Tổ chức thành các lớp `data`, `domain`, và `presentation`.
- [x] **Dependency Injection**: Tích hợp hoàn toàn với **Hilt**.
- [x] **Networking**: **Retrofit** được cấu hình cho TMDb API.
- [x] **Backend & Auth**: Tích hợp **Supabase** cho xác thực và đồng bộ đám mây.
- [x] **Lưu trữ cục bộ**: Cơ sở dữ liệu **Room** cho Yêu thích và Lịch sử.
- [x] **Navigation**: Điều hướng type-safe sử dụng **Navigation Compose**.

### **Tính năng & UI**
- [x] **Splash Screen**: Màn hình chào và tải dữ liệu ban đầu.
- [x] **Xác thực**: Luồng Đăng nhập/Đăng ký với Supabase.
- [x] **Trang chủ**: Phim đang thịnh hành và các thể loại.
- [x] **Tìm kiếm**: Tìm kiếm thời gian thực với **Paging 3**.
- [x] **Chi tiết phim**: Thông tin phim chi tiết, diễn viên, phim tương tự và trailer.
- [x] **Yêu thích/Lịch sử**: Lưu trữ cục bộ và quản lý danh sách.
- [x] **Quản lý Theme**: Hỗ trợ chế độ Sáng/Tối.

---

## 3. Công nghệ sử dụng
- **UI**: Jetpack Compose, Material 3
- **Ngôn ngữ**: Kotlin 100%
- **DI**: Hilt
- **Network**: Retrofit, OkHttp, Kotlinx Serialization
- **Database**: Room, Supabase (PostgREST & Auth)
- **Images**: Coil
- **Pagination**: Paging 3

---

## 4. Kế hoạch mới (Các bước tiếp theo)

### **Giai đoạn 1: Hoàn thiện & Tối ưu cốt lõi**
- [x] **Trình phát video**: Tích hợp **Media3/ExoPlayer** để xem trailer.
- [x] **Quản lý hồ sơ**: Hoàn thiện `ProfileScreen` với thông tin người dùng, tải ảnh đại diện và đăng xuất.
- [x] **Xác thực dữ liệu**: Thêm kiểm tra email/mật khẩu và cải thiện UI cho Đăng nhập/Đăng ký.
- [x] **Hiệu ứng Shimmer**: Thêm hiệu ứng tải dữ liệu cho tất cả các màn hình.

### **Giai đoạn 2: Trải nghiệm người dùng & Thẩm mỹ**
- [x] **Shared Element Transitions**: Chuyển cảnh mượt mà từ danh sách sang chi tiết.
- [x] **Lottie Animations**: Thêm micro-animations (ví dụ: khi nhấn yêu thích).
- [x] **Dynamic Theming**: Sử dụng màu sắc từ poster phim để làm nền trang chi tiết.

### **Giai đoạn 3: Tính ổn định & Kết nối**
- [x] **Cache ngoại tuyến thông minh**: Sử dụng Room để lưu phản hồi từ TMDb, cho phép xem khi không có mạng.
- [x] **Theo dõi kết nối mạng**: Hiển thị thông báo "Không có Internet" khi mất kết nối.
- [x] **Deep Linking**: Cho phép mở chi tiết phim trực tiếp từ liên kết ngoài hoặc thông báo.

### **Giai đoạn 4: Xã hội & Bản địa hóa**
- [x] **Hệ thống đánh giá**: Cho phép người dùng đánh giá/nhận xét phim (đồng bộ qua Supabase).
- [x] **Ưu tiên tiếng Việt**: Đã bản địa hóa toàn bộ ứng dụng sang tiếng Việt.
- [x] **Tính năng chia sẻ**: Chia sẻ thông tin phim qua các ứng dụng khác.

---

## 5. Quy tắc phát triển
- Luôn tuân thủ mô hình **Clean Architecture**.
- Giữ các thành phần UI có thể tái sử dụng trong `presentation.components`.
- Sử dụng `Resource<T>` để xử lý trạng thái kết quả từ mạng/cơ sở dữ liệu.
- Đảm bảo tất cả các chuỗi ký tự mới đều được đặt trong `strings.xml`.
