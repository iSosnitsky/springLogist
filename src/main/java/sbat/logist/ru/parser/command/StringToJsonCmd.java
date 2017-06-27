package sbat.logist.ru.parser.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component("json")
public class StringToJsonCmd implements Command<String, Map<String, Object>> {
    private static final Logger logger = LoggerFactory.getLogger("main");

    private final JsonParser parser = new BasicJsonParser();

    public Map<String, Object> execute(String fileAsString) {
        return getJsonObjectFromString(fileAsString);
    }

    protected Map<String, Object> getJsonObjectFromString(String jsonFileAsString) {
        // идти по файлу используя regex и исправлять все ошибки
        final String jsonFileAsStringWithoutBom = jsonFileAsString.replaceAll("\uFEFF", "");
        final String fixedString = fixString(jsonFileAsStringWithoutBom);
        try {
            return parser.parseMap(fixedString);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("cant parse json");
        }
    }

    protected String fixString(String jsonFileAsString) {
        Pattern pattern = Pattern.compile("(\"num_boxes\":\\s)(.*)(,)", Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(jsonFileAsString);
        StringBuffer stringBuffer = new StringBuffer();

        while (matcher.find()) {
            String forReplace = matcher.group(2);
            if (!forReplace.equals("\"\"") && !forReplace.matches("\\d+")) {
                String replacement = "\"\"";
                matcher.appendReplacement(stringBuffer, "$1" + replacement + "$3");
                logger.warn("num_boxes = [{}] is replaced with [{}]", forReplace, replacement);
            }
        }
        matcher.appendTail(stringBuffer);
        if (stringBuffer.length() > 0)
            return stringBuffer.toString();
        else
            return jsonFileAsString;
    }
}

