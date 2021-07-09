import React from "react";
import './App.css';
import {BrowserRouter, Route, Switch} from 'react-router-dom'
import Keycloak from 'keycloak-js'
import {UserView} from "../userview/UserView";
import {AdminView} from "../adminview/AdminView";

class App extends React.Component {
    constructor(props) {
        super(props)
        this.state = {keycloak: null, authenticated: false}
    }

    componentDidMount() {
        const keycloak = Keycloak('/keycloak.json')
        keycloak.init({onLoad: 'login-required'}).then(authenticated => {
            keycloak.loadUserInfo().then(userInfo => {
                localStorage.setItem('username', userInfo.name)
                localStorage.setItem('email', userInfo.email)
                localStorage.setItem('user-id', userInfo.sub)

                let isAdmin = keycloak.tokenParsed.realm_access.roles.includes('app-admin')
                localStorage.setItem('is-admin', isAdmin.toString())

                this.setState({keycloak, authenticated})
            })
        })
    }

    render() {
        if (this.state.keycloak) {
            if (this.state.authenticated) return (
                <div className="App">
                    <BrowserRouter>
                        <Switch>
                            <Route path="/">
                                {localStorage.getItem('is-admin') === 'false' ?
                                    <UserView keycloak={this.state.keycloak}/> :
                                    <AdminView keycloak={this.state.keycloak}/>}
                            </Route>
                        </Switch>
                    </BrowserRouter>
                </div>
            );
            else return (<div>Unable to authenticate</div>);
        }
        return (<div>Initializing Keycloak...</div>);
    }
}


export default App;
