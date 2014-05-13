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
  int counter = 0;

  @Override
  public void onReceive(Object message) {
    if ((message instanceof Messages.Request) && backends.isEmpty()) {
      Messages.Request request = (Messages.Request) message;
      getSender().tell(
          new Messages.Failure("Service unavailable", request),
          getSender());

    } else if (message instanceof Messages.Request) {
      Messages.Request request = (Messages.Request) message;
      counter++;
      backends.get(counter % backends.size()).forward(request, getContext());

    } else if (message instanceof Messages.BackendRegistration) {
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
