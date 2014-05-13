package example.akka;

import static akka.pattern.Patterns.ask;
import static example.akka.Messages.Request;

import akka.actor.ActorRef;
import akka.dispatch.OnSuccess;
import akka.util.Timeout;

import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class WorkSimulator implements Runnable {
  final Timeout timeout = new Timeout(Duration.create(5, TimeUnit.SECONDS));

  final AtomicInteger counter = new AtomicInteger();

  final ActorRef frontend;
  final ExecutionContext ec;

  public WorkSimulator(ActorRef frontend, ExecutionContext ec) {
    this.frontend = frontend;
    this.ec = ec;
  }

  public void run() {
    Request request = new Request("request-" + counter.incrementAndGet());

    Future<Object> future = ask(frontend, request, timeout);

    future.onSuccess(new OnSuccess<Object>() {
        public void onSuccess(Object result) { System.out.println(result); }
      }, ec);
  }
}
