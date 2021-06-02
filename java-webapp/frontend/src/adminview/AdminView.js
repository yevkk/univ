import React from "react";
import {ViewHeader} from "../view-header/ViewHeader";
import {ViewMenu} from "../view-menu/ViewMenu";
import {BrowserRouter, Route, Switch} from "react-router-dom";
import {AdminBooksPanel} from "../books-panel/admin/AdminBooksPanel";

export class AdminView extends React.Component {
    render() {
        return <div className="AdminView">
            <ViewHeader/>
            <BrowserRouter>
                <ViewMenu/>
                <Switch>
                    <Route path="/books">
                        <AdminBooksPanel/>
                    </Route>
                    <Route path="/requests">
                        {/*<UserRequestsPanel />*/}
                    </Route>
                </Switch>
            </BrowserRouter>
        </div>
    }
}
