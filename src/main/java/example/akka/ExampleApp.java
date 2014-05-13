package example.akka;

import akka.actor.ActorSystem;
import akka.kernel.Bootable;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.Config;


public class ExampleApp implements Bootable {
  final Config config = ConfigFactory.load("example");
  final ActorSystem system = ActorSystem.create("example", config);

  public void startup() {
    //TODO: create top-level actors
  }

  public void shutdown() {
    system.shutdown();
  }

}
