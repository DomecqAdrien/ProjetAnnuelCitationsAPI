package citation.controllers;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import citation.domain.Book;
import citation.services.BookService;

@RestController
@RequestMapping("/book")
public class BookController {
	
	@Resource BookService bookService;
	
	@GetMapping("/hw")
	public String HelloWorld() throws InterruptedException, ExecutionException{
		return "Hello World";
	}
	
	@GetMapping("/all")
	public List<Book> getAllCitations() throws InterruptedException, ExecutionException{
		return bookService.getAllBooks();
	}
	
	@GetMapping("/{id}")
	public Book getBook(@PathVariable int id) throws InterruptedException, ExecutionException {
		return bookService.getBookById(id);
	}
	
	@PostMapping("")
	public String addBook(@RequestBody Book book) throws InterruptedException, ExecutionException {
		return bookService.addBook(book);
	}

}
