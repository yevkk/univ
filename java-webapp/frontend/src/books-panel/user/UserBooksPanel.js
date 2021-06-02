import React from "react";
import '../BooksPanel.css'
import './UserBooksPanel.css'
import {getBooks, getDeliveryTypes, getStats} from "../../api/api";
import {serverURL} from "../../index";

let selectedBookID = 0;

class BookRow extends React.Component {
    bookID

    openRequestForm() {
        let requestForm = document.getElementsByClassName('RequestForm-holder')[0]
        selectedBookID = this.bookID
        requestForm.style.display = 'block'
    }

    constructor(props) {
        super(props);
        this.bookID = this.props.book.id
    }

    render() {
        return <tr>
            <td>{this.props.book.name}</td>
            <td>{this.props.book.author}</td>
            <td>{this.props.book.lang}</td>
            <td>{this.props.book.tags.join(', ')}</td>
            <td>{this.props.book.stats.amount}</td>
            <td>{this.props.book.stats.totalRequests}</td>
            <td>{this.props.book.stats.rate.toFixed(2)}</td>
            <td> <div className="UserBooksPanel-request-button" onClick={() => this.openRequestForm()}>create request</div> </td>
        </tr>
    }
}

class DeliveryTypeOption extends React.Component {
    render() {
        return <option value={this.props.deliveryType.id}>{this.props.deliveryType.description}</option>
    }
}

class RequestForm extends React.Component {
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

    async sendRequest() {
        let url = new URL(`${serverURL}/request/add?login=${localStorage.getItem('login')}&password=${localStorage.getItem('password')}`)

        let deliveryTypeID = document.forms.requestForm.elements.deliveryTypeID.value
        let contact = document.forms.requestForm.elements.contact.value
        url.searchParams.set('book_id',  selectedBookID)
        url.searchParams.set('delivery_type_id',  deliveryTypeID)
        url.searchParams.set('contact', contact)
        await fetch(url.toString(), {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify({})
        })
        document.getElementsByClassName('RequestForm-holder')[0].style.display = 'none'
    }

    render() {
        return <div className="RequestForm-holder">
            <div className="RequestForm">
                <header className="RequestForm-header">
                    Request book
                </header>
                <form name="requestForm">
                    <label className="RequestForm-label" htmlFor="RequestForm-deliveryTypeID-input">login: </label>
                    <select className="RequestForm-input" id="RequestForm-deliveryTypeID-input" name="deliveryTypeID">
                        {this.state.deliveryTypes.map(item => <DeliveryTypeOption deliveryType={item} />)}
                    </select>
                    <label className="RequestForm-label" htmlFor="RequestForm-contact-input">contact: </label>
                    <input className="RequestForm-input" id="RequestForm-contact-input" name="contact" />
                </form>

                <div className="RequestForm-button" onClick={() => this.sendRequest()}>
                    send
                </div>
            </div>
        </div>
    }
}

export class UserBooksPanel extends React.Component {
    componentDidMount() {
        let books;

        getBooks().then(result => {
            books = result
            getStats().then(result => {
                let stats = result;
                books.forEach(book => book.stats = stats.find(item => item.bookID === book.id))
                this.setState({...this.state, books})
            })
        })
    }

    constructor(props) {
        super(props)
        this.state = {
            books: [],
        }
    }

    render() {
        return <div className="content-wrapper">
            <RequestForm id="BooksPanel-request-form" />
            <div className="BooksPanel UserBooksPanel">
                <header className="BooksPanel-header">
                    Books
                </header>
                <table className="BooksPanel-table UserBooksPanel-table">
                    <colgroup>
                        <col span="1" style={{width: "20%"}} />
                        <col span="1" style={{width: "20%"}} />
                        <col span="1" style={{width: "10%"}} />
                        <col span="1" style={{width: "20%"}} />
                        <col span="1" style={{width: "5%"}} />
                        <col span="1" style={{width: "5%"}} />
                        <col span="1" style={{width: "5%"}} />
                        <col span="1" style={{width: "15%"}} />
                    </colgroup>

                    <tbody>
                    <tr>
                        <td>Name</td>
                        <td>Author</td>
                        <td>Language</td>
                        <td>Tags</td>
                        <td>Amount</td>
                        <td>Readers</td>
                        <td>Rate</td>
                        <td></td>
                    </tr>
                    {this.state.books.map(book => <BookRow book={book}/>)}
                    </tbody>
                </table>
            </div>
        </div>
    }
}

