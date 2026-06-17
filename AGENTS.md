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
- **Connecting to MySQL from the shell:** the unix socket (`mysql -uroot ...`) is not accessible to the agent user (permission denied). Always connect over TCP: `mysql -uroot -p123456 -h 127.0.0.1 ddd`.
- **Database:** schema `ddd`, user `root` / password `123456` (matches `application.yml` defaults). The app does NOT auto-create application tables (`spring.datasource.initialization-mode: never`); the `sys_*` / `biz_*` schema is loaded from SQL (persists in the snapshot DB). Activiti's `ACT_*` tables auto-create on backend startup (`spring.activiti.database-schema-update: true`).
- **Schema source of truth:** use `admin/sql/schema_full.sql` — a single, idempotent, reproducible script that creates every application table (reconstructed from the DO entities in `admin/infrastructure/.../dal/entity/` + mapper XML join tables) plus seed/reference data. Apply with `mysql -uroot -p123456 -h 127.0.0.1 ddd < admin/sql/schema_full.sql` if the DB is ever empty. The other older scripts under `admin/sql/` and `admin/infrastructure/src/main/resources/sql/` are an inconsistent set of overlapping create/migration scripts (conflicting IDs, missing columns) that cannot be applied in sequence — prefer `schema_full.sql`.
- **Login credentials:** `admin` / `123456` (and `user` / `123456`). Passwords are validated with `BCryptPasswordEncoder`, so seeded passwords MUST be BCrypt hashes (plaintext seeds will not authenticate). `schema_full.sql` seeds the correct BCrypt hash for `123456`. If you ever need to regenerate a hash, the `BCrypt` class is bundled in `spring-security-core` (not a separate `spring-security-crypto` jar): `BCrypt.hashpw("123456", BCrypt.gensalt(10))`.
- **Permissions / 403s:** controllers like `UserController`, `RoleController`, `DepartmentController`, `MenuController`, etc. use `@PreAuthorize("hasAuthority('...')")` with NO `hasRole('admin')` fallback. `CustomUserDetailsService` grants an admin user *every* code present in `sys_permission` (via `findAll()`), so any `@PreAuthorize` code missing from `sys_permission` causes a 403 even for admin. `schema_full.sql` seeds the complete permission-code catalog matching all `@PreAuthorize` annotations, so admin has full UI access. Authorities are loaded per-request from the DB, so changing `sys_permission`/`sys_role_permission` takes effect on the next request (no backend restart needed).
- **API paths have no `/api` prefix.** The frontend calls `/api/...` and the Vite dev proxy rewrites `/api` → `` to `localhost:8086`. When hitting the backend directly (e.g. curl), drop the prefix: use `POST http://localhost:8086/login`, not `/api/login`.
- **CORS origin must match the host you browse with.** The backend CORS allow-list (`SecurityConfig.corsConfigurationSource()`) lists explicit origins. When accessing the UI via a forwarded port the browser origin is usually `http://127.0.0.1:3000`, so that origin (plus `localhost`) must be in the allow-list or the login `POST` is blocked with 403 ("拒绝访问") even though the page and captcha load. `vite.config.ts` sets `server.host: true` so the dev server is reachable on `127.0.0.1` and the LAN IP, not only IPv6 `localhost` (this VM's `localhost` resolves only to `::1`).
