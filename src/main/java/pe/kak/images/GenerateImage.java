package pe.kak.images;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.image.ImageGeneration;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageClient;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
@Slf4j
public class GenerateImage {

  private final OpenAiImageClient openaiImageClient;

  private final String BASE_PROMPT="You're creating an innovative streaming platform that uses the psychology of movies to personalize recommendations. Instead of focusing on genres or actors, your platform analyzes users' favorite character choices to decipher their hidden tastes. If a user selects characters like Ellen Ripley (Alien), Indiana Jones, and Sherlock Holmes, you could infer that the user values intelligence, bravery, and problem solving. Based on this, you would recommend science fiction, action and mystery movies.";
  private final String USERNAME = "gonza_cc";
  // relevant data for the user like realname, age, perfil photo,

  @GetMapping
  public List<ImageGeneration> generateImage(
      @RequestParam(required = false, defaultValue = "1024") Integer width,
      @RequestParam(required = false, defaultValue = "1024") Integer height,
      @RequestParam(required = false, defaultValue = "1") Integer quantity
  ) {

    Map<String, List<String>> CHARACTERS = new HashMap<>();

    CHARACTERS.put("Star Wars: Episode IV - A New Hope", List.of("Luke Skywalker", "Han Solo", "Princess Leia"));
    CHARACTERS.put("The Lord of the Rings: The Fellowship of the Ring", List.of("Frodo Baggins", "Samwise Gamgee", "Aragorn"));
    CHARACTERS.put("Harry Potter and the Philosopher's Stone", List.of("Harry Potter", "Hermione Granger", "Ron Weasley"));
    CHARACTERS.put("Game of Thrones", List.of("Daenerys Targaryen", "Jon Snow", "Tyrion Lannister"));
    CHARACTERS.put("Stranger Things", List.of("Eleven", "Mike Wheeler", "Dustin Henderson"));
    CHARACTERS.put("Money Heist", List.of("The Professor", "Tokyo", "Rio"));

    var finalPrompt = String.format(
        "Now the user %s likes the following series and movies: %s I need that based on this I want you to generate what he would be like in person, for example, the color of his eyes, his physique, his gender, height, stature.",
        USERNAME,
        String.join(",", CHARACTERS.keySet())
    );

    ImageResponse response = openaiImageClient.call(
        new ImagePrompt(
            finalPrompt,
            OpenAiImageOptions.builder()
                .withQuality("hd")
                .withN(quantity)
                .withHeight(width)
                .withWidth(height).build()
        )
    );

    return response.getResults();
//    log.info(finalPrompt);
//    return null;
  }

  @GetMapping("/prompt")
  public String getPrompt() {
    return USERNAME;
  }
}
