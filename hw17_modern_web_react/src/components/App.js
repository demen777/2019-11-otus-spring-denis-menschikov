import React from 'react';
import {
    Route,
    Redirect, BrowserRouter
} from "react-router-dom";
import Books from "./Books";
import Authors from "./Authors";
import Genres from "./Genres";
import AddGenreForm from "./AddGenreForm";
import 'bootstrap/dist/css/bootstrap.css'
import AddBookForm from "./AddBookForm";
import EditBookForm from "./EditBookForm";
import ViewBook from "./ViewBook";
import AddAuthorForm from "./AddAuthorForm";
import Navigation from "./Navigation";

export default class App extends React.Component {
    render() {
        return (
            <BrowserRouter>
                    <Navigation/>
                    <div className="content">
                        <Route exact path="/">
                            <Redirect to="/books"/>
                        </Route>
                        <Route path="/books" component={Books}/>
                        <Route path="/book/add" component={AddBookForm}/>
                        <Route path="/book/edit/:bookId" component={EditBookForm}/>
                        <Route path="/book/view/:bookId" component={ViewBook}/>
                        <Route path="/authors" component={Authors}/>
                        <Route path="/author/add" component={AddAuthorForm}/>
                        <Route path="/genres" component={Genres}/>
                        <Route path="/genre/add" component={AddGenreForm}/>
                    </div>
            </BrowserRouter>
        );
    }
}