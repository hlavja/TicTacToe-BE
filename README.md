# Tic Tac Toe BackEnd

A simple TicTacToe (Three-In-Rrow) app developed as semester project in [PIA](https://github.com/osvetlik/pia2020/tree/master/semester-project) on Master's studies on Faculty of applied sciences University of West Bohemia.
This repository contains only backend [Spring Boot Application](https://spring.io/projects/spring-boot) which was build on the top of [JHipster](https://www.jhipster.tech/) platform.

For running whole project you will need also [Angular](https://angular.io/) frontend part of app - [GitHub repository](https://github.com/hlavja/TicTacToe-FrontEnd)

## Run application

Tools for running application: Docker, Maven

Requires also frontend part.

## Building containers

1. checkout [frontend](https://github.com/hlavja/TicTacToe-FrontEnd) and backend
2. build frontend app and create container (execute in frontend root). It creates container with name pia/hlavja-tictactoe-frontend

```
docker build -t pia/hlavja-tictactoe-frontend .
```

3. build backend app and create container (execute in backend root). It creates container with name pia/hlavja-tictactoe-backend

```
mvnw -Pprod verify jib:dockerBuild
```

4. run docker compose in backend root

```
docker-compose -f src/main/docker/compose.yml up -d
```

5. shut down whole app by

```
docker-compose -f src/main/docker/compose.yml down
```

##OpenAPI
Application also includes OpenAPI [documentation](/src/main/swagger/swagger.yaml) which was used to generate HttpClient code in frontend part. _Swagger Codegen CLI_ is a powerful tool and make coding faster.

##Usage of application
To enable dev mode you need to change **SPRING_PROFILES_ACTIVE** property in [app.yml](src/main/docker/app.yml) to **dev,swagger**. In default, application has 10 sec delay
before start cause of creating other containers (especially database needs some time to be ready).

Application has three predefined users:

|        Login        | Password |
| :-----------------: | :------: |
|  user@localhost.cz  |   user   |
| admin@localhost.cz  |  admin   |
| system@localhost.cz |  system  |

User profile is accessible under email in the right part of navbar. It should be used to change user's password or change non mandatory properties.

Users can add or remove friends on lobby tab. Only online non friends are shown, in friends tab are shown also offline friends.

Player can be challenged to game only if not playing one already.

On board screen user sees who's turn, what piece he has and Give Up! button.

On history tab are shown all games results in system.

###Docker fck-up
Sometimes on the first deploy to docker, frontend container may fail because of not knowing the backend service. It needed to be restarted manually
or use compose down and then again up.

**Important:** do not change backend service name or Nginx config in frontend will crash!
