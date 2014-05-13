package example.akka;

import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.Member;
import akka.cluster.ClusterEvent.MemberEvent;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.ClusterEvent.MemberRemoved;
import akka.cluster.ClusterEvent.UnreachableMember;
import akka.event.Logging;
import akka.event.LoggingAdapter;


public class SeedActor extends UntypedActor {
  final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
  final Cluster cluster = Cluster.get(getContext().system());

  @Override
  public void preStart() {
    cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(),
                      MemberEvent.class, UnreachableMember.class);
  }

  @Override
  public void postStop() {
    cluster.unsubscribe(getSelf());
  }

  @Override
  public void onReceive(Object message) {
    if (message instanceof MemberUp) {
      MemberUp mUp = (MemberUp) message;
      onMemberUp(mUp.member());

    } else if (message instanceof UnreachableMember) {
      UnreachableMember mUnreachable = (UnreachableMember) message;
      onMemberUnreachable(mUnreachable.member());

    } else if (message instanceof MemberRemoved) {
      MemberRemoved mRemoved = (MemberRemoved) message;
      onMemberRemoved(mRemoved.member());

    } else if (message instanceof MemberEvent) {
      // ignore
    } else
      unhandled(message);
  }

  private void onMemberUp(Member member) {
    log.info("Member is Up: {} {}", member, member.getRoles());
  }

  private void onMemberUnreachable(Member member) {
    log.info("Member detected as unreachable: {}", member);
  }

  private void onMemberRemoved(Member member) {
    log.info("Member is Removed: {}", member);
  }
}
