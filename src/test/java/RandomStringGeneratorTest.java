import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.text.CharacterPredicate;
import org.apache.commons.text.RandomStringGenerator;
import org.junit.Test;

public class RandomStringGeneratorTest {

    @Test
    public void test() {
        RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder()
                .filteredBy((CharacterPredicate) codePoint -> (codePoint >= 'a' && codePoint <= 'z') ||
                                (codePoint >= 'A' && codePoint <= 'Z') ||
                                (codePoint >= '0' && codePoint <= '9')
                )
                .build();
        System.out.println(randomStringGenerator.generate(16));
        System.out.println(RandomStringUtils.randomAlphanumeric(16));
    }
}
