export const environment = {
  production: true,
  keycloak: {
    url: 'http://localhost:8080', // Em produção, altere para a URL real do Keycloak
    realm: 'unifor',
    clientId: 'unifor-frontend',
  },
  backend: {
    url: 'http://localhost:8081', // Em produção, altere para a URL real do backend
    apiPath: '/api'
  }
};

