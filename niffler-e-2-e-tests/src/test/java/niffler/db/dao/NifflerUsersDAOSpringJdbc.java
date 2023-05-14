package niffler.db.dao;

import niffler.db.DataSourceProvider;
import niffler.db.ServiceDB;
import niffler.db.entity.AuthorityEntity;
import niffler.db.entity.UserEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.UUID;

public class NifflerUsersDAOSpringJdbc implements NifflerUsersDAO {

    private final TransactionTemplate transactionTemplate;
    private final JdbcTemplate jdbcTemplate;

    public NifflerUsersDAOSpringJdbc() {
        DataSourceTransactionManager transactionManager = new JdbcTransactionManager(
                DataSourceProvider.INSTANCE.getDataSource(ServiceDB.NIFFLER_AUTH));
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.jdbcTemplate = new JdbcTemplate(transactionManager.getDataSource());
    }

    @Override
    public int createUser(UserEntity user) {

        return transactionTemplate.execute(st -> {
            KeyHolder key = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement stUser = con.prepareStatement("INSERT INTO users "
                        + "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) "
                        + " VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                stUser.setString(1, user.getUsername());
                stUser.setString(2, pe.encode(user.getPassword()));
                stUser.setBoolean(3, user.getEnabled());
                stUser.setBoolean(4, user.getAccountNonExpired());
                stUser.setBoolean(5, user.getAccountNonLocked());
                stUser.setBoolean(6, user.getCredentialsNonExpired());
                return stUser;
            }, key);

            final UUID userId;
            if (key.getKeys().get("id") != null) {
                userId = (UUID) key.getKeys().get("id");
                user.setId(userId);
            }
            for (AuthorityEntity autority : user.getAuthorities()) {
                jdbcTemplate.update("INSERT INTO authorities (user_id, authority) VALUES (?, ?)", user.getId(), autority.getAuthority().name());
            }
            return 1;
        });
    }

    @Override
    public String getUserId(String userName) {
        return jdbcTemplate.query("SELECT * FROM users WHERE username = ?",
                rs -> {
                    return rs.getString(1);
                },
                userName
        );
    }

    @Override
    public UserEntity getUser(String userId) {
        UserEntity user = new UserEntity();
        return jdbcTemplate.query("SELECT * FROM users u where u.id = ?::uuid", rs -> {
                    user.setId(UUID.fromString(rs.getString("id")));
                    user.setUsername(rs.getString("username"));
                    user.setEnabled(rs.getBoolean("enabled"));
                    user.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                    user.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                    user.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
                    return user;
                },
                userId);
    }

    @Override
    public int updateUser(UserEntity user) {
        return jdbcTemplate.update(con -> {
            PreparedStatement stUser = con.prepareStatement("UPDATE users set username = ?, password =?, enabled = ?, account_non_expired = ?, " +
                    "account_non_locked = ?, credentials_non_expired = ? where id = ?::uuid");
            stUser.setString(1, user.getUsername());
            stUser.setString(2, pe.encode(user.getPassword()));
            stUser.setBoolean(3, user.getEnabled());
            stUser.setBoolean(4, user.getAccountNonExpired());
            stUser.setBoolean(5, user.getAccountNonLocked());
            stUser.setBoolean(6, user.getCredentialsNonExpired());
            stUser.setString(7, String.valueOf(user.getId()));
            return stUser;
        });
    }

    @Override
    public int deleteUser(String userId) {
        return transactionTemplate.execute(st -> {
            jdbcTemplate.update("DELETE from authorities a where a.user_id = ?", userId);
            return jdbcTemplate.update("DELETE FROM users WHERE id = ?", userId);
        });
    }

    @Override
    public int removeUser(UserEntity user) {
        return transactionTemplate.execute(st -> {
            jdbcTemplate.update("DELETE FROM authorities WHERE user_id = ?", user.getId());
            return jdbcTemplate.update("DELETE FROM users WHERE id = ?", user.getId());
        });
    }
}
