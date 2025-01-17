package guru.qa.niffler.db.dao;


import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.ServiceDB;
import guru.qa.niffler.db.entity.AuthorityEntity;
import guru.qa.niffler.db.entity.UserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.UUID;

public class NifflerUsersDAOJdbc implements NifflerUsersDAO {

    private static final DataSource ds = DataSourceProvider.INSTANCE.getDataSource(ServiceDB.NIFFLER_AUTH);
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public int createUser(UserEntity user) {
        int executeUpdate = 0;

        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stUser = conn.prepareStatement("INSERT INTO users "
                    + "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) "
                    + " VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement stAutorities = conn.prepareStatement("INSERT INTO authorities (user_id, authority) VALUES (?, ?)")
            ) {

                stUser.setString(1, user.getUsername());
                stUser.setString(2, pe.encode(user.getPassword()));
                stUser.setBoolean(3, user.getEnabled());
                stUser.setBoolean(4, user.getAccountNonExpired());
                stUser.setBoolean(5, user.getAccountNonLocked());
                stUser.setBoolean(6, user.getCredentialsNonExpired());
                executeUpdate = stUser.executeUpdate();
                try (ResultSet resultSet = stUser.getGeneratedKeys()) {
                    if (resultSet.next())
                        user.setId(UUID.fromString(resultSet.getString(1)));
                    else
                        throw new SQLException("User not fined");
                }

                for (AuthorityEntity autority : user.getAuthorities()) {
                    stAutorities.setObject(1, user.getId());
                    stAutorities.setString(2, autority.getAuthority().name());
                    stAutorities.addBatch();
                    stAutorities.clearParameters();
                }
                stAutorities.executeBatch();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.print("Create user failed");
                conn.rollback();
                conn.setAutoCommit(true);
            }
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return executeUpdate;
    }

    @Override
    public String getUserId(String userName) {
        try (Connection conn = ds.getConnection();
             PreparedStatement st = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            st.setString(1, userName);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            } else {
                throw new IllegalArgumentException("Can`t find user by given username: " + userName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserEntity getUser(String userId) {
        UserEntity user = new UserEntity();
        try (Connection conn = ds.getConnection();
             PreparedStatement st = conn.prepareStatement("SELECT * FROM users u where u.id = ?::uuid")) {
            st.setString(1, userId);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                user.setId(UUID.fromString(resultSet.getString("id")));
                user.setUsername(resultSet.getString("username"));
                user.setEnabled(resultSet.getBoolean("enabled"));
                user.setAccountNonExpired(resultSet.getBoolean("account_non_expired"));
                user.setAccountNonLocked(resultSet.getBoolean("account_non_locked"));
                user.setCredentialsNonExpired(resultSet.getBoolean("credentials_non_expired"));
                return user;
            } else {
                throw new IllegalArgumentException("Can`t find user by given userId: " + userId);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int updateUser(UserEntity user) {
        int executeUpdate;

        try (Connection conn = ds.getConnection();
             PreparedStatement st = conn.prepareStatement("UPDATE users set username = ?, password =?, enabled = ?, " +
                     "account_non_expired = ?, account_non_locked = ?, credentials_non_expired = ?"
                     + " where id = ?::uuid")) {
            st.setString(1, user.getUsername());
            st.setString(2, pe.encode(user.getPassword()));
            st.setBoolean(3, user.getEnabled());
            st.setBoolean(4, user.getAccountNonExpired());
            st.setBoolean(5, user.getAccountNonLocked());
            st.setBoolean(6, user.getCredentialsNonExpired());
            st.setString(7, String.valueOf(user.getId()));

            executeUpdate = st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return executeUpdate;
    }

    @Override
    public int removeUser(UserEntity user) {
        int executeUpdate;

        try (Connection conn = ds.getConnection()) {

            conn.setAutoCommit(false);

            try (PreparedStatement deleteUserSt = conn.prepareStatement("DELETE FROM users WHERE id = ?");
                 PreparedStatement deleteAuthoritySt = conn.prepareStatement(
                         "DELETE FROM authorities WHERE user_id = ?")) {
                deleteUserSt.setObject(1, user.getId());
                deleteAuthoritySt.setObject(1, user.getId());

                deleteAuthoritySt.executeUpdate();
                executeUpdate = deleteUserSt.executeUpdate();

            } catch (SQLException e) {
                conn.rollback();
                conn.setAutoCommit(true);
                throw new RuntimeException(e);
            }

            conn.commit();
            conn.setAutoCommit(true);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return executeUpdate;
    }
}
