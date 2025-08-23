package conconccc.schnofiticationbe.repository;


import conconccc.schnofiticationbe.entity.BasePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasePostRepository extends JpaRepository<BasePost, Long> {

}