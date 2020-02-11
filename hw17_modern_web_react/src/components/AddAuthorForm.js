import React from "react";
import {AuthorService} from "../services/AuthorService";

export default class AddGenreForm extends React.Component {
    constructor(props) {
        super(props);
        this.authorService = new AuthorService();
        this.state = {
            author: {
                firstName: "",
                surname: ""
            }
        }
    }

    addAuthor = event => {
        event.preventDefault();
        console.log(event.target.value);
        console.log(this.state.author);
        const jsonPromise = this.authorService.addAuthor(this.state.author);
        jsonPromise.then(newAuthor => {
                console.log("---" + newAuthor);
                if (newAuthor !== undefined) {
                    console.log(newAuthor);
                    window.location.href = "/authors";
                }
            }
        ).catch(error => alert(error.message));
    };

    render() {
        const {author} = this.state;
        return (
            <div>
                <h4>Добавление информации об авторе</h4>
                <form onSubmit={this.addAuthor}>
                    <div className="form-group">
                        <label>Имя
                            <input name="firstName" className="form-control" placeholder="Enter firstName" value={author.firstName}
                                   onChange={this.handleChange}/>
                        </label>
                    </div>
                    <div className="form-group">
                        <label>Фамилия
                            <input name="surname" className="form-control" placeholder="Enter surname" value={author.surname}
                                   onChange={this.handleChange}/>
                        </label>
                    </div>
                    <button type="submit" className="btn btn-primary">Добавить</button>
                </form>
            </div>
        );
    }

    handleChange = event => {
        const {name, value} = event.target;
        this.setState({
            author:{...this.state.author, [name]: value}});
    };
}