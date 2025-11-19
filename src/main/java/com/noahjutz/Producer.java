package com.noahjutz;

import com.google.protobuf.Empty;
import com.noahjutz.proto.HelloServiceGrpc;
import com.noahjutz.proto.HelloWorld;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

class Service extends HelloServiceGrpc.HelloServiceImplBase {
  @Override
  public StreamObserver<HelloWorld> sayHello(StreamObserver<HelloWorld> responseObserver) {
    responseObserver.onNext(HelloWorld.newBuilder().setHello("Hello from server!").build());
    responseObserver.onNext(HelloWorld.newBuilder().setHello("Hello from server 2!").build());
    responseObserver.onNext(HelloWorld.newBuilder().setHello("Hello from server 3!").build());
    //responseObserver.onCompleted();
    return new StreamObserver<>() {
      @Override
      public void onNext(HelloWorld helloWorld) {
        System.out.println(helloWorld);
      }

      @Override
      public void onError(Throwable throwable) {
        throwable.printStackTrace();
      }

      @Override
      public void onCompleted() {
        System.out.println("onCompleted");
      }
    };
  }
}

public class Producer {
  static void main() throws IOException, InterruptedException {
    var server = ServerBuilder.forPort(3000)
      .addService(ProtoReflectionService.newInstance())
      .addService(new Service())
      .build();

    server.start();
    Runtime.getRuntime().addShutdownHook(new Thread(server::shutdownNow));

    server.awaitTermination();
  }
}
