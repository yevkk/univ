import React from "react";
import './App.css';
import {BrowserRouter, Redirect, Route, Switch} from 'react-router-dom'
import {Login} from "../login/Login";
import {UserView} from "../userview/UserView";
import {AdminView} from "../adminview/AdminView";

class App extends React.Component {
    loginRedirect() {
        if (!localStorage.getItem('login') || !localStorage.getItem('password') || !localStorage.getItem('user-role')) {
            return <Redirect to="/login"/>
        }
    }

    render() {
        return <div className="App" >
            <BrowserRouter>
                {this.loginRedirect()}
                <Switch>
                    <Route path="/login">
                        <Login/>
                    </Route>
                    <Route path="/">
                        {localStorage.getItem('user-role') === 'user' ? <UserView/> : <AdminView/>}
                    </Route>
                </Switch>
            </BrowserRouter>
        </div>
    }
}


export default App;
