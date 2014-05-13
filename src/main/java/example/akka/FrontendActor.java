package example.akka;

import static example.akka.Messages.Request;
import static example.akka.Messages.Failure;
import static example.akka.Messages.BackendRegistration;

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
    if ((message instanceof Request) && backends.isEmpty()) {
      Request request = (Request) message;
      rejectRequest(getSender(), request);

    } else if (message instanceof Request) {
      Request request = (Request) message;
      handleRequest(request);

    } else if (message instanceof BackendRegistration) {
      registerBackend(getSender());

    } else if (message instanceof Terminated) {
      Terminated terminated = (Terminated) message;
      unregisterBackend(terminated.getActor());

    } else {
      unhandled(message);
    }
  }
  
  private void rejectRequest(ActorRef sender, Request request) {
    Failure failure = new Failure("Service unavailable", request);
    sender.tell(failure, getSelf());
  }

  private void handleRequest(Request request) {
    counter++;
    backends.get(counter % backends.size()).forward(request, getContext());
  }

  private void registerBackend(ActorRef backend) {
    getContext().watch(backend);
    backends.add(backend);
    log.info("Registered {} backend", backend.path());
  }

  private void unregisterBackend(ActorRef backend) {
    backends.remove(backend);
    log.info("Unregistered {} backend", backend.path());
  }

}
