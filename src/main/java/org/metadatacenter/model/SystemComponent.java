package org.metadatacenter.model;

public enum SystemComponent {

  SERVER_GROUP(ServerName.GROUP),
  SERVER_MESSAGING(ServerName.MESSAGING),
  SERVER_REPO(ServerName.REPO),
  SERVER_RESOURCE(ServerName.RESOURCE),
  SERVER_SCHEMA(ServerName.SCHEMA),
  SERVER_SUBMISSION(ServerName.SUBMISSION),
  SERVER_ARTIFACT(ServerName.ARTIFACT),
  SERVER_TERMINOLOGY(ServerName.TERMINOLOGY),
  SERVER_USER(ServerName.USER),
  SERVER_VALUERECOMMENDER(ServerName.VALUERECOMMENDER),
  SERVER_WORKER(ServerName.WORKER),
  SERVER_OPENVIEW(ServerName.OPENVIEW),
  SERVER_MONITOR(ServerName.MONITOR),
  SERVER_IMPEX(ServerName.IMPEX),
  SERVER_BRIDGE(ServerName.BRIDGE),
  FRONTEND_DEVELOPMENT("frontend-development"),
  FRONTEND_TEST("frontend-test"),
  FRONTEND_PRODUCTION("frontend-production"),
  ADMIN_TOOL("admin-tool"),
  CADSR_TOOL("cadsr-tool"),
  // The infrastructure CEDAR runs on. None of it is a JVM: these are nginx templates, container init
  // scripts and docker-compose, which read the environment directly and never touch the Java
  // configuration model. Split by service rather than kept as one "infra" because they share nothing —
  // the certificate authority and the reverse proxy have no variables and no failures in common, and a
  // single column would say only "some part of the infrastructure reads this".
  INFRA_NGINX("infra-nginx"),
  INFRA_MONGO("infra-mongo"),
  INFRA_MYSQL("infra-mysql"),
  INFRA_NEO4J("infra-neo4j"),
  INFRA_CA("infra-ca"),
  INFRA_DOCKER("infra-docker"),
  KEYCLOAK_SERVER("keycloak-server"), // Keycloak itself: its datasource, ports and CA, from standalone.xml
  KEYCLOAK_EVENT_LISTENER("keycloak-event-listener"), // the CEDAR listener deployed into Keycloak
  CEDAR_CLI("cedar-cli"), // the Python CLI: build, release and mode management
  UTIL_BIN("util-bin"), // shell scripts in cedar-util/bin
  ALL("all"); // generic case, include all variables

  private ServerName serverName;
  private String useCase;

  SystemComponent(ServerName serverName) {
    this.serverName = serverName;
  }

  SystemComponent(String useCase) {
    this.useCase = useCase;
  }

  public ServerName getServerName() {
    return serverName;
  }

  public String getStringValue() {
    if (serverName != null) {
      return serverName.getName();
    } else {
      return useCase;
    }
  }

  public static SystemComponent getFor(ServerName serverName) {
    for (SystemComponent sc : values()) {
      if (sc.getServerName() == serverName) {
        return sc;
      }
    }
    return null;
  }

  public static SystemComponent getForUseCase(String useCase) {
    for (SystemComponent sc : values()) {
      if (sc.useCase != null && sc.useCase.equals(useCase)) {
        return sc;
      }
    }
    return null;
  }
}
