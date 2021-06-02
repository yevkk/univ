import React from "react";
import {ViewHeader} from "../view-header/ViewHeader";
import {ViewMenu} from "../view-menu/ViewMenu";
import {Switch, Route, BrowserRouter} from "react-router-dom";
import {UserBooksPanel} from "../books-panel/user/UserBooksPanel";
import {UserRequestsPanel} from "../requests-panel/user/UserRequestsPanel";

export class UserView extends React.Component {
    render() {
        return <div className="UserView">
            <ViewHeader/>
            <BrowserRouter>
            <ViewMenu/>
                <Switch>
                    <Route path="/books">
                        <UserBooksPanel/>
                    </Route>
                    <Route path="/requests">
                        <UserRequestsPanel />
                    </Route>
                </Switch>
            </BrowserRouter>
        </div>
    }
}