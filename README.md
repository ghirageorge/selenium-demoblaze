# Selenium — DemoBlaze (Java + Maven + TestNG)

CI status:

[![selenium-e2e](https://github.com/ghirageorge/selenium-demoblaze/actions/workflows/selenium.yml/badge.svg?branch=main)](https://github.com/ghirageorge/selenium-demoblaze/actions/workflows/selenium.yml)

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
