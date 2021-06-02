import React from "react";
import '../BooksPanel.css'
import './UserBooksPanel.css'
import {getBooks, getStats} from "../../api/api-for-user";

class BookRow extends React.Component {
    render() {
        return <tr>
            <td>{this.props.book.name}</td>
            <td>{this.props.book.author}</td>
            <td>{this.props.book.lang}</td>
            <td>{this.props.book.tags.join(', ')}</td>
            <td>{this.props.book.stats.amount}</td>
            <td>{this.props.book.stats.totalRequests}</td>
            <td>{this.props.book.stats.rate}</td>
            <td> <div className="UserBooksPanel-request-button">create request</div> </td>
        </tr>
    }
}

class RequestForm extends React.Component {

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

