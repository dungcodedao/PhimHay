# 🚀 Bộ Prompt Nâng cấp MovieApp (PhimHay) Chuyên nghiệp

Bộ prompt này được thiết kế để nâng cấp dự án MovieApp của bạn lên tiêu chuẩn Production-Ready, giúp bạn tạo ấn tượng mạnh với Nhà tuyển dụng (NTD).

---

## 🔐 Prompt 1: Hoàn thiện UI/UX & Xử lý lỗi (Edge-to-edge, Animations, State)
**Mục tiêu:** Làm app trông mượt mà và "pro" hơn đúng chuẩn bộ UI/UX hiện đại.

**Nội dung Prompt:**
> "Kích hoạt skills: @project-movie-context, @jetpack-compose-ui.
> Ứng dụng MovieApp của tôi đã hoàn thiện logic cốt lõi. Bây giờ, hãy đóng vai trò là Senior UI/UX Android Developer để giúp tôi nâng cấp trải nghiệm người dùng với các tác vụ sau:
> 1. **Shared Element Transition:** Cung cấp code mẫu để làm hiệu ứng chuyển cảnh mượt mà: khi click vào Poster phim ở HomeScreen, ảnh đó sẽ 'bay' và phóng to sang màn hình DetailScreen.
> 2. **Edge-to-edge & Dark Mode:** Hướng dẫn tôi cấu hình App tràn viền (Edge-to-edge) và đảm bảo code giao diện của tôi tự động tương thích với Dark Mode của hệ thống.
> 3. **Error Handling & Empty State:** Viết một Composable ErrorScreen và EmptyStateScreen dùng chung. Giao diện này phải hiển thị Lottie animation cảnh báo khi mất mạng hoặc khi danh sách yêu thích (Watchlist) trống, kèm theo nút 'Thử lại' (Retry)."

---

## 🔐 Prompt 2: Bảo mật Database (Supabase RLS)
**Mục tiêu:** Thiết lập bảo mật, bảo vệ dữ liệu người dùng tránh truy cập trái phép.

**Nội dung Prompt:**
> "Kích hoạt skill: @supabase-engineer.
> Tôi đang sử dụng Supabase để lưu trữ danh sách Phim yêu thích (Watchlist) và Lịch sử xem (History) của người dùng. Hãy hướng dẫn tôi cách thiết lập Row Level Security (RLS) trên Supabase Dashboard.
> Yêu cầu:
> 1. **Viết các câu lệnh SQL (Policies) để:**
>     ◦ Người dùng chỉ có thể SELECT, INSERT, UPDATE, DELETE những dòng dữ liệu có user_id trùng khớp với auth.uid() của họ.
>     ◦ Chặn hoàn toàn quyền truy cập ẩn danh (Anonymous) vào các bảng này.
> 2. **Giải thích ngắn gọn** cách hoạt động của RLS để tôi có thể đưa vào file README giải thích cho nhà tuyển dụng."

---

## 💼 Prompt 3: Chuẩn bị Portfolio & Sơ đồ Architecture (Mermaid)
**Mục tiêu:** Tạo tài liệu kỹ thuật chất lượng cao (Technical Documentation) cho Portfolio.

**Nội dung Prompt:**
> "Bạn là một Tech Lead đang cần viết tài liệu cho dự án MovieApp (PhimHay). Dự án sử dụng Clean Architecture, MVVM, Hilt, Supabase, Room, Paging 3, và Jetpack Compose.
> Hãy giúp tôi:
> 1. **Kiến trúc (Architecture Diagram):** Viết code tạo sơ đồ bằng định dạng Mermaid. Sơ đồ phải thể hiện rõ luồng dữ liệu một chiều (UDF) từ: UI (Compose) -> ViewModel -> UseCase -> Repository -> Remote (TMDb/Supabase) & Local (Room DB). Chú thích rõ phần Room đóng vai trò là 'Single Source of Truth'.
> 2. **Professional README.md:** Viết một file README.md chuyên nghiệp bằng tiếng Anh và tiếng Việt để nộp CV. Cấu trúc bao gồm: Tóm tắt dự án, Tech Stack, Tính năng nổi bật, Sơ đồ Mermaid vừa tạo, và Hướng dẫn cài đặt."

---

## 💡 Hướng dẫn sử dụng:
1. Bạn có thể copy từng prompt này gửi cho mình bất cứ khi nào bạn muốn thực hiện phần đó.
2. Bạn có thể lưu file này vào GitHub để làm tài liệu tham khảo cho các dự án sau.

*Ngày tạo: 21/02/2026*
