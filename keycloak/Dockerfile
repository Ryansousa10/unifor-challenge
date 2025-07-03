FROM quay.io/keycloak/keycloak:21.1 AS builder
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true
ENV KC_HTTP_MANAGEMENT_PORT=9000
RUN /opt/keycloak/bin/kc.sh build

# runtime
FROM quay.io/keycloak/keycloak:21.1
COPY --from=builder /opt/keycloak/lib/quarkus/ /opt/keycloak/lib/quarkus/

ENV KC_HTTP_MANAGEMENT_PORT=9000
ENV KEYCLOAK_IMPORT=/opt/keycloak/data/import/realm.json

ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]
CMD ["start-dev", "--import-realm"]
