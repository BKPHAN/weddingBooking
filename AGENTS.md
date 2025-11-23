# Repository Guidelines

## Project Structure & Module Organization
Backend code resides in `src/main/java/com/demo/web`, split into `config`, `controller`, `model`, `repository`, and `service`; keep business logic inside services and expose endpoints through controllers only. Thymeleaf templates live under `src/main/resources/templates` and should mirror controller mapping names for easy resolution. Static resources (`css`, `js`, `images`) belong in `src/main/resources/static`; reuse concise names like `home.css` or `booking.js` and compress heavy media before merging. Place integration notes or diagrams in `docs/`, and update `database/` with any schema or seed adjustments so local teammates can replay migrations. Build output stays in `target/`; exclude that directory and `.idea/` artifacts from commits.

## Build, Test, and Development Commands
Use `./mvnw spring-boot:run` (macOS/Linux) or `.\mvnw spring-boot:run` (Windows) to start the dev server with DevTools reloading; confirm the local MySQL `wedding_db` is reachable first. Run `./mvnw test` before every push to execute the JUnit/Spring Boot suite. `./mvnw clean package` produces the runnable JAR in `target/` for release validation, while `./mvnw dependency:tree` helps audit transitive upgrades during reviews.

## Coding Style & Naming Conventions
Target Java 21 with 4-space indentation and UTF-8 sources. Package names remain lowercase, classes and interfaces use PascalCase, methods and fields use camelCase, and constants stay SCREAMING_SNAKE_CASE. Favor constructor injection plus Spring stereotypes (`@Service`, `@Repository`) and rely on Lombok (`@Data`, `@Builder`) to reduce boilerplate unless custom validation is needed. Keep Thymeleaf markup semantic and CSS selectors descriptive but short.

## Testing Guidelines
Mirror production packages under `src/test/java/com/demo/web`, naming classes `*Tests`. Use `@SpringBootTest` for full-stack scenarios, `@DataJpaTest` for repositories, and slice tests or mocks for web layers when you only need controller coverage. Add regression tests for each bug fix or database change and capture edge cases in booking calculations. Always run `./mvnw test` locally prior to PR submission.

## Commit & Pull Request Guidelines
Commits follow conventional-commit style (e.g., `feat: add booking confirmation email`) and should cover one logical concern. Pull requests must describe the issue solved, list affected layers (controller, service, etc.), mention any new configs or SQL scripts, include screenshots when templates or CSS change, and reference tickets. Document verification steps (tests run, manual flows exercised) within the PR body.

## Security & Configuration Tips
Never commit secrets; move credentials into `application-{env}.properties` or environment variables. The defaults in `src/main/resources/application.properties` are for local use only—override them before deploying anywhere else. Review new dependencies for licenses and vulnerabilities, and call out any configuration toggles or feature flags introduced in a PR.
