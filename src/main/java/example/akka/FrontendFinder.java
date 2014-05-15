package example.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.Member;
import akka.cluster.MemberStatus;
import akka.dispatch.OnSuccess;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.util.Timeout;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author npetrov
 */
public class FrontendFinder extends UntypedActor {
  private final static Timeout timeout = new Timeout(5, TimeUnit.SECONDS);
  final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
  final Cluster cluster = Cluster.get(getContext().system());
  final ActorRef backend;
  
  public FrontendFinder(ActorRef backend) {
    this.backend = backend;
  }
 
  @Override
  public void preStart() {
    cluster.subscribe(getSelf(), ClusterEvent.MemberUp.class);
  }

  @Override
  public void postStop() {
    cluster.unsubscribe(getSelf());
  }

  @Override
  public void onReceive(Object message) {
    if (message instanceof ClusterEvent.CurrentClusterState) {
      ClusterEvent.CurrentClusterState state = (ClusterEvent.CurrentClusterState) message;
      notifyBackend(state.getMembers());
    } else if (message instanceof ClusterEvent.MemberUp) {
      ClusterEvent.MemberUp mUp = (ClusterEvent.MemberUp) message;
      notifyBackend(mUp.member());

    } else {
      unhandled(message);
    }
  }

  private void notifyBackend(Iterable<Member> members) {
    for (Member member : members) {
      if (member.status().equals(MemberStatus.up())) {
        notifyBackend(member);
      }
    }
  }

  private void notifyBackend(Member member) {
    if (member.hasRole("frontend")) {
      String name = member.address() + "/user/frontend";
      ActorSelection frontend = getContext().actorSelection(name);
      frontend.resolveOne(timeout).onSuccess(new OnSuccess<ActorRef>() {
        @Override
        public void onSuccess(ActorRef result) {
          backend.tell(new Messages.FrontendAvailable(result), getSelf());
        }
      }, getContext().dispatcher());     
    }
  }
 
}
