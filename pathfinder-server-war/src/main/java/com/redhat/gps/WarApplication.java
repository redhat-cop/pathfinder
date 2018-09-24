package com.redhat.gps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.redhat.gps.pathfinder.PathfinderApp;

@SpringBootApplication
public class WarApplication{
	public static void main(String[] args) {
		SpringApplication.run(PathfinderApp.class, args);
	}
}
