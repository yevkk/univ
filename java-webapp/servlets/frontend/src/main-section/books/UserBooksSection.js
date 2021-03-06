import React from "react";
import '../MainSection.css'
import '../OverlayForm.css'
import {getBooks, getDeliveryTypes, getStats, serverURL} from "../../utils/api";

let selectedBookID = 0;

class BookRow extends React.Component {
    bookID

    openRequestForm() {
        let requestForm = document.getElementsByClassName('RequestBookForm-holder')[0]
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
            <td className="centralized">{this.props.book.lang}</td>
            <td>{this.props.book.tags.join(', ')}</td>
            <td className="centralized">{this.props.book.stats.amount}</td>
            <td className="centralized">{this.props.book.stats.totalRequests}</td>
            <td className="centralized">{this.props.book.stats.rate.toFixed(2)}</td>
            {this.props.book.stats.amount > 0 ?
            <td> <div className="MainSection-button" onClick={() => this.openRequestForm()}>create request</div> </td> : <td />}
        </tr>
    }
}

class DeliveryTypeOption extends React.Component {
    render() {
        return <option value={this.props.deliveryType.id}>{this.props.deliveryType.description}</option>
    }
}

class RequestBookForm extends React.Component {
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

    close() {
        document.getElementsByClassName('RequestBookForm-holder')[0].style.display = 'none'
    }

    async sendRequest() {
        let url = new URL(`${serverURL}/request/add?login=${localStorage.getItem('login')}&password=${localStorage.getItem('password')}`)

        let deliveryTypeID = document.forms.requestBookForm.elements.deliveryTypeID.value
        let contact = document.forms.requestBookForm.elements.contact.value
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
        this.close();
    }

    render() {
        return <div className="OverlayForm-holder RequestBookForm-holder">
            <div className="OverlayForm">
                <div className="OverlayForm-close-button" onClick={() => this.close()}>
                    X
                </div>
                <header className="OverlayForm-header">
                    Request book
                </header>
                <form name="requestBookForm">
                    <label className="OverlayForm-label" htmlFor="RequestBookForm-deliveryTypeID-input">login: </label>
                    <select className="OverlayForm-input" id="RequestBookForm-deliveryTypeID-input" name="deliveryTypeID">
                        {this.state.deliveryTypes.map(item => <DeliveryTypeOption deliveryType={item} />)}
                    </select>
                    <label className="OverlayForm-label" htmlFor="RequestBookForm-contact-input">contact: </label>
                    <input className="OverlayForm-input" id="RequestBookForm-contact-input" name="contact" />
                </form>

                <div className="OverlayForm-button" onClick={() => this.sendRequest()}>
                    send
                </div>
            </div>
        </div>
    }
}

export class UserBooksSection extends React.Component {
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
            <RequestBookForm id="BooksPanel-request-form" />
            <section className="MainSection">
                <header className="MainSection-header">
                    Books
                </header>
                <table className="MainSection-table">
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
                        <td/>
                    </tr>
                    {this.state.books.map(book => <BookRow book={book}/>)}
                    </tbody>
                </table>
            </section>
        </div>
    }
}

