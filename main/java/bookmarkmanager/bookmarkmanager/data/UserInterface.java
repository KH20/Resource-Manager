package bookmarkmanager.bookmarkmanager.data;


public interface UserInterface {
    boolean doesUserNameExist(String u);
    boolean addUser(User user);
}

