package net.ion.meteoros21.apiserver.repository;

import net.ion.meteoros21.apiserver.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Integer>
{
    @Query(value="select p from Post p join fetch p.user",
            countQuery="select count(p) from Post p")
    Page<Post> findAllJoinFetch(Pageable p);

    @Query(value="select p from Post p join fetch p.user where p.postId = :postId")
    Post findOneByPostId(int postId);
}