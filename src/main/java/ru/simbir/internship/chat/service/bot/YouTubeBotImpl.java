package ru.simbir.internship.chat.service.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.simbir.internship.chat.dto.MessageDto;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class YouTubeBotImpl implements YouTubeBot{
    @Value("${chat.google-api-key}")
    private String key;

    private final Logger logger = Logger.getLogger(YouTubeBotImpl.class.getName());
    private final String API_PREFIX = "https://www.googleapis.com/youtube/v3/";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;

    @Autowired
    public YouTubeBotImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public List<MessageDto> channelInfo(String command) {
        int bias = 19; //сколько пропускать символов до начала поисковой фразы
        int numberOfLinks = 5; //количество сслылок в ответе
        List<MessageDto> result = new ArrayList<>();
        Map<String, String> channelInfo = getChannelInfo(command.substring(bias));
        result.add(new MessageDto(channelInfo.get("title"), new Date()));
        getLastVideoUrls(channelInfo.get("channelId"), numberOfLinks).stream()
                .map(s -> new MessageDto(s, new Date()))
                .forEach(result::add);
        return result;
    }

    //todo
    @Override
    public List<MessageDto> videoCommentRandom(String command) {
        return null;
    }

    //todo
    @Override
    public List<MessageDto> find(String command) {
        return null;
    }

    @Override
    public List<MessageDto> help() {
        return Arrays.stream(BotCommand.values())
                .map(c-> new MessageDto(c.getTitle(), new Date()))
                .collect(Collectors.toList());
    }

    private String createVideoUrl(String videoId){
        return "https://www.youtube.com/watch?v=" + videoId;
    }

    private Map<String, String> getChannelInfo(String channelName){
        ResponseEntity<String> response
                = restTemplate.getForEntity(API_PREFIX + "search?q=" + channelName
                + "&type=channel&part=snippet&maxResults=1&key=" + key, String.class);
        JsonNode root;
        try {
            root = objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
            return Collections.emptyMap();
        }
        HashMap<String, String> result = new HashMap<>(2);
        result.put("channelId", root.path("items").get(0).path("id").path("channelId").textValue());
        result.put("title", root.path("items").get(0).path("snippet").path("title").textValue());
        return result;
    }

    private List<String> getLastVideoUrls(String channelId, int numberOfLinks) {
        JsonNode root;
            ResponseEntity<String> response
                = restTemplate.getForEntity(API_PREFIX + "search?channelId=" + channelId
                + "&part=id&order=date&maxResults=" + numberOfLinks + "&key=" + key, String.class);
        try{
            root = objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
        List<String> result = new ArrayList<>();
        for (int i = 0; i < numberOfLinks; i++) {
            result.add(createVideoUrl(root.path("items").get(i).path("id").path("videoId").textValue()));
        }
        return result;
    }
}
