# learnTrack

[![Check & Release](https://github.com/learn-track/learnTrack/actions/workflows/check-and-release.yml/badge.svg)](https://github.com/learn-track/learnTrack/actions/workflows/check-and-release.yml)
![Uptime Robot ratio (30 days)](https://img.shields.io/uptimerobot/ratio/30/m796586558-f9a88c47069e786ed216bd8d)

Backend:
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=learntrack_backend&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=learntrack_backend)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=learntrack_backend&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=learntrack_backend)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=learntrack_backend&metric=bugs)](https://sonarcloud.io/summary/new_code?id=learntrack_backend)

Frontend:
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=learntrack_frontend&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=learntrack_frontend)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=learntrack_frontend&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=learntrack_frontend)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=learntrack_frontend&metric=bugs)](https://sonarcloud.io/summary/new_code?id=learntrack_frontend)

The learnTrack Application is a LMS for swiss schools.  
- [Documentation](./doc/architecture/README.md)
- [Jira](https://learntrack.atlassian.net/jira/software/projects/LERN/boards/2/backlog)

## Deployment

| Branch | Domain                                                                      | Server        | Repo                                                              |
|--------|-----------------------------------------------------------------------------|---------------|-------------------------------------------------------------------|
| Master | https://testing.learntrack.ch                                               | 46.232.178.95 | https://github.com/learn-track/learnTrack-Deployment/tree/master  |
| Master | https://staging.learntrack.ch <br> https://backoffice-staging.learntrack.ch | 5.45.111.246  | https://github.com/learn-track/learnTrack-Deployment/tree/staging |

To get the currently deployed version got to: https://testing.learntrack.ch/info

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

Start [learnTrack-Backend](./backend/src/main/kotlin/ch/learntrack/backend/BackendApplication.kt)
with the [**dev** profile](./.run/learnTrack-Backend%20dev.run.xml).
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

The backend exposes an [OpenAPI definition](http://localhost:8080/openapi/v3/api-docs/learntrack-api) and the according [Swagger UI](http://localhost:3005/api/swagger-ui/index.html) for its API.

Generate API client for the frontend:
```shell
# if necessary
cd frontend
yarn generate-api
```

### Testing

We use Playwright for e2e test. To run the test the [backend](#backend) needs to be running.

To execute the local tests run:

```shell
cd frontend
yarn test:ci
```

To Run the test against the testing environment with different browsers run:

```shell 
cd frontend
yarn test:testing
```

#### Troubleshooting

If test fail which are dependent on data in the database reinitialise the database with the [DB seeds](backend/src/main/resources/db/seeds).


### Backoffice

#### Development Flow

Make sure to always pull the latest changes before you start developing. Then develop locally and push the changes to the repo.
When the API changes are deployed pull the backoffice on the server.

#### Local Setup

1. Start de [backend dev environment](#backend)
2. Navigate to http://localhost
3. Sign up with the email `nilsrothe05@icloud.com`
4. Create a new Workspace
5. Click on **Create new** in the top right and select **Import**
6. Choose **Import from Git repository**
7. Select **Github** and check the **I have an existing repository**.
8. Enter `git@github.com:learn-track/backoffice-appsmith.git` as remote url
9. Add the generated key to the [repo's deployment keys](https://github.com/learn-track/backoffice-appsmith/settings/keys) with write access
10. Click **Import**
11. When asked for Datasource credentials. Enter the credentials found in the [application-dev.yml](./backend/src/main/resources/application-dev.yml)
    - URL: http://host.docker.internal:8080/backoffice

### E-Mail

Locally send mails are received by a [mail crab](https://github.com/tweedegolf/mailcrab) container instance.

You can find the mailbox under: http://localhost:1090.

### Tutorials

If you have questions [see](./doc/tutorials) for some basic workflows.


