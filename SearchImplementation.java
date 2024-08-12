import java.util.*;
import java.sql.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.*;
import edu.stanford.nlp.ling.CoreAnnotations.*;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.PTBTokenizer;

// TO DO: clean up comments + add StanfordNLP stop word removal annotator + 
// + search all tables in database (remove placeholder in query)

public class SearchImplementation {
  private StanfordCoreNLP pipeline;

  // constructor
  public SearchImplementation() {
    Properties properties = new Properties();
    // tokenization, sentence splitting, POS, lemmatization
    properties.setProperty("annotators", "tokenize,ssplit,lemma,pos");
    pipeline = new StanfordCoreNLP(properties);
  }

  public List<Integer> search(String term) {
    List<Integer> IDs = new ArrayList<>();

    String preprocessedTerm = preprocessSpecialCharNoiseWords(term);
    String query = query(preprocessedTerm);

    try (
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();) {
      while (resultSet.next()) {
        IDs.add(resultSet.getInt("IDs"));
      }
    } catch (SQLException exception) {
      exception.printStackTrace(); // handles exceptions
    }
    return IDs;
  }

  private String query(String term) {
    // put spaces between ( ) to correctly tokenize for complex expressions
    term = term.replace("(", "( ");
    term = term.replace(")", " )");

    // tokenize - split by space -- java regular expression
    String[] tokens = term.split("\\s+");
    // String[] tokens = term.edu.stanford.nlp.process.tokenize();

    int start = 0;
    int end = tokens.length - 1;

    return process(tokens, start, end);

  }

  private String process(String[] tokens, int start, int end) {
    // builds String for SQL query by by parsing through tokens

    // "table" in query is placeholder - need to search all tables

    StringBuilder query = new StringBuilder("SELECT ID FROM table WHERE ");

    for (int i = start; i <= end; i++) {

      // process parentheses
      if (tokens[i].equals("(")) {
        // find index of corresponding parentheses
        int j = indexClosingParen(tokens, i);
        // recursively go thru all terms within parentheses
        query.append("(").append(process(tokens, i + 1, j - 1)).append(")");
        i = j;
      }

      else if (tokens[i].equals("and")) {
        query.append("AND ");
      }

      else if (tokens[i].equals("or")) {
        query.append("OR ");
      }

      else {
        query.append("LOWER(Text) LIKE LOWER('%").append(tokens[i]).append("%') ");
      }
    }
    String queryString = query.toString().trim();
    return queryString;
  }

  private int indexClosingParen(String[] tokens, int openingIndex) {
    int count = 0;
    int closingIndex = openingIndex;
    for (int i = openingIndex; i < tokens.length; i++) {
      if (tokens[i].equals("(")) {
        count++;
      } else if (tokens[i].equals(")")) {
        count--;
      }
      if (count == 0) {
        closingIndex = i;
        break;
      }

    }
    return closingIndex;
  }

  private String preprocessSpecialCharNoiseWords(String term) {
    // Stanford NLP annotation
    Annotation doc = new Annotation(term);
    pipeline.annotate(doc);

    List<CoreMap> sentences = doc.get(SentencesAnnotation.class);
    StringBuilder newTerm = new StringBuilder();

    // NOTE: update to use stanford stop word removal annotator
    // can't remove AND / OR -- check
    List<String> noiseWords = Arrays.asList("a", "an", "the", "but", "not");

    for (CoreMap sentence : sentences)
      for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
        String oneWord = token.get(TextAnnotation.class).toLowerCase();
        if (oneWord.matches("[a-zA-Z0-9]+") && !noiseWords.contains(oneWord)) {
          newTerm.append(oneWord).append(" ");
        }
      }

    String returnedTerm = newTerm.toString().trim();
    return returnedTerm;

  }

}
