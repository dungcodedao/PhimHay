# 🎬 MovieApp - Ứng dụng Khám phá Phim Hiện đại

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://www.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-orange.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-blue.svg)](https://developer.android.com/jetpack/compose)
[![Clean Architecture](https://img.shields.io/badge/Architecture-Clean%20Architecture-lightgrey.svg)](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

**MovieApp** là một ứng dụng Android mẫu mạnh mẽ, được xây dựng với các công nghệ hiện đại nhất để thể hiện kỹ năng phát triển ứng dụng di động chuẩn Production-ready. Ứng dụng tập trung vào trải nghiệm người dùng mượt mà, hỗ trợ tiếng Việt và tích hợp đồng bộ dữ liệu đám mây.

---

## ✨ Tính năng nổi bật

- 🏠 **Khám phá phim:** Cập nhật các phim đang thịnh hành (Trending), phim phổ biến và lọc theo thể loại.
- 🔍 **Tìm kiếm thông minh:** Tìm kiếm phim thời gian thực với cơ chế Debounce và Paging 3 (Tải trang vô tận).
- 📱 **Chi tiết phim cao cấp:**
  - Xem thông tin chi tiết, dàn diễn viên.
  - Xem trailer trực tiếp với trình phát **Media3/ExoPlayer**.
  - **Dynamic Theming:** Giao diện tự động đổi màu theo poster phim.
  - **Shared Element Transition:** Hiệu ứng chuyển cảnh "ảnh bay" nghệ thuật.
- ❤️ **Yêu thích & Lịch sử:** Lưu trữ phim yêu thích và lịch sử xem đồng bộ giữa local (Room) và cloud (Supabase).
- ⭐ **Đánh giá & Nhận xét:** Hệ thống review phim thời gian thực tích hợp với cộng đồng người dùng.
- 🌐 **Offline First:** Hỗ trợ lưu cache dữ liệu để xem phim ngay cả khi không có mạng.
- 🔗 **Deep Linking:** Mở trực tiếp trang chi tiết phim từ link bên ngoài.

---

## 🛠 Tech Stack (Công cụ sử dụng)

- **Ngôn ngữ:** Kotlin 100%.
- **Giao diện:** Jetpack Compose (Material 3).
- **Kiến trúc:** Clean Architecture + MVVM + UseCases.
- **Dependency Injection:** Dagger Hilt.
- **Networking:** Retrofit & OkHttp (Kotlinx Serialization).
- **Cơ sở dữ liệu:** 
  - **Room:** Lưu trữ cache và dữ liệu offline.
  - **Supabase:** Quản lý Auth (Đăng nhập) và Cloud Sync (Real-time DB).
- **Xử lý ảnh:** Coil.
- **Animation:** Lottie & Compose Animations.
- **Phản hồi người dùng:** Palette API (Dynamic colors).

---

## 📐 Kiến trúc dự án (Architecture)

Dự án tuân thủ nghiêm ngặt **Clean Architecture** với 3 lớp tách biệt:

1.  **Data Layer:** Chịu trách nhiệm truy xuất dữ liệu từ API (TMDb) và Local DB (Room).
2.  **Domain Layer:** Chứa Business Logic (UseCases) và Repository Interfaces. Đây là lớp trung tâm, độc lập với các thư viện bên ngoài.
3.  **Presentation Layer:** Sử dụng Jetpack Compose và ViewModel để quản lý State và hiển thị UI.

---

## 🚀 Cài đặt & Chạy ứng dụng

1.  Clone dự án về máy.
2.  Cung cấp **TMDb API Key** và **Supabase URL/API Key** trong file `AppUtil.kt` (hoặc cấu hình buildConfig).
3.  Build và chạy trên thiết bị Android (Min SDK 26).

---

## 📸 Ảnh chụp màn hình (Mockup)

*(Sẽ được bổ sung sau khi chụp ảnh Portfolio)*

---

*Dự án được thực hiện nhằm mục đích trình diễn kỹ năng Android chuyên sâu.*
