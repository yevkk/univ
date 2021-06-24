import React from "react";
import '../MainSection.css'
import '../OverlayForm.css'
import {getBooks, getRequests, getReturnRequests, getDeliveryTypes, serverURL} from "../../utils/api";
import {convertDatetime} from "../../utils/utils";

let selectedRequestID;

class RequestRow extends React.Component {
    requestID

    openRequestForm() {
        let returnRequestForm = document.getElementsByClassName('ReturnRequestForm-holder')[0]
        selectedRequestID = this.requestID
        returnRequestForm.style.display = 'block'
    }

    constructor(props) {
        super(props);
        this.requestID = this.props.request.id
    }

    render() {
        return <tr>
            <td className="centralized">{convertDatetime(this.props.request.datetime)}</td>
            <td className="centralized">{this.props.request.bookName}</td>
            <td>{this.props.request.deliveryTypeText}</td>
            <td>{this.props.request.contact}</td>
            <td className="centralized">{this.props.request.state}</td>
            {this.props.request.state === 'PROCESSED' ? <td>
                <div className="MainSection-button" onClick={() => this.openRequestForm()}>return</div>
            </td> : <td/>}
            {this.props.request.returnRequest !== undefined ?
                <td className="centralized">{convertDatetime(this.props.request.returnRequest.datetime)}</td> : <td/>}
            {this.props.request.returnRequest !== undefined ? <td className="centralized">{this.props.request.returnRequest.state}</td> : <td/>}
        </tr>
    }
}

class ReturnRequestForm extends React.Component {
    close() {
        document.getElementsByClassName('ReturnRequestForm-holder')[0].style.display = 'none'
    }

    async sendReturnRequest() {
        let url = new URL(`${serverURL}/return_request/add?login=${localStorage.getItem('login')}&password=${localStorage.getItem('password')}`)

        let rate = document.forms.returnRequestForm.elements.rate.value

        url.searchParams.set('request_id', selectedRequestID)
        url.searchParams.set('rate', rate)

        await fetch(url.toString(), {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify({})
        })
        this.close();
        window.location.reload(false);
    }

    render() {
        return <div className="OverlayForm-holder ReturnRequestForm-holder">
            <div className="OverlayForm">
                <header className="OverlayForm-header">
                    Request book
                </header>
                <div className="OverlayForm-close-button" onClick={() => this.close()}>
                    X
                </div>
                <form name="returnRequestForm">
                    <label className="OverlayForm-label" htmlFor="ReturnRequestForm-rate-input">rate: </label>
                    <input className="OverlayForm-input" id="ReturnRequestForm-rate-input" name="rate" type="number" min="0" max="5" step="0.1"/>
                </form>

                <div className="OverlayForm-button" onClick={() => this.sendReturnRequest()}>
                    send
                </div>
            </div>
        </div>
    }
}

export class UserRequestsSection extends React.Component {
    componentDidMount() {
        let requests;

        getRequests().then(result => {
            requests = result
            getReturnRequests().then(result => {
                let returnRequests = result;
                requests.forEach(request => request.returnRequest = returnRequests.find(item => item.getBookRequestID === request.id))
                getBooks().then(result => {
                    let books = result;
                    requests.forEach(request => request.bookName = books.find(item => item.id === request.bookID).name)
                    getDeliveryTypes().then(result => {
                        let deliveryTypes = result;
                        requests.forEach(request => request.deliveryTypeText = deliveryTypes.find(item => item.id === request.deliveryTypeID).description)
                        this.setState({...this.state, requests})
                    })
                })
            })
        })
    }

    constructor(props) {
        super(props)
        this.state = {
            requests: [],
        }
    }

    render() {
        return <div className="content-wrapper">
            <ReturnRequestForm />
            <section className="MainSection">
                <header className="MainSection-header">
                    Requests
                </header>
                <table className="MainSection-table">
                    <colgroup>
                        <col span="1" style={{width: "15%"}}/>
                        <col span="1" style={{width: "15%"}}/>
                        <col span="1" style={{width: "15%"}}/>
                        <col span="1" style={{width: "10%"}}/>
                        <col span="1" style={{width: "10%"}}/>
                        <col span="1" style={{width: "10%"}}/>
                        <col span="1" style={{width: "15%"}}/>
                        <col span="1" style={{width: "10%"}}/>
                    </colgroup>

                    <tbody>
                    <tr>
                        <td>Datetime</td>
                        <td>Book</td>
                        <td>Delivery</td>
                        <td>Contact</td>
                        <td>State</td>
                        <td/>
                        <td>Return Datetime</td>
                        <td>Return state</td>
                    </tr>
                    {this.state.requests.map(request => <RequestRow request={request}/>)}
                    </tbody>
                </table>
            </section>
        </div>
    }
}

