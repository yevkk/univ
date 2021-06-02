import React from "react";
import '../RequestsPanel.css'
import './UserRequestsPanel.css'
import {getBooks, getRequests, getReturnRequests, getDeliveryTypes} from "../../api/api-for-user";

class RequestRow extends React.Component {
    convertDatetime(datetime) {
        let str = ""
        str += `${datetime.date.year}-${datetime.date.month}-${datetime.date.day} `
        str += `${datetime.time.hour}:${datetime.time.minute}`
        return str
    }

    render() {
        return <tr>
            <td>{this.convertDatetime(this.props.request.datetime)}</td>
            <td>{this.props.request.bookName}</td>
            <td>{this.props.request.deliveryTypeText}</td>
            <td>{this.props.request.contact}</td>
            <td>{this.props.request.state}</td>
            {this.props.request.state === 'PROCESSED' ? <td> <div className="UserRequestsPanel-return-button">return</div> </td> : <td />}
            {this.props.request.returnRequest !== undefined ? <td>{this.convertDatetime(this.props.request.returnRequest.datetime)}</td> : <td/>}
            {this.props.request.returnRequest !== undefined ?  <td>{this.props.request.returnRequest.state}</td> : <td/>}
        </tr>
    }
}

export class UserRequestsPanel extends React.Component {
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
            <div className="RequestsPanel UserRequestsPanel">
                <header className="RequestsPanel-header">
                    Requests
                </header>
                <table className="RequestsPanel-table UserRequestsPanel-table">
                    <colgroup>
                        <col span="1" style={{width: "15%"}} />
                        <col span="1" style={{width: "15%"}} />
                        <col span="1" style={{width: "15%"}} />
                        <col span="1" style={{width: "10%"}} />
                        <col span="1" style={{width: "10%"}} />
                        <col span="1" style={{width: "10%"}} />
                        <col span="1" style={{width: "15%"}} />
                        <col span="1" style={{width: "10%"}} />
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
                        <td>State</td>
                    </tr>
                    {this.state.requests.map(request => <RequestRow request={request}/>)}
                    </tbody>
                </table>
            </div>
        </div>
    }
}

