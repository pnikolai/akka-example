package example.akka;

import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.ArrayList;
import java.util.List;


public class FrontendActor extends UntypedActor {
  final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

  List<ActorRef> backends = new ArrayList<ActorRef>();

  @Override
  public void onReceive(Object message) {
    if (message instanceof Messages.BackendRegistration) {
      ActorRef backend = getSender();
      getContext().watch(backend);
      backends.add(backend);
      log.info("Registered {} backend", backend.path());

    } else if (message instanceof Terminated) {
      Terminated terminated = (Terminated) message;
      ActorRef backend = terminated.getActor();
      backends.remove(backend);
      log.info("Unregistered {} backend", backend.path());

    } else {
      unhandled(message);
    }
  }
}
