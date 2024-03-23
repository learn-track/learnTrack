# learnUP

[![Check & Release](https://github.com/lernUP/learnUP/actions/workflows/check-and-release.yml/badge.svg)](https://github.com/lernUP/learnUP/actions/workflows/check-and-release.yml)
![Uptime Robot ratio (30 days)](https://img.shields.io/uptimerobot/ratio/30/m796586558-f9a88c47069e786ed216bd8d)

Backend:
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=learnUP_backend&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=learnUP_backend)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=learnUP_backend&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=learnUP_backend)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=learnUP_backend&metric=bugs)](https://sonarcloud.io/summary/new_code?id=learnUP_backend)

Frontend:
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=learnUP_frontend&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=learnUP_frontend)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=learnUP_frontend&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=learnUP_frontend)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=learnUP_frontend&metric=bugs)](https://sonarcloud.io/summary/new_code?id=learnUP_frontend)

The learnUP Application is a digital learning platform. It is a school group project fot the Modul 345 
- [Documentation](./doc/architecture/README.md)
- [Jira](https://v7bauma.atlassian.net/jira/software/projects/LERN/boards/2/backlog)

## Deployment

| Branch | Domain                     | Server        | Repo                                         |
|--------|----------------------------|---------------|----------------------------------------------|
| Master | http://learnup.brunner.top | 46.232.178.95 | https://github.com/lernUP/learnUP-Deployment |

To get the currently deployed version got to: http://learnup.brunner.top/info

## Development

### Prerequisites
* [Docker](https://docs.docker.com/desktop/install/mac-install/)
* [SDKMAN!](https://sdkman.io/install)
* [nvm](https://github.com/nvm-sh/nvm)

### Setup

```shell
./setup.sh
```

#### Backend

Start [learnUP-Backend](./backend/src/main/kotlin/ch/learnup/backend/BackendApplication.kt)
with the [**dev** profile](./.run/learnUP-Backend%20dev.run.xml).
The docker containers needed for development should be started automatically. If not run:

```shell
(cd backend && docker compose up -d)
```

#### Frontend

In IntelliJ, install the prettier plugin and select the installed prettier dependency to format js, jsx, ts and tsx files.

Start the dev environment:

```shell
# if necessary
cd frontend 
yarn dev
```

Format:

```shell
yarn format
```

Lint:

```shell
yarn lint
```

### Jooq

The backend uses jooq to generate kotlin database classes.
To make changes to the database schema add an entry to [changes](./backend/src/main/resources/db/changelog/changes) and restart the backend to apply the changes to the databse.
To generate the jooq classes run `(cd backend && ./gradlew generateJooq)`.

### Open API

The backend exposes an [OpenAPI definition](http://localhost:8080/openapi/v3/api-docs) and the according [Swagger UI](http://localhost:3005/api/swagger-ui/index.html) for its API.

Generate API client for the frontend:
```shell
# if necessary
cd frontend
yarn generate-api-client
```

### Tutorials

If you have questions [see](./doc/tutorials) for some basic workflows.


