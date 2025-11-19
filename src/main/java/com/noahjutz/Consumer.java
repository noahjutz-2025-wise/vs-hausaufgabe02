package com.noahjutz;

import com.noahjutz.proto.HelloServiceGrpc;
import com.noahjutz.proto.HelloWorld;
import io.grpc.ManagedChannelBuilder;

public class Consumer {
  static void main() {
    var channel = ManagedChannelBuilder.forAddress("localhost", 3000)
      .usePlaintext()
      .build();

    var stub = HelloServiceGrpc.newBlockingStub(channel);

    var response = stub.sayHello(HelloWorld.newBuilder().setHello("Hiiii :3").build());
    //response.forEachRemaining(System.out::println);
  }
}
