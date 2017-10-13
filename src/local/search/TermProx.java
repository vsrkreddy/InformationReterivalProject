package local.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class TermProx {

	public static final Map<String, Integer> docDistances = new HashMap<>();

	private static String readFile(String file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");

		try {
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}

			String content1 = stringBuilder.toString();
			if (content1.contains("<script>")) {
				content1 = content1.replaceAll("(<script>)[^&]*.?(</script>)",
						"");
			}
			content1 = content1.replaceAll("\\<.*?\\>", "");
			content1 = content1.toLowerCase();
			return content1;
		} finally {
			reader.close();
		}
	}

	public static void main(String[] args) {
		TermProx testObj = new TermProx();
		// Assume this list has stemmed words of query
		// I tested it by adding few static words in my main method.
		List<String> query = new ArrayList<String>();
		String directoryPath = "C://Users/Sasanka/Documents/Files/test";
		File directory = new File(directoryPath);
		File[] fList = directory.listFiles();
		/**
		 * assume these set "docs" will be having the doc names only those which
		 * contain all query terms
		 **/
		Set<String> docs = new HashSet<>();
		docs.add("test1.html");
		docs.add("test.html");
		for (File file : fList) {
			if (file.isFile() && docs.contains(file.getName())) {
				try {
					String content = readFile(directoryPath + "/"
							+ file.getName());
					int distance = testObj.findDist(query, content);
					docDistances.put(file.getName(), distance);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		// for(double val : keys){
		// System.out.print(val+" ,");
		// }
		// String big = "devdepdeepFuckassholedhadeldhadel";
		// String small = "de";
		// int index = big.indexOf(small);
		// do{
		// System.out.println(index);
		// index = big.indexOf(small, index+small.length());
		// }while(index>=0);

	}

	/**
	 * a method to take one file content as a string and list of query words
	 **/
	public static int findDist(List<String> query, String file) {

		ListIterator<String> listItr = query.listIterator();
		//System.out.println(query);
		/**
		 * create a hashmap to store terms and their order of occurring indexes.
		 * First key of inner map is order of occurrence and its value is the
		 * index of occurrence
		 **/
		Map<String, Map<Integer, Integer>> termSpots = new HashMap<>();
		/**
		 * for each term in query, prepare a map with order of occurrence and
		 * the index of occurrence.
		 **/
		while (listItr.hasNext()) {
			String presTerm = listItr.next();
			int index = file.indexOf(presTerm);
			Map<Integer, Integer> indices = new HashMap<Integer, Integer>();
			int count = 0;
			while (index >= 0) {
				index = file.indexOf(presTerm, index + presTerm.length());
				indices.put(count++, index);
			}
			termSpots.put(presTerm, indices);
		}
		//System.out.println(termSpots);
		/**
		 * make a treeset and put sizes of inner maps in it so that we get the
		 * smallest size without sorting
		 **/
		TreeSet<Integer> sizes = new TreeSet<>();
		for (String each : query) {
			sizes.add(termSpots.get(each).size());
		}
		/**
		 * getting first item of the set which would be the smallest size
		 */
		Iterator<Integer> itr1 = sizes.iterator();
		int small = itr1.next();
		int smallDist = 0;
		/***
		 * for each occurrence order in the inner maps, we find the distances
		 * i.e, we find distance between terms of all first occurrences, all
		 * second occurrences and so on. finally we return the smallest of all
		 * the distances.
		 * */
		for (int i = 0; i < small; i++) {
			int dist = 0;
			int prevDist = 0;
			ListIterator<String> qItr = query.listIterator();
			String word = qItr.next();
			System.out.println(word);
			System.out.println(termSpots.get(word));
			int prev = (termSpots.get(word)).get(i);
			//System.out.println("prev = "+ prev);
			while (qItr.hasNext()) {
				int curr = termSpots.get(qItr.next()).get(i);
				System.out.println("curr = "+curr);
				dist = dist + Math.abs(curr - prev);
				prev = curr;
			}
			if (prevDist == 0 || dist < prevDist) {
				prevDist = dist;
				smallDist = dist;
			}
		}
		return smallDist;
	}

}


