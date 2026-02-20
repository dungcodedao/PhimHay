# 🎬 MovieApp - PhimHay 
### *Ứng dụng Khám phá Phim Hiện đại & Trải nghiệm Điện ảnh Đỉnh cao*

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://www.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Clean Architecture](https://img.shields.io/badge/Architecture-Clean-FF4081?style=for-the-badge)](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

**PhimHay (MovieApp)** là một dự án Android cao cấp, được xây dựng để thể hiện kỹ năng phát triển ứng dụng chuẩn **Production-Ready**. Ứng dụng không chỉ có giao diện Dark Cinematic sang trọng mà còn sở hữu kiến trúc bên dưới cực kỳ vững chắc, đáp ứng các tiêu chuẩn khắt khe nhất của các công ty công nghệ lớn.

---

## 🚀 Tính năng vượt trội

- 🎬 **Trải nghiệm Cinematic:** Giao diện tối (Dark Mode) huyền bí, tập trung tối đa vào hình ảnh phim.
- 🔍 **Tìm kiếm mạnh mẽ:** Tìm kiếm thời gian thực với **Paging 3** (Load tới đâu tải tới đó) và cơ chế xử lý tránh lag (Debounce).
- 🎞️ **Trình phát Video Media3:** Xem Trailer phim mượt mà ngay trong app với **ExoPlayer**.
- 🎨 **Dynamic UI:** Tự động trích xuất màu từ poster phim để thay đổi theme trang chi tiết (sử dụng Palette API).
- 🔐 **Xác thực Bảo mật:** Hệ thống Đăng nhập/Đăng ký tích hợp **Supabase Auth**.
- 💾 **Offline First:** Lưu trữ lịch sử và yêu thích qua **Room Database**, cho phép xem offline bất cứ lúc nào.
- ✨ **Animation đỉnh cao:** Hiệu ứng chuyển động mượt mà với **Shared Element Transitions** và **Lottie Animations**.

---

## 🛠 Tech Stack (Công nghệ cốt lõi)

| Mảng | Công nghệ sử dụng |
| :--- | :--- |
| **Giao diện** | **Jetpack Compose**, Material 3, Lottie, Coil (Image Loading) |
| **Logic/Xử lý** | **Kotlin Coroutines**, Flow, UseCases |
| **Quản lý dữ liệu** | **Retrofit**, OkHttp, Kotlinx Serialization |
| **Bên thứ 3** | **Supabase** (Auth & Real-time DB), TMDb API |
| **Lưu trữ** | **Room Persistence**, DataStore |
| **Dependency Injection** | **Dagger Hilt** |

---

## 📐 Kiến trúc (Architecture)

Dự án áp dụng mô hình **Clean Architecture** kết hợp với **MVVM**, đảm bảo code Dễ đọc - Dễ test - Dễ bảo trì:

1.  **Presentation (UI/ViewModel):** Xử lý UI State và nhận sự kiện từ người dùng.
2.  **Domain (Business Logic):** Chứa các UseCase độc lập, thuần Kotlin, không phụ thuộc Framework.
3.  **Data (Infrastructure):** Nơi giao tiếp với API và Database (Repository Pattern).

---

## 📸 Hình ảnh minh họa

| Splash & Home | Chi tiết Phim | Tìm kiếm |
| :---: | :---: | :---: |
| *(Chèn ảnh 1)* | *(Chèn ảnh 2)* | *(Chèn ảnh 3)* |

---

## 🛠 Hướng dẫn cài đặt

1. **Clone project:**
   ```bash
   git clone https://github.com/dungcodedao/PhimHay.git
   ```
2. **Cấu hình API:**
   - Đăng ký API Key tại [TMDb](https://www.themoviedb.org/).
   - Tạo Project tại [Supabase](https://supabase.com/).
   - Cập nhật thông tin vào file cấu hình `local.properties` hoặc biến môi trường.
3. **Build & Run:** Mở dự án bằng **Android Studio Ladybug** (hoặc mới hơn) và nhấn Run.

---

## 📮 Liên hệ

Dự án được phát triển bởi **Ngô Văn Dũng**
- 📧 Email: [Địa chỉ email của bạn]
- 💼 LinkedIn: [Link LinkedIn của bạn]
- 📁 Portfolio: [Link Portfolio nếu có]

---
*Nếu bạn thấy dự án này thú vị, đừng quên tặng mình 1 ⭐ nhé!*
