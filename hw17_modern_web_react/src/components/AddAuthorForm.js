import React from "react";
import {AuthorService} from "../services/AuthorService";
import {Redirect} from "react-router-dom";

export default class AddGenreForm extends React.Component {
    constructor(props) {
        super(props);
        this.authorService = new AuthorService();
        this.state = {
            redirect: false,
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
                    this.setState({redirect: true});
                }
            }
        ).catch(error => alert(error.message));
    };

    render() {
        if (this.state.redirect) {
            return <Redirect push to="/authors" />;
        }
        const {author} = this.state;
        return (
            <div>
                <h4>Добавление информации об авторе</h4>
                <form onSubmit={this.addAuthor}>
                    <table className="paleblue">
                        <tbody>
                        <tr>
                            <td className="header">Имя</td>
                            <td><input name="firstName" placeholder="Enter firstName" value={author.firstName}
                                       onChange={this.handleChange}/></td>
                        </tr>
                        <tr>
                            <td className="header">Фамилия</td>
                            <td><input name="surname" placeholder="Enter surname" value={author.surname}
                                       onChange={this.handleChange}/></td>
                        </tr>
                        </tbody>
                    </table>
                    <div className="buttons">
                        <button type="submit">Добавить</button>
                    </div>
                </form>
            </div>
        );
    }

    handleChange = event => {
        const {name, value} = event.target;
        this.setState({
            author: {...this.state.author, [name]: value}
        });
    };
}