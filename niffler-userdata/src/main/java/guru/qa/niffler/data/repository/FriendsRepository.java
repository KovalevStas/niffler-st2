package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.FriendsEntity;
import guru.qa.niffler.data.FriendsId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendsRepository extends JpaRepository<FriendsEntity, FriendsId> {


}
