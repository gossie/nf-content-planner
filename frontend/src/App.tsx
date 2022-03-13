import { Outlet } from 'react-router-dom';

function App() {


    return (
        <div>
            <a href="https://github.com/login/oauth/authorize?client_id=ac870a1600ec03b7be10">Login</a>
            <Outlet />
        </div>
    );
}

export default App;
