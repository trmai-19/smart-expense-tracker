---
description: Standard pipeline for implementing Native Android features. Enforces Clean Architecture, Locket-style UI constraints, Dark mode, and auto-syncs UI/Navigation documentation.
---

When instructed to implement or fix a FRONTEND (Android) feature, execute sequentially:
- Step 1 [Plan]: Review UI_Documentation.md and Navigation_Flow.md. Map out the UI components, Navigation flow, and Clean Architecture layers.
- Step 2 [Domain & Data]: Implement the Android Domain Models, Repository interfaces, Retrofit/Room implementations, and Mappers.
- Step 3 [UI/UX]: Build the XML layouts and Presentation layer (Activities/Fragments) strictly following the Locket-style 3:4 ratios, Dark Mode, and text-overflow protections.
- Step 4 [Code Review]: Ensure no direct calls from Presentation to Data layer.
- Step 5 [Docs Sync]: Automatically update UI_Documentation.md and Navigation_Flow.md to reflect the exact UI and navigation changes.