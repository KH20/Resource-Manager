package bookmarkmanager.bookmarkmanager.data;

import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private String cPassword;
    private ArrayList<Bookmark> favourtes;
    private ArrayList<Bookmark> upvoted;
    private ArrayList<Bookmark> downvoted;


    public User(){

    }

    public User(String username) {
        this.username = username;
    }

    public User(String username, String password, String cPassword) {
        this.username = username;
        this.password = password;
        this.cPassword = cPassword;
        this.favourtes = new ArrayList<>();
        this.upvoted = new ArrayList<>();
        this.downvoted = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getcPassword() {
        return cPassword;
    }

    public void setcPassword(String cPassword) {
        this.cPassword = cPassword;
    }

    public ArrayList<Bookmark> getFavourtes() {
        return favourtes;
    }

    public void setFavourtes(ArrayList<Bookmark> favourtes) {
        this.favourtes = favourtes;
    }

    public ArrayList<Bookmark> getUpvoted() {
        return upvoted;
    }

    public void setUpvoted(ArrayList<Bookmark> upvoted) {
        this.upvoted = upvoted;
    }

    public ArrayList<Bookmark> getDownvoted() {
        return downvoted;
    }

    public void setDownvoted(ArrayList<Bookmark> downvoted) {
        this.downvoted = downvoted;
    }
}
