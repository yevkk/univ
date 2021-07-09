import React from "react";
import '../MainSection.css'
import '../OverlayForm.css'
import {getBooks, getStats, serverURL} from "../../utils/api";

let selectedBookID = 0;

class BookRow extends React.Component {
    bookID

    openUpdateForm() {
        let requestForm = document.getElementsByClassName('UpdateBookForm-holder')[0]
        selectedBookID = this.bookID
        requestForm.style.display = 'block'
    }

    constructor(props) {
        super(props);
        this.bookID = this.props.book.id
    }

    render() {
        return <tr>
            <td className="centralized">{this.props.book.id}</td>
            <td>{this.props.book.name}</td>
            <td>{this.props.book.author}</td>
            <td className="centralized">{this.props.book.lang}</td>
            <td>{this.props.book.tags.join(', ')}</td>
            <td className="centralized">{this.props.book.stats.amount}</td>
            <td className="centralized">{this.props.book.stats.totalRequests}</td>
            <td className="centralized">{this.props.book.stats.rate.toFixed(2)}</td>
            <td> <div className="MainSection-button" onClick={() => this.openUpdateForm()}>update</div> </td>
        </tr>
    }
}

class UpdateBookForm extends React.Component {
    close() {
        document.getElementsByClassName('UpdateBookForm-holder')[0].style.display = 'none'
    }

    async updateAmount() {
        let url = new URL(`${serverURL}/stats/update?login=${localStorage.getItem('login')}&password=${localStorage.getItem('password')}`)

        let amount = document.forms.updateBookForm.elements.amount.value
        url.searchParams.set('book_id',  selectedBookID)
        url.searchParams.set('amount',  amount)
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
        return <div className="OverlayForm-holder UpdateBookForm-holder">
            <div className="OverlayForm">
                <header className="OverlayForm-header">
                    Update book
                </header>
                <div className="OverlayForm-close-button" onClick={() => this.close()}>
                    X
                </div>
                <form name="updateBookForm">
                    <label className="OverlayForm-label" htmlFor="UpdateBookForm-amount-input">amount: </label>
                    <input className="OverlayForm-input" id="UpdateBookForm-amount-input" name="amount" type="number" min="0" max="100" step="1"/>
                </form>

                <div className="OverlayForm-button" onClick={() => this.updateAmount()}>
                    send
                </div>
            </div>
        </div>
    }
}

class CreateBookForm extends React.Component {
    close() {
        document.getElementsByClassName('CreateBookForm-holder')[0].style.display = 'none'
    }

    async create() {
        let url = new URL(`${serverURL}/books/add?login=${localStorage.getItem('login')}&password=${localStorage.getItem('password')}`)

        let name = document.forms.createBookForm.elements.name.value
        let author = document.forms.createBookForm.elements.author.value
        let lang = document.forms.createBookForm.elements.lang.value
        let tags = document.forms.createBookForm.elements.tags.value.split(',')

        url.searchParams.set('name',  name)
        url.searchParams.set('author',  author)
        url.searchParams.set('lang',  lang)
        tags.forEach(tag => url.searchParams.append('tag',  tag))

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
        return <div className="OverlayForm-holder CreateBookForm-holder">
            <div className="OverlayForm">
                <header className="OverlayForm-header">
                    Create book
                </header>
                <div className="OverlayForm-close-button" onClick={() => this.close()}>
                    X
                </div>
                <form name="createBookForm">
                    <label className="OverlayForm-label" htmlFor="CreateBookForm-name-input">name: </label>
                    <input className="OverlayForm-input" id="CreateBookForm-name-input" name="name"/>
                    <label className="OverlayForm-label" htmlFor="CreateBookForm-author-input">author: </label>
                    <input className="OverlayForm-input" id="CreateBookForm-author-input" name="author"/>
                    <label className="OverlayForm-label" htmlFor="CreateBookForm-lang-input">lang: </label>
                    <input className="OverlayForm-input" id="CreateBookForm-lang-input" name="lang"/>
                    <label className="OverlayForm-label" htmlFor="CreateBookForm-tags-input">tags: </label>
                    <input className="OverlayForm-input" id="CreateBookForm-tags-input" name="tags"/>
                </form>

                <div className="OverlayForm-button" onClick={() => this.create()}>
                    create
                </div>
            </div>
        </div>
    }
}

export class AdminBooksSection extends React.Component {
    componentDidMount() {
        let books;

        getBooks().then(result => {
            books = result
            getStats().then(result => {
                let stats = result;
                books.forEach(book => book.stats = stats.find(item => item.bookID === book.id))
                console.log(books);
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
        let requestForm = document.getElementsByClassName('CreateBookForm-holder')[0]
        requestForm.style.display = 'block'
    }

    render () {
        return <div className="content-wrapper">
            <UpdateBookForm />
            <CreateBookForm />
            <div className="MainSection">
                <header className="MainSection-header">
                    Books
                    <div className="MainSection-header-button" onClick={() => this.openCreateForm()}>Create</div>
                </header>
                <table className="MainSection-table">
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
                        <td />
                    </tr>
                    {this.state.books.map(book => <BookRow book={book}/>)}
                    </tbody>
                </table>
            </div>
        </div>
    }
}