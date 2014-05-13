package example.akka;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent.CurrentClusterState;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.Member;
import akka.cluster.MemberStatus;
import akka.event.Logging;
import akka.event.LoggingAdapter;


public class BackendActor extends UntypedActor {
  final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
  final Cluster cluster = Cluster.get(getContext().system());

  @Override
  public void preStart() {
    cluster.subscribe(getSelf(), MemberUp.class);
  }

  @Override
  public void postStop() {
    cluster.unsubscribe(getSelf());
  }

  @Override
  public void onReceive(Object message) {
    if (message instanceof Messages.Request) {
      Messages.Request request = (Messages.Request) message;
      getSender().tell(new Messages.Response(getSelf(),request.getData()),
                       getSelf());

    } else if (message instanceof CurrentClusterState) {
      CurrentClusterState state = (CurrentClusterState) message;
      for (Member member : state.getMembers()) {
        if (member.status().equals(MemberStatus.up())) {
          registerTo(member);
        }
      }

    } else if (message instanceof MemberUp) {
      MemberUp mUp = (MemberUp) message;
      registerTo(mUp.member());

    } else
      unhandled(message);
  }


  private void registerTo(Member member) {
    if (member.hasRole("frontend")) {
      String name = member.address() + "/user/frontend";
      ActorSelection frontend = getContext().actorSelection(name);
      frontend.tell(new Messages.BackendRegistration(), getSelf());
    }
  }
}
