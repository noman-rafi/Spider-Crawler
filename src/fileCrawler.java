
import java.util.*;
import java.io.*;

public class fileCrawler {

	private WorkQueue workQ;
	static int i = 0;

	private class Worker implements Runnable {

		private WorkQueue queue;

		public Worker(WorkQueue q) {
			queue = q;
		}

		public void run() {
			String search = "test.txt";
			String name;
			while ((name = queue.remove()) != null) {
				File file = new File(name);
				String entries[] = file.list();
				if (entries == null)
					continue;
				for (String entry : entries) {
					if (entry.compareTo(".") == 0)
						continue;
					if (entry.compareTo("..") == 0)
						continue;
					search = search.toLowerCase();
					String temp = entry.toLowerCase();
					if (temp.compareTo(search) == 0){
						System.out.println("Found");
						String fn = name + "\\" + entry;
						System.out.println(fn);
					}
						
					//String fn = name + "\\" + entry;
					//System.out.println(fn);

				}
			}
		}
	}

	public fileCrawler() {
		workQ = new WorkQueue();
	}

	public Worker createWorker() {
		return new Worker(workQ);
	}


	public void processDirectory(String dir) {
		try{
			File file = new File(dir);
			if (file.isDirectory()) {
				String entries[] = file.list();
				if (entries != null)
					workQ.add(dir);

				for (String entry : entries) {
					String subdir;
					if (entry.compareTo(".") == 0)
						continue;
					if (entry.compareTo("..") == 0)
						continue;
					if (dir.endsWith("\\"))
						subdir = dir+entry;
					else
						subdir = dir+"\\"+entry;
					processDirectory(subdir);
				}
			}}catch(Exception e){}
	}

	public static void main(String Args[]) {

		fileCrawler fc = new fileCrawler();

		int N = 5;
		ArrayList<Thread> thread = new ArrayList<Thread>(N);
		for (int i = 0; i < N; i++) {
			Thread t = new Thread(fc.createWorker());
			thread.add(t);
			t.start();
		}

		fc.processDirectory("D://");

		fc.workQ.finish();

		for (int i = 0; i < N; i++){
			try {
				thread.get(i).join();
			} catch (Exception e) {};
		}
	}
}