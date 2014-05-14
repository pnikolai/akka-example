package example.akka;

import static org.junit.Assert.assertEquals;
import static example.akka.Messages.BackendRegistration;
import static example.akka.Messages.Request;
import static example.akka.Messages.Failure;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import akka.actor.ActorSystem;
import akka.actor.Props;

import akka.testkit.TestActorRef;
import akka.testkit.JavaTestKit;


public class FrontendActorTest {
  static ActorSystem system;

  @BeforeClass
  public static void setupSystem() {
    system = ActorSystem.create();
  }

  @AfterClass
  public static void teardownSystem() {
    JavaTestKit.shutdownActorSystem(system);
  }

  TestActorRef<FrontendActor> frontend;
  FrontendActor actor;

  @Before
  public void setup() {
    frontend = TestActorRef.create(system, Props.create(FrontendActor.class));
    actor = frontend.underlyingActor();
  }

  @Test
  public void testBackendRegistration() {
    new JavaTestKit(system) {{
      assertEquals(actor.backends.size(), 0);

      frontend.tell(new BackendRegistration(), getTestActor());

      assertEquals(actor.backends.size(), 1);
      assertEquals(actor.backends.get(0), getTestActor());
    }};
  }

  @Test
  public void testNoBackends() {
    new JavaTestKit(system) {{
      assertEquals(actor.backends.size(), 0);

      frontend.tell(new Request("Test"), getTestActor());

      Failure failure = expectMsgClass(Failure.class);
      assertEquals(failure.getRequest().getData(), "Test");
    }};
  }

}
