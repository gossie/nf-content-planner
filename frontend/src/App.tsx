import { Link } from "react-router-dom";

function App() {


    return (
        <div className="flex justify-around w-full">
            <div>
                <Link to="/registration">Register</Link>
                <Link to="/login">Login</Link>
                <a href="https://github.com/login/oauth/authorize?client_id=ac870a1600ec03b7be10">GitHub</a>
            </div>
        </div>
    );
}

export default App;
