import React from "react";
import './App.css';
import {BrowserRouter, Redirect} from 'react-router-dom'

class App extends React.Component {
    

    render() {
        return <div className="App">
            <BrowserRouter>
                <Redirect to="/login"/>
            </BrowserRouter>

            <header className="App-header">
                <p>
                    Edit and save to reload.
                </p>
            </header>
        </div>
    }
}


export default App;
