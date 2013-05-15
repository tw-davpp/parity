package classification;

import org.junit.Test;

import java.io.IOException;

public class NaiveBayesTest {
    @Test
    public void test_naive_bayes() throws IOException {
        MySearcher searcher = new MySearcher("./index");
        UserClick userClick = new UserClick();
        UserClick[] clicks = userClick.load();
        TrainingSet trainingSet = new TrainingSet(clicks);
        NaiveBayes naiveBayes = new NaiveBayes("Naive Bayes", trainingSet);
        naiveBayes.trainOnAttribute("Username");
        naiveBayes.trainOnAttribute("QueryTerm_1");
        naiveBayes.trainOnAttribute("QueryTerm_2");
        naiveBayes.train();
        searcher.setUserLearner(naiveBayes);
        UserQuery davppQuery = new UserQuery("davpp", "galaxy");
        searcher.search(davppQuery, 1000);

    }


}
