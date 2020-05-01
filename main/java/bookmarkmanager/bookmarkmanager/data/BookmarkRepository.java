package bookmarkmanager.bookmarkmanager.data;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class BookmarkRepository implements BookmarkInterface{

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public BookmarkRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Bookmark> findAllBookmarks() {
        return jdbcTemplate.query("select idbookmarks, title, imageurl, link, description, likes from bookmarks",
                new Object[]{},
                (rs, i) -> new Bookmark(
                    rs.getInt("idbookmarks"),
                    rs.getString("title"),
                    rs.getString("link"),
                    rs.getString("imageurl"),
                    rs.getInt("likes"),
                    rs.getString("description")
                )
        );
    }

    public List<Bookmark> findAllSortedByScoreBookmarks() {
        return jdbcTemplate.query("SELECT * FROM c1933378_IndividualProject.bookmarks order by likes desc",
                new Object[]{},
                (rs, i) -> new Bookmark(
                        rs.getInt("idbookmarks"),
                        rs.getString("title"),
                        rs.getString("link"),
                        rs.getString("imageurl"),
                        rs.getInt("likes"),
                        rs.getString("description")
                )
        );

    }

    public Bookmark findBookmark(int id){
        List<Bookmark> bookmarkList = jdbcTemplate.query("select idbookmarks, title, imageurl, link, description, likes from bookmarks where idbookmarks=" + id + " limit 1",
                new Object[]{},
                (rs, i) -> new Bookmark(
                        rs.getInt("idbookmarks"),
                        rs.getString("title"),
                        rs.getString("link"),
                        rs.getString("imageurl"),
                        rs.getInt("likes"),
                        rs.getString("description")
                )
        );
        return bookmarkList.get(0);
    }

    public int addBookmark(Bookmark bookmark, String username) {
        KeyHolder kh = new GeneratedKeyHolder();
        String sql="insert into bookmarks(title, link, imageurl, likes, favourites, description, poster)  values(?,?,?,?,?,?,?)";
        jdbcTemplate.update(new PreparedStatementCreator() {
                                @Override
                                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"}); //list of auto-generated key columns
                                    ps.setString(1, bookmark.getTitle());
                                    ps.setString(2, bookmark.getUrl());
                                    ps.setString(3, bookmark.getImg());
                                    ps.setInt(4,0);
                                    ps.setInt(5,0);
                                    ps.setString(6, bookmark.getDescription());
                                    ps.setString(7,username);
                                    return ps;
                                }
                            }
                , kh);
        return kh.getKey().intValue();
    }

    public int upvoteBookmarkSave(Bookmark bookmark, String username) {
        KeyHolder kh = new GeneratedKeyHolder();
        String sql="insert into liked_bookmarks(username,idbookmark)  values(?,?)";
        jdbcTemplate.update(new PreparedStatementCreator() {
                                @Override
                                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"}); //list of auto-generated key columns
                                    ps.setString(1, username);
                                    ps.setInt(2,bookmark.getId());
                                    return ps;
                                }
                            }
                , kh);

        return kh.getKey().intValue();
    }

    public int upvoteBookmarkUnsave(Bookmark bookmark, String username) {
        KeyHolder kh = new GeneratedKeyHolder();
        String sql="delete from liked_bookmarks where username=? and idBookmark=?";
        jdbcTemplate.update(new PreparedStatementCreator() {
                                @Override
                                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"}); //list of auto-generated key columns
                                    ps.setString(1, username);
                                    ps.setInt(2,bookmark.getId());
                                    return ps;
                                }
                            }
                , kh);

        return 1;
    }

    public int downvoteBookmarkSave(Bookmark bookmark, String username) {
        KeyHolder kh = new GeneratedKeyHolder();
        String sql="insert into disliked_bookmarks(username,idbookmark)  values(?,?)";
        jdbcTemplate.update(new PreparedStatementCreator() {
                                @Override
                                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"}); //list of auto-generated key columns
                                    ps.setString(1, username);
                                    ps.setInt(2,bookmark.getId());
                                    return ps;
                                }
                            }
                , kh);

        return kh.getKey().intValue();
    }

    public int downvoteBookmarkUnsave(Bookmark bookmark, String username) {
        KeyHolder kh = new GeneratedKeyHolder();
        String sql="delete from disliked_bookmarks where username=? and idBookmark=?";
        jdbcTemplate.update(new PreparedStatementCreator() {
                                @Override
                                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"}); //list of auto-generated key columns
                                    ps.setString(1, username);
                                    ps.setInt(2,bookmark.getId());
                                    return ps;
                                }
                            }
                , kh);

        return 1;
    }

    public void updateBookmarkValue(Bookmark bookmark) {
        KeyHolder kh = new GeneratedKeyHolder();
        String sql2="update bookmarks set likes = ? where idbookmarks = ?";
        jdbcTemplate.update(new PreparedStatementCreator() {
                                @Override
                                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                                    PreparedStatement ps = connection.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS); //list of auto-generated key columns
                                    ps.setInt(1, bookmark.getLikes());
                                    ps.setInt(2,bookmark.getId());
                                    ps.executeUpdate();
                                    return ps;
                                }
                            }
                , kh);
    }

    public void downvoteBookmarkValue(Bookmark bookmark) {
        KeyHolder kh = new GeneratedKeyHolder();
        String sql2="update bookmarks set likes = ? where idbookmarks = ?";
        jdbcTemplate.update(new PreparedStatementCreator() {
                                @Override
                                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                                    PreparedStatement ps = connection.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS); //list of auto-generated key columns
                                    ps.setInt(1, bookmark.getLikes());
                                    ps.setInt(2,bookmark.getId());
                                    ps.executeUpdate();
                                    return ps;
                                }
                            }
                , kh);

    }

    public int favouriteBookmark(Bookmark bookmark, String username) {
        KeyHolder kh = new GeneratedKeyHolder();
        String sql="insert into favourite_bookmarks(username,idbookmark)  values(?,?)";
        jdbcTemplate.update(new PreparedStatementCreator() {
                                @Override
                                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"}); //list of auto-generated key columns
                                    ps.setString(1, username);
                                    ps.setInt(2,bookmark.getId());
                                    return ps;
                                }
                            }
                , kh);

        return kh.getKey().intValue();
    }

    public int unfavouriteBookmark(Bookmark bookmark, String username) {
        KeyHolder kh = new GeneratedKeyHolder();
        String sql="delete from favourite_bookmarks where username=? and idBookmark=?";
        jdbcTemplate.update(new PreparedStatementCreator() {
                                @Override
                                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"}); //list of auto-generated key columns
                                    ps.setString(1, username);
                                    ps.setInt(2,bookmark.getId());
                                    return ps;
                                }
                            }
                , kh);

        return 1;
    }

    public List<Bookmark> findAllLikedBookmarks(HttpSession session) {
        String username = (String) session.getAttribute("SESSION_USERNAME");
        List<Bookmark> likedBookmarks = jdbcTemplate.query("select liked_bookmarks.username, idbookmarks, title, imageurl, link, description, likes from bookmarks inner join liked_bookmarks on liked_bookmarks.idbookmark = bookmarks.idbookmarks && liked_bookmarks.username = '" + username + "'",
                new Object[]{},
                (rs, i) -> new Bookmark(
                        rs.getInt("idbookmarks"),
                        rs.getString("title"),
                        rs.getString("link"),
                        rs.getString("imageurl"),
                        rs.getInt("likes"),
                        rs.getString("description")
                )
        );
        return likedBookmarks;
    }

    public List<Bookmark> findAllDislikedBookmarks(HttpSession session) {
        String username = (String) session.getAttribute("SESSION_USERNAME");
        List<Bookmark> dislikedBookmarks = jdbcTemplate.query("select disliked_bookmarks.username, idbookmarks, title, imageurl, link, description, likes from bookmarks inner join disliked_bookmarks on disliked_bookmarks.idbookmark = bookmarks.idbookmarks && disliked_bookmarks.username = '" + username + "'",
                new Object[]{},
                (rs, i) -> new Bookmark(
                        rs.getInt("idbookmarks"),
                        rs.getString("title"),
                        rs.getString("link"),
                        rs.getString("imageurl"),
                        rs.getInt("likes"),
                        rs.getString("description")
                )
        );
        return dislikedBookmarks;
    }

    public List<Bookmark> findAllFavouritedBookmarks(HttpSession session) {
        String username = (String) session.getAttribute("SESSION_USERNAME");
        List<Bookmark> favouriteBookmarks = jdbcTemplate.query("select favourite_bookmarks.username, idbookmarks, title, imageurl, link, description, likes from bookmarks inner join favourite_bookmarks on favourite_bookmarks.idbookmark = bookmarks.idbookmarks && favourite_bookmarks.username = '" + username + "'",
                new Object[]{},
                (rs, i) -> new Bookmark(
                        rs.getInt("idbookmarks"),
                        rs.getString("title"),
                        rs.getString("link"),
                        rs.getString("imageurl"),
                        rs.getInt("likes"),
                        rs.getString("description")
                )
        );
        return favouriteBookmarks;
    }

    public List<Bookmark> findAllPostedBookmarks(HttpSession session) {
        String username = (String) session.getAttribute("SESSION_USERNAME");
        List<Bookmark> postedBookmarks = jdbcTemplate.query("select * from bookmarks where poster='" + username + "'",
                new Object[]{},
                (rs, i) -> new Bookmark(
                        rs.getInt("idbookmarks"),
                        rs.getString("title"),
                        rs.getString("link"),
                        rs.getString("imageurl"),
                        rs.getInt("likes"),
                        rs.getString("description")
                )
        );
        return postedBookmarks;
    }

    public int updateBookmark(int id, Bookmark bookmark) {
        KeyHolder kh = new GeneratedKeyHolder();
        String sql="UPDATE bookmarks SET title=?, link=?, imageurl=?, description=? WHERE idbookmarks=" + id;
        return jdbcTemplate.update(new PreparedStatementCreator() {
                                @Override
                                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                                    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                                    ps.setString(1, bookmark.getTitle());
                                    ps.setString(2, bookmark.getUrl());
                                    ps.setString(3, bookmark.getImg());
                                    ps.setString(4,bookmark.getDescription());

                                    return ps;
                                }
                            }
                , kh);
    }

    public int deleteBookmark(int id) {
        KeyHolder kh = new GeneratedKeyHolder();
        String sql="DELETE FROM bookmarks WHERE idbookmarks=?";
        return jdbcTemplate.update(new PreparedStatementCreator() {
                                       @Override
                                       public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                                           PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                                           ps.setInt(1, id);
                                           return ps;
                                       }
                                   }
                , kh);
    }

}
