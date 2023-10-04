package ru.practicum.shareit.comment;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase
@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CommentRepositoryJpaTest {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private User user1;
    private Item item1;
    private Item item2;
    private Comment comment1;
    private Comment comment2;
    private Comment comment3;
    @BeforeEach
    void init() {
        user1 = User.builder()
                .id(1L)
                .name("testUser")
                .email("test@mail.ru")
                .build();
        item1 = Item.builder()
                .id(1L)
                .name("test1")
                .description("test1")
                .available(true)
                .owner(user1)
                .build();
        item2 = Item.builder()
                .id(2L)
                .name("test2")
                .description("test2")
                .available(true)
                .owner(user1)
                .build();
        comment1 = Comment.builder()
                .id(1L)
                .text("PRIKOL1")
                .item(item1)
                .author(user1)
                .created(LocalDateTime.now())
                .build();
        comment2 = Comment.builder()
                .id(2L)
                .text("PRIKOL2")
                .item(item1)
                .author(user1)
                .created(LocalDateTime.now())
                .build();
        comment3 = Comment.builder()
                .id(3L)
                .text("PRIKOL3")
                .item(item2)
                .author(user1)
                .created(LocalDateTime.now())
                .build();
        userRepository.save(user1);
        itemRepository.save(item1);
        itemRepository.save(item2);
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
    }

    @Test
    void testCreateAndFindById() {
        commentRepository.save(comment1);
        Comment comment = commentRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException(String.format("Comment with id %x not found!", 1L)));
        assertEquals(comment1, comment);
    }

    @Test
    void testUpdate() {
        commentRepository.save(comment1);
        Comment comment = commentRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException(String.format("Comment with id %x not found!", 1L)));
        assertEquals(comment1, comment);
        comment.setText("Omegalul");
        commentRepository.save(comment1);
        comment = commentRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException(String.format("Comment with id %x not found!", 1L)));
        assertEquals(comment1, comment);
    }

    @Test
    void testFindAll() {
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        List<Comment> test = List.of(comment1, comment2, comment3);
        List<Comment> comments = commentRepository.findAll();
        assertEquals(test.size(), comments.size());
    }

    @Test
    void testDelete() {
        commentRepository.save(comment1);
        Comment comment = commentRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException(String.format("Comment with id %x not found!", 1L)));
        assertEquals(comment1, comment);
        commentRepository.delete(comment1);
        Optional<Comment> opt = commentRepository.findById(1L);
        assertTrue(opt.isEmpty());
    }

    @Test
    void testFindByItemId() {
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        List<Comment> comments = commentRepository.findByItemId(1L);
        assertEquals(2, comments.size());
    }
}
