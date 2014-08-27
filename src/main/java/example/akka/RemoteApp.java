package example.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.kernel.Bootable;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 *
 * @author npetrov
 */
public class RemoteApp implements Bootable {

  Config config;
  ActorSystem system;
  ActorRef remoteActor;

  @Override
  public void startup() {
    config = ConfigFactory.load("remote");
    system = ActorSystem.create("Remote", config);
    remoteActor = startRemoteActor();
    System.out.println("RemoteActor: " + remoteActor.path());
  }

  @Override
  public void shutdown() {
    system.shutdown();
  }

  private ActorRef startRemoteActor() {
    return system.actorOf(Props.create(RemoteActor.class), "RemoteActor");
  }

}
