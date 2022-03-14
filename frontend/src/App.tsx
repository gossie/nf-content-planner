import { Outlet } from 'react-router-dom';

function App() {


    return (
        <div>
            <a href={`${process.env.REACT_APP_BASE_URL}/oauth2/authorization/github`}>Login</a>
            <Outlet />
        </div>
    );
}

export default App;
