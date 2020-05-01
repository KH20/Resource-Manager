package bookmarkmanager.bookmarkmanager.controllers;

import bookmarkmanager.bookmarkmanager.data.*;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;


@Controller
public class BaseController {

    public BaseController(){

    }

    private BookmarkRepository repo;
    private UserRepository userRepo;

    @Autowired
    public BaseController(BookmarkRepository repo){
        this.repo = repo;
    }

    @GetMapping("/")
    public ModelAndView getAllBookmarks(HttpSession session) throws IOException {
        ModelAndView mv = new ModelAndView();
        List<Bookmark> bookmarks = repo.findAllBookmarks();
        List<Bookmark> likedBookmarks = repo.findAllLikedBookmarks(session);
        List<Bookmark> dislikedBookmarks = repo.findAllDislikedBookmarks(session);
        List<Bookmark> favouritedBookmarks = repo.findAllFavouritedBookmarks(session);
        List<Bookmark2> allBookmarks2 = new ArrayList<>();

        for(Bookmark bookmark : bookmarks){
            Bookmark2 bookmark2 = new Bookmark2(bookmark.getId(),bookmark.getTitle(),bookmark.getUrl(),
                    bookmark.getImg(),bookmark.getLikes(),bookmark.getDescription(),false,false,false);
            for(Bookmark likedBookmark : likedBookmarks){
                if(bookmark.getId() == likedBookmark.getId()){
                    bookmark2.setLiked(true);
                    break;
                }
            }
            for(Bookmark dislikedBookmark : dislikedBookmarks){
                if(bookmark.getId() == dislikedBookmark.getId()){
                    bookmark2.setDisliked(true);
                    break;
                }
            }
            for(Bookmark favouritedBookmark : favouritedBookmarks){
                if(bookmark.getId() == favouritedBookmark.getId()){

                    bookmark2.setFavourited(true);
                    break;
                }
            }
            allBookmarks2.add(bookmark2);
        }
        List<Bookmark2> sortedBookmarks2 = sortBookmarks2(allBookmarks2);
        mv.addObject("bookmarks2", sortedBookmarks2);
        mv.setViewName("/index");
        return mv;
    }

    @GetMapping("/liked")
    public ModelAndView getAllLikedBookmarks(HttpSession session){
        ModelAndView mv = new ModelAndView();
        List<Bookmark> bookmarks = repo.findAllLikedBookmarks(session);
        List<Bookmark> sortedBookmarks = sortBookmarks(bookmarks);

        mv.addObject("likedBookmarks", sortedBookmarks);
        mv.setViewName("/liked");
        return mv;
    }

    @GetMapping("/disliked")
    public ModelAndView getAllDislikedBookmarks(HttpSession session){
        ModelAndView mv = new ModelAndView();
        List<Bookmark> bookmarks = repo.findAllDislikedBookmarks(session);
        List<Bookmark> sortedBookmarks = sortBookmarks(bookmarks);

        mv.addObject("dislikedBookmarks", sortedBookmarks);
        mv.setViewName("/disliked");
        return mv;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        new SecurityContextLogoutHandler().logout(request, null, null);
        return "redirect:/";
    }


    @GetMapping("addResource")
    public String getAddResourcePage(Model model){
        model.addAttribute("bookmark", new Bookmark());
        return "addResource";
    }

    @GetMapping("/favourites")
    public ModelAndView getFavouriteBookmarks(HttpSession session){
        ModelAndView mv = new ModelAndView();
        List<Bookmark> bookmarks = repo.findAllFavouritedBookmarks(session);
        List<Bookmark> sortedBookmarks = sortBookmarks(bookmarks);

        mv.addObject("favouriteBookmarks", sortedBookmarks);
        mv.setViewName("/favourites");
        return mv;
    }

    @PostMapping("/addResource")
    public String submitBookmark(@ModelAttribute Bookmark bookmark, HttpSession session){
        String username = (String) session.getAttribute("SESSION_USERNAME");
        repo.addBookmark(bookmark, username);
        return("redirect:/");
    }

