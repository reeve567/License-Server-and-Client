package org.koalakode.reeve.LicenseServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class Main {

	private static HashMap<String,String> licenses = new HashMap<>();

	private static Socket client = null;

	private static Calendar cal = Calendar.getInstance();
	private static SimpleDateFormat sdf = new SimpleDateFormat("MMM. dd hh:mm:ss a");

	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("Incorrect args");
			System.exit(1);
		}

		int portNumber;

		portNumber = Integer.parseInt(args[0]);

		ArrayList<String> keys = new ArrayList<>();
		ArrayList<String> values = new ArrayList<>();

		try(BufferedReader br = new BufferedReader(new FileReader("ips.yml"))) {
			for(String line; (line = br.readLine()) != null; ) {
				keys.add(line);
			}
			// line is not visible here.
		} catch (FileNotFoundException e) {
			System.out.println("No licenses");
		} catch (IOException e) {
			System.exit(1);
		}


		try(BufferedReader br = new BufferedReader(new FileReader("owners.yml"))) {
			for(String line; (line = br.readLine()) != null; ) {
				values.add(line);
			}
			// line is not visible here.
		} catch (FileNotFoundException e) {
			System.out.println("No owners");
		} catch (IOException e) {
			System.exit(1);
		}

		if (keys.size()==values.size()) {
			for (int i = 0; i < keys.size(); i++) {
				licenses.put(keys.get(i),values.get(i));
			}
		}
		else {
			System.out.println("Amount of keys/values is not equal!");
		}

		if (licenses.isEmpty()) {
			System.out.println("No ips detected!");
			System.exit(1);
		}

		cal = Calendar.getInstance();
		System.out.println();
		System.out.println("DRM Server started with " + licenses.size() + " IPs available.");
		System.out.println();
		System.out.println("Current port: " + portNumber);
		System.out.println();
		System.out.println(sdf.format(cal.getTime()));
		System.out.println();

		connect(portNumber);

	}

	private static void connect(int portNumber) {

		try (
				    ServerSocket serverSocket = new ServerSocket(portNumber);
				    Socket clientSocket = serverSocket.accept();
				    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		) {
			client = clientSocket;
			cal = Calendar.getInstance();
			System.out.println("----------------------------------------------------");
			System.out.println();
			System.out.println("Connect from " + client.getRemoteSocketAddress());
			System.out.println("");
			System.out.println();
			System.out.println(sdf.format(cal.getTime()));
			System.out.println();
			String license = client.getInetAddress().toString();
			if (licenses.containsKey(license)) {
				System.out.println("Successful load from " + licenses.get(license));
				out.println(true);
			} else {
				System.out.println("Unsuccessful load -- invalid IP.");
				out.println(false);
			}

		} catch (IOException ignored) {}
		if (client != null) {
			System.out.println();
			System.out.println("Disconnect from " + client.getRemoteSocketAddress());
			System.out.println();
			System.out.println();
		}
		connect(portNumber);
	}


}
