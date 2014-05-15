package example.akka;

import static example.akka.Messages.Request;
import static example.akka.Messages.Response;
import static example.akka.Messages.BackendRegistration;
import static example.akka.Messages.FrontendAvailable;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class BackendActor extends UntypedActor {
  final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

  @Override
  public void onReceive(Object message) {
    if (message instanceof Request) {
      Request request = (Request) message;
      handleRequest(getSender(), request);

    } else if (message instanceof FrontendAvailable) {
      registerTo((FrontendAvailable) message);
    } else {
      unhandled(message);
    }
  }

  private void registerTo(FrontendAvailable message) {   
    message.getFrontend().tell(new BackendRegistration(), getSelf());   
  }

  private void handleRequest(ActorRef frontend, Request request) {
    Response response = new Response(getSelf(),request.getData());
    frontend.tell(response, getSelf());
  }

}
