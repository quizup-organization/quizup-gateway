# AGENTS.md — quizup-gateway

> **API Gateway** Spring Cloud Gateway (WebFlux). C'est un service **config-driven** : aucune classe
> Java de route — tout est dans les fichiers `application-{profile}.yml`.
> Pour les règles de patterns (non applicable ici) : [
`../../best-practices/.backend/hexagonal-architecture.md`](../../best-practices/.backend/hexagonal-architecture.md).

---

## 1. Rôle

Point d'entrée unique vers les microservices QuizUp. Routage par préfixe (`/identity-service/`,
`/theme-service/`, …) avec `StripPrefix=1` (le préfixe est retiré avant d'envoyer vers le
microservice cible). Le frontend appelle **toujours** à travers ce gateway.

**Package** : `io.github.quizup.gateway`
**Port** : `8080` (local et prod)
**CORS** : global allow-all (`/**`)
**Swagger** : agrégation des docs OpenAPI des services (identity/theme/game/social)

---

## 2. Routes Spring Cloud Gateway

### `application-local.yml`

| id                      | uri (local)             | Path (prédicat)            | StripPrefix |
|-------------------------|-------------------------|----------------------------|-------------|
| `identity-service`      | `http://localhost:8085` | `/identity-service/**`     | `1`         |
| `game-service`          | `http://localhost:8082` | `/game-service/**`         | `1`         |
| `theme-service`         | `http://localhost:8086` | `/theme-service/**`        | `1`         |
| `social-service`        | `http://localhost:8087` | `/social-service/**`       | `1`         |
| `matchmaking-service`   | `http://localhost:8090` | `/matchmaking-service/**`  | `1`         |
| `profile-service`       | `http://localhost:8088` | `/profile-service/**`      | `1`         |
| `leaderboard-service`   | `http://localhost:8091` | `/leaderboard-service/**`  | `1`         |
| `game-service-ws`       | `ws://localhost:8082`   | `/game-service/ws`         | `1`         |
| `social-service-ws`     | `ws://localhost:8087`   | `/social-service/ws`       | `1`         |
| `matchmaking-service-ws`| `ws://localhost:8090`   | `/matchmaking-service/ws`  | `1`         |
| `profile-service-ws`    | `ws://localhost:8088`   | `/profile-service/ws`      | `1`         |

### `application-prod.yml`

| id                      | uri (env var / défaut)                                                              | Path                        | StripPrefix |
|-------------------------|-------------------------------------------------------------------------------------|-----------------------------|-------------|
| `identity-service`      | `${QUIZUP_GATEWAY_IDENTITY_URL:…quizup-identity.quizup-prod.svc.cluster.local}`       | `/identity-service/**`      | `1`         |
| `game-service`          | `${QUIZUP_GATEWAY_GAME_URL:…quizup-game.quizup-prod.svc.cluster.local}`               | `/game-service/**`          | `1`         |
| `theme-service`         | `${QUIZUP_GATEWAY_THEME_URL:…quizup-theme.quizup-prod.svc.cluster.local}`             | `/theme-service/**`         | `1`         |
| `social-service`        | `${QUIZUP_GATEWAY_SOCIAL_URL:…quizup-social.quizup-prod.svc.cluster.local}`           | `/social-service/**`        | `1`         |
| `matchmaking-service`   | `${QUIZUP_GATEWAY_MATCHMAKING_URL:…quizup-matchmaking.quizup-prod.svc.cluster.local}` | `/matchmaking-service/**`   | `1`         |
| `profile-service`       | `${QUIZUP_GATEWAY_PROFILE_URL:…quizup-profile.quizup-prod.svc.cluster.local}`         | `/profile-service/**`       | `1`         |
| `leaderboard-service`   | `${QUIZUP_GATEWAY_LEADERBOARD_URL:…quizup-leaderboard.quizup-prod.svc.cluster.local}` | `/leaderboard-service/**` | `1`         |
| `game-service-ws`       | `${QUIZUP_GATEWAY_GAME_URL:ws://quizup-game.quizup-prod.svc.cluster.local}`           | `/game-service/ws`          | `1`         |
| `social-service-ws`     | `${QUIZUP_GATEWAY_SOCIAL_URL:ws://quizup-social.quizup-prod.svc.cluster.local}`       | `/social-service/ws`        | `1`         |
| `matchmaking-service-ws`| `${QUIZUP_GATEWAY_MATCHMAKING_URL:ws://quizup-matchmaking.quizup-prod.svc.cluster.local}` | `/matchmaking-service/ws` | `1`         |
| `profile-service-ws`    | `${QUIZUP_GATEWAY_PROFILE_URL:ws://quizup-profile.quizup-prod.svc.cluster.local}`     | `/profile-service/ws`       | `1`         |

> **WebSocket/STOMP** : le frontend ouvre **N connexions STOMP** (1 par service émetteur :
> game, social, matchmaking, profile) **via le gateway** — jamais les ports des services en direct.
> Le gateway route chaque connexion vers le `/ws` SockJS du service concerné (le préfixe
> `/xxx-service/` est retiré avant forwarding). Les services `identity` et `theme` n'ont
> **pas** de route WS (theme : broker désactivé ; identity : rien d'émis).

---

## 3. Structure Maven

- `quizup-gateway-domain` : **vide** (seulement `pom.xml`) — pas de domaine, pas d'use cases.
- `quizup-gateway-infrastructure` :
    - `GatewayServiceApplication.java` (`@SpringBootApplication`, `static void main`)
    - `application.yml` / `application-local.yml` / `application-prod.yml`


