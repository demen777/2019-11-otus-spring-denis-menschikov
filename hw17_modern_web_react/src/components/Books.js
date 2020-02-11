import React, {Fragment} from "react";
import {BookService} from "../services/BookService";
import {Link} from "react-router-dom";
import {removeById} from "../utils/Misc";
import ActionList from "./ActionList/ActionList";

export default class Books extends React.Component {
    constructor(props) {
        super(props);
        this.bookService = new BookService();
        this.state = {
            books: []
        }
    }

    componentDidMount = () => {
        this.getBooks();
    };

    getBooks() {
        this.bookService.getAll()
            .then(books => {
                if (books !== undefined) {
                    this.setState({books: books});
                }
            })
            .catch(error => console.log(error));
    }

    deleteBook = (event, bookId) => {
        event.preventDefault();
        if (window.confirm("Вы действительно хотите удалить книгу с id=" + bookId)) {
            this.bookService.deleteBook(bookId)
                .then(() => {
                    const newBookList = removeById(this.state.books, bookId);
                    console.log(newBookList);
                    this.setState({books: newBookList});
                })
                .catch(error => console.log(error));
        }
    };

    render() {
        const {books} = this.state;
        // noinspection JSUnresolvedVariable
        return (
            <Fragment>
                <ActionList actions={[{url:"/book/add", text: "Добавить"}]}/>
                <h4>Список книг</h4>
                <table className="table table-striped">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Название</th>
                        <th>Автор</th>
                        <th>Жанр</th>
                        <th>Действия</th>
                    </tr>
                    </thead>
                    <tbody>
                    {books.map((book) =>
                        <tr key={book.id}>
                            <td>{book.id}</td>
                            <td>{book.name}</td>
                            <td>{book.author.name}</td>
                            <td>{book.genre.name}</td>
                            <td>
                                <Link to={"/book/view/" + book.id}>Просмотр</Link>
                                <Link to={"/book/edit/" + book.id}>Изменить</Link>
                                <Link to="" onClick={event => this.deleteBook(event, book.id)}>Удалить</Link>
                            </td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </Fragment>
        );
    }
}