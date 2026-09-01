package org.metadatacenter.model;

public enum ServerName {

  GROUP("group", "CEDAR Group Server"),
  MESSAGING("messaging", "CEDAR Messaging Server"),
  REPO("repo", "CEDAR Repo Server"),
  RESOURCE("resource", "CEDAR Resource Server"),
  SCHEMA("schema", "CEDAR Schema Server"),
  SUBMISSION("submission", "CEDAR Submission Server"),
  ARTIFACT("artifact", "CEDAR Template Server"),
  TERMINOLOGY("terminology", "CEDAR Terminology Server"),
  USER("user", "CEDAR User Server"),
  VALUERECOMMENDER("valuerecommender", "CEDAR ValueRecommender Server"),
  WORKER("worker", "CEDAR Worker Server"),
  OPENVIEW("openview", "CEDAR OpenView Server"),
  MONITOR("monitor", "CEDAR Monitor Server"),
  IMPEX("impex", "CEDAR Impex Server"),
  BRIDGE("bridge", "CEDAR Bridge Server");

  private final String name;

  /**
   * What this server calls itself on its index page.
   *
   * <p>Not derived from the name: the display forms capitalise differently (OpenView,
   * ValueRecommender) and the artifact server has reported itself as "CEDAR Template Server" since
   * before it was renamed. Each string is what that server's index page has always returned, kept
   * here so the fifteen identical IndexResource subclasses that carried them could go.
   */
  private final String displayName;

  ServerName(String name, String displayName) {
    this.name = name;
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }

  public static ServerName forName(String server) {
    for (ServerName s : ServerName.values()) {
      if (s.getName().equals(server)) {
        return s;
      }
    }
    return null;
  }

  public String getName() {
    return name;
  }
}
