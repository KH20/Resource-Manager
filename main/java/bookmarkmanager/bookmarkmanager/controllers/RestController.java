package bookmarkmanager.bookmarkmanager.controllers;


import bookmarkmanager.bookmarkmanager.data.Bookmark;
import bookmarkmanager.bookmarkmanager.data.BookmarkInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    @Autowired
    private BookmarkInterface bookmarkInterface;

    @GetMapping("/bookmarks")
    public Object getBookmarks(){
        List<Bookmark> bookmarks = bookmarkInterface.findAllBookmarks();
        return bookmarks;
    }

    @GetMapping("/bookmarks/sorted")
    public Object getSortedBookmarks(){
        List<Bookmark> bookmarks = bookmarkInterface.findAllSortedByScoreBookmarks();
        return bookmarks;
    }

}
