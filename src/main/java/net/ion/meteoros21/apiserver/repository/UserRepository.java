package net.ion.meteoros21.apiserver.repository;

import net.ion.meteoros21.apiserver.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String>
{
    User findOneByUserId(String userId);
    User findOneByUserIdAndUserPwd(String userId, String passwd);
}
