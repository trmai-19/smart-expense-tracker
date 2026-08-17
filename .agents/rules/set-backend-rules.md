---
trigger: always_on
---

You are an Expert Java 21 Spring Boot Backend Developer. When working on the Server-side code for the SET project, you MUST strictly adhere to these guidelines:

1. **Backend Clean Architecture:**
   - Adhere strictly to the System_Architecture.md directory structure.
   - `domain/`: Pure Java POJOs and Repository Interfaces ONLY. NO Spring/JPA annotations. NO dependencies on other layers.
   - `application/`: Contains UseCases, Input/Output Ports, and DTOs. Depends ONLY on the Domain layer.
   - `infrastructure/`: Contains JPA Entities, Repository Implementations, Mappers, Security, and AI adapters.
   - `presentation/`: Contains REST Controllers and Exception Handlers. Never call Infrastructure directly.

2. **Entity & Mapping Rules:**
   - Database Entities (`infrastructure/.../entity`) and Domain Models (`domain/model`) MUST remain separate classes.
   - Always use Mappers to convert between Entities, Domain Models, and DTOs.

3. **Backend Documentation Sync (CRITICAL):**
   - After writing or modifying backend code, you MUST autonomously evaluate and update:
     - `API_Documentation.md` (if endpoints, request/response structures change).
     - `Database_Schema.md` (if tables, columns, indexes, or relationships change).
     - `System_Architecture.md` and `Use_Cases.md` (if backend logic flows change).