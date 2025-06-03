package library.repository;

import library.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Override
    @EntityGraph(attributePaths = {"author", "genres"}) // Eagerly fetch author & genres
    @NonNull
    Page<Book> findAll(@NonNull Pageable pageable);

    List<Book> findByAuthorId(Long authorId);
}