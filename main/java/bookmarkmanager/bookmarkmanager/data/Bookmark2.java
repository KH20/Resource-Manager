package bookmarkmanager.bookmarkmanager.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Bookmark2 extends Bookmark {
    private boolean liked;
    private boolean disliked;
    private boolean favourited;

    public Bookmark2(int id, String title, String url, String img, int likes, String description, boolean liked, boolean disliked, boolean favourited) {
        super(id, title, url, img, likes, description);
        this.liked = liked;
        this.disliked = disliked;
        this.favourited = favourited;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isDisliked() {
        return disliked;
    }

    public void setDisliked(boolean disliked) {
        this.disliked = disliked;
    }

    public boolean isFavourited() {
        return favourited;
    }

    public void setFavourited(boolean favourited) {
        this.favourited = favourited;
    }

    public String toString() {
        return "{" +
                "id:" + getId() +
                ",title:" + getTitle() +
                ",url:" + getUrl() +
                ",img:" + getImg() +
                ",liked:" + liked +
                ", disliked:" + disliked +
                ", favourited:" + favourited +
                '}';
    }

    public String toJSON() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}
