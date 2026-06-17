# AGENTS.md

For project architecture, DDD layering rules, and coding conventions, see `CLAUDE.md`.
For per-app build/run commands, see `admin/README.md` (backend) and `admin-ui/README.md` (frontend).

## Cursor Cloud specific instructions

### Services overview

| Service | Path | Port | Run command |
|---------|------|------|-------------|
| Backend API (Spring Boot 2.3 + Activiti 7) | `admin/` | 8086 | `mvn spring-boot:run -pl interface` (run from `admin/`) |
| Frontend SPA (Vue 3 + Vite) | `admin-ui/` | 3000 | `npm run dev` (run from `admin-ui/`) |
| MySQL 8 | — | 3306 | `sudo service mysql start` |
| Redis (optional) | — | 6379 | `sudo service redis-server start` |

### Critical caveats

- **Backend requires JDK 8.** This is a Spring Boot 2.3.12 / Spring 5.2 project and will not run on the default Java 21. Always build/run the backend with `JAVA_HOME=/usr/lib/jvm/temurin-8-jdk-amd64` (prepend `$JAVA_HOME/bin` to `PATH`). Maven itself is installed but defaults to Java 21, so set `JAVA_HOME` explicitly for every backend command.
- **Backend build before run:** `mvn spring-boot:run -pl interface` needs the sibling modules (`common`, `domain`, `infrastructure`, `application`) installed in the local `.m2`. After changing backend code in those modules, run `mvn clean install -DskipTests` (with JDK 8) first. The local Maven repo persists in the VM snapshot, so this is usually already warm.
- **Services do not auto-start.** MySQL and Redis must be started each session (see table). The `npm install` update script only refreshes frontend deps; it does not start services. MySQL data (including the loaded schema) persists in the VM snapshot.
- **Database:** schema `ddd`, user `root` / password `123456` (matches `application.yml` defaults). The app does NOT auto-create application tables (`spring.datasource.initialization-mode: never`); the `sys_*` / `biz_*` schema is already loaded into the snapshot DB. Activiti's `ACT_*` tables auto-create on backend startup (`spring.activiti.database-schema-update: true`).
- **Schema source of truth:** the SQL files under `admin/sql/` and `admin/infrastructure/src/main/resources/sql/` are an inconsistent set of overlapping create/migration scripts (conflicting IDs, missing columns like `sys_permission.parent_id` and the `sys_data_dictionary*` tables). They cannot all be applied in sequence. The schema currently in the snapshot DB was assembled from the DO entities in `admin/infrastructure/.../dal/entity/` and is the working reference.
- **Login credentials:** `admin` / `123456` (and `user` / `123456`). Passwords are validated with `BCryptPasswordEncoder`, so seeded passwords must be BCrypt hashes — plaintext seeds in `permission.sql` will not authenticate.
- **API paths have no `/api` prefix.** The frontend calls `/api/...` and the Vite dev proxy rewrites `/api` → `` to `localhost:8086`. When hitting the backend directly (e.g. curl), drop the prefix: use `POST http://localhost:8086/login`, not `/api/login`.
