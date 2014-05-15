package example.akka;

import static example.akka.Messages.FrontendAvailable;
import static example.akka.Messages.BackendRegistration;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import akka.testkit.TestActorRef;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author npetrov
 */
public class BackendActorTest {
  static ActorSystem system;

  @BeforeClass
  public static void setupSystem() {
    system = ActorSystem.create();
  }

  @AfterClass
  public static void teardownSystem() {
    JavaTestKit.shutdownActorSystem(system);
  }
  
  TestActorRef<BackendActor> backend;
  BackendActor actor;

  @Before
  public void setup() {
    backend = TestActorRef.create(system, Props.create(BackendActor.class));
    actor = backend.underlyingActor();
  }
  
  @Test
  public void testFrontendNotification() {
    new JavaTestKit(system) {{

     backend.tell(new FrontendAvailable(getTestActor()), getTestActor());
     expectMsgClass(BackendRegistration.class);      
    }};
  }
  
}
