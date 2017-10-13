package local.search;
import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jdk.nashorn.internal.ir.RuntimeNode.Request;

/**
 * Servlet implementation class MainServlet
 */
@WebServlet("/MainServlet")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String qry = request.getParameter("first");
		System.out.println(qry);
		MainServlet listFilesUtil = new MainServlet();
		final String directoryLinuxMac = "C://Users/vsrkreddy/Downloads/docsnew";
		listFilesUtil.listFiles(directoryLinuxMac);
		// System.out.println(indexes.keySet());
//		for (String key : indexes.keySet()) {
//			System.out.println(key);
//			System.out.println(indexes.get(key));
//		}
//		for(String key : weights.keySet()){
//			System.out.println(key);
//			System.out.println(weights.get(key));
//		}
		// System.out.println(indexes.values());
		//System.out.println("Enter query ");
        //Scanner scan = new Scanner(System.in);
        //String input = scan.nextLine();
		Map docRanks = new HashMap();
		List finalResults = new ArrayList<>();
		docRanks.putAll(takeInput(qry));
		for(Object e : (docRanks).keySet()){
			finalResults.add(docRanks.get(e));
		}
		HttpSession session = request.getSession();
		session.setAttribute("finalResults", finalResults);
		RequestDispatcher rd = request.getRequestDispatcher("display.jsp");
		rd.forward(request, response);
	}
	public static final Map<Integer, String> fileSeq = new TreeMap<Integer, String>();
	public static Map<Integer, String> newQueryProperties =  new TreeMap<Integer, String>();
	public static Map<String, String> contentsMap = new HashMap<>();
	public static Map<String, HashMap<String, Integer>> indexes = new HashMap<String, HashMap<String, Integer>>();
	public static Map<String, Double> idfMap = new HashMap<String, Double>();
	public static Map<String, HashMap<String, Double>> weights = new HashMap<String, HashMap<String, Double>>();
	static final String[] set = { "a", "about", "above", "above", "across",
			"after", "afterwards", "again", "against", "all", "almost",
			"alone", "along", "already", "also", "although", "always",
			"am", "among", "amongst", "amoungst", "amount", "an", "and",
			"another", "any", "anyhow", "anyone", "anything", "anyway",
			"anywhere", "are", "around", "as", "at", "back", "be",
			"became", "because", "become", "becomes", "becoming", "been",
			"before", "beforehand", "behind", "being", "below", "beside",
			"besides", "between", "beyond", "bill", "both", "bottom",
			"but", "by", "call", "can", "cannot", "cant", "co", "con",
			"could", "couldnt", "cry", "de", "describe", "detail", "do",
			"done", "down", "due", "during", "each", "eg", "eight",
			"either", "eleven", "else", "elsewhere", "empty", "enough",
			"etc", "even", "ever", "every", "everyone", "everything",
			"everywhere", "except", "few", "fifteen", "fify", "fill",
			"find", "fire", "first", "five", "for", "former", "formerly",
			"forty", "found", "four", "from", "front", "full", "further",
			"get", "give", "go", "had", "has", "hasnt", "have", "he",
			"hence", "her", "here", "hereafter", "hereby", "herein",
			"hereupon", "hers", "herself", "him", "himself", "his", "how",
			"however", "hundred", "ie", "if", "in", "inc", "indeed",
			"interest", "into", "is", "it", "its", "itself", "keep",
			"last", "latter", "latterly", "least", "less", "ltd", "made",
			"many", "may", "me", "meanwhile", "might", "mill", "mine",
			"more", "moreover", "most", "mostly", "move", "much", "must",
			"my", "myself", "name", "namely", "neither", "never",
			"nevertheless", "next", "nine", "no", "nobody", "none",
			"noone", "nor", "not", "nothing", "now", "nowhere", "of",
			"off", "often", "on", "once", "one", "only", "onto", "or",
			"other", "others", "otherwise", "our", "ours", "ourselves",
			"out", "over", "own", "part", "per", "perhaps", "please",
			"put", "rather", "re", "same", "see", "seem", "seemed",
			"seeming", "seems", "serious", "several", "she", "should",
			"show", "side", "since", "sincere", "six", "sixty", "so",
			"some", "somehow", "someone", "something", "sometime",
			"sometimes", "somewhere", "still", "such", "system", "take",
			"ten", "than", "that", "the", "their", "them", "themselves",
			"then", "thence", "there", "thereafter", "thereby",
			"therefore", "therein", "thereupon", "these", "they", "thickv",
			"thin", "third", "this", "those", "though", "three", "through",
			"throughout", "thru", "thus", "to", "together", "too", "top",
			"toward", "towards", "twelve", "twenty", "two", "un", "under",
			"until", "up", "upon", "us", "very", "via", "was", "we",
			"well", "were", "what", "whatever", "when", "whence",
			"whenever", "where", "whereafter", "whereas", "whereby",
			"wherein", "whereupon", "wherever", "whether", "which",
			"while", "whither", "who", "whoever", "whole", "whom", "whose",
			"why", "will", "with", "within", "without", "would", "yet",
			"you", "your", "yours", "yourself", "yourselves", "the" };
	public void listFiles(String directoryName) {
		File directory = new File(directoryName);
		HashMap<String, List<String>> filesMap = new HashMap<>();
		
		// get all the files from a directory
		File[] fList = directory.listFiles();
		// int seq=1;
		for (File file : fList) {
			if (file.isFile()) {
				try {
					String content = readFile(directoryName + "/"
							+ file.getName());
					contentsMap.put(file.getName(), content);
					List<String> wordsList = new ArrayList<String>();
					Pattern pattern = Pattern.compile("\\w+");
					Matcher matcher = pattern.matcher(content);
					while (matcher.find()) {
						wordsList.add(matcher.group());
					}
					// fileSeq.put(seq, file.getName());
					filesMap.put(file.getName(), wordsList);
				} catch (FileNotFoundException ex) {
					System.out.println("Unable to open file '" + file.getName()
							+ "'");
					ex.printStackTrace();
				} catch (IOException ex) {
					System.out.println("Error reading file '" + file.getName()
							+ "'");
				}
			}
		}
		for (File file : fList) {
			List<String> words = filesMap.get(file.getName());
			words.removeAll(Arrays.asList(set));
			filesMap.put(file.getName(), words);
		}
		for (File file : fList) {
			List<String> words1 = filesMap.get(file.getName());
			List<String> stemmedwords = completeStem(words1);
			filesMap.put(file.getName(), stemmedwords);
		}
		int seq = 1;
		for (String name : filesMap.keySet()) {
			fileSeq.put(seq, name);
			seq++;
		}
		makeIndexes(filesMap);
		makeIDFs();
	}

	public static ArrayList<String> completeStem(List<String> tokens1) {
		PorterAlgo pa = new PorterAlgo();
		ArrayList<String> arrstr = new ArrayList<String>();
		for (String i : tokens1) {
			String s1 = pa.step1(i);
			String s2 = pa.step2(s1);
			String s3 = pa.step3(s2);
			String s4 = pa.step4(s3);
//			String s5 = pa.step5(s4);
			arrstr.add(s4);
		}
		return arrstr;
	}

	private String readFile(String file) throws IOException {
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

	public void makeIndexes(HashMap<String, List<String>> filesMap) {
		Collection<List<String>> wordLists = filesMap.values();
		/*for (List<String> list : wordLists) {
			System.out.println(list);
		}*/
		for (String name : filesMap.keySet()) {
			List<String> eachList = filesMap.get(name);
			SortedSet<String> eachSet = new TreeSet<String>(eachList);
			for (String term : eachSet) {
				int termFreq = Collections.frequency(eachList, term);
				addOrUpdateIndex(term, termFreq, name);
			}
		}
	}

	public void addOrUpdateIndex(String term, int freq, String doc) {
		if (!(indexes.keySet().contains(term))) {
			HashMap<String, Integer> posting = new HashMap<String, Integer>();
			posting.put(doc, freq);
			indexes.put(term, posting);
		} else {
			(indexes.get(term)).put(doc, freq);
		}
	}

	public void makeIDFs() {
		for (String eachTerm : indexes.keySet()) {
			int df = indexes.get(eachTerm).size();
			//System.out.println(eachTerm+"  "+df);
			double idf = Math.log10(91/ (double)df);
			//System.out.println(eachTerm+"  "+idf);
			idfMap.put(eachTerm, idf);
		}
		findWeights();
	}

	public void findWeights() {
		for (String term : indexes.keySet()) {
			Map<String, Integer> freqMap = indexes.get(term);
			for (String docID : freqMap.keySet()) {
				double weight = ((int)freqMap.get(docID)) *((double)idfMap.get(term));
				addOrUpdateWeight(term,weight, docID);
			}
		}
	}
	
	public void addOrUpdateWeight(String term, double weight, String doc) {
		if (!(weights.keySet().contains(term))) {
			HashMap<String, Double> posting = new HashMap<String, Double>();
			posting.put(doc, weight);
			weights.put(term, posting);
		} else {
			(weights.get(term)).put(doc, weight);
		}
	}

	
	 public static Map takeInput(String query) {
	        query = query.toLowerCase();
	        List<String> wordsList = new ArrayList<>();
	        Pattern pattern = Pattern.compile("\\w+");
	        Matcher matcher = pattern.matcher(query);
	        while (matcher.find()) {
	            wordsList.add(matcher.group());
	        }
	        wordsList.removeAll(Arrays.asList(set));
	        List<String> stemmedQuery = completeStem(wordsList);
//	        System.out.println(stemmedQuery);
	        Map docRanks = new HashMap();
	        docRanks.putAll(processQuery(stemmedQuery));
	        return docRanks;
	    }
	    
	    public static Map processQuery(List<String> stemWords ) {
	    	Set<String> querySet = new HashSet<>(stemWords);
	    	HashMap<String, Integer> queryFreq = new HashMap<String, Integer>();
	    	for(String each : querySet){
	    		int freq = Collections.frequency(stemWords, each);
	    		queryFreq.put(each, freq);
	    	}
	    	//System.out.println(indexes);
	    	//System.out.println(idfMap);
	    	//System.out.println(weights);
	    	Map<String, Double> queryWeights = new HashMap<>();
//	    	System.out.println(weights);
	    	for(String term : weights.keySet()){
//	    		System.out.println("*****Outside if stmt*******");
	    		if(queryFreq.keySet().contains(term)){
//	    			System.out.println("****It came in********");
	    			Double idf = idfMap.get(term);
	    			Double qWeight = queryFreq.get(term)*idf;
	    			queryWeights.put(term, qWeight);
	    		}
	    	}
	    	Map<String, HashMap<String, Double>> matchMap = new HashMap<String, HashMap<String,Double>>();
	    	for(String each : querySet){
	    		if(weights.keySet().contains(each.toString())){
	    			matchMap.put(each, weights.get(each));
	    		}
	    	}
	    	Set<String> keys = matchMap.keySet();
	    	Set<String> docs = new HashSet<>();
	    	for(String each : matchMap.keySet()){
	    		for(String eachDoc : matchMap.get(each).keySet()){
	    			boolean flag = true;
	    			for(String key : keys){
	    				if(!(matchMap.get(key).keySet().contains(eachDoc))){
	    					flag = false;
	    				}
	    			}
	    			if(flag){
	    				docs.add(eachDoc);
	    			}
	    		}
//	    		break;
	    	}
//	    	System.out.println("**********  Match Map*********");
//	    	System.out.println(matchMap);
	    	HashMap<String, Double> docDenoms = new HashMap<>();
	    	for(String eachDoc : docs) {
	    		double docVectNum = 0;
	    		for(String keyTerm : weights.keySet()){
	    			for(String docName : weights.get(keyTerm).keySet()){
	    				if(docName.equals(eachDoc)){
	    					docVectNum = docVectNum + Math.pow(weights.get(keyTerm).get(docName), 2);
	    					break;
	    				}
	    			}
	    		}
	    		docDenoms.put(eachDoc, docVectNum);
	    	}
	    	
	    	double qVectdenom1 = 0;
	    	for(String key : queryWeights.keySet()){
	    		qVectdenom1 += Math.pow(queryWeights.get(key), 2);
	    	}
	    	double qvectDenom = Math.sqrt(qVectdenom1);
//	    	System.out.println(queryFreq);
//	    	System.out.println(queryWeights);
	    	HashMap<String, Double> Numerators = new HashMap<>();
	    	for(String eachDoc : docs){
	    		double numerator = 0;
	    		for(String term : matchMap.keySet()){
	    			numerator += matchMap.get(term).get(eachDoc)*queryWeights.get(term);
	    		}
	    		Numerators.put(eachDoc, numerator);    		
	    	}
	    	HashMap<String, Double> docCosines = new HashMap<>();
	    	for(String each : docs){
	    		double cosine = Numerators.get(each)/docDenoms.get(each)*qvectDenom;
	    		docCosines.put(each, cosine);
	        }
	    	
	    	Map<String, Double> distMap = new HashMap<>();
	    	/**
		     * calculating term proximity
		     * */
	    	for(String file: docs){
	    		int dist = TermProx.findDist(new LinkedList(querySet), contentsMap.get(file));
	    		distMap.put(file, dist/10.0);
	    	}
	    	Map<String, Double> finalFactors = new HashMap<>();
	    	for(String file: docs){
	    		double rankFactor = 0.6*docCosines.get(file)+0.4*distMap.get(file);
	    		finalFactors.put(file, rankFactor);
	    	}
	    	
	    	TreeSet<Double> keySet = new TreeSet<>(Collections.reverseOrder());
	    	keySet.addAll(finalFactors.values());
	    	HashMap<Integer, String> docRanks = new HashMap<>();
	    	int rank=1;
	    	for(Double key : keySet){
	    		for(String doc : finalFactors.keySet()){
	    			if(key==finalFactors.get(doc)){
	    				docRanks.put(rank, doc);
	    				rank++;
	    			}
	    		}
//	    		
	    
	    		
	    	}
// ranks are in reverse order
	    	System.out.println(docRanks);
	    	return docRanks;
	    }

}
