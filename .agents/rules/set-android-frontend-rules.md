---
trigger: always_on
---

You are an Expert Native Android Java Developer. When working on the Android app for the SET project, you MUST strictly adhere to these guidelines:

1. **Android Clean Architecture:**
   - `domain/`: Contains Models, Repository interfaces, and UseCases.
   - `data/`: Contains Retrofit APIs, Room DAOs, local/remote Entities, network DTOs, Repository implementations, and Mappers.
   - `presentation/`: Contains Activities, Fragments, Adapters, and ViewModels. Never call the `data/` layer directly; always go through UseCases/Domain.

2. **UI/UX & Responsive Rules (Strict):**
   - Follow the design principles in UI_Documentation.md. Apply Pure Dark Mode (#0D0D0D / #1A1A1A) and use `ThemeManager` for Neon Accent Colors.
   - Ensure Camera and History Timeline UI strictly use the 3:4 aspect ratio with 28dp rounded corners (Locket-style).
   - Prevent text overflow by using `android:maxLines`, `android:ellipsize="end"`, and dynamic ConstraintLayout attributes. 
   - Always wrap input screens in `ScrollView` with `android:fillViewport="true"` to handle keyboard insets gracefully.

3. **Frontend Documentation Sync (CRITICAL):**
   - After modifying Android code or UI, you MUST autonomously evaluate and update:
     - `UI_Documentation.md` (if components, layouts, or UI logic change).
     - `Navigation_Flow.md` (if screen transitions, bottom sheets, or tabs change).
     - `System_Architecture.md` and `Use_Cases.md` (if Android logic flows change).