import React from "react";
import '../index.css'
import  './DeliveryPanel.css'
import {getDeliveryTypes} from "../utils/api";
import {serverURL} from "../index";

class DeliveryRow extends React.Component {
    render() {
        return <tr>
            <td>{this.props.deliveryType.id}</td>
            <td>{this.props.deliveryType.description}</td>
            <td />
        </tr>
    }
}

class CreateForm extends React.Component {
    async create() {
        let url = new URL(`${serverURL}/delivery_types/add?login=${localStorage.getItem('login')}&password=${localStorage.getItem('password')}`)

        let description = document.forms.createForm.elements.description.value

        url.searchParams.set('description',  description)
        await fetch(url.toString(), {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify({})
        })
        document.getElementsByClassName('CreateForm-holder')[0].style.display = 'none'
    }

    render() {
        return <div className="CreateForm-holder">
            <div className="CreateForm">
                <header className="CreateForm-header">
                    Create book
                </header>
                <form name="createForm">
                    <label className="CreateForm-label" htmlFor="CreateForm-description-input">description: </label>
                    <input className="CreateForm-input" id="CreateForm-description-input" name="description"/>
                </form>

                <div className="CreateFrom-button" onClick={() => this.create()}>
                    create
                </div>
            </div>
        </div>
    }
}

export class DeliveryPanel extends React.Component {
    componentDidMount() {
        getDeliveryTypes().then(result => {
            this.setState({...this.state, deliveryTypes: result})
        })
    }

    constructor(props) {
        super(props)
        this.state = {
            deliveryTypes: [],
        }
    }

    openCreateForm() {
        let requestForm = document.getElementsByClassName('CreateForm-holder')[0]
        requestForm.style.display = 'block'
    }

    render() {
        return <div className="content-wrapper">
            <CreateForm />
            <div className="DeliveryPanel">
                <header className="DeliveryPanel-header">
                    Delivery types
                    <div className="DeliveryPanel-create-button" onClick={() => this.openCreateForm()}>Create</div>
                </header>
                <table className="DeliveryPanel-table">
                    <colgroup>
                        <col span="1" style={{width: "5%"}} />
                        <col span="1" style={{width: "25%"}} />
                        <col span="1" style={{width: "70%"}} />
                    </colgroup>

                    <tbody>
                    <tr>
                        <td>ID</td>
                        <td>Description</td>
                        <td />
                    </tr>
                    {this.state.deliveryTypes.map(item => <DeliveryRow deliveryType={item}/>)}
                    </tbody>
                </table>
            </div>
        </div>
    }
}
