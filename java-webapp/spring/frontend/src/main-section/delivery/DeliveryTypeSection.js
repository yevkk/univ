import React from "react";
import  '../MainSection.css';
import  '../OverlayForm.css';
import {getDeliveryTypes, serverURL} from "../../utils/api";

class DeliveryRow extends React.Component {
    render() {
        return <tr>
            <td className="centralized">{this.props.deliveryType.id}</td>
            <td>{this.props.deliveryType.description}</td>
            <td className="white" />
        </tr>
    }
}

class CreateDeliveryTypeForm extends React.Component {
    close() {
        document.getElementsByClassName('CreateDeliveryTypeForm-holder')[0].style.display = 'none'
    }

    async create() {
        let url = new URL(`${serverURL}/delivery_types`)

        let description = document.forms.createDeliveryTypeForm.elements.description.value

        url.searchParams.set('description',  description)
        await fetch(url.toString(), {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify({})
        })
        this.close()
        window.location.reload(false);
    }

    render() {
        return <div className="OverlayForm-holder CreateDeliveryTypeForm-holder">
            <div className="OverlayForm">
                <header className="OverlayForm-header">
                    Create book
                </header>
                <div className="OverlayForm-close-button" onClick={() => this.close()}>
                    X
                </div>
                <form name="createDeliveryTypeForm">
                    <label className="OverlayForm-label" htmlFor="CreateDeliveryTypeForm-description-input">description: </label>
                    <input className="OverlayForm-input" id="CreateDeliveryTypeForm-description-input" name="description"/>
                </form>

                <div className="OverlayForm-button" onClick={() => this.create()}>
                    create
                </div>
            </div>
        </div>
    }
}

export class DeliveryTypeSection extends React.Component {
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
        let requestForm = document.getElementsByClassName('CreateDeliveryTypeForm-holder')[0]
        requestForm.style.display = 'block'
    }

    render() {
        return <div className="content-wrapper">
            <CreateDeliveryTypeForm />
            <section className="MainSection">
                <header className="MainSection-header">
                    Delivery types
                    <div className="MainSection-header-button" onClick={() => this.openCreateForm()}>Create</div>
                </header>
                <table className="MainSection-table">
                    <colgroup>
                        <col span="1" style={{width: "5%"}} />
                        <col span="1" style={{width: "25%"}} />
                        <col span="1" style={{width: "70%"}} />
                    </colgroup>

                    <tbody>
                    <tr>
                        <td>ID</td>
                        <td>Description</td>
                        <td className="white" />
                    </tr>
                    {this.state.deliveryTypes.map(item => <DeliveryRow deliveryType={item}/>)}
                    </tbody>
                </table>
            </section>
        </div>
    }
}
