package com.noahjutz;

import com.noahjutz.proto.HelloServiceGrpc;
import com.noahjutz.proto.HelloWorld;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

public class Consumer {
  static void main() throws InterruptedException {
    var channel = ManagedChannelBuilder.forAddress("localhost", 3000)
      .usePlaintext()
      .build();

    var stub = HelloServiceGrpc.newStub(channel);

    var finishLatch = new CountDownLatch(1);

    var handle = stub.sayHello(new StreamObserver<>() {
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
        System.out.println("onComplete");
        finishLatch.countDown();
      }
    });

    handle.onNext(HelloWorld.newBuilder().setHello("Hello from Consumer 1!").build());
    handle.onNext(HelloWorld.newBuilder().setHello("Hello from Consumer 2!").build());
    handle.onNext(HelloWorld.newBuilder().setHello("Hello from Consumer 3!").build());

    finishLatch.await();
  }
}
