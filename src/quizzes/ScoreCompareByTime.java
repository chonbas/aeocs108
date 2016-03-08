package quizzes;

import java.util.Comparator;

/*
 * Comparator class of which a new instance is created
 * only when sorting scores by time completed.
 */
public class ScoreCompareByTime implements Comparator<Score>{

	@Override
	public int compare(Score o1, Score o2) {
		return (int)(o2.getTimeCompleted() - o1.getTimeCompleted());
	}

}
