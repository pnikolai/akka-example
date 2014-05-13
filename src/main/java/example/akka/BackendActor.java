package example.akka;

import static example.akka.Messages.Request;
import static example.akka.Messages.Response;
import static example.akka.Messages.BackendRegistration;

import akka.actor.ActorRef;
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
    if (message instanceof Request) {
      Request request = (Request) message;
      handleRequest(getSender(), request);

    } else if (message instanceof CurrentClusterState) {
      CurrentClusterState state = (CurrentClusterState) message;
      registerTo(state.getMembers());

    } else if (message instanceof MemberUp) {
      MemberUp mUp = (MemberUp) message;
      registerTo(mUp.member());

    } else {
      unhandled(message);
    }
  }

  private void registerTo(Iterable<Member> members) {
    for (Member member : members) {
      if (member.status().equals(MemberStatus.up())) {
        registerTo(member);
      }
    }
  }

  private void registerTo(Member member) {
    if (member.hasRole("frontend")) {
      String name = member.address() + "/user/frontend";
      ActorSelection frontend = getContext().actorSelection(name);
      frontend.tell(new BackendRegistration(), getSelf());
    }
  }

  private void handleRequest(ActorRef frontend, Request request) {
    Response response = new Response(getSelf(),request.getData());
    frontend.tell(response, getSelf());
  }

}
