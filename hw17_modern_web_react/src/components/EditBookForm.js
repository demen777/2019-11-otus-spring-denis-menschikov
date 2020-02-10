import React from "react";
import {BookService} from "../services/BookService";
import {GenreService} from "../services/GenreService";
import {AuthorService} from "../services/AuthorService";

export default class EditBookForm extends React.Component {
    constructor(props) {
        super(props);
        console.log(props);
        const book = props.location.book;
        this.bookService = new BookService();
        this.genreService = new GenreService();
        this.authorService = new AuthorService();
        this.state = {
            bookId: book.id,
            book:{
                name: book.name,
                authorId: book.author.id,
                genreId: book.genre.id
            },
            genres: [],
            authors: []
        }
    }

    componentDidMount = () => {
        this.getGenres();
        this.getAuthors();
    };

    getGenres() {
        this.genreService.getAll()
            .then(genres => {
                console.log(genres);
                if (genres !== undefined) {
                    this.setState({genres: genres});
                }
            })
            .catch(error => console.log(error));
    }

    getAuthors() {
        this.authorService.getAll()
            .then(authors => {
                console.log(authors);
                if (authors !== undefined) {
                    this.setState({authors: authors});
                }
            })
            .catch(error => console.log(error));
    }

    editBook = event => {
        event.preventDefault();
        console.log(event.target.value);
        const jsonPromise = this.bookService.editBook(this.state.bookId, this.state.book);
        jsonPromise.then(editedBook => {
                console.log("---" + editedBook);
                if (editedBook !== undefined) {
                    console.log(editedBook);
                    window.location.href = "/books";
                }
            }
        ).catch(error => alert(error.message));
    };

    render() {
        const {bookId, book, genres, authors} = this.state;
        return (
            <div>
                <h4>Изменение информации о книге</h4>
                <form onSubmit={this.editBook}>
                    <div className="form-group">
                        <label>ID<span>{bookId}</span>
                        </label>
                        <label>Наименование
                            <input name="name" className="form-control" placeholder="Enter name" value={book.name}
                                   onChange={this.handleChange}/>
                        </label>
                        <label>Жанр
                            <select name="genreId" value={book.genreId} onChange={this.handleChange}>
                                {genres.map((genre) =>
                                    <option key={genre.id} value={genre.id}>{genre.name}</option>
                                )}
                            </select>
                        </label>
                        <label>Автор
                            <select name="authorId" value={book.authorId} onChange={this.handleChange}>
                                {authors.map((author) =>
                                    <option key={author.id} value={author.id}>{author.name}</option>
                                )}
                            </select>
                        </label>
                    </div>
                    <button type="submit" className="btn btn-primary">Изменить</button>
                </form>
            </div>
        );
    }

    handleChange = event => {
        const {name, value} = event.target;
        this.setState({
            book:{...this.state.book, [name]: value}});
    };
}