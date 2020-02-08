import React from 'react';
import {
    Route,
    NavLink,
    Redirect, BrowserRouter
} from "react-router-dom";
import Books from "./Books";
import Authors from "./Authors";
import Genres from "./Genres";
import AddGenreForm from "./AddGenreForm";
import 'bootstrap/dist/css/bootstrap.css'

export default class App extends React.Component {
    render() {
        return (
            <BrowserRouter>
                <div className="App">
                    <nav className="nav nav-pills">
                        <NavLink className="nav-link" to="/books">Книги</NavLink>
                        <NavLink className="nav-link" to="/authors">Авторы</NavLink>
                        <NavLink className="nav-link" to="/genres">Жанры</NavLink>
                    </nav>
                    <div className="content">
                        <Route exact path="/">
                            <Redirect to="/books"/>
                        </Route>
                        <Route path="/books" component={Books}/>
                        <Route path="/authors" component={Authors}/>
                        <Route path="/genres" component={Genres}/>
                        <Route path="/genre/add" component={AddGenreForm}/>
                    </div>
                </div>
            </BrowserRouter>
        );
    }
}