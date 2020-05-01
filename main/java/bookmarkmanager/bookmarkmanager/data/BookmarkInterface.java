package bookmarkmanager.bookmarkmanager.data;

import java.util.List;

public interface BookmarkInterface {

    List<Bookmark> findAllBookmarks();
    List<Bookmark> findAllSortedByScoreBookmarks();
}
