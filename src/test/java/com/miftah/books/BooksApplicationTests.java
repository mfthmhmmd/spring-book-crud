package com.miftah.books;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miftah.books.models.Book;
import com.miftah.books.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest
@AutoConfigureMockMvc
class BooksApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    public void setUp() {
        Book book1 = new Book();
        Book book2 = new Book();
        Book book3 = new Book();

        book1.setTitle("The Great Gatsby");
        book1.setAuthor("F. Scott Fitzgerald");
        book1.setYearPublished(1925);
        bookRepository.save(book1);

        book2.setTitle("Moby Dick");
        book2.setAuthor("Herman Melville");
        book2.setYearPublished(1851);
        bookRepository.save(book2);

        book3.setTitle("Learn Java in One Day and Learn It Well");
        book3.setAuthor("Jamie Chan");
        book3.setYearPublished(2016);
        bookRepository.save(book3);

    }

    @Test
    void contextLoads() {
    }

    @Test
    public void testGetAllBooks() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/books"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void testCreateNewBook() throws Exception {
        Book bookTest = new Book();
        bookTest.setTitle("Test");
        bookTest.setAuthor("Author Test");
        bookTest.setYearPublished(1990);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookTest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void testCreateNewBookValidation() throws Exception {
        Book bookTest = new Book();
        bookTest.setTitle("Test");
        bookTest.setYearPublished(1990);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookTest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void testUpdateBook() throws Exception {
        Book book = bookRepository.findById(1L).get();
        book.setTitle("Test Update title");

        this.mockMvc.perform(MockMvcRequestBuilders.put("/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
