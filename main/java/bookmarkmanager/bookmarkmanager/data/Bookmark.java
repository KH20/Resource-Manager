package bookmarkmanager.bookmarkmanager.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Bookmark {
    private int id;
    private String title;
    private String url;
    private String img;
    private int likes;
    private String description;

    public Bookmark() {
        this.likes = 0;
    }

    public Bookmark(int id, String title, String url, String img, int likes, String description) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.img = img;
        this.likes = likes;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return "{" +
                "id:" + getId() +
                ",title:" + getTitle() +
                ",url:" + getUrl() +
                ",img:" + getImg() +
                '}';
    }

    public String toJSON() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

}
