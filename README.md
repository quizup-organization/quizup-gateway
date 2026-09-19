# quizup-gateway (staging local)

Structure cible en 2 sous-modules Maven :

- `quizup-gateway-domain`
- `quizup-gateway-infrastructure`

Seul `quizup-gateway-domain` est importable inter-services.

Le code applicatif, lorsqu'il existe, reste organise sous `io.github.quizup.gateway.application.*` dans le module
infrastructure.

