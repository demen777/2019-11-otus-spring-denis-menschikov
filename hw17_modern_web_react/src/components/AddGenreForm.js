import React from "react";
import {GenreService} from "../services/GenreService";

export default class AddGenreForm extends React.Component {
    constructor(props) {
        super(props);
        this.genreService = new GenreService();
        this.state = {
            genreName: ''
        }
    }

    addGenre = event => {
        event.preventDefault();
        console.log(event.target.value);
        console.log(this.state.genreName);
        const jsonPromise = this.genreService.addGenre(this.state.genreName);
        jsonPromise.then(newGenre => {
                console.log("---" + newGenre);
                if (newGenre !== undefined) {
                    console.log(newGenre);
                    window.location.href = "/genres";
                }
            }
        ).catch(error => alert(error.message));
    };

    render() {
        const {genreName} = this.state;
        return (
            <div>
                <h4>Добавление информации о жанре</h4>
                <form onSubmit={this.addGenre}>
                    <table className="paleblue">
                        <tbody>
                        <tr>
                            <td className="header">Наименование</td>
                            <td><input name="genreName" placeholder="Enter name" value={genreName}
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
        this.setState({[name]: value})
    };
}