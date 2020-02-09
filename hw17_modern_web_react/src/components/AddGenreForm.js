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
                    <div className="form-group">
                        <label>Наименование
                            <input name="genreName" className="form-control" placeholder="Enter name" value={genreName}
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
        this.setState({[name]: value})
    };
}