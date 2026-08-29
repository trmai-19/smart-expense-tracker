---
description: Standard pipeline for implementing Native Android features. Enforces Clean Architecture, Locket-style UI constraints, Dark mode, and auto-syncs UI/Navigation documentation.
---

When instructed to implement or fix a FRONTEND (Android) feature, execute sequentially:
- Step 1 [Plan]: Review UI_Documentation.md and Navigation_Flow.md. Map out the UI components, Navigation flow, and Clean Architecture layers.
- Step 2 [Domain & Data]: Implement the Android Domain Models, Repository interfaces, Retrofit/Room implementations, and Mappers in Kotlin.
- Step 3 [UI/UX]: Build the Jetpack Compose Screens and Presentation layer (ViewModels) strictly following the Locket-style 3:4 ratios, Dark Mode, and Compose layout guidelines.
- Step 4 [Code Review]: Ensure no direct calls from Presentation to Data layer, and verify no XML layouts were used.
- Step 5 [Docs Sync]: Automatically update UI_Documentation.md and Navigation_Flow.md to reflect the exact UI and navigation changes.