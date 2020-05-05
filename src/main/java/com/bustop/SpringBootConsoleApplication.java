package com.bustop;

import com.bustop.service.TimeTableService;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import static java.lang.System.*;

@SpringBootApplication
public class SpringBootConsoleApplication implements CommandLineRunner {

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private TimeTableService timeService;

	public static void main(String[]args) throws Exception {

		SpringApplication app = new SpringApplication(SpringBootConsoleApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);

	}

	@Override
	public void run(String... args) throws Exception {

		while (true) {
			Scanner scanner = new Scanner(in);

			out.println("Available commands: ");
			out.println(" 1: 'exit' ");
			out.println(" 2: 'parse input file'");
			out.println();
			out.println("Enter the number of your command: ");

			String command = scanner.next();
			if ("1".equals(command)) {
				exit(0);
			} else if ("2".equals(command)) {
				Resource res = resourceLoader.getResource("classpath:inputFile.txt");

				timeService.generateTimeTable(res);

			}



		}

	}
}
