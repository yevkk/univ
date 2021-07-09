import React from "react";
import {ViewHeader} from "../view-header/ViewHeader";
import {ViewMenu} from "../view-menu/ViewMenu";
import {BrowserRouter, Route, Switch} from "react-router-dom";
import {AdminBooksSection} from "../main-section/books/AdminBooksSection";
import {AdminRequestsSection} from "../main-section/requests/AdminRequestsSection";
import {DeliveryTypeSection} from "../main-section/delivery/DeliveryTypeSection";
import {BalanceLogSection} from "../main-section/history/BalanceLogSection";

export class AdminView extends React.Component {
    render() {
        return <div className="AdminView printable">
            <ViewHeader keycloak={this.props.keycloak}/>
            <BrowserRouter>
                <ViewMenu />
                <Switch>
                    <Route path="/books">
                        <AdminBooksSection />
                    </Route>
                    <Route path="/requests">
                        <AdminRequestsSection />
                    </Route>
                    <Route path="/delivery">
                        <DeliveryTypeSection />
                    </Route>
                    <Route path="/balance_log">
                        <BalanceLogSection />
                    </Route>
                </Switch>
            </BrowserRouter>
        </div>
    }
}
