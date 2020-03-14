package com.bonc.test.akka;

import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class AkkaSimple {
	public static void main(String[] args) {
		ActorSystem as=ActorSystem.create("lkk",ConfigFactory.load("akka.conf"));
		ActorRef ar=as.actorOf(Props.create(HelloWorld.class), "helloworld");
		System.out.println("HelloWorld actor path: "+ar.path());
		ActorRef watcher=as.actorOf(Props.create(Watcher.class, ar), "watcher");
		System.out.println("watcher actor path: "+watcher.path());
		
	}
}
