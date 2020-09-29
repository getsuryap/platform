package com.context.springsecurity.controllers;

import com.context.springsecurity.domain.Books;
import com.context.springsecurity.payload.response.MessageResponse;
import com.context.springsecurity.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/books")
public class BooksController {
    @Autowired
    private BookRepository bookRepository;

    @PostMapping("/")
    @ResponseBody
    public ResponseEntity<?> createNewBook(@Valid @RequestBody Books bookRequest) {
        if (bookRepository.existsByName(bookRequest.getName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Book with the same name already exist!"));
        }

        if (bookRepository.existsByMssIdn(bookRequest.getMssIdn())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Another book with the same MSSIDN is already exist!"));
        }
        Books book = new Books(bookRequest.getName(), bookRequest.getMssIdn(), bookRequest.getAuthor(), bookRequest.getPublisher());

        bookRepository.save(book);

        return ResponseEntity.ok(new MessageResponse("Book Added successfully!"));
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    List<Books> all() {
        return bookRepository.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    Books one(@PathVariable Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    Books replaceBook(@RequestBody Books newBook, @PathVariable Long id) {

        return bookRepository.findById(id)
                .map(book -> {
                    book.setName(newBook.getName() == null ? book.getName() : newBook.getName());
                    book.setMssIdn(newBook.getMssIdn() == null ? book.getMssIdn() : newBook.getMssIdn().toUpperCase(Locale.ROOT));
                    book.setAuthor(newBook.getAuthor() == null ? book.getAuthor() : newBook.getAuthor());
                    book.setPublisher(newBook.getPublisher() == null ? book.getPublisher() : newBook.getPublisher());
                    return bookRepository.save(book);
                })
                .orElseGet(() -> {
                    newBook.setId(id);
                    this.createNewBook(newBook);
                    return newBook;
                });
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    void deleteEmployee(@PathVariable Long id) {
        bookRepository.deleteById(id);
    }
}
