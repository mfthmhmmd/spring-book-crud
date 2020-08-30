package com.miftah.books.controllers;

import com.miftah.books.models.Book;
import com.miftah.books.repositories.BookRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController extends BaseController {

    private final BookRepository bookRepository;

    BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    List<Book> getAll() {
        return this.bookRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @PostMapping
    Book createBook(@Valid @RequestBody Book newBook) {
        return this.bookRepository.save(newBook);
    }

    @PutMapping("/{id}")
    ResponseEntity<Book> updateBook(@PathVariable(value = "id") Long id, @Valid @RequestBody Book bookDetails) {
        Optional<Book> optionalBook = this.bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.setAuthor(bookDetails.getAuthor());
            book.setTitle(bookDetails.getTitle());
            book.setYearPublished(bookDetails.getYearPublished());
            return ResponseEntity.ok(this.bookRepository.save(book));
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("/{id}")
    ResponseEntity<Book> deleteBook(@PathVariable(value = "id") Long id) {
        Optional<Book> book = this.bookRepository.findById(id);
        if (book.isPresent()) {
            Book deletedBook = book.get();
            this.bookRepository.delete(deletedBook);
            return ResponseEntity.accepted().body(deletedBook);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
