package bookmarkmanager.bookmarkmanager.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserRepository implements UserInterface {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate aTemplate) {
        jdbcTemplate = aTemplate;
    }

    @Bean //https://www.baeldung.com/spring-bean
    public BCryptPasswordEncoder passwordEncoder2() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    /*
    Method to return whether a username already exists. Used as part of the registration process
    */
    public boolean doesUserNameExist(String u) {
        return (jdbcTemplate.queryForList("select username from users where username = ?", new Object[]{u})).size() > 0;
    }

    /*
    Add a new user to the users table in the database using a User object passed
    from RegController which is built from what the user submits as part of the
    registration form.
    */
    private int _addToUserTable(User u){
        String sql = "insert into users(username, password, enabled) values(?,?,?)";
        return jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                //unlike the databases learning resources surrounding tv shows, the user table here does not contain
                // an auto-incrementing field named id, otherwise a second attribute would be added to the line
                // above --> new String[]{"id"}
                ps.setString(1, u.getUsername());
                ps.setString(2, passwordEncoder2().encode(u.getPassword()));
                ps.setInt(3, 1);
                return ps;
            }
        });
    }
    /*
    Add authority information for a user. Authorities refer to user groups which
    can have different levels of security access. Here all users are part of a
    single group, but this could be extended to allow some users to also be
    administrators, or other types of additional privilege.
    */
    private int _addToAuthoritiesTable(User u){
        String sql2 = "insert into authorities(username, authority) values(?,?)";
        return jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql2);
                ps.setString(1, u.getUsername());
                ps.setString(2, "ROLE_USER");
                return ps;
            }
        });
    }

    /*
        Add a new user to the database.
        User data is added across two tables, which have been split into separate methods here.
    */
    public boolean addUser(User u) {
        return _addToUserTable(u) > 0 && _addToAuthoritiesTable(u) > 0;
    }

    public User findUser(String user){
        List<User> userList = jdbcTemplate.query("select username from users where username='" + "'" +" limit 1",
                new Object[]{},
                (rs, i) -> new User(
                        rs.getString(user)
                )
        );
        return userList.get(0);
    }

}
