package ru.simbir.internship.chat.service.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;
import ru.simbir.internship.chat.dto.MessageDto;
import ru.simbir.internship.chat.service.MessageService;
import ru.simbir.internship.chat.service.UserService;

import java.net.URI;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@Service
public class YouTubeBotImpl implements YouTubeBot {
    public static final UUID BOT_ROOM_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    public static final UUID BOT_USER_ID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");

    @Value("${chat.google-api-key}")
    private String key;

    private final Logger logger = Logger.getLogger(YouTubeBotImpl.class.getName());
    private final String prefix = "https://www.googleapis.com/youtube/v3/";
    private final RestTemplate restTemplate = new RestTemplate();


    private final ObjectMapper objectMapper;
    private final MessageService messageService;
    private final UserService userService;

    @Autowired
    public YouTubeBotImpl(ObjectMapper objectMapper, MessageService messageService, UserService userService) {
        this.objectMapper = objectMapper;
        this.messageService = messageService;
        this.userService = userService;
    }

    @Override
    public List<MessageDto> channelInfo(String command) {
        try {
            int bias = 19; //сколько пропускать символов до начала поисковой фразы
            int numberOfLinks = 5; //количество сслылок в ответе
            List<MessageDto> result = new ArrayList<>();
            Map<String, String> channelInfo = getChannelInfo(command.substring(bias));
            StringBuilder links = new StringBuilder();
            getLastVideoUrls(channelInfo.get("channelId"), numberOfLinks).stream()
                    .map(s -> s.concat(System.lineSeparator()))
                    .forEach(links::append);
            result.add(createMessageDto(channelInfo.get("title")));
            result.add(createMessageDto(links.toString()));
            return result;
        } catch (JsonProcessingException e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
            return Collections.singletonList(createMessageDto("Ошибка при выполнении запроса."));
        }
    }

    @Override
    public List<MessageDto> videoCommentRandom(String command) {
        try {
            int bias = 26; //сколько пропускать символов до начала названия канала
            String separator = "||"; //разделитель имён канала и ролика
            String[] data = command.split(Pattern.quote(separator));
            if (data.length != 2) {
                logger.severe("Unexpected command");
                return Collections.singletonList(createMessageDto("Команда не распознана."));
            }
            String channelName = data[0].substring(bias);
            String videoName = data[1];
            String videoId = getVideoId(videoName, getChannelId(channelName));
            Map<String, String> randomCommentInfo = getCommentInfo(getRandomCommentID(videoId));
            List<MessageDto> result = new ArrayList<>();
            result.add(createMessageDto(randomCommentInfo.get("authorDisplayName")));
            result.add(createMessageDto(randomCommentInfo.get("textDisplay")));
            return result;
        } catch (JsonProcessingException e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
            return Collections.singletonList(createMessageDto("Ошибка при выполнении запроса."));
        }
    }

    //todo
    @Override
    public List<MessageDto> find(String command) {
        return null;
    }

    @Override
    public List<MessageDto> help() {
        StringBuilder answer = new StringBuilder();
        Arrays.stream(BotCommand.values())
                .map(c -> c.getTitle().concat(System.lineSeparator()))
                .forEach(answer::append);
        return Collections.singletonList(createMessageDto(answer.toString()));
    }

    private String createVideoUrl(String videoId) {
        return "https://www.youtube.com/watch?v=" + videoId;
    }

    private Map<String, String> getChannelInfo(String channelName) throws JsonProcessingException {
        UriTemplate uriTemplate = new UriTemplate(
                "{PREFIX}search?q={CHANNEL_NAME}&type=channel&part=snippet&maxResults=1&key={KEY}");
        URI uri = uriTemplate.expand(prefix, channelName, key);
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        JsonNode root = objectMapper.readTree(response.getBody());
        HashMap<String, String> result = new HashMap<>(2);
        result.put("channelId", root.path("items").get(0).path("id").path("channelId").textValue());
        result.put("title", root.path("items").get(0).path("snippet").path("title").textValue());
        return result;
    }

    private List<String> getLastVideoUrls(String channelId, int numberOfLinks) throws JsonProcessingException {
        UriTemplate uriTemplate = new UriTemplate(
                "{PREFIX}search?channelId={CHANNEL_ID}&part=id&order=date&maxResults={NUMBER_OF_LINKS}&key={KEY}");
        URI uri = uriTemplate.expand(prefix, channelId, numberOfLinks, key);
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        JsonNode root = objectMapper.readTree(response.getBody());
        List<String> result = new ArrayList<>();
        for (int i = 0; i < numberOfLinks; i++) {
            result.add(createVideoUrl(root.path("items").get(i).path("id").path("videoId").textValue()));
        }
        return result;
    }

    private String getChannelId(String channelName) throws JsonProcessingException {
        UriTemplate uriTemplate = new UriTemplate(
                "{PREFIX}search?q={CHANNEL_NAME}&type=channel&part=id&maxResults=1&key={KEY}");
        URI uri = uriTemplate.expand(prefix, channelName, key);
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        JsonNode root = objectMapper.readTree(response.getBody());
        return root.path("items").get(0).path("id").path("channelId").textValue();
    }

    private String getVideoId(String videoName, String channelId) throws JsonProcessingException {
        UriTemplate uriTemplate = new UriTemplate(
                "{PREFIX}search?q={VIDEO_NAME}&channelId={CHANNEL_ID}&type=video&part=id&maxResults=1&key={KEY}");
        URI uri = uriTemplate.expand(prefix, videoName, channelId, key);
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        JsonNode root = objectMapper.readTree(response.getBody());
        return root.path("items").get(0).path("id").path("videoId").textValue();
    }

    //Некорректно для видео с числом комментариев больше 100
    private String getRandomCommentID(String videoId) throws JsonProcessingException {
        UriTemplate uriTemplate = new UriTemplate(
                "{PREFIX}commentThreads?part=id&&maxResults=100&videoId={VIDEO_ID}&key={KEY}");
        URI uri = uriTemplate.expand(prefix, videoId, key);
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        JsonNode root = objectMapper.readTree(response.getBody());
        int random = (int) (Math.random() * root.path("pageInfo").path("totalResults").intValue());
        return root.path("items").get(random).path("id").textValue();
    }

    private Map<String, String> getCommentInfo(String commentId) throws JsonProcessingException {
        UriTemplate uriTemplate = new UriTemplate("{PREFIX}comments?part=snippet&id={COMMENT_ID}&key={KEY}");
        URI uri = uriTemplate.expand(prefix, commentId, key);
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        JsonNode root = objectMapper.readTree(response.getBody());
        HashMap<String, String> result = new HashMap<>(2);
        result.put("authorDisplayName",
                root.path("items").get(0).path("snippet").path("authorDisplayName").textValue());
        result.put("textDisplay", root.path("items").get(0).path("snippet").path("textDisplay").textValue());
        return result;
    }

    @Override
    public MessageDto createMessageDto(String text){
        MessageDto dto = new MessageDto();
        dto.setRoomId(BOT_ROOM_ID);
        dto.setUserId(BOT_USER_ID);
        dto.setText(text);
        return messageService.save(dto, BOT_ROOM_ID, userService.getById(BOT_USER_ID));
    }
}
