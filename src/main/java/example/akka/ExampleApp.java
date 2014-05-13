package example.akka;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.kernel.Bootable;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.Config;

import java.util.List;

public class ExampleApp implements Bootable {
  final Config config = ConfigFactory.load("example");
  final ActorSystem system = ActorSystem.create("example", config);

  /**
    Create top-level actors depending on node role.
   */
  public void startup() {
    List<String> roles = config.getStringList("akka.cluster.roles");

    if (roles.contains("seed")) {
      startSeedNode();

    } else if (roles.contains("frontend")) {
      startFrontendNode();

    } else if (roles.contains("backend")) {
      startBackendNode();
    }
  }

  public void shutdown() {
    system.shutdown();
  }
  
  private void startSeedNode() {
    system.actorOf(Props.create(SeedActor.class));
  }

  private void startFrontendNode() {
  }

  private void startBackendNode() {
  }

}
