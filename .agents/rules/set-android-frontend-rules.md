You are an Expert Native Android Kotlin Developer. When working on the Android app for the SET project, you MUST strictly adhere to these guidelines:

1. **Android Clean Architecture:**
   - `domain/`: Contains Models, Repository interfaces, and UseCases.
   - `data/`: Contains Retrofit APIs, Room DAOs, local/remote Entities, network DTOs, Repository implementations, and Mappers.
   - `presentation/`: Contains Activities (Single Activity architecture), Jetpack Compose Screens, and ViewModels. Never call the `data/` layer directly; always go through UseCases/Domain.

2. **UI/UX & Responsive Rules (Strict Jetpack Compose):**
   - Follow the design principles in UI_Documentation.md. Apply Pure Dark Mode (#0D0D0D / #1A1A1A).
   - Use Jetpack Compose exclusively for all UI components. Avoid XML layouts.
   - Use `LocalAccentColor` (CompositionLocal) for dynamic Neon Accent Colors.
   - Ensure Camera and History Timeline UI strictly use the 3:4 aspect ratio with 28dp rounded corners (Locket-style).
   - Prevent text overflow by using `TextOverflow.Ellipsis` and `maxLines`. 
   - Always wrap input screens with `verticalScroll` and `imePadding()` to handle keyboard insets gracefully.

3. **Frontend Documentation Sync (CRITICAL):**
   - After modifying Android code or UI, you MUST autonomously evaluate and update:
     - `UI_Documentation.md` (if components, composables, or UI logic change).
     - `Navigation_Flow.md` (if screen transitions, bottom sheets, or tabs change in NavHost).
     - `System_Architecture.md` and `Use_Cases.md` (if Android logic flows change).