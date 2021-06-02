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
            return <Link to="/stats" onClick={() => window.location.reload()}>
                <ViewMenuItem image={statsImg} text="Stats" />
            </Link>
        }
    }

    render() {
        return <div className="ViewMenu">
            <div className="content-wrapper">
                    <Link to="/books">
                        <ViewMenuItem image={booksImg} text="Books" />
                    </Link>
                    <Link to="/requests">
                        <ViewMenuItem image={listImg} text="Requests" />
                    </Link>
                    {this.privilegedItems()}
            </div>
        </div>
    }
}