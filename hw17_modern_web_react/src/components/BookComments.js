import React from "react";
import {BookService} from "../services/BookService";

export default class BookComments extends React.Component {
    constructor(props) {
        super(props);
        console.log(props);
        this.bookService = new BookService();
        this.state = {
            bookId: props.bookId,
            bookComments: [],
            newCommentText: ""
        }
    }

    componentDidMount = () => {
        this.getBookComments();
    };

    getBookComments() {
        this.bookService.getBookComments(this.state.bookId)
            .then(bookComments => {
                console.log(bookComments);
                if (bookComments !== undefined) {
                    this.setState({bookComments: bookComments});
                }
            })
            .catch(error => console.log(error));
    }

    render() {
        const {bookComments, newCommentText} = this.state;
        return (
            <div>
                <h4>Комментарии</h4>
                <div className="form-group">
                    <table className="table table-striped">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Текст комментария</th>
                        </tr>
                        </thead>
                        <tbody>
                        {bookComments.map((bookComment) =>
                            <tr key={bookComment.id}>
                                <td>{bookComment.id}</td>
                                <td><pre>{bookComment.text}</pre></td>
                            </tr>
                        )}
                        </tbody>
                    </table>
                    <form onSubmit={this.addBookComment}>
                        <div className="form-group">
                                <textarea name="newCommentText" cols="100" rows="12"
                                          value={newCommentText} onChange={this.handleCommentTextChange}/>
                            <div className="buttons">
                                <button type="submit" className="btn btn-primary">Добавить</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        );
    }

    handleCommentTextChange = event => {
        const value = event.target.value;
        this.setState({newCommentText: value})
    };

    addBookComment = event => {
        event.preventDefault();
        console.log(event.target.value);
        console.log(this.state.newCommentText);
        const jsonPromise = this.bookService.addBookComment(this.state.bookId, this.state.newCommentText);
        jsonPromise.then(newBookComment => {
                console.log("---" + newBookComment);
                if (newBookComment !== undefined) {
                    // noinspection JSCheckFunctionSignatures
                    this.setState({bookComments: this.state.bookComments.concat(newBookComment)})
                }
            }
        ).catch(error => alert(error.message));
    };
}