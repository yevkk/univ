import React from "react";
import './AdminBookPanel.css'
import {getBooks, getDeliveryTypes, getStats} from "../../api/api";
import {serverURL} from "../../index";

let selectedBookID = 0;

class BookRow extends React.Component {
    bookID

    openUpdateForm() {
        let requestForm = document.getElementsByClassName('UpdateForm-holder')[0]
        selectedBookID = this.bookID
        requestForm.style.display = 'block'
    }

    constructor(props) {
        super(props);
        this.bookID = this.props.book.id
    }

    render() {
        return <tr>
            <td>{this.props.book.id}</td>
            <td>{this.props.book.name}</td>
            <td>{this.props.book.author}</td>
            <td>{this.props.book.lang}</td>
            <td>{this.props.book.tags.join(', ')}</td>
            <td>{this.props.book.stats.amount}</td>
            <td>{this.props.book.stats.totalRequests}</td>
            <td>{this.props.book.stats.rate}</td>
            <td> <div className="AdminBooksPanel-update-button" onClick={() => this.openUpdateForm()}>update</div> </td>
        </tr>
    }
}

class UpdateForm extends React.Component {
    async updateAmount() {
        let url = new URL(`${serverURL}/stats/update?login=${localStorage.getItem('login')}&password=${localStorage.getItem('password')}`)

        let amount = document.forms.updateForm.elements.amount.value
        url.searchParams.set('book_id',  selectedBookID)
        url.searchParams.set('amount',  amount)
        await fetch(url.toString(), {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify({})
        })
        document.getElementsByClassName('UpdateForm-holder')[0].style.display = 'none'
    }

    render() {
        return <div className="UpdateForm-holder">
            <div className="UpdateForm">
                <header className="UpdateForm-header">
                    Update book
                </header>
                <form name="updateForm">
                    <label className="UpdateForm-label" htmlFor="UpdateForm-amount-input">amount: </label>
                    <input className="UpdateForm-input" id="UpdateForm-amount-input" name="amount" type="number" min="0" max="100" step="1"/>
                </form>

                <div className="UpdateForm-button" onClick={() => this.updateAmount()}>
                    send
                </div>
            </div>
        </div>
    }
}

class CreateForm extends React.Component {
    async create() {
        let url = new URL(`${serverURL}/books/add?login=${localStorage.getItem('login')}&password=${localStorage.getItem('password')}`)

        let name = document.forms.createForm.elements.name.value
        let author = document.forms.createForm.elements.author.value
        let lang = document.forms.createForm.elements.lang.value
        let tags = document.forms.createForm.elements.tags.value.split(',')

        url.searchParams.set('name',  name)
        url.searchParams.set('author',  author)
        url.searchParams.set('lang',  lang)
        tags.forEach(tag => url.searchParams.set('tag',  tag))

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
                    <label className="CreateForm-label" htmlFor="CreateForm-name-input">name: </label>
                    <input className="CreateForm-input" id="CreateForm-name-input" name="name"/>
                    <label className="CreateForm-label" htmlFor="CreateForm-author-input">author: </label>
                    <input className="CreateForm-input" id="CreateForm-author-input" name="author"/>
                    <label className="CreateForm-label" htmlFor="CreateForm-lang-input">lang: </label>
                    <input className="CreateForm-input" id="CreateForm-lang-input" name="lang"/>
                    <label className="CreateForm-label" htmlFor="CreateForm-tags-input">tags: </label>
                    <input className="CreateForm-input" id="CreateForm-tags-input" name="tags"/>
                </form>

                <div className="CreateFrom-button" onClick={() => this.create()}>
                    create
                </div>
            </div>
        </div>
    }
}

export class AdminBooksPanel extends React.Component {
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

    openCreateForm() {
        let requestForm = document.getElementsByClassName('CreateForm-holder')[0]
        requestForm.style.display = 'block'
    }

    render () {
        return <div className="content-wrapper">
            <UpdateForm />
            <CreateForm />
            <div className="BooksPanel AdminBooksPanel">
                <header className="BooksPanel-header">
                    Books
                    <div className="AdminBooksPanel-update-button AdminBooksPanel-create-button" onClick={() => this.openCreateForm()}>Create</div>
                </header>
                <table className="BooksPanel-table AdminBooksPanel-table">
                    <colgroup>
                        <col span="1" style={{width: "5%"}} />
                        <col span="1" style={{width: "20%"}} />
                        <col span="1" style={{width: "15%"}} />
                        <col span="1" style={{width: "15%"}} />
                        <col span="1" style={{width: "20%"}} />
                        <col span="1" style={{width: "5%"}} />
                        <col span="1" style={{width: "5%"}} />
                        <col span="1" style={{width: "5%"}} />
                        <col span="1" style={{width: "15%"}} />
                    </colgroup>

                    <tbody>
                    <tr>
                        <td>ID</td>
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