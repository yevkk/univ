import React from 'react'
import './Login.css'
import {serverURL} from "../utils/api";

export class Login extends React.Component {
    render() {
        return <div className="content-wrapper">
            <div className="Login-form">
                <header className="Login-form-header">
                    Welcome!
                </header>
                <form name="loginForm">
                    <label className="Login-form-label" htmlFor="Login-login-input">login: </label>
                    <input className="Login-form-input" id="Login-login-input" name="login" />
                    <label className="Login-form-label" htmlFor="Login-password-input">password: </label>
                    <input className="Login-form-input" id="Login-password-input" type="password" name="password" />
                </form>

                <div className="Login-button" onClick={this.login}>
                    log in
                </div>
            </div>
        </div>
    }

    async login() {
        let url = new URL(`${serverURL}/login`)

        let login = document.forms.loginForm.elements.login.value
        let password = document.forms.loginForm.elements.password.value

        url.searchParams.set('login',  login)
        url.searchParams.set('password', password)

        let response = await fetch(url.toString())

        let result = await response.json();

        if (result === null) {
            alert('login failed');
            return
        }

        localStorage.setItem('login', login)
        localStorage.setItem('password', password)
        localStorage.setItem('user-role', result)

        window.location.href = '/'

    }
}