import React from "react";
import './ViewMenu.css'
import '../index.css'
import {ViewMenuItem} from './ViewMenuItem';
import {BrowserRouter, Link} from "react-router-dom";

import booksImg from './assets/books.svg'
import listImg from './assets/list.svg'
import statsImg from './assets/stats.svg'

export class ViewMenu extends React.Component {
    privilegedItems() {
        if (localStorage.getItem('user-role') === 'admin') {
            return <Link to="/stats">
                <ViewMenuItem image={statsImg} text="Stats" />
            </Link>
        }
    }

    render() {
        return <div className="ViewMenu">
            <div className="content-wrapper">
                <BrowserRouter>
                    <Link to="/books" style={{ textDecoration: 'none' }}>
                        <ViewMenuItem image={booksImg} text="Books" />
                    </Link>
                    <Link to="/requests">
                        <ViewMenuItem image={listImg} text="Requests" />
                    </Link>
                    {this.privilegedItems()}
                </BrowserRouter>
            </div>
        </div>
    }
}