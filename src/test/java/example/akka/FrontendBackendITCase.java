/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package example.akka;

import akka.actor.ActorIdentity;
import static example.akka.Messages.Failure;
import static example.akka.Messages.Request;
import static example.akka.Messages.Response;
import static example.akka.Messages.FrontendAvailable;
import static org.junit.Assert.assertEquals;
import static akka.testkit.JavaTestKit.duration;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Identify;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import scala.concurrent.duration.Duration;

/**
 *
 * @author npetrov
 */
public class FrontendBackendITCase {
  
  ActorSystem system;
  ActorRef frontend;
  ActorRef backend;
  JavaTestKit testKit;
  
  @Before
  public void setup() {
    system = ActorSystem.create();
    testKit = new JavaTestKit(system);
    frontend = system.actorOf(Props.create(FrontendActor.class));
    backend = system.actorOf(Props.create(BackendActor.class));
  }
  
  @After
  public void cleanup() {
    testKit.shutdownActorSystem(system, true);
  }
  
  @Test
  public void testBackendRegistration() {
    frontend.tell(new Request("Test Request"), testKit.getTestActor());
    Failure fail = testKit.expectMsgClass(Failure.class);
    assertEquals(fail.getRequest().getData(), "Test Request");
    
    backend.tell(new FrontendAvailable(frontend), testKit.getRef());
    backend.tell(new Identify(this), testKit.getRef());
    testKit.expectMsgClass(duration("1 second"), ActorIdentity.class);

    frontend.tell(new Request("Test Request"), testKit.getTestActor());
    Response res = testKit.expectMsgClass(Duration.create(10, TimeUnit.SECONDS), Response.class);
    assertEquals(res.getData(), "Test Request");
  }
}
