package example.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.kernel.Bootable;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.Config;

import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;
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
      ActorRef frontend = startFrontendNode();
      simulateWork(frontend);

    } else if (roles.contains("backend")) {
      startBackendNode();
    }
  }

  public void shutdown() {
    system.shutdown();
  }
  
  private ActorRef startSeedNode() {
    return system.actorOf(Props.create(SeedActor.class));
  }

  private ActorRef startFrontendNode() {
    return system.actorOf(Props.create(FrontendActor.class), "frontend");
  }

  private ActorRef startBackendNode() {
    return system.actorOf(Props.create(BackendActor.class));
  }

  private void simulateWork(ActorRef frontend) {
    ExecutionContext ec = system.dispatcher();
    FiniteDuration interval = Duration.create(2, TimeUnit.SECONDS);

    system.scheduler()
      .schedule(interval, interval, new WorkSimulator(frontend, ec), ec);
  }

}
