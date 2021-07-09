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
        this.bookID = this.props.stats.book.id
    }

    render() {
        return <tr>
            <td className="centralized">{this.props.stats.book.id}</td>
            <td>{this.props.stats.book.name}</td>
            <td>{this.props.stats.book.author}</td>
            <td className="centralized">{this.props.stats.book.lang}</td>
            <td className="centralized">{this.props.stats.amount}</td>
            <td className="centralized">{this.props.stats.totalRequests}</td>
            <td className="centralized">{this.props.stats.rate.toFixed(2)}</td>
            <td>
                <div className="MainSection-button" onClick={() => this.openUpdateForm()}>update</div>
            </td>
        </tr>
    }
}

class UpdateBookForm extends React.Component {
    close() {
        document.getElementsByClassName('UpdateBookForm-holder')[0].style.display = 'none'
    }

    async updateAmount() {
        let url = new URL(`${serverURL}/stats`)

        let amount = document.forms.updateBookForm.elements.amount.value
        url.searchParams.set('book_id', selectedBookID)
        url.searchParams.set('amount', amount)
        await fetch(url.toString(), {
            method: 'PATCH',
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
                    <input className="OverlayForm-input" id="UpdateBookForm-amount-input" name="amount" type="number"
                           min="0" max="100" step="1"/>
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
        let url = new URL(`${serverURL}/books`)

        let name = document.forms.createBookForm.elements.name.value
        let author = document.forms.createBookForm.elements.author.value
        let lang = document.forms.createBookForm.elements.lang.value

        url.searchParams.set('name', name)
        url.searchParams.set('author', author)
        url.searchParams.set('lang', lang)

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
        getStats().then(result => {
            let stats = result;
            console.log(stats)
            this.setState({...this.state, stats})
        })
    }

    constructor(props) {
        super(props)
        this.state = {
            stats: [],
        }
    }

    openCreateForm() {
        let requestForm = document.getElementsByClassName('CreateBookForm-holder')[0]
        requestForm.style.display = 'block'
    }

    render() {
        return <div className="content-wrapper">
            <UpdateBookForm/>
            <CreateBookForm/>
            <div className="MainSection">
                <header className="MainSection-header">
                    Books
                    <div className="MainSection-header-button" onClick={() => this.openCreateForm()}>Create</div>
                </header>
                <table className="MainSection-table">
                    <colgroup>
                        <col span="1" style={{width: "5%"}}/>
                        <col span="1" style={{width: "20%"}}/>
                        <col span="1" style={{width: "15%"}}/>
                        <col span="1" style={{width: "20%"}}/>
                        <col span="1" style={{width: "5%"}}/>
                        <col span="1" style={{width: "5%"}}/>
                        <col span="1" style={{width: "5%"}}/>
                        <col span="1" style={{width: "15%"}}/>
                    </colgroup>

                    <tbody>
                    <tr>
                        <td>ID</td>
                        <td>Name</td>
                        <td>Author</td>
                        <td>Language</td>
                        <td>Amount</td>
                        <td>Readers</td>
                        <td>Rate</td>
                        <td/>
                    </tr>
                    {this.state.stats.map(stats => <BookRow stats={stats}/>)}
                    </tbody>
                </table>
            </div>
        </div>
    }
}