package library.userservice.controller;

import library.userservice.dto.BookDTO;
import library.userservice.service.BookClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final BookClientService bookClientService;

    @Autowired
    public UserController(BookClientService bookClientService) {
        this.bookClientService = bookClientService;
    }

    @GetMapping("/my-books")
    public ResponseEntity<List<BookDTO>> getMyBooks(@RequestHeader("Authorization") String authorizationHeader) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String token = authorizationHeader.substring(7); // Remove the "Bearer " prefix
        List<BookDTO> books = bookClientService.getBooksByUser(username, token);
        return ResponseEntity.ok(books);
    }
}