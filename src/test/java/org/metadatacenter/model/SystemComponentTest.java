package org.metadatacenter.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * A microservice reads only the environment variables declared for its component, and it finds that
 * component from its server name as it boots. The admin tool's {@code env-listFor} accepts either a
 * use case or a server name. A server without a component could not build its configuration.
 */
class SystemComponentTest {

  @ParameterizedTest
  @EnumSource(ServerName.class)
  void everyServerFindsTheComponentItReadsItsEnvironmentAs(ServerName serverName) {
    SystemComponent component = SystemComponent.getFor(serverName);

    assertNotNull(component, serverName.getName());
    assertSame(serverName, component.getServerName());
    assertEquals(serverName.getName(), component.getStringValue());
    assertSame(serverName, ServerName.forName(serverName.getName()));
  }

  @ParameterizedTest
  @EnumSource(SystemComponent.class)
  void everyComponentThatIsNoServerIsFoundByItsUseCase(SystemComponent component) {
    if (component.getServerName() != null) {
      assertNull(SystemComponent.getForUseCase(component.getStringValue()),
          "a server is found by its server name, not as a use case");
      return;
    }
    assertSame(component, SystemComponent.getForUseCase(component.getStringValue()));
    assertNull(ServerName.forName(component.getStringValue()));
  }

  @Test
  void anUnknownNameFindsNothing() {
    assertNull(SystemComponent.getForUseCase("frontend"));
    assertNull(SystemComponent.getForUseCase(null));
    assertNull(ServerName.forName("Resource"));
    assertNull(ServerName.forName(null));
  }
}
