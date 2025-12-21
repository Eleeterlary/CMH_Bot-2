package eleeter.Cmhbot.loaders;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

public class ResponseLoader {

    private final String responsesFile;
    private final String faqFile;

    private Map<String, String> responses;
    private Map<String, String> faq;

    public ResponseLoader(String responsesFile, String faqFile) {
        this.responsesFile = responsesFile;
        this.faqFile = faqFile;
        loadResponses();
        loadFAQ();
    }

    private void loadResponses() {
        try (FileReader reader = new FileReader(responsesFile)) {
            Type type = new TypeToken<Map<String, String>>() {}.getType();
            responses = new Gson().fromJson(reader, type);
            if (responses == null) responses = Collections.emptyMap();
        } catch (Exception e) {
            System.out.println("Failed to load " + responsesFile);
            responses = Collections.emptyMap();
        }
    }

    private void loadFAQ() {
        try (FileReader reader = new FileReader(faqFile)) {
            Type type = new TypeToken<Map<String, String>>() {}.getType();
            faq = new Gson().fromJson(reader, type);
            if (faq == null) faq = Collections.emptyMap();
        } catch (Exception e) {
            System.out.println("Failed to load " + faqFile);
            faq = Collections.emptyMap();
        }
    }

    public Map<String, String> getResponses() {
        return responses;
    }

    public Map<String, String> getFAQ() {
        return faq;
    }

    public void saveResponses() {try (FileWriter writer = new FileWriter(responsesFile)) {new Gson().toJson(responses, writer);}catch (Exception e) {e.printStackTrace();}

    }
}
