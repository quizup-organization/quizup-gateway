# quizup-gateway (staging local)

Structure Maven : un seul module `quizup-gateway-infrastructure` (le module `quizup-gateway-domain`
a été supprimé — le gateway est purement config-driven, sans domaine ni use cases).

Le code applicatif, lorsqu'il existe, reste organise sous `io.github.quizup.gateway.application.*` dans le module
infrastructure.

