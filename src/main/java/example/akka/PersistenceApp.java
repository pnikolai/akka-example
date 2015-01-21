package example.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.kernel.Bootable;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import example.akka.persistence.Color;
import example.akka.persistence.commands.CreateForm;
import example.akka.persistence.commands.DeleteForm;
import example.akka.persistence.commands.MakeSnapshot;
import example.akka.persistence.commands.Show;
import example.akka.persistence.commands.ThrowException;
import example.akka.persistence.forms.Circle;
import example.akka.persistence.forms.Rectangle;
import example.akka.persistence.forms.Triangle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PersistenceApp implements Bootable{

  ActorSystem system;

  @Override
  public void startup() {

  Config config = ConfigFactory.load("persistence");
  system = ActorSystem.create("persistence", config);
  final ActorRef persistenceActor =
            system.actorOf(Props.create(PersistenceActor.class), "persistenceActor");

    persistenceActor.tell(new CreateForm(new Circle(1, Color.RED)), null);
    persistenceActor.tell(new CreateForm(new Rectangle(2, Color.GREEN)), null);
    persistenceActor.tell(new MakeSnapshot(), null);
    persistenceActor.tell(new CreateForm(new Triangle(3, Color.BLUE)), null);
    persistenceActor.tell(new Show(), null);
    persistenceActor.tell(new ThrowException(), null);
    persistenceActor.tell(new Show(), null);
    persistenceActor.tell(new DeleteForm(2), null);
    persistenceActor.tell(new Show(), null);
    persistenceActor.tell(new ThrowException(), null);
    persistenceActor.tell(new Show(), null);


    try {
      Thread.sleep(3000);
    } catch (InterruptedException ex) {
      Logger.getLogger(PersistenceApp.class.getName()).log(Level.SEVERE, null, ex);
    }
    system.shutdown();

  }

  @Override
  public void shutdown() {
    system.shutdown();
  }

}
