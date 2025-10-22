# Selenium — DemoBlaze (Java + Maven + TestNG)

Two clean, portfolio-friendly Selenium tests against the public DemoBlaze store.
- **Add to cart → Checkout** (happy path)
- **Catalog pagination** (next/prev)

## Run locally
```bash
mvn test
# headless:
mvn -Dheadless=true test
# override site:
mvn -DbaseUrl=https://www.demoblaze.com test
```

## CI (GitHub Actions)
Workflow in `.github/workflows/selenium.yml` runs on push/PR, installs Java 21 + Chrome, executes tests in headless mode, and uploads Surefire reports and failure screenshots.
