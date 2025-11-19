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
  public void sayHello(HelloWorld request, StreamObserver<HelloWorld> responseObserver) {
    System.out.println(request);
    responseObserver.onNext(HelloWorld.newBuilder().setHello("Hello from server!").build());
    responseObserver.onNext(HelloWorld.newBuilder().setHello("Hello from server 2!").build());
    responseObserver.onNext(HelloWorld.newBuilder().setHello("Hello from server 3!").build());
    responseObserver.onCompleted();
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