    @RequestMapping(value="/upvote", method=RequestMethod.GET)
    @ResponseBody
    public ModelAndView getUpvote(@RequestParam int id, HttpSession session){
        String username = (String) session.getAttribute("SESSION_USERNAME");
        boolean alreadyLiked=false;
        boolean alreadyDisliked=false;
        if (username == null || username.isEmpty())
            return new ModelAndView("redirect:/login");

        List<Bookmark> likedBookmarks = repo.findAllLikedBookmarks(session);
        List<Bookmark> dislikedBookmarks = repo.findAllDislikedBookmarks(session);

        for(Bookmark bookmark : likedBookmarks){
            if(bookmark.getId() == id){
                alreadyLiked = true;
            }
        }
        for(Bookmark bookmark : dislikedBookmarks){
            if(bookmark.getId() == id){
                alreadyDisliked = true;
            }
        }

        Bookmark bookmark = repo.findBookmark(id);

        if(alreadyLiked){
            bookmark.setLikes(bookmark.getLikes() - 1);
            repo.upvoteBookmarkUnsave(bookmark, username);
            repo.updateBookmarkValue(bookmark);
            return new ModelAndView("redirect:/");
        }
        else if(alreadyDisliked){
            bookmark.setLikes(bookmark.getLikes() + 2);
            repo.downvoteBookmarkUnsave(bookmark,username);
            repo.upvoteBookmarkSave(bookmark,username);
            repo.updateBookmarkValue(bookmark);
            return new ModelAndView("redirect:/");
        }
        else{
            bookmark.setLikes(bookmark.getLikes() + 1);
            repo.upvoteBookmarkSave(bookmark,username);
            repo.updateBookmarkValue(bookmark);
            return new ModelAndView("redirect:/");
        }

    }

    @RequestMapping(value="/downvote", method=RequestMethod.GET)
    @ResponseBody
    public ModelAndView getDownvote(@RequestParam int id, HttpSession session){
        String username = (String) session.getAttribute("SESSION_USERNAME");
        boolean alreadyLiked=false;
        boolean alreadyDisliked=false;

        if (username == null || username.isEmpty())
            return new ModelAndView("redirect:/login");

        List<Bookmark> likedBookmarks = repo.findAllLikedBookmarks(session);
        List<Bookmark> dislikedBookmarks = repo.findAllDislikedBookmarks(session);

        for(Bookmark bookmark : likedBookmarks){
            if(bookmark.getId() == id){
                alreadyLiked = true;
            }
        }
        for(Bookmark bookmark : dislikedBookmarks){
            if(bookmark.getId() == id){
                alreadyDisliked = true;
            }
        }
        Bookmark bookmark = repo.findBookmark(id);
        if(alreadyLiked){
            bookmark.setLikes(bookmark.getLikes() - 2);
            repo.upvoteBookmarkUnsave(bookmark, username);
            repo.downvoteBookmarkSave(bookmark,username);
            repo.updateBookmarkValue(bookmark);
            return new ModelAndView("redirect:/");
        }
        else if(alreadyDisliked){
            bookmark.setLikes(bookmark.getLikes() + 1);
            repo.downvoteBookmarkUnsave(bookmark,username);
            repo.updateBookmarkValue(bookmark);
            return new ModelAndView("redirect:/");
        }
        else{
            bookmark.setLikes(bookmark.getLikes() - 1);
            repo.downvoteBookmarkSave(bookmark,username);
            repo.updateBookmarkValue(bookmark);
            return new ModelAndView("redirect:/");
        }

    }

    @RequestMapping(value="/favourite", method=RequestMethod.GET)
    @ResponseBody
    public ModelAndView favouriteBookmark(@RequestParam int id, HttpSession session){
        String username = (String) session.getAttribute("SESSION_USERNAME");
        boolean alreadyFavourited=false;
        if (username == null || username.isEmpty())
            return new ModelAndView("redirect:/login");

        List<Bookmark> favourites = repo.findAllFavouritedBookmarks(session);

        for(Bookmark bookmark : favourites){
            if(bookmark.getId() == id){
                alreadyFavourited = true;
            }
        }

        Bookmark bookmark = repo.findBookmark(id);

        if(alreadyFavourited){
            repo.unfavouriteBookmark(bookmark, username);
        }
        else{
            repo.favouriteBookmark(bookmark, username);
        }

        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value="/unlike", method=RequestMethod.GET)
    @ResponseBody
    public ModelAndView unlikedBookmark(@RequestParam int id, HttpSession session){
        String username = (String) session.getAttribute("SESSION_USERNAME");
        if (username == null || username.isEmpty())
            return new ModelAndView("redirect:/login");

        Bookmark bookmark = repo.findBookmark(id);

        repo.upvoteBookmarkUnsave(bookmark,username);
        bookmark.setLikes(bookmark.getLikes()-1);
        repo.updateBookmarkValue(bookmark);

        return new ModelAndView("redirect:/liked");
    }

