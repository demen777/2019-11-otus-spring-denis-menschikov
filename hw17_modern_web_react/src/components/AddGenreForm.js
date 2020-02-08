import React from "react";

export default class AddGenreForm extends React.Component {
    state = {
        genreName: ''
    }

    addGenre = (event) => {
        event.preventDefault()
        console.log(event.target.value)
        console.log(this.state.genreName)
    }

    render() {
        const {genreName} = this.state
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
        const {name, value} = event.target
        this.setState({[name]: value})
    };
}