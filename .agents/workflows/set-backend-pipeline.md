---
description: Standard pipeline for implementing Spring Boot backend features. Handles Clean Architecture layers, DB entities, REST APIs, and auto-syncs API/DB documentation.
---

When instructed to implement or fix a BACKEND (Server) feature, execute sequentially:
- Step 1 [Plan]: Review API_Documentation.md and Database_Schema.md. Propose the REST endpoints, DB tables, and architecture layers involved.
- Step 2 [Domain & App]: Write the pure Kotlin Domain Models, Repository interfaces, UseCases, and DTOs.
- Step 3 [Infrastructure]: Implement JPA Entities, Repositories, and Mappers.
- Step 4 [Presentation]: Implement REST Controllers and global exception handling.
- Step 5 [Docs Sync]: Automatically update Database_Schema.md and API_Documentation.md to perfectly reflect the new backend state.