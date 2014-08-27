package example.akka;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 *
 * @author npetrov
 */
public class RemoteActor extends UntypedActor {

  final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

  @Override
  public void onReceive(Object message) {
    if (message instanceof Messages.RemoteMessage) {
      onRemoteMessage((Messages.RemoteMessage) message);
    } else {
      unhandled(message);
    }
  }

  private void onRemoteMessage(Messages.RemoteMessage message) {
    log.info("Remote received: {} from {}", message.getMessage(), getSender().path());
    getSender().tell(new Messages.RemoteMessage("Response to " + message.getMessage()), self());
  }
}
