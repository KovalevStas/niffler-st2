package niffler.db.dao;

import java.util.UUID;
import niffler.db.entity.UserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.SQLException;

public interface NifflerUsersDAO {

  PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();
  int createUser(UserEntity user);

    String getUserId(String userName);

    UserEntity getUser(String userId);

    int updateUser(UserEntity user);

    int deleteUser(String userId) throws SQLException;

  int removeUser(UserEntity user);

}
