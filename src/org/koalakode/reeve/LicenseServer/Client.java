package org.koalakode.reeve.LicenseServer;// made by reeve
// on 9:14 PM

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	public static void main(String[] args) {


		if (args.length != 1) {
			System.out.println("Args wrong");
			return;
		}

		String ip = args[0];

		try {
			Socket socket = new Socket(ip,6565);

			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			System.out.println("connected with " + args[0]);

			out.println(args[0]);
			System.out.println(in.readLine());
			socket.close();
			System.exit(0);


		}
		catch (IOException e) {
			System.out.println("Remote server not running.");
			System.out.println("Remote server not running.");
			System.out.println("Remote server not running.");
		}
	}

}
