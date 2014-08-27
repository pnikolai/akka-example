package example.akka;

import akka.actor.ActorRef;

import java.io.Serializable;

public interface Messages {

  public static class Request implements Serializable {
    private final String data;

    public Request(String data) {
      this.data = data;
    }

    public String getData() {
      return data;
    }

    @Override
    public String toString() {
      return String.format("Request(%s)", data);
    }
  }

  public static class Response implements Serializable {
    private final ActorRef backend;
    private final String data;

    public Response(ActorRef backend, String data) {
      this.backend = backend;
      this.data = data;
    }

    public ActorRef getBackend() {
      return backend;
    }

    public String getData() {
      return data;
    }

    @Override
    public String toString() {
      return String.format("Response(%s,%s)", data, backend.path());
    }
  }

  public static class Failure implements Serializable {
    private final String reason;
    private final Request request;

    public Failure(String reason, Request request) {
      this.reason = reason;
      this.request = request;
    }

    public String getReason() {
      return reason;
    }

    public Request getRequest() {
      return request;
    }

    @Override
    public String toString() {
      return String.format("Failure(%s, %s)", reason, request);
    }
  }

  public static class BackendRegistration implements Serializable {
    @Override
    public String toString() {
      return "BackendRegistration";
    }
  }

  public static class FrontendAvailable implements Serializable {
    private final ActorRef frontend;

    public FrontendAvailable(ActorRef frontend) {
      this.frontend = frontend;
    }

    public ActorRef getFrontend() {
      return frontend;
    }

    @Override
    public String toString() {
     return String.format("FrontendAvailable: %s", frontend) ;
    }
  }

  public static class RemoteMessage implements Serializable {
    private String message;

    public RemoteMessage(String message) {
      this.message = message;
    }

    public String getMessage() {
      return message;
    }

    @Override
    public String toString() {
     return String.format("RemoteMessage: %s", message) ;
    }
  }
}