    @RequestMapping(value="/undislike", method=RequestMethod.GET)
    @ResponseBody
    public ModelAndView undislikeBookmark(@RequestParam int id, HttpSession session){
        String username = (String) session.getAttribute("SESSION_USERNAME");
        if (username == null || username.isEmpty())
            return new ModelAndView("redirect:/login");

        Bookmark bookmark = repo.findBookmark(id);

        repo.downvoteBookmarkUnsave(bookmark,username);
        bookmark.setLikes(bookmark.getLikes()+1);
        repo.downvoteBookmarkValue(bookmark);

        return new ModelAndView("redirect:/disliked");
    }

    @RequestMapping(value="/unfavourite", method=RequestMethod.GET)
    @ResponseBody
    public ModelAndView unfavouriteBookmark(@RequestParam int id, HttpSession session){
        String username = (String) session.getAttribute("SESSION_USERNAME");
        if (username == null || username.isEmpty())
            return new ModelAndView("redirect:/login");

        Bookmark bookmark = repo.findBookmark(id);

        repo.unfavouriteBookmark(bookmark,username);

        return new ModelAndView("redirect:/favourites");
    }

    @GetMapping("/postedBookmarks")
    public ModelAndView postedBookmarks(HttpSession session){
        ModelAndView mv = new ModelAndView();
        List<Bookmark> postedBookmarks = repo.findAllPostedBookmarks(session);

        mv.addObject("postedBookmarks", postedBookmarks);
        mv.setViewName("/postedBookmarks");
        return mv;
    }

    @RequestMapping(value="/editBookmark", method=RequestMethod.GET)
    @ResponseBody
    public ModelAndView editBookmark(HttpSession session, @RequestParam int id){
        ModelAndView mv = new ModelAndView();
        Bookmark bookmark = repo.findBookmark(id);
        mv.addObject("bookmark",bookmark);
        mv.setViewName("/editBookmark");
        return mv;
    }

    private List<Bookmark> sortBookmarks(List<Bookmark> bookmarks) {
        List<Bookmark> sortedBookmarks = new ArrayList<>();
        for(Bookmark bookmark : bookmarks){
            sortedBookmarks.add(bookmark);
        }

        Collections.sort(sortedBookmarks, new Comparator<Bookmark>() {
            @Override
            public int compare(Bookmark o1, Bookmark o2) {
                if(o1.getLikes() > o2.getLikes()){
                    return 1;
                }
                else if(o1.getLikes() == o2.getLikes()){
                    return 0;
                }
                else
                    return -1;
            }
        });
        Collections.reverse(sortedBookmarks);
        return sortedBookmarks;
    }

    @PostMapping("/update/bookmark{id}")
    public String updateBookmark(@ModelAttribute Bookmark bookmark, @RequestParam int id){

        repo.updateBookmark(id, bookmark);
        return("redirect:/postedBookmarks");

    }

    @PostMapping("/delete/bookmark")
    public String deleteBookmark(@RequestParam int id){
        System.out.println("DELETE");
        repo.deleteBookmark(id);
        return("redirect:/postedBookmarks");
    }

    private List<Bookmark2> sortBookmarks2(List<Bookmark2> bookmarks2) {
        List<Bookmark2> sortedBookmarks2 = new ArrayList<>();
        for(Bookmark2 bookmark2 : bookmarks2){
            sortedBookmarks2.add(bookmark2);
        }

        Collections.sort(sortedBookmarks2, new Comparator<Bookmark2>() {
            @Override
            public int compare(Bookmark2 o1, Bookmark2 o2) {
                if (o1.getLikes() > o2.getLikes()) {
                    return 1;
                } else if (o1.getLikes() == o2.getLikes()) {
                    return 0;
                } else
                    return -1;
            }
        });
        Collections.reverse(sortedBookmarks2);
        return sortedBookmarks2;
    }
}
