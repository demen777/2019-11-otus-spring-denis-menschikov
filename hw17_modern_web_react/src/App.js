import React from 'react';
import {
    Route,
    NavLink,
    HashRouter
} from "react-router-dom";
import Books from "./Books";
import Authors from "./Authors";
import Genres from "./Genres";

class App extends React.Component {
    render() {
        return (
            <HashRouter>
            <div className="App">
                <nav className="menu">
                    <NavLink to="/">Книги</NavLink>
                    <NavLink to="/authors">Авторы</NavLink>
                    <NavLink to="/genres">Жанры</NavLink>
                </nav>
                <div className="content">
                    <Route exact path="/" component={Books}/>
                    <Route path="/authors" component={Authors}/>
                    <Route path="/genres" component={Genres}/>
                </div>
            </div>
            </HashRouter>
        );
    }
}

export default App;
