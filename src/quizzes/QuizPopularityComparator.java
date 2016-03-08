package quizzes;

import java.util.Comparator;
import java.util.Map;

public class QuizPopularityComparator implements Comparator<String>{
	
	private Map<String, Integer> quizFrequencies;
	
	public QuizPopularityComparator(Map<String,Integer> frequencies){
		super();
		quizFrequencies = frequencies;
	}

	@Override
	public int compare(String o1, String o2) {
		return quizFrequencies.get(o2) - quizFrequencies.get(o1);
	}
	
	
}
